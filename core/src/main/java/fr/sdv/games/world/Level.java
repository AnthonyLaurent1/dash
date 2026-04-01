package fr.sdv.games.world;

import com.badlogic.gdx.utils.Array;
import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;

/**
 * Conteneur mutable des obstacles, portails et de la ligne d'arrivee.
 */
public class Level {
    private final Array<Obstacle> obstacles = new Array<>();
    private final Array<Portal> portals = new Array<>();
    private float finishX = 5000f;
    private float initialFinishX = 5000f;

    /**
     * Fait defiler tous les elements du niveau et l'arrivee.
     */
    public void update(float delta, float speed) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update(delta, speed);
        }
        for (Portal portal : portals) {
            portal.update(delta, speed);
        }
        finishX -= speed * delta;
    }

    /**
     * Ajoute un obstacle au niveau.
     */
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * Ajoute un portail au niveau.
     */
    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    /**
     * Supprime les elements deja sortis de l'ecran pour limiter la taille du niveau en memoire.
     *
     * @param minX seuil horizontal sous lequel un element peut etre retire
     */
    public void removeOffscreen(float minX) {
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle.getX() + obstacle.getWidth() < minX) {
                obstacles.removeIndex(i);
            }
        }

        for (int i = portals.size - 1; i >= 0; i--) {
            Portal portal = portals.get(i);
            if (portal.getX() + portal.getWidth() < minX) {
                portals.removeIndex(i);
            }
        }
    }

    /**
     * @return l'abscisse la plus a droite occupee par un obstacle ou un portail
     */
    public float getFarthestX() {
        float farthestX = 0f;

        for (Obstacle obstacle : obstacles) {
            farthestX = Math.max(farthestX, obstacle.getX() + obstacle.getWidth());
        }

        for (Portal portal : portals) {
            farthestX = Math.max(farthestX, portal.getX() + portal.getWidth());
        }

        return farthestX;
    }

    /**
     * @return liste des obstacles du niveau
     */
    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * @return liste des portails du niveau
     */
    public Array<Portal> getPortals() {
        return portals;
    }

    /**
     * @return position horizontale courante de la fin
     */
    public float getFinishX() {
        return finishX;
    }

    /**
     * Defini la position initiale de la fin de niveau.
     */
    public void setFinishX(float finishX) {
        this.finishX = finishX;
        this.initialFinishX = finishX;
    }

    /**
     * @return position initiale de l'arrivee avant defilement
     */
    public float getInitialFinishX() {
        return initialFinishX;
    }
}
