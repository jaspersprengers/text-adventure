package adventure.engine.ui;

import java.util.function.Consumer;

public interface UserInterface {
    void onNextCommand(Consumer<String> commandString);
    void printLine(String line);
    void shutdown();
}
