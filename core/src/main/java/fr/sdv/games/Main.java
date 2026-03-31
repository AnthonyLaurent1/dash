package fr.sdv.games;

import com.badlogic.gdx.Game;
import fr.sdv.games.screen.GameScreen;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
