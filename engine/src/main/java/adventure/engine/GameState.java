package adventure.engine;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public record GameState(Set<String> goodies,
                        String currentLocation) implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    GameState(String locationId) {
        this(new HashSet<>(), locationId);
    }
}
