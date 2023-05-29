package adventure.engine;


import adventure.api.Game;
import adventure.api.Location;
import adventure.api.Story;
import adventure.api.UserInterface;

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class GameImpl implements Game {

    //dependencies
    private final StoryLoader storyLoader;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private final Persistence persistence;
    private final UserInterface ui;
    private final CommandFactory commandFactory = new CommandFactory();

    //game state
    private Locale locale;
    private ResourceBundle rb;

    private Story story;
    private Location location;
    private Set<String> goodies = new HashSet<>();


    public GameImpl(UserInterface ui) {
        this(ui, new ServiceStoryLoader(), new FilePersistence());
    }

    public GameImpl(UserInterface ui, StoryLoader storyLoader, Persistence persistence) {
        this.ui = ui;
        ui.onNextCommand(this::onNext);
        this.storyLoader = storyLoader;
        this.persistence = persistence;
    }

    public void start() {
        print("Welcome/Bonvenon:");
        print("1: English");
        print("2: Esperanto");
        commandFactory.newModalDialog().addHandler("1", () -> {
            onLocaleSelected(Locale.ENGLISH);
        }).addHandler("2", () -> {
            onLocaleSelected(new Locale("eo"));
        });
    }

    private void onLocaleSelected(Locale locale) {
        this.locale = locale;
        rb = ResourceBundle.getBundle("UI", locale);
        commandFactory.setResourceBundle(rb);
        if (storyLoader.getNames().isEmpty()) {
            translate("engine_no_stories");
            shutDown();
            return;
        }
        selectStory();
    }

    private void selectStory() {
        translate("engine_select_story");
        print(storyLoader.getStoriesFormatted());
        var dialog = commandFactory.newModalDialog();
        var stories = storyLoader.getStories().stream().filter(s -> s.setLanguage(locale)).toList();

        IntStream.rangeClosed(1, stories.size()).forEach(i -> {
            dialog.addHandler(Integer.toString(i), () ->
                    onStorySelected(storyLoader.getStories().get(i - 1)));
        });
    }

    private void onStorySelected(Story story) {
        this.story = story;
        loadGameState();
    }

    private void loadGameState() {
        var blankSlate = new GameState(story.getLocations().element().getId());
        persistence.loadGame(story.getFilename())
                .ifPresentOrElse(gs -> {
                    translate("engine_saved_game");
                    commandFactory.newModalDialog()
                            .addHandler(rb.getString("engine_yes"), () -> onGameStateLoaded(gs))
                            .addHandler(rb.getString("engine_no"), () -> onGameStateLoaded(blankSlate));
                }, () -> {
                    onGameStateLoaded(blankSlate);
                });
    }

    private void onGameStateLoaded(GameState gameState) {
        this.goodies = gameState.goodies();
        this.location = story.getLocation(gameState.currentLocation());
        //registers a new callback on the UI, to be called when the user enters a command
        print(location.toString());//print the description of the start location to the UI
    }

    public Location getCurrentLocation() {
        return location;
    }

    public void setCurrentLocation(Location location) {
        this.location = location;
    }

    private void onNext(String commandStr) {
        commandFactory.fromString(commandStr).accept(commandStr, this);
    }

    private void translate(String resourceKey) {
        print(rb.getString(resourceKey));
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

    public void gameCompleted() {
        persistence.saveGame(story.getFilename(), null);
        print("1: %s".formatted(rb.getString("engine_exit")));
        print("2: %s".formatted(rb.getString("engine_play_another")));
        commandFactory.newModalDialog()
                .addHandler("1", this::shutDown)
                .addHandler("2", this::selectStory);
    }

    public void saveAndQuit() {
        persistence.saveGame(story.getFilename(), new GameState(goodies, location.getId()));
        shutDown();
    }

    private void shutDown() {
        executor.shutdownNow();
        ui.shutdown();
    }

    public boolean isShutDown() {
        return executor.isShutdown();
    }


}

