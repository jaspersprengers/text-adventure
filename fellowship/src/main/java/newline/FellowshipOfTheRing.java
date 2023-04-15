package newline;

import adventure.api.Exit;
import adventure.api.Location;
import adventure.api.Story;

public class FellowshipOfTheRing extends Story {

    public FellowshipOfTheRing() {
        super("Fellowship of the Ring");
    }

    public Location getStartLocation() {
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
            g.conclude();
        }));

        outside.addActionable("see wall", g -> {
            g.print("There's a text that reads: 'Speak friend, and enter'");
        });
        outside.addActionable("say mellon", g -> {
            g.print("The wall opens up to reveal an entrance to the mine");
            outside.getExit(mine).unlock();
        });


        return outside;
    }
}
