package fr.sdv.games.world;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.entity.Obstacle.ObstacleType;
import fr.sdv.games.entity.Portal.PortalType;

/**
 * Construit le niveau principal du jeu.
 *
 * <p>Le layout est organise en grandes sections successives pour garder un rythme
 * proche d'un niveau Geometry Dash classique: introduction lisible, montees en
 * blocs, couloir vaisseau, reprise au sol, passage inverse puis final.</p>
 */
public final class LevelFactory {
    private static final float TILE = 42f;
    private static final float SPIKE_WIDTH = 28f;
    private static final float SPIKE_HEIGHT = 38f;

    private LevelFactory() {
    }

    /**
     * Cree le niveau complet avec tous ses obstacles, portails et la position de fin.
     *
     * @return un niveau entier pret a etre injecte dans le monde de jeu
     */
    public static Level createLevel1() {
        Level level = new Level();

        addIntro(level);
        addStepSection(level);
        addFlySection(level);
        addRecoverySection(level);
        addInvertedSection(level);
        addFinalSection(level);

        level.setFinishX(17820f);
        return level;
    }

    /**
     * Pose l'ouverture du niveau avec des sauts simples et des premiers empilements.
     */
    private static void addIntro(Level level) {
        addGroundSpike(level, 940);
        addGroundSpike(level, 1320);

        addBlock(level, 1760, 0);
        addGroundSpike(level, 1888);

        addGroundSpike(level, 2240);
        addBlock(level, 2480, 0);
        addBlock(level, 2660, 1);
        addGroundSpike(level, 2740);
    }

    /**
     * Ajoute une section cube en marches, calibree pour la hauteur de saut reelle.
     */
    private static void addStepSection(Level level) {
        addBlock(level, 3140, 0);
        addBlock(level, 3325, 1);
        addGroundSpike(level, 3400);

        addBlock(level, 3690, 0);
        addGroundSpike(level, 3820);

        addGroundSpike(level, 4100);
        addGroundSpike(level, 4152);
        addBlock(level, 4380, 0);
        addBlock(level, 4560, 1);
        addGroundSpike(level, 4640);

        addBlock(level, 4820, 0);
        addTrap(level, 5000, 1);
        addGroundSpike(level, 5128);

        level.addPortal(new Portal(4910, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.FLY));
    }

    /**
     * Construit le couloir vaisseau avec alternance de plafonds, sols et blocs flottants.
     */
    private static void addFlySection(Level level) {
        level.addObstacle(new Obstacle(5280, 180, 54, 66, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(5280, 364, 54, 36, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(5560, GameWorld.GROUND_Y, 30, 34, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(5560, GameWorld.SCREEN_HEIGHT - 36, 30, 34, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(5800, 170, 54, 66, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(6060, 346, 54, 46, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(6310, 125, 56, 92, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(6510, 384, 56, 52, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(6710, 242, 72, 58, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(6935, GameWorld.GROUND_Y, 32, 46, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(6935, GameWorld.SCREEN_HEIGHT - 36, 32, 46, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(7160, 150, 58, 84, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(7345, 332, 58, 76, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(7540, 188, 60, 74, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(7725, 350, 60, 60, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(7935, GameWorld.GROUND_Y, 34, 52, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(8010, GameWorld.SCREEN_HEIGHT - 36, 34, 52, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(8180, 122, 64, 108, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(8370, 382, 64, 44, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(8565, 260, 70, 62, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(8765, 156, 60, 86, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(8960, 340, 60, 68, ObstacleType.FLY_BLOCK));

        level.addPortal(new Portal(9260, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.CUBE));
    }

    /**
     * Replace le joueur au sol avec une difficulte intermediaire avant inversion.
     */
    private static void addRecoverySection(Level level) {
        addBlock(level, 9620, 0);
        addGroundSpike(level, 9750);
        addBlock(level, 10010, 0);
        addBlock(level, 10190, 1);
        addGroundSpike(level, 10342);

        addGroundSpike(level, 10640);
        addGroundSpike(level, 10692);
        addBlock(level, 10920, 0);
        addTrap(level, 11100, 1);
        addGroundSpike(level, 11232);

        addBlock(level, 11500, 0);
        addBlock(level, 11680, 1);
        addTrap(level, 11860, 0);
        addGroundSpike(level, 11992);

        level.addPortal(new Portal(12300, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_ON));
    }

    /**
     * Ajoute la portion au plafond une fois la gravite inversee.
     */
    private static void addInvertedSection(Level level) {
        addCeilingBlock(level, 12640, 1);
        addCeilingSpike(level, 12770);
        addCeilingBlock(level, 13020, 1);
        addCeilingBlock(level, 13200, 2);

        addCeilingSpike(level, 13460);
        addCeilingBlock(level, 13680, 1);
        addCeilingTrap(level, 13860, 2);
        addCeilingBlock(level, 14040, 2);

        addCeilingSpike(level, 14300);
        addCeilingBlock(level, 14520, 1);
        addCeilingTrap(level, 14700, 1);

        level.addPortal(new Portal(15020, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_OFF));
    }

    /**
     * Termine le niveau avec une derniere acceleration de difficultes en escalier.
     */
    private static void addFinalSection(Level level) {
        addBlock(level, 15320, 0);
        addGroundSpike(level, 15448);

        addGroundSpike(level, 15720);
        addGroundSpike(level, 15772);
        addBlock(level, 15990, 0);
        addBlock(level, 16170, 1);
        addGroundSpike(level, 16324);

        addBlock(level, 16600, 0);
        addTrap(level, 16780, 1);
        addGroundSpike(level, 16908);

        addGroundSpike(level, 17220);
        addBlock(level, 17440, 0);
    }

    /**
     * Ajoute un bloc standard aligne sur la grille du sol.
     */
    private static void addBlock(Level level, float x, int heightSteps) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y + TILE * heightSteps, TILE, TILE, ObstacleType.BLOCK));
    }

    /**
     * Ajoute un bloc piege aligne sur la grille du sol.
     */
    private static void addTrap(Level level, float x, int heightSteps) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y + TILE * heightSteps, TILE, TILE, ObstacleType.TRAP_BLOCK));
    }

    /**
     * Ajoute un pic au sol.
     */
    private static void addGroundSpike(Level level, float x) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, SPIKE_WIDTH, SPIKE_HEIGHT, ObstacleType.SPIKE));
    }

    /**
     * Ajoute un bloc standard aligne sur la grille du plafond inverse.
     */
    private static void addCeilingBlock(Level level, float x, int depthSteps) {
        float y = GameWorld.SCREEN_HEIGHT - 36f - (TILE * depthSteps);
        level.addObstacle(new Obstacle(x, y, TILE, TILE, ObstacleType.BLOCK));
    }

    /**
     * Ajoute un bloc piege aligne sur la grille du plafond inverse.
     */
    private static void addCeilingTrap(Level level, float x, int depthSteps) {
        float y = GameWorld.SCREEN_HEIGHT - 36f - (TILE * depthSteps);
        level.addObstacle(new Obstacle(x, y, TILE, TILE, ObstacleType.TRAP_BLOCK));
    }

    /**
     * Ajoute un pic suspendu au plafond pour la section inversee.
     */
    private static void addCeilingSpike(Level level, float x) {
        level.addObstacle(new Obstacle(x, GameWorld.SCREEN_HEIGHT - 36f, SPIKE_WIDTH, SPIKE_HEIGHT, ObstacleType.FLY_SPIKE_TOP));
    }
}
