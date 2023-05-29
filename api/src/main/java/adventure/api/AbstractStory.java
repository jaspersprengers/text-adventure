package adventure.api;

import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Set;

public abstract class AbstractStory implements Story {

    protected final String name;
    protected final Set<Locale> supportedLanguages;
    protected ResourceBundle resourceBundle;

    public AbstractStory(String name, Set<Locale> supportedLanguages) {
        this.name = name;
        this.supportedLanguages = supportedLanguages;
    }

    private final Queue<Location> locations = new LinkedList<>();

    protected Location addLocation(String id, String description) {
        Location location = new Location(id, description);
        locations.offer(location);
        return location;
    }

    public boolean setLanguage(Locale loc) {
        var matches = supportedLanguages.stream().anyMatch(l -> l.getLanguage().equals(loc.getLanguage()));
        if (matches) {
            try {
                resourceBundle = ResourceBundle.getBundle(getFilename(), loc);
            } catch (MissingResourceException missingResourceException) {
                //if the game supports only one language, you don't need a resource bundle
                if (supportedLanguages.size() > 1) {
                    throw new IllegalStateException("You must provide a suitable properties file", missingResourceException);
                }
            }
        }
        return matches;
    }

    public String getName() {
        return name;
    }

    public Queue<Location> getLocations() {
        if (locations.isEmpty()) {
            setup();
        }
        return locations;
    }

    protected abstract void setup();
}
