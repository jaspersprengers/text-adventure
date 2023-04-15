package adventure.engine;

import adventure.api.Game;
import adventure.engine.ui.SwingUI;

public class AdventureSwing {

    public static void main(String[] args) {
        Game game = new GameImpl(new SwingUI());
        game.start();
    }


}
