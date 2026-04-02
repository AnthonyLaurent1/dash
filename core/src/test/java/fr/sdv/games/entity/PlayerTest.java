package fr.sdv.games.entity;

import fr.sdv.games.state.DeadState;
import fr.sdv.games.state.FlyState;
import fr.sdv.games.state.InvertedCubeState;
import fr.sdv.games.world.GameWorld;
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
        assertEquals(GameWorld.GROUND_Y, player.getY());
        assertEquals(0f, player.getVelocityY());
        assertEquals(0f, player.getRotation());
        assertFalse(player.isInputPressed());
        assertFalse(player.isDeathAnimating());
        assertFalse(player.isFlying());
        assertFalse(player.isInverted());
        assertFalse(player.isDead());
        assertEquals("CUBE", player.getState().getName());
    }

    @Test
    void dieShouldActivateDeathAnimationAndDeadState() {
        Player player = new Player();

        player.die();

        assertTrue(player.isDead());
        assertTrue(player.isDeathAnimating());
        assertInstanceOf(DeadState.class, player.getState());
    }

    @Test
    void dieShouldDoNothingIfPlayerIsAlreadyDead() {
        Player player = new Player();
        player.die();
        float firstProgress = player.getDeathProgress();

        player.die();

        assertTrue(player.isDead());
        assertTrue(player.isDeathAnimating());
        assertEquals(firstProgress, player.getDeathProgress(), 0.0001f);
    }

    @Test
    void updateDeathAnimationShouldAdvanceProgressUntilOne() {
        Player player = new Player();
        player.startDeathAnimation();

        player.updateDeathAnimation(0.35f);

        assertEquals(1f, player.getDeathProgress(), 0.0001f);
    }

    @Test
    void setGroundedCubeStateShouldPlacePlayerOnGround() {
        Player player = new Player();
        player.changeState(new FlyState());
        player.jump(500f);
        player.moveVertical(0.2f);

        player.setGroundedCubeState();

        assertEquals(GameWorld.GROUND_Y, player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertEquals("CUBE", player.getState().getName());
    }

    @Test
    void jumpShouldSetVerticalVelocity() {
        Player player = new Player();

        player.jump(650f);

        assertEquals(650f, player.getVelocityY(), 0.0001f);
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

        assertEquals(GameWorld.GROUND_Y, player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertEquals(0f, player.getRotation(), 0.0001f);
    }

    @Test
    void clampToFlyBoundsShouldKeepPlayerInsidePlayableArea() {
        Player player = new Player();
        player.landOn(600f);
        player.setVelocityY(200f);

        player.clampToFlyBounds();

        assertTrue(player.getY() <= GameWorld.SCREEN_HEIGHT - 36f - player.getSize());
    }

    @Test
    void snapToCeilingShouldPlacePlayerAtCeiling() {
        // Arrange
        Player player = new Player();

        player.snapToCeiling();

        assertEquals(GameWorld.SCREEN_HEIGHT - 36f - player.getSize(), player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
    }

    @Test
    void clampToCeilingShouldLockPlayerToCeilingAndRotate() {
        Player player = new Player();
        player.snapToCeiling();
        player.addForce(0.2f, 100f);

        player.clampToCeiling();

        assertEquals(GameWorld.SCREEN_HEIGHT - 36f - player.getSize(), player.getY(), 0.0001f);
        assertEquals(0f, player.getVelocityY(), 0.0001f);
        assertEquals(180f, player.getRotation(), 0.0001f);
    }

    @Test
    void changeStateShouldUpdateFlyingAndInvertedFlags() {
        Player player = new Player();

        player.changeState(new FlyState());

        assertTrue(player.isFlying());
        assertFalse(player.isInverted());

        player.changeState(new InvertedCubeState());

        assertFalse(player.isFlying());
        assertTrue(player.isInverted());
    }

    @Test
    void handleInputShouldStorePressedState() {
        Player player = new Player();

        player.handleInput(true, false);


        assertTrue(player.isInputPressed());
    }
}
