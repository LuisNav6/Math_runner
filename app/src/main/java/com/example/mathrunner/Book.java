package com.example.mathrunner;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

public class Book extends AppCompatImageView {
    private Rect bookRect = new Rect();
    private int width;
    private int x;
    private int height;
    private int y;


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

        // Actualiza bookRect con los nuevos límites
        bookRect.set(left, top, right, bottom);
    }

    public Rect getBookRect() {
        return bookRect;
    }

    public void updateBookRect() {

        bookRect.set(x, y, x + width, y + height);
        Log.d("BookRect", "Updated BookRect coordinates: " + bookRect.toString());
    }

    public void setX(int x) {
        this.x = x;
        updateBookRect();
    }

    public void setY(int y) {
        this.y = y;
        updateBookRect();
    }

    public void setWidth(int width) {
        this.width = width;
        updateBookRect();
    }

    public void setHeight(int height) {
        this.height = height;
        updateBookRect();
    }
}
