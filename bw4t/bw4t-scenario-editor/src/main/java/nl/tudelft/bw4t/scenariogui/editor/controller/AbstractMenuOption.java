package nl.tudelft.bw4t.scenariogui.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import nl.tudelft.bw4t.scenariogui.BW4TClientConfig;
import nl.tudelft.bw4t.scenariogui.DefaultConfigurationValues;
import nl.tudelft.bw4t.scenariogui.ScenarioEditor;
import nl.tudelft.bw4t.scenariogui.editor.gui.MenuBar;
import nl.tudelft.bw4t.scenariogui.util.FileFilters;
import nl.tudelft.bw4t.scenariogui.util.MapSpec;

/**
 * Handles the event of the menu.
 * <p>
 * 
 * @version 0.1
 * @since 12-05-2014
 */
public abstract class AbstractMenuOption implements ActionListener {

	private MenuBar view;
	
	private BW4TClientConfig model;

	private ScenarioEditorController controller;

	// made a variable for this so we can call it during testing
	private JFileChooser currentFileChooser;

	/**
	 * Constructs a menu option object.
	 * 
	 * @param newView
	 *            The new view.
	 * @param mainView
	 *            The main view controllers.
	 * @param model
	 *            The model.
	 */
	public AbstractMenuOption(final MenuBar newView,
			final ScenarioEditorController mainView, BW4TClientConfig model) {
		this.view = newView;
		this.setController(mainView);
		this.model = model;

		/*
		 * Set the intial file chooser and option prompt, can eventually be
		 * changed when tests need to be ran.
		 */
		setCurrentFileChooser(new JFileChooser());
	}

	/**
	 * Gets the current file chooser.
	 * 
	 * @return The current file chooser.
	 */
	public JFileChooser getCurrentFileChooser() {
		return currentFileChooser;
	}

	/**
	 * Sets the new current file chooser.
	 * 
	 * @param newFileChooser
	 *            The new file chooser to set.
	 */
	public void setCurrentFileChooser(final JFileChooser newFileChooser) {
		currentFileChooser = newFileChooser;
	}

	/**
	 * Saves a file.
	 */
	public void saveFile() {
		saveFile(!view.hasLastFileLocation());
	}

	/**
	 * Saves the configuration to XML. When the configuration hasn't been saved
	 * before an file chooser is opened.
	 * 
	 * @param saveAs
	 *            Whether or not to open a file chooser.
	 */
	public void saveFile(final boolean saveAs) {
        if (validateBotCount() && verifyMapSelected()) {

            String path = view.getLastFileLocation();
            // Check if the previous save location exists.
            if (wasPreviousSaveRemoved(path)) {
                view.setLastFileLocation(null);
                currentFileChooser.setCurrentDirectory(new File("."));
            }

            if (saveAs || !view.hasLastFileLocation()) {
                path = getPathToSaveFromUser();
                if (path == null)
                    return;
            }
            saveConfigAsXMLFile(path);
        }
    }

    private boolean wasPreviousSaveRemoved(String path) {
        return view.hasLastFileLocation() && !new File(path).exists();
    }

	/**
	 * Gets the path to save the file to from the user via a file chooser.
	 * @return The path to save the file, null if the user closed the file chooser.
	 */
	private String getPathToSaveFromUser() {
	    String path = null;
        currentFileChooser = getCurrentFileChooser();

        /** Adds an xml filter for the file chooser: */
        currentFileChooser.setFileFilter(FileFilters.xmlFilter());

        if (currentFileChooser
                .showDialog(getController().getMainView(), "Save Scenario") == JFileChooser.APPROVE_OPTION) {
            File file = currentFileChooser.getSelectedFile();

            path = file.getAbsolutePath();

            String extension = ".xml";
            if (!path.endsWith(extension)) {
                path += extension;
                file = new File(path);
            }
            controller.getMainView().setWindowTitle(file.getName());
        }
        return path;
	}


    private boolean validateBotCount() {
        MapSpec map = controller.getMainView().getMainPanel()
                .getConfigurationPanel().getMapSpecifications();
        int botCount = getModel().getAmountBot();
        if (map.isSet() && botCount > map.getEntitiesAllowedInMap()) {
            ScenarioEditor.getOptionPrompt().showMessageDialog(
                    view,
                    "The selected map can only hold "
                            + map.getEntitiesAllowedInMap()
                            + " bots. Please delete some first.");
            return false;
        }
        return true;
    }

    private boolean verifyMapSelected() {
        String map = getController().getMainView().getMainPanel().getConfigurationPanel().getMapFile();
        if(map.trim().isEmpty()) {
            int response = ScenarioEditor.getOptionPrompt().showConfirmDialog(getController().getMainView(),
                    "Warning: No map file has been selected. Press OK to continue.",
                    "",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            return response == JOptionPane.OK_OPTION;
        }
        return true;
    }

    /**
     * Saves the config at the path specified as an XML file.
     * @param destination The path.
     */
	public void saveConfigAsXMLFile(String destination) {
        try {
    		BW4TClientConfig configuration = getModel();
    		configuration.setFileLocation(destination);
    		configuration.setUseGoal(DefaultConfigurationValues.USE_GOAL.getBooleanValue());
    
    		configuration.toXML();
    		view.setLastFileLocation(destination);
        } catch (JAXBException e) {
            ScenarioEditor.handleException(e,
                    "Error: Saving to XML has failed.");
        } catch (FileNotFoundException e) {
            ScenarioEditor.handleException(e, "Error: No file has been found.");
        }
	}

    /**
     * Update the model with the new bot and epartners, and update the counts in the view.
     */
    public void updateModelAndView() {
        getController().getMainView().getMainPanel().getConfigurationPanel().updateOldValues();
        getModel().updateOldBotConfigs();
        getController().getMainView().getMainPanel().getEntityPanel().updateEPartnerCount(
                getModel().getAmountEPartner());
        getModel().updateOldEpartnerConfigs();
    }

	/**
	 * Returns the MenuBar
	 * 
	 * @return The MenuBar
	 */
	public MenuBar getMenuView() {
		return this.view;
	}

	/**
	 * Gets called when the button associated with this action is pressed.
	 * 
	 * @param e
	 *            The action event.
	 */
	public abstract void actionPerformed(ActionEvent e);

	/**
	 * Gets the controllers.
	 * 
	 * @return The controllers.
	 */
	public ScenarioEditorController getController() {
		return controller;
	}

	/**
	 * Sets the controllers.
	 * 
	 * @param newController
	 *            The new controllers.
	 */
	public void setController(final ScenarioEditorController newController) {
		controller = newController;
	}
	
	/**
	 * Gets the BW4TClientConfig model.
	 * 
	 * @return model
	 * 			The model being used.
	 */
	public BW4TClientConfig getModel() {
		return model;
	}

}
