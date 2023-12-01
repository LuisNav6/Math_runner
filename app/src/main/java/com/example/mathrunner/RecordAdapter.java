package com.example.mathrunner;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RecordAdapter extends CursorAdapter {

    public RecordAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflar la vista de cada elemento individual (record_item)
        return LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        try {
            TextView usernameTextView = view.findViewById(R.id.usernameTextView);
            TextView pointsTextView = view.findViewById(R.id.pointsTextView);
            TextView timeTextView = view.findViewById(R.id.timeTextView);

            // Utiliza "id" como el nombre de la columna en lugar de "_id"
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            int points = cursor.getInt(cursor.getColumnIndexOrThrow("points"));
            int time = cursor.getInt(cursor.getColumnIndexOrThrow("time"));

            usernameTextView.setText(username);
            pointsTextView.setText(String.valueOf(points));
            timeTextView.setText(String.valueOf(time));
        } catch (Exception e) {
            Log.e("MyApp", "Error in bindView: " + e.getMessage());
        }
    }
}
