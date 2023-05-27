package adventure.engine;

import adventure.api.Story;

import java.util.List;

public class TestStoryLoader implements StoryLoader {

    private List<Story> stories;

    public TestStoryLoader(List<Story> stories) {
        this.stories = stories;
    }

    @Override public List<Story> getStories() {
        return stories;
    }
}
