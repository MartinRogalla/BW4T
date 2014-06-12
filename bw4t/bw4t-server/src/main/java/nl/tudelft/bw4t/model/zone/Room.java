package nl.tudelft.bw4t.model.zone;

import java.awt.Color;

import nl.tudelft.bw4t.map.RenderOptions;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Abstract definition of a room in the environment.
 */
public abstract class Room extends Zone {

    /**
     * CHECK is color actually used?
     */
    private final Color color;
    private RenderOptions renderOptions;

    /**
     * Creates a new room that has the given color.
     * 
     * @param color
     *            The color of the room, used for visualization.
     * @param space
     *            The space in which the room should be placed.
     * @param context
     *            The context in which the room should be placed.
     */
    public Room(Color color, nl.tudelft.bw4t.map.Zone roomzone, ContinuousSpace<Object> space, Grid<Object> grid, Context<Object> context) {
        super(roomzone, space, grid, context);
        this.color = color;
        this.renderOptions = roomzone.getRenderOptions();
    }

    public Color getColor() {
        return color;
    }

    public RenderOptions getRenderOptions() {
        return renderOptions;
    }

}
