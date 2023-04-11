package adventure;

import adventure.stories.Story;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Game {

    @Getter
    private final Set<String> goodies = new HashSet<>();
    @Getter
    @Setter
    private Location currentLocation;
    @Getter
    private final Story story;

    private final UserInterface ui;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private boolean quit = false;

    public Game(Story story, UserInterface ui) {
        this.story = story;
        this.ui = ui;
        this.setCurrentLocation(story.getStartLocation());
    }

    public Game start() {
        print(getCurrentLocation().toString());
        return this;
    }

    public void loopUntilDone() {
        while (next()) {
            //enter next iteration
        }
    }

    public boolean next() {
        Command command = Command.parse(ui.readLine());
        command.accept(this);
        if (quit) {
            executor.shutdownNow();
            return false;
        } else {
            return true;
        }
    }

    public void print(String text) {
        ui.printLine(text);
    }

    public boolean isCurrentLocation(String locationId) {
        return currentLocation.getId().equals(locationId);
    }

    public Location getCurrentLocation(String locationId) {
        return story.getLocation(locationId);
    }

    public Game setTimeout(long timeInSeconds, Consumer<Game> action) {
        executor.schedule(() -> {
            action.accept(this);
        }, timeInSeconds, TimeUnit.SECONDS);
        return this;
    }

    public void addGoodie(String goodie) {
        goodies.add(goodie);
    }

    public boolean hasGoody(String goodie) {
        return goodies.contains(goodie);
    }


    public boolean isInProgress() {
        return !quit;
    }

    public void quit() {
        quit = true;
    }
}
