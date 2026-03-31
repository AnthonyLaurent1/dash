package fr.sdv.games.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.sdv.games.entity.RestartButton;
import fr.sdv.games.world.GameWorld;

public class UiOverlay {
    private final GameWorld world;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    public UiOverlay(GameWorld world) {
        this.world = world;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(1.2f);
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT, camera);
        this.viewport.apply();
    }

    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Score: " + (int) world.getScore(), 20, GameWorld.SCREEN_HEIGHT - 20);
        font.draw(batch, "State: " + world.getPlayer().getState().getName(), 20, GameWorld.SCREEN_HEIGHT - 50);

        if (world.getPlayer().isDead()) {
            RestartButton button = world.getRestartButton();
            font.draw(batch, "Game Over", 405, 235);
            font.draw(batch, button.getText(), button.getBounds().x + 48, button.getBounds().y + 38);
        }

        if (world.isVictory()) {
            RestartButton button = world.getRestartButton();
            font.draw(batch, "Victoire !", 410, 235);
            font.draw(batch, button.getText(), button.getBounds().x + 48, button.getBounds().y + 38);
        }

        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
