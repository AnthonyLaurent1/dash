package fr.sdv.games.world;

import com.badlogic.gdx.math.Vector2;
import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.entity.Obstacle.ObstacleType;
import fr.sdv.games.entity.Portal.PortalType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameWorldTest {

    @Test
    void playerShouldEnterFlyStateWhenTouchingFlyPortal() {
        Level level = new Level();
        level.setFinishX(10_000f);
        level.addPortal(new Portal(140f, 0f, 50f, GameWorld.SCREEN_HEIGHT, PortalType.FLY));
        GameWorld world = new GameWorld(level);

        world.update(0.016f);

        assertEquals("FLY", world.getPlayer().getState().getName());
        assertTrue(world.getPlayer().isFlying());
    }

    @Test
    void playerShouldDieWhenTouchingSpike() {
        Level level = new Level();
        level.setFinishX(10_000f);
        level.addObstacle(new Obstacle(140f, GameWorld.GROUND_Y, 32f, 40f, ObstacleType.SPIKE));
        GameWorld world = new GameWorld(level);

        world.update(0.016f);

        assertTrue(world.getPlayer().isDead());
        assertTrue(world.getPlayer().isDeathAnimating());
    }

    @Test
    void trapBlockShouldStartBreakingWhenPlayerTouchesIt() {
        Level level = new Level();
        level.setFinishX(10_000f);
        Obstacle trapBlock = new Obstacle(140f, GameWorld.GROUND_Y, 42f, 42f, ObstacleType.TRAP_BLOCK);
        level.addObstacle(trapBlock);
        GameWorld world = new GameWorld(level);

        world.update(0.016f);

        assertTrue(trapBlock.isBreaking());
        assertFalse(trapBlock.isBroken());
        assertFalse(world.getPlayer().isDead());
    }

    @Test
    void worldShouldEnterEndlessModeWhenScoreThresholdIsReached() {
        Level level = new Level();
        level.setFinishX(10_000f);
        GameWorld world = new GameWorld(level);

        world.update(50.1f);

        assertTrue(world.isEndlessMode());
        assertFalse(world.isVictory());
        assertTrue(world.getScore() >= GameWorld.ENDLESS_START_SCORE);
    }

    @Test
    void worldShouldDeclareVictoryWhenFinishLineIsReachedBeforeEndlessMode() {
        Level level = new Level();
        level.setFinishX(100f);
        GameWorld world = new GameWorld(level);

        world.update(0.016f);

        assertTrue(world.isVictory());
        assertFalse(world.isEndlessMode());
    }

    @Test
    void clickRestartShouldResetWorldAfterDeath() {
        Level level = new Level();
        level.setFinishX(10_000f);
        level.addObstacle(new Obstacle(140f, GameWorld.GROUND_Y, 32f, 40f, ObstacleType.SPIKE));
        GameWorld world = new GameWorld(level);
        world.update(0.016f);

        Vector2 clickInsideRestartButton = new Vector2(350f, 140f);

        boolean restarted = world.clickRestart(clickInsideRestartButton);

        assertTrue(restarted);
        assertFalse(world.getPlayer().isDead());
        assertFalse(world.isVictory());
        assertFalse(world.isEndlessMode());
        assertEquals(0f, world.getScore());
    }

    @Test
    void clickRestartShouldReturnFalseWhileGameIsRunning() {
        Level level = new Level();
        level.setFinishX(10_000f);
        GameWorld world = new GameWorld(level);

        boolean restarted = world.clickRestart(new Vector2(350f, 140f));

        assertFalse(restarted);
        assertFalse(world.getPlayer().isDead());
    }
}
