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
import android.widget.TextView;

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
    private int lives = 3;
    private TextView life;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Obtén la referencia del nuevo objeto Brain
        brain = findViewById(R.id.brain);
        book = findViewById(R.id.book);
        life= findViewById(R.id.life);
        life.setText(String.valueOf(lives));

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

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Actualizar la posición del cerebro
                brain.update();

                // Verificar la colisión con el libro
                if (brain.isCollidingWithBook(book)) {
                    handleCollision(); // Agrega aquí la lógica que desees al producirse la colisión
                }

                // Repite el bucle de juego después de un breve intervalo
                uiHandler.postDelayed(this, 16); // Aproximadamente 60 FPS (1000 ms / 60 frames)
            }
        }, 0);
    }


    private void startBookAnimation() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            private long startTime = System.currentTimeMillis();
            private long lastUpdateTime = startTime;

            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastUpdateTime;

                // Actualiza la posición del libro basándote en el tiempo transcurrido y la velocidad deseada
                int displacement = (int) (elapsedTime * Constants.BOOK_SPEED / 500); // velocidad en píxeles por segundo
                float newBookX = book.getX() - displacement;

                // Verifica si el libro se ha movido completamente fuera de la pantalla
                if (newBookX + book.getWidth() < 0) {
                    // Reinicia la posición del libro en el lado derecho de la pantalla
                    newBookX = getResources().getDisplayMetrics().widthPixels;
                }

                // Establece la nueva posición del libro
                book.setX(newBookX);


                // Ajusta la velocidad del libro con el tiempo
                float elapsedSeconds = (currentTime - startTime) / 500.0f;
                float acceleration = 0.001f; // Puedes ajustar este valor según sea necesario
                float newBookSpeed = Constants.BOOK_SPEED + acceleration * elapsedSeconds;

                // Establece un límite máximo de velocidad para el libro
                float maxBookSpeed = 900f; // Puedes ajustar este valor según sea necesario
                if (newBookSpeed > maxBookSpeed) {
                    newBookSpeed = maxBookSpeed;
                }
                Log.d("pain","Spped Book:" + newBookSpeed);
                // Guarda la nueva velocidad del libro
                Constants.BOOK_SPEED = newBookSpeed;

                // Guarda el tiempo actual para la próxima actualización
                lastUpdateTime = currentTime;

                handler.postDelayed(this, 16); // Aproximadamente 60 FPS (1000 ms / 60 frames)
            }
        });
    }

    // Método para manejar la colisión
    private void handleCollision() {
        Log.d("Collision", "Brain collided with Book!");
        lives--; // Reducir el número de vidas
        life.setText(String.valueOf(lives)); // Actualizar el texto de las vidas
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
