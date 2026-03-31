package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;
import fr.sdv.games.state.CubeState;
import fr.sdv.games.state.DeadState;
import fr.sdv.games.state.PlayerState;
import fr.sdv.games.world.GameWorld;

public class Player {
    private static final float SIZE = 32f;

    private float x;
    private float y;
    private float velocityY;
    private float rotation;
    private boolean inputPressed;
    private PlayerState state;
    private boolean deathAnimating;
    private float deathTimer;
    private float deathDuration = 0.35f;


    public Player() {
        reset();
    }

    public void reset() {
        x = 140f;
        y = GameWorld.GROUND_Y;
        velocityY = 0f;
        rotation = 0f;
        inputPressed = false;
        deathAnimating = false;
        deathTimer = 0f;
        changeState(new CubeState());
    }

    public void startDeathAnimation() {
        deathAnimating = true;
        deathTimer = deathDuration;
    }

    public void updateDeathAnimation(float delta) {
        if (!deathAnimating) {
            return;
        }

        deathTimer -= delta;
        if (deathTimer < 0f) {
            deathTimer = 0f;
        }
    }

    public boolean isDeathAnimating() {
        return deathAnimating;
    }

    public float getDeathProgress() {
        if (!deathAnimating) {
            return 0f;
        }
        return 1f - (deathTimer / deathDuration);
    }


    public void landOn(float y) {
        this.y = y;
        this.velocityY = 0f;
        this.rotation = 0f;
    }

    public void update(float delta) {
        state.update(this, delta);
    }

    public void handleInput(boolean pressed, boolean justPressed) {
        inputPressed = pressed;
        state.handleInput(this, pressed, justPressed);
    }

    public void changeState(PlayerState newState) {
        state = newState;
        state.enter(this);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isGrounded() {
        return velocityY == 0f;
    }


    public boolean isFlying() {
        return "FLY".equals(state.getName());
    }

    public boolean isDead() {
        return state instanceof DeadState;
    }

    public void jump(float force) {
        velocityY = force;
    }

    public void applyGravity(float delta, float gravity) {
        velocityY -= gravity * delta;
    }

    public void addForce(float delta, float force) {
        velocityY += force * delta;
    }

    public void moveVertical(float delta) {
        y += velocityY * delta;
    }

    public void clampToGround() {
        if (y <= GameWorld.GROUND_Y) {
            y = GameWorld.GROUND_Y;
            velocityY = 0f;
            rotation = 0f;
        }
    }

    public void clampToFlyBounds() {
        if (y < GameWorld.GROUND_Y) {
            y = GameWorld.GROUND_Y;
            velocityY = 0f;
        }

        float maxY = GameWorld.SCREEN_HEIGHT - 36f - SIZE;
        if (y > maxY) {
            y = maxY;
            velocityY = -60f;
        }
    }

    public void snapToCeiling() {
        y = GameWorld.SCREEN_HEIGHT - 36f - SIZE;
        velocityY = 0f;
    }

    public void clampToCeiling() {
        float ceilingY = GameWorld.SCREEN_HEIGHT - 36f - SIZE;
        if (y >= ceilingY) {
            y = ceilingY;
            velocityY = 0f;
            rotation = 180f;
        }
    }

    public boolean isOnCeiling() {
        float ceilingY = GameWorld.SCREEN_HEIGHT - 36f - SIZE;
        return y >= ceilingY - 0.1f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return SIZE;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void addRotation(float amount) {
        rotation += amount;
    }

    public boolean isInputPressed() {
        return inputPressed;
    }

    public PlayerState getState() {
        return state;
    }
}
