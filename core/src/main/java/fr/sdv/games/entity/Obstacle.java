package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * Represente un obstacle ou un bloc du niveau.
 */
public class Obstacle {
    /**
     * Liste des variantes d'obstacles supportees par le rendu et les collisions.
     */
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

    /**
     * Cree un obstacle positionne dans le monde.
     */
    public Obstacle(float x, float y, float width, float height, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /**
     * Fait defiler l'obstacle vers la gauche et gere sa destruction eventuelle.
     */
    public void update(float delta, float speed) {
        x -= speed * delta;

        if (breaking && !broken) {
            breakTimer -= delta;
            if (breakTimer <= 0f) {
                broken = true;
            }
        }
    }

    /**
     * Lance la destruction differee d'un bloc cassable.
     */
    public void triggerBreak(float duration) {
        if (!breaking && !broken) {
            breaking = true;
            breakTimer = duration;
        }
    }

    /**
     * Retourne la boite de collision pleine de l'obstacle.
     */
    public Rectangle getBounds() {
        if (broken) {
            return new Rectangle(0, 0, 0, 0);
        }
        return new Rectangle(x, y, width, height);
    }

    /**
     * Retourne la zone mortelle utile pour les pics.
     */
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

    /**
     * Indique si l'obstacle participe encore aux collisions.
     */
    public boolean isSolid() {
        return !broken;
    }

    /**
     *  {@code true} si la destruction a ete declenchee mais n'est pas finie
     */
    public boolean isBreaking() {
        return breaking;
    }

    /**
     *  {@code true} si l'obstacle a deja disparu
     */
    public boolean isBroken() {
        return broken;
    }

    /**
     *  position horizontale courante
     */
    public float getX() {
        return x;
    }

    /**
     *  position verticale fixe
     */
    public float getY() {
        return y;
    }

    /**
     *  largeur de l'obstacle
     */
    public float getWidth() {
        return width;
    }

    /**
     *  hauteur de l'obstacle
     */
    public float getHeight() {
        return height;
    }

    /**
     *  type logique de l'obstacle
     */
    public ObstacleType getType() {
        return type;
    }
}
