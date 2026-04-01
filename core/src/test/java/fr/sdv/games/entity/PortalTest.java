package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortalTest {

    @Test
    void constructorShouldInitializePortalFields() {
        Portal portal = new Portal(300f, 0f, 52f, 540f, Portal.PortalType.FLY);

        assertEquals(300f, portal.getX(), 0.0001f);
        assertEquals(0f, portal.getY(), 0.0001f);
        assertEquals(52f, portal.getWidth(), 0.0001f);
        assertEquals(540f, portal.getHeight(), 0.0001f);
        assertEquals(Portal.PortalType.FLY, portal.getType());
    }

    @Test
    void updateShouldMovePortalToTheLeft() {
        Portal portal = new Portal(500f, 0f, 52f, 540f, Portal.PortalType.CUBE);

        portal.update(0.5f, 100f);

        assertEquals(450f, portal.getX(), 0.0001f);
    }

    @Test
    void getBoundsShouldReturnPortalRectangle() {
        Portal portal = new Portal(700f, 20f, 50f, 300f, Portal.PortalType.INVERT_ON);

        Rectangle bounds = portal.getBounds();

        assertEquals(700f, bounds.x, 0.0001f);
        assertEquals(20f, bounds.y, 0.0001f);
        assertEquals(50f, bounds.width, 0.0001f);
        assertEquals(300f, bounds.height, 0.0001f);
    }

    @Test
    void portalTypeShouldSupportInvertOff() {
        Portal portal = new Portal(900f, 0f, 52f, 540f, Portal.PortalType.INVERT_OFF);

        assertEquals(Portal.PortalType.INVERT_OFF, portal.getType());
    }
}
