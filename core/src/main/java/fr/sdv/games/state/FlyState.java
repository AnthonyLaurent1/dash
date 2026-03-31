package fr.sdv.games.state;

import fr.sdv.games.entity.Player;

public class FlyState implements PlayerState {
    private static final float GRAVITY = 1800f;
    private static final float FLY_FORCE = 1700f;

    @Override
    public void enter(Player player) {
        player.setRotation(0f);
    }

    @Override
    public void update(Player player, float delta) {
        if (player.isInputPressed()) {
            player.addForce(delta, FLY_FORCE);
        } else {
            player.applyGravity(delta, GRAVITY);
        }

        if (player.getVelocityY() > 420f) {
            player.setVelocityY(420f);
        }
        if (player.getVelocityY() < -420f) {
            player.setVelocityY(-420f);
        }

        player.moveVertical(delta);
        player.setRotation(player.getVelocityY() * 0.08f);
        player.clampToFlyBounds();
    }

    @Override
    public void handleInput(Player player, boolean pressed, boolean justPressed) {
    }

    @Override
    public String getName() {
        return "FLY";
    }
}
