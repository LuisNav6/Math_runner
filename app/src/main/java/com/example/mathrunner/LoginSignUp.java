package com.example.mathrunner;


import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
public class LoginSignUp extends AppCompatActivity {
    private String username;
    private MusicService musicService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sign_up);

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        startService(new Intent(this, MusicService.class));


        username = getIntent().getStringExtra("USERNAME");
        // Encuentra los botones por su ID
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);
        Button recordsButton = findViewById(R.id.recordsButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        // Establece los manejadores de clic para los botones
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de inicio de sesión (reemplaza LoginActivity.class con tu actividad real)
                Intent intent = new Intent(LoginSignUp.this, Login.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de registro (reemplaza SignUpActivity.class con tu actividad real)
                Intent intent = new Intent(LoginSignUp.this, SignUp.class);
                startActivity(intent);
            }
        });

        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de registros (reemplaza RecordsActivity.class con tu actividad real)
                Intent intent = new Intent(LoginSignUp.this, Records.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de configuraciones (reemplaza SettingsActivity.class con tu actividad real)
                Intent intent = new Intent(LoginSignUp.this, Settings.class);
                startActivity(intent);
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
            musicService.startMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}

