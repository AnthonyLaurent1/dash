package fr.sdv.games.world;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelFactoryTest {

    @Test
    void createLevel1ShouldReturnNonNullLevel() {
        Level level = LevelFactory.createLevel1();

        assertNotNull(level);
    }

    @Test
    void createLevel1ShouldSetExpectedFinishX() {
        Level level = LevelFactory.createLevel1();

        assertEquals(17820f, level.getInitialFinishX(), 0.0001f);
    }

    @Test
    void createLevel1ShouldContainExpectedPortalsInOrder() {
        Level level = LevelFactory.createLevel1();

        assertEquals(4, level.getPortals().size);

        assertEquals(Portal.PortalType.FLY, level.getPortals().get(0).getType());
        assertEquals(Portal.PortalType.CUBE, level.getPortals().get(1).getType());
        assertEquals(Portal.PortalType.INVERT_ON, level.getPortals().get(2).getType());
        assertEquals(Portal.PortalType.INVERT_OFF, level.getPortals().get(3).getType());

        assertEquals(4910f, level.getPortals().get(0).getX(), 0.0001f);
        assertEquals(9260f, level.getPortals().get(1).getX(), 0.0001f);
        assertEquals(12300f, level.getPortals().get(2).getX(), 0.0001f);
        assertEquals(15020f, level.getPortals().get(3).getX(), 0.0001f);
    }

    @Test
    void createLevel1ShouldContainGroundSpikes() {
        Level level = LevelFactory.createLevel1();

        boolean foundGroundSpike = false;
        for (Obstacle obstacle : level.getObstacles()) {
            if (obstacle.getType() == Obstacle.ObstacleType.SPIKE) {
                foundGroundSpike = true;
                break;
            }
        }

        assertTrue(foundGroundSpike);
    }

    @Test
    void createLevel1ShouldContainFlySectionObstacles() {
        Level level = LevelFactory.createLevel1();

        boolean foundFlyBlock = false;
        boolean foundTopFlySpike = false;
        boolean foundBottomFlySpike = false;

        for (Obstacle obstacle : level.getObstacles()) {
            if (obstacle.getType() == Obstacle.ObstacleType.FLY_BLOCK) {
                foundFlyBlock = true;
            }
            if (obstacle.getType() == Obstacle.ObstacleType.FLY_SPIKE_TOP) {
                foundTopFlySpike = true;
            }
            if (obstacle.getType() == Obstacle.ObstacleType.FLY_SPIKE_BOTTOM) {
                foundBottomFlySpike = true;
            }
        }

        assertTrue(foundFlyBlock);
        assertTrue(foundTopFlySpike);
        assertTrue(foundBottomFlySpike);
    }

    @Test
    void createLevel1ShouldContainTrapBlocksInNormalAndInvertedSections() {
        Level level = LevelFactory.createLevel1();

        boolean foundNormalTrap = false;
        boolean foundCeilingTrap = false;

        for (Obstacle obstacle : level.getObstacles()) {
            if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
                if (obstacle.getY() <= GameWorld.GROUND_Y + 2 * 42f) {
                    foundNormalTrap = true;
                }
                if (obstacle.getY() > GameWorld.SCREEN_HEIGHT / 2f) {
                    foundCeilingTrap = true;
                }
            }
        }

        assertTrue(foundNormalTrap);
        assertTrue(foundCeilingTrap);
    }

    @Test
    void createLevel1ShouldContainManyObstacles() {
        Level level = LevelFactory.createLevel1();

        assertTrue(level.getObstacles().size > 20);
    }
}
