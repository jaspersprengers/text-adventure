package adventure.stories;

import adventure.Location;

import java.util.HashSet;
import java.util.Set;

public abstract class Story {

    private final Set<Location> locations = new HashSet<>();

    protected Location addLocation(String id, String description){
        Location location = new Location(id, description);
        locations.add(location);
        return location;
    }

    public abstract Location getStartLocation();

    public Location getLocation(String id) {
        return locations.stream().filter(l -> l.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Location not found: " + id));
    }

}
