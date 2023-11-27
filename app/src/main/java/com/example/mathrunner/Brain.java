package com.example.mathrunner;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatImageView;

public class Brain extends AppCompatImageView {

    private MyAnimationDrawable brainAnimation;
    private int x, y; // Posición del cerebro en la pantalla
    private int velocityY; // Velocidad vertical del cerebro
    private boolean isJumping; // Indica si el cerebro está saltando
    private ValueAnimator jumpAnimator;

    private Rect brainRect = new Rect();
    private Rect bookRect = new Rect();

    public Brain(Context context) {
        super(context);
        init();
    }

    public Brain(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Brain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Inicializar otras propiedades del cerebro
        x = 0; // Establecer la posición inicial en la pantalla
        y = 0;
        velocityY = 0;
        isJumping = false;

        initializeBrainAnimation(); // Inicializar la animación del cerebro (brain)
    }

    public void initializeBrainAnimation() {
        int totalFrames = 45;
        int[] frameDurations = new int[totalFrames];
        Drawable[] frames = new Drawable[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            frameDurations[i] = Math.max(frameDurations[i], 1);
            int drawableResourceId = getResources().getIdentifier("brain_frame_" + (i + 1), "drawable", getContext().getPackageName());
            frames[i] = getResources().getDrawable(drawableResourceId);
        }

        brainAnimation = new MyAnimationDrawable(frameDurations, frames);
        setImageDrawable(brainAnimation);

        // Iniciar la animación
        startAnimation();
    }

    public boolean isCollidingWithBook(Book book) {
        // Obtén los límites (Rect) de las vistas
        this.getHitRect(brainRect);
        book.getHitRect(bookRect);

        // Si el cerebro está saltando, actualiza el top del brainRect
        if (isJumping) {
            // Calcular la nueva posición en Y durante el salto
            Log.d("pain","Y ->" + y);
            int newY = y + velocityY;

            // Establecer el nuevo top y bottom del brainRect
            brainRect.top = newY;
            brainRect.bottom = newY + getHeight(); // Ajusta getHeight() según tu lógica
            Log.d("pain","Brain Rect JUMP ->" + brainRect);
            Log.d("pain","Book Rect JUMP ->" + bookRect);
            return Rect.intersects(brainRect,bookRect);
        }else{
            Log.d("pain","Brain Rect ->" + brainRect);
            Log.d("pain","Book Rect ->" + bookRect);
            // Verifica si los Rects se superponen
            return Rect.intersects(brainRect, bookRect);
        }
    }


    public void update() {
        // Actualizar la posición del cerebro
        y += velocityY;

        // Simular la gravedad (ajustar según sea necesario)
        velocityY += Constants.GRAVITY;

        // Verificar si el cerebro ha alcanzado el suelo
        if (y > Constants.GROUND_LEVEL) {
            y = Constants.GROUND_LEVEL;
            velocityY = 0;
            isJumping = false;
        }
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;

            // Configura el ValueAnimator para animar la posición en Y del cerebro
            jumpAnimator = ValueAnimator.ofInt(y, y - Constants.JUMP_HEIGHT);
            jumpAnimator.setDuration(Constants.JUMP_DURATION);
            jumpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // Actualiza la posición en Y del cerebro durante la animación
                    int animatedValue = (int) animation.getAnimatedValue();
                    setBrainY(animatedValue);
                    invalidate(); // Invalida la vista para forzar el redibujo durante la animación
                }
            });

            jumpAnimator.start();
        }
    }

    public void stopJumpAnimation() {
        if (isJumping && jumpAnimator != null) {
            jumpAnimator.cancel();
            isJumping = false;
            jumpAnimator = null;
        }
    }


    public void crouch() {
        // Agrega lógica para manejar el agacharse
        // Puedes cambiar la posición, la animación, etc., según tus necesidades
        // Por ejemplo, si tienes una animación específica para cuando se agacha, puedes cambiar la animación aquí

        // Restablecer la posición en Y para simular el agacharse
        y = Constants.GROUND_LEVEL;
    }

    // Getter para la posición en X
    public int getBrainX() {
        return x;
    }

    // Setter para la posición en X
    public void setBrainX(int x) {
        this.x = x;
    }

    // Getter para la posición en Y
    public int getBrainY() {
        return y;
    }

    // Setter para la posición en Y
    public void setBrainY(int y) {
        this.y = y;
    }

    // Método para reducir la velocidad
    public void reduceSpeed() {
        brainAnimation.restartAnimation();
    }

    // Método para iniciar la animación
    private void startAnimation() {
        brainAnimation.start();
    }
}
