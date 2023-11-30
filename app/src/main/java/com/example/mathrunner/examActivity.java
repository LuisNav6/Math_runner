package com.example.mathrunner;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class examActivity extends AppCompatActivity {

    private TextView operationTextView;

    private int correctAnswer;
    private int difficultyLevel;
    private Button button1, button2, button3;
    private GameInterpreter gameActivity = new GameInterpreter();
    private int lives = 3;
    private int points = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);
        Intent intent = getIntent();
        lives = intent.getIntExtra("lives", 0);
        points = intent.getIntExtra("points", 0);
        operationTextView = findViewById(R.id.operationTextView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        // Obtener el nivel de dificultad del intent
        difficultyLevel = getIntent().getIntExtra("DIFFICULTY_LEVEL", 0);

        generateRandomOperation();
        setButtonClickListeners();
    }

    private void generateRandomOperation() {
        Random random = new Random();
        int operand1 = random.nextInt(10) + 1;
        int operand2 = random.nextInt(10) + 1;

        int operator;
        String operatorSymbol = "";
        int result = 0;

        switch (difficultyLevel) {
            case 0: // Fácil
                operator = random.nextInt(2); // Suma o resta
                break;
            case 1: // Medio
                operator = random.nextInt(3); // Suma, resta o multiplicación
                break;
            case 2: // Difícil
                operator = random.nextInt(4); // Suma, resta, multiplicación o división
                break;
            default:
                operator = random.nextInt(4);
                break;
        }

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
                    generateRandomOperation();
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
                // If there are remaining lives, restart the game
                Intent game = new Intent(this, GameActivity.class);
                game.putExtra("lives", lives);
                game.putExtra("points", points);  // Pass the updated points value
                startActivity(game);
                finish();
            } else {
                // If no lives remaining, go to the end screen or perform any other logic
                // You may want to create an EndActivity or handle game over here
                // Example:
                Intent endIntent = new Intent(this, EndActivity.class);
                endIntent.putExtra("points", points);
                startActivity(endIntent);
                finish();
            }
        } else {
            --lives;
            // If the answer is incorrect, restart the game
            Intent game = new Intent(this, GameActivity.class);
            game.putExtra("lives", lives);
            game.putExtra("points", points);  // Pass the current points value
            startActivity(game);
            finish();
        }

        // Generate a new random operation and update options
        generateRandomOperation();
    }
}
