package adventure.api;

import java.util.Set;
import java.util.function.Consumer;

public interface Game {

    void start();

    void print(String s);

    void quit();

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

}
