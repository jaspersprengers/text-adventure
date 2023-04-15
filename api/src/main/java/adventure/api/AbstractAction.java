package adventure.api;


import java.io.Serializable;
import java.util.function.Consumer;

public abstract class AbstractAction implements Consumer<Game>, Serializable {

    private int counter;

    protected void incrementCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
