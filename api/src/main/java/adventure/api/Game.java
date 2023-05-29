package adventure.api;

import java.util.Set;
import java.util.function.Consumer;

public interface Game {

    /**
     * Starts the game
     */
    void start();

    /**
     * Prints a line of text to the interface
     */
    void print(String s);

    /**
     * Quits the engine mid-game, persisting the current state to file.
     */
    void saveAndQuit();

    /**
     * Called when the game has been completed, successfully or not
     */
    void gameCompleted();

    void setCurrentLocation(Location newLocation);

    Location getCurrentLocation();

    Game setTimeout(long timeInSeconds, Consumer<Game> action);

    default boolean isCurrentLocation(String locationId){
        return getCurrentLocation().getId().equals(locationId);
    }

    Set<String> getGoodies();

    default void addGoodie(String goodie){
        getGoodies().add(goodie);
    }

    default boolean hasGoody(String goody){
        return getGoodies().contains(goody);
    }

    boolean isShutDown();

}
