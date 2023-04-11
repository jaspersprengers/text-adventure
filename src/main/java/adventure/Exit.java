package adventure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

@AllArgsConstructor
public class Exit extends Actionable {

    @Getter
    Direction direction;
    @Getter
    Location newLocation;
    @Getter
    String lockedMessage;

    private Consumer<Game> onMove;

    public Exit(Direction direction, Location newLocation) {
        this(direction, newLocation, null, null);
    }

    public static Exit North(Location newLocation){
        return new Exit(Direction.NORTH, newLocation);
    }

    public static Exit South(Location newLocation){
        return new Exit(Direction.SOUTH, newLocation);
    }

    public static Exit East(Location newLocation){
        return new Exit(Direction.EAST, newLocation);
    }

    public static Exit West(Location newLocation){
        return new Exit(Direction.WEST, newLocation);
    }

    public static Exit Up(Location newLocation){
        return new Exit(Direction.UP, newLocation);
    }

    public static Exit Down(Location newLocation) {
        return new Exit(Direction.DOWN, newLocation);
    }

    public Exit lock(String lockedMessage) {
        this.lockedMessage = lockedMessage;
        return this;
    }

    public void unlock(){
        this.lockedMessage = null;
    }

    public Exit setOnMove(Consumer<Game> onMove) {
        this.onMove = onMove;
        return this;
    }

    public boolean isLocked() {
        return lockedMessage != null;
    }

    @Override public void accept(Game game) {
        incrementCounter();
        if (onMove != null){
            onMove.accept(game);
        }
    }
}
