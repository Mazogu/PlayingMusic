package com.example.micha.musicservice.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.micha.musicservice.MainActivity;
import com.example.micha.musicservice.R;

import java.io.IOException;

public class MusicService extends Service {
    MediaPlayer media;
    int[] musicList;
    int currentTrack;
    private final int NOTIFICATION_ID = 1;
    public static final String SERVICE_PLAY = "Play";
    public static final String SERVICE_REWIND = "Rewind";
    public static final String SERVICE_STOP = "Stop";
    public static final String SERVICE_FASTFORWARD = "Fastforward";
    public static final String TAG = MusicService.class.getSimpleName();

    IBinder iBinder = new MusicBinder();

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        remove();
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()) {
            case MainActivity.SERVICE_START:
                startService();
                break;
            case MusicService.SERVICE_PLAY:
                play();
                break;

            case MusicService.SERVICE_STOP:
                stop();
                break;

            case MusicService.SERVICE_REWIND:
                rewind();
                break;

            case MusicService.SERVICE_FASTFORWARD:
                fastForward();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startService() {
        currentTrack = 0;
        musicList = new int[]{R.raw.dream_catcher, R.raw.inspiring_storytelling, R.raw.swift, R.raw.a_promising_future, R.raw.pocket_size_moon};
        media = MediaPlayer.create(this, musicList[currentTrack]);
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                currentTrack = (currentTrack + 1) % musicList.length;
                media = MediaPlayer.create(MusicService.this, musicList[currentTrack]);
            }
        });
        media.start();
        Intent playIntent = new Intent(this,MusicService.class);
        playIntent.setAction(MusicService.SERVICE_PLAY);

        Intent stopIntent = new Intent(this, MusicService.class);
        stopIntent.setAction(MusicService.SERVICE_STOP);

        Intent rewindIntent = new Intent(this, MusicService.class);
        rewindIntent.setAction(MusicService.SERVICE_REWIND);

        Intent fastforwardIntent = new Intent(this, MusicService.class);
        fastforwardIntent.setAction(MusicService.SERVICE_FASTFORWARD);

        PendingIntent pendingPlay = PendingIntent.getActivity(this, 0, playIntent, 0);
        PendingIntent pendingStop = PendingIntent.getActivity(this, 0, stopIntent, 0);
        PendingIntent pendingRewind = PendingIntent.getActivity(this, 0, rewindIntent, 0);
        PendingIntent pendingFastforward = PendingIntent.getActivity(this, 0, fastforwardIntent, 0);

        //Notification notification = new Notification.Builder(this).setSmallIcon(R.drawable.stop)
        //      .addAction(R.drawable.play,"Music Player",pendingIntent).build();
        //startForeground(NOTIFICATION_ID, notification);
    }

    public int getProgress() {
        if (media == null) {
            return 0;
        }
        return media.getCurrentPosition();
    }

    public void fastForward() {
        if (media == null) {
            return;
        }
        currentTrack = (currentTrack + 1) % musicList.length;
        media.stop();
        media.release();
        media = MediaPlayer.create(this, musicList[currentTrack]);
        media.start();
    }

    public void rewind() {
        if (media == null) {
            return;
        }
        currentTrack = (currentTrack - 1) % musicList.length;
        if (currentTrack == -1) {
            currentTrack = musicList.length - 1;
        }
        media.stop();
        media.release();
        media = MediaPlayer.create(this, musicList[currentTrack]);
        media.start();
    }

    public void stop() {
        media.pause();
        media.seekTo(0);
    }

    public void play() {
        if (media.isPlaying()) {
            media.pause();
        } else {
            media.start();
        }

    }

    public boolean isPlaying() {
        if (media == null) {
            return false;
        }

        return media.isPlaying();
    }

    public int getDuration() {
        if (media == null) {
            return 0;
        }
        return media.getDuration();
    }

    public class MusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    public void seek(int input) {
        if (media == null) {
            return;
        }
        float percent = input / 100f;
        float progess = percent * media.getDuration();
        media.seekTo(Math.round(progess));
    }

    public void remove() {
        if (media == null) {
            return;
        }
        media.stop();
        media.release();
        media = null;
    }
}
