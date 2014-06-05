package nl.tudelft.bw4t.scenariogui.controllers.editor;


import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import nl.tudelft.bw4t.agent.EntityType;
import nl.tudelft.bw4t.scenariogui.BW4TClientConfig;
import nl.tudelft.bw4t.scenariogui.BotConfig;
import nl.tudelft.bw4t.scenariogui.gui.panel.MainPanel;

/**
 * Handles the event when a cell in the bot table is edited.
 * <p>
 * @author      Nick Feddes
 * @version     0.1                
 * @since       27-05-2014        
 */
public class EditBotTable implements TableModelListener {

    /**
     * The <code>MainPanel</code> serving as the content pane.
     */
    private MainPanel view;
    
    private BW4TClientConfig model;

    /**
     * Create a EditBotTable event handler.
     *
     * @param newView The parent view.
     */
    public EditBotTable(final MainPanel newView, BW4TClientConfig model) {
        this.view = newView;
        this.model = model;
    }

    /**
     * Gets called when a cell in the table was changed.
     * Updates the config object with the new data.
     * @param event The event.
     */
    @Override
    public void tableChanged(TableModelEvent event) {
        if (event.getColumn() == -1) 
            return;
        model.getAmountBot();
        BotConfig config = model.getBots().get(event.getFirstRow());
        Object value =  view.getEntityPanel().getBotTable().getValueAt(
                event.getFirstRow(), event.getColumn());
        switch (event.getColumn()) {
        case 0:
            config.setBotName((String) value);
            break;
        case 1:
            EntityType botController = EntityType.getType((String) value);

            config.setBotController(botController);
            break;
        case 2:
            config.setBotAmount(Integer.parseInt((String) value));
            break;
         default:
            break;
        }
    }
    
}
