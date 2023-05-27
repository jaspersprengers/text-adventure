package adventure.api;

import java.io.Serializable;
import java.util.Queue;

public interface Story extends Serializable {

    String getName();

    Queue<Location> getLocations();

    default Location getLocation(String id) {
        return getLocations().stream().filter(l -> l.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Location not found: " + id));
    }

    //parse string to valid filename
    default String getFilename() {
        return getName().toLowerCase().replaceAll("\\W", "_");
    }

}
