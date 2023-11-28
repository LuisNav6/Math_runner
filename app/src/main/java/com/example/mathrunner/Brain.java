package com.example.mathrunner;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.widget.AppCompatImageView;

public class Brain extends AppCompatImageView {

    private MyAnimationDrawable brainAnimation;
    private int x, y; // Posición del cerebro en la pantalla
    private int velocityY; // Velocidad vertical del cerebro
    private boolean isJumping; // Indica si el cerebro está saltando
    private Book book; // Agrega una referencia al objeto Book
    private int jumpStartY; // Nueva variable global

    private Rect brainRect = new Rect();
    private Rect bookRect = new Rect();
    private boolean hasCollided = false;
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

    public void setBook(Book book) {
        this.book = book;
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
        if (book != null) {
            // Ajusta la posición del cerebro
            brainRect.top = y;
            brainRect.bottom = y + getHeight();

            // Actualiza el rectángulo del cerebro durante el salto
            if (isJumping) {
                brainRect.top = jumpStartY;
                brainRect.bottom = jumpStartY + getHeight();
            }

            this.getHitRect(brainRect);
            book.getHitRect(bookRect);

            // Log de las posiciones de los rectángulos para debug
            Log.d("info brain", "Brain Rect: " + brainRect.toString());
            Log.d("info book", "Book Rect: " + bookRect.toString());

            boolean collisionDetected = Rect.intersects(brainRect, bookRect);

            if (collisionDetected) {
                Log.d("Collision", "Collision detected!");
                return true;
            }
        }

        return false;
    }






    public void update() {
        // Restablece hasCollided en cada actualización
        hasCollided = false;
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
        jumpStartY = getBrainY(); // Almacena la posición Y al iniciar el salto

        // Inicia la animación de salto
        startJumpAnimation();
    }


    public void stopJumpAnimation() {
        if (isJumping) {
            isJumping = false;
        }
    }

    private void startJumpAnimation() {
        Animation jumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.brain_jump);

        jumpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                updateBrainPositionDuringAnimation(animation, Constants.GROUND_LEVEL);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                y = Constants.GROUND_LEVEL;
                isJumping = false;

                // Luego de que la animación ha finalizado, verifica la colisión
                checkCollisionWithBook();

                // Ajusta la posición Y después de la animación
                brainRect.top = y;
                brainRect.bottom = y + getHeight();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                updateBrainPositionDuringAnimation(animation, Constants.GROUND_LEVEL);

                // Durante la repetición de la animación, verifica la colisión
                checkCollisionWithBook();
            }
        });

        if (isJumping) {
            startAnimation(jumpAnimation);
        }
    }




    private void updateBrainPositionDuringAnimation(final Animation animation, final int finalY) {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Remueve el listener para que no se llame continuamente
                getViewTreeObserver().removeOnPreDrawListener(this);

                // Establece la nueva posición del cerebro
                brainRect.top = finalY; // Utiliza la posición Y final
                brainRect.bottom = finalY + getHeight(); // Ajusta la parte inferior en consecuencia

                // Asegúrate de que la posición Y no sea inferior al suelo
                if (brainRect.bottom > Constants.GROUND_LEVEL) {
                    brainRect.bottom = Constants.GROUND_LEVEL;
                    brainRect.top = brainRect.bottom - getHeight();
                }

                return true;
            }
        });
    }




    private void checkCollisionWithBook() {
        // Verifica la colisión después de que la animación ha finalizado
        if (isCollidingWithBook(book)) {
            if (!hasCollided) {
                Log.d("check Collision", "Brain collided with Book!");
                Log.d("check Collision", "Brain position (X, Y): (" + getBrainX() + ", " + getBrainY() + ")");
                Log.d("check Collision", "Book position (X, Y): (" + book.getBookX() + ", " + book.getBookY() + ")");
                Log.d("check Collision", "Brain Rect: " + brainRect.toString());
                Log.d("check Collision", "Book Rect: " + bookRect.toString());
                // Agrega aquí cualquier lógica adicional después de la colisión
                hasCollided = true;
            } else {
                Log.d("check Collision", "Collision already detected!");
            }
        } else {
            Log.d("check Collision", "Brain not collided with Book!");
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
        // Lógica para reducir la velocidad según tus necesidades
        // Puedes ajustar la velocidad directamente o aplicar alguna lógica específica aquí
        Constants.BOOK_SPEED *= 0.9; // Por ejemplo, reducir la velocidad en un 10%
    }


    // Método para iniciar la animación
    private void startAnimation() {
        brainAnimation.start();
    }
}
