package com.example.mathrunner;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    private DBHelper dbHelper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        dbHelper = new DBHelper(this);
        handler = new Handler();

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button signUpButton = findViewById(R.id.signUpButton);
        Button backButton = findViewById(R.id.backButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    long result = dbHelper.addUser(username, password);

                    if (result != -1) {
                        // Registro exitoso
                        showAlertDialog("Registro Exitoso", "Usuario registrado correctamente.");
                        // Retraso de 2 segundos antes de redirigir
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SignUp.this, LoginSignUp.class);
                                intent.putExtra("USERNAME", username);
                                startActivity(intent);
                                finish();
                            }
                        }, 2000); // 2000 milisegundos (2 segundos)
                    } else {
                        // Registro fallido
                        showAlertDialog("Registro fallido", "Por favor inténtalo de nuevo.");
                    }
                } else {
                    // Manejar el caso de campos vacíos
                    showAlertDialog("Campos Vacíos", "Por favor, completa todos los campos.");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LoginSignUp.class);
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
