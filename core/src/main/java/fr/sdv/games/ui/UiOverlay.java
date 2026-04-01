package fr.sdv.games.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.sdv.games.entity.RestartButton;
import fr.sdv.games.world.GameWorld;

/**
 * Affiche l'interface de score, progression et redemarrage.
 */
public class UiOverlay {
    private final GameWorld world;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    /**
     * Cree l'overlay de l'UI lie au monde courant.
     */
    public UiOverlay(GameWorld world) {
        this.world = world;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(1.2f);
        this.shapeRenderer = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT, camera);
        this.viewport.apply();
    }

    /**
     * Dessine la progression, les textes de debug et les messages de fin.
     */
    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawProgressBar();
        shapeRenderer.end();

        batch.begin();
        font.setColor(new Color(0.18f, 0.12f, 0.14f, 1f));
        font.draw(batch, "Score: " + (int) world.getScore(), 20, GameWorld.SCREEN_HEIGHT - 20);
        font.draw(batch, "State: " + world.getPlayer().getState().getName(), 20, GameWorld.SCREEN_HEIGHT - 50);
        font.draw(batch, getProgressLabel(), GameWorld.SCREEN_WIDTH - 92, GameWorld.SCREEN_HEIGHT - 20);

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

    /**
     * Dessine la barre de progression basee sur l'avancement dans le niveau.
     */
    private void drawProgressBar() {
        float x = 210f;
        float y = GameWorld.SCREEN_HEIGHT - 32f;
        float width = 540f;
        float height = 12f;
        float progress = getProgress();

        shapeRenderer.setColor(new Color(0.24f, 0.18f, 0.20f, 0.9f));
        shapeRenderer.rect(x - 4f, y - 4f, width + 8f, height + 8f);

        shapeRenderer.setColor(new Color(0.96f, 0.88f, 0.76f, 1f));
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(new Color(0.88f, 0.40f, 0.28f, 1f));
        shapeRenderer.rect(x, y, width * progress, height);

        shapeRenderer.setColor(new Color(1f, 0.95f, 0.85f, 0.6f));
        shapeRenderer.rect(x, y + height - 3f, width * progress, 3f);
    }

    /**
     * Calcule la progression normalisee entre le debut et la ligne d'arrivee.
     */
    private float getProgress() {
        if (world.isEndlessMode()) {
            return 1f;
        }

        float initialFinish = Math.max(world.getLevel().getInitialFinishX(), 1f);
        float remaining = Math.max(world.getLevel().getFinishX(), 0f);
        return Math.min(1f, Math.max(0f, 1f - (remaining / initialFinish)));
    }

    /**
     * Retourne le libelle d'avancement adapte au mode tutoriel ou infini.
     */
    private String getProgressLabel() {
        if (world.isEndlessMode()) {
            return "INF";
        }
        return (int) (getProgress() * 100f) + "%";
    }

    /**
     * Met a jour le viewport de l'overlay.
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Libere les ressources graphiques de l'interface.
     */
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
