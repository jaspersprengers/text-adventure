package adventure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

@AllArgsConstructor
public abstract class Command {

    @Getter
    protected String text;

    public abstract void accept(Game game);

    public static Command parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return new QuitCommand();
        }
        String line = raw.toLowerCase().trim();
        if (line.matches("^(n|s|e|w|u|d)$")) {
            return new DirectionCommand(line);
        } else if (line.equals("q")) {
            return new QuitCommand();
        }
        return new QueryCommand(line);
    }
}


class DirectionCommand extends Command {

    @Getter
    private Direction direction;

    public DirectionCommand(String direction) {
        super(direction);
        this.direction = Direction.fromString(direction).orElseThrow(() -> new IllegalArgumentException("Invalid direction: " + direction));
    }

    public void accept(Game game) {
        game.getCurrentLocation().getExit(direction).ifPresentOrElse(
                exit -> {
                    if (exit.isLocked()) {
                        game.print(exit.getLockedMessage());
                    } else {
                        game.setCurrentLocation(exit.getNewLocation());
                        game.print(exit.getNewLocation().toString());
                        exit.accept(game);
                    }
                },
                () -> game.print("There is no %s exit".formatted(direction))
        );
    }

}


class QuitCommand extends Command {

    public QuitCommand() {
        super("q");
    }

    @Override public void accept(Game game) {
        game.quit();
    }
}


class QueryCommand extends Command {

    public QueryCommand(String query) {
        super(query);
    }

    @Override public void accept(Game game) {
        game.getCurrentLocation().findAction(text).ifPresentOrElse(
                action -> {
                    action.accept(game);
                },
                () -> game.print("I don't understand")
        );
    }
}
