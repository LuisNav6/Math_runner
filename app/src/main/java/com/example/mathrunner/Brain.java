package com.example.mathrunner;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.widget.AppCompatImageView;

public class Brain extends AppCompatImageView {

    private MyAnimationDrawable brainAnimation;
    private int x, y; // Posición del cerebro en la pantalla
    private int velocityY; // Velocidad vertical del cerebro
    private boolean isJumping; // Indica si el cerebro está saltando

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
        Rect brainRect = new Rect();
        Rect bookRect = new Rect();

        // Obtén los límites (Rect) de las vistas
        this.getHitRect(brainRect);
        book.getHitRect(bookRect);

        // Verifica si los Rects se superponen
        return Rect.intersects(brainRect, bookRect);
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
        velocityY = Constants.JUMP_VELOCITY;
        isJumping = true;

        // Inicia la animación de salto
        startJumpAnimation();
    }

    public void stopJumpAnimation() {
        if (isJumping) {
            isJumping = false;
        }
    }

    private void startJumpAnimation() {
        // Cargar la animación de salto desde el recurso XML
        Animation jumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.brain_jump);
        Animation foward = AnimationUtils.loadAnimation(getContext(), R.anim.foward);
        // Configurar la posición final después de la animación
        jumpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Puedes realizar acciones al inicio de la animación si es necesario
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Puedes realizar acciones en cada repetición de la animación si es necesario
            }
        });

        // Iniciar la animación solo si no está saltando actualmente
        if (isJumping) {
            startAnimation(jumpAnimation);
        }
    }



    public void crouch() {
        // Agrega lógica para manejar el agacharse
        // Puedes cambiar la posición, la animación, etc., según tus necesidades
        // Por ejemplo, si tienes una animación específica para cuando se agacha, puedes cambiar la animación aquí

        // Restablecer la posición en Y para simular el agacharse
        y = Constants.GROUND_LEVEL;

        // Cambiar la animación o realizar otras acciones necesarias
        // brainAnimation.setCrouchAnimation(); // Esto es un ejemplo, necesitas definirlo según tus clases y lógica
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
