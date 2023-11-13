package com.example.mathrunner;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Obtén la referencia del ImageView de la animación del cerebro
        ImageView brainImageView = findViewById(R.id.brainImageView);

        // Inicia la animación
        if (brainImageView != null && brainImageView.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable brainAnimation = (AnimationDrawable) brainImageView.getDrawable();
            brainAnimation.start();
        }
    }
}
