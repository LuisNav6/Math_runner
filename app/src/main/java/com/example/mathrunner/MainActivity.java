package com.example.mathrunner;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
        ProgressBar progressBar = findViewById(R.id.loadingProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.Darker), PorterDuff.Mode.SRC_IN);

    }
}
