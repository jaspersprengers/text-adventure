package adventure;

import java.io.Console;

public class ConsoleInterface implements UserInterface {

    Console console;

    {
        console = System.console();
        if (console == null) {
            System.err.println("No console available.");
            System.exit(1);
        }
    }

    public String readLine(){
        String input = console.readLine();
        if (input == null || input.isBlank()) {
            System.err.println("Input cannot be empty");
            return readLine();
        }
        return input;
    }

    @Override public void printLine(String line) {
        System.out.println(line);
    }

}
