package com.example.mathrunner;
import android.os.Handler;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
public class Login extends AppCompatActivity {
    private DBHelper dbHelper;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dbHelper = new DBHelper(this);
        handler = new Handler();
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button backButton = findViewById(R.id.backButton);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    boolean isValidUser = dbHelper.checkUser(username, password);

                    if (isValidUser) {
                        // Autenticación exitosa
                        // Puedes redirigir a la pantalla de inicio o realizar otras acciones
                        Intent intent = new Intent(Login.this, Logged.class);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
                        finish();
                    } else {
                        // Autenticación fallida
                        showAlertDialog("Error de Autenticación", "Credenciales incorrectas. Inténtalo de nuevo.");
                    }
                } else {
                    // Manejar el caso de campos vacíos
                    showAlertDialog("Campos Vacíos", "Por favor, completa todos los campos.");}
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, LoginSignUp.class);
                startActivity(intent);
            }
        });
    }
    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No permitir salir del cuadro de diálogo
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Cerrar automáticamente el cuadro de diálogo después de 2 segundos
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 2000); // 2000 milisegundos (2 segundos)
    }
}
