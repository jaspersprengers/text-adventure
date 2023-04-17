package adventure.engine;

import adventure.api.Story;
import adventure.engine.ui.UserInterface;

import java.util.List;
import java.util.OptionalInt;
import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * Acts as the service locator for Game implementations.
 */
public class StoryLoader  {

    List<String> loadStoryNames() {
        return ServiceLoader.load(Story.class).stream()
                .map(s -> s.get().getName())
                .toList();
    }

    Story loadStory(String name) {
        return ServiceLoader.load(Story.class).stream()
                .filter(s -> s.get().getName().equals(name))
                .findFirst().orElseThrow().get();
    }

    public String getWelcomeText() {
        var names = loadStoryNames();
        StringBuilder sb = new StringBuilder("Welcome to the Adventure Game!\n Select the number of the story you want to play:\n");
        names.forEach(name -> sb
                .append(1 + names.indexOf(name))
                .append("  : ")
                .append(name).append('\n'));
        return sb.toString();
    }

    /**
     * Listen for user input and call the callback with the selected story
     */
    public void setCallback(UserInterface ui, Consumer<Story> callback){
        ui.onNextCommand(cmd -> {
            parseStoryNumber(cmd).ifPresentOrElse(
                    storyNumber -> {
                        var story = loadStory(loadStoryNames().get(storyNumber - 1));
                        callback.accept(story);
                    },
                    () -> ui.printLine("Invalid story number, please try again")
            );
        });
    }

    private OptionalInt parseStoryNumber(String cmd) {
        try {
            return OptionalInt.of(Integer.parseInt(cmd));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
}
