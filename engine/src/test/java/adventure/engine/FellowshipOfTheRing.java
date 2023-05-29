package adventure.engine;

import adventure.api.AbstractStory;
import adventure.api.Exit;

import java.util.Locale;
import java.util.Set;

public class FellowshipOfTheRing extends AbstractStory {

    public FellowshipOfTheRing() {
        super("Fellowship of the Ring", Set.of(Locale.ENGLISH));
    }

    protected void setup() {
        var outside = addLocation("hall", """
                You are standing in front of the mine entrance.
                Behind you is a shallow pool of water.
                """);

        var mineOfMoria = addLocation("mine entrance", """ 
                You are in the mines of Moria.
                """);

        outside.addExit(Exit.to(mineOfMoria).lock("You can't go there yet").onMove(g -> {
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
            g.print("The wall opens up to reveal an entrance to the mineOfMoria");
            outside.getExit(mineOfMoria).unlock();
        });
    }
}
