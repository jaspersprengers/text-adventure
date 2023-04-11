package adventure;

import java.util.Optional;

public enum Direction {
    UP("u"),
    DOWN("d"),
    NORTH("n"),
    SOUTH("s"),
    EAST("e"),
    WEST("w");

    private final String shortText;

    Direction(String shortText) {
        this.shortText = shortText;
    }

    public static Optional<Direction> fromString(String text) {
        for (Direction direction : Direction.values()) {
            if (direction.shortText.equalsIgnoreCase(text)) {
                return Optional.of(direction);
            }
        }
        return Optional.empty();
    }

}
