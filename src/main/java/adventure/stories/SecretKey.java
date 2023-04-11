package adventure.stories;

import adventure.Action;
import adventure.Direction;
import adventure.Exit;
import adventure.Location;

public class SecretKey extends Story {

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

        hall.addExit(Exit.North(kitchen).setOnMove(g ->
                g.setTimeout(5, g2 -> g.print("The dog starts growling."))
                        .setTimeout(15, g2 -> {
                            if (g2.isCurrentLocation("kitchen")) {
                                g.print("The dog has killed you. Game over.");
                                g.quit();
                            }
                        })));

        hall.addActionable(Action.inspectObject("table", "There is a cup placed upside down on the table."));
        hall.addActionable(Action.inspectObject("clock", "The time is ten minutes past midnight."));
        hall.addActionable(Action.inspectObject("mirror", "You're looking at a puzzled person."));
        hall.addActionable("lift cup", g -> {
            g.print("There is a key under the cup");
            g.getCurrentLocation().addActionable(Action.takeObject("key"));
        });

        kitchen.addExit(Exit.South(hall));
        kitchen.addExit(Exit.North(bathroom)
                .lock("Sorry, the door is locked")
                .setOnMove(g -> {
                    g.print("Congratulations! You won the game!");
                    g.quit();
                }));

        kitchen.addActionable("unlock door", game -> {
            if (game.hasGoody("key")) {
                game.print("The door is unlocked");
                kitchen.getExit(Direction.NORTH).ifPresent(Exit::unlock);
            } else {
                game.print("What with?");
            }
        });
        return hall;
    }
}
