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

        if (obstacle.getType() == Obstacle.ObstacleType.GHOST_BLOCK
            || obstacle.getType() == Obstacle.ObstacleType.TRAP_BLOCK) {
            return;
        }

        boolean insideBlockHeight =
            playerCenterY > blockBottom + 2f &&
                playerCenterY < blockTop - 2f;

        boolean deepOverlap = overlapX > 12f && overlapY > 12f;

        if (insideBlockHeight && deepOverlap) {
            player.die();
        }
    }

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
            player.die();
        }
    }
}
