package fr.sdv.games.entity;

import com.badlogic.gdx.math.Rectangle;
import fr.sdv.games.state.CubeState;
import fr.sdv.games.state.DeadState;
import fr.sdv.games.state.FlyState;
import fr.sdv.games.state.InvertedCubeState;
import fr.sdv.games.state.PlayerState;
import fr.sdv.games.world.GameWorld;

/**
 * Represente le personnage controle par le joueur et son etat physique courant.
 */
public class Player {
    private static final float SIZE = 32f;

    private float x;
    private float y;
    private float velocityY;
    private float rotation;
    private boolean inputPressed;
    private PlayerState state;
    private boolean deathAnimating;
    private float deathTimer;
    private final float deathDuration = 0.35f;

    /**
     * Cree un joueur remis a son etat initial.
     */
    public Player() {
        reset();
    }

    /**
     * Repositionne le joueur au depart et restaure son etat de cube.
     */
    public void reset() {
        x = 140f;
        velocityY = 0f;
        rotation = 0f;
        inputPressed = false;
        deathAnimating = false;
        deathTimer = 0f;
        setGroundedCubeState();
    }

    /**
     * Lance l'animation de mort.
     */
    public void startDeathAnimation() {
        deathAnimating = true;
        deathTimer = deathDuration;
    }

    /**
     * Active l'etat de mort et son animation.
     */
    public void die() {
        if (isDead()) {
            return;
        }
        startDeathAnimation();
        changeState(new DeadState());
    }

    /**
     * Avance l'animation de mort si elle est active.
     */
    public void updateDeathAnimation(float delta) {
        if (!deathAnimating) {
            return;
        }

        deathTimer -= delta;
        if (deathTimer < 0f) {
            deathTimer = 0f;
        }
    }

    /**
     *  {@code true} si l'animation de mort est en cours
     */
    public boolean isDeathAnimating() {
        return deathAnimating;
    }

    /**
     *  progression normalisee de l'animation de mort entre 0 et 1
     */
    public float getDeathProgress() {
        if (!deathAnimating) {
            return 0f;
        }
        return 1f - (deathTimer / deathDuration);
    }

    /**
     * Pose instantanement le joueur sur une surface.
     */
    public void landOn(float y) {
        this.y = y;
        this.velocityY = 0f;
        this.rotation = 0f;
    }

    /**
     * Remet le joueur en mode cube pose au sol.
     */
    public void setGroundedCubeState() {
        landOn(GameWorld.GROUND_Y);
        changeState(new CubeState());
    }

    /**
     * Delegue la mise a jour au state actif.
     */
    public void update(float delta) {
        state.update(this, delta);
    }

    /**
     * Transmet les entrees du joueur au state actif.
     */
    public void handleInput(boolean pressed, boolean justPressed) {
        inputPressed = pressed;
        state.handleInput(this, pressed, justPressed);
    }

    /**
     * Change le state de controle du joueur.
     */
    public void changeState(PlayerState newState) {
        state = newState;
        state.enter(this);
    }

    /**
     *  boite de collision du joueur
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    /**
     *  {@code true} si la vitesse verticale est nulle
     */
    public boolean isGrounded() {
        return velocityY == 0f;
    }

    /**
     *  {@code true} si le joueur est en mode vaisseau
     */
    public boolean isFlying() {
        return state instanceof FlyState;
    }

    /**
     *  {@code true} si le joueur est en mode cube inverse
     */
    public boolean isInverted() {
        return state instanceof InvertedCubeState;
    }

    /**
     *  {@code true} si le joueur est dans l'etat de mort
     */
    public boolean isDead() {
        return state instanceof DeadState;
    }

    /**
     * Applique une impulsion verticale immediate.
     */
    public void jump(float force) {
        velocityY = force;
    }

    /**
     * Applique une gravite vers le bas.
     */
    public void applyGravity(float delta, float gravity) {
        velocityY -= gravity * delta;
    }

    /**
     * Ajoute une force verticale continue.
     */
    public void addForce(float delta, float force) {
        velocityY += force * delta;
    }

    /**
     * Deplace verticalement le joueur selon sa vitesse courante.
     */
    public void moveVertical(float delta) {
        y += velocityY * delta;
    }

    /**
     * Contrainte le joueur au sol pour le mode normal.
     */
    public void clampToGround() {
        if (y <= GameWorld.GROUND_Y) {
            y = GameWorld.GROUND_Y;
            velocityY = 0f;
            rotation = 0f;
        }
    }

    /**
     * Contrainte le vaisseau entre le sol et le plafond de la zone de vol.
     */
    public void clampToFlyBounds() {
        if (y < GameWorld.GROUND_Y) {
            y = GameWorld.GROUND_Y;
            velocityY = 0f;
        }

        float maxY = getCeilingY();
        if (y > maxY) {
            y = maxY;
            velocityY = -60f;
        }
    }

    /**
     * Colle le joueur au plafond utilise par le mode inverse.
     */
    public void snapToCeiling() {
        y = getCeilingY();
        velocityY = 0f;
    }

    /**
     * Contrainte le joueur au plafond en mode inverse.
     */
    public void clampToCeiling() {
        float ceilingY = getCeilingY();
        if (y >= ceilingY) {
            y = ceilingY;
            velocityY = 0f;
            rotation = 180f;
        }
    }

    private float getCeilingY() {
        return GameWorld.SCREEN_HEIGHT - 36f - SIZE;
    }

    /**
     *  position horizontale du joueur
     */
    public float getX() {
        return x;
    }

    /**
     *  position verticale du joueur
     */
    public float getY() {
        return y;
    }

    /**
     *  taille du joueur
     */
    public float getSize() {
        return SIZE;
    }

    /**
     *  vitesse verticale actuelle
     */
    public float getVelocityY() {
        return velocityY;
    }

    /**
     * Met a jour directement la vitesse verticale.
     */
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    /**
     *  rotation visuelle courante
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Defini la rotation visuelle du joueur.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Ajoute un delta de rotation.
     */
    public void addRotation(float amount) {
        rotation += amount;
    }

    /**
     *  etat brut de l'entree maintenue
     */
    public boolean isInputPressed() {
        return inputPressed;
    }

    /**
     *  state actif du joueur
     */
    public PlayerState getState() {
        return state;
    }
}
