package com.example.mathrunner;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

public class MyAnimationDrawable extends AnimationDrawable {

    private int[] frameDurations; // Array para almacenar las velocidades de cada fotograma
    private int currentFrame;

    public MyAnimationDrawable(int[] durations, Drawable[] frames) {
        super();

        frameDurations = durations;

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

        // Selecciona la duración del fotograma actual
        int currentDuration = frameDurations[currentFrame];

        // Este método está deprecado, pero se usa para mantener la compatibilidad
        // con versiones antiguas de Android. Puedes buscar una alternativa más moderna si es necesario.
        scheduleSelf(this, System.currentTimeMillis() + currentDuration);
    }

    // Método para obtener la velocidad de un fotograma específico
    public int getFrameSpeed(int frameIndex) {
        return frameDurations[frameIndex];
    }

    // Nuevo método para actualizar las velocidades de todos los fotogramas
    public void updateFrameDurations(int[] speeds) {
        if (speeds.length == getTotalFrames()) {
            for (int i = 0; i < speeds.length; i++) {
                frameDurations[i] = speeds[i];
            }
        }
    }

    // Método para obtener la cantidad total de fotogramas
    public int getTotalFrames() {
        return getNumberOfFrames();
    }

    // Método para establecer la duración de un fotograma específico
    public void setDuration(int index, int duration) {
        if (index >= 0 && index < getNumberOfFrames()) {
            frameDurations[index] = duration;
        }
    }
}
