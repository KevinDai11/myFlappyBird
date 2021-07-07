 package com.example.myflappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class ufo extends BaseObject{
    private ArrayList<Bitmap> arrBms = new ArrayList<>();
    private float drop;
    public ufo(){
        drop = 0;
    }

    public void draw(Canvas canvas){
        drop();
        canvas.drawBitmap(this.getMp(),this.x,this.y,null);
    }
    public void drop(){
        drop+=.6;
        y+=drop;
    }
    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
        for(int x=0;x<arrBms.size();x++){
            this.arrBms.set(x,Bitmap.createScaledBitmap(this.arrBms.get(x),this.width,this.height,true));
        }
    }

    @Override
    public Bitmap getMp() {
        return this.getArrBms().get(0);
    }

    public float getDrop() {
        return drop;
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }
}
