package com.gamershubco.DazHunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView startbtn,audio;
    private TextView score;
    private Boolean isMute ;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startbtn=findViewById(R.id.startbtn);
        audio=findViewById(R.id.audiobtn);
        score=findViewById(R.id.hscore);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.soundtrack);


        final SharedPreferences preferences = getSharedPreferences("game", MODE_PRIVATE);
        score.setText("High score : "+preferences.getInt("highscore",0));

        isMute = preferences.getBoolean("isMute",false);

        if (isMute){
            audio.setImageResource(R.drawable.volume_off);
            mediaPlayer.stop();

        }
        else{
            audio.setImageResource(R.drawable.volume_on);
            mediaPlayer.start();

        }

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute=!isMute;
                if (isMute){
                    audio.setImageResource(R.drawable.volume_off);
                    mediaPlayer.stop();

                }
                else{
                    audio.setImageResource(R.drawable.volume_on);
                    mediaPlayer.start();

                }
                SharedPreferences.Editor editor =preferences.edit();
                editor.putBoolean("isMute",isMute);
                editor.apply();
            }
        });

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                startActivity(new Intent(MainActivity.this,GameScreen.class));
            }
        });
    }
}