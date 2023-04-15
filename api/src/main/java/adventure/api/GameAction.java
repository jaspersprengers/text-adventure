package adventure.api;

import java.io.Serializable;
import java.util.function.Consumer;

public interface GameAction extends Consumer<Game>, Serializable {

}
