package adventure.engine;

import adventure.api.Game;
import adventure.engine.ui.ConsoleInterface;

public class Adventure {

    public static void main(String[] args) {
        Game game = new GameImpl(new ConsoleInterface());
        game.start();
    }


}
