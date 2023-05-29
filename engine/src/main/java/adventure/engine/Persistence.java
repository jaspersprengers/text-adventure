package adventure.engine;

import java.util.Optional;

public interface Persistence {

    /**
     * Saves gameState to file, or clears an existing file
     * @param storyName is resolved to filename
     * @param gameState if null, clear any file
     */
    void saveGame(String storyName, GameState gameState);

    Optional<GameState> loadGame(String name);
}
