package com.example.mathrunner;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializa el MediaPlayer aquí
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);

        // Verifica si el MediaPlayer se inicializó correctamente
        if (mediaPlayer != null) {
            // Configura el MediaPlayer para que se reproduzca en bucle
            mediaPlayer.setLooping(true);
        }
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
            mediaPlayer.pause();
        }
    }

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public void onDestroy() {
        // Detén y libera el MediaPlayer cuando se destruya el servicio
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}