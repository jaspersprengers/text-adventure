package adventure.engine;

import adventure.api.Game;
import adventure.api.Location;
import adventure.api.Story;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MockGame implements Game {

    private Location newLocation;
    Set<String> goodies = new HashSet<>();

    @Override public void start() {
        System.out.println("MockGame.start()");
    }

    @Override public void print(String s) {
        System.out.println("MockGame.print(" + s + ")");
    }

    @Override public void quit() {
        System.out.println("MockGame.quit()");
    }

    @Override public void setCurrentLocation(Location newLocation) {
        this.newLocation = newLocation;
    }

    @Override public Location getCurrentLocation() {
        return newLocation;
    }

    @Override public Game setTimeout(long timeInSeconds, Consumer<Game> action) {
        return null;
    }

    @Override public boolean isCurrentLocation(String locationId) {
        return newLocation.getId().equals(locationId);
    }

    @Override public Set<String> getGoodies() {
        return goodies;
    }

    @Override public void addGoodie(String goodie) {
        goodies.add(goodie);
    }

    @Override public boolean hasGoody(String goody) {
        return goodies.contains(goody);
    }

}
