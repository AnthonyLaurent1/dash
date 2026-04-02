package fr.sdv.games.state;

import fr.sdv.games.entity.Player;
import fr.sdv.games.world.GameWorld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvertedCubeStateTest {

    @Test
    void enterShouldSnapPlayerToCeilingAndRotateIt() {
        Player player = new Player();
        player.landOn(GameWorld.GROUND_Y);
        InvertedCubeState state = new InvertedCubeState();

        state.enter(player);

        assertEquals(GameWorld.SCREEN_HEIGHT - 36f - player.getSize(), player.getY(), 0.0001f);
        assertEquals(180f, player.getRotation(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void handleInputShouldLaunchInverseJumpWhenGrounded() {
        Player player = new Player();
        player.snapToCeiling();
        InvertedCubeState state = new InvertedCubeState();

        state.handleInput(player, true, true);

        assertEquals(-650f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void handleInputShouldDoNothingWhenNotPressed() {
        Player player = new Player();
        player.snapToCeiling();
        InvertedCubeState state = new InvertedCubeState();

        state.handleInput(player, false, false);

        assertEquals(0f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void updateShouldMovePlayerAwayFromCeilingAfterInverseJump() {
        Player player = new Player();
        InvertedCubeState state = new InvertedCubeState();
        state.enter(player);
        player.jump(-650f);

        float startY = player.getY();

        state.update(player, 0.1f);

        assertTrue(player.getY() < startY);
    }

    @Test
    void updateShouldClampPlayerBackToCeiling() {
        Player player = new Player();
        player.snapToCeiling();
        player.addForce(0.5f, 3000f);
        InvertedCubeState state = new InvertedCubeState();

        state.update(player, 0.1f);

        assertEquals(GameWorld.SCREEN_HEIGHT - 36f - player.getSize(), player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertEquals(180f, player.getRotation(), 0.0001f);
    }

    @Test
    void getNameShouldReturnInverted() {
        InvertedCubeState state = new InvertedCubeState();

        assertEquals("INVERTED", state.getName());
    }
}
