package nl.tudelft.bw4t.scenariogui.util;

import java.awt.Component;

/**
 * An interface for showConfirmDialog, used to make the mocking of responses
 * simpler.
 * <p>
 * @author        
 * @version     0.1                
 * @since       12-05-2014        
 */
public interface OptionPrompt {

    /**
     * @param parentComponent The <code>java.awt.Component</code> that will be used to align the box.
     * @param message         The message to be shown to the user.
     * @param title           The title of the confirmation dialog
     * @param optionType      The option type
     * @param messageType     The message type
     * @return The chosen option
     */
    int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType);

    /**
     *
     * @param parentComponent The <code>java.awt.Component</code> that will be used to align the box.
     * @param message
     */
    void showMessageDialog(Component parentComponent, Object message);
}




