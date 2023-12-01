package com.example.mathrunner;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    private float volume = 1.0f;
    private SharedPreferences preferences;
    private boolean isMuted = false;

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void setLooping(boolean looping) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(looping);
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        preferences = getSharedPreferences("MusicService", MODE_PRIVATE);
        volume = preferences.getFloat("volume", 1.0f);
        isMuted = preferences.getBoolean("isMuted", false);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    public void muteMusic() {
        if (mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.setVolume(volume, volume);
                isMuted = false;
            } else {
                mediaPlayer.setVolume(0, 0);
                isMuted = true;
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isMuted", isMuted);
            editor.apply();  // Aplica los cambios al editor antes de llamar a apply()
        }
    }

    public boolean isMuted() { 
        return isMuted;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
            this.volume = volume;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("volume", volume);
            editor.apply();
        }
    }

    public float getVolume() { // Add this method
        return volume;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer.stop();
        super.onDestroy();
    }
}