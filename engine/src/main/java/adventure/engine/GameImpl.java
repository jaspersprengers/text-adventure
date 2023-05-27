package adventure.engine;


import adventure.api.Game;
import adventure.api.Location;
import adventure.api.Story;
import adventure.api.UserInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
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

    //game state
    private Locale locale;
    private Location location;
    private Story story;
    private Set<String> goodies = new HashSet<>();
    private MandatoryQuery mandatoryQuery;

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
        newMandatoryQuery().addHandler("1", i -> {
            onLocaleSelected(Locale.ENGLISH);
        }).addHandler("2", i -> {
            onLocaleSelected(new Locale("eo"));
        });
    }

    private void onLocaleSelected(Locale locale) {
        this.locale = locale;
        if (storyLoader.getNames().isEmpty()) {
            print("No stories to choose from");
            shutDown();
            return;
        }
        print(storyLoader.getWelcomeText());
        newMandatoryQuery();
        IntStream.rangeClosed(1, storyLoader.getStories().size()).forEach(i -> {
            mandatoryQuery.addHandler(Integer.toString(i), input ->
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
                    print("There is a saved game. Resume? (Y/N)");
                    newMandatoryQuery()
                            .addHandler("Y", i -> onGameStateLoaded(gs))
                            .addHandler("N", i -> onGameStateLoaded(blankSlate));
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
        if (mandatoryQuery != null) {
            if (mandatoryQuery.check(commandStr)) {
                mandatoryQuery = null;
            }
        } else {
            Command.parse(commandStr).accept(this);
        }
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

    public boolean isShutDown() {
        return executor.isShutdown();
    }

    MandatoryQuery newMandatoryQuery() {
        mandatoryQuery = new MandatoryQuery();
        return mandatoryQuery;
    }

    class MandatoryQuery {

        private final Map<String, Consumer<String>> handlers = new HashMap<>();

        boolean check(String input) {
            return Optional.ofNullable(handlers.get(input.toLowerCase())).map(consumer -> {
                consumer.accept(input);
                return false;
            }).orElseGet(() -> {
                print("Invalid option!");
                return true;
            });
        }

        MandatoryQuery addHandler(String key, Consumer<String> handler) {
            handlers.put(key.toLowerCase(), handler);
            return this;
        }


    }


}

