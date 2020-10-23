package com.gamershubco.DazHunter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.gamershubco.DazHunter.Charachters.Bullet;
import com.gamershubco.DazHunter.Charachters.Hunter;
import com.gamershubco.DazHunter.Charachters.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private Boolean isPLaying = true;
    private int screenX, screenY,score=0;
    public static float screenRatioX, screenratioY;
    private Background background1, background2;
    private Monster[] monsters;
    private List<Bullet> bullets;
    private Hunter hunter;
    private SharedPreferences preferences;
    private Paint paint;
    private int sound, deadsound, backgroundmusic;
    private GameScreen activity;
    private SoundPool soundPool;
    private Random random;
    private boolean isGameOver =false;

    public GameView(GameScreen activity, int screenX, int screenY) {
        super(activity);

        this.activity=activity;
        this.screenY = screenY;
        this.screenX = screenX;
        screenratioY = 1080f / screenY;
        screenRatioX = 22246f / screenX;

        preferences =activity.getSharedPreferences("game",Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }

        sound = soundPool.load(activity,R.raw.shoot,1);
        deadsound = soundPool.load(activity,R.raw.astronomia,1);



        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        hunter = new Hunter(screenY, getResources());
        bullets = new ArrayList<>();

        background2.x = screenX;
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        monsters =new Monster[4];
        random =new Random();
        for (int i =0;i<4 ;i++){
            Monster monster =new Monster(getResources());
            monsters[i]=monster;
        }
    }

    @Override
    public void run() {
        while (isPLaying) {
            update();
            draw();
            sleep();
        }

    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            for (Monster monster : monsters){
                canvas.drawBitmap(monster.getMonster(),monster.x,monster.y,paint);
            }
            canvas.drawText(+score +"",screenX/2f,154,paint);
            if (isGameOver){
                isPLaying =false;
                canvas.drawBitmap(hunter.getDead(),hunter.x,hunter.y,paint);
                getHolder().unlockCanvasAndPost(canvas);
                if (!preferences.getBoolean("isMute",false)){
                    soundPool.play(deadsound,1,1,0,0,1);
                }
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(hunter.getHunter(), hunter.x, hunter.y, paint);
            for (Bullet bullet : bullets){
                canvas.drawBitmap(bullet.bullet,bullet.x,bullet.y,paint);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }




    private void update() {

        background1.x -= 3 * screenRatioX;
        background2.x -= 3 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }
        if (hunter.isGoingUp){
            hunter.y -=30 *screenratioY;
        }
        else {
            hunter.y +=30 *screenratioY;
        }
        if (hunter.y<0){
            hunter.y=0;
        }
        if (hunter.y> screenY - hunter.height){
            hunter.y = screenY -hunter.height;
        }

        List<Bullet> trash =new ArrayList<>();
        for (Bullet bullet : bullets){
            if (bullet.x > screenX){
                trash.add(bullet);
            }

            bullet.x += 10 * screenRatioX;

            for (Monster monster : monsters){
                if (Rect.intersects(monster.getCollisionShape(),bullet.getCollisionShape())){
                    score++;
                    monster.x = -500;
                    bullet.x = screenX+500;
                    monster.wasShot =true;
                }
            }
        }

        for (Bullet bullet : trash){
            bullets.remove(bullet);
        }

        for (Monster monster : monsters){
            monster.x -= monster.speed;

            if (monster.x +monster.width <0){

                if (!monster.wasShot){
                    isGameOver =true;
                    return;
                }
                int bound = (int) (30);
                monster.speed =random.nextInt(bound);

                if (monster.speed <10) {
                    monster.speed = 10;
                }
                //position of monster
                monster.x =screenX;
                monster.y =random.nextInt(screenY - monster.height);
                monster.wasShot =false;
            }

            if (Rect.intersects(monster.getCollisionShape(),hunter.getCollisionShape())){
                isGameOver =true;
            }
        }

    }

    public void resunme() {
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPLaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX/2){
                    hunter.isGoingUp =true;
                }

                break;
            case MotionEvent.ACTION_UP:
                hunter.isGoingUp=false;

                if (event.getX() > screenX /2){
                    newBullet();
                }
                break;
        }

        return true;

    }

    public void newBullet(){

        if (!preferences.getBoolean("isMute",false) && isPLaying){
            soundPool.play(sound,1,1,0,0,1);
        }
        Bullet bullet =new Bullet(getResources());
        bullet.x  = hunter.x + hunter.width;
        bullet.y  = hunter.y + (hunter.height/3);
        bullets.add(bullet);
    }


    private void saveIfHighScore() {

        if (preferences.getInt("highscore",0) < score){
            SharedPreferences.Editor editor =preferences.edit();
            editor.putInt("highscore",score);
            editor.apply();
        }
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(4000);
            activity.startActivity(new Intent(activity,MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
