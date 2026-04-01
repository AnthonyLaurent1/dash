package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * Represente un portail qui change le mode de jeu du joueur.
 */
public class Portal {
    /**
     * Types de transitions supportes par les portails.
     */
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

    /**
     * Cree un portail positionne dans le niveau.
     */
    public Portal(float x, float y, float width, float height, PortalType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /**
     * Fait defiler le portail avec le reste du niveau.
     */
    public void update(float delta, float speed) {
        x -= speed * delta;
    }

    /**
     * boite de collision du portail
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * position horizontale courante
     */
    public float getX() {
        return x;
    }

    /**
     * position verticale
     */
    public float getY() {
        return y;
    }

    /**
     * largeur du portail
     */
    public float getWidth() {
        return width;
    }

    /**
     * hauteur du portail
     */
    public float getHeight() {
        return height;
    }

    /**
     * type de transition applique par le portail
     */
    public PortalType getType() {
        return type;
    }
}
