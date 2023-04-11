package adventure;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;

public class TestUI implements UserInterface {

    @Getter
    LinkedList<String> messages = new LinkedList<>();

    private LinkedList<String> lines;

    public TestUI(String... lines) {
        this(new LinkedList<>(Arrays.asList(lines))); // convert array to list
    }

    public TestUI(LinkedList<String> lines) {
        this.lines = lines;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    @Override
    public String readLine() {
        return lines.poll(); // remove first element
    }

    @Override public void printLine(String line) {
        System.out.println(line);
        messages.add(line);
    }

    public String lastMessage(){
        return messages.getLast();
    }

}
