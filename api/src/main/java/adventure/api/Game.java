package adventure.api;

import java.util.function.Consumer;

public interface Game {

    void load(String text);

    void start();

    void print(String s);

    void quit();

    void setCurrentLocation(Location newLocation);

    Location getCurrentLocation();

    void conclude();

    Game setTimeout(long timeInSeconds, Consumer<Game> action);

    boolean isCurrentLocation(String locationId);

    void addGoodie(String goodie);

    boolean hasGoody(String goody);

    void startStory(Story loadStory);
}
