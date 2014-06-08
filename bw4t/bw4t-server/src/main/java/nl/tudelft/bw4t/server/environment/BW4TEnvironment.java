package nl.tudelft.bw4t.server.environment;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import nl.tudelft.bw4t.BW4TBuilder;
import nl.tudelft.bw4t.client.BW4TClientActions;
import nl.tudelft.bw4t.eis.EPartnerEntity;
import nl.tudelft.bw4t.eis.RobotEntity;
import nl.tudelft.bw4t.map.Entity;
import nl.tudelft.bw4t.map.NewMap;
import nl.tudelft.bw4t.model.blocks.EPartner;
import nl.tudelft.bw4t.model.robots.EntityFactory;
import nl.tudelft.bw4t.model.robots.handicap.IRobot;
import nl.tudelft.bw4t.scenariogui.BotConfig;
import nl.tudelft.bw4t.scenariogui.EPartnerConfig;
import nl.tudelft.bw4t.server.BW4TServer;
import nl.tudelft.bw4t.server.RobotEntityInt;
import nl.tudelft.bw4t.server.logging.BW4TFileAppender;
import nl.tudelft.bw4t.server.logging.BotLog;
import nl.tudelft.bw4t.server.view.ServerContextDisplay;

import org.apache.log4j.Logger;

import repast.simphony.context.Context;
import repast.simphony.scenario.ScenarioLoadException;
import repast.simphony.space.continuous.NdPoint;

/**
 * The central environment which runs the data model and performs actions received from remote environments through the
 * server. Remote environments also poll percepts from this environment. Remote environments are notified of entity and
 * environment events also using the server.
 * 
 * This is a singleton. Needed because we store the map info here.
 */
public class BW4TEnvironment extends AbstractEnvironment {

    public static final String VERSION = "@PROJECT_VERSION@";

    private static final String ENTITY_NAME_FORMAT = "%s_%d";

    private static final long serialVersionUID = -279637264069930353L;
    private static BW4TEnvironment instance;

    /**
     * The log4j logger, logs to the console and file
     */
    private static final Logger LOGGER = Logger.getLogger(BW4TEnvironment.class);

    private String mapName;

    /**
     * start time of the first action.
     */
    private static Long starttime = null;

    private BW4TServer server;
    private boolean mapFullyLoaded;
    private Stepper stepper;
    private final String scenarioLocation;
    private Context context;
    private static NewMap theMap;
    private ServerContextDisplay contextDisplay;
    private final boolean guiEnabled;
    private final String shutdownKey;

    private int nextBotSpawnIndex = 0;

    /**
     * Create a new instance of this environment
     * 
     * @param scenarioLocation
     *            the location of the scenario that should be loaded in Repast
     * @param mapLocation
     *            the location of the map file
     * @param serverIp
     *            the ip address the server should listen on
     * @param serverPort
     *            the port the server should listen on
     * @throws IOException
     * @throws ManagementException
     * @throws ScenarioLoadException
     * @throws JAXBException
     */
    BW4TEnvironment(BW4TServer server2, String scenarioLocation, String mapLocation, boolean guiEnabled,
            String shutdownKey) throws IOException, ManagementException, ScenarioLoadException, JAXBException {
        super();
        setInstance(this); 
        this.server = server2;
     	mapName = mapLocation;
        this.scenarioLocation = System.getProperty("user.dir") + "/" + scenarioLocation;
        this.guiEnabled = guiEnabled;
        this.shutdownKey = shutdownKey;
    }
    private static void setInstance(BW4TEnvironment env){
    	instance = env;  
    }
    /**
     * Launch server and start repast.
     * 
     * @throws IOException
     * @throws ManagementException
     * @throws ScenarioLoadException
     * @throws JAXBException
     */
    void launchAll() throws IOException, ManagementException, ScenarioLoadException, JAXBException {
        launchServer();
        setState(EnvironmentState.RUNNING);
        launchRepast();
    }

    /**
     * Notify listeners that a new entity is available, server handles correct distribution of entities to listeners
     * 
     * @param entity
     *            the new entity
     */
    @Override
    public void notifyNewEntity(String entity) {
        server.notifyNewEntity(entity);
    }

    @Override
    public void notifyDeletedEntity(String entity, Collection<String> agents) {
        server.notifyDeletedEntity(entity, agents);
    }

    @Override
    public void setState(EnvironmentState newstate) throws ManagementException {
        super.setState(newstate);
        LOGGER.info("Environment now in state: " + newstate.name());
        server.notifyStateChange(getState());
    }

    /**
     * takes down all entities and agents. Stops repast. Leaves the server open. Tries to take down in any case, just
     * reports errors and proceeds.
     */
    public void removeAllEntities() throws ManagementException {
      
    	BW4TFileAppender.logFinish(System.currentTimeMillis(), "total time is ");
        // FIXME: BOTLOG gives nullpointer exception if no bots.

        setState(EnvironmentState.KILLED);

        LOGGER.debug("Removing all entities");
        for (String entity : this.getEntities()) {
        	try {
                this.deleteEntity(entity);
            } catch (EntityException | RelationException e) {
                LOGGER.error("Failure to delete entity: " + entity, e);
            }
        }
        
        LOGGER.debug("Remove all (remaining) agents");
        for (String agent : this.getAgents()) {
            try {
                this.unregisterAgent(agent);
            } catch (AgentException e) {
                LOGGER.error("Failure to unregister agent: " + agent, e);
            }
        }
        mapFullyLoaded = false;
    }

    @Override
    public void pause() throws ManagementException {
        setState(EnvironmentState.PAUSED);
    }

    @Override
    public void start() throws ManagementException {
        setState(EnvironmentState.RUNNING);
    }

    /**
     * initialize Repast. Does not reset the {@link BW4TServer}.
     */
    @Override
    public void init(Map<String, Parameter> parameters) throws ManagementException {
        setState(EnvironmentState.INITIALIZING);
        takeDownSimulation();
        String error = "launch of Repast failed";
        Parameter map = parameters.get("map");
        if (map != null) {
            setMapName(((Identifier) map).getValue());
        }
        try {
            launchRepast();
        } catch (IOException e) {
            throw new ManagementException(error, e);
        } catch (ScenarioLoadException e) {
            throw new ManagementException(error, e);
        } catch (JAXBException e) {
            throw new ManagementException(error, e);
        }

        setState(EnvironmentState.RUNNING);
    }

    /**
     * Launch the server
     * 
     * @param serverIp
     *            the ip address that the server should listen to
     * @param serverPort
     *            the port that the server should listen to
     * @throws RemoteException
     * @throws ManagementException
     * @throws MalformedURLException
     */
    private void launchServer() throws RemoteException, ManagementException, MalformedURLException {
        if (server == null) {
            server = Launcher.getInstance().setupRemoteServer();
        }
        setState(EnvironmentState.INITIALIZING);
        LOGGER.info("BW4T Server has been bound.");
    }

    /**
     * Launches the Repast framework and GUI. Does not return until there is an exception or getState()==KILLED. After
     * stopping, runner is set back to null.
     * 
     * @throws IOException
     * @throws ScenarioLoadException
     * @throws JAXBException
     * 
     * @throws Exception
     */
    private void launchRepast() throws IOException, ScenarioLoadException, JAXBException {
        theMap = NewMap.create(new FileInputStream(new File(System.getProperty("user.dir") + "/maps/"
                + mapName)));
        stepper = new Stepper(scenarioLocation, this);
        new Thread(stepper).start();
    }

    /**
     * Get the instance of this environment
     * 
     * @return the instance
     */
    public static BW4TEnvironment getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BW4TEnvironment has not been initialized");
        }
        return instance;
    }

    public String getMapLocation() {
        return mapName;
    }

    public void setMapName(String mapName) {
         this.mapName = mapName;
    }

    /**
     * Check whether an action is supported by this environment.
     * 
     * @param arg0
     *            the action that should be checked
     * @return true if there is an entity, a dropzone and sequence not yet complete
     */
    @Override
    public boolean isSupportedByEnvironment(Action arg0) {

        return !getEntities().isEmpty();
    }

    /**
     * Check whether an action is supported by an entity type, always returns true for now
     * 
     * @param arg0
     *            the action that should be checked
     * @param arg1
     *            the type of entity
     * @return the result
     */
    @Override
    protected boolean isSupportedByType(Action arg0, String arg1) {
        return true;
    }

    /**
     * Check whether a state transition is valid, for now always returns true
     * 
     * @param oldState
     *            the old state of the environment
     * @param newState
     *            the new state of the environment
     * @return the result
     */
    @Override
    public boolean isStateTransitionValid(EnvironmentState oldState, EnvironmentState newState) {
        return true;
    }

    /**
     * Helper method to allow the server to call actions received from attached clients
     * 
     * @param entity
     *            the entity that should perform the action
     * @param action
     *            the action that should be performed
     * @return the percept received after performing the action
     */
    public Percept performClientAction(String entity, Action action) throws ActException {
        Long time = System.currentTimeMillis();
        LOGGER.log(BotLog.BOTLOG, String.format("action %s %s", entity, action.toProlog()));

        if (starttime == null) {
            starttime = time;
        }

        return performEntityAction(entity, action);
    }

    /**
     * Helper method to allow the server to get all percepts for a connected client.
     * <p>
     * This function is synchronized to ensure that multiple calls are properly sequenced. This is important because
     * getAllPercepts must 'lock' the environment and parallel calls would cause overlapping 'locks' taken at different
     * moments in time.
     * <p>
     * Actually, locking the environment is done by copying the current location of the entity.
     * <p>
     * It seems that this new function is created because {@link AbstractEnvironment#getAllPerceptsFromEntity(String)}
     * is final.
     * 
     * @param entity
     *            , the entity for which all percepts should be gotten
     * @return all percepts for the entity
     */
    public synchronized List<Percept> getAllPerceptsFrom(String entity) {
        try {
            if (this.isMapFullyLoaded()) {
                ((RobotEntityInt) getEntity(entity)).initializePerceptionCycle();
                return getAllPerceptsFromEntity(entity);
            }
        } catch (PerceiveException | NoEnvironmentException e) {
            LOGGER.error("failed to get percepts for entity: '" + entity + "'", e);
        }
        return new LinkedList<Percept>();
    }

    /**
     * Check if the map was fully loaded. When this is true, all entities also have been processed and the environment
     * is ready to run. Note that because of #2016 there may be an async between Repast and this BW4TEnvironment,
     * causing this flag to remain true while repast has in fact stopped. We detect this only when the user turns 'on'
     * the Repast environment again, in {@link BW4TBuilder#build()}.
     * 
     * @return
     */
    public boolean isMapFullyLoaded() {
        return mapFullyLoaded;
    }

    public void setMapFullyLoaded() {
        mapFullyLoaded = true;
    }

    public double getTps() {
        if (stepper == null) {
            return Stepper.MIN_TPS;
        }
        return stepper.getTps();
    }

    public void setTps(double tps) {
        if (stepper == null) {
            return;
        }
        stepper.setTps(tps);
    }

    /**
     * reset using parameters for initial situation. Does not kill the server.
     * 
     * @param parameters
     *            only the map parameter is accepted
     */
    @Override
    public void reset(Map<String, Parameter> parameters) throws ManagementException {
        try {
            setMapName(((Identifier) parameters.get("map")).getValue());
            if (mapName == null) {
                setMapName("Random");
            }
            reset();
        } catch (Exception e) {
            throw new ManagementException("reset failed", e);
        }
    }

    /**
     * reset to initial situation.
     * 
     * @throws EnvironmentResetException
     *             if the server was unable to restart
     */
    public void reset() throws EnvironmentResetException {
        try {
            takeDownSimulation();
        } catch (ManagementException e) {
            throw new EnvironmentResetException(e);
        }
        if (server != null) {
            server.takeDown();
            server = null;
        }
        try {
            launchAll();
        } catch (ManagementException | IOException | ScenarioLoadException | JAXBException e) {
            throw new EnvironmentResetException(e);
        }
    }

    /**
     * Take down the simulation: remove all entities, stop the stepper. Stop the {@link ServerContextDisplay}.
     * 
     * @throws ManagementException
     */
    private void takeDownSimulation() throws ManagementException {
        // this should set state->KILLED which stops stepper.
        removeAllEntities();

        stepper.terminate();
        contextDisplay.close();
        contextDisplay = null;
        context = null;
    }

    /**
     * get the repast current context. May be null if Repast not running now. Called from {@link BW4TBuilder} when
     * repast gives us context.
     * 
     * @return Repast {@link Context}.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Set a new repast Context. May be null if Repast sstopped running.
     * 
     * @param c
     */
    public void setContext(Context c) {
        context = c;
        if (guiEnabled) {
            LOGGER.info("Launching the BW4T Server Graphical User Interface.");
            try {
                contextDisplay = new ServerContextDisplay(context);
            } catch (Exception e) {
                LOGGER.error("BW4T Server started ok but failed to launch display.", e);
            }
        } else {
            LOGGER.info("Launching the BW4T Server without a graphical user interface.");
        }
    }

    @Override
    public void freeEntity(String entity) throws RelationException, EntityException {
        super.freeEntity(entity);
        this.deleteEntity(entity);
        // this.registerEntity(entity, entity);
    }

    @Override
    public void freePair(String agent, String entity) throws RelationException {
        RobotEntity robot = (RobotEntity) getEntity(entity);
        robot.disconnect();
        super.freePair(agent, entity);
    }

    /**
     * Stop this BW4TEnvironment completely.
     * 
     * @param key
     *            the key required to stop the system
     */
    public void shutdownServer(String key) {
        if (key.equals(this.shutdownKey)) {
            LOGGER.info("Server shutdown requested with correct key");
            try {
                this.setState(EnvironmentState.KILLED);
            } catch (ManagementException e) {
                LOGGER.warn("failed to notify clients that the server is going down...", e);
            }
            server.takeDown();
            server = null;
            System.exit(0);
        } else {
            LOGGER.warn("Server shutdown attempted with wrong key: " + key);
        }
    }

    public String getShutdownKey() {
        return this.shutdownKey;
    }

    public NewMap getMap() {
        return theMap;
    }
    
    public long getStarttime() {
    	return starttime;
    }
    
    /**
     * Selects a spawn point from the list of entities in the map.
     * Using an index that rotates through the number of spawns.
     * 
     * @return the coordinates of the spawn point
     */
    private Point2D getNextBotSpawnPoint() {
        List<Entity> ents = getMap().getEntities();
        Point2D p = ents.get(nextBotSpawnIndex++).getPosition().asPoint2D();
        if (nextBotSpawnIndex  >= ents.size()) {
            nextBotSpawnIndex = 0;
        }
        return p;
    }

    /**
     * @return
     * The list of points the humans are right now. 
     */
    private List<Point2D> getHumanWithoutEPartners() {
        List<Point2D> points = new ArrayList<Point2D>();
        
        for (Object robot : context.getObjects(IRobot.class)) {
            IRobot robotTemp = (IRobot) robot;
            
            if (robotTemp.isHuman() && !robotTemp.isHoldingEPartner()) {
                NdPoint location = robotTemp.getLocation();
                points.add(new Point2D.Double(location.getX(), location.getY()));
            }
        }
        
        return points;
    }

    /**
     * Spawns a new Robot according to the given specifications and notifies the given client.
     * 
     * @param bots
     *            list of bots to spawn
     * @param client the client to notify
     */
    public void spawnBots(List<BotConfig> bots, BW4TClientActions client) {
        for (BotConfig c : bots) {
            int created = 0;
            String name = c.getBotName();

            while (created < c.getBotAmount()) {
                c.setBotName(String.format(ENTITY_NAME_FORMAT, name, created + 1));
                try {
                    spawn(c);
                    
                    // assign robot to client
                    server.notifyFreeRobot(client, c);
                } catch (EntityException e) {
                    LOGGER.error("Failed to register new Robot in the environment.", e);
                }
                created++;
            }
        }
    }

    /**
     * Spawns a new Robot according to the given specifications and notifies the given client.
     * 
     * @param c
     *            the configuration to use
     * @throws EntityException
     *             when we are unable to register Robot
     */
    private void spawn(BotConfig c) throws EntityException {
        EntityFactory entityFactory = Launcher.getInstance().getEntityFactory();
        // create robot from request
        IRobot bot = entityFactory.makeRobot(c);
        // create the entity for the environment
        RobotEntity be = new RobotEntity(bot);
        // register the entity in the environment
        this.registerEntity(c.getBotName(), be);
        // Place the Robot
        Point2D p = getNextBotSpawnPoint();
        bot.moveTo(p.getX(), p.getY());
    }
    
    /**
     * Spawns a new e-Partner according to the given specifications and notifies the given client.
     * 
     * @param epartners
     *            list of epartners to spawn
     * @param client the client to notify
     */
    public void spawnEPartners(List<EPartnerConfig> epartners, BW4TClientActions client) {
        
        List<Point2D> points = getHumanWithoutEPartners();
        int index = 0;
        
        for (EPartnerConfig c : epartners) {
            
            int created = 0;
            String name = c.getEpartnerName();
    
            while (created < c.getEpartnerAmount()) {
                c.setEpartnerName(String.format(ENTITY_NAME_FORMAT, name, created + 1));
                try {
                    // if we run out of humans who don't have an e-Partner. 
                    if (index >= points.size()) {
                        spawn(c, getNextBotSpawnPoint());
                    } else {
                        spawn(c, points.get(index));
                    }
                    
                    // assign e-Partner to client
                    server.notifyFreeEpartner(client, c);
                } catch (EntityException e) {
                    LOGGER.error("Failed to register new Robot in the environment.", e);
                }
                index++;
                created++;
            }
        }
    }

    /**
     * Spawns a new EPartner according to the given specifications and notifies the given client.
     * 
     * @param c
     *            the configuration to use
     * @param point
     *            the point the e-Partner should spawn at
     * @throws EntityException
     *             when we are unable to register EPartner
     */
    private void spawn(EPartnerConfig c, Point2D point) throws EntityException {
        EntityFactory entityFactory = Launcher.getInstance().getEntityFactory();
        // create robot from request
        EPartner epartner = entityFactory.makeEPartner(c);
        // create the entity for the environment
        EPartnerEntity ee = new EPartnerEntity(epartner);
        // register the entity in the environment
        this.registerEntity(epartner.getName(), ee);
        // TODO: Place the EPartner
        epartner.moveTo(point.getX(), point.getY());
    }

}
