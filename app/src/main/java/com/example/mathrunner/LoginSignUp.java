package com.example.mathrunner;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
public class LoginSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sign_up);

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
}

