package adventure.engine;

import adventure.api.Story;

import java.util.List;

public interface StoryLoader {

    List<Story> getStories();

    default List<String> getNames() {
        return getStories().stream().map(Story::getName).toList();
    }

    default String getStoriesFormatted() {
        StringBuilder sb = new StringBuilder();
        getNames().forEach(name -> sb
                .append(1 + getNames().indexOf(name))
                .append("  : ")
                .append(name).append('\n'));
        return sb.toString();
    }


}
