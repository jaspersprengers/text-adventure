package epics.secretkey;

import adventure.api.AbstractStory;
import adventure.api.Action;
import adventure.api.Exit;

import java.util.Locale;
import java.util.Set;

public class SecretKey extends AbstractStory {

    public SecretKey() {
        super("secret_key", Set.of(Locale.ENGLISH));
    }

    protected void setup() {
        var hall = addLocation("hall", """
                You are in the hall. There is a door NORTH.
                On the wall are a clock and a mirror.
                There is also a small table.            
                """);

        var kitchen = addLocation("kitchen", """ 
                You are in the kitchen. There is a door NORTH.
                There is a huge dog, sleeping.               
                """);

        var bathroom = addLocation("bathroom", """ 
                You are in the bathroom.                
                """);

        hall.addExit(Exit.to(kitchen).onMove(g ->
                g.setTimeout(5, g2 -> g.print("The dog starts growling."))
                        .setTimeout(15, g2 -> {
                            if (g2.isCurrentLocation("kitchen")) {
                                g.print("The dog has killed you. Game over.");
                                g.gameCompleted();
                            }
                        })));

        hall.addAction(Action.see("table", "There is a cup placed upside down on the table."));
        hall.addAction(Action.see("clock", "The time is ten minutes past midnight."));
        hall.addAction(Action.see("mirror", "You're looking at a puzzled person."));
        hall.addAction("lift cup", g -> {
            g.print("There is a key under the cup");
            g.getCurrentLocation().addAction(Action.take("key"));
        });

        kitchen.addExit(Exit.to(hall));
        kitchen.addExit(Exit.to(bathroom)
                .lock("Sorry, the door is locked")
                .onMove(g -> {
                    g.print("Congratulations! You won the game!");
                    g.gameCompleted();
                }));

        kitchen.addAction("unlock door", game -> {
            if (game.hasGoody("key")) {
                game.print("The door is unlocked");
                kitchen.getExit(bathroom).unlock();
            } else {
                game.print("What with?");
            }
        });
    }
}
