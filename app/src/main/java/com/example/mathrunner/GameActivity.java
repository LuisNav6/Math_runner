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
    private int initialSpeed = 40; // Velocidad inicial
    private int reductionFactor = 10; // Factor de reducción
    private MyAnimationDrawable brainAnimation;
    private int[] frameDurations;
    private Handler uiHandler;
    private Handler speedHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Obtén las referencias de los ImageView
        brainImageView = findViewById(R.id.brainImageView);
        cloudImageView = findViewById(R.id.cloudImageView);
        uiHandler = new Handler();

        brainAnimation = initializeBrainAnimation();
        if (brainImageView != null) {
            brainImageView.setImageDrawable(brainAnimation);
        }

        // Inicia la animación de desplazamiento de las nubes
        startCloudAnimation();
        speedHandler = new Handler();
        speedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reduceSpeed();
                // Repite después de 10 segundos
                speedHandler.postDelayed(this, 10000);
            }
        }, 1000);

        // Inicia el Handler para imprimir el log cada 2 segundos
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                printLog();
                uiHandler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    private MyAnimationDrawable initializeBrainAnimation() {
        int totalFrames = 45;
        frameDurations = new int[totalFrames];

        Drawable[] frames = new Drawable[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            frameDurations[i] = Math.max(frameDurations[i], 1);

            int drawableResourceId = getResources().getIdentifier("brain_frame_" + (i + 1), "drawable", getPackageName());
            frames[i] = getResources().getDrawable(drawableResourceId);

            Log.d("AnimationDuration", "Frame " + i + " calculated duration: " + frameDurations[i] +
                    ", actual duration: " + (brainAnimation != null ? brainAnimation.getFrameSpeed(i) : "N/A") + "\n");
        }

        return new MyAnimationDrawable(frameDurations, frames);
    }

    private void reduceSpeed() {
        initialSpeed -= reductionFactor;
        initialSpeed = Math.max(initialSpeed, 1);
        // Reiniciar la animación después de reducir la velocidad
        brainAnimation.restartAnimation();
    }

    private void printLog() {
        StringBuilder logMessage = new StringBuilder("Current initialSpeed: " + initialSpeed);

        for (int i = 0; i < 45; i++) {
            logMessage.append(", Frame ").append(i).append(" actual duration: ").append(brainAnimation.getFrameSpeed(i)).append("\n");
        }

        Log.d("AnimationDuration", logMessage.toString());
    }

    private void startCloudAnimation() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            private int currentDuration = cloudAnimationDuration;

            @Override
            public void run() {
                cloudImageView.setImageResource(getCloudResource(currentCloudIndex));
                Animation cloudTranslationAnimation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.cloud_translation);
                currentDuration = Math.max(currentDuration, 0);
                cloudTranslationAnimation.setDuration(currentDuration);
                cloudImageView.startAnimation(cloudTranslationAnimation);
                currentCloudIndex = (currentCloudIndex % totalClouds) + 1;

                if (currentDuration >= 500) {
                    currentDuration -= 100;
                }

                handler.postDelayed(this, currentDuration);
            }
        });
    }

    private int getCloudResource(int index) {
        String cloudResourceName = "cloud_" + index;
        return getResources().getIdentifier(cloudResourceName, "drawable", getPackageName());
    }
}
