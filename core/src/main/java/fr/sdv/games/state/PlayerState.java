package fr.sdv.games.state;

import fr.sdv.games.entity.Player;

/**
 * Contrat commun des etats de controle du joueur.
 */
public interface PlayerState {
    /**
     * Appele une fois lors de l'entree dans l'etat.
     */
    void enter(Player player);

    /**
     * Met a jour le comportement de l'etat sur un frame.
     */
    void update(Player player, float delta);

    /**
     * Recoit les entrees du frame courant.
     */
    void handleInput(Player player, boolean pressed, boolean justPressed);

    /**
     *  nom court de l'etat pour debug et UI
     */
    String getName();
}
