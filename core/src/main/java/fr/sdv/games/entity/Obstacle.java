package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    public enum ObstacleType {
        SPIKE,
        BLOCK,
        FRAGILE_BLOCK,
        GHOST_BLOCK,
        TRAP_BLOCK,
        FLY_BLOCK,
        FLY_SPIKE_TOP,
        FLY_SPIKE_BOTTOM
    }

    private float x;
    private final float y;
    private final float width;
    private final float height;
    private final ObstacleType type;

    private boolean breaking;
    private boolean broken;
    private float breakTimer;

    public Obstacle(float x, float y, float width, float height, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void update(float delta, float speed) {
        x -= speed * delta;

        if (breaking && !broken) {
            breakTimer -= delta;
            if (breakTimer <= 0f) {
                broken = true;
            }
        }
    }

    public void triggerBreak(float duration) {
        if (!breaking && !broken) {
            breaking = true;
            breakTimer = duration;
        }
    }

    public Rectangle getBounds() {
        if (broken) {
            return new Rectangle(0, 0, 0, 0);
        }
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getDangerBounds() {
        switch (type) {
            case SPIKE:
            case FLY_SPIKE_BOTTOM:
                return new Rectangle(x + width * 0.22f, y, width * 0.56f, height * 0.82f);

            case FLY_SPIKE_TOP:
                return new Rectangle(x + width * 0.22f, y - height * 0.82f, width * 0.56f, height * 0.82f);

            default:
                return getBounds();
        }
    }

    public boolean isSolid() {
        return !broken;
    }

    public boolean isBreaking() {
        return breaking;
    }

    public boolean isBroken() {
        return broken;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public ObstacleType getType() {
        return type;
    }
}
