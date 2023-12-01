package com.example.mathrunner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
    private int points = 0;
    private TextView life, pointsTxt, timeTxt;
    private boolean isGameActive = true;
    private  ImageView Imageviewlife;
    private boolean isGameStopped = false;
    private static final String TIMER_PREFS = "TimerPrefs";
    private static final String TIMER_KEY = "TimerKey";
    private SharedPreferences timerPrefs;
    private int timer;
    private static final int END_ACTIVITY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        timerPrefs = getSharedPreferences(TIMER_PREFS, MODE_PRIVATE);
        timer = timerPrefs.getInt(TIMER_KEY, 0);
        Intent intent = getIntent();
        lives = intent.getIntExtra("lives", 3);
        points = intent.getIntExtra("points", 0);
        Log.d("Lives", "onCreate: lives: " + lives);
        Log.d("Points", "points:  " + points);
        Constants.setGroundLevel(this);
        // Obtén la referencia del nuevo objeto Brain
        brain = findViewById(R.id.brain);
        book = findViewById(R.id.book);
        pointsTxt = findViewById(R.id.points);
        timeTxt = findViewById(R.id.timer);
        life= findViewById(R.id.life);
        pointsTxt.setText(String.valueOf(points));
        life.setText(String.valueOf(lives));
        Imageviewlife = findViewById(R.id.heart);

        cloudImageView = findViewById(R.id.cloudImageView);
        uiHandler = new Handler(); // Inicializada uiHandler

        // Inicializa la animación del cerebro en la clase Brain
        brain.initializeBrainAnimation();

        // Inicia la animación de desplazamiento de las nubes
        startCloudAnimation();
        // Inicia la animación de desplazamiento del libro
        startBookAnimation();

        switch (lives) {
            case 3:
                Imageviewlife.setImageResource(R.drawable.single_heart_complete);
                break;
            case 2:
                Imageviewlife.setImageResource(R.drawable.single_heart_one);
                break;
            case 1:
                Imageviewlife.setImageResource(R.drawable.single_heart_two);
                break;
            default:
                Imageviewlife.setImageResource(R.drawable.single_heart_tree);
                break;
        }

        if(lives == 0){
            exitGame();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Guardar el valor del temporizador en SharedPreferences
        SharedPreferences.Editor editor = timerPrefs.edit();
        editor.putInt(TIMER_KEY, timer);
        editor.apply();
    }

    private Handler bookAnimationHandler = new Handler();

    private void startBookAnimation() {
        bookAnimationHandler.post(new Runnable() {
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
                // Guarda la nueva velocidad del libro
                Constants.BOOK_SPEED = newBookSpeed;

                // Guarda el tiempo actual para la próxima actualización
                lastUpdateTime = currentTime;

                bookAnimationHandler.postDelayed(this, 16); // Aproximadamente 60 FPS (1000 ms / 60 frames)
            }
        });
    }

    public void stopBookAnimation() {
        bookAnimationHandler.removeCallbacksAndMessages(null);
    }


    private boolean isCollisionHandled = false;

    private void handleCollision() {
        if (!isCollisionHandled) {
            Log.d("Collision", "Brain collided with Book!");
            
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.hit);
            mediaPlayer.start();
            // Stop all animations
            brain.stopAnimation();
            stopBookAnimation();
            stopCloudAnimation();

            isCollisionHandled = true;
            isGameStopped = true;

            // Detener el temporizador
            uiHandler.removeCallbacksAndMessages(null);
            // Guardar el valor del temporizador en SharedPreferences
            SharedPreferences.Editor editor = timerPrefs.edit();
            editor.putInt(TIMER_KEY, timer);
            editor.apply();

            Intent intent = new Intent(this, examActivity.class);
            intent.putExtra("lives", lives);
            intent.putExtra("points", points);
            // Start the new Activity
            startActivity(intent);
            // Finish GameActivity
            finish();
        }
    }

    // Método para salir del juego
    private void exitGame() {
        lives = 3; // Reset lives
        life.setText(String.valueOf(lives));
        timeTxt.setText(String.valueOf(timer));
        pointsTxt.setText(String.valueOf(points));

        Log.d("GameActivity", "Exiting game. Timer before reset: " + timer);

        int timerSend = timer;
        timer = 0;
        timerPrefs.edit().putInt(TIMER_KEY, timer).apply();
        Log.d("GameActivity", "Timer after reset: " + timer);

        // Update the TIMER_KEY in timerPrefs with the reset value
        SharedPreferences.Editor editor = timerPrefs.edit();
        editor.putInt(TIMER_KEY, timer);
        editor.apply();

        // Start EndActivity with startActivityForResult
        Intent endIntent = new Intent(this, EndActivity.class);
        endIntent.putExtra("timer", timerSend);
        endIntent.putExtra("points", points);
        startActivityForResult(endIntent, END_ACTIVITY_REQUEST_CODE);
        points = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == END_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle any logic you need after returning from EndActivity
            // For example, you can finish this activity
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isGameStopped) {
            resumeTimer();
        }
    }

    private void resumeTimer() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer++;
                // Actualizar el texto del temporizador
                timeTxt.setText(String.valueOf(timer));

                // Actualizar la posición del cerebro
                brain.update();
                if (brain.isCollidingWithBook(book)) {
                    handleCollision();
                    isGameStopped = true;
                }

                // Guardar el valor del temporizador en SharedPreferences
                SharedPreferences.Editor editor = timerPrefs.edit();
                editor.putInt(TIMER_KEY, timer);
                editor.apply();

                // Repite el bucle de juego después de un segundo
                if (!isGameStopped) {
                    uiHandler.postDelayed(this, 16);
                }
            }
        }, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isGameStopped) {
            pauseTimer();
        }
    }

    private void pauseTimer() {
        uiHandler.removeCallbacksAndMessages(null);
    }


    private void resetTimer() {
        timer = 0;
        timeTxt.setText(String.valueOf(timer));
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

    private Handler cloudAnimationHandler = new Handler();

    private void startCloudAnimation() {
        cloudAnimationHandler.post(new Runnable() {
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

                cloudAnimationHandler.postDelayed(this, currentDuration);
            }
        });
    }

    public void stopCloudAnimation() {
        cloudAnimationHandler.removeCallbacksAndMessages(null);
        cloudImageView.clearAnimation();
    }

    private int getCloudResource(int index) {
        String cloudResourceName = "cloud_" + index;
        return getResources().getIdentifier(cloudResourceName, "drawable", getPackageName());
    }
}
