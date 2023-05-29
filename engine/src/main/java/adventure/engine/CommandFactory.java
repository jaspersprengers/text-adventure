package adventure.engine;

import java.util.ResourceBundle;

public class CommandFactory {

    private ResourceBundle rb;
    private ModalDialog modalDialog;

    public ModalDialog newModalDialog(){
        this.modalDialog = new ModalDialog(rb);
        return this.modalDialog;
    }

    public Command fromString(String raw) {
        if (raw == null || raw.isBlank()) {
            return new InvalidCommand(rb);
        }
        String line = raw.toLowerCase().trim();
        if (modalDialog != null && !modalDialog.isSuccess()) {
            return modalDialog;
        }

        if (line.startsWith(rb.getString("cmd_go"))) {
            return new DirectionCommand(rb);
        } else if (line.equals(rb.getString("cmd_help"))) {
            return new HelpCommand(rb);
        } else if (line.equals(rb.getString("cmd_quit"))) {
            return new QuitCommand(rb);
        }
        return new QueryCommand(rb);
    }

    public void setResourceBundle(ResourceBundle rb) {
        this.rb = rb;
        this.modalDialog = new ModalDialog(rb);
    }
}
