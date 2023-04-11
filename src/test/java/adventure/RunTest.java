package adventure;

import adventure.stories.SecretKey;
import adventure.stories.Story;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RunTest {

    TestUI ui = new TestUI();
    Story story = new SecretKey();
    Game game = new Game(story, ui);

    @Test
    void runTest() {

        game.start();
        proceed("lift cup");
        assertThat(ui.lastMessage()).isEqualTo("There is a key under the cup");
        proceed("take key");
        assertThat(ui.lastMessage()).isEqualTo("You took the key");
        proceed("N");
        assertThat(game.getCurrentLocation().getId()).isEqualTo("kitchen");
        proceed("unlock door");
        assertThat(ui.lastMessage()).isEqualTo("The door is unlocked");
        proceed("N");
        assertThat(ui.lastMessage()).isEqualTo("Congratulations! You won the game!");
        assertThat(game.isInProgress()).isFalse();
    }

    void proceed(String command) {
        ui.addLine(command);
        game.next();
    }

}

