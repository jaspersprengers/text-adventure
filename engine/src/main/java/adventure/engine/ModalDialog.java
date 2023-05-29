package adventure.engine;

import adventure.api.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

class ModalDialog extends Command {

    private boolean success;
    private final Map<String, Procedure> handlers = new HashMap<>();

    public ModalDialog(ResourceBundle rb) {
        super(rb);
    }

    @Override public void accept(String text, Game game) {
        this.success = Optional.ofNullable(handlers.get(text.toLowerCase())).map(consumer -> {
            consumer.execute();
            return true;
        }).orElse(false);
    }

    ModalDialog addHandler(String key, Procedure handler) {
        handlers.put(key.toLowerCase(), handler);
        return this;
    }

    public boolean isSuccess() {
        return success;
    }
}
