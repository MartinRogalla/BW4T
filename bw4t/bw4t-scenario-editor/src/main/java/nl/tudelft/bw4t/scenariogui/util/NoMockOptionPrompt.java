package nl.tudelft.bw4t.scenariogui.util;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * The OptionPrompt that always returns no, used when mocking the
 * objects during tests, to prevent the system from hanging due
 * to having to press a button on the prompt.
 */
public class NoMockOptionPrompt implements OptionPrompt {

    /**
     * @param parentComponent The <code>java.awt.Component</code> that will be used to align the box.
     * @param message         The message to be shown to the user.
     * @param title           The title of the confirmation dialog
     * @param optionType      The option type
     * @param messageType     The message type
     * @return The chosen option
     */
    public int showConfirmDialog(Component parentComponent, Object message, String title,
                                 int optionType, int messageType) {
        return JOptionPane.NO_OPTION;
    }

    /**
     * This class will not show any message dialog.
     *
     * @param parentComponent The <code>java.awt.Component</code> that will be used to align the box.
     * @param message
     */
    public void showMessageDialog(Component parentComponent, Object message) {
        return;
    }
}