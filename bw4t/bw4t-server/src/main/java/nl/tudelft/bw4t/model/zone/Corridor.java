package nl.tudelft.bw4t.model.zone;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;

/**
 * A corridor is a zone outside the rooms.
 */
public class Corridor extends Zone {

    public Corridor(nl.tudelft.bw4t.map.Zone zone, ContinuousSpace<Object> space, Context<Object> context) {
        super(zone, space, context);
    }

}