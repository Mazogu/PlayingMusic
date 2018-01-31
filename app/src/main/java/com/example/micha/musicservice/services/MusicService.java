package com.example.micha.musicservice.services;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.micha.musicservice.R;

import java.io.IOException;

public class MusicService extends Service {
    MediaPlayer media;
    int[] musicList;
    int currentTrack;
    IBinder iBinder = new MusicBinder();
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentTrack = 0;
        musicList = new int[]{R.raw.dream_catcher,R.raw.inspiring_storytelling,R.raw.swift,R.raw.a_promising_future,R.raw.pocket_size_moon};
        media = MediaPlayer.create(this, musicList[currentTrack]);
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                currentTrack = (currentTrack+1)%musicList.length;
                media = MediaPlayer.create(MusicService.this, musicList[currentTrack]);
            }
        });
        media.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public int getProgress(){
        if(media == null){
            return 0;
        }
        return media.getCurrentPosition();
    }

    public void fastForward(){
        if(media == null){
            return;
        }
        currentTrack = (currentTrack + 1) % musicList.length;
        media.stop();
        media.release();
        media = MediaPlayer.create(this, musicList[currentTrack]);
        media.start();
    }

    public void rewind(){
        if(media == null){
            return;
        }
        currentTrack = (currentTrack - 1) % musicList.length;
        if(currentTrack == -1){
            currentTrack = musicList.length - 1;
        }
        media.stop();
        media.release();
        media = MediaPlayer.create(this, musicList[currentTrack]);
        media.start();
    }

    public void stop(){
        media.pause();
        media.seekTo(0);
    }

    public void play(){
        if(media.isPlaying()){
            media.pause();
        }
        else{
            media.start();
        }

    }

    public boolean isPlaying(){
        if (media==null){
            return false;
        }

        return media.isPlaying();
    }

    public int getDuration(){
        if(media == null){
            return 0;
        }
        return media.getDuration();
    }

    public class MusicBinder extends Binder{
        public MusicService getMusicService(){
            return MusicService.this;
        }
    }

    public void seek(int input){
        if(media == null){
            return;
        }
        float percent = input/100f;
        float progess = percent*media.getDuration();
        media.seekTo(Math.round(progess));
    }
}
