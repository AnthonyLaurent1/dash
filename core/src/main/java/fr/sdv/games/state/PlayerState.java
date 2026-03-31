package fr.sdv.games.state;

import fr.sdv.games.entity.Player;

public interface PlayerState {
    void enter(Player player);
    void update(Player player, float delta);
    void handleInput(Player player, boolean pressed, boolean justPressed);
    String getName();
}
