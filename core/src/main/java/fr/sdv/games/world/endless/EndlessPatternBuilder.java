package fr.sdv.games.world.endless;

import java.util.Random;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Obstacle.ObstacleType;
import fr.sdv.games.world.GameWorld;
import fr.sdv.games.world.Level;

/**
 * Construit les patterns d'obstacles du mode infini.
 */
public class EndlessPatternBuilder {
    private final Random random;
    private final EndlessObstaclePlacer placer = new EndlessObstaclePlacer();

    public EndlessPatternBuilder(Random random) {
        this.random = random;
    }

    /**
     * Ajoute un pattern correspondant au mode courant et retourne la nouvelle position du curseur.
     */
    public float append(Level level, EndlessLevelGenerator.SegmentMode mode, float cursorX, float difficulty) {
        switch (mode) {
            case FLY:
                return appendFlySegment(level, cursorX, difficulty);
            case INVERTED:
                return appendInvertedSegment(level, cursorX, difficulty);
            case CUBE:
            default:
                return appendCubeSegment(level, cursorX, difficulty);
        }
    }

    private float appendCubeSegment(Level level, float cursorX, float difficulty) {
        int pattern = random.nextInt(7);
        float spacingA = lerp(120f, 92f, difficulty);
        float spacingB = lerp(250f, 190f, difficulty);
        float spacingC = lerp(390f, 300f, difficulty);
        float tail = lerp(170f, 110f, difficulty);

        switch (pattern) {
            case 0:
                placer.addGroundSpike(level, cursorX + spacingA);
                placer.addGroundSpike(level, cursorX + spacingC);
                return cursorX + spacingC + tail;

            case 1:
                placer.addBlock(level, cursorX + spacingA, 0);
                placer.addBlock(level, cursorX + spacingB, 1);
                placer.addGroundSpike(level, cursorX + spacingC);
                return cursorX + spacingC + tail;

            case 2:
                placer.addGroundSpike(level, cursorX + spacingA);
                placer.addGroundSpike(level, cursorX + spacingA + lerp(72f, 52f, difficulty));
                placer.addBlock(level, cursorX + spacingB + 80f, 0);
                placer.addTrap(level, cursorX + spacingC + 120f, 1);
                return cursorX + spacingC + 260f + tail;

            case 3:
                placer.addBlock(level, cursorX + spacingA, 0);
                placer.addTrap(level, cursorX + spacingB, 1);
                placer.addBlock(level, cursorX + spacingC, 0);
                placer.addGroundSpike(level, cursorX + spacingC + lerp(150f, 116f, difficulty));
                return cursorX + spacingC + 270f + tail;

            case 4:
                placer.addBlock(level, cursorX + spacingA, 0);
                placer.addGroundSpike(level, cursorX + spacingB - 34f);
                placer.addGroundSpike(level, cursorX + spacingB + 18f);
                placer.addBlock(level, cursorX + spacingC + 10f, 1);
                placer.addTrap(level, cursorX + spacingC + 180f, 0);
                return cursorX + spacingC + 340f + tail;

            case 5:
                placer.addGroundSpike(level, cursorX + spacingA);
                placer.addBlock(level, cursorX + spacingB, 0);
                placer.addTrap(level, cursorX + spacingB + 165f, 1);
                placer.addBlock(level, cursorX + spacingC + 120f, 0);
                placer.addGroundSpike(level, cursorX + spacingC + 260f);
                return cursorX + spacingC + 390f + tail;

            case 6:
                placer.addBlock(level, cursorX + spacingA, 0);
                placer.addBlock(level, cursorX + spacingB, 1);
                placer.addTrap(level, cursorX + spacingB + 132f, 0);
                placer.addGroundSpike(level, cursorX + spacingC + 64f);
                placer.addBlock(level, cursorX + spacingC + 172f, 1);
                return cursorX + spacingC + 300f + tail;

            default:
                placer.addGroundSpike(level, cursorX + spacingA);
                placer.addBlock(level, cursorX + spacingB + 20f, 0);
                placer.addBlock(level, cursorX + spacingC + 20f, 1);
                placer.addGroundSpike(level, cursorX + spacingC + lerp(190f, 138f, difficulty));
                return cursorX + spacingC + 310f + tail;
        }
    }

    private float appendFlySegment(Level level, float cursorX, float difficulty) {
        float centerY = 282f + randomOffset(24f);
        int pattern = random.nextInt(4);
        float flyEase = 1f - Math.min(1f, difficulty * 1.45f);

        switch (pattern) {
            case 0:
                return appendFlySlalom(level, cursorX, centerY, 6 + random.nextInt(2), lerp(176f, 132f, difficulty), difficulty, flyEase);
            case 1:
                return appendFlyTunnel(level, cursorX, centerY, difficulty, flyEase);
            case 2:
                return appendFlySaw(level, cursorX, centerY, difficulty, flyEase);
            default:
                return appendFlyPocket(level, cursorX, centerY, difficulty, flyEase);
        }
    }

    private float appendInvertedSegment(Level level, float cursorX, float difficulty) {
        int pattern = random.nextInt(6);
        float spacingA = lerp(120f, 96f, difficulty);
        float spacingB = lerp(250f, 198f, difficulty);
        float spacingC = lerp(430f, 330f, difficulty);
        float tail = lerp(170f, 110f, difficulty);

        switch (pattern) {
            case 0:
                placer.addCeilingBlock(level, cursorX + spacingA, 1);
                placer.addCeilingSpike(level, cursorX + spacingB - 30f);
                placer.addCeilingBlock(level, cursorX + spacingC, 1);
                return cursorX + spacingC + tail;

            case 1:
                placer.addCeilingBlock(level, cursorX + spacingA, 1);
                placer.addCeilingBlock(level, cursorX + spacingB, 2);
                placer.addCeilingSpike(level, cursorX + spacingC);
                return cursorX + spacingC + tail;

            case 2:
                placer.addCeilingSpike(level, cursorX + spacingA);
                placer.addCeilingBlock(level, cursorX + spacingB + 20f, 1);
                placer.addCeilingTrap(level, cursorX + spacingC - 10f, 2);
                return cursorX + spacingC + tail;

            case 3:
                placer.addCeilingBlock(level, cursorX + spacingA, 1);
                placer.addCeilingSpike(level, cursorX + spacingB - 20f);
                placer.addCeilingTrap(level, cursorX + spacingB + 160f, 2);
                placer.addCeilingBlock(level, cursorX + spacingC + 60f, 1);
                return cursorX + spacingC + 300f + tail;

            case 4:
                placer.addCeilingBlock(level, cursorX + spacingA, 1);
                placer.addCeilingBlock(level, cursorX + spacingB, 2);
                placer.addCeilingTrap(level, cursorX + spacingC - 10f, 1);
                placer.addCeilingSpike(level, cursorX + spacingC + 128f);
                return cursorX + spacingC + 230f + tail;

            default:
                placer.addCeilingBlock(level, cursorX + spacingA, 1);
                placer.addCeilingTrap(level, cursorX + spacingB - 10f, 1);
                placer.addCeilingBlock(level, cursorX + spacingC - 20f, 2);
                placer.addCeilingSpike(level, cursorX + spacingC + lerp(180f, 132f, difficulty));
                return cursorX + spacingC + 260f + tail;
        }
    }

    private float appendFlySlalom(Level level, float startX, float centerY, int columns, float columnSpacing, float difficulty, float flyEase) {
        float currentCenter = centerY;
        float baseGap = lerp(196f, 132f, difficulty) + flyEase * 22f;

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

            if (difficulty > 0.45f && flyEase < 0.55f) {
                float midY = clamp(currentCenter - 26f + randomOffset(14f), 176f, 338f);
                level.addObstacle(new Obstacle(x + 66f, midY, 34, 38, ObstacleType.FLY_BLOCK));
            }

            if ((i == 2 || i == columns - 2) && flyEase < 0.85f) {
                level.addObstacle(new Obstacle(x + 90f, GameWorld.GROUND_Y, 30, 42, ObstacleType.FLY_SPIKE_BOTTOM));
                level.addObstacle(new Obstacle(x + 90f, GameWorld.SCREEN_HEIGHT - 36f, 30, 42, ObstacleType.FLY_SPIKE_TOP));
            }

            currentCenter = clamp(currentCenter + randomOffset(lerp(28f, 68f, difficulty)), 212f, 352f);
        }

        return startX + columns * columnSpacing + lerp(140f, 84f, difficulty);
    }

    private float appendFlyTunnel(Level level, float startX, float centerY, float difficulty, float flyEase) {
        float spacing = lerp(164f, 126f, difficulty);
        float gap = lerp(182f, 120f, difficulty) + flyEase * 18f;

        for (int i = 0; i < 7; i++) {
            float x = startX + 80f + i * spacing;
            float localCenter = clamp(centerY + (i % 2 == 0 ? -24f : 24f), 220f, 336f);
            float bottomHeight = clamp(localCenter - gap * 0.5f - GameWorld.GROUND_Y, 46f, 186f);
            float topClearY = localCenter + gap * 0.5f;
            float topHeight = clamp((GameWorld.SCREEN_HEIGHT - 36f) - topClearY, 38f, 176f);

            level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, 48, bottomHeight, ObstacleType.FLY_BLOCK));
            level.addObstacle(new Obstacle(x, topClearY, 48, topHeight, ObstacleType.FLY_BLOCK));

            if (i % 2 == 1 && flyEase < 0.65f) {
                float midY = clamp(localCenter - 24f + randomOffset(12f), 182f, 330f);
                level.addObstacle(new Obstacle(x + 72f, midY, 32, 34, ObstacleType.FLY_BLOCK));
            }
        }

        return startX + 7 * spacing + lerp(132f, 82f, difficulty);
    }

    private float appendFlySaw(Level level, float startX, float centerY, float difficulty, float flyEase) {
        float spacing = lerp(170f, 130f, difficulty);

        for (int i = 0; i < 6; i++) {
            float x = startX + 84f + i * spacing;
            float swing = (i % 3 - 1) * lerp(32f, 70f, difficulty);
            float localCenter = clamp(centerY + swing, 212f, 352f);
            float gap = lerp(188f, 122f, difficulty) + flyEase * 14f;
            float bottomHeight = clamp(localCenter - gap * 0.5f - GameWorld.GROUND_Y, 44f, 190f);
            float topClearY = localCenter + gap * 0.5f;
            float topHeight = clamp((GameWorld.SCREEN_HEIGHT - 36f) - topClearY, 36f, 180f);

            level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, 54, bottomHeight, ObstacleType.FLY_BLOCK));
            level.addObstacle(new Obstacle(x + 76f, topClearY, 54, topHeight, ObstacleType.FLY_BLOCK));

            if (flyEase < 0.8f) {
                level.addObstacle(new Obstacle(x + 138f, GameWorld.GROUND_Y, 28, 40, ObstacleType.FLY_SPIKE_BOTTOM));
                level.addObstacle(new Obstacle(x + 138f, GameWorld.SCREEN_HEIGHT - 36f, 28, 40, ObstacleType.FLY_SPIKE_TOP));
            }
        }

        return startX + 6 * spacing + lerp(132f, 78f, difficulty);
    }

    private float appendFlyPocket(Level level, float startX, float centerY, float difficulty, float flyEase) {
        float spacing = lerp(166f, 124f, difficulty);

        for (int i = 0; i < 7; i++) {
            float x = startX + 82f + i * spacing;
            float gap = lerp(192f, 128f, difficulty) + flyEase * 16f;
            float bottomHeight = clamp(centerY - gap * 0.5f - GameWorld.GROUND_Y, 44f, 178f);
            float topClearY = centerY + gap * 0.5f;
            float topHeight = clamp((GameWorld.SCREEN_HEIGHT - 36f) - topClearY, 36f, 172f);

            if (i % 4 == 0 || i % 4 == 3) {
                level.addObstacle(new Obstacle(x, GameWorld.GROUND_Y, 58, bottomHeight, ObstacleType.FLY_BLOCK));
            } else {
                level.addObstacle(new Obstacle(x, topClearY, 58, topHeight, ObstacleType.FLY_BLOCK));
            }

            if (i % 4 == 1 && flyEase < 0.7f) {
                level.addObstacle(new Obstacle(x + 72f, GameWorld.GROUND_Y + 84f, 36, 92, ObstacleType.FLY_BLOCK));
            } else if (i % 4 == 2 && flyEase < 0.7f) {
                level.addObstacle(new Obstacle(x + 72f, 260f, 36, 92, ObstacleType.FLY_BLOCK));
            }
        }

        return startX + 7 * spacing + lerp(136f, 84f, difficulty);
    }

    private float lerp(float start, float end, float alpha) {
        return start + (end - start) * alpha;
    }

    private float randomOffset(float amplitude) {
        return (random.nextFloat() * 2f - 1f) * amplitude;
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
