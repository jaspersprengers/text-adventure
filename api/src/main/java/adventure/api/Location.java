package adventure.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Location implements Serializable {

    private final String id;
    private final String description;

    private final Set<Action> actions;
    private final List<Exit> exits;
    public Location(String id, String description) {
        this.id = id;
        this.description = description;
        this.actions = new HashSet<>();
        this.exits = new ArrayList<>();
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public void addAction(String command, GameAction action ) {
        this.actions.add(new Action(command, action));
    }

    public void addExit(Exit exit) {
        this.exits.add(exit);
    }

    public Exit getExit(Location location) {
        return getExitOpt(location.getId()).orElseThrow(()-> new IllegalArgumentException("No exit with name " + location.getId()));
    }

    public Optional<Exit> getExitOpt(String exit) {
        return exits.stream().filter(mt -> mt.getName().equalsIgnoreCase(exit)).findFirst();
    }

    public Optional<Action> findAction(String command) {
        return actions.stream()
                .filter(a -> a.match(command))
                .findFirst();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString(){
        var exitStr = exits.isEmpty() ?
                "" :
                "\nExits: %s".formatted(exits.stream().map(Exit::getName).collect(Collectors.joining(", ")));
        return "%s%s".formatted(description, exitStr);

    }

}

