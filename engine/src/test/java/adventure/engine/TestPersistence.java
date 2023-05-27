package adventure.engine;

import java.util.Optional;

public class TestPersistence implements Persistence{

    private String story;
    private GameState gameState;

    @Override public void saveGame(String storyName, GameState gameState) {
        this.story = story;
        this.gameState = gameState;
    }

    @Override public Optional<GameState> loadGame(String name) {
        return Optional.ofNullable(gameState);
    }

    public String getStory() {
        return story;
    }

    public GameState getGameState() {
        return gameState;
    }
}
