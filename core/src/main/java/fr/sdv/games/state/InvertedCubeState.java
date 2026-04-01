package fr.sdv.games.state;

import fr.sdv.games.entity.Player;
import fr.sdv.games.world.GameWorld;

/**
 * Variante du cube ou la gravite est inversee et le joueur se deplace depuis le plafond.
 */
public class InvertedCubeState implements PlayerState {
    private static final float GRAVITY = 2300f;
    private static final float JUMP_FORCE = 650f;

    /**
     * Place le joueur au plafond en entree d'etat.
     */
    @Override
    public void enter(Player player) {
        player.setRotation(180f);
        player.snapToCeiling();
    }

    /**
     * Applique la gravite inverse et la rotation correspondante.
     */
    @Override
    public void update(Player player, float delta) {
        player.addForce(delta, GRAVITY);
        player.moveVertical(delta);

        if (!player.isGrounded()) {
            player.addRotation(420f * delta);
        }

        player.clampToCeiling();
    }

    /**
     * Declenche un saut inverse quand le joueur est ancre au plafond.
     */
    @Override
    public void handleInput(Player player, boolean pressed, boolean justPressed) {
        if (pressed && player.isGrounded()) {
            player.jump(-JUMP_FORCE);
        }
    }

    /**
     * @return identifiant textuel de l'etat
     */
    @Override
    public String getName() {
        return "INVERTED";
    }
}
