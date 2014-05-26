package nl.tudelft.bw4t.client.controller;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.tudelft.bw4t.agent.HumanAgent;
import nl.tudelft.bw4t.client.environment.handlers.PerceptsHandler;
import nl.tudelft.bw4t.client.gui.ClientGUI;
import nl.tudelft.bw4t.client.startup.Launcher;
import nl.tudelft.bw4t.controller.AbstractMapController;
import nl.tudelft.bw4t.map.BlockColor;
import nl.tudelft.bw4t.map.NewMap;
import nl.tudelft.bw4t.map.Zone;
import nl.tudelft.bw4t.map.view.Block;
import nl.tudelft.bw4t.map.view.Entity;
import nl.tudelft.bw4t.view.MapRendererInterface;

import org.apache.log4j.Logger;

import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import eis.iilang.Percept;

public class ClientController extends AbstractMapController {
	/**
	 * The log4j logger which writes logs.
	 */
	private static final Logger LOGGER = Logger.getLogger(ClientController.class);

	private final Set<Zone> occupiedRooms = new HashSet<Zone>();
	private final Entity theBot = new Entity();
	private int sequenceIndex = 0;
	private final Set<Block> visibleBlocks = new HashSet<>();
	private final Map<Long, Block> allBlocks = new HashMap<>();
	private final List<BlockColor> sequence = new LinkedList<>();

	private final Set<String> otherPlayers = new HashSet<>();
	private final List<String> chatHistory = new LinkedList<>();

	private HumanAgent humanAgent;

	private List<Percept> toBePerformedAction = new LinkedList<>();

	private boolean updateNextFrame = true;

	public ClientController(NewMap map, String entityId) {
		super(map);
		getTheBot().setName(entityId);
		humanAgent = null;
	}

	public ClientController(NewMap map, String entityId, HumanAgent humanAgent2) {
		this(map, entityId);
		humanAgent = humanAgent2;
	}

	public Set<Zone> getOccupiedRooms() {
		return occupiedRooms;
	}

	@Override
	public List<BlockColor> getSequence() {
		return sequence;
	}

	@Override
	public int getSequenceIndex() {
		return sequenceIndex;
	}

	public void setSequenceIndex(int sequenceIndex) {
		this.sequenceIndex = sequenceIndex;
	}

	@Override
	public boolean isOccupied(Zone room) {
		return getOccupiedRooms().contains(room);
	}

	@Override
	public Set<Block> getVisibleBlocks() {
		return visibleBlocks;
	}

	@Override
	public Set<Entity> getVisibleEntities() {
		Set<Entity> entities = new HashSet<>();
		entities.add(theBot);
		return entities;
	}

	public Entity getTheBot() {
		return theBot;
	}

	public Set<String> getOtherPlayers() {
		return otherPlayers;
	}

	public List<String> getChatHistory() {
		return chatHistory;
	}

	public boolean isHuman() {
		return humanAgent != null;
	}

	public HumanAgent getHumanAgent() {
		return humanAgent;
	}

	public void setToBePerformedAction(List<Percept> toBePerformedAction) {
		this.toBePerformedAction = toBePerformedAction;
	}

	/**
	 * Method used for returning the next action that a human player wants the bot to perform. This is received by the
	 * GOAL human bot, and then forwarded to the entity on the server side.
	 * 
	 * @return a percept containing the next action to be performed
	 */
	public List<Percept> getToBePerformedAction() {
		List<Percept> toBePerformedActionClone = toBePerformedAction;
		setToBePerformedAction(new LinkedList<Percept>());
		return toBePerformedActionClone;
	}

	private void addOccupiedRoom(String name) {
		getOccupiedRooms().add(getMap().getZone(name));
	}

	private void removeOccupiedRoom(String name) {
		getOccupiedRooms().remove(getMap().getZone(name));
	}

	private Block getBlock(Long id) {
		Block b = allBlocks.get(id);
		if (b == null) {
			b = new Block();
			allBlocks.put(id, b);
		}
		return b;
	}

	/**
	 * Interprets the given list of {@link Percept}s and extracts the required information.
	 * 
	 * @param percepts
	 *            the list of percepts
	 */
	public void handlePercepts(List<Percept> percepts) {

		getVisibleBlocks().clear();

		for (Percept percept : percepts) {
			String name = percept.getName();
			List<Parameter> parameters = percept.getParameters();

			// first process the not percepts.
			if (name.equals("not")) {
				Function function = ((Function) parameters.get(0));
				if (function.getName().equals("occupied")) {
					LinkedList<Parameter> paramOcc = function.getParameters();
					removeOccupiedRoom(((Identifier) paramOcc.get(0)).getValue());
				}
				else if (function.getName().equals("holding")) {
					theBot.getHolding().remove(((Numeral) function.getParameters().get(0)).getValue());
				}
			}
			// Prepare updated occupied rooms list
			else if (name.equals("occupied")) {
				addOccupiedRoom(((Identifier) parameters.get(0)).getValue());
			}
			// Check if holding a block
			else if (name.equals("holding")) {
				Long blockId = ((Numeral) percept.getParameters().get(0)).getValue().longValue();
				theBot.getHolding().put(blockId, getBlock(blockId));
			}
			else if (name.equals("position")) {
				long id = ((Numeral) parameters.get(0)).getValue().longValue();
				double x = ((Numeral) parameters.get(1)).getValue().doubleValue();
				double y = ((Numeral) parameters.get(2)).getValue().doubleValue();

				Block b = getBlock(id);
				b.setObjectId(id);
				b.setPosition(new Point2D.Double(x, y));
			}
			else if (name.equals("color")) {
				long id = ((Numeral) parameters.get(0)).getValue().longValue();
				char color = ((Identifier) parameters.get(1)).getValue().charAt(0);

				Block b = getBlock(id);
				b.setColor(BlockColor.toAvailableColor(color));
				getVisibleBlocks().add(b);
			}
			// Update group goal sequence
			else if (name.equals("sequence")) {
				for (Parameter i : parameters) {
					ParameterList list = (ParameterList) i;
					for (Parameter j : list) {
						char letter = (((Identifier) j).getValue().charAt(0));
						getSequence().add(BlockColor.toAvailableColor(letter));
					}
				}
			}
			// get the current index of the sequence
			else if (name.equals("sequenceIndex")) {
				int index = ((Numeral) parameters.get(0)).getValue().intValue();
				setSequenceIndex(index);
			}
			// Location can be updated immediately.
			else if (name.equals("location")) {
				double x = ((Numeral) parameters.get(0)).getValue().doubleValue();
				double y = ((Numeral) parameters.get(1)).getValue().doubleValue();
				theBot.setLocation(x, y);
			}// Initialize room ids in all rooms gotten from the map loader
			// Should only be done one time
			else if (name.equals("player")) {
				String player = ((Identifier) parameters.get(0)).getValue();
				getOtherPlayers().add(player);
			}
			// Update chat history
			else if (name.equals("message")) {
				ParameterList parameterList = ((ParameterList) parameters.get(0));

				Iterator<Parameter> iterator = parameterList.iterator();

				String sender = ((Identifier) iterator.next()).getValue();
				String message = ((Identifier) iterator.next()).getValue();

				getChatHistory().add(sender + ": " + message);

				updateNextFrame = true;
			}
		}

	}

	@Override
	public void run() {

		List<Percept> percepts;
		try {
			percepts = PerceptsHandler.getAllPerceptsFromEntity(getTheBot().getName() + "gui",
					Launcher.getEnvironment());
			if (percepts != null) {
				handlePercepts(percepts);
			}
		} catch (PerceiveException e) {
			LOGGER.error("Could not correctly poll the percepts from the environment.", e);
		} catch (NoEnvironmentException e) {
			LOGGER.fatal(
					"Could not correctly poll the percepts from the environment. No connection could be made to the environment",
					e);
			setRunning(false);
		}
		super.run();
		updateNextFrame = false;
	}

	@Override
	protected void updateRenderer(MapRendererInterface mri) {
		if (updateNextFrame && mri instanceof ClientGUI) {
			ClientGUI gui = (ClientGUI) mri;
			if (gui != null) {
				gui.update();
			}
		}
		mri.validate();
		mri.repaint();
	}

}
