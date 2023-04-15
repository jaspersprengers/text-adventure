package adventure.engine;

import adventure.api.Location;
import adventure.api.Story;

import java.util.List;
import java.util.ServiceLoader;

public class StoryLoader extends Story {

    public StoryLoader() {
        super("Story loader");
    }

    List<String> loadStoryNames() {
        return ServiceLoader.load(Story.class).stream()
                .map(s -> s.get().getName())
                .toList();
    }

    Story loadStory(String fileName) {
        return ServiceLoader.load(Story.class).stream()
                .filter(s -> s.get().getName().equals(fileName))
                .findFirst().orElseThrow().get();
    }

    @Override public Location getStartLocation() {
        var names = loadStoryNames();
        StringBuilder sb = new StringBuilder("Welcome to the Adventure Game!\n Select the number of the story you want to play:\n");
        names.forEach(name -> sb
                .append(1 + names.indexOf(name))
                .append("  : ")
                .append(name).append('\n'));

        Location welcome = new Location("welcome", sb.toString());

        names.forEach(name -> {
            welcome.addActionable(Integer.toString(1 + names.indexOf(name)), game -> game.startStory(loadStory(name)));
        });
        return welcome;
    }
}
