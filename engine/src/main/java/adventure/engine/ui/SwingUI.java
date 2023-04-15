package adventure.engine.ui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class SwingUI implements UserInterface {

    private Consumer<String> onNext;
    final JFrame frame;
    final JTextField textField;
    final JTextArea textArea;

    public SwingUI() {
        frame = new JFrame("Adventure game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // create the text input field
        textField = new JTextField();
        textArea = new JTextArea();


        textField.setBounds(10, 10, 200, 30);
        textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = textField.getText();
                    if (onNext != null && text != null) {
                        textArea.setText("");
                        textArea.append("You: "+text + "\n");
                        textField.setText("");
                        onNext.accept(text);
                    }
                }
            }
        });
        // create the text output area
        textArea.setBounds(10, 50, 400, 300);
        textArea.setEditable(false);

        // add the text input field and text output area to the frame
        frame.add(textField);
        frame.add(textArea);

        // set the size and visibility of the frame
        frame.setSize(450, 400);
        frame.setLayout(null);
        frame.setVisible(true);
    }


    @Override public void onNextLine(Consumer<String> onNext) {
        this.onNext = onNext;
    }

    @Override public void printLine(String line) {
        textArea.append(line+'\n');
    }

    @Override public void shutdown() {
        frame.dispose();
    }
}
