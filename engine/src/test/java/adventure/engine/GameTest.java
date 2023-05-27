package adventure.engine;

import adventure.api.AbstractStory;
import adventure.api.Game;
import adventure.api.Story;
import adventure.api.UserInterface;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GameTest {

    MockInterface ui = new MockInterface();
    TestPersistence testPersistence = new TestPersistence();
    GameState state = new GameState("mine entrance");
    Game game = new GameImpl(ui,
            new TestStoryLoader(List.of(new SillyAdventure(), new FellowshipOfTheRing())),
            testPersistence
    );

    @Test
    public void runGameWithoutStory() {
        Game game = new GameImpl(ui);
        game.start();
        ui.enter("1");//language
        Assertions.assertThat(game.isShutDown()).isTrue();
    }

    @Test
    public void runGameWithoutSavedState() {
        game.start();
        ui.enter("1");//language
        ui.enter("2");
        ui.enter("see wall");
        ui.enter("say mellon");
        ui.enter("go mine entrance");
        Assertions.assertThat(game.isShutDown()).isTrue();
    }

    @Test
    public void runGameIgnoreSavedState() {
        testPersistence.saveGame("fellowShip", state);

        game.start();
        ui.enter("1");//language
        ui.enter("2");
        ui.enter("N");
        ui.enter("see wall");
        ui.enter("say mellon");
        ui.enter("go mine entrance");
        Assertions.assertThat(game.isShutDown()).isTrue();
    }

    @Test
    public void runGameWithPriorState() {
        testPersistence.saveGame("fellowShip", state);

        game.start();
        ui.enter("1");//language
        ui.enter("2");
        ui.enter("Y");
        Assertions.assertThat(game.getCurrentLocation().getId()).isEqualTo("mine entrance");
    }

    static class SillyAdventure extends AbstractStory implements Story {

        public SillyAdventure() {
            super("Silly adventure");
            addLocation("finished", "You're already there");
        }
    }


}
