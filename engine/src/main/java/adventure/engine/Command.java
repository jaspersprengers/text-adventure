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
        }
        else if (line.startsWith("see")) {
            return new SeeCommand(line);
        }
        else if (line.equals("help")) {
            return new HelpCommand();
        }
        else if (line.equals("quit")) {
            return new QuitCommand();
        }
        return new QueryCommand(line);
    }

    public String getText() {
        return text;
    }
}

class HelpCommand extends  Command {

    public HelpCommand() {
        super("help");
    }

    @Override public void accept(Game game) {
        game.print("Available commands:");
        game.print("go <direction> to move to a different location. Access may be blocked.");
        game.print("see <some object> to get more information about the object.");
        game.print("quit to exit the game and resume later.");
        game.print("help");
    }
}

abstract class StandardCommand extends Command {

    public StandardCommand(String text) {
        super(text.replaceFirst("^(\\w+) (.*?)$", "$2"));
    }
}

class DirectionCommand extends StandardCommand {

    public DirectionCommand(String command) {
        super(command);
    }

    public void accept(Game game) {
        game.getCurrentLocation().getExitOpt(text)
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

class SeeCommand extends Command {

    public SeeCommand(String command) {
        super(command);
    }

    @Override public void accept(Game game) {
        game.getCurrentLocation().findAction(text).ifPresentOrElse(
                action -> {
                    action.accept(game);
                },
                () -> game.print(game.getCurrentLocation().toString())
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
