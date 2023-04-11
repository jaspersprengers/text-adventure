package adventure;

import adventure.stories.SecretKey;
import adventure.stories.Story;

public class Adventure {


    public static void main(String[] args) {

        Story story = new SecretKey();
        Game game = new Game(story, new ConsoleInterface());
        game.start().loopUntilDone();
    }


}
