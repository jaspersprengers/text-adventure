package adventure.engine.ui;

import java.util.function.Consumer;

public interface UserInterface {
    void onNextLine(Consumer<String> onNext);
    void printLine(String line);
    void shutdown();
}
