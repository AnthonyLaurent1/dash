package fr.sdv.games.world;

import com.badlogic.gdx.math.Vector2;
import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Player;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.entity.RestartButton;
import fr.sdv.games.state.CubeState;
import fr.sdv.games.state.DeadState;
import fr.sdv.games.state.FlyState;
import fr.sdv.games.state.InvertedCubeState;

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
            if (player.getBounds().overlaps(portal.getBounds())) {
                switch (portal.getType()) {
                    case FLY:
                        if (!player.isFlying()) {
                            player.changeState(new FlyState());
                        }
                        break;

                    case CUBE:
                        if (player.isFlying()) {
                            player.changeState(new CubeState());
                            player.landOn(GROUND_Y);
                        }
                        break;

                    case INVERT_ON:
                        if (!"INVERTED".equals(player.getState().getName())) {
                            player.changeState(new InvertedCubeState());
                        }
                        break;

                    case INVERT_OFF:
                        if ("INVERTED".equals(player.getState().getName())) {
                            player.changeState(new CubeState());
                            player.landOn(GROUND_Y);
                        }
                        break;
                }
            }
        }
    }

    /**
     * Verifie toutes les collisions entre joueur et obstacles.
     */
    private void checkObstacles() {
        for (Obstacle obstacle : level.getObstacles()) {
            if (!obstacle.isSolid()) {
                continue;
            }

            switch (obstacle.getType()) {
                case SPIKE:
                case FLY_SPIKE_TOP:
                case FLY_SPIKE_BOTTOM:
                    if (player.getBounds().overlaps(obstacle.getDangerBounds())) {
                        player.startDeathAnimation();
                        player.changeState(new DeadState());
                        return;
                    }
                    break;

                case BLOCK:
                case FRAGILE_BLOCK:
                case GHOST_BLOCK:
                case FLY_BLOCK:
                    if (player.getBounds().overlaps(obstacle.getBounds())) {
                        resolveBlockCollision(obstacle);
                        if (player.isDead()) {
                            return;
                        }
                    }
                    break;

                case TRAP_BLOCK:
                    if (player.getBounds().overlaps(obstacle.getBounds())) {
                        obstacle.triggerBreak(0.08f);
                    }
                    break;
            }
        }
    }

    /**
     * Resout les collisions avec les blocs selon le mode courant du joueur.
     */
    private void resolveBlockCollision(Obstacle obstacle) {
        if (player.isFlying()) {
            player.startDeathAnimation();
            player.changeState(new DeadState());
            return;
        }

        float playerLeft = player.getX();
        float playerRight = player.getX() + player.getSize();
        float playerBottom = player.getY();
        float playerTop = player.getY() + player.getSize();
        float playerCenterY = player.getY() + player.getSize() * 0.5f;
        float playerCenterX = player.getX() + player.getSize() * 0.5f;

        float blockLeft = obstacle.getX();
        float blockRight = obstacle.getX() + obstacle.getWidth();
        float blockTop = obstacle.getY() + obstacle.getHeight();
        float blockBottom = obstacle.getY();
        float overlapX = Math.min(playerRight, blockRight) - Math.max(playerLeft, blockLeft);
        float overlapY = Math.min(playerTop, blockTop) - Math.max(playerBottom, blockBottom);

        if ("INVERTED".equals(player.getState().getName())) {
            boolean horizontallyAligned =
                playerRight > blockLeft + 6f &&
                    playerLeft < blockRight - 6f;

            boolean rising = player.getVelocityY() >= -10f;
            boolean touchingUnderSide =
                rising &&
                    horizontallyAligned &&
                    playerTop >= blockBottom - 14f &&
                    playerTop <= blockBottom + 18f;

            if (touchingUnderSide) {
                player.landOn(blockBottom - player.getSize());

                if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
                    obstacle.triggerBreak(0.08f);
                }
                return;
            }

            if (obstacle.getType() != Obstacle.ObstacleType.GHOST_BLOCK
                && obstacle.getType() != Obstacle.ObstacleType.TRAP_BLOCK) {
                boolean insideBlockHeight =
                    playerCenterY > blockBottom + 2f &&
                        playerCenterY < blockTop - 2f;

                boolean deepOverlap = overlapX > 12f && overlapY > 12f;

                if (insideBlockHeight && deepOverlap) {
                    player.startDeathAnimation();
                    player.changeState(new DeadState());
                }
            }

            return;
        }

        boolean falling = player.getVelocityY() <= 0f;

        boolean horizontallyAligned;
        boolean landingOnTop;

        if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
            horizontallyAligned =
                playerCenterX > blockLeft + 12f &&
                    playerCenterX < blockRight - 12f;

            landingOnTop =
                falling &&
                    horizontallyAligned &&
                    playerBottom >= blockTop - 8f &&
                    playerBottom <= blockTop + 4f;
        } else {
            horizontallyAligned =
                playerRight > blockLeft + 2f &&
                    playerLeft < blockRight - 2f;

            landingOnTop =
                falling &&
                    horizontallyAligned &&
                    playerBottom >= blockTop - 18f &&
                    playerBottom <= blockTop + 10f;
        }

        if (landingOnTop) {
            player.landOn(blockTop);

            if (obstacle.getType() == Obstacle.ObstacleType.FRAGILE_BLOCK) {
                obstacle.triggerBreak(0.35f);
            }

            if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
                obstacle.triggerBreak(0.08f);
            }
            return;
        }

        if (obstacle.getType() == Obstacle.ObstacleType.GHOST_BLOCK
            || obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
            return;
        }

        boolean insideBlockHeight =
            playerCenterY > blockBottom + 2f &&
                playerCenterY < blockTop - 2f;

        if (insideBlockHeight) {
            player.startDeathAnimation();
            player.changeState(new DeadState());

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
     *
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
