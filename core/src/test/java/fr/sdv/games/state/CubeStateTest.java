package fr.sdv.games.state;

import fr.sdv.games.entity.Player;
import fr.sdv.games.world.GameWorld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CubeStateTest {

    @Test
    void enterShouldResetPlayerRotation() {
        Player player = new Player();
        player.setRotation(90f);
        CubeState state = new CubeState();

        state.enter(player);

        assertEquals(0f, player.getRotation(), 0.0001f);
    }

    @Test
    void handleInputShouldMakePlayerJumpWhenGroundedAndPressed() {
        Player player = new Player();
        CubeState state = new CubeState();

        state.handleInput(player, true, true);

        assertEquals(650f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void handleInputShouldDoNothingWhenNotPressed() {
        Player player = new Player();
        CubeState state = new CubeState();

        state.handleInput(player, false, false);

        assertEquals(0f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void updateShouldApplyGravityAndMovePlayerDown() {
        Player player = new Player();
        player.landOn(200f);
        CubeState state = new CubeState();

        state.update(player, 0.1f);

        assertTrue(player.getY() < 200f);
        assertTrue(player.getVelocityY() < 0f);
    }

    @Test
    void updateShouldClampPlayerToGroundWhenBelowGround() {
        Player player = new Player();
        player.landOn(GameWorld.GROUND_Y);
        player.setVelocityY(-200f);
        CubeState state = new CubeState();

        state.update(player, 0.2f);

        assertEquals(GameWorld.GROUND_Y, player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertEquals(0f, player.getRotation(), 0.0001f);
    }

    @Test
    void getNameShouldReturnCube() {
        CubeState state = new CubeState();

        String name = state.getName();

        assertEquals("CUBE", name);
    }
}
