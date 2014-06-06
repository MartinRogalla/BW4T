package nl.tudelft.bw4t.botstore.boteditor;

import static org.junit.Assert.assertEquals;
import nl.tudelft.bw4t.scenariogui.BW4TClientConfig;
import nl.tudelft.bw4t.scenariogui.BotConfig;
import nl.tudelft.bw4t.scenariogui.ScenarioEditor;
import nl.tudelft.bw4t.scenariogui.botstore.gui.BotEditor;
import nl.tudelft.bw4t.scenariogui.botstore.gui.BotEditorPanel;
import nl.tudelft.bw4t.scenariogui.editor.gui.ConfigurationPanel;
import nl.tudelft.bw4t.scenariogui.editor.gui.EntityPanel;
import nl.tudelft.bw4t.scenariogui.editor.gui.MainPanel;

import org.junit.Test;

/**
 * Test for boteditor
 */
public class BotEditorTest {

    /** The boteditor used to test */
    private BotEditor botEditor;

    /** Test the setup of boteditor */
    @Test
    public void testBotEditorPane() {
        EntityPanel entityPanel = new EntityPanel();
        ScenarioEditor main = new ScenarioEditor(new ConfigurationPanel(), entityPanel, new BW4TClientConfig());
        MainPanel parent = main.getMainPanel();
        main.getController().getModel().getBots().add(new BotConfig());
        botEditor = new BotEditor(parent, 0, main.getController().getModel());
        BotEditorPanel panel = new BotEditorPanel(botEditor, parent, main.getController().getModel());
        botEditor.setBotEditorPanel(panel);
        botEditor.setParent(parent);
        assertEquals(parent, botEditor.getParent());
        assertEquals(panel, botEditor.getBotEditorPanel());
        botEditor.setVisible(false);
    }

}