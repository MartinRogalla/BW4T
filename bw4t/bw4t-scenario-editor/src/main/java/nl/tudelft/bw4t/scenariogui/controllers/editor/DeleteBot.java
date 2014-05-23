package nl.tudelft.bw4t.scenariogui.controllers.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nl.tudelft.bw4t.scenariogui.gui.panel.MainPanel;

/**
 * Handles the event to delete a bot.
 */
class DeleteBot implements ActionListener {

    /**
     * The <code>MainPanel</code> serving as the content pane.
     */
    private MainPanel view;

    /**
     * Create an DeleteBot event handler.
     *
     * @param newView The parent view.
     */
    public DeleteBot(final MainPanel newView) {
        this.view = newView;
    }

    /**
     * Executes action that needs to happen when the "Delete bot" button is
     * pressed.
     * Gets called when the delete button is pressed.
     *
     * @param ae The action event.
     */
    public void actionPerformed(final ActionEvent ae) {
        int row = view.getEntityPanel().getSelectedBotRow();

        if (row == -1) {
            return;
        }

        int response = view.getEntityPanel().showConfirmDialog(null,
                "Are you sure you want to delete this bot?", "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            view.getEntityPanel().getBotTableModel().removeRow(row);
        }
    }
}