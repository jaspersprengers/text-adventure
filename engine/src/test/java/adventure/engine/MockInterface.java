package adventure.engine;

import adventure.api.UserInterface;

import java.util.function.Consumer;

public class MockInterface implements UserInterface {

    private Consumer<String> onNext;

    public void enter(String text) {
        System.out.println("Entered:" + text.indent(2));
        onNext.accept(text);
    }

    @Override public void onNextCommand(Consumer<String> commandString) {
        this.onNext = commandString;
    }

    @Override public void printLine(String line) {
        System.out.println(line);
    }

    @Override public void shutdown() {
        System.out.println("Shutting down...");
    }
}
