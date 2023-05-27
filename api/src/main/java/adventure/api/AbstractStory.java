package adventure.api;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractStory implements Story{

    protected final String name;

    private final Queue<Location> locations = new LinkedList<>();

    public AbstractStory(String name) {
        this.name = name;
    }

    protected Location addLocation(String id, String description) {
        Location location = new Location(id, description);
        locations.offer(location);
        return location;
    }

    public String getName() {
        return name;
    }

    public Queue<Location> getLocations() {
        return locations;
    }
}
