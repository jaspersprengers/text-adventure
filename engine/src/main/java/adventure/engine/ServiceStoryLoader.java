package adventure.engine;

import adventure.api.Story;

import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Acts as the service locator for Game implementations.
 */
public class ServiceStoryLoader implements StoryLoader {

    private List<Story> stories;

    public List<Story> getStories() {
        if (stories == null) {
            stories = ServiceLoader.load(Story.class).stream()
                    .map(ServiceLoader.Provider::get)
                    .filter(Objects::nonNull)
                    .toList();
            System.out.printf("Loaded %d Story implementations.\n", stories.size());
        }
        return stories;
    }




}
