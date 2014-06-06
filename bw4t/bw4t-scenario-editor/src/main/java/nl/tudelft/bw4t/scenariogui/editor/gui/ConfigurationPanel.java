package nl.tudelft.bw4t.scenariogui.editor.gui;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nl.tudelft.bw4t.scenariogui.DefaultConfigurationValues;
import nl.tudelft.bw4t.scenariogui.util.FileFilters;
import nl.tudelft.bw4t.scenariogui.util.Format;
import nl.tudelft.bw4t.scenariogui.util.MapSpec;

/**
 * The ConfigurationPanel class represents the left pane of the MainPanel. It
 * shows the options the user can configure.
 * 
 */
public class ConfigurationPanel extends JPanel {
    
    private static final long serialVersionUID = 2925174902776539436L;

    private static final String FONT_NAME = "Sans-Serif";

    private static final int INSET = 8;

    private static final double GRID_BAG_CONSTRAINTS_WEIGHT = 0.5;

    private static final int TEXT_FIELD_COLUMN_SIZE_BIG = 15;

    private static final int TEXT_FIELD_COLUMN_SIZE_SMALL = 6;

    private MapSpec mapSpec;


    private JTextField clientIP = new JTextField(
            DefaultConfigurationValues.DEFAULT_CLIENT_IP.getValue(),
            TEXT_FIELD_COLUMN_SIZE_BIG);

    private JTextField clientPort = new JTextField(
            DefaultConfigurationValues.DEFAULT_CLIENT_PORT.getValue(),
            TEXT_FIELD_COLUMN_SIZE_SMALL);
    {
        Format.addIntegerDocumentFilterForTextField(clientPort);
    }

    private JTextField serverIP = new JTextField(
            DefaultConfigurationValues.DEFAULT_SERVER_IP.getValue(),
            TEXT_FIELD_COLUMN_SIZE_BIG);

    private JTextField serverPort = new JTextField(
            DefaultConfigurationValues.DEFAULT_SERVER_PORT.getValue(),
            TEXT_FIELD_COLUMN_SIZE_SMALL);
    {
        Format.addIntegerDocumentFilterForTextField(serverPort);
    }

    private JTextField mapFileTextField = new JTextField(
            DefaultConfigurationValues.MAP_FILE.getValue());

    private CheckboxGroup guiCheckBox = new CheckboxGroup();

    private Checkbox guiYes = new Checkbox("Yes",
            DefaultConfigurationValues.USE_GUI.getBooleanValue(), guiCheckBox);

    private Checkbox guiNo = new Checkbox("No",
            !DefaultConfigurationValues.USE_GUI.getBooleanValue(), guiCheckBox);

    private JButton chooseMapFile = new JButton("Open File");

    private JFileChooser fileChooser;

    private GridBagConstraints c;

    private static final int FONT_SIZE = 16;

    private static final int FONT_SIZE_SMALL = 14;

    private String oldClientIP = clientIP.getText();

    private String oldClientPort = clientPort.getText();

    private String oldServerIP = serverIP.getText();

    private String oldServerPort = serverPort.getText();

    private Checkbox oldGui = guiCheckBox.getSelectedCheckbox();

    private String oldMapFile = mapFileTextField.getText();

    /**
     * Create a ConfigurationPanel object.
     */
    public ConfigurationPanel() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileFilters.mapFilter());
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        Border loweredetched = BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(loweredetched,
                "Configuration ");
        title.setTitleJustification(TitledBorder.LEFT);
        title.setTitleFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
        setBorder(title);

        mapSpec = new MapSpec(DefaultConfigurationValues.MAP_FILE.getValue());

        // showConfigLabel();
        showClientOptions();
        showServerOptions();
        showGuiOptions();
        showMapOptions();

    }

    /**
     * Show the "Configuration" label in the panel.
     */
    private void showConfigLabel() {
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        add(new JLabel("Configuration"), c);
    }

    /**
     * Show the client configuration options in the panel.
     */
    private void showClientOptions() {
        c.insets = new Insets(INSET, INSET, 0, 0);

        c.gridx = 0;
        c.gridy += 1;

        JLabel client = new JLabel("Client");
        client.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_SMALL));
        add(client, c);

        c.insets = new Insets(0, INSET, 0, 0);

        c.weightx = GRID_BAG_CONSTRAINTS_WEIGHT;
        c.gridx = 0;
        c.gridy += 1;
        add(new JLabel("IP "), c);

        c.gridx = 1;
        c.weightx = 2;
        c.weighty = 1;
        add(clientIP, c);

        c.weightx = GRID_BAG_CONSTRAINTS_WEIGHT;
        c.gridx = 0;
        c.gridy += 1;
        add(new JLabel("Port "), c);

        c.gridx = 1;
        c.weightx = 2;
        c.weighty = 1;
        add(clientPort, c);
    }

    /**
     * Show the server configuration options in the panel.
     */
    private void showServerOptions() {
        c.insets = new Insets(INSET, INSET, 0, 0);

        c.gridx = 0;
        c.gridy += 1;

        JLabel server = new JLabel("Server");
        server.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_SMALL));
        add(server, c);

        c.insets = new Insets(0, INSET, 0, 0);

        c.weightx = GRID_BAG_CONSTRAINTS_WEIGHT;
        c.gridx = 0;
        c.gridy += 1;
        add(new JLabel("IP"), c);

        c.gridx = 1;
        c.weightx = 2;
        c.weighty = 1;
        add(serverIP, c);

        c.weightx = GRID_BAG_CONSTRAINTS_WEIGHT;
        c.gridx = 0;
        c.gridy += 1;
        add(new JLabel("Port"), c);

        c.gridx = 1;
        c.weightx = 2;
        c.weighty = 1;
        add(serverPort, c);
    }

    /**
     * Show the option to use a GUI in the panel.
     */
    private void showGuiOptions() {
        c.insets = new Insets(INSET, INSET, 0, 0);

        c.gridx = 0;
        c.gridy += 1;

        JLabel gui = new JLabel("Launch GUI");
        gui.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_SMALL));
        add(gui, c);

        c.insets = new Insets(0, INSET, 0, 0);

        c.gridx = 0;
        c.gridy += 1;
        add(guiYes, c);

        c.gridx = 1;
        add(guiNo, c);
    }

    /**
     * Show the options to add a map file in the panel.
     */
    private void showMapOptions() {
        c.insets = new Insets(INSET, INSET, 0, 0);

        c.gridx = 0;
        c.gridy += 1;

        JLabel map = new JLabel("Map File");
        map.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_SMALL));
        add(map, c);

        c.insets = new Insets(INSET / 2, INSET, INSET, INSET);

        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy += 1;
        add(mapFileTextField, c);

        mapFileTextField.setPreferredSize(chooseMapFile.getPreferredSize());

        c.gridx = 2;
        c.gridwidth = 1;
        add(chooseMapFile, c);
    }

    /**
     * Returns the client IP.
     *
     * @return The client IP.
     */
    public final String getClientIP() {
        return clientIP.getText();
    }

    /**
     * Sets the value of the text field of the client IP.
     *
     * @param newClientIP
     *            The IP of the client
     */
    public final void setClientIP(final String newClientIP) {
        this.clientIP.setText(newClientIP);
    }

    /**
     * Returns the client port.
     *
     * @return The client port.
     */
    public final int getClientPort() {
        return Integer.parseInt(clientPort.getText());
    }

    /**
     * Sets the value of the text field of the client port.
     *
     * @param newClientPort
     *            The port of the client
     */
    public final void setClientPort(final String newClientPort) {
        this.clientPort.setText(newClientPort);
    }

    /**
     * Returns the server IP.
     *
     * @return The server IP.
     */
    public final String getServerIP() {
        return serverIP.getText();
    }

    /**
     * Sets the value of the text field of the server IP.
     *
     * @param newServerIP
     *            The IP of the server
     */
    public final void setServerIP(final String newServerIP) {
        this.serverIP.setText(newServerIP);
    }

    /**
     * Returns the server port.
     *
     * @return The server port.
     */
    public final int getServerPort() {
        return Integer.parseInt(serverPort.getText());
    }

    /**
     * Sets the value of the text field of the server port.
     *
     * @param newServerPort
     *            The port of the server
     */
    public final void setServerPort(final String newServerPort) {
        this.serverPort.setText(newServerPort);
    }

    /**
     * Returns if a GUI needs to be displayed.
     *
     * @return The use of a GUI.
     */
    public final boolean useGui() {
        return guiCheckBox.getSelectedCheckbox() == guiYes;
    }

    /**
     * Sets if GOAL needs to be used.
     *
     * @param useGui
     *           The  use of GOAL.
     */
    public final void setUseGui(final boolean useGui) {
        if (useGui) {
            guiCheckBox.setSelectedCheckbox(guiYes);
        }
        else {
            guiCheckBox.setSelectedCheckbox(guiNo);
        }
    }

    /**
     * Returns the button to choose a map file.
     *
     * @return The button to choose a map file.
     */
    public final JButton getChooseMapFile() {
        return chooseMapFile;
    }

    /**
     * Returns the path to the Map file.
     *
     * @return The path to the Map file.
     */
    public final String getMapFile() {
        return mapFileTextField.getText();
    }

    /**
     * Sets the value of the text field to the path of the Map file.
     *
     * @param mapFile
     *            The path of the Map file
     */
    public final void setMapFile(final String mapFile) {
        mapSpec.setMapFileLocation(mapFile);
        this.mapFileTextField.setText(mapFile);
    }

    /**
     * Returns a File Chooser.
     *
     * @return A File Chooser.
     */
    public final JFileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * Sets the new file chooser.
     * @param newFileChooser The new file chooser.
     */
    public final void setFileChooser(final JFileChooser newFileChooser) {
        fileChooser = newFileChooser;
    }

    /**
     * Returns if changes has been made to the default configuration.
     *
     * @return whether changes have been made.
     */
    public final boolean isDefault() {
        boolean isDefault = true;

        if (!this.getClientIP().equals(DefaultConfigurationValues.DEFAULT_CLIENT_IP.getValue())) {
            isDefault = false;
        }
        else if (this.getClientPort() != DefaultConfigurationValues.DEFAULT_CLIENT_PORT.getIntValue()) {
            isDefault = false;
        }
        else if (!this.getServerIP().equals(DefaultConfigurationValues.DEFAULT_SERVER_IP.getValue())) {
            isDefault = false;
        }
        else if (this.getServerPort() != DefaultConfigurationValues.DEFAULT_SERVER_PORT.getIntValue()) {
            isDefault = false;
        }
        else if (this.useGui() != DefaultConfigurationValues.USE_GUI.getBooleanValue()) {
            isDefault = false;
        }
        else if (!this.getMapFile().equals(DefaultConfigurationValues.MAP_FILE.getValue())) {
            isDefault = false;
        }

        // TODO: check if the bot list is empty (since that is default too)

        return isDefault;
    }

    /**
     * Returns the values that have been saved previously.
     * @return The values that have been saved previously.
     */
    public String getOldValues() {
        return this.oldClientIP + this.oldClientPort
                + this.oldServerIP + this.oldServerPort + this.oldGui
                + this.oldMapFile;
    }

    /**
     * Updates the "old" values after a file has been saved.
     */
    public void updateOldValues() {
        this.oldClientIP = clientIP.getText();
        this.oldClientPort = clientPort.getText();
        this.oldServerIP = serverIP.getText();
        this.oldServerPort = serverPort.getText();
        this.oldGui = guiCheckBox.getSelectedCheckbox();
        this.oldMapFile = mapFileTextField.getText();
    }

    /**
     * Returns the current values.
     * @return The current values.
     */
    public String getCurrentValues() {
        return this.clientIP.getText()
                + this.clientPort.getText()
                + this.serverIP.getText() + this.serverPort.getText()
                + this.guiCheckBox.getSelectedCheckbox()
                + this.mapFileTextField.getText();
    }
    
    /**
     * Returns the map specifications for the selected map.
     * @return The map specifications for the selected map.
     */
    public MapSpec getMapSpecifications() {
        return mapSpec;
    }
    
    public JTextField getClientIPTextField() {
        return clientIP;
    }
    
    public JTextField getClientPortTextField() {
        return clientPort;
    }
    
    public JTextField getServerIPTextField() {
        return serverIP;
    }
    
    public JTextField getServerPortTextField() {
        return serverPort;
    }
    
    public Checkbox getGUIYesCheckbox() {
        return guiYes;
    }
    
    public Checkbox getGUINoCheckbox() {
        return guiNo;
    }
    
    public JTextField getMapFileTextField() {
        return mapFileTextField;
    }
    
}