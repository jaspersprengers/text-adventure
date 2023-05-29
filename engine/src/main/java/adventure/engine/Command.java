package adventure.engine;

import adventure.api.Game;

import java.util.ResourceBundle;

public abstract class Command {

    protected ResourceBundle rb;

    public Command(ResourceBundle rb) {
        this.rb = rb;
    }

    public abstract void accept(String text, Game game);

}

class HelpCommand extends  Command {

    public HelpCommand(ResourceBundle rb) {
        super(rb);
    }

    @Override public void accept(String text, Game game) {
        game.print(rb.getString("cmd_help_text"));
    }
}

abstract class StandardCommand extends Command {

    public StandardCommand(ResourceBundle rb) {
        super(rb);
    }

    public void accept(String text, Game game) {
        handle(text.replaceFirst("^(\\w+) (.*?)$", "$2"), game);
    }

    protected abstract void handle(String text, Game game);

}

class DirectionCommand extends StandardCommand {

    public DirectionCommand(ResourceBundle rb) {
        super(rb);
    }

    public void handle(String text, Game game) {
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
                () -> game.print(rb.getString("cmd_no_exit"))
        );
    }
}

class QuitCommand extends Command {

    public QuitCommand(ResourceBundle rb) {
        super(rb);
    }

    @Override public void accept(String text, Game game) {
        game.saveAndQuit();
    }
}

class InvalidCommand extends Command {

    public InvalidCommand(ResourceBundle rb) {
        super(rb);
    }

    @Override public void accept(String text, Game game) {
        game.print(rb.getString("cmd_unknown"));
    }
}

class QueryCommand extends Command {

    public QueryCommand(ResourceBundle rb) {
        super(rb);
    }

    @Override public void accept(String text, Game game) {
        game.getCurrentLocation().findAction(text).ifPresentOrElse(
                action -> {
                    action.accept(game);
                },
                () -> game.print(rb.getString("cmd_unknown"))
        );
    }
}
