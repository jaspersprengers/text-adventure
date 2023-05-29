package adventure.engine;

import adventure.api.AbstractStory;
import adventure.api.Game;
import adventure.api.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GameTest {

    MockInterface ui = new MockInterface();
    TestPersistence testPersistence = new TestPersistence();
    GameState state = new GameState("mine entrance");
    Game game = new GameImpl(ui,
            new TestStoryLoader(List.of(new StarTrekAdventure(), new FellowshipOfTheRing())),
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
        ui.enter("2");//fellowship game
        ui.enter("see wall");
        ui.enter("say mellon");
        ui.enter("go mine entrance");
        ui.enter("1");//exit
        Assertions.assertThat(game.isShutDown()).isTrue();
    }

    @Test
    public void runGameIgnoreSavedState() {
        testPersistence.saveGame("fellowShip", state);

        game.start();
        ui.enter("1");//English
        ui.enter("2");//fellowship game
        ui.enter("N");//ignore saved game
        ui.enter("see wall");
        ui.enter("say mellon");
        ui.enter("go mine entrance");
        ui.enter("1");//exit
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

    @Test
    public void runGameAndPlayAnother() {
        game.start();
        ui.enter("1");//language
        ui.enter("2");//adventure 2
        ui.enter("Y");
        ui.enter("see wall");
        ui.enter("say mellon");
        ui.enter("go mine entrance");
        ui.enter("2");//play another game
        ui.enter("1");//game one
        Assertions.assertThat(game.getCurrentLocation().getId()).isEqualTo("planet");
    }

    @Test
    public void playEsperantoGame() {
        game.start();
        ui.enter("2");//language
        ui.enter("1");//adventure 2
        ui.enter("diru suprenradiumu min, Skoti");
        ui.enter("1");//play another game
        Assertions.assertThat(game.isShutDown()).isTrue();
    }

    static class StarTrekAdventure extends AbstractStory implements Story {

        public StarTrekAdventure() {
            super("Star Trek", Set.of(Locale.ENGLISH, new Locale("eo")));
        }

        protected void setup() {
            addLocation("planet", resourceBundle.getString("planet_description")).addAction(resourceBundle.getString("command_text"), g -> {
                g.print(resourceBundle.getString("on_complete"));
                g.gameCompleted();
            });
        }
    }


}
