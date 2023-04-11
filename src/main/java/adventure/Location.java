package adventure;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@EqualsAndHashCode(of = "id")
public class Location {

    @Getter
    private final String id;
    @Getter
    private final String description;
    private final Set<Action> actions;
    private final List<Exit> exits;

    public Location(String id, String description) {
        this.id = id;
        this.description = description;
        this.actions = new HashSet<>();
        this.exits = new ArrayList<>();
    }

    public void addActionable(Action action) {
        this.actions.add(action);
    }

    public void addActionable(String command, Consumer<Game> action ) {
        this.actions.add(new Action(command, action));
    }

    public void addExit(Exit movement) {
        this.exits.add(movement);
    }

    public Optional<Exit> getExit(Direction direction) {
        return exits.stream().filter(mt -> mt.getDirection() == direction).findFirst();
    }

    public Optional<Action> findAction(String command) {
        return actions.stream()
                .filter(a -> a.match(command))
                .findFirst();
    }

    @Override
    public String toString(){
        return description;

    }

}

