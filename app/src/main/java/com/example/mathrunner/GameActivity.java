package com.example.mathrunner;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private Brain brain;
    private Book book;
    private ImageView cloudImageView;
    private int currentCloudIndex = 1;
    private final int totalClouds = 10;
    private final int cloudAnimationDuration = 5000; // 5 seconds
    private final int bookAnimationDuration = 5000;
    private Handler uiHandler; // Agregada declaración de uiHandler
    private Handler speedHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Obtén la referencia del nuevo objeto Brain
        brain = findViewById(R.id.brain);
        book = findViewById(R.id.book);

        cloudImageView = findViewById(R.id.cloudImageView);
        uiHandler = new Handler(); // Inicializada uiHandler

        // Inicializa la animación del cerebro en la clase Brain
        brain.initializeBrainAnimation();

        // Inicia la animación de desplazamiento de las nubes
        startCloudAnimation();
        // Inicia la animación de desplazamiento del libro
        startBookAnimation();
        speedHandler = new Handler();
        speedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ajusta la velocidad utilizando el método de Brain
                brain.reduceSpeed(); // Cambiado a reduceSpeed en Brain
                // Repite después de 10 segundos
                speedHandler.postDelayed(this, 10000);
            }
        }, 0);

    }


    private void startBookAnimation() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            private int currentDuration = bookAnimationDuration;

            @Override
            public void run() {
                // Agrega la lógica para la animación del libro
                Animation bookTranslationAnimation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.cloud_translation);
                currentDuration = Math.max(currentDuration, 0);
                bookTranslationAnimation.setDuration(currentDuration);
                book.startAnimation(bookTranslationAnimation);
                currentCloudIndex = (currentCloudIndex % totalClouds) + 1;

                if (currentDuration >= 500) {
                    currentDuration -= 100;
                }

                boolean test = brain.isCollidingWithBook(book);
                Log.d("Collision", String.valueOf(test));
                // Verifica la colisión con el cerebro
                if (brain.isCollidingWithBook(book)) {
                    Log.d("Collision", "Brain collided with Book!");
                    // Realiza acciones cuando hay colisión
                    // Por ejemplo, resta una vida al cerebro
                }else{
                    Log.d("Collision", "Brain not collided with Book!");
                }

                handler.postDelayed(this, currentDuration);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Finger touched down
                // Start brain jump
                brain.jump();
                break;
            case MotionEvent.ACTION_UP:
                // Finger lifted up
                // Stop jump animation
                brain.stopJumpAnimation();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            // Tecla de espacio presionada, realiza el salto
            brain.jump();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            // Tecla de espacio liberada, detén el salto
            brain.stopJumpAnimation();
            return true;
        }
        return super.onKeyUp(keyCode, event);
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
