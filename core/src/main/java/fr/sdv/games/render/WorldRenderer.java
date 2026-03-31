package fr.sdv.games.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Player;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.world.GameWorld;

public class WorldRenderer {
    private final GameWorld world;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    public WorldRenderer(GameWorld world) {
        this.world = world;
        this.shapeRenderer = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT, camera);
        this.viewport.apply();
    }

    public void render() {
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawBackground();
        drawGround();
        drawPortals();
        drawObstacles();
        drawPlayer();
        drawRestartButton();
        shapeRenderer.end();
    }

    private void drawBackground() {
        shapeRenderer.setColor(new Color(0.12f, 0.15f, 0.24f, 1f));
        shapeRenderer.rect(0, 0, GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT);

        shapeRenderer.setColor(new Color(0.18f, 0.22f, 0.34f, 1f));
        shapeRenderer.rect(0, 330, GameWorld.SCREEN_WIDTH, 120);
    }

    private void drawGround() {
        shapeRenderer.setColor(new Color(0.20f, 0.85f, 0.70f, 1f));
        shapeRenderer.rect(0, 0, GameWorld.SCREEN_WIDTH, GameWorld.GROUND_Y);

        shapeRenderer.setColor(new Color(0.10f, 0.55f, 0.46f, 1f));
        shapeRenderer.rect(0, GameWorld.GROUND_Y, GameWorld.SCREEN_WIDTH, 8f);
    }

    private void drawPortals() {
        for (Portal portal : world.getLevel().getPortals()) {
            shapeRenderer.setColor(new Color(0.7f, 0.2f, 1f, 0.35f));
            shapeRenderer.rect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());

            shapeRenderer.setColor(new Color(0.95f, 0.55f, 1f, 0.8f));
            shapeRenderer.rect(portal.getX() + 6, portal.getY() + 8, portal.getWidth() - 12, portal.getHeight() - 16);
        }
    }

    private void drawObstacles() {
        for (Obstacle obstacle : world.getLevel().getObstacles()) {
            switch (obstacle.getType()) {
                case BLOCK:
                case GHOST_BLOCK:
                case TRAP_BLOCK:
                case FLY_BLOCK:
                    shapeRenderer.setColor(new Color(0.55f, 0.85f, 1f, 1f));
                    shapeRenderer.rect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
                    break;

                case FRAGILE_BLOCK:
                    if (!obstacle.isBroken()) {
                        if (obstacle.isBreaking()) {
                            shapeRenderer.setColor(new Color(1f, 0.75f, 0.35f, 1f));
                        } else {
                            shapeRenderer.setColor(new Color(0.95f, 0.85f, 0.45f, 1f));
                        }
                        shapeRenderer.rect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
                    }
                    break;

                case SPIKE:
                case FLY_SPIKE_BOTTOM:
                    shapeRenderer.setColor(new Color(1f, 0.35f, 0.45f, 1f));
                    shapeRenderer.triangle(
                        obstacle.getX(), obstacle.getY(),
                        obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() + obstacle.getHeight(),
                        obstacle.getX() + obstacle.getWidth(), obstacle.getY()
                    );
                    break;

                case FLY_SPIKE_TOP:
                    shapeRenderer.setColor(new Color(1f, 0.45f, 0.55f, 1f));
                    shapeRenderer.triangle(
                        obstacle.getX(), obstacle.getY(),
                        obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() - obstacle.getHeight(),
                        obstacle.getX() + obstacle.getWidth(), obstacle.getY()
                    );
                    break;
            }
        }
    }


    private void drawPlayer() {
        Player player = world.getPlayer();

        if (player.isDead() && player.isDeathAnimating()) {
            float progress = player.getDeathProgress();

            float centerX = player.getX() + player.getSize() / 2f;
            float centerY = player.getY() + player.getSize() / 2f;

            float size = player.getSize() * (1f + progress * 1.2f);
            float alpha = 1f - progress;

            shapeRenderer.setColor(new Color(1f, 0.9f, 0.2f, alpha));
            shapeRenderer.rect(
                centerX - size / 2f,
                centerY - size / 2f,
                size / 2f,
                size / 2f,
                size,
                size,
                1f,
                1f,
                player.getRotation() + progress * 220f
            );

            shapeRenderer.setColor(new Color(1f, 0.4f, 0.2f, alpha * 0.8f));
            shapeRenderer.rect(centerX - 6 - progress * 18f, centerY + progress * 10f, 10, 10);
            shapeRenderer.rect(centerX + 8 + progress * 18f, centerY - progress * 8f, 10, 10);
            shapeRenderer.rect(centerX - progress * 12f, centerY + 12 + progress * 18f, 10, 10);
            shapeRenderer.rect(centerX - progress * 10f, centerY - 18 - progress * 14f, 10, 10);
            return;
        }

        if (player.isDead()) {
            return;
        }

        shapeRenderer.setColor(new Color(1f, 0.9f, 0.2f, 1f));
        shapeRenderer.rect(
            player.getX(),
            player.getY(),
            player.getSize() / 2f,
            player.getSize() / 2f,
            player.getSize(),
            player.getSize(),
            1f,
            1f,
            player.getRotation()
        );
    }

    private void drawRestartButton() {
        if (!world.getPlayer().isDead() && !world.isVictory()) {
            return;
        }

        var button = world.getRestartButton();
        var bounds = button.getBounds();

        shapeRenderer.setColor(new Color(0.22f, 0.52f, 0.92f, 1f));
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        shapeRenderer.setColor(new Color(0.92f, 0.96f, 1f, 1f));
        shapeRenderer.rect(bounds.x + 8, bounds.y + 8, bounds.width - 16, bounds.height - 16);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
