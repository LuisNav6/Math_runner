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

    private Handler speedHandler = new Handler();


    public MyAnimationDrawable(int[] durations, Drawable[] frames) {
        super();

        frameDurations = durations;
        handler = new Handler(); // Inicializa el Handler aquí
        speedHandler = new Handler();

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], durations[i]);
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

        // Selecciona la duración del fotograma actual con el factor de velocidad aplicado
        int currentDuration = (int) (frameDurations[currentFrame] + 50);

        // Imprime el valor de currentFrame en el registro de log
        Log.d("MyAnimationDrawable", "Current Frame: " + currentDuration);

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
