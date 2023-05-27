package adventure.engine;

import java.util.Optional;

public interface Persistence {

    void saveGame(String storyName, GameState gameState);

    Optional<GameState> loadGame(String name);
}
