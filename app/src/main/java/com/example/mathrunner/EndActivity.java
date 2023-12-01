package com.example.mathrunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    private TextView pointsTxt, timeTxt;
    private int points, timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Intent intent = getIntent();
        timer = intent.getIntExtra("timer", 0);
        points = intent.getIntExtra("points", 0);

        pointsTxt = findViewById(R.id.scoreValueTxt);
        timeTxt = findViewById(R.id.timeValueTxt);

        pointsTxt.setText(String.valueOf(points));
        timeTxt.setText(String.valueOf(timer));

        Button backButton = findViewById(R.id.exitButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In EndActivity when you are done
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
