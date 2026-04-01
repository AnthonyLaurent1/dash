package fr.sdv.games.world;

import java.util.Random;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;
import fr.sdv.games.entity.Obstacle.ObstacleType;
import fr.sdv.games.entity.Portal.PortalType;

/**
 * Genere des segments infinis apres la partie tutoriel.
 */
public class EndlessLevelGenerator {
    private static final float TILE = 42f;
    private static final float SPIKE_WIDTH = 28f;
    private static final float SPIKE_HEIGHT = 38f;
    private static final float BUFFER_AHEAD = 2800f;
    private static final float DESPAWN_X = -220f;
    private static final float BASE_SAFE_GAP = 120f;

    private final Random random = new Random();
    private float cursorX;
    private SegmentMode mode;
    private int segmentsUntilSwitch;
    private float difficulty;

    private enum SegmentMode {
        CUBE,
        FLY,
        INVERTED
    }

    /**
     * Cree un generateur infini a partir de la position de spawn fournie.
     *
     * @param startX abscisse initiale a partir de laquelle commencer a poser les segments
     * @param initialState nom logique de l'etat joueur courant
     */
    public EndlessLevelGenerator(float startX, String initialState) {
        this.cursorX = startX;
        this.mode = fromState(initialState);
        this.segmentsUntilSwitch = mode == SegmentMode.FLY ? 1 : 2 + random.nextInt(2);
    }

    /**
     * Maintient une reserve de segments devant la camera et nettoie les anciens elements.
     */
    public void update(Level level, float score) {
        level.removeOffscreen(DESPAWN_X);
        difficulty = Math.min(1f, Math.max(0f, (score - GameWorld.ENDLESS_START_SCORE) / 900f));

        float farthestX = Math.max(level.getFarthestX(), GameWorld.SCREEN_WIDTH);
        cursorX = farthestX + lerp(BASE_SAFE_GAP, 72f, difficulty);

        while (level.getFarthestX() < GameWorld.SCREEN_WIDTH + BUFFER_AHEAD) {
            appendSegment(level);
        }
    }

    private void appendSegment(Level level) {
        if (segmentsUntilSwitch <= 0) {
            appendTransition(level);
            segmentsUntilSwitch = nextSegmentCount();
        }

        switch (mode) {
            case FLY:
                appendFlySegment(level);
                break;
            case INVERTED:
                appendInvertedSegment(level);
                break;
            case CUBE:
            default:
                appendCubeSegment(level);
                break;
        }

        segmentsUntilSwitch--;
    }

    private void appendTransition(Level level) {
        cursorX += lerp(130f, 90f, difficulty);

        switch (mode) {
            case CUBE:
                if (random.nextFloat() < lerp(0.45f, 0.55f, difficulty)) {
                    level.addPortal(new Portal(cursorX, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.FLY));
                    mode = SegmentMode.FLY;
                } else {
                    level.addPortal(new Portal(cursorX, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_ON));
                    mode = SegmentMode.INVERTED;
                }
                break;

            case FLY:
                if (difficulty > 0.35f && random.nextFloat() < 0.35f) {
                    level.addPortal(new Portal(cursorX, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.CUBE));
                    mode = SegmentMode.CUBE;
                } else {
                    level.addPortal(new Portal(cursorX, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.CUBE));
                    mode = SegmentMode.CUBE;
                }
                break;

            case INVERTED:
                level.addPortal(new Portal(cursorX, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_OFF));
                mode = SegmentMode.CUBE;
                break;
        }

        cursorX += lerp(120f, 86f, difficulty);
    }

    private void appendCubeSegment(Level level) {
        int pattern = random.nextInt(7);
        float spacingA = lerp(120f, 92f, difficulty);
        float spacingB = lerp(250f, 190f, difficulty);
        float spacingC = lerp(390f, 300f, difficulty);
        float tail = lerp(170f, 110f, difficulty);

        switch (pattern) {
            case 0:
                addGroundSpike(level, cursorX + spacingA);
                addGroundSpike(level, cursorX + spacingC);
                cursorX += spacingC + tail;
                break;

            case 1:
                addBlock(level, cursorX + spacingA, 0);
                addBlock(level, cursorX + spacingB, 1);
                addGroundSpike(level, cursorX + spacingC);
                cursorX += spacingC + tail;
                break;

            case 2:
                addGroundSpike(level, cursorX + spacingA);
                addGroundSpike(level, cursorX + spacingA + lerp(72f, 52f, difficulty));
                addBlock(level, cursorX + spacingB + 80f, 0);
                addTrap(level, cursorX + spacingC + 120f, 1);
                cursorX += spacingC + 260f + tail;
                break;

            case 3:
                addBlock(level, cursorX + spacingA, 0);
                addTrap(level, cursorX + spacingB, 1);
                addBlock(level, cursorX + spacingC, 0);
                addGroundSpike(level, cursorX + spacingC + lerp(150f, 116f, difficulty));
                cursorX += spacingC + 270f + tail;
                break;

            case 4:
                addBlock(level, cursorX + spacingA, 0);
                addGroundSpike(level, cursorX + spacingB - 34f);
                addGroundSpike(level, cursorX + spacingB + 18f);
                addBlock(level, cursorX + spacingC + 10f, 1);
                addTrap(level, cursorX + spacingC + 180f, 0);
                cursorX += spacingC + 340f + tail;
                break;

            case 5:
                addGroundSpike(level, cursorX + spacingA);
                addBlock(level, cursorX + spacingB, 0);
                addTrap(level, cursorX + spacingB + 165f, 1);
                addBlock(level, cursorX + spacingC + 120f, 0);
                addGroundSpike(level, cursorX + spacingC + 260f);
                cursorX += spacingC + 390f + tail;
                break;

            case 6:
                addBlock(level, cursorX + spacingA, 0);
                addBlock(level, cursorX + spacingB, 1);
                addTrap(level, cursorX + spacingB + 132f, 0);
                addGroundSpike(level, cursorX + spacingC + 64f);
                addBlock(level, cursorX + spacingC + 172f, 1);
                cursorX += spacingC + 300f + tail;
                break;

            default:
                addGroundSpike(level, cursorX + spacingA);
                addBlock(level, cursorX + spacingB + 20f, 0);
                addBlock(level, cursorX + spacingC + 20f, 1);
                addGroundSpike(level, cursorX + spacingC + lerp(190f, 138f, difficulty));
                cursorX += spacingC + 310f + tail;
                break;
        }
    }

    private void appendFlySegment(Level level) {
        float segmentStart = cursorX;
        float centerY = 282f + randomOffset(24f);
        int pattern = random.nextInt(4);

        switch (pattern) {
            case 0:
                cursorX = appendFlySlalom(level, segmentStart, centerY, 7 + random.nextInt(3), lerp(152f, 124f, difficulty));
                break;
            case 1:
                cursorX = appendFlyTunnel(level, segmentStart, centerY);
                break;
            case 2:
                cursorX = appendFlySaw(level, segmentStart, centerY);
                break;
            default:
                cursorX = appendFlyPocket(level, segmentStart, centerY);
                break;
        }
    }

    private void appendInvertedSegment(Level level) {
        int pattern = random.nextInt(6);
        float spacingA = lerp(120f, 96f, difficulty);
        float spacingB = lerp(250f, 198f, difficulty);
        float spacingC = lerp(430f, 330f, difficulty);
        float tail = lerp(170f, 110f, difficulty);

        switch (pattern) {
            case 0:
                addCeilingBlock(level, cursorX + spacingA, 1);
                addCeilingSpike(level, cursorX + spacingB - 30f);
                addCeilingBlock(level, cursorX + spacingC, 1);
                cursorX += spacingC + tail;
                break;

            case 1:
                addCeilingBlock(level, cursorX + spacingA, 1);
                addCeilingBlock(level, cursorX + spacingB, 2);
                addCeilingSpike(level, cursorX + spacingC);
                cursorX += spacingC + tail;
                break;

            case 2:
                addCeilingSpike(level, cursorX + spacingA);
                addCeilingBlock(level, cursorX + spacingB + 20f, 1);
                addCeilingTrap(level, cursorX + spacingC - 10f, 2);
                cursorX += spacingC + tail;
                break;

            case 3:
                addCeilingBlock(level, cursorX + spacingA, 1);
                addCeilingSpike(level, cursorX + spacingB - 20f);
                addCeilingTrap(level, cursorX + spacingB + 160f, 2);
                addCeilingBlock(level, cursorX + spacingC + 60f, 1);
                cursorX += spacingC + 300f + tail;
                break;

            case 4:
                addCeilingBlock(level, cursorX + spacingA, 1);
                addCeilingBlock(level, cursorX + spacingB, 2);
                addCeilingTrap(level, cursorX + spacingC - 10f, 1);
                addCeilingSpike(level, cursorX + spacingC + 128f);
                cursorX += spacingC + 230f + tail;
                break;

            default:
                addCeilingBlock(level, cursorX + spacingA, 1);
                addCeilingTrap(level, cursorX + spacingB - 10f, 1);
                addCeilingBlock(level, cursorX + spacingC - 20f, 2);
                addCeilingSpike(level, cursorX + spacingC + lerp(180f, 132f, difficulty));
                cursorX += spacingC + 260f + tail;
                break;
        }
    }

    private float lerp(float start, float end, float alpha) {
        return start + (end - start) * alpha;
    }

    private float appendFlySlalom(Level level, float startX, float centerY, int columns, float columnSpacing) {
        float currentCenter = centerY;
        float baseGap = lerp(164f, 122f, difficulty);

        for (int i = 0; i < columns; i++) {
            float x = startX + 84f + i * columnSpacing;
            float gap = baseGap - i * lerp(3f, 7f, difficulty) + randomOffset(10f);
            float bottomHeight = clamp(currentCenter - gap * 0.5f - GameWorld.GROUND_Y, 44f, lerp(146f, 196f, difficulty));
            float topClearY = currentCenter + gap * 0.5f;
            float topHeight = clamp((GameWorld.SCREEN_HEIGHT - 36f) - topClearY, 38f, lerp(136f, 184f, difficulty));

            if (i % 2 == 0) {
                level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, 56, bottomHeight, ObstacleType.FLY_BLOCK));
            } else {
                level.addObstacle(new Obstacle(x, topClearY, 56, topHeight, ObstacleType.FLY_BLOCK));
            }

            if (difficulty > 0.35f) {
                float midY = clamp(currentCenter - 26f + randomOffset(14f), 176f, 338f);
                level.addObstacle(new Obstacle(x + 66f, midY, 34, 38, ObstacleType.FLY_BLOCK));
            }

            if (i == 2 || i == columns - 2) {
                level.addObstacle(new Obstacle(x + 90f, GameWorld.GROUND_Y, 30, 42, ObstacleType.FLY_SPIKE_BOTTOM));
                level.addObstacle(new Obstacle(x + 90f, GameWorld.SCREEN_HEIGHT - 36f, 30, 42, ObstacleType.FLY_SPIKE_TOP));
            }

            currentCenter = clamp(currentCenter + randomOffset(lerp(40f, 68f, difficulty)), 212f, 352f);
        }

        return startX + columns * columnSpacing + lerp(118f, 84f, difficulty);
    }

    private float appendFlyTunnel(Level level, float startX, float centerY) {
        float spacing = lerp(148f, 122f, difficulty);
        float gap = lerp(154f, 114f, difficulty);

        for (int i = 0; i < 8; i++) {
            float x = startX + 80f + i * spacing;
            float localCenter = clamp(centerY + (i % 2 == 0 ? -30f : 30f), 220f, 336f);
            float bottomHeight = clamp(localCenter - gap * 0.5f - GameWorld.GROUND_Y, 46f, 186f);
            float topClearY = localCenter + gap * 0.5f;
            float topHeight = clamp((GameWorld.SCREEN_HEIGHT - 36f) - topClearY, 38f, 176f);

            level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, 48, bottomHeight, ObstacleType.FLY_BLOCK));
            level.addObstacle(new Obstacle(x, topClearY, 48, topHeight, ObstacleType.FLY_BLOCK));

            if (i % 2 == 1) {
                float midY = clamp(localCenter - 24f + randomOffset(12f), 182f, 330f);
                level.addObstacle(new Obstacle(x + 72f, midY, 32, 34, ObstacleType.FLY_BLOCK));
            }
        }

        return startX + 8 * spacing + lerp(116f, 80f, difficulty);
    }

    private float appendFlySaw(Level level, float startX, float centerY) {
        float spacing = lerp(156f, 126f, difficulty);

        for (int i = 0; i < 7; i++) {
            float x = startX + 84f + i * spacing;
            float swing = (i % 3 - 1) * lerp(46f, 70f, difficulty);
            float localCenter = clamp(centerY + swing, 212f, 352f);
            float gap = lerp(160f, 118f, difficulty);
            float bottomHeight = clamp(localCenter - gap * 0.5f - GameWorld.GROUND_Y, 44f, 190f);
            float topClearY = localCenter + gap * 0.5f;
            float topHeight = clamp((GameWorld.SCREEN_HEIGHT - 36f) - topClearY, 36f, 180f);

            level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, 54, bottomHeight, ObstacleType.FLY_BLOCK));
            level.addObstacle(new Obstacle(x + 70f, topClearY, 54, topHeight, ObstacleType.FLY_BLOCK));
            level.addObstacle(new Obstacle(x + 128f, GameWorld.GROUND_Y, 28, 40, ObstacleType.FLY_SPIKE_BOTTOM));
            level.addObstacle(new Obstacle(x + 128f, GameWorld.SCREEN_HEIGHT - 36f, 28, 40, ObstacleType.FLY_SPIKE_TOP));
        }

        return startX + 7 * spacing + lerp(108f, 76f, difficulty);
    }

    private float appendFlyPocket(Level level, float startX, float centerY) {
        float spacing = lerp(150f, 120f, difficulty);

        for (int i = 0; i < 8; i++) {
            float x = startX + 82f + i * spacing;
            float gap = lerp(166f, 124f, difficulty);
            float bottomHeight = clamp(centerY - gap * 0.5f - GameWorld.GROUND_Y, 44f, 178f);
            float topClearY = centerY + gap * 0.5f;
            float topHeight = clamp((GameWorld.SCREEN_HEIGHT - 36f) - topClearY, 36f, 172f);

            if (i % 4 == 0 || i % 4 == 3) {
                level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, 58, bottomHeight, ObstacleType.FLY_BLOCK));
            } else {
                level.addObstacle(new Obstacle(x, topClearY, 58, topHeight, ObstacleType.FLY_BLOCK));
            }

            if (i % 4 == 1) {
                level.addObstacle(new Obstacle(x + 72f, GameWorld.GROUND_Y + 84f, 36, 92, ObstacleType.FLY_BLOCK));
            } else if (i % 4 == 2) {
                level.addObstacle(new Obstacle(x + 72f, 260f, 36, 92, ObstacleType.FLY_BLOCK));
            }
        }

        return startX + 8 * spacing + lerp(114f, 82f, difficulty);
    }

    private int nextSegmentCount() {
        switch (mode) {
            case FLY:
                return difficulty < 0.55f ? 1 : 1 + random.nextInt(2);
            case INVERTED:
                return difficulty < 0.45f ? 1 + random.nextInt(2) : 1;
            case CUBE:
            default:
                return difficulty < 0.45f ? 2 + random.nextInt(2) : 1 + random.nextInt(2);
        }
    }

    private SegmentMode fromState(String stateName) {
        if ("FLY".equals(stateName)) {
            return SegmentMode.FLY;
        }
        if ("INVERTED".equals(stateName)) {
            return SegmentMode.INVERTED;
        }
        return SegmentMode.CUBE;
    }

    private float randomOffset(float amplitude) {
        return (random.nextFloat() * 2f - 1f) * amplitude;
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private void addBlock(Level level, float x, int heightSteps) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y + TILE * heightSteps, TILE, TILE, ObstacleType.BLOCK));
    }

    private void addTrap(Level level, float x, int heightSteps) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y + TILE * heightSteps, TILE, TILE, ObstacleType.TRAP_BLOCK));
    }

    private void addGroundSpike(Level level, float x) {
        level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, SPIKE_WIDTH, SPIKE_HEIGHT, ObstacleType.SPIKE));
    }

    private void addCeilingBlock(Level level, float x, int depthSteps) {
        float y = GameWorld.SCREEN_HEIGHT - 36f - (TILE * depthSteps);
        level.addObstacle(new Obstacle(x, y, TILE, TILE, ObstacleType.BLOCK));
    }

    private void addCeilingTrap(Level level, float x, int depthSteps) {
        float y = GameWorld.SCREEN_HEIGHT - 36f - (TILE * depthSteps);
        level.addObstacle(new Obstacle(x, y, TILE, TILE, ObstacleType.TRAP_BLOCK));
    }

    private void addCeilingSpike(Level level, float x) {
        level.addObstacle(new Obstacle(x, GameWorld.SCREEN_HEIGHT - 36f, SPIKE_WIDTH, SPIKE_HEIGHT, ObstacleType.FLY_SPIKE_TOP));
    }
}
