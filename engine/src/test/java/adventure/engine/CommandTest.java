package adventure.engine;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandTest {

    @Test
    public void standard_commands(){
        var goNorth = Command.parse("go north");
        assertThat(goNorth).isInstanceOf(DirectionCommand.class);
        assertThat(goNorth.getText()).isEqualTo("north");
    }

}
