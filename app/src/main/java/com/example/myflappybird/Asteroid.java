package com.example.myflappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Asteroid extends BaseObject{

    public static int SPEED;

    public Asteroid(float x, float y, int width, int height){
        super(x, y, width, height);
        SPEED = 10*Constants.SCREEN_WIDTH/1080;
    }

    public void draw(Canvas c ){
        this.x-=SPEED;
        c.drawBitmap(this.mp,this.x,this.y,null);
    }

    public void randomY(){
        Random r = new Random();
        this.y = r.nextInt((0+this.height/4)+1)-this.height/4;
    }

    @Override
    public void setMp(Bitmap mp) {
        this.mp = Bitmap.createScaledBitmap(mp,width,height,true);
    }
}
