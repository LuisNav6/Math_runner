package com.example.mathrunner;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class examActivity extends AppCompatActivity {

    private TextView operationTextView;
    private int correctAnswer;
    private Button button1, button2, button3;
    private int lives = 3;
    private int points = 0;
    private String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);
        Intent intent = getIntent();
        lives = intent.getIntExtra("lives", 0);
        points = intent.getIntExtra("points", 0);
        username = intent.getStringExtra("USERNAME");
        operationTextView = findViewById(R.id.operationTextView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        generateRandomOperation();
        setButtonClickListeners();
    }

    private void generateRandomOperation() {
        Random random = new Random();
        int operand1 = random.nextInt(100) + 1;
        int operand2 = random.nextInt(100) + 1;

        int operator = random.nextInt(4); // Suma, resta, multiplicación o división
        String operatorSymbol = "";
        int result = 0;

        switch (operator) {
            case 0:
                operatorSymbol = "+";
                result = operand1 + operand2;
                break;
            case 1:
                operatorSymbol = "-";
                result = operand1 - operand2;
                break;
            case 2:
                operatorSymbol = "x";
                result = operand1 * operand2;
                break;
            case 3:
                if (operand2 != 0) {
                    operatorSymbol = "/";
                    result = operand1 / operand2;
                } else {
                    generateRandomOperation(); // Retry if division by zero
                    return;
                }
                break;
        }

        correctAnswer = result;

        String operation = operand1 + " " + operatorSymbol + " " + operand2 + " = ?";
        operationTextView.setText(operation);

        // Establecer valores en los botones
        int[] options = generateOptions();
        button1.setText(String.valueOf(options[0]));
        button2.setText(String.valueOf(options[1]));
        button3.setText(String.valueOf(options[2]));
    }

    private void setButtonClickListeners() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Integer.parseInt(button1.getText().toString()));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Integer.parseInt(button2.getText().toString()));
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(Integer.parseInt(button3.getText().toString()));
            }
        });
    }

    private int[] generateOptions() {
        int[] options = new int[3];

        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int incorrectOption;
            do {
                incorrectOption = random.nextInt(20);
            } while (incorrectOption == correctAnswer || contains(options, incorrectOption));

            options[i] = incorrectOption;
        }

        int randomPosition = random.nextInt(3);
        options[randomPosition] = correctAnswer;

        shuffleArray(options);
        return options;
    }

    private boolean contains(int[] array, int value) {
        for (int num : array) {
            if (num == value) {
                return true;
            }
        }
        return false;
    }

    private void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void checkAnswer(int selectedAnswer) {
        if (selectedAnswer == correctAnswer) {
            if (lives > 0) {
                points += 100;
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.points);
                mediaPlayer.start();

                // Si quedan vidas, reinicia el juego
                Intent game = new Intent(this, GameActivity.class);
                game.putExtra("lives", lives);
                game.putExtra("points", points);
                game.putExtra("USERNAME",username);// Pasa el valor actualizado de puntos
                startActivity(game);
                finish();
            } else {
                // Si no quedan vidas, ve a la pantalla de fin de juego o realiza cualquier otra lógica
                // Puedes crear una EndActivity o manejar el juego terminado aquí
                // Ejemplo:
                Intent endIntent = new Intent(this, GameActivity.class);
                endIntent.putExtra("points", points);
                endIntent.putExtra("USERNAME",username);
                startActivity(endIntent);
                finish();
            }
        } else {
            --lives;
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.lose);
            mediaPlayer.start();

            // Si la respuesta es incorrecta, reinicia el juego
            Intent game = new Intent(this, GameActivity.class);
            game.putExtra("lives", lives);
            game.putExtra("points", points);
            game.putExtra("USERNAME",username);// Pasa el valor actual de puntos
            startActivity(game);
            finish();
        }

        // Genera una nueva operación aleatoria y actualiza las opciones con un retraso de 3 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                generateRandomOperation();
            }
        }, 3000); // Retraso de 3 segundos
    }
}