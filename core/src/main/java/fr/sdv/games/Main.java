package fr.sdv.games;

import com.badlogic.gdx.Game;
import fr.sdv.games.screen.GameScreen;

/**
 * Point d'entree principal de l'application LibGDX cote core.
 */
public class Main extends Game {
    /**
     * Cree l'ecran de jeu unique au demarrage.
     */
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
