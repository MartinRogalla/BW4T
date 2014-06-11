package nl.tudelft.bw4t.map.editor.controller;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.bw4t.map.BlockColor;
import nl.tudelft.bw4t.map.Entity;
import nl.tudelft.bw4t.map.Zone;
import nl.tudelft.bw4t.map.renderer.AbstractMapController;
import nl.tudelft.bw4t.map.renderer.MapRendererInterface;
import nl.tudelft.bw4t.map.view.ViewBlock;
import nl.tudelft.bw4t.map.view.ViewEPartner;
import nl.tudelft.bw4t.map.view.ViewEntity;

public class MapPreviewController extends AbstractMapController {

	private Map map;
	
	public MapPreviewController(Map theMap) {
		super(theMap.createMap());
		this.map = theMap;
		this.getRenderSettings().setUpdateDelay(1000);
	}

	@Override
	public List<BlockColor> getSequence() {
		return getMap().getSequence();
	}

	@Override
	public int getSequenceIndex() {
		return 0;
	}

	@Override
	public boolean isOccupied(Zone room) {
		return false;
	}

	@Override
	public Set<ViewBlock> getVisibleBlocks() {
		Set<ViewBlock> blocks = new HashSet<ViewBlock>();
		
		for (Zone z : getMap().getZones()) {
	        Rectangle2D roomBox = z.getBoundingbox().getRectangle();
	        
        	double x = roomBox.getMinX();
        	double y = roomBox.getMinY() + 2;
        	double roomsizeX = roomBox.getMinX() + roomBox.getWidth();
        	
	        for (BlockColor color : z.getBlocks()) {
	        	if (x < roomsizeX - 2) {
	        		x = x + 2;
	        	} else {
	        		x = roomBox.getMinX() + 2;
	        		y = y + 3;
	        	}
	            Rectangle2D newpos = new Rectangle2D.Double(x, y, ViewBlock.BLOCK_SIZE, ViewBlock.BLOCK_SIZE);
	            Point2D point = new Point2D.Double(newpos.getX(), newpos.getY());
	            ViewBlock block = new ViewBlock(0, color, point);
	            blocks.add(block);
	        }
		}
		
		return blocks;
	}

	@Override
	public Set<ViewEntity> getVisibleEntities() {
		Set<ViewEntity> entities = new HashSet<ViewEntity>();
		
		for (Entity e : getMap().getEntities()) {
			ViewEntity viewe = new ViewEntity(0, e.getName(), e.getPosition().getX(), e.getPosition().getY(), new ArrayList<ViewBlock>(), 2);
			entities.add(viewe);
		}
		
		return entities;
	}

	@Override
	public Set<ViewEPartner> getVisibleEPartners() {
		return new HashSet<ViewEPartner>();
	}

	@Override
	protected void updateRenderer(MapRendererInterface mri) {
		this.setMap(map.createMap());
		mri.validate();
		mri.repaint();
	}
}
