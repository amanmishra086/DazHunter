package com.gamershubco.DazHunter.Charachters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.gamershubco.DazHunter.R;

import static com.gamershubco.DazHunter.GameView.screenRatioX;
import static com.gamershubco.DazHunter.GameView.screenratioY;

public class Bullet {

    public int x,y,width,height;
    public Bitmap bullet;

    public Bullet(Resources resources){

        bullet = BitmapFactory.decodeResource(resources, R.drawable.bullet);

        width =bullet.getWidth();
        height =bullet.getHeight();

        width /=4;
        height /=4;

//        width*=(int)screenRatioX;
//        height*=(int)screenratioY;

        bullet=Bitmap.createScaledBitmap(bullet,width,height,false);

    }

    public Rect getCollisionShape(){
        return  new Rect(x,y,x+width,y+height);
    }
}
