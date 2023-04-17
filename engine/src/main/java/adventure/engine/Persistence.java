package adventure.engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.Optional;

public class Persistence {

    public void saveGame(String storyName, GameState gameState) {
        //get path to user home dir
        var home = Paths.get(System.getProperty("user.home"));
        var fileName = Paths.get("%s.ser".formatted(storyName));
        var file = home.resolve(fileName).toFile();
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(gameState);
        } catch (IOException e) {
            System.err.println("Could not save game for file %s".formatted(file));
            e.printStackTrace();
        }
    }

    public Optional<GameState> loadGame(String name) {
        var home = Paths.get(System.getProperty("user.home"));
        var fileName = Paths.get("%s.ser".formatted(name));
        var file = home.resolve(fileName).toFile();
        try {
            Object gameState = new ObjectInputStream(new FileInputStream(file)).readObject();
            return Optional.of((GameState) gameState);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Could not load game for file %s".formatted(file));
            return Optional.empty();
        }
    }

}
