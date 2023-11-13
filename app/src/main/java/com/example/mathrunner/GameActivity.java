package com.example.mathrunner;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Obtén la referencia del ImageView de la animación del cerebro
        brainImageView = findViewById(R.id.brainImageView);

        // Obtén la referencia del ImageView para la nube
        cloudImageView = findViewById(R.id.cloudImageView);

        // Inicia la animación del cerebro
        if (brainImageView != null && brainImageView.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable brainAnimation = (AnimationDrawable) brainImageView.getDrawable();
            brainAnimation.start();
        }

        // Inicia la animación de desplazamiento de las nubes
        startCloudAnimation();
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
                if(currentDuration >= 1000){
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