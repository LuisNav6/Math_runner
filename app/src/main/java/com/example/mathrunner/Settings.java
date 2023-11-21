package com.example.mathrunner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private SeekBar difficultySeekBar;
    private static final String PREFERENCES_NAME = "MyPreferences";
    private static final String DIFFICULTY_LEVEL_KEY = "DifficultyLevel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        difficultySeekBar = findViewById(R.id.difficultSeekBar);

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
}


