package fr.sdv.games.state;

import fr.sdv.games.entity.Player;

/**
 * Etat vaisseau avec poussee continue tant que l'entree est maintenue.
 */
public class FlyState implements PlayerState {
    private static final float GRAVITY = 1800f;
    private static final float FLY_FORCE = 1700f;

    /**
     * Reinitialise l'inclinaison du vaisseau a l'entree dans le mode.
     */
    @Override
    public void enter(Player player) {
        player.setRotation(0f);
    }

    /**
     * Met a jour poussée, gravite, inclinaison et limites de vol.
     */
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

    /**
     * Aucune action immediate: l'etat lit directement {@link Player#isInputPressed()}.
     */
    @Override
    public void handleInput(Player player, boolean pressed, boolean justPressed) {
    }

    /**
     *  identifiant textuel de l'etat
     */
    @Override
    public String getName() {
        return "FLY";
    }
}
