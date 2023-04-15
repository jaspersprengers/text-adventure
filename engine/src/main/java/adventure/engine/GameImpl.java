package adventure.engine;


import adventure.api.Game;
import adventure.api.Location;
import adventure.api.Story;
import adventure.engine.ui.UserInterface;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GameImpl implements Game {

    private GameState gameState;
    private final StoryLoader storyLoader = new StoryLoader();
    private final UserInterface ui;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private final Persistence persistence = new Persistence();

    public GameImpl(UserInterface ui) {
        this.ui = ui;
        ui.onNextLine(this::onNext);
    }

    public void start() {
        this.gameState = new GameState(storyLoader);
        this.gameState.setCurrentLocation(storyLoader.getStartLocation());
        print(storyLoader.getStartLocation().toString());
    }

    public void startStory(Story story){
        this.gameState = new GameState(story);
        if (gameState.getCurrentLocation() == null) {
            this.gameState.setCurrentLocation(gameState.getStory().getStartLocation());
        }
        print(gameState.getCurrentLocation().toString());
    }

    public Location getCurrentLocation() {
        return gameState.getCurrentLocation();
    }

    public void setCurrentLocation(Location location) {
        gameState.setCurrentLocation(location);
    }

    private void onNext(String commandStr) {
        Command.parse(commandStr).accept(this);
    }

    public void print(String text) {
        ui.printLine(text);
    }

    public void load(String storyName){
        print("Loading game "+storyName);
        persistence.loadGame(storyName).ifPresentOrElse(
                gameState -> {
                    this.gameState = gameState;
                    print("Successfully loaded. Resuming from last save...");
                    start();
                },
                () -> print("No saved game found for "+storyName)
        );
    }

    public boolean isCurrentLocation(String locationId) {
        return gameState.getCurrentLocation().getId().equals(locationId);
    }

    public Game setTimeout(long timeInSeconds, Consumer<Game> action) {
        executor.schedule(() -> {
            action.accept(this);
        }, timeInSeconds, TimeUnit.SECONDS);
        return this;
    }

    public void addGoodie(String goodie) {
        gameState.getGoodies().add(goodie);
    }

    public boolean hasGoody(String goodie) {
        return gameState.getGoodies().contains(goodie);
    }

    public void quit() {
        persistence.saveGame(gameState);
        shutDown();
    }

    public void conclude() {
        shutDown();
    }

    private void shutDown() {
        executor.shutdownNow();
        ui.shutdown();
    }
}
