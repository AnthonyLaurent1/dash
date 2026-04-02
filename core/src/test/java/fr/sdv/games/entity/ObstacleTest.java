package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {

    @Test
    void constructorShouldInitializeObstacleFields() {
        Obstacle obstacle = new Obstacle(100f, 90f, 42f, 42f, Obstacle.ObstacleType.BLOCK);

        assertEquals(100f, obstacle.getX());
        assertEquals(90f, obstacle.getY());
        assertEquals(42f, obstacle.getWidth());
        assertEquals(42f, obstacle.getHeight());
        assertEquals(Obstacle.ObstacleType.BLOCK, obstacle.getType());
    }

    @Test
    void updateShouldMoveObstacleToTheLeft() {
        Obstacle obstacle = new Obstacle(200f, 90f, 42f, 42f, Obstacle.ObstacleType.BLOCK);

        obstacle.update(0.5f, 100f);

        assertEquals(150f, obstacle.getX(), 0.0001f);
    }

    @Test
    void triggerBreakShouldStartBreakingState() {
        Obstacle obstacle = new Obstacle(200f, 90f, 42f, 42f, Obstacle.ObstacleType.FRAGILE_BLOCK);

        obstacle.triggerBreak(0.3f);

        assertTrue(obstacle.isBreaking());
        assertFalse(obstacle.isBroken());
        assertTrue(obstacle.isSolid());
    }

    @Test
    void updateShouldMarkObstacleAsBrokenWhenBreakTimerExpires() {
        Obstacle obstacle = new Obstacle(200f, 90f, 42f, 42f, Obstacle.ObstacleType.TRAP_BLOCK);
        obstacle.triggerBreak(0.1f);

        obstacle.update(0.2f, 0f);

        assertTrue(obstacle.isBroken());
        assertFalse(obstacle.isSolid());
    }

    @Test
    void getBoundsShouldReturnEmptyRectangleWhenObstacleIsBroken() {
        Obstacle obstacle = new Obstacle(200f, 90f, 42f, 42f, Obstacle.ObstacleType.FRAGILE_BLOCK);
        obstacle.triggerBreak(0.1f);
        obstacle.update(0.2f, 0f);

        Rectangle bounds = obstacle.getBounds();

        assertEquals(0f, bounds.width, 0.0001f);
        assertEquals(0f, bounds.height, 0.0001f);
    }

    @Test
    void getDangerBoundsShouldShrinkGroundSpikeHitbox() {
        Obstacle obstacle = new Obstacle(100f, 90f, 28f, 38f, Obstacle.ObstacleType.SPIKE);

        Rectangle dangerBounds = obstacle.getDangerBounds();

        assertEquals(100f + 28f * 0.22f, dangerBounds.x, 0.0001f);
        assertEquals(90f, dangerBounds.y, 0.0001f);
        assertEquals(28f * 0.56f, dangerBounds.width, 0.0001f);
        assertEquals(38f * 0.82f, dangerBounds.height, 0.0001f);
    }

    @Test
    void getDangerBoundsShouldShiftTopSpikeHitboxUpward() {
        Obstacle obstacle = new Obstacle(150f, 500f, 28f, 38f, Obstacle.ObstacleType.FLY_SPIKE_TOP);

        Rectangle dangerBounds = obstacle.getDangerBounds();

        assertEquals(150f + 28f * 0.22f, dangerBounds.x, 0.0001f);
        assertEquals(500f - 38f * 0.82f, dangerBounds.y, 0.0001f);
        assertEquals(28f * 0.56f, dangerBounds.width, 0.0001f);
        assertEquals(38f * 0.82f, dangerBounds.height, 0.0001f);
    }

    @Test
    void getDangerBoundsShouldMatchBoundsForNonSpikeObstacle() {
        Obstacle obstacle = new Obstacle(300f, 90f, 42f, 42f, Obstacle.ObstacleType.BLOCK);

        Rectangle bounds = obstacle.getBounds();
        Rectangle dangerBounds = obstacle.getDangerBounds();

        assertEquals(bounds.x, dangerBounds.x, 0.0001f);
        assertEquals(bounds.y, dangerBounds.y, 0.0001f);
        assertEquals(bounds.width, dangerBounds.width, 0.0001f);
        assertEquals(bounds.height, dangerBounds.height, 0.0001f);
    }

    @Test
    void triggerBreakShouldDoNothingWhenObstacleIsAlreadyBroken() {
        Obstacle obstacle = new Obstacle(200f, 90f, 42f, 42f, Obstacle.ObstacleType.FRAGILE_BLOCK);
        obstacle.triggerBreak(0.1f);
        obstacle.update(0.2f, 0f);

        obstacle.triggerBreak(1f);

        assertTrue(obstacle.isBroken());
        assertFalse(obstacle.isSolid());
    }
}
