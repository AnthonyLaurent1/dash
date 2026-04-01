package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * Modele logique du bouton de redemarrage affiche en overlay.
 */
public class RestartButton {
    private final Rectangle bounds;
    private final String text;

    /**
     * Cree un bouton avec son rectangle cliquable et son libelle.
     */
    public RestartButton(float x, float y, float width, float height, String text) {
        this.bounds = new Rectangle(x, y, width, height);
        this.text = text;
    }

    /**
     * Verifie si un point appartient a la zone cliquable du bouton.
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    /**
     * @return rectangle cliquable du bouton
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * @return texte affiche dans le bouton
     */
    public String getText() {
        return text;
    }
}
