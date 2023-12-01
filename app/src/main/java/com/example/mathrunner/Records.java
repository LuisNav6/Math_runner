package com.example.mathrunner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Records extends AppCompatActivity {
    private ListView recordsListView;
    private DBHelper dbHelperRecords;
private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);
        username = getIntent().getStringExtra("USERNAME");
        recordsListView = findViewById(R.id.recordsListView);
        dbHelperRecords = new DBHelper(this);
        displayAllRecords();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username != null){
                    Log.d("user rec",username);
                    Intent intent = new Intent(Records.this, Logged.class);
                    intent.putExtra("USERNAME", username);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Records.this, LoginSignUp.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void displayAllRecords() {
        //solo se usa la siguiente línea para probar la bd
        //dbHelperRecords.addRecord("TestUser", 100, 30);
        //solo se usa para limpiar los registros
        //dbHelperRecords.deleteAllRecords();
        Cursor cursor = dbHelperRecords.getAllRecordsOrdered();
        RecordAdapter adapter = new RecordAdapter(this, cursor);
        recordsListView.setAdapter(adapter);
    }


}
