package com.example.micha.musicservice.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.micha.musicservice.R;

public class MusicService extends Service {
    MediaPlayer media;
    int[] musicList;
    int currentTrack;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentTrack = 0;
        musicList = new int[]{R.raw.dream_catcher,R.raw.inspiring_storytelling,R.raw.swift,R.raw.a_promising_future,R.raw.pocket_size_moon};
        media = MediaPlayer.create(this, musicList[currentTrack]);
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
        media = MediaPlayer.create(this, musicList[currentTrack]);
    }

    public void rewind(){
        if(media == null){
            return;
        }
        currentTrack = (currentTrack - 1) % musicList.length;
        media = MediaPlayer.create(this, musicList[currentTrack]);
    }

    public void stop(){
        media.stop();
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
}
