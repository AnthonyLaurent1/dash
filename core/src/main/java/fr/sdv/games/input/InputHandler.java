package fr.sdv.games.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import fr.sdv.games.world.GameWorld;

/**
 * Centralise la traduction des entrees clavier et souris en actions de jeu.
 */
public class InputHandler {
    /**
     * Lit les entrees du frame courant et les applique au monde.
     *
     * @param world monde a piloter
     */
    public void update(GameWorld world) {
        boolean pressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        boolean justPressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        world.getPlayer().handleInput(pressed, justPressed);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            world.reset();
            return;
        }

        if (Gdx.input.justTouched()) {
            Vector2 click = new Vector2(
                Gdx.input.getX(),
                GameWorld.SCREEN_HEIGHT - Gdx.input.getY()
            );
            world.clickRestart(click);
        }
    }
}
