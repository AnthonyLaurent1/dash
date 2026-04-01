package fr.sdv.games.world.endless;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Obstacle.ObstacleType;
import fr.sdv.games.world.GameWorld;
import fr.sdv.games.world.Level;

/**
 * Petit helper de placement des obstacles du mode infini.
 */
public class EndlessObstaclePlacer {
    private static final float TILE = 42f;
    private static final float SPIKE_WIDTH = 28f;
    private static final float SPIKE_HEIGHT = 38f;

    /**
     * Ajoute un bloc classique aligne sur la grille du sol.
     */
    public void addBlock(Level level, float x, int heightSteps) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y + TILE * heightSteps, TILE, TILE, ObstacleType.BLOCK));
    }

    /**
     * Ajoute un bloc piege traversable aligne sur la grille du sol.
     */
    public void addTrap(Level level, float x, int heightSteps) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y + TILE * heightSteps, TILE, TILE, ObstacleType.TRAP_BLOCK));
    }

    /**
     * Ajoute un pic au sol.
     */
    public void addGroundSpike(Level level, float x) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, SPIKE_WIDTH, SPIKE_HEIGHT, ObstacleType.SPIKE));
    }

    /**
     * Ajoute un bloc classique sur la grille du plafond inverse.
     */
    public void addCeilingBlock(Level level, float x, int depthSteps) {
        float y = GameWorld.SCREEN_HEIGHT - 36f - (TILE * depthSteps);
        level.addObstacle(new Obstacle(x, y, TILE, TILE, ObstacleType.BLOCK));
    }

    /**
     * Ajoute un bloc piege sur la grille du plafond inverse.
     */
    public void addCeilingTrap(Level level, float x, int depthSteps) {
        float y = GameWorld.SCREEN_HEIGHT - 36f - (TILE * depthSteps);
        level.addObstacle(new Obstacle(x, y, TILE, TILE, ObstacleType.TRAP_BLOCK));
    }

    /**
     * Ajoute un pic suspendu pour les sequences inversees.
     */
    public void addCeilingSpike(Level level, float x) {
        level.addObstacle(new Obstacle(x, GameWorld.SCREEN_HEIGHT - 36f, SPIKE_WIDTH, SPIKE_HEIGHT, ObstacleType.FLY_SPIKE_TOP));
    }
}
