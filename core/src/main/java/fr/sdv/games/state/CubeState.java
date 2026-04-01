package fr.sdv.games.state;

import fr.sdv.games.entity.Player;

/**
 * Etat standard de deplacement au sol avec saut simple.
 */
public class CubeState implements PlayerState {
    private static final float GRAVITY = 2300f;
    private static final float JUMP_FORCE = 650f;

    /**
     * Reinitialise la rotation du cube a l'entree.
     */
    @Override
    public void enter(Player player) {
        player.setRotation(0f);
    }

    /**
     * Applique gravite, rotation et collision avec le sol.
     */
    @Override
    public void update(Player player, float delta) {
        player.applyGravity(delta, GRAVITY);
        player.moveVertical(delta);

        if (!player.isGrounded()) {
            player.addRotation(-420f * delta);
        }

        player.clampToGround();
    }

    /**
     * Declenche un saut tant que le joueur est pose.
     */
    @Override
    public void handleInput(Player player, boolean pressed, boolean justPressed) {
        if (pressed && player.isGrounded()) {
            player.jump(JUMP_FORCE);
        }
    }

    /**
     * @return identifiant textuel de l'etat
     */
    @Override
    public String getName() {
        return "CUBE";
    }
}
