package com.example.mathrunner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicia el servicio de música
        startService(new Intent(this, MusicService.class));

        // Asegúrate de que musicService no sea nulo antes de llamar a startMusic()
        if (musicService == null) {
            musicService = new MusicService();
            musicService.startMusic();
        }else{
            Log.d("Null", "onCreate: musicService is null");
        }

        // Espera 5 segundos antes de pasar a la siguiente actividad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Detiene el servicio de música
        stopService(new Intent(this, MusicService.class));
    }
}
