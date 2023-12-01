package com.example.mathrunner;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Settings extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private long lastUpdate;
    private SeekBar difficultySeekBar, volumeSeekBar;
    private MusicService musicService;
    private boolean isBound = false;
    private static final String PREFERENCES_NAME = "MyPreferences";
    private static final String DIFFICULTY_LEVEL_KEY = "DifficultyLevel";


    // Declaration of ServiceConnection
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        // Bind to MusicService
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // Agrega esto para manejar el volumen con la SeekBar
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f; // Escala el progreso a un rango de 0.0 a 1.0
                if (musicService != null) {
                    musicService.setVolume(volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Redirigir a LoginSignUp y enviar el grado de dificultad como parámetro
                Intent loginIntent = new Intent(Settings.this, LoginSignUp.class);
                startActivity(loginIntent);

                // Finalizar la actividad actual (Settings) si no se desea volver a ella
                finish();
            }
        });

        // Inicializa el detector de agitación
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > 100) {
                long timeDiff = currentTime - lastUpdate;
                lastUpdate = currentTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDiff * 10000;

                // Ajusta el umbral según sea necesario
                if (speed > 800) {
                    // Agitar detectada, apagar/encender la música
                    if (musicService != null) {
                        if (musicService.isPlaying()) {
                            musicService.stopMusic();
                        } else {
                            musicService.startMusic();
                        }
                    }
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}


