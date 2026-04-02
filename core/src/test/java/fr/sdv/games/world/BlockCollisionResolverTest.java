package fr.sdv.games.world;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Player;
import fr.sdv.games.state.FlyState;
import fr.sdv.games.state.InvertedCubeState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockCollisionResolverTest {

    @Test
    void resolveShouldKillFlyingPlayerOnBlockContact() {
        Player player = new Player();
        player.changeState(new FlyState());
        Obstacle block = new Obstacle(140f, GameWorld.GROUND_Y, 42f, 42f, Obstacle.ObstacleType.BLOCK);

        BlockCollisionResolver.resolve(player, block);

        assertTrue(player.isDead());
    }

    @Test
    void resolveShouldLandPlayerOnTopOfTrapBlock() {
        Player player = new Player();
        player.landOn(125f);
        player.setVelocityY(-50f);
        Obstacle trapBlock = new Obstacle(140f, 90f, 42f, 42f, Obstacle.ObstacleType.TRAP_BLOCK);

        BlockCollisionResolver.resolve(player, trapBlock);

        assertEquals(132f, player.getY(), 0.0001f);
        assertTrue(trapBlock.isBreaking());
    }

    @Test
    void resolveShouldLandInvertedPlayerUnderBlock() {
        Player player = new Player();
        player.changeState(new InvertedCubeState());
        player.setVelocityY(0f);
        Obstacle block = new Obstacle(140f, 490f, 42f, 42f, Obstacle.ObstacleType.BLOCK);

        BlockCollisionResolver.resolve(player, block);

        assertEquals(458f, player.getY(), 0.0001f);
    }
}
