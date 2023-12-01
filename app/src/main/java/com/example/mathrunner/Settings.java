package com.example.mathrunner;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private SeekBar difficultySeekBar;
    private MusicService musicService;
    private boolean isBound = false;
    private static final String PREFERENCES_NAME = "MyPreferences";
    private static final String DIFFICULTY_LEVEL_KEY = "DifficultyLevel";

    // Declaration of ServiceConnection
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        difficultySeekBar = findViewById(R.id.difficultSeekBar);

        // Bind to MusicService
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // Recuperar el valor del nivel de dificultad almacenado
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        int savedDifficultyLevel = preferences.getInt(DIFFICULTY_LEVEL_KEY, 0);
        difficultySeekBar.setProgress(savedDifficultyLevel);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nivel de dificultad seleccionado
                int difficultyLevel = difficultySeekBar.getProgress();

                // Guardar el nivel de dificultad en SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(DIFFICULTY_LEVEL_KEY, difficultyLevel);
                editor.apply();

                // Adjust volume based on difficulty level
                float volume = difficultyLevel / 100f; // Example: Convert 0-100 to 0.0-1.0
                if (isBound) {
                    musicService.setVolume(volume);
                }

                // Pasar el nivel de dificultad a examActivity
                Intent intent = new Intent(Settings.this, examActivity.class);
                intent.putExtra("DIFFICULTY_LEVEL", difficultyLevel);

                // Redirigir a LoginSignUp y enviar el grado de dificultad como parámetro
                Intent loginIntent = new Intent(Settings.this, LoginSignUp.class);
                startActivity(loginIntent);

                // Finalizar la actividad actual (Settings) si no se desea volver a ella
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}


