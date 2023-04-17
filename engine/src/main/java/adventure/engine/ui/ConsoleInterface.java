package adventure.engine.ui;

import java.io.Console;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ConsoleInterface implements UserInterface {

    private Consumer<String> onNext;
    private final Console console;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    {
        console = System.console();
        if (console == null) {
            System.err.println("No console available.");
            System.exit(1);
        }
        executor.scheduleWithFixedDelay(() -> {
            String nextLine = console.readLine();
            if (onNext != null && nextLine != null) {
                onNext.accept(nextLine);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    @Override public void onNextCommand(Consumer<String> commandString) {
        this.onNext = commandString;
    }

    @Override public void printLine(String line) {
        System.out.println(line);
    }

    @Override public void shutdown() {
        executor.shutdownNow();
    }

}
