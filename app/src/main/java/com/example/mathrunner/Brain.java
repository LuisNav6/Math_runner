package com.example.mathrunner;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.widget.AppCompatImageView;

public class Brain extends AppCompatImageView {

    private MyAnimationDrawable brainAnimation;
    private int x, y; // Posición del cerebro en la pantalla
    private float velocityY; // Velocidad vertical del cerebro
    private boolean isJumping; // Indica si el cerebro está saltando
    private Book book; // Agrega una referencia al objeto Book
    private int jumpStartY; // Nueva variable global
    private int originalY;
    private int jumpHeight = 100;
    private long jumpAnimationStartTime;
    private Rect brainRect = new Rect();
    private Rect bookRect = new Rect();
    private long elapsedTimeSinceJumpStart = 0;
    private Handler updateHandler;
    private boolean isColliding = false;

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
        y = Constants.GROUND_LEVEL;
        velocityY = 0;
        isJumping = false;

        initializeBrainAnimation(); // Inicializar la animación del cerebro (brain)
        updateHandler = new Handler();
        startUpdateLoop();
    }


    private void checkAndResetJumpPosition() {
        if (elapsedTimeSinceJumpStart >= 2200) { // 2200 ms = 2.2 segundos
            // Restablecer la posición a GROUND_LEVEL
            setBrainY(Constants.GROUND_LEVEL);
            elapsedTimeSinceJumpStart = 0; // Reiniciar el tiempo transcurrido
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Detener el bucle de actualización
        updateHandler.removeCallbacksAndMessages(null);
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void initializeBrainAnimation() {
        int totalFrames = 45;
        int[] frameDurations = new int[totalFrames];
        Drawable[] frames = new Drawable[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            // Ajusta la duración de cada fotograma según sea necesario
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
            // Update the bookRect
            bookRect.set((int)book.getX(), (int)book.getY(), (int)(book.getX() + book.getWidth()), (int)(book.getY() + book.getHeight()));

            // Check if the brainRect and bookRect are intersecting
            if (Rect.intersects(brainRect, bookRect)) {
                Log.d("Collision", "Brain collided with Book!");
                    return true;
            }
        }

        return false;
    }

    public void update() {
        // Update the brainRect
        updateBrainRect();
    
        
        // Update the position of the brain only if it's not jumping
        if (!isJumping) {
            // Update the position of the brain without simulating gravity
            y = Constants.GROUND_LEVEL;
        } else {
            // If it's jumping, update the position with gravity simulation
            y += velocityY;
            Log.d("BrainRect", "Updated BrainRect coordinates: " + brainRect.toString());
            Log.d("BookRect", "Updated BoockRect coordinates: " + bookRect.toString());
            velocityY += Constants.GRAVITY;
    
            // Check if the brain has reached the ground
            if (y > Constants.GROUND_LEVEL) {
                y = Constants.GROUND_LEVEL;
                velocityY = 0;
                Log.d("BrainRect", "Updated BrainRect coordinates: " + brainRect.toString());
                Log.d("BookRect", "Updated BoockRect coordinates: " + bookRect.toString());
                isJumping = false;
            }
        }
    }

    public void jump() {
        if (!isJumping) {
            velocityY = -Constants.JUMP_VELOCITY;
            isJumping = true;
            jumpStartY = getBrainY(); // Almacena la posición Y al iniciar el salto


            // Inicia la animación de salto
            startJumpAnimation();
        }
    }

    public void stopJumpAnimation() {
        // Puedes agregar lógica adicional aquí si es necesario
        // Por ejemplo, detener la animación de salto manualmente si es posible
    }

    public void stopAnimation() {
        brainAnimation.stop();
    }

    private void startJumpAnimation() {
        isJumping = true;
        elapsedTimeSinceJumpStart = 0;
        originalY = getBrainY();
        jumpAnimationStartTime = System.currentTimeMillis();

        Animation jumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.brain_jump);

        jumpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // No es necesario iniciar nada aquí
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                y = Constants.GROUND_LEVEL;
                isJumping = false;

                // No need to check collision with book here
                stopJumpAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // No es necesario repetir nada aquí
            }
        });

        startAnimation(jumpAnimation);
    }

    private void startUpdateLoop() {
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateBrainPositionDuringAnimation();
                elapsedTimeSinceJumpStart += 16; // Assume that it's called every 16 ms

                checkAndResetJumpPosition();

                // Repeat the update loop after a short interval
                updateHandler.postDelayed(this, 16); // Approximately 60 FPS (1000 ms / 60 frames)
            }
        }, 0);
    }
    
    private void updateBrainPositionDuringAnimation() {
        if (isJumping) {
            double time = elapsedTimeSinceJumpStart / 1000.0; // Convert ms to seconds
            double newY = Constants.GROUND_LEVEL + Constants.JUMP_VELOCITY * time - 0.5 * Constants.GRAVITY * time * time;
            setBrainY((int) newY);
        } else {
            // If the brain is not jumping, it should fall to the ground due to gravity
            double time = elapsedTimeSinceJumpStart / 1000.0; // Convert ms to seconds
            double newY = y + 0.5 * Constants.GRAVITY * time * time;
            setBrainY((int) newY);
        }
    
        updateBrainRect();
    
        if (isCollidingWithBook(book) && !isJumping) {
            startJumpAnimation();
        }
        if (book != null) {
            if (y + getHeight() < book.getY()) {
                // The brain is above the book and should not collide with it

                Log.d("BrainRect", "Updated BrainRect coordinates: " + brainRect.toString());
                Log.d("BookRect", "Updated BoockRect coordinates: " + bookRect.toString());
            } else if (x >= book.getX() && x <= book.getX() + book.getWidth()) {
                // The brain is colliding with the book and should jump
                if (!isJumping && !isColliding) {

                    startJumpAnimation();

                    isColliding = true;
    
                    // Start a timer to ignore further collisions for 500 milliseconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isColliding = false;
                        }
                    }, 500);
                }
            }
        }
    }

    private void updateBrainRect() {
        brainRect.set(x, y, x + getWidth(), y + getHeight());
    }


    private int getJumpAnimationOffset() {
        long animationDurationMillis = getJumpAnimationDuration();
        float progress = (float) (System.currentTimeMillis() - jumpAnimationStartTime) / animationDurationMillis;
        float offset = (float) Math.sin(progress * Math.PI) * jumpHeight;
        return Math.round(offset);
    }

    private long getJumpAnimationDuration() {
        return 2200; // 2.2 segundos en milisegundos
    }

    private void checkCollisionWithBook() {
        if (isCollidingWithBook(book)) {
            // Agrega aquí cualquier lógica adicional después de la colisión
            handleCollision();
        }
    }

    private void handleCollision() {
        // Agrega aquí la lógica que deseas al producirse la colisión
        // Por ejemplo, reducir vidas, actualizar puntajes, etc.
    }

    public void setBrainX(int x) {
        this.x = x;
    }

    public int getBrainX() {
        return x;
    }

    public void setBrainY(int y) {
        this.y = y;
    }

    public int getBrainY() {
        return y;
    }


    public void reduceSpeed() {
        brainAnimation.restartAnimation();
    }

    private void startAnimation() {
        brainAnimation.start();
    }
}