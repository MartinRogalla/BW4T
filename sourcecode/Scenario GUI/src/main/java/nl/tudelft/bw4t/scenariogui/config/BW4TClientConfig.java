package nl.tudelft.bw4t.scenariogui.config;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import nl.tudelft.bw4t.scenariogui.gui.panel.EntityPanel;
import nl.tudelft.bw4t.scenariogui.gui.panel.ConfigurationPanel;
import nl.tudelft.bw4t.scenariogui.gui.panel.MainPanel;
import nl.tudelft.bw4t.scenariogui.util.XMLManager;

/**
 * This class holds the possible options that can be specified in the Scenario
 * GUI and is meant to be directly convertible to XML and constructible from
 * XML.
 *
 * @author Nick / Calvin
 *
 */
@XmlRootElement
public class BW4TClientConfig {

    /** The file in which the configuration in this class will be stored. */
    private String outputFile;
    /** The clientIP. */
    private String clientIp;
    /** The client port. */
    private int clientPort;
    /** The serverIP. */
    private String serverIp;
    /** The server port. */
    private int serverPort;
    /** Boolean indicating if a GUI should be launched. */
    private boolean launchGui;
    /** Boolean indicating if a GOAL should be used. */
    private boolean useGoal;
    /** The location of the map file. */
    private String mapFile;

    // @XmlElementWrapper(name = "botlist")
    // @XmlElement(name = "bot")
    // private LinkedList<BotConfig> bots = new LinkedList<BotConfig>();

    /**
     * An empty <code>BW4TClientConfig</code> object.
     */
    public BW4TClientConfig() {
    }

    /**
     * A <code>BW4TClientConfig</code> object holding the configuration.
     *
     * @param mainPanel serves as the content pane
     * @param newOutputFile contains the file location used for saving
     */
    public BW4TClientConfig(final MainPanel mainPanel,
            final String newOutputFile) {
        ConfigurationPanel configPanel = mainPanel.getConfigurationPanel();
        clientIp = configPanel.getClientIP();
        clientPort = configPanel.getClientPort();
        serverIp = configPanel.getServerIP();
        serverPort = configPanel.getServerPort();
        launchGui = configPanel.useGui();
//        configPanel.useGoal();
        useGoal = ConfigurationPanel.DEFAULT_VALUES.USE_GOAL.getBooleanValue();
        mapFile = configPanel.getMapFile();
        EntityPanel entityPanel = mainPanel.getEntityPanel();
//        TODO: read out bot panel and add each BotConfig to the list of bots
//        botPanel.getTable().;
        this.outputFile = newOutputFile;
    }

    /**
     * Converts Java Object into XML file.
     *
     * @throws FileNotFoundException
     *             Signals that an attempt to open the file denoted by a
     *             specified pathname has failed.
     * @throws JAXBException
     *             Root exception class for all JAXB exceptions.
     */
    public final void toXML() throws FileNotFoundException, JAXBException {
        XMLManager.toXML(outputFile, this);
    }

    /**
     * Construct Java Object from XML file.
     *
     * @param inputFile
     *            The file location of the XML-file
     * @return The BW4TClientConfig object
     * @throws FileNotFoundException
     *             Signals that an attempt to open the file denoted by a
     *             specified pathname has failed.
     * @throws JAXBException
     *             Root exception class for all JAXB exceptions.
     */
    public static BW4TClientConfig fromXML(final String inputFile)
            throws FileNotFoundException, JAXBException {
        return (BW4TClientConfig) XMLManager.fromXML(inputFile,
                BW4TClientConfig.class);
    }

    /**
     * Gets the location to store the XML version of this file in.
     *
     * @return The path of the file to store this object in as XML.
     */
    public final String getFileLocation() {
        return outputFile;
    }

    /**
     * Sets the location to store the XML version of this file in.
     *
     * @param newFileLocation
     *            The path of the file to store this object in as XML.
     */
    public final void setFileLocation(final String newFileLocation) {
        this.outputFile = newFileLocation;
    }

    /**
     * Gets the clientIP.
     *
     * @return The clientIP.
     */
    public final String getClientIp() {
        return clientIp;
    }

    /**
     * Sets the clientIP.
     *
     * @param newClientIp The clientIP.
     */

    @XmlElement
    public final void setClientIp(final String newClientIp) {
        this.clientIp = newClientIp;
    }

    /**
     * Gets the clientPort.
     *
     * @return The clientPort.
     */
    public final int getClientPort() {
        return clientPort;
    }

    /**
     * Sets the clientPort.
     *
     * @param newClientPort
     *            The clientPort.
     */
    @XmlElement
    public final void setClientPort(final int newClientPort) {
        this.clientPort = newClientPort;
    }

    /**
     * Gets the serverIP.
     *
     * @return The serverIP.
     */
    public final String getServerIp() {
        return serverIp;
    }

    /**
     * Sets the serverIP.
     *
     * @param newServerIp
     *            The serverIP.
     */
    @XmlElement
    public final void setServerIp(final String newServerIp) {
        this.serverIp = newServerIp;
    }

    /**
     * Gets the serverPort.
     *
     * @return The serverPort.
     */
    public final int getServerPort() {
        return serverPort;
    }

    /**
     * Sets the serverPort.
     *
     * @param newServerPort
     *            The serverPort.
     */
    @XmlElement
    public final void setServerPort(final int newServerPort) {
        this.serverPort = newServerPort;
    }

    /**
     * Gets if GUI should be launched.
     *
     * @return if a GUI should be launched.
     */
    public final boolean isLaunchGui() {
        return launchGui;
    }

    /**
     * Sets if a GUI should be launched.
     *
     * @param newLaunchGui
     *            Boolean indicating if a GUI should be launched.
     */
    @XmlElement
    public final void setLaunchGui(final boolean newLaunchGui) {
        this.launchGui = newLaunchGui;
    }

    /**
     * Gets if Goal should be used.
     *
     * @return if Goal should be used.
     */
    public final boolean isUseGoal() {
        return useGoal;
    }

    /**
     * Sets if GOAL should be used.
     *
     * @param newUseGoal
     *            Boolean indicating if GOAL should be used.
     */
    @XmlElement
    public final void setUseGoal(final boolean newUseGoal) {
        this.useGoal = newUseGoal;
    }

    /**
     * Gets location of the MapFile.
     *
     * @return mapFile The MapFile location.
     */
    public final String getMapFile() {
        return mapFile;
    }

    /**
     * Sets the MapFile.
     *
     * @param newMapFile
     *            The MapFile location.
     */
    @XmlElement
    public final void setMapFile(final String newMapFile) {
        this.mapFile = newMapFile;
    }

//    public LinkedList<BotConfig> getBots() {
//        return bots;
//    }
//
//    public void setBots(LinkedList<BotConfig> bots) {
//        this.bots = bots;
//    }
}