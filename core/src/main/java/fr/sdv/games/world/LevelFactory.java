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
        addTowerSection(level);
        addFlySection(level);
        addRecoverySection(level);
        addInvertedSection(level);
        addFinalSection(level);

        level.setFinishX(13880f);
        return level;
    }

    /**
     * Pose l'ouverture du niveau avec des sauts simples et des premiers empilements.
     */
    private static void addIntro(Level level) {
        addGroundSpike(level, 940);
        addGroundSpike(level, 1260);

        addBlock(level, 1570, 0);
        addGroundSpike(level, 1652);

        addBlock(level, 1890, 0);
        addBlock(level, 1975, 1);
        addGroundSpike(level, 2068);

        addGroundSpike(level, 2320);
        addGroundSpike(level, 2370);
        addBlock(level, 2550, 0);
        addBlock(level, 2635, 1);
        addGroundSpike(level, 2728);

        addBlock(level, 2975, 0);
        addBlock(level, 3058, 0);
        addGroundSpike(level, 3155);
        addGroundSpike(level, 3205);
    }

    /**
     * Ajoute une section cube plus verticale avec tours, paliers et pieges.
     */
    private static void addTowerSection(Level level) {
        addBlock(level, 3490, 0);
        addBlock(level, 3572, 1);
        addBlock(level, 3654, 2);
        addGroundSpike(level, 3748);

        addBlock(level, 3970, 0);
        addBlock(level, 4052, 1);
        addTrap(level, 4134, 2);
        addGroundSpike(level, 4228);

        addBlock(level, 4480, 0);
        addGroundSpike(level, 4565);
        addGroundSpike(level, 4615);
        addBlock(level, 4770, 0);
        addBlock(level, 4852, 1);
        addGroundSpike(level, 4945);

        addBlock(level, 5230, 0);
        addBlock(level, 5312, 0);
        addBlock(level, 5394, 1);
        addGroundSpike(level, 5488);

        level.addPortal(new Portal(5770, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.FLY));
    }

    /**
     * Construit le couloir vaisseau avec alternance de plafonds, sols et blocs flottants.
     */
    private static void addFlySection(Level level) {
        level.addObstacle(new Obstacle(6075, 150, 64, 108, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(6075, 384, 64, 58, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(6285, GameWorld.GROUND_Y, 34, 48, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(6285, GameWorld.SCREEN_HEIGHT - 36, 34, 48, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(6450, 128, 62, 92, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(6620, 334, 62, 92, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(6810, 220, 80, 76, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(6995, GameWorld.GROUND_Y, 34, 52, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(7050, GameWorld.SCREEN_HEIGHT - 36, 34, 52, ObstacleType.FLY_SPIKE_TOP));

        level.addObstacle(new Obstacle(7235, 156, 66, 118, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(7410, 398, 66, 44, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(7585, 290, 66, 86, ObstacleType.FLY_BLOCK));
        level.addObstacle(new Obstacle(7760, 112, 66, 92, ObstacleType.FLY_BLOCK));

        level.addObstacle(new Obstacle(7945, GameWorld.SCREEN_HEIGHT - 36, 34, 54, ObstacleType.FLY_SPIKE_TOP));
        level.addObstacle(new Obstacle(8020, GameWorld.GROUND_Y, 34, 54, ObstacleType.FLY_SPIKE_BOTTOM));
        level.addObstacle(new Obstacle(8165, 228, 88, 94, ObstacleType.FLY_BLOCK));

        level.addPortal(new Portal(8420, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.CUBE));
    }

    /**
     * Replace le joueur au sol avec une difficulte intermediaire avant inversion.
     */
    private static void addRecoverySection(Level level) {
        addBlock(level, 8755, 0);
        addGroundSpike(level, 8838);
        addBlock(level, 9050, 0);
        addBlock(level, 9132, 1);
        addGroundSpike(level, 9226);

        addGroundSpike(level, 9445);
        addGroundSpike(level, 9495);
        addBlock(level, 9670, 0);
        addBlock(level, 9752, 0);
        addBlock(level, 9834, 1);

        addGroundSpike(level, 10062);
        addBlock(level, 10240, 0);
        addTrap(level, 10322, 1);
        addGroundSpike(level, 10414);

        level.addPortal(new Portal(10660, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_ON));
    }

    /**
     * Ajoute la portion au plafond une fois la gravite inversee.
     */
    private static void addInvertedSection(Level level) {
        addCeilingBlock(level, 10945, 1);
        addCeilingSpike(level, 11038);
        addCeilingBlock(level, 11240, 1);
        addCeilingBlock(level, 11322, 2);

        addCeilingSpike(level, 11548);
        addCeilingBlock(level, 11720, 1);
        addCeilingTrap(level, 11802, 2);
        addCeilingBlock(level, 11884, 3);

        addCeilingSpike(level, 12124);
        addCeilingSpike(level, 12174);
        addCeilingBlock(level, 12330, 1);
        addCeilingBlock(level, 12412, 2);
        addCeilingBlock(level, 12494, 3);

        level.addPortal(new Portal(12740, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_OFF));
    }

    /**
     * Termine le niveau avec une derniere acceleration de difficultes en escalier.
     */
    private static void addFinalSection(Level level) {
        addBlock(level, 13070, 0);
        addBlock(level, 13152, 1);
        addGroundSpike(level, 13245);

        addGroundSpike(level, 13425);
        addGroundSpike(level, 13475);
        addBlock(level, 13620, 0);
        addBlock(level, 13702, 1);
        addBlock(level, 13784, 2);
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
