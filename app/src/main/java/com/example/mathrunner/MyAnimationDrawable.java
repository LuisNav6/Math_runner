package com.example.mathrunner;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class MyAnimationDrawable extends AnimationDrawable {
    private int[] frameDurations; // Array para almacenar las velocidades de cada fotograma
    private int currentFrame;
    private Handler handler;
    private int currentDuration;

    private int newSpeed = 0;

    private Handler speedHandler = new Handler();


    @Override
    public void stop() {
        super.stop();

        // Add your additional behavior here
    }
    public MyAnimationDrawable(int[] durations, Drawable[] frames) {
        super();

        frameDurations = durations;
        handler = new Handler(); // Inicializa el Handler aquí
        speedHandler = new Handler();

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], durations[i]);
        }
    }

    // Método para actualizar dinámicamente las duraciones de los fotogramas
    public void updateFrameDurations(int initialSpeed) {
        newSpeed = initialSpeed - 10;
        Log.d("Speed", "New speed: " + newSpeed);
        // Aplicar la nueva velocidad a todas las duraciones de los fotogramas
        for (int i = 0; i < frameDurations.length; i++) {
            frameDurations[i] = newSpeed;
        }
    }

    @Override
    public void run() {
        int n = getNumberOfFrames();
        currentFrame++;
        if (currentFrame >= n) {
            currentFrame = 0;
        }
        selectDrawable(currentFrame);

        // Selecciona la duración del fotograma
        currentDuration =(frameDurations[currentFrame] -50);

        // Programa la próxima actualización con el nuevo tiempo de duración y velocidad
        speedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postAtTime(MyAnimationDrawable.this, SystemClock.uptimeMillis() + currentDuration);
            }
        }, currentDuration);
    }

    // Método para obtener la velocidad de un fotograma específico
    public int getFrameSpeed(int frameIndex) {
        return frameDurations[frameIndex];
    }


    // Variable para indicar si se ha solicitado detener la animación
    private boolean stopRequested = false;

    // Método para detener la animación
    public void stopAnimation() {
        stopRequested = true;
    }

    // Método para reiniciar la animación
    public void resetAnimation() {
        stopRequested = false;
        // Reinicia la animación desde el principio
        currentFrame = 0;
        selectDrawable(currentFrame);
        // Programa la próxima actualización
        handler.postAtTime(this, SystemClock.uptimeMillis() + frameDurations[currentFrame]);
    }

    // Método para verificar si la animación ha sido detenida
    private boolean isStopRequested() {
        return stopRequested;
    }

    // Método para reiniciar la animación y establecer stopRequested en false
    public void restartAnimation() {
        stopRequested = false;
        // Reinicia la animación desde el principio
        currentFrame = 0;
        selectDrawable(currentFrame);
        // Programa la próxima actualización
        handler.postAtTime(this, SystemClock.uptimeMillis() + frameDurations[currentFrame]);
    }
}
