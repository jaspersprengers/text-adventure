package adventure.engine;

import adventure.api.Game;
import adventure.engine.ui.SwingUI;

public class AdventureSwing {

    public static void main(String[] args) {
        new GameImpl(new SwingUI()).start();
    }


}
