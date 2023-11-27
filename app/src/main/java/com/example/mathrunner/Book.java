package com.example.mathrunner;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class Book extends AppCompatImageView {
    private Rect bookRect = new Rect();

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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // Update the bookRect with the new bounds
        bookRect.set(left, top, right, bottom);
    }

    public Rect getBookRect() {
        return bookRect;
    }
}