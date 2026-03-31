package fr.sdv.games.world;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.entity.Obstacle.ObstacleType;
import fr.sdv.games.entity.Portal.PortalType;

public final class LevelFactory {
    private LevelFactory() {
    }

    public static Level createLevel1() {
        Level level = new Level();

        // Debut normal
        level.addObstacle(new Obstacle(820, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(1040, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.addObstacle(new Obstacle(1420, GameWorld.GROUND_Y + 42, 42, 42, ObstacleType.TRAP_BLOCK));
        level.addObstacle(new Obstacle(1495, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(1535, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.addObstacle(new Obstacle(1630, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(1670, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.addObstacle(new Obstacle(1940, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(2050, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(2120, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.addObstacle(new Obstacle(2260, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(2300, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(2440, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));

        // Portail fly
        level.addPortal(new Portal(2700, 0, 50, GameWorld.SCREEN_HEIGHT, PortalType.FLY));

        // Section fly
        level.addObstacle(new Obstacle(2900, 120, 44, 150, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(3050, 320, 44, 160, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(3210, GameWorld.GROUND_Y, 34, 46, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(3210, GameWorld.SCREEN_HEIGHT - 120, 34, 46, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(3370, 160, 50, 190, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(3530, 90, 44, 120, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(3530, 360, 44, 110, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(3710, GameWorld.GROUND_Y, 34, 50, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(3750, GameWorld.GROUND_Y, 34, 50, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(3790, GameWorld.SCREEN_HEIGHT - 120, 34, 50, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(3980, 120, 50, 150, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(4100, 300, 50, 170, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(4240, 180, 50, 90, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(4390, GameWorld.GROUND_Y, 34, 48, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(4435, GameWorld.SCREEN_HEIGHT - 120, 34, 48, ObstacleType.FLY_SPIKE_TOP));
        level.addObstacle(new Obstacle(4480, GameWorld.GROUND_Y, 34, 48, ObstacleType.FLY_SPIKE_BOTTOM));

        level.addObstacle(new Obstacle(4660, 120, 52, 210, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(4830, 350, 52, 110, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(4970, GameWorld.GROUND_Y, 34, 52, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(5015, GameWorld.SCREEN_HEIGHT - 120, 34, 52, ObstacleType.FLY_SPIKE_TOP));

        // Retour normal
        level.addPortal(new Portal(5300, 0, 50, GameWorld.SCREEN_HEIGHT, PortalType.CUBE));

        // Transition courte avant inversion
        level.addObstacle(new Obstacle(5560, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(5640, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(5780, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));

        // Portail inversion
        level.addPortal(new Portal(6100, 0, 50, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_ON));

//      Section inversee simplifiee : uniquement en haut
        level.addObstacle(new Obstacle(6350, GameWorld.SCREEN_HEIGHT - 36 - 42, 42, 42, ObstacleType.TRAP_BLOCK));
        level.addObstacle(new Obstacle(6430, GameWorld.SCREEN_HEIGHT - 36, 28, 38, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(6640, GameWorld.SCREEN_HEIGHT - 36 - 42, 42, 42, ObstacleType.BLOCK));

        level.addObstacle(new Obstacle(6880, GameWorld.SCREEN_HEIGHT - 36 - 42, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(6960, GameWorld.SCREEN_HEIGHT - 36, 28, 38, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(7200, GameWorld.SCREEN_HEIGHT - 36 - 42, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(7280, GameWorld.SCREEN_HEIGHT - 36 - 84, 42, 42, ObstacleType.BLOCK));

        level.addObstacle(new Obstacle(7540, GameWorld.SCREEN_HEIGHT - 36, 28, 38, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(7780, GameWorld.SCREEN_HEIGHT - 36 - 42, 42, 42, ObstacleType.BLOCK));

        // Retour normal
        level.addPortal(new Portal(8050, 0, 50, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_OFF));

        // FIN NORMALE avec plus d'obstacles
        level.addObstacle(new Obstacle(8620, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(8690, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.addObstacle(new Obstacle(8860, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(8930, GameWorld.GROUND_Y + 42, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(9010, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.addObstacle(new Obstacle(9190, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(9230, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(9370, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));

        level.addObstacle(new Obstacle(9520, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(9590, GameWorld.GROUND_Y + 42, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(9660, GameWorld.GROUND_Y + 84, 42, 42, ObstacleType.BLOCK));

        level.addObstacle(new Obstacle(9810, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(9850, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));
        level.addObstacle(new Obstacle(9890, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.addObstacle(new Obstacle(10070, GameWorld.GROUND_Y, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(10140, GameWorld.GROUND_Y + 42, 42, 42, ObstacleType.BLOCK));
        level.addObstacle(new Obstacle(10220, GameWorld.GROUND_Y, 28, 38, ObstacleType.SPIKE));

        level.setFinishX(10650f);
        return level;
    }
}
