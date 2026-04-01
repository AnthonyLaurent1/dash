package fr.sdv.games.world;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Portal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelFactoryTest {

    @Test
    void createLevel1ShouldReturnANonNullLevel() {
        Level level = LevelFactory.createLevel1();

        assertNotNull(level);
    }

    @Test
    void createLevel1ShouldSetExpectedFinishX() {
        Level level = LevelFactory.createLevel1();

        assertEquals(17820f, level.getFinishX());
    }

    @Test
    void createLevel1ShouldContainFourPortalsInExpectedOrder() {
        Level level = LevelFactory.createLevel1();

        var portals = level.getPortals();

        assertEquals(4, portals.size);

        assertEquals(Portal.PortalType.FLY, portals.get(0).getType());
        assertEquals(Portal.PortalType.CUBE, portals.get(1).getType());
        assertEquals(Portal.PortalType.INVERT_ON, portals.get(2).getType());
        assertEquals(Portal.PortalType.INVERT_OFF, portals.get(3).getType());

        assertEquals(4910f, portals.get(0).getX());
        assertEquals(9260f, portals.get(1).getX());
        assertEquals(12300f, portals.get(2).getX());
        assertEquals(15020f, portals.get(3).getX());
    }

    @Test
    void createLevel1ShouldContainTrapBlocksInNormalAndInvertedSections() {
        Level level = LevelFactory.createLevel1();

        boolean foundNormalTrap = false;
        boolean foundCeilingTrap = false;

        for (Obstacle obstacle : level.getObstacles()) {
            if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
                if (obstacle.getY() > GameWorld.GROUND_Y + 1f) {
                    foundNormalTrap = true;
                }
                if (obstacle.getY() > GameWorld.SCREEN_HEIGHT / 2f) {
                    foundCeilingTrap = true;
                }
            }
        }

        assertTrue(foundNormalTrap, "Le niveau devrait contenir au moins un trap block dans une section normale.");
        assertTrue(foundCeilingTrap, "Le niveau devrait contenir au moins un trap block dans la section inversée.");
    }

    @Test
    void createLevel1ShouldContainGroundSpikesAndFlySectionObstacles() {
        Level level = LevelFactory.createLevel1();

        boolean foundGroundSpike = false;
        boolean foundFlyBlock = false;
        boolean foundTopFlySpike = false;
        boolean foundBottomFlySpike = false;

        for (Obstacle obstacle : level.getObstacles()) {
            switch (obstacle.getType()) {
                case SPIKE:
                    foundGroundSpike = true;
                    break;
                case FLY_BLOCK:
                    foundFlyBlock = true;
                    break;
                case FLY_SPIKE_TOP:
                    foundTopFlySpike = true;
                    break;
                case FLY_SPIKE_BOTTOM:
                    foundBottomFlySpike = true;
                    break;
                default:
                    break;
            }
        }

        assertTrue(foundGroundSpike, "Le niveau devrait contenir des pics au sol.");
        assertTrue(foundFlyBlock, "Le niveau devrait contenir des blocs dans la section fly.");
        assertTrue(foundTopFlySpike, "Le niveau devrait contenir des pics au plafond dans la section fly/inversée.");
        assertTrue(foundBottomFlySpike, "Le niveau devrait contenir des pics au sol dans la section fly.");
    }
}
