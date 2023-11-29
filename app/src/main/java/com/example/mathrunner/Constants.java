package com.example.mathrunner;

import android.content.Context;
import android.util.DisplayMetrics;

public class Constants {
    // Game-related constants
    public static final float GRAVITY = 0.5f; // Adjust as needed
    public static final float JUMP_VELOCITY = 1000; // Adjust as needed
    public static int GROUND_LEVEL; // Adjust as needed
    public static float BOOK_SPEED = 150;
    public static final int JUMP_HEIGHT = 1000;
    public static final int JUMP_DURATION = 1000;

    // Other constants
    public static final int INITIAL_BRAIN_X = 0; // Initial X position of the brain
    public static final int INITIAL_BRAIN_Y = 0; // Initial Y position of the brain

    public static void setGroundLevel(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        GROUND_LEVEL = (int) (displayMetrics.heightPixels * 0.8); // Set ground level to 90% of the screen height
    }
}