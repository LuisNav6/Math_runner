package com.example.mathrunner;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;

public class Logged extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);
        String username = getIntent().getStringExtra("USERNAME");
        // Encuentra los botones por su ID
        Button playButton = findViewById(R.id.playButton);
        Button exitButton = findViewById(R.id.exitButton);
        Button recordsButton = findViewById(R.id.recordsButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(username);
        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de registros (reemplaza RecordsActivity.class con tu actividad real)
                Intent intent = new Intent(Logged.this, Records.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de registros (reemplaza RecordsActivity.class con tu actividad real)
                Intent intent = new Intent(Logged.this, GameActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Logged.this, LoginSignUp.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de configuraciones (reemplaza SettingsActivity.class con tu actividad real)
                Intent intent = new Intent(Logged.this, Settings.class);
                startActivity(intent);
            }
        });
    }
}

