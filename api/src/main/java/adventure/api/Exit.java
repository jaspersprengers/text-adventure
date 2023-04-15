package adventure.api;


import java.io.Serializable;

public class Exit extends AbstractAction implements Serializable {

    private final String name;
    private final Location newLocation;
    private String lockedMessage;
    private GameAction onMove;

    private Exit(String name, Location newLocation, String lockedMessage, GameAction onMove) {
        this.name = name;
        this.newLocation = newLocation;
        this.lockedMessage = lockedMessage;
        this.onMove = onMove;
    }

    public static Exit to(Location location){
        return new Exit(location.getId(), location, null, null);
    }

    public void unlock(){
        this.lockedMessage = null;
    }

    public Exit lock(String lockedMessage) {
        this.lockedMessage = lockedMessage;
        return this;
    }

    public Exit onMove(GameAction onMove) {
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

    public String getName() {
        return name;
    }

    public Location getNewLocation() {
        return newLocation;
    }

    public String getLockedMessage() {
        return lockedMessage;
    }
}
