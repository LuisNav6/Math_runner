package com.example.mathrunner;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private ImageView brainImageView;
    private ImageView cloudImageView;
    private int currentCloudIndex = 1;
    private final int totalClouds = 10;
    private final int cloudAnimationDuration = 5000; // 5 seconds
    private int initialSpeed = 50; // Velocidad inicial
    private int reductionFactor = 5; // Factor de reducción
    private Handler speedHandler;
    private Handler logHandler;
    MyAnimationDrawable brainAnimation;
    private int[] frameDurations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Obtén la referencia del ImageView de la animación del cerebro
        brainImageView = findViewById(R.id.brainImageView);

        // Obtén la referencia del ImageView para la nube
        cloudImageView = findViewById(R.id.cloudImageView);

        brainAnimation = initializeBrainAnimation();
        // Asegúrate de declarar brainAnimation fuera del bloque if

        if (brainImageView != null) {
            int totalFrames = 45;
            frameDurations = new int[totalFrames];
            Drawable[] frames = new Drawable[totalFrames];

            for (int i = 0; i < totalFrames; i++) {

                // Asegúrate de que la velocidad no sea negativa
                frameDurations[i] = Math.max(frameDurations[i], 1);

                int drawableResourceId = getResources().getIdentifier("brain_frame_" + (i + 1), "drawable", getPackageName());
                frames[i] = getResources().getDrawable(drawableResourceId);

                // Imprime la duración calculada y actual para cada frame
                Log.d("AnimationDuration", "Frame " + i + " calculated duration: " + frameDurations[i] +
                        ", actual duration: " + (brainAnimation != null ? brainAnimation.getFrameSpeed(i) : "N/A") + "\n");
            }

            brainAnimation = new MyAnimationDrawable(frameDurations, frames);
            brainImageView.setImageDrawable(brainAnimation);
        }

        // Inicia la animación de desplazamiento de las nubes
        startCloudAnimation();

        // Inicia el Handler para reducir la velocidad cada 2 segundos
        speedHandler = new Handler();
        speedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Reducir la velocidad cada 2 segundos
                reduceSpeed();
                speedHandler.postDelayed(this, initialSpeed); // Repetir después de 5 segundos
            }
        }, 500); // Comienza después de 0.5 segundos

        // Inicia el Handler para imprimir el log cada 2 segundos
        logHandler = new Handler();
        logHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Imprimir el log cada 2 segundos
                StringBuilder logMessage = new StringBuilder("Current initialSpeed: " + initialSpeed);

                for (int i = 0; i < 45; i++) {
                    logMessage.append(", Frame ").append(i).append(" actual duration: ").append(brainAnimation.getFrameSpeed(i) + "\n");
                }

                Log.d("AnimationDuration", logMessage.toString());
                logHandler.postDelayed(this, 2000); // Repetir después de 2 segundos
            }
        }, 2000); // Comienza después de 2 segundos
    }

    // Método para inicializar la animación del cerebro
    private MyAnimationDrawable initializeBrainAnimation() {
        int totalFrames = 45;
        frameDurations = new int[totalFrames]; // Utiliza la variable de instancia

        Drawable[] frames = new Drawable[totalFrames];

        for (int i = 0; i < totalFrames; i++) {

            // Asegúrate de que la velocidad no sea negativa
            frameDurations[i] = Math.max(frameDurations[i], 1);

            int drawableResourceId = getResources().getIdentifier("brain_frame_" + (i + 1), "drawable", getPackageName());
            frames[i] = getResources().getDrawable(drawableResourceId);

            // Imprime la duración calculada y actual para cada frame
            Log.d("AnimationDuration", "Frame " + i + " calculated duration: " + frameDurations[i] +
                    ", actual duration: " + (brainAnimation != null ? brainAnimation.getFrameSpeed(i) : "N/A") + "\n");
        }

        return new MyAnimationDrawable(frameDurations, frames);
    }


    // Método para actualizar las duraciones de los frames
    public void updateFrameDurations(int[] durations) {
        for (int i = 0; i < durations.length; i++) {
            frameDurations[i] = durations[i];
        }
    }

    private void reduceSpeed() {
        initialSpeed -= reductionFactor;
        initialSpeed = Math.max(initialSpeed, 1);

        int totalFrames = 45;
        int[] newFrameDurations = new int[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            // Ajusta la velocidad en función del tiempo transcurrido
            newFrameDurations[i] = initialSpeed - reductionFactor;

            // Asegúrate de que la velocidad no sea negativa
            newFrameDurations[i] = Math.max(newFrameDurations[i], 1);
        }

        // Actualiza las duraciones de los frames en la animación del cerebro
        if (brainAnimation != null) {
            brainAnimation.updateFrameDurations(newFrameDurations);

            // Reinicia la animación del cerebro
            brainAnimation.stop();
            brainAnimation.start();
        }
    }



    private void startCloudAnimation() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            private int currentDuration = cloudAnimationDuration; // Duración inicial

            @Override
            public void run() {
                // Actualiza el recurso de la nube en el ImageView
                cloudImageView.setImageResource(getCloudResource(currentCloudIndex));

                // Carga la animación de traslación desde el recurso
                Animation cloudTranslationAnimation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.cloud_translation);

                // Ajusta la duración de la animación, asegurándote de que no sea negativa
                currentDuration = Math.max(currentDuration, 0);
                cloudTranslationAnimation.setDuration(currentDuration);

                // Aplica la animación al ImageView
                cloudImageView.startAnimation(cloudTranslationAnimation);

                // Incrementa el índice actual y vuelve a 1 si supera el número total de nubes
                currentCloudIndex = (currentCloudIndex % totalClouds) + 1;

                // Incrementa la velocidad de la animación
                if (currentDuration >= 500) {
                    currentDuration -= 100;
                }

                // Programa el siguiente cambio después del tiempo de duración de la animación
                handler.postDelayed(this, currentDuration);
            }
        });
    }

    private int getCloudResource(int index) {
        // Construye el nombre del recurso de nube basado en el índice
        String cloudResourceName = "cloud_" + index;
        // Obtiene el identificador del recurso
        return getResources().getIdentifier(cloudResourceName, "drawable", getPackageName());
    }
}
