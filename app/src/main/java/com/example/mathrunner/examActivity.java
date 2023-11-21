package com.example.mathrunner;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class examActivity extends AppCompatActivity {

    private TextView operationTextView;
    private GridLayout optionsGridLayout;
    private int correctAnswer;
    private int difficultyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);

        operationTextView = findViewById(R.id.operationTextView);
        optionsGridLayout = findViewById(R.id.optionsGridLayout);

        // Obtener el nivel de dificultad del intent
        difficultyLevel = getIntent().getIntExtra("DIFFICULTY_LEVEL", 0);

        generateRandomOperation();
        generateOptions();
    }

    private void generateRandomOperation() {
        Random random = new Random();
        int operand1 = random.nextInt(10) + 1; // Número aleatorio entre 1 y 10
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
                // Verificar que no haya división por cero
                if (operand2 != 0) {
                    operatorSymbol = "/";
                    result = operand1 / operand2;
                } else {
                    // En caso de división por cero, generar una operación diferente
                    generateRandomOperation();
                    return;
                }
                break;
        }

        correctAnswer = result;

        String operation = operand1 + " " + operatorSymbol + " " + operand2 + " = ?";
        operationTextView.setText(operation);
    }

    private void generateOptions() {
        int[] options = new int[3];

        // Generate random incorrect options
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int incorrectOption;
            do {
                incorrectOption = random.nextInt(20); // Random number between 0 and 19
            } while (incorrectOption == correctAnswer || contains(options, incorrectOption));

            options[i] = incorrectOption;
        }

        // Add correct answer to a random position
        int randomPosition = random.nextInt(3);
        options[randomPosition] = correctAnswer;

        // Shuffle the array to randomize button positions
        shuffleArray(options);

        // Add buttons dynamically
        for (int i = 0; i < 3; i++) {
            Button button = new Button(this);
            button.setText(String.valueOf(options[i]));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer(Integer.parseInt(((Button) view).getText().toString()));
                }
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(i / 2);
            params.columnSpec = GridLayout.spec(i % 2);
            params.setGravity(Gravity.FILL);
            button.setLayoutParams(params);

            optionsGridLayout.addView(button);
        }
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
            // Correct answer logic
        } else {
            // Incorrect answer logic
        }

        // Generate a new random operation and update options
        generateRandomOperation();
        optionsGridLayout.removeAllViews();
        generateOptions();
    }
}
