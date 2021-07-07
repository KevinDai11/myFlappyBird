package com.example.myflappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static TextView score, bestScore, scoreOver, playAgain, startButton;
    public static RelativeLayout gameOverScreen;
    private GameView gv;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH = dm.widthPixels;
        setContentView(R.layout.activity_main);
        score = findViewById(R.id.txtscore);
        bestScore = findViewById(R.id.bestScore);
        scoreOver = findViewById(R.id.gameOverScore);
        gameOverScreen = findViewById(R.id.gameOverScreen);
        playAgain = findViewById(R.id.playAgain);
        startButton = findViewById(R.id.startButton);
        gv = findViewById(R.id.gv);
        mediaPlayer = MediaPlayer.create(this, R.raw.sillychipsong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.setStart(true);
                score.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
            }
        });
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOverScreen.setVisibility(View.INVISIBLE);
                gv.setStart(false);
                gv.reset();
                startButton.performClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }
}