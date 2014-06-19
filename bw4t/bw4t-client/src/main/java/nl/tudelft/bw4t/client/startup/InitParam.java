package nl.tudelft.bw4t.client.startup;

import eis.iilang.Identifier;
import eis.iilang.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

/**
 * Available init parameters and default values for {@link BW4TEnvironment}.
 */
public enum InitParam {
    /** Our IP. Passed to server so server is able to find us with this IP. */
    CLIENTIP("localhost"),
    /** The port that we use. */
    CLIENTPORT("2000"),
    /** The IP address of the server. */
    SERVERIP("localhost"),
    /** The Port used by the server. */
    SERVERPORT("8000"),
    /** Number of agents that we have. */
    AGENTCOUNT("0"),
    /** Launch GUI for entities? (check is this only for humans?) */
    LAUNCHGUI("true"),
    /** Number of human bots (is this to connect GUIs?) */
    HUMANCOUNT("1"),
    /**
     * The java agent class to load when new entities appear.
     */
    AGENTCLASS("nl.tudelft.bw4t.client.agent.BW4TAgent"),
    /**
     * are we connected with GOAL? This param should be auto detected, it will be set to false if the program is started
     * from commandline.
     */
    GOAL("true"),
    /**
     * The key we should try to use to kill the remote server.
     */
    KILL(""),
    /**
     * The file from which the client reads the configuration.
     */
    CONFIGFILE("");
    
    private static final Logger LOGGER = Logger.getLogger(InitParam.class);
    
    private String defaultvalue;

    /** Store the program-wide parameters given to the {@link RemoteEnvironment}. */
    private static Map<String, Parameter> parameters = null;

    /**
     * @param def
     *            - The default value of the init parameter.
     */
    InitParam(String def) {
        defaultvalue = def;
    }

    public String getDefaultValue() {
        return defaultvalue;
    }

    /**
     * {@link #name()} but in lower case.
     * 
     * @return Name in lower case.
     */
    public String nameLower() {
        return this.name().toLowerCase();
    }

    /**
     * Retrieve the value of this InitParam  EnumType.
     * 
     * @return the value of the parameter or the default value.
     */
    public String getValue() {
        Parameter param = getParameter(this.nameLower());
        if (param != null) {
            return ((Identifier) param).getValue();
        }
        return getDefaultValue();
    }

    /**
     * Set the program-wide parameters.
     * 
     * @param params
     *            - The Map of parameters given to the {@link RemoteEnvironment}
     */
    public static void setParameters(Map<String, Parameter> params) {
        parameters = params;
        
        final String cfile = CONFIGFILE.getValue();
        if (!cfile.isEmpty()) {
            LOGGER.info(String.format("Reading configuration file '%s'", cfile));
            try {
                ConfigFile.readConfigFile(cfile);
            } catch (JAXBException e) {
                LOGGER.error(String.format("Unable to load configuration file: '%s'", cfile), e);
            }
        }
    }
    
    

    /**
     * Get all program-wide parameters.
     * 
     * @return The Map of parameters given to the {@link RemoteEnvironment}
     */
    public static Map<String, Parameter> getParameters() {
        return parameters;
    }

    /**
     * Filter out all the parameters for the server from the given set of
     * parameters. We do this by removing all parameters that we can handle as
     * client; the remaining ones must be server parameters.
     * 
     * @return parameters for the server
     */
    public static Map<String, Parameter> getServerParameters() {
        Map<String, Parameter> serverparams = new HashMap<String, Parameter>(
                getParameters());
        for (InitParam param : InitParam.values()) {
            serverparams.remove(param.nameLower());
        }
        return serverparams;
    }

    /**
     * Get the Parameter by name.
     * 
     * @param name
     *            - The name of the parameter to be read.
     * @return The parameter with the given name or {@code null} if not found.
     */
    public static Parameter getParameter(String name) {
        if (parameters == null) {
            return null;
        }
        return parameters.get(name);
    }

    public void setDefaultValue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }
}
