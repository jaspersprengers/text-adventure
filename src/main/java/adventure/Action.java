package adventure;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@EqualsAndHashCode(of = "text", callSuper = false)
@AllArgsConstructor
public class Action extends Actionable {

    @Getter
    private final String text;
    @Getter
    @Setter
    private Consumer<Game> action;

    public boolean match(String input) {
        return this.text.equalsIgnoreCase(input);
    }

    public void accept(Game game) {
        incrementCounter();
        if (action != null) {
            action.accept(game);
        }
    }

    public static Action takeObject(String objectName) {
        return new Action("take " + objectName, game -> {
            game.print("You took the " + objectName);
            game.addGoodie("key");
        });
    }

    public static Action inspectObject(String objectName, String explanation) {
        return new Action("see " + objectName, game -> {
            game.print(explanation);
        });
    }

}
