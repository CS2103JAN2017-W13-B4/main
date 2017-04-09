package guitests;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import seedu.doist.ui.CommandBox;

public class ModeTest extends DoistGUITest {

    @Test
    public void testEditingModeAfterLaunch() {
        assertInEditingMode();
    }

    @Test
    public void testTurnOnNavigationMode() {
        typeEsc();
        assertInNavigationMode();
    }

    @Test
    public void testTurnOnEditingMode() {
        typeEsc();
        assertInNavigationMode();

        typeEsc();
        assert getMessageBoxContent().equals(CommandBox.EDITING_MODE_MESSAGE);
        assertInEditingMode();
    }

    private void typeEsc() {
        GuiRobot bot = new GuiRobot();
        bot.press(KeyCode.ESCAPE);
        bot.release(KeyCode.ESCAPE);
    }

    private String getInputBoxContent() {
        return commandBox.getCommandInput();
    }

    private String getMessageBoxContent() {
        return resultDisplay.getText();
    }

    private void assertInNavigationMode() {
        assert getMessageBoxContent().equals(CommandBox.NAVIGATION_MODE_MESSAGE);
        commandBox.enterCommand("some random input");
        System.out.println(getInputBoxContent());
        assert getInputBoxContent().length() == 0;
    }

    private void assertInEditingMode() {
        assert !getMessageBoxContent().equals(CommandBox.NAVIGATION_MODE_MESSAGE);
        String input = "some randome input";
        commandBox.enterCommand(input);
        assert getInputBoxContent().equals(input);
    }
}










