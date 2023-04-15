package epics.secretkey;

import adventure.api.ActionOption;
import adventure.api.Exit;
import adventure.api.Location;
import adventure.api.Story;

public class SecretKey extends Story {

    public SecretKey() {
        super("secret_key");
    }

    public Location getStartLocation() {
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
                                g.quit();
                            }
                        })));

        hall.addActionable(ActionOption.inspectObject("table", "There is a cup placed upside down on the table."));
        hall.addActionable(ActionOption.inspectObject("clock", "The time is ten minutes past midnight."));
        hall.addActionable(ActionOption.inspectObject("mirror", "You're looking at a puzzled person."));
        hall.addActionable("lift cup", g -> {
            g.print("There is a key under the cup");
            g.getCurrentLocation().addActionable(ActionOption.takeObject("key"));
        });

        kitchen.addExit(Exit.to(hall));
        kitchen.addExit(Exit.to(bathroom)
                .lock("Sorry, the door is locked")
                .onMove(g -> {
                    g.print("Congratulations! You won the game!");
                    g.conclude();
                }));

        kitchen.addActionable("unlock door", game -> {
            if (game.hasGoody("key")) {
                game.print("The door is unlocked");
                kitchen.getExit(bathroom).unlock();
            } else {
                game.print("What with?");
            }
        });
        return hall;
    }
}
