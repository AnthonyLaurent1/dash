package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;

public class Portal {
    public enum PortalType {
        FLY,
        CUBE,
        INVERT_ON,
        INVERT_OFF
    }


    private float x;
    private final float y;
    private final float width;
    private final float height;
    private final PortalType type;

    public Portal(float x, float y, float width, float height, PortalType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void update(float delta, float speed) {
        x -= speed * delta;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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

    public PortalType getType() {
        return type;
    }
}
