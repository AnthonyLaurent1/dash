package fr.sdv.games.state;

import fr.sdv.games.entity.Player;
import fr.sdv.games.world.GameWorld;

public class InvertedCubeState implements PlayerState {
    private static final float GRAVITY = 2300f;
    private static final float JUMP_FORCE = 650f;

    @Override
    public void enter(Player player) {
        player.setRotation(180f);
        player.snapToCeiling();
    }

    @Override
    public void update(Player player, float delta) {
        player.addForce(delta, GRAVITY);
        player.moveVertical(delta);

        if (!player.isGrounded()) {
            player.addRotation(420f * delta);
        }

        player.clampToCeiling();
    }

    @Override
    public void handleInput(Player player, boolean pressed, boolean justPressed) {
        if (pressed && player.isGrounded()) {
            player.jump(-JUMP_FORCE);
        }
    }

    @Override
    public String getName() {
        return "INVERTED";
    }
}
