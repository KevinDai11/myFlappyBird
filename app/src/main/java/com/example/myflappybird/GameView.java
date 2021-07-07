package com.example.myflappybird;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameView extends View {
    private ufo ship;
    private Handler handler;
    private Runnable r;
    private ArrayList<Asteroid> arrA;
    private int sumA, disA;

    private int score, bestScore;
    private boolean start;
    private Context context;

    private int soundJump;
    private float volume;
    private boolean loadedSound;
    private SoundPool soundPool;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SharedPreferences sp = context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
        if(sp!=null){
            bestScore=sp.getInt("bestscore",0);
        }
        score = 0;
        start = false;
        this.context = context;
        createUfo();
        createAsteroid();
        handler = new Handler();
        r=new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        if(Build.VERSION.SDK_INT>=21){
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            SoundPool.Builder b = new SoundPool.Builder();
            b.setAudioAttributes(audioAttributes).setMaxStreams(5);
            soundPool = b.build();
        }else{
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedSound = true;
            }
        });
        soundJump = soundPool.load(context, R.raw.shipjump,1);
    }

    private void createAsteroid() {
        sumA=6;
        disA = 450*Constants.SCREEN_HEIGHT/1920;
        arrA = new ArrayList<>();
        for(int x=0;x<sumA;x++){
            if(x<sumA/2){
                arrA.add(new Asteroid(Constants.SCREEN_WIDTH+x*((Constants.SCREEN_WIDTH+250*Constants.SCREEN_WIDTH/1080)/(sumA/2)),0,200*Constants.SCREEN_WIDTH/1080,Constants.SCREEN_HEIGHT/2));
                arrA.get(arrA.size()-1).setMp(BitmapFactory.decodeResource(this.getResources(),R.drawable.asteroid));
                arrA.get(arrA.size()-1).randomY();
            }else{
                arrA.add(new Asteroid(arrA.get(x-sumA/2).getX(),arrA.get(x-sumA/2).getY()+arrA.get(x-sumA/2).getHeight()+disA,200*Constants.SCREEN_WIDTH/1080,Constants.SCREEN_HEIGHT/2));
                arrA.get(arrA.size()-1).setMp(BitmapFactory.decodeResource(this.getResources(),R.drawable.asteroid));
            }
        }
    }

    public void createUfo(){
        ship = new ufo();

        ship.setWidth(115*Constants.SCREEN_WIDTH/1080);
        ship.setHeight((110*Constants.SCREEN_HEIGHT/1920));
        ship.setX(100*Constants.SCREEN_WIDTH/1080);
        ship.setY(Constants.SCREEN_HEIGHT/2-ship.getHeight()/2);
        ArrayList<Bitmap> al = new ArrayList<>();
        al.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.ufo));
        ship.setArrBms(al);

    }

    public void draw(Canvas c){
        super.draw(c);

        if(start){
            ship.draw(c);

            for(int x=0;x<sumA;x++){

                if(ship.getRect().intersect(arrA.get(x).getRect())|| ship.getY()-ship.getHeight()<0 || ship.getY()>Constants.SCREEN_HEIGHT){



                    Asteroid.SPEED=0;
                    MainActivity.scoreOver.setText(MainActivity.score.getText());
                    if(score>bestScore){
                        bestScore=score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore",bestScore);
                        editor.apply();
                    }
                    MainActivity.bestScore.setText("best: "+bestScore);
                    MainActivity.score.setVisibility(INVISIBLE);
                    MainActivity.gameOverScreen.setVisibility(VISIBLE);

                }
                if(ship.getX()+ship.getWidth()>arrA.get(x).getX()+arrA.get(x).getWidth()/2 &&ship.getX()+ship.getWidth()<=arrA.get(x).getX()+arrA.get(x).getWidth()/2+Asteroid.SPEED && x<sumA/2){
                    score++;
                    MainActivity.score.setText(""+score);
                }
                if(arrA.get(x).getX()<-arrA.get(x).getWidth()){
                    arrA.get(x).setX(Constants.SCREEN_WIDTH);
                    if(x<sumA/2){
                        arrA.get(x).randomY();
                    }
                    else{
                        arrA.get(x).setY(arrA.get(x-sumA/2).getY()+arrA.get(x-sumA/2).getHeight()+disA);
                    }
                }
                arrA.get(x).draw(c);
            }
        }
        else{
            if(ship.getY()>Constants.SCREEN_HEIGHT/2){
                ship.setDrop(-15*Constants.SCREEN_HEIGHT/1920);
            }
            ship.draw(c);}

        handler.postDelayed(r,10);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            ship.setDrop(-13);
            if(loadedSound){
                int streamId = soundPool.play(soundJump,(float)0.5, (float).5,1,0,1f);
            }
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.score.setText(Integer.toString(0));
        score=0;
        createUfo();
        createAsteroid();
    }
}
