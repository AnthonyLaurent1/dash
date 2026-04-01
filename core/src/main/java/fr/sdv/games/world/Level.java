package fr.sdv.games.world;

import com.badlogic.gdx.utils.Array;
import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;

public class Level {
    private final Array<Obstacle> obstacles = new Array<>();
    private final Array<Portal> portals = new Array<>();
    private float finishX = 5000f;
    private float initialFinishX = 5000f;

    public void update(float delta, float speed) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update(delta, speed);
        }
        for (Portal portal : portals) {
            portal.update(delta, speed);
        }
        finishX -= speed * delta;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Array<Portal> getPortals() {
        return portals;
    }

    public float getFinishX() {
        return finishX;
    }

    public void setFinishX(float finishX) {
        this.finishX = finishX;
        this.initialFinishX = finishX;
    }

    public float getInitialFinishX() {
        return initialFinishX;
    }
}
