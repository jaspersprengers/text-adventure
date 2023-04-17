package adventure.engine;


import adventure.api.Game;
import adventure.api.Location;
import adventure.api.Story;
import adventure.engine.ui.UserInterface;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GameImpl implements Game {

    private final StoryLoader storyLoader = new StoryLoader();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private final Persistence persistence = new Persistence();

    private final UserInterface ui;

    private Location location;
    private Story story;
    private Set<String> goodies = new HashSet<>();

    public GameImpl(UserInterface ui) {
        this.ui = ui;
    }

    public void start() {
        print(storyLoader.getWelcomeText());
        storyLoader.setCallback(ui, s -> {
            this.story = s;
            GameState gameState = persistence.loadGame(story.getFilename())
                    .orElse(new GameState(story.getStartLocation().getId()));
            this.goodies = gameState.goodies();
            this.location = story.getLocation(gameState.currentLocation());
            //registers a new callback on the UI, to be called when the user enters a command
            ui.onNextCommand(this::onNext);
            print(location.toString());//print the description of the start location to the UI
        });
    }

    public Location getCurrentLocation() {
        return location;
    }

    public void setCurrentLocation(Location location) {
        this.location = location;
    }

    private void onNext(String commandStr) {
        Command.parse(commandStr).accept(this);
    }

    public void print(String text) {
        ui.printLine(text);
    }

    public Game setTimeout(long timeInSeconds, Consumer<Game> action) {
        executor.schedule(() -> {
            action.accept(this);
        }, timeInSeconds, TimeUnit.SECONDS);
        return this;
    }

    @Override public Set<String> getGoodies() {
        return goodies;
    }

    public void quit() {
        persistence.saveGame(story.getFilename(), new GameState(goodies, location.getId()));
        shutDown();
    }

    private void shutDown() {
        executor.shutdownNow();
        ui.shutdown();
    }
}
