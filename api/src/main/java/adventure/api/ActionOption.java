package adventure.api;

import java.io.Serializable;

public class ActionOption extends AbstractAction implements Serializable {

    private final String text;

    private GameAction action;

    public ActionOption(String text, GameAction action) {
        this.text = text;
        this.action = action;
    }

    public boolean match(String input) {
        return this.text.equalsIgnoreCase(input);
    }

    public void accept(Game game) {
        incrementCounter();
        if (action != null) {
            action.accept(game);
        }
    }

    public static ActionOption takeObject(String objectName) {
        return new ActionOption("take " + objectName, game -> {
            game.print("You took the " + objectName);
            game.addGoodie("key");
        });
    }

    public static ActionOption inspectObject(String objectName, String explanation) {
        return new ActionOption("see " + objectName, game -> {
            game.print(explanation);
        });
    }

    public String getText() {
        return text;
    }

    public GameAction getAction() {
        return action;
    }

    public void setAction(GameAction action) {
        this.action = action;
    }

}
