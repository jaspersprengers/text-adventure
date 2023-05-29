package newline;

import adventure.api.AbstractStory;
import adventure.api.Exit;
import adventure.api.Story;

import java.util.Locale;
import java.util.Set;

public class FellowshipOfTheRing extends AbstractStory implements Story {

    public FellowshipOfTheRing() {
        super("Fellowship of the Ring", Set.of(Locale.ENGLISH));
    }

    protected void setup() {

        var outside = addLocation("hall", """
                You are standing in front of a great wall of rock.
                Behind you is a shallow pool of water.
                """);

        var mine = addLocation("mine", """ 
                You are in the mines of Moria.
                """);

        outside.addExit(Exit.to(mine).lock("You can't go there yet").onMove(g -> {
            g.print("""
                    Congratulations for making it thus far!
                    The story continues on disk two.'
                    """);
            g.gameCompleted();
        }));

        outside.addAction("see wall", g -> {
            g.print("There's a text that reads: 'Speak friend, and enter'");
        });
        outside.addAction("say mellon", g -> {
            g.print("The wall opens up to reveal an entrance to the mine");
            outside.getExit(mine).unlock();
        });
    }
}
