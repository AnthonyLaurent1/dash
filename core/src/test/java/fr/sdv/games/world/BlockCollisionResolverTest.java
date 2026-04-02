package fr.sdv.games.world;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Player;
import fr.sdv.games.state.FlyState;
import fr.sdv.games.state.InvertedCubeState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockCollisionResolverTest {

    @Test
    void resolveShouldKillPlayerWhenFlyingIntoBlock() {
        Player player = new Player();
        player.changeState(new FlyState());

        Obstacle obstacle = new Obstacle(
            player.getX(),
            player.getY(),
            42f,
            42f,
            Obstacle.ObstacleType.BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertTrue(player.isDead());
        assertTrue(player.isDeathAnimating());
    }

    @Test
    void resolveShouldLandPlayerOnTopOfStandardBlock() {
        Player player = new Player();
        player.landOn(140f);
        player.setVelocityY(-50f);

        Obstacle obstacle = new Obstacle(
            player.getX(),
            90f,
            42f,
            42f,
            Obstacle.ObstacleType.BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertEquals(132f, player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertFalse(player.isDead());
    }

    @Test
    void resolveShouldBreakFragileBlockWhenLandingOnIt() {
        Player player = new Player();
        player.landOn(140f);
        player.setVelocityY(-50f);

        Obstacle obstacle = new Obstacle(
            player.getX(),
            90f,
            42f,
            42f,
            Obstacle.ObstacleType.FRAGILE_BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertTrue(obstacle.isBreaking());
        assertFalse(obstacle.isBroken());
        assertFalse(player.isDead());
    }

    @Test
    void resolveShouldBreakTrapBlockWhenLandingOnIt() {
        Player player = new Player();
        player.landOn(136f);
        player.setVelocityY(-20f);

        Obstacle obstacle = new Obstacle(
            player.getX(),
            90f,
            42f,
            42f,
            Obstacle.ObstacleType.TRAP_BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertTrue(obstacle.isBreaking());
        assertFalse(player.isDead());
    }

    @Test
    void resolveShouldNotKillPlayerWhenPassingThroughGhostBlock() {
        Player player = new Player();
        player.landOn(110f);
        player.setVelocityY(0f);

        Obstacle obstacle = new Obstacle(
            player.getX(),
            90f,
            42f,
            42f,
            Obstacle.ObstacleType.GHOST_BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertFalse(player.isDead());
    }

    @Test
    void resolveShouldKillPlayerWhenIntersectingSolidBlockFromSide() {
        Player player = new Player();
        player.landOn(100f);
        player.setVelocityY(0f);

        Obstacle obstacle = new Obstacle(
            player.getX(),
            90f,
            42f,
            42f,
            Obstacle.ObstacleType.BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertTrue(player.isDead());
    }

    @Test
    void resolveShouldLandPlayerUnderBlockWhenInverted() {
        Player player = new Player();
        player.changeState(new InvertedCubeState());
        player.setVelocityY(20f);

        Obstacle obstacle = new Obstacle(
            player.getX(),
            player.getY() + player.getSize() - 4f,
            42f,
            42f,
            Obstacle.ObstacleType.BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertEquals(obstacle.getY() - player.getSize(), player.getY(), 0.0001f);
        assertFalse(player.isDead());
    }

    @Test
    void resolveShouldKillPlayerWhenDeepInsideSolidBlockInInvertedMode() {
        Player player = new Player();
        player.changeState(new InvertedCubeState());
        player.landOn(120f);
        player.setVelocityY(0f);

        Obstacle obstacle = new Obstacle(
            player.getX(),
            100f,
            42f,
            42f,
            Obstacle.ObstacleType.BLOCK
        );

        BlockCollisionResolver.resolve(player, obstacle);

        assertTrue(player.isDead());
    }
}
