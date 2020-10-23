package com.gamershubco.DazHunter.Charachters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.gamershubco.DazHunter.GameView;
import com.gamershubco.DazHunter.R;

import static com.gamershubco.DazHunter.GameView.screenRatioX;

public class Hunter {

    public int x,y,width,height;
    public boolean isGoingUp =false;
    Bitmap hunter,dead;
//    private  GameView gameView;

    public Hunter(int screenY, Resources resources) {

        hunter= BitmapFactory.decodeResource(resources, R.drawable.huntermodel);

        width=hunter.getWidth();
        height=hunter.getHeight();

        width/=4;
        height/=4;
//
//        width*=(int)screenRatioX;
//        height*=(int)screenratioY;

        hunter=Bitmap.createScaledBitmap(hunter,width,height,false);

        dead= BitmapFactory.decodeResource(resources, R.drawable.dead);
        dead=Bitmap.createScaledBitmap(dead,width,height,false);

        y=screenY /2;
        x=(int) (5*screenRatioX);

    }

    public Bitmap getHunter(){
        return hunter;
    }
     public Bitmap getDead(){
        return dead;
    }

    public Rect getCollisionShape(){
        return  new Rect(x,y,x+width,y+height);
    }


}
