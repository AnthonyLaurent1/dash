package fr.sdv.games.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Player;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.world.GameWorld;

/**
 * Dessine l'integralite du monde de jeu en primitives simples.
 *
 * <p>Le rendu reprend une palette tres contrastée inspiree du visuel de reference:
 * fond violet profond, obstacles clairs cernes et portails tres lumineux afin de
 * rendre la lecture du niveau immediate.</p>
 */
public class WorldRenderer {
    private static final Color SKY_BASE = new Color(0.09f, 0.04f, 0.30f, 1f);
    private static final Color SKY_BAND = new Color(0.16f, 0.08f, 0.46f, 1f);
    private static final Color SKY_TOP = new Color(0.22f, 0.12f, 0.60f, 1f);
    private static final Color GLOW = new Color(0.44f, 0.26f, 0.95f, 0.24f);
    private static final Color HORIZON = new Color(0.12f, 0.05f, 0.24f, 1f);
    private static final Color GROUND_BASE = new Color(0.07f, 0.04f, 0.12f, 1f);
    private static final Color GROUND_STRIP = new Color(0.74f, 0.60f, 1.00f, 1f);
    private static final Color GROUND_ACCENT = new Color(0.98f, 0.96f, 1.00f, 1f);
    private static final Color BLOCK_OUTLINE = new Color(0.90f, 0.90f, 0.98f, 1f);
    private static final Color BLOCK_SHELL = new Color(0.10f, 0.10f, 0.14f, 1f);
    private static final Color BLOCK_CORE = new Color(0.86f, 0.88f, 0.96f, 1f);
    private static final Color SPIKE_SHELL = new Color(0.95f, 0.95f, 1.00f, 1f);
    private static final Color SPIKE_CORE = new Color(0.72f, 0.74f, 0.84f, 1f);

    private final GameWorld world;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    /**
     * Initialise le renderer associe au monde courant.
     *
     * @param world monde de jeu source pour les obstacles, portails et le joueur
     */
    public WorldRenderer(GameWorld world) {
        this.world = world;
        this.shapeRenderer = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT, camera);
        this.viewport.apply();
    }

    /**
     * Lance un frame complet de rendu.
     */
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

    /**
     * Dessine le decor de fond et les silhouettes d'horizon.
     */
    private void drawBackground() {
        shapeRenderer.setColor(SKY_BASE);
        shapeRenderer.rect(0, 0, GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT);

        shapeRenderer.setColor(SKY_BAND);
        shapeRenderer.rect(0, 180, GameWorld.SCREEN_WIDTH, 190);

        shapeRenderer.setColor(SKY_TOP);
        shapeRenderer.rect(0, 360, GameWorld.SCREEN_WIDTH, 180);

        shapeRenderer.setColor(GLOW);
        shapeRenderer.rect(40, 280, 220, 120);
        shapeRenderer.rect(300, 340, 170, 88);
        shapeRenderer.rect(650, 250, 230, 104);

        shapeRenderer.setColor(HORIZON);
        shapeRenderer.rect(0, GameWorld.GROUND_Y + 8, GameWorld.SCREEN_WIDTH, 20);
        shapeRenderer.rect(45, GameWorld.GROUND_Y + 28, 56, 84);
        shapeRenderer.rect(130, GameWorld.GROUND_Y + 28, 28, 132);
        shapeRenderer.rect(208, GameWorld.GROUND_Y + 28, 64, 68);
        shapeRenderer.rect(330, GameWorld.GROUND_Y + 28, 34, 156);
        shapeRenderer.rect(426, GameWorld.GROUND_Y + 28, 82, 92);
        shapeRenderer.rect(584, GameWorld.GROUND_Y + 28, 42, 138);
        shapeRenderer.rect(694, GameWorld.GROUND_Y + 28, 74, 82);
        shapeRenderer.rect(834, GameWorld.GROUND_Y + 28, 48, 124);
    }

    /**
     * Dessine le sol principal et son liseré lumineux.
     */
    private void drawGround() {
        shapeRenderer.setColor(GROUND_BASE);
        shapeRenderer.rect(0, 0, GameWorld.SCREEN_WIDTH, GameWorld.GROUND_Y);

        shapeRenderer.setColor(GROUND_STRIP);
        shapeRenderer.rect(0, GameWorld.GROUND_Y - 10f, GameWorld.SCREEN_WIDTH, 10f);

        shapeRenderer.setColor(new Color(0.12f, 0.08f, 0.20f, 1f));
        for (int x = 0; x < GameWorld.SCREEN_WIDTH; x += 52) {
            shapeRenderer.rect(x, 16, 24, 18);
        }

        shapeRenderer.setColor(GROUND_ACCENT);
        shapeRenderer.rect(0, GameWorld.GROUND_Y, GameWorld.SCREEN_WIDTH, 8f);
    }

    /**
     * Dessine les portails de transition avec une bordure tres visible.
     */
    private void drawPortals() {
        for (Portal portal : world.getLevel().getPortals()) {
            Color outer;
            Color inner;

            switch (portal.getType()) {
                case FLY:
                    outer = new Color(0.94f, 0.48f, 0.86f, 0.56f);
                    inner = new Color(1f, 0.90f, 0.98f, 0.94f);
                    break;
                case CUBE:
                    outer = new Color(0.40f, 0.78f, 1.00f, 0.50f);
                    inner = new Color(0.92f, 0.98f, 1.00f, 0.94f);
                    break;
                case INVERT_ON:
                    outer = new Color(0.98f, 0.43f, 0.72f, 0.54f);
                    inner = new Color(1f, 0.92f, 0.96f, 0.94f);
                    break;
                case INVERT_OFF:
                default:
                    outer = new Color(0.98f, 0.72f, 0.26f, 0.54f);
                    inner = new Color(1f, 0.96f, 0.86f, 0.94f);
                    break;
            }

            shapeRenderer.setColor(new Color(0.96f, 0.90f, 1f, 0.92f));
            shapeRenderer.rect(portal.getX() - 6, portal.getY() - 2, portal.getWidth() + 12, portal.getHeight() + 4);

            shapeRenderer.setColor(outer);
            shapeRenderer.rect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());

            shapeRenderer.setColor(new Color(0.07f, 0.04f, 0.12f, 0.96f));
            shapeRenderer.rect(portal.getX() + 6, portal.getY() + 10, portal.getWidth() - 12, portal.getHeight() - 20);

            shapeRenderer.setColor(inner);
            shapeRenderer.rect(portal.getX() + 12, portal.getY() + 18, portal.getWidth() - 24, portal.getHeight() - 36);
        }
    }

    /**
     * Dessine tous les obstacles du niveau selon leur type.
     */
    private void drawObstacles() {
        for (Obstacle obstacle : world.getLevel().getObstacles()) {
            switch (obstacle.getType()) {
                case BLOCK:
                    drawBlock(obstacle, BLOCK_SHELL, BLOCK_CORE);
                    break;
                case GHOST_BLOCK:
                    drawBlock(obstacle, new Color(0.10f, 0.10f, 0.14f, 0.55f), new Color(0.86f, 0.88f, 0.96f, 0.55f));
                    break;
                case TRAP_BLOCK:
                    drawBlock(obstacle, new Color(0.40f, 0.08f, 0.18f, 1f), new Color(0.98f, 0.52f, 0.68f, 1f));
                    break;
                case FLY_BLOCK:
                    drawBlock(obstacle, new Color(0.12f, 0.08f, 0.22f, 1f), new Color(0.82f, 0.86f, 1.00f, 1f));
                    break;
                case FRAGILE_BLOCK:
                    if (!obstacle.isBroken()) {
                        if (obstacle.isBreaking()) {
                            drawBlock(obstacle, new Color(0.38f, 0.24f, 0.08f, 1f), new Color(0.98f, 0.84f, 0.34f, 1f));
                        } else {
                            drawBlock(obstacle, new Color(0.30f, 0.28f, 0.08f, 1f), new Color(0.96f, 0.94f, 0.42f, 1f));
                        }
                    }
                    break;
                case SPIKE:
                case FLY_SPIKE_BOTTOM:
                    drawBottomSpike(obstacle, SPIKE_SHELL, SPIKE_CORE);
                    break;
                case FLY_SPIKE_TOP:
                    drawTopSpike(obstacle, SPIKE_SHELL, SPIKE_CORE);
                    break;
            }
        }
    }

    /**
     * Dessine un bloc rectangulaire avec contour et coeur clair.
     */
    private void drawBlock(Obstacle obstacle, Color outer, Color inner) {
        shapeRenderer.setColor(BLOCK_OUTLINE);
        shapeRenderer.rect(obstacle.getX() - 4, obstacle.getY() - 4, obstacle.getWidth() + 8, obstacle.getHeight() + 8);

        shapeRenderer.setColor(outer);
        shapeRenderer.rect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());

        shapeRenderer.setColor(inner);
        shapeRenderer.rect(obstacle.getX() + 5, obstacle.getY() + 5, obstacle.getWidth() - 10, obstacle.getHeight() - 10);

        shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.22f));
        shapeRenderer.rect(obstacle.getX() + 5, obstacle.getY() + obstacle.getHeight() - 11, obstacle.getWidth() - 10, 3);
    }

    /**
     * Dessine un pic oriente vers le haut.
     */
    private void drawBottomSpike(Obstacle obstacle, Color base, Color highlight) {
        shapeRenderer.setColor(new Color(0.16f, 0.12f, 0.28f, 1f));
        shapeRenderer.triangle(
            obstacle.getX() - 3, obstacle.getY() - 3,
            obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() + obstacle.getHeight() + 3,
            obstacle.getX() + obstacle.getWidth() + 3, obstacle.getY() - 3
        );

        shapeRenderer.setColor(base);
        shapeRenderer.triangle(
            obstacle.getX(), obstacle.getY(),
            obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() + obstacle.getHeight(),
            obstacle.getX() + obstacle.getWidth(), obstacle.getY()
        );

        shapeRenderer.setColor(highlight);
        shapeRenderer.triangle(
            obstacle.getX() + obstacle.getWidth() * 0.24f, obstacle.getY() + obstacle.getHeight() * 0.08f,
            obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() + obstacle.getHeight() * 0.66f,
            obstacle.getX() + obstacle.getWidth() * 0.76f, obstacle.getY() + obstacle.getHeight() * 0.08f
        );
    }

    /**
     * Dessine un pic suspendu oriente vers le bas.
     */
    private void drawTopSpike(Obstacle obstacle, Color base, Color highlight) {
        shapeRenderer.setColor(new Color(0.16f, 0.12f, 0.28f, 1f));
        shapeRenderer.triangle(
            obstacle.getX() - 3, obstacle.getY() + 3,
            obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() - obstacle.getHeight() - 3,
            obstacle.getX() + obstacle.getWidth() + 3, obstacle.getY() + 3
        );

        shapeRenderer.setColor(base);
        shapeRenderer.triangle(
            obstacle.getX(), obstacle.getY(),
            obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() - obstacle.getHeight(),
            obstacle.getX() + obstacle.getWidth(), obstacle.getY()
        );

        shapeRenderer.setColor(highlight);
        shapeRenderer.triangle(
            obstacle.getX() + obstacle.getWidth() * 0.24f, obstacle.getY() - obstacle.getHeight() * 0.08f,
            obstacle.getX() + obstacle.getWidth() / 2f, obstacle.getY() - obstacle.getHeight() * 0.66f,
            obstacle.getX() + obstacle.getWidth() * 0.76f, obstacle.getY() - obstacle.getHeight() * 0.08f
        );
    }

    /**
     * Dessine le joueur dans son etat courant, y compris l'animation de mort.
     */
    private void drawPlayer() {
        Player player = world.getPlayer();

        if (player.isDead() && player.isDeathAnimating()) {
            float progress = player.getDeathProgress();

            float centerX = player.getX() + player.getSize() / 2f;
            float centerY = player.getY() + player.getSize() / 2f;

            float size = player.getSize() * (1f + progress * 1.2f);
            float alpha = 1f - progress;

            shapeRenderer.setColor(new Color(0.99f, 0.95f, 0.86f, alpha));
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

            shapeRenderer.setColor(new Color(0.90f, 0.38f, 0.25f, alpha * 0.85f));
            shapeRenderer.rect(centerX - 6 - progress * 18f, centerY + progress * 10f, 10, 10);
            shapeRenderer.rect(centerX + 8 + progress * 18f, centerY - progress * 8f, 10, 10);
            shapeRenderer.rect(centerX - progress * 12f, centerY + 12 + progress * 18f, 10, 10);
            shapeRenderer.rect(centerX - progress * 10f, centerY - 18 - progress * 14f, 10, 10);
            return;
        }

        if (player.isDead()) {
            return;
        }

        if (player.isFlying()) {
            drawRocket(player);
            return;
        }

        drawCube(player);
    }

    /**
     * Dessine le cube avec une palette jaune, cyan et creme.
     */
    private void drawCube(Player player) {
        shapeRenderer.setColor(new Color(0.92f, 1f, 0.34f, 1f));
        shapeRenderer.rect(
            player.getX() - 5,
            player.getY() - 5,
            player.getSize() / 2f + 5,
            player.getSize() / 2f + 5,
            player.getSize() + 10,
            player.getSize() + 10,
            1f,
            1f,
            player.getRotation()
        );

        shapeRenderer.setColor(new Color(0.17f, 0.72f, 1.00f, 1f));
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

        shapeRenderer.setColor(new Color(0.96f, 0.95f, 0.56f, 1f));
        shapeRenderer.rect(
            player.getX() + 5,
            player.getY() + 5,
            player.getSize() / 2f - 5,
            player.getSize() / 2f - 5,
            player.getSize() - 10,
            player.getSize() - 10,
            1f,
            1f,
            player.getRotation()
        );
    }

    /**
     * Dessine le vaisseau avec ses ailerons et sa flamme.
     */
    private void drawRocket(Player player) {
        float x = player.getX();
        float y = player.getY();
        float size = player.getSize();
        float centerY = y + size * 0.5f;
        float noseX = x + size + 2f;
        float tailX = x + 4f;
        float wingX = x + size * 0.35f;
        float exhaustLength = 10f + Math.min(16f, Math.abs(player.getVelocityY()) * 0.02f);

        shapeRenderer.setColor(new Color(0.94f, 0.48f, 0.86f, 1f));
        shapeRenderer.triangle(tailX - 4f, centerY - 12f, noseX + 4f, centerY, tailX - 4f, centerY + 12f);

        shapeRenderer.setColor(new Color(0.92f, 0.98f, 1.00f, 1f));
        shapeRenderer.triangle(tailX, centerY - 10f, noseX, centerY, tailX, centerY + 10f);

        shapeRenderer.setColor(new Color(0.24f, 0.76f, 1.00f, 1f));
        shapeRenderer.triangle(wingX, centerY - 8f, noseX - 6f, centerY, wingX, centerY + 8f);

        shapeRenderer.setColor(new Color(0.97f, 0.95f, 0.56f, 1f));
        shapeRenderer.rect(x + 10f, centerY - 4f, 8f, 8f);

        shapeRenderer.setColor(new Color(0.90f, 0.30f, 0.62f, 1f));
        shapeRenderer.triangle(tailX, centerY - 8f, x - 5f, y + 2f, tailX + 4f, centerY - 2f);
        shapeRenderer.triangle(tailX, centerY + 8f, x - 5f, y + size - 2f, tailX + 4f, centerY + 2f);

        shapeRenderer.setColor(new Color(1f, 0.96f, 0.68f, 0.95f));
        shapeRenderer.triangle(x - exhaustLength, centerY, tailX + 1f, centerY - 5f, tailX + 1f, centerY + 5f);

        shapeRenderer.setColor(new Color(1f, 0.62f, 0.20f, 0.90f));
        shapeRenderer.triangle(x - exhaustLength * 0.65f, centerY, tailX + 1f, centerY - 3f, tailX + 1f, centerY + 3f);
    }

    /**
     * Affiche le bouton de relance une fois mort ou victoire atteinte.
     */
    private void drawRestartButton() {
        if (!world.getPlayer().isDead() && !world.isVictory()) {
            return;
        }

        var button = world.getRestartButton();
        var bounds = button.getBounds();

        shapeRenderer.setColor(new Color(0.16f, 0.09f, 0.30f, 1f));
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        shapeRenderer.setColor(new Color(0.96f, 0.95f, 0.56f, 1f));
        shapeRenderer.rect(bounds.x + 8, bounds.y + 8, bounds.width - 16, bounds.height - 16);
    }

    /**
     * Met a jour le viewport en cas de changement de taille de fenetre.
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Libere les ressources GPU allouees par le renderer.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}
