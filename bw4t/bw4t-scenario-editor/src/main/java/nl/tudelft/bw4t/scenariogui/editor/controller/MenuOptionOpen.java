package nl.tudelft.bw4t.scenariogui.editor.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;

import nl.tudelft.bw4t.agent.EntityType;
import nl.tudelft.bw4t.scenariogui.BW4TClientConfig;
import nl.tudelft.bw4t.scenariogui.ScenarioEditor;
import nl.tudelft.bw4t.scenariogui.gui.MenuBar;
import nl.tudelft.bw4t.scenariogui.panel.gui.ConfigurationPanel;
import nl.tudelft.bw4t.scenariogui.panel.gui.EntityPanel;
import nl.tudelft.bw4t.scenariogui.util.FileFilters;

/**
 * Handles the event to open a file.
 * <p>
 * 
 * @version 0.1
 * @since 12-05-2014
 */
class MenuOptionOpen extends AbstractMenuOption {

	/**
	 * Constructs a new menu option open object.
	 * 
	 * @param view
	 *            The view.
	 * @param mainView
	 *            The controlling main view.
	 * @param model           
	 *            The model.
	 */
	public MenuOptionOpen(final MenuBar view,
			final ScenarioEditorController mainView, BW4TClientConfig model) {
		super(view, mainView, model);
	}

	/**
	 * Gets called when the menu option open button is pressed.
	 * 
	 * @param e
	 *            The action event.
	 */
	// TODO: Split up in multiple shorter methods
	public void actionPerformed(final ActionEvent e) {
		ConfigurationPanel configPanel = super.getController().getMainView()
				.getMainPanel().getConfigurationPanel();
		EntityPanel entityPanel = super.getController().getMainView()
				.getMainPanel().getEntityPanel();

		// Check if current config is different from last saved config
		if (getController().hasConfigBeenModified()) {
			// Check if user wants to save current configuration
			int response = ScenarioEditor.getOptionPrompt().showConfirmDialog(
					null, ScenarioEditorController.CONFIRM_SAVE_TXT, "",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (response == JOptionPane.YES_OPTION) {
				saveFile();
				super.getController().getMainView().getMainPanel()
						.getConfigurationPanel().updateOldValues();
				super.getController().getModel().updateBotConfigs();
				super.getController().getModel().updateEpartnerConfigs();
			}
		}

		// Open configuration file
		JFileChooser fileChooser = getCurrentFileChooser();
		fileChooser.setFileFilter(FileFilters.xmlFilter());

		if (fileChooser.showOpenDialog(getController().getMainView()) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String openedFile = fileChooser.getSelectedFile().toString();

			try {
				BW4TClientConfig configuration = BW4TClientConfig.fromXML(file
						.getAbsolutePath());

				// Fill the configuration panel
				configPanel.setClientIP(configuration.getClientIp());
				configPanel.setClientPort("" + configuration.getClientPort());
				configPanel.setServerIP(configuration.getServerIp());
				configPanel.setServerPort("" + configuration.getServerPort());
				configPanel.setUseGui(configuration.isLaunchGui());
				configPanel.setMapFile(configuration.getMapFile());

				// clear bots/epartners from the previous config
				resetBotTable(entityPanel);
				resetEpartnerTable(entityPanel);
				
				super.getModel().getBots().clear();
                super.getModel().getEpartners().clear();

				// Fill the bot panel
				int botRows = configuration.getBots().size();

				for (int i = 0; i < botRows; i++) {
					String botName = configuration.getBot(i).getBotName();
					EntityType botController = configuration.getBot(i)
							.getBotController();
					String botAmount = Integer.toString(configuration.getBot(i)
							.getBotAmount());
					Object[] botObject = {botName, botController, botAmount };
					entityPanel.getBotTableModel().addRow(botObject);
					super.getModel().getBots().add(configuration.getBot(i));
				}
				
				// Fill the epartner panel
				int epartnerRows = configuration.getEpartners().size();

				for (int i = 0; i < epartnerRows; i++) {
					String epartnerName = configuration.getEpartner(i).getEpartnerName();
					String epartnerAmount = Integer.toString(configuration.getEpartner(i)
							.getEpartnerAmount());
					Object[] epartnerObject = {epartnerName, epartnerAmount };
					entityPanel.getEPartnerTableModel().addRow(epartnerObject);
					super.getModel().getEpartners().add(configuration.getEpartner(i));
				}
			} catch (JAXBException e1) {
				ScenarioEditor.handleException(e1,
						"Error: Opening the XML has failed.");
			} catch (FileNotFoundException e1) {
				ScenarioEditor.handleException(e1,
						"Error: No file has been found. ");
			}

			// set last file location to the opened file so that the previous
			// saved file won't get
			// overwritten when the new config is saved.
			super.getMenuView().setLastFileLocation(openedFile);
            getController().getMainView().setWindowTitle(file.getName());
		}
		super.getController().getMainView().getMainPanel()
				.getConfigurationPanel().updateOldValues();
		super.getModel().updateBotConfigs();
		super.getModel().updateEpartnerConfigs();
	}

	/**
	 * Reset the list with bots.
	 * 
	 * @param entityPanel
	 *            The EntityPanel which contains the bot list.
	 */
	public void resetBotTable(EntityPanel entityPanel) {
		DefaultTableModel botTable = entityPanel.getBotTableModel();
		int rows = botTable.getRowCount();

		if (rows > 0) {
			for (int i = rows - 1; i >= 0; i--) {
				botTable.removeRow(i);
			}
		}
	}

	/**
	 * Reset the list with epartners.
	 * 
	 * @param entityPanel
	 *            The EntityPanel which contains the epartner list.
	 */
	public void resetEpartnerTable(EntityPanel entityPanel) {
		DefaultTableModel epartnerTable = entityPanel.getEPartnerTableModel();
		int rows = epartnerTable.getRowCount();

		if (rows > 0) {
			for (int i = rows - 1; i >= 0; i--) {
				epartnerTable.removeRow(i);
			}
		}
	}
}