package fr.sdv.games.state;

import fr.sdv.games.entity.Player;

public class DeadState implements PlayerState {
    @Override
    public void enter(Player player) {
        player.setVelocityY(0f);
    }

    @Override
    public void update(Player player, float delta) {
    }

    @Override
    public void handleInput(Player player, boolean pressed, boolean justPressed) {
    }

    @Override
    public String getName() {
        return "DEAD";
    }
}
