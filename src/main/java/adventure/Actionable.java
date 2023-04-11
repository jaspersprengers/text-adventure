package adventure;

import lombok.Getter;

import java.util.function.Consumer;

public abstract class Actionable implements Consumer<Game> {

    @Getter
    private int counter;

    protected void incrementCounter() {
        counter++;
    }
}
