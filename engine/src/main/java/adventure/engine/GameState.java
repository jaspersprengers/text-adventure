package adventure.engine;

import adventure.api.Location;
import adventure.api.Story;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GameState implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    private final Set<String> goodies = new HashSet<>();
    private Location currentLocation;
    private final Story story;

    public GameState(Story story) {
        this.story = story;
    }

    public Set<String> getGoodies() {
        return goodies;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Story getStory() {
        return story;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
