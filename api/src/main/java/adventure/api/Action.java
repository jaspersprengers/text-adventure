package adventure.api;

import java.io.Serializable;

public class Action extends AbstractAction implements Serializable {

    private final String text;

    private GameAction onAccept;

    public Action(String text, GameAction onAccept) {
        this.text = text;
        this.onAccept = onAccept;
    }

    public boolean matches(String input) {
        return this.text.equalsIgnoreCase(input);
    }

    public void accept(Game game) {
        incrementCounter();
        if (onAccept != null) {
            onAccept.accept(game);
        }
    }

    public static Action take(String objectName) {
        return new Action("take " + objectName, game -> {
            game.print("You took the " + objectName);
            game.addGoodie("key");
        });
    }

    public static Action see(String objectName, String explanation) {
        return new Action("see " + objectName, game -> {
            game.print(explanation);
        });
    }

    public String getText() {
        return text;
    }

}
