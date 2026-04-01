package fr.sdv.games.world.endless;

import java.util.Random;

import fr.sdv.games.entity.Portal;
import fr.sdv.games.entity.Portal.PortalType;
import fr.sdv.games.world.GameWorld;
import fr.sdv.games.world.Level;

/**
 * Genere des segments infinis apres la partie tutoriel.
 *
 * <p>Le generateur maintient toujours un stock d'obstacles devant la camera,
 * retire les elements passes hors champ et alterne entre plusieurs familles
 * d'epreuves en fonction de l'etat courant du joueur.</p>
 */
public class EndlessLevelGenerator {
    private static final float BUFFER_AHEAD = 2800f;
    private static final float DESPAWN_X = -220f;
    private static final float BASE_SAFE_GAP = 120f;

    private final Random random = new Random();
    private final EndlessPatternBuilder patternBuilder = new EndlessPatternBuilder(random);

    private float cursorX;
    private SegmentMode mode;
    private int segmentsUntilSwitch;
    private float difficulty;

    /**
     * Modes de segment pris en charge par le generateur infini.
     */
    public enum SegmentMode {
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
     *
     * @param level niveau mutable a completer
     * @param score score courant utilise pour calculer la difficulte
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

    /**
     * Genere un segment complet dans le mode courant ou insere une transition si necessaire.
     */
    private void appendSegment(Level level) {
        if (segmentsUntilSwitch <= 0) {
            appendTransition(level);
            segmentsUntilSwitch = nextSegmentCount();
        }

        cursorX = patternBuilder.append(level, mode, cursorX, difficulty);
        segmentsUntilSwitch--;
    }

    /**
     * Place un portail de transition puis change le mode logique courant du generateur.
     */
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
                level.addPortal(new Portal(cursorX, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.CUBE));
                mode = SegmentMode.CUBE;
                break;

            case INVERTED:
                level.addPortal(new Portal(cursorX, 0, 52, GameWorld.SCREEN_HEIGHT, PortalType.INVERT_OFF));
                mode = SegmentMode.CUBE;
                break;
        }

        cursorX += lerp(120f, 86f, difficulty);
    }

    /**
     * Determine combien de segments consecutifs conserver avant le prochain portail.
     */
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

    /**
     * Convertit le nom d'etat du joueur en mode de generation.
     */
    private SegmentMode fromState(String stateName) {
        if ("FLY".equals(stateName)) {
            return SegmentMode.FLY;
        }
        if ("INVERTED".equals(stateName)) {
            return SegmentMode.INVERTED;
        }
        return SegmentMode.CUBE;
    }

    /**
     * Interpole lineairement entre deux valeurs flottantes.
     */
    private float lerp(float start, float end, float alpha) {
        return start + (end - start) * alpha;
    }
}
