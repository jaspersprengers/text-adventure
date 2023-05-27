package adventure.engine;

import adventure.api.Story;

import java.util.List;

public interface StoryLoader {

    List<Story> getStories();

    default List<String> getNames() {
        return getStories().stream().map(Story::getName).toList();
    }

    default String getWelcomeText() {
        StringBuilder sb = new StringBuilder("Select the number of the story you want to play:\n");
        getNames().forEach(name -> sb
                .append(1 + getNames().indexOf(name))
                .append("  : ")
                .append(name).append('\n'));
        return sb.toString();
    }


}
