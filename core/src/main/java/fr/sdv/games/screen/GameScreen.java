package fr.sdv.games.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import fr.sdv.games.input.InputHandler;
import fr.sdv.games.render.WorldRenderer;
import fr.sdv.games.ui.UiOverlay;
import fr.sdv.games.world.GameWorld;
import fr.sdv.games.world.LevelFactory;

/**
 * Ecran principal qui orchestre logique, rendu monde et interface.
 */
public class GameScreen implements Screen {
    private GameWorld world;
    private WorldRenderer renderer;
    private InputHandler inputHandler;
    private UiOverlay uiOverlay;

    /**
     * Initialise le monde, le renderer et l'overlay lors de l'affichage de l'ecran.
     */
    @Override
    public void show() {
        world = new GameWorld(LevelFactory.createLevel1());
        renderer = new WorldRenderer(world);
        inputHandler = new InputHandler();
        uiOverlay = new UiOverlay(world);
    }

    /**
     * Met a jour les entrees, la simulation et le rendu pour un frame.
     *
     * @param delta temps ecoule depuis le frame precedent
     */
    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1f / 30f);

        inputHandler.update(world);
        world.update(delta);

        Gdx.gl.glClearColor(0.08f, 0.09f, 0.14f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        uiOverlay.render();
    }

    /**
     * Propage les changements de taille au renderer et a l'overlay.
     */
    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        renderer.resize(width, height);
        uiOverlay.resize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Libere les ressources allouees par l'ecran.
     */
    @Override
    public void dispose() {
        renderer.dispose();
        uiOverlay.dispose();
    }
}
