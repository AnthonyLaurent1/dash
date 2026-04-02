package fr.sdv.games.world;

import com.badlogic.gdx.math.Vector2;
import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Player;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.entity.RestartButton;
import fr.sdv.games.state.FlyState;
import fr.sdv.games.state.InvertedCubeState;
import fr.sdv.games.world.endless.EndlessLevelGenerator;

/**
 * Contient l'etat complet de la partie et applique les regles de simulation.
 */
public class GameWorld {
    /**
     * Score a partir duquel le tutoriel laisse place au mode infini.
     */
    public static final float ENDLESS_START_SCORE = 500f;

    /**
     * Largeur logique de la zone de jeu.
     */
    public static final float SCREEN_WIDTH = 960f;

    /**
     * Hauteur logique de la zone de jeu.
     */
    public static final float SCREEN_HEIGHT = 540f;

    /**
     * Ordonnée du sol sur lequel evolue le joueur en mode normal.
     */
    public static final float GROUND_Y = 90f;

    /**
     * Vitesse de defilement horizontale appliquee au niveau.
     */
    public static final float WORLD_SPEED = 350;

    private final Player player;
    private Level level;
    private final RestartButton restartButton;
    private EndlessLevelGenerator endlessGenerator;
    private float score;
    private boolean endlessMode;
    private boolean victory;

    /**
     * Cree un monde avec son joueur, son niveau et son bouton de redemarrage.
     */
    public GameWorld(Level level) {
        this.player = new Player();
        this.level = level;
        this.restartButton = new RestartButton(340f, 130f, 280f, 60f, "Recommencer");
    }

    /**
     * Met a jour la simulation d'un frame.
     */
    public void update(float delta) {
        if (victory) {
            return;
        }

        if (player.isDead()) {
            player.updateDeathAnimation(delta);
            return;
        }

        player.update(delta);
        player.updateDeathAnimation(delta);
        level.update(delta, WORLD_SPEED);
        if (endlessMode) {
            endlessGenerator.update(level, score);
        }
        checkPortals();
        checkObstacles();
        score += delta * 10f;
        if (!endlessMode && score >= ENDLESS_START_SCORE) {
            startEndlessMode();
        }
        checkVictory();
    }

    /**
     * Gere les changements d'etat provoques par les portails traverses.
     */
    private void checkPortals() {
        for (Portal portal : level.getPortals()) {
            if (isPlayerTouchingPortal(portal)) {
                applyPortalEffect(portal);
            }
        }
    }

    /**
     * Indique si le joueur est en collision avec un portail.
     *
     * @param portal portail a verifier
     * @return {@code true} si le joueur touche ce portail
     */
    private boolean isPlayerTouchingPortal(Portal portal) {
        return player.getBounds().overlaps(portal.getBounds());
    }

    /**
     * Applique l'effet du portail touche sur l'etat du joueur.
     *
     * @param portal portail active
     */
    private void applyPortalEffect(Portal portal) {
        switch (portal.getType()) {
            case FLY:
                enterFlyMode();
                break;
            case CUBE:
                returnToNormalMode();
                break;
            case INVERT_ON:
                enterInvertedMode();
                break;
            case INVERT_OFF:
                exitInvertedMode();
                break;
        }
    }

    /**
     * Passe le joueur en mode vol s'il n'y est pas deja.
     */
    private void enterFlyMode() {
        if (!player.isFlying()) {
            player.changeState(new FlyState());
        }
    }

    /**
     * Replace le joueur en mode cube normal s'il etait en vol.
     */
    private void returnToNormalMode() {
        if (player.isFlying()) {
            player.setGroundedCubeState();
        }
    }

    /**
     * Passe le joueur en mode inverse s'il n'y est pas deja.
     */
    private void enterInvertedMode() {
        if (!player.isInverted()) {
            player.changeState(new InvertedCubeState());
        }
    }

    /**
     * Replace le joueur en mode cube normal s'il etait inverse.
     */
    private void exitInvertedMode() {
        if (player.isInverted()) {
            player.setGroundedCubeState();
        }
    }


    /**
     * Verifie toutes les collisions entre joueur et obstacles.
     */
    private void checkObstacles() {
        for (Obstacle obstacle : level.getObstacles()) {
            if (shouldSkipObstacle(obstacle)) {
                continue;
            }

            handleObstacleCollision(obstacle);

            if (player.isDead()) {
                return;
            }
        }
    }

    /**
     * Indique si un obstacle peut etre ignore pendant cette frame.
     *
     * @param obstacle obstacle a verifier
     * @return {@code true} si l'obstacle ne doit pas etre traite
     */
    private boolean shouldSkipObstacle(Obstacle obstacle) {
        return !obstacle.isSolid();
    }

    /**
     * Applique la logique de collision en fonction du type d'obstacle.
     *
     * @param obstacle obstacle actuellement teste
     */
    private void handleObstacleCollision(Obstacle obstacle) {
        switch (obstacle.getType()) {
            case SPIKE:
            case FLY_SPIKE_TOP:
            case FLY_SPIKE_BOTTOM:
                handleSpikeCollision(obstacle);
                break;

            case BLOCK:
            case FRAGILE_BLOCK:
            case GHOST_BLOCK:
            case FLY_BLOCK:
                handleBlockCollision(obstacle);
                break;

            case TRAP_BLOCK:
                handleTrapBlockCollision(obstacle);
                break;
        }
    }

    /**
     * Gere les collisions mortelles avec les pics.
     *
     * @param obstacle pic a tester
     */
    private void handleSpikeCollision(Obstacle obstacle) {
        if (player.getBounds().overlaps(obstacle.getDangerBounds())) {
            player.die();
        }
    }

    /**
     * Gere les collisions avec les blocs solides.
     *
     * @param obstacle bloc a tester
     */
    private void handleBlockCollision(Obstacle obstacle) {
        if (player.getBounds().overlaps(obstacle.getBounds())) {
            BlockCollisionResolver.resolve(player, obstacle);
        }
    }

    /**
     * Gere le comportement des blocs pieges.
     *
     * @param obstacle bloc piege a tester
     */
    private void handleTrapBlockCollision(Obstacle obstacle) {
        if (player.getBounds().overlaps(obstacle.getBounds())) {
            obstacle.triggerBreak(0.08f);
        }
    }


    /**
     * Valide la victoire quand la ligne d'arrivee a ete depassee.
     */
    private void checkVictory() {
        if (endlessMode) {
            return;
        }

        if (level.getFinishX() <= player.getX()) {
            victory = true;
        }
    }

    /**
     * Bascule la partie en mode infini une fois le tutoriel termine.
     */
    private void startEndlessMode() {
        endlessMode = true;

        float spawnStartX = Math.max(level.getFarthestX(), SCREEN_WIDTH) + 420f;
        endlessGenerator = new EndlessLevelGenerator(spawnStartX, player.getState().getName());
        endlessGenerator.update(level, score);
    }

    /**
     * Remet la partie a zero avec un nouveau niveau initial.
     */
    public void reset() {
        player.reset();
        score = 0f;
        endlessMode = false;
        endlessGenerator = null;
        victory = false;
        level = LevelFactory.createLevel1();
    }

    /**
     * Tente d'activer le bouton de redemarrage a une position donnee.
     *  {@code true} si la partie a effectivement ete relancee
     */
    public boolean clickRestart(Vector2 worldClick) {
        if (!player.isDead() && !victory) {
            return false;
        }

        if (restartButton.contains(worldClick.x, worldClick.y)) {
            reset();
            return true;
        }
        return false;
    }

    /**
     *  joueur courant
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *  niveau actuellement charge
     */
    public Level getLevel() {
        return level;
    }

    /**
     *  bouton de redemarrage de l'overlay
     */
    public RestartButton getRestartButton() {
        return restartButton;
    }

    /**
     *  score courant
     */
    public float getScore() {
        return score;
    }

    /**
     *  {@code true} si le mode infini est actif
     */
    public boolean isEndlessMode() {
        return endlessMode;
    }

    /**
     *  {@code true} si la fin du niveau a ete atteinte
     */
    public boolean isVictory() {
        return victory;
    }
}
