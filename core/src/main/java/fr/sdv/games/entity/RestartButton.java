package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;

public class RestartButton {
    private final Rectangle bounds;
    private final String text;

    public RestartButton(float x, float y, float width, float height, String text) {
        this.bounds = new Rectangle(x, y, width, height);
        this.text = text;
    }

    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getText() {
        return text;
    }
}
