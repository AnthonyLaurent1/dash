package fr.sdv.games.state;

import fr.sdv.games.entity.Player;
import fr.sdv.games.world.GameWorld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlyStateTest {

    @Test
    void enterShouldResetPlayerRotation() {
        Player player = new Player();
        player.setRotation(120f);
        FlyState state = new FlyState();

        state.enter(player);

        assertEquals(0f, player.getRotation(), 0.0001f);
    }

    @Test
    void updateShouldApplyUpwardForceWhenInputIsPressed() {
        Player player = new Player();
        player.handleInput(true, true);
        FlyState state = new FlyState();

        state.update(player, 0.1f);

        assertTrue(player.getVelocityY() > 0f);
        assertTrue(player.getY() > GameWorld.GROUND_Y);
    }

    @Test
    void updateShouldApplyGravityWhenInputIsNotPressed() {
        Player player = new Player();
        player.landOn(200f);
        player.handleInput(false, false);
        FlyState state = new FlyState();

        state.update(player, 0.1f);

        assertTrue(player.getVelocityY() < 0f);
        assertTrue(player.getY() < 200f);
    }

    @Test
    void updateShouldClampVelocityToMaximumPositiveValue() {
        Player player = new Player();
        player.setVelocityY(1000f);
        player.handleInput(true, true);
        FlyState state = new FlyState();

        state.update(player, 0.1f);

        assertEquals(420f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void updateShouldClampVelocityToMaximumNegativeValue() {
        Player player = new Player();
        player.landOn(300f);
        player.setVelocityY(-1000f);
        player.handleInput(false, false);
        FlyState state = new FlyState();

        state.update(player, 0.1f);

        assertEquals(-420f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void updateShouldClampPlayerInsideFlyBounds() {
        Player player = new Player();
        player.landOn(1000f);
        player.setVelocityY(200f);
        player.handleInput(true, true);
        FlyState state = new FlyState();

        state.update(player, 0.1f);

        assertTrue(player.getY() <= GameWorld.SCREEN_HEIGHT - 36f - player.getSize());
    }

    @Test
    void getNameShouldReturnFly() {
        FlyState state = new FlyState();

        assertEquals("FLY", state.getName());
    }
}
