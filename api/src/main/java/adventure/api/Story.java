package adventure.api;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class Story implements Serializable {

    protected final String name;

    private final Set<Location> locations = new HashSet<>();

    public String getName() {
        return name;
    }

    public Story(String name) {
        this.name = name;
    }

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

    //parse string to valid filename
    public String getFilename() {
        return name.toLowerCase().replaceAll("\\W", "_");
    }

}
