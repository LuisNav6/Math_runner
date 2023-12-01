package com.example.mathrunner;
import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {
    private GifImageView confettiImageView;
    private TextView pointsTxt, timeTxt, usernameTxt;
    private int points, timer;

private String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.victory);
        mediaPlayer.start();
        Intent intent = getIntent();
        timer = intent.getIntExtra("timer", 0);
        points = intent.getIntExtra("points", 0);
        username = intent.getStringExtra("USERNAME");
        pointsTxt = findViewById(R.id.scoreValueTxt);
        timeTxt = findViewById(R.id.timeValueTxt);
        usernameTxt = findViewById(R.id.usernameTextView);
        confettiImageView = findViewById(R.id.confettiImageView);
        pointsTxt.setText(String.valueOf(points));
        timeTxt.setText(String.valueOf(timer));
        usernameTxt.setText(username);
        Button backButton = findViewById(R.id.exitButton);
// Carga tu archivo GIF de confeti
        try {
            GifDrawable confettiDrawable = new GifDrawable(getResources(), R.drawable.confetti);

            // Configura el ImageView con la animación
            confettiImageView.setImageDrawable(confettiDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Muestra la animación
        confettiImageView.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In EndActivity when you are done
                DBHelper dbHelperRecords = new DBHelper(EndActivity.this);
                dbHelperRecords.addRecord(username, points, timer);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
