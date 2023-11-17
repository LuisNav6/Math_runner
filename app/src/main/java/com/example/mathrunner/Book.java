package com.example.mathrunner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class Book extends AppCompatImageView {

    public Book(Context context) {
        super(context);
        init();
    }

    public Book(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Book(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Configura la imagen (book)
        int drawableResourceId = getResources().getIdentifier("book", "drawable", getContext().getPackageName());
        Drawable bookDrawable = getResources().getDrawable(drawableResourceId);
        setImageDrawable(bookDrawable);
    }

}
