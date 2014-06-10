package nl.tudelft.bw4t.model.doors;

import java.awt.Color;

import nl.tudelft.bw4t.BoundedMoveableObject;
import nl.tudelft.bw4t.map.Door.Orientation;
import nl.tudelft.bw4t.model.zone.Room;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;

/**
 * A door is a platform between a room and the outside. You can enter rooms only through a door. This is implemented by
 * making a thick door and requiring you to first step on the door before stepping into the room.
 * 
 * Only when a door is open, someone from outside can go onto the door. From inside, you are always allowed to go onto
 * the door.
 */
public class Door extends BoundedMoveableObject {

    /**
     * The single room that this door connects to. There may be multiple doors connecting to a single room. Stays null
     * until the room is actually connected. Unconnected doors are always open.
     */
    private Room roomBehindTheDoor = null;

    /**
     * Creates a new door.
     * 
     * @param space
     *            The space in which the room will be located.
     * @param context
     *            The context in which the room will be located.
     */
    public Door(ContinuousSpace<Object> space, Context<Object> context) {
        super(space, context);
    }

    public Color getColor() {
        return isOpen() ? nl.tudelft.bw4t.map.Door.COLOR_OPEN : nl.tudelft.bw4t.map.Door.COLOR_CLOSED;
    }

    /**
     * only when a door is open, someone from outside can go onto the door. From inside, you are always allowed to go
     * onto the door.
     * 
     * @return
     */
    public boolean isOpen() {
        return roomBehindTheDoor == null || roomBehindTheDoor.getOccupier() == null;
    }

    /**
     * Set position and orientation of a door
     * 
     * @param x
     *            xpos of door
     * @param y
     *            ypos of door
     * @param ori
     *            is {@link Orientation} of the door.
     */
    public void setPos(double x, double y, Orientation ori) {
        int width = nl.tudelft.bw4t.map.Door.DOOR_THICKNESS;
        int height = nl.tudelft.bw4t.map.Door.DOOR_THICKNESS;
        switch (ori) {
        case HORIZONTAL:
            width = nl.tudelft.bw4t.map.Door.DOOR_WIDTH;
            break;
        case VERTICAL:
        default:
            height = nl.tudelft.bw4t.map.Door.DOOR_WIDTH;
            break;
        }
        setSize(width, height);
        moveTo(x, y);
    }

    /**
     * Connect this door to a room
     * 
     * @param room
     */
    public void connectTo(Room room) {
        roomBehindTheDoor = room;
    }

}