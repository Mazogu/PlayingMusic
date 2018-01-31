package com.example.micha.musicservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.micha.musicservice.services.MusicService;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MusicService music;
    private ImageButton play,stop,rewind,fastforward;
    private SeekBar timestamp;
    private boolean bound;
    private Handler handler;
    private TextView time;
    private Timer timer;
    public static final String SERVICE_START = "Start";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        fastforward = findViewById(R.id.fastforward);
        rewind = findViewById(R.id.rewind);
        timestamp = findViewById(R.id.songProgress);
        time = findViewById(R.id.timestamp);
        timer = new Timer();
        handler = new Handler();
        bound = false;
        timestamp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){music.seek(i);}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                music.play();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                music.play();
            }
        });
    }

    public void handleMusic(View view) {
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        switch (view.getId()) {

            case R.id.play:
                if(!bound){
                    play.setImageResource(R.drawable.pause);
                    intent.setAction(SERVICE_START);
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                    startService(intent);
                    timer.schedule(updateTrack,0,1000);
                }
                else{
                    if(music.isPlaying()){
                        play.setImageResource(R.drawable.play);
                    }
                    else{
                        play.setImageResource(R.drawable.pause);
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

    TimerTask updateTrack = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(bound){
                        int track = music.getProgress();
                        int total = music.getDuration();
                        int totalSeconds = total/1000;
                        int currentSeconds = track/1000;
                        float percent = ((float) track)/total;
                        timestamp.setProgress(Math.round(percent*100));
                        String current = String.format("%02d:%02d",currentSeconds/60,currentSeconds%60);
                        String duration = String.format("%02d:%02d",totalSeconds/60,totalSeconds%60);
                        time.setText(current+"\\"+duration);
                    }
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        if(bound){
            music.remove();
            unbindService(connection);
            bound = false;
        }
        super.onDestroy();
    }
}
