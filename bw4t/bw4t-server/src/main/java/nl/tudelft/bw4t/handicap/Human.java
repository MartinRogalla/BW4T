package nl.tudelft.bw4t.handicap;

import nl.tudelft.bw4t.BoundedMoveableObject;
import nl.tudelft.bw4t.blocks.EPartner;
import nl.tudelft.bw4t.robots.AbstractRobot;
import repast.simphony.random.RandomHelper;

/**
 * @author Valentine Mairet
 */
public class Human extends AbstractRobotDecorator {
    
    /**
     * The e-Partner the human is holding.
     */
    private EPartner ePartner;
	
    /**
     * Sets the handicap to active,
     * Adds the handicap to the robot handicap storage.
     * @param p is the HandicapInterface the Human wraps around.
     */
    public Human(IRobot p) {
        super(p);
        robot.getHandicapsList().add("Human");
        this.ePartner = null;
    }
    
    /**
     * @param eP the ePartner to be picked up.
     * @return true if the human can pick it up. 
     */
    @Override
    public boolean canPickUp(BoundedMoveableObject eP) {
        if (eP instanceof EPartner){
            return (robot.distanceTo(eP) <= AbstractRobot.ARM_DISTANCE);
        }
        return getParent().canPickUp(eP);
    }
    
    /**
     * The Human drops the e-Partner they are holding. 
     */
    @Override
    public void dropEPartner() {
    	
    	if (!isHoldingEPartner()) {
    		return;
    	}
    	
    	this.ePartner.addToContext();
    	
        double x = robot.getLocation().getX();
        double y = robot.getLocation().getY();
        
        this.ePartner.moveTo(RandomHelper.nextDoubleFromTo(x - 5, x + 5), RandomHelper.nextDoubleFromTo(y - 5, y + 5));
        
        this.ePartner = null;
    }

    @Override
    public boolean isHoldingEPartner() {
		return this.ePartner != null;
	}
	
    @Override
    public EPartner getEPartner() {
		return this.ePartner;
	}
}
