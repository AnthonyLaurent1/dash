package fr.sdv.games.entity;

import fr.sdv.games.state.CubeState;
import fr.sdv.games.state.DeadState;
import fr.sdv.games.state.FlyState;
import fr.sdv.games.state.InvertedCubeState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void resetShouldRestoreInitialPlayerState() {
        Player player = new Player();
        player.jump(500f);
        player.moveVertical(0.1f);
        player.addRotation(90f);
        player.startDeathAnimation();
        player.changeState(new FlyState());

        player.reset();

        assertEquals(140f, player.getX());
        assertEquals(90f, player.getY());
        assertEquals(0f, player.getVelocityY());
        assertEquals(0f, player.getRotation());
        assertFalse(player.isInputPressed());
        assertFalse(player.isDeathAnimating());
        assertEquals("CUBE", player.getState().getName());
    }

    @Test
    void startDeathAnimationShouldActivateAnimationProgress() {
        Player player = new Player();

        player.startDeathAnimation();

        assertTrue(player.isDeathAnimating());
        assertEquals(0f, player.getDeathProgress(), 0.0001f);
    }

    @Test
    void dieShouldCentralizeDeadStateTransition() {
        Player player = new Player();

        player.die();

        assertTrue(player.isDead());
        assertTrue(player.isDeathAnimating());
    }

    @Test
    void updateDeathAnimationShouldAdvanceProgressUntilOne() {
        Player player = new Player();
        player.startDeathAnimation();

        player.updateDeathAnimation(0.35f);

        assertTrue(player.isDeathAnimating());
        assertEquals(1f, player.getDeathProgress(), 0.0001f);
    }

    @Test
    void jumpShouldSetVerticalVelocity() {
        Player player = new Player();

        player.jump(650f);

        assertEquals(650f, player.getVelocityY());
    }

    @Test
    void applyGravityShouldDecreaseVerticalVelocity() {
        Player player = new Player();
        player.setVelocityY(300f);

        player.applyGravity(0.1f, 1000f);

        assertEquals(200f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void addForceShouldIncreaseVerticalVelocity() {
        Player player = new Player();
        player.setVelocityY(100f);

        player.addForce(0.2f, 500f);

        assertEquals(200f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void clampToGroundShouldSnapPlayerToGroundWhenBelowGround() {
        Player player = new Player();
        player.setVelocityY(-300f);
        player.moveVertical(1f);

        player.clampToGround();

        assertEquals(90f, player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertEquals(0f, player.getRotation(), 0.0001f);
    }

    @Test
    void clampToFlyBoundsShouldKeepPlayerInsidePlayableArea() {
        Player player = new Player();
        player.landOn(600f);
        player.setVelocityY(50f);

        player.clampToFlyBounds();

        assertTrue(player.getY() <= 472f);
    }

    @Test
    void snapToCeilingShouldPlacePlayerAtCeiling() {
        Player player = new Player();

        player.snapToCeiling();

        assertEquals(472f, player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void clampToCeilingShouldLockPlayerToCeilingAndRotate() {
        Player player = new Player();
        player.snapToCeiling();
        player.addForce(0.2f, 100f);

        player.clampToCeiling();

        assertEquals(472f, player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertEquals(180f, player.getRotation(), 0.0001f);
    }

    @Test
    void changeStateShouldUpdateCurrentState() {
        Player player = new Player();

        player.changeState(new FlyState());

        assertEquals("FLY", player.getState().getName());
        assertTrue(player.isFlying());
    }

    @Test
    void isInvertedShouldReturnTrueWhenInvertedStateIsApplied() {
        Player player = new Player();

        player.changeState(new InvertedCubeState());

        assertTrue(player.isInverted());
    }

    @Test
    void isDeadShouldReturnTrueWhenDeadStateIsApplied() {
        Player player = new Player();

        player.changeState(new DeadState());

        assertTrue(player.isDead());
    }

    @Test
    void handleInputShouldStorePressedState() {
        Player player = new Player();

        player.handleInput(true, false);

        assertTrue(player.isInputPressed());
    }
}
