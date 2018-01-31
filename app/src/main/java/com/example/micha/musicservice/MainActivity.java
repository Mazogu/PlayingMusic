package com.example.micha.musicservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.micha.musicservice.services.MusicService;

public class MainActivity extends AppCompatActivity {

    private MusicService music;
    private ImageButton play,stop,rewind,fastforward;
    private SeekBar timestamp;
    private boolean bound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        fastforward = findViewById(R.id.fastforward);
        rewind = findViewById(R.id.rewind);
        timestamp = findViewById(R.id.songProgress);
        bound = false;
    }

    public void handleMusic(View view) {
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        switch (view.getId()) {

            case R.id.play:
                if(!bound){
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                    startService(intent);
                }
                else{
                    if(music.isPlaying()){
                        play.setImageResource(R.drawable.pause);
                    }
                    else{
                        play.setImageResource(R.drawable.play);
                    }
                    music.play();
                }
                break;

            case R.id.stop:
                if(bound){
                    music.stop();
                    play.setImageResource(R.drawable.play);
                }
                break;

            case R.id.fastforward:
                if(bound){music.fastForward();}
                break;

            case R.id.rewind:
                if(bound){music.rewind();}
                break;
        }
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            music = binder.getMusicService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };
}
