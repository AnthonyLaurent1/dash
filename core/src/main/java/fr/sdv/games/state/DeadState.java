package fr.sdv.games.state;

import fr.sdv.games.entity.Player;

/**
 * Etat terminal du joueur apres collision mortelle.
 */
public class DeadState implements PlayerState {
    /**
     * Coupe toute vitesse residuelle lors de l'entree en mort.
     */
    @Override
    public void enter(Player player) {
        player.setVelocityY(0f);
    }

    /**
     * Aucun comportement actif pendant la mort.
     */
    @Override
    public void update(Player player, float delta) {
    }

    /**
     * Ignore les entrees une fois mort.
     */
    @Override
    public void handleInput(Player player, boolean pressed, boolean justPressed) {
    }

    /**
     * @return identifiant textuel de l'etat
     */
    @Override
    public String getName() {
        return "DEAD";
    }
}
