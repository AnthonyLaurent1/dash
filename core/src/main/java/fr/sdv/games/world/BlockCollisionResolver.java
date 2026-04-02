package fr.sdv.games.world;

import fr.sdv.games.entity.Obstacle;
import fr.sdv.games.entity.Player;

/**
 * Encapsule les regles de collision entre le joueur et les blocs solides.
 */
public final class BlockCollisionResolver {
    private BlockCollisionResolver() {
    }

    /**
     * Resout la collision entre un joueur et un bloc selon son mode courant.
     */
    public static void resolve(Player player, Obstacle obstacle) {
        if (player.isFlying()) {
            player.die();
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

        if (player.isInverted()) {
            resolveInvertedCollision(player, obstacle, playerLeft, playerRight, playerTop, playerCenterY,
                blockLeft, blockRight, blockTop, blockBottom, overlapX, overlapY);
            return;
        }

        resolveStandardCollision(player, obstacle, playerLeft, playerRight, playerBottom, playerCenterY,
            playerCenterX, blockLeft, blockRight, blockTop, blockBottom);
    }

    /**
     * Resout une collision avec un bloc lorsque le joueur est en mode inverse.
     *
     * @param player joueur courant
     * @param obstacle obstacle percute
     * @param playerLeft bord gauche du joueur
     * @param playerRight bord droit du joueur
     * @param playerTop bord haut du joueur
     * @param playerCenterY centre vertical du joueur
     * @param blockLeft bord gauche du bloc
     * @param blockRight bord droit du bloc
     * @param blockTop bord haut du bloc
     * @param blockBottom bord bas du bloc
     * @param overlapX chevauchement horizontal
     * @param overlapY chevauchement vertical
     */
    private static void resolveInvertedCollision(
        Player player,
        Obstacle obstacle,
        float playerLeft,
        float playerRight,
        float playerTop,
        float playerCenterY,
        float blockLeft,
        float blockRight,
        float blockTop,
        float blockBottom,
        float overlapX,
        float overlapY
    ) {
        if (isTouchingBlockUnderside(player, playerLeft, playerRight, playerTop, blockLeft, blockRight, blockBottom)) {
            landPlayerOnInvertedBlock(player, obstacle, blockBottom);
            return;
        }

        if (isNonLethalInvertedBlock(obstacle)) {
            return;
        }

        if (isDeepInsideInvertedBlock(playerCenterY, blockTop, blockBottom, overlapX, overlapY)) {
            player.die();
        }
    }

    /**
     * Indique si le joueur touche la face inferieure d'un bloc en mode inverse.
     *
     * @return {@code true} si le joueur doit se poser sur le bloc
     */
    private static boolean isTouchingBlockUnderside(
        Player player,
        float playerLeft,
        float playerRight,
        float playerTop,
        float blockLeft,
        float blockRight,
        float blockBottom
    ) {
        boolean horizontallyAligned = isHorizontallyAlignedInverted(playerLeft, playerRight, blockLeft, blockRight);
        boolean rising = player.getVelocityY() >= -10f;

        return rising
            && horizontallyAligned
            && playerTop >= blockBottom - 14f
            && playerTop <= blockBottom + 18f;
    }

    /**
     * Indique si le joueur est suffisamment aligne horizontalement avec le bloc inverse.
     *
     * @return {@code true} si l'alignement horizontal est valide
     */
    private static boolean isHorizontallyAlignedInverted(
        float playerLeft,
        float playerRight,
        float blockLeft,
        float blockRight
    ) {
        return playerRight > blockLeft + 6f
            && playerLeft < blockRight - 6f;
    }

    /**
     * Pose le joueur sur un bloc inverse et declenche le piege si necessaire.
     *
     * @param player joueur courant
     * @param obstacle obstacle touche
     * @param blockBottom base du bloc touche
     */
    private static void landPlayerOnInvertedBlock(Player player, Obstacle obstacle, float blockBottom) {
        player.landOn(blockBottom - player.getSize());

        if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
            obstacle.triggerBreak(0.08f);
        }
    }

    /**
     * Indique si le bloc inverse ne doit jamais tuer le joueur.
     *
     * @param obstacle obstacle teste
     * @return {@code true} si le bloc est non mortel
     */
    private static boolean isNonLethalInvertedBlock(Obstacle obstacle) {
        return obstacle.getType() == Obstacle.ObstacleType.GHOST_BLOCK
            || obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK;
    }

    /**
     * Indique si le joueur est suffisamment enfonce dans un bloc inverse pour mourir.
     *
     * @return {@code true} si la collision doit etre fatale
     */
    private static boolean isDeepInsideInvertedBlock(
        float playerCenterY,
        float blockTop,
        float blockBottom,
        float overlapX,
        float overlapY
    ) {
        boolean insideBlockHeight = playerCenterY > blockBottom + 2f
            && playerCenterY < blockTop - 2f;

        boolean deepOverlap = overlapX > 12f && overlapY > 12f;

        return insideBlockHeight && deepOverlap;
    }


    /**
     * Resout une collision avec un bloc lorsque le joueur est en mode standard.
     *
     * @param player joueur courant
     * @param obstacle obstacle touche
     * @param playerLeft bord gauche du joueur
     * @param playerRight bord droit du joueur
     * @param playerBottom bord bas du joueur
     * @param playerCenterY centre vertical du joueur
     * @param playerCenterX centre horizontal du joueur
     * @param blockLeft bord gauche du bloc
     * @param blockRight bord droit du bloc
     * @param blockTop bord haut du bloc
     * @param blockBottom bord bas du bloc
     */
    private static void resolveStandardCollision(
        Player player,
        Obstacle obstacle,
        float playerLeft,
        float playerRight,
        float playerBottom,
        float playerCenterY,
        float playerCenterX,
        float blockLeft,
        float blockRight,
        float blockTop,
        float blockBottom
    ) {
        if (isLandingOnBlockTop(
            player,
            obstacle,
            playerLeft,
            playerRight,
            playerBottom,
            playerCenterX,
            blockLeft,
            blockRight,
            blockTop
        )) {
            landPlayerOnStandardBlock(player, obstacle, blockTop);
            return;
        }

        if (isNonLethalStandardBlock(obstacle)) {
            return;
        }

        if (isInsideStandardBlock(playerCenterY, blockTop, blockBottom)) {
            player.die();
        }
    }

    /**
     * Indique si le joueur est en train d'atterrir sur le dessus du bloc.
     *
     * @return {@code true} si le joueur doit se poser sur le bloc
     */
    private static boolean isLandingOnBlockTop(
        Player player,
        Obstacle obstacle,
        float playerLeft,
        float playerRight,
        float playerBottom,
        float playerCenterX,
        float blockLeft,
        float blockRight,
        float blockTop
    ) {
        boolean falling = player.getVelocityY() <= 0f;

        if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
            boolean horizontallyAligned = isTrapTopAligned(playerCenterX, blockLeft, blockRight);
            return falling
                && horizontallyAligned
                && playerBottom >= blockTop - 8f
                && playerBottom <= blockTop + 4f;
        }

        boolean horizontallyAligned = isStandardTopAligned(playerLeft, playerRight, blockLeft, blockRight);
        return falling
            && horizontallyAligned
            && playerBottom >= blockTop - 18f
            && playerBottom <= blockTop + 10f;
    }

    /**
     * Indique si le joueur est suffisamment centre pour se poser sur un bloc piege.
     *
     * @return {@code true} si l'alignement horizontal est valide pour un trap block
     */
    private static boolean isTrapTopAligned(float playerCenterX, float blockLeft, float blockRight) {
        return playerCenterX > blockLeft + 12f
            && playerCenterX < blockRight - 12f;
    }

    /**
     * Indique si le joueur recouvre suffisamment le dessus d'un bloc standard.
     *
     * @return {@code true} si l'alignement horizontal est valide pour un bloc normal
     */
    private static boolean isStandardTopAligned(
        float playerLeft,
        float playerRight,
        float blockLeft,
        float blockRight
    ) {
        return playerRight > blockLeft + 2f
            && playerLeft < blockRight - 2f;
    }

    /**
     * Pose le joueur sur le bloc et declenche les effets de casse si necessaire.
     *
     * @param player joueur courant
     * @param obstacle obstacle touche
     * @param blockTop sommet du bloc
     */
    private static void landPlayerOnStandardBlock(Player player, Obstacle obstacle, float blockTop) {
        player.landOn(blockTop);

        if (obstacle.getType() == Obstacle.ObstacleType.FRAGILE_BLOCK) {
            obstacle.triggerBreak(0.35f);
        }

        if (obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
            obstacle.triggerBreak(0.08f);
        }
    }

    /**
     * Indique si le bloc standard ne doit pas tuer le joueur en collision laterale.
     *
     * @param obstacle obstacle teste
     * @return {@code true} si ce bloc est traversable
     */
    private static boolean isNonLethalStandardBlock(Obstacle obstacle) {
        return obstacle.getType() == Obstacle.ObstacleType.GHOST_BLOCK
            || obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK;
    }

    /**
     * Indique si le centre du joueur est suffisamment a l'interieur du bloc pour mourir.
     *
     * @return {@code true} si la collision laterale est fatale
     */
    private static boolean isInsideStandardBlock(
        float playerCenterY,
        float blockTop,
        float blockBottom
    ) {
        return playerCenterY > blockBottom + 2f
            && playerCenterY < blockTop - 2f;
    }

}
