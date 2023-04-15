package adventure.engine;

import adventure.api.Game;

public abstract class Command {

    protected String text;

    public Command(String text) {
        this.text = text;
    }

    public abstract void accept(Game game);

    public static Command parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return new InvalidCommand();
        }
        String line = raw.toLowerCase().trim();
        if (line.startsWith("go")) {
            return new DirectionCommand(line);
        } else if (line.equals("q")) {
            return new QuitCommand();
        }  else if (line.startsWith("load")) {
            return new LoadGameCommand(raw);
        }
        return new QueryCommand(line);
    }

    public String getText() {
        return text;
    }
}

class LoadGameCommand extends Command {

    public LoadGameCommand(String command) {
        super(command.replace("load ", ""));
    }

    @Override public void accept(Game game) {
        game.load(getText());
    }
}

class DirectionCommand extends Command {

    private final String direction;

    public String getDirection() {
        return direction;
    }

    public DirectionCommand(String command) {
        super(command);
        this.direction = command.replace("go ", "");
    }

    public void accept(Game game) {
        game.getCurrentLocation().getExitOpt(direction)
                .ifPresentOrElse(
                exit -> {
                    if (exit.isLocked()) {
                        game.print(exit.getLockedMessage());
                    } else {
                        game.setCurrentLocation(exit.getNewLocation());
                        game.print(exit.getNewLocation().toString());
                        exit.accept(game);
                    }
                },
                () -> game.print("Sorry, can't go there")
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

class InvalidCommand extends Command {

    public InvalidCommand() {
        super(null);
    }

    @Override public void accept(Game game) {
        game.print("I don't understand");
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
