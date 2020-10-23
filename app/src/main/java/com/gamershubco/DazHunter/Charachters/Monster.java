package com.gamershubco.DazHunter.Charachters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.gamershubco.DazHunter.R;

public class Monster {

    public int x = 0, y, width, height;
    public int speed = 20;
    public boolean wasShot = true;
    public Bitmap  dragon;

    public Monster(Resources resources) {


        dragon = BitmapFactory.decodeResource(resources, R.drawable.dragon);

        width = dragon.getWidth();
        height = dragon.getHeight();

        width /= 3;
        height /= 3;

        dragon = Bitmap.createScaledBitmap(dragon, width, height, false);

        y = -height;


    }

    public Bitmap getMonster() {
        
        return dragon;

    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
