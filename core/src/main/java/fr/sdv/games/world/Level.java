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
