package nl.tudelft.bw4t.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import nl.tudelft.bw4t.controller.MapController;
import nl.tudelft.bw4t.controller.MapRenderSettings;
import nl.tudelft.bw4t.map.BlockColor;
import nl.tudelft.bw4t.map.Door;
import nl.tudelft.bw4t.map.Zone;
import nl.tudelft.bw4t.map.view.ViewBlock;
import nl.tudelft.bw4t.map.view.ViewEPartner;
import nl.tudelft.bw4t.map.view.ViewEntity;

public class MapRenderer extends JPanel implements MapRendererInterface {
    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 2854272641217795283L;

    private transient MapController controller = null;

    public MapRenderer(MapController controller) {
        this.setController(controller);
    }

    public MapController getController() {
        return controller;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nl.tudelft.bw4t.view.MapRendererInterface#setController(nl.tudelft.bw4t.controller.MapController)
     */
    public void setController(MapController controller) {
        if (this.controller != null) {
            this.controller.removeRenderer(this);
        }
        this.controller = controller;
        this.controller.addRenderer(this);
        updateMinimumSize();
    }

    private void updateMinimumSize() {
        MapRenderSettings set = getController().getRenderSettings();
        Dimension size = new Dimension(set.scale(set.getWorldWidth()), set.scale(set.getWorldHeight())
                + set.getSequenceBlockSize());
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
    }

    /**
     * Processes all objects to display them on the panel
     * 
     * @param g
     *            the graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateMinimumSize();
        Graphics2D g2d = (Graphics2D) g;
        drawRooms(g2d);
        drawLabels(g2d);
        drawDropZone(g2d);
        drawBlocks(g2d);
        drawEntity(g2d);
        drawSequence(g2d);
        drawEPartners(g2d);
    }

    /**
     * Display the goal sequence
     * 
     * @param g2d
     *            the graphics2d object
     */
    public void drawSequence(Graphics2D g2d) {
        MapRenderSettings set = getController().getRenderSettings();
        final int size = set.getSequenceBlockSize();
        final int wh = set.scale(set.getWorldHeight());
        int startPosX = 0;

        for (BlockColor color : getController().getSequence()) {
            g2d.setColor(color.getColor());
            g2d.fill(new Rectangle2D.Double(startPosX, set.scale(set.getWorldHeight()), size, size));
            if (getController().getSequenceIndex() > (startPosX / size)) {
                g2d.setColor(Color.BLACK);
                int[] xpoints = new int[] { startPosX, startPosX, startPosX + size };
                int[] ypoints = new int[] { wh, wh + size, wh + (size / 2) };
                g2d.fillPolygon(xpoints, ypoints, 3);
            }
            startPosX += size;
        }
    }

    /**
     * Process all rooms and their connected doors, and display them in the panel with an outline
     * 
     * @param g2d
     *            the graphics2d object
     */
    public void drawRooms(Graphics2D g2d) {
        MapRenderSettings set = getController().getRenderSettings();

        for (Zone room : getController().getRooms()) {
            boolean occupied = getController().isOccupied(room);

            for (Door door : room.getDoors()) {
                drawDoor(g2d, door, occupied);
            }

            // paint the room
            g2d.setColor(Color.GRAY);
            Shape roomDisplayCoordinates = set.transformRectangle(room.getBoundingbox().getRectangle());
            g2d.fill(roomDisplayCoordinates);
            g2d.setColor(Color.BLACK);
            g2d.draw(roomDisplayCoordinates);

        }
    }

    public void drawDoor(Graphics2D g2d, Door door, boolean closed) {
        MapRenderSettings set = getController().getRenderSettings();

        if (closed) {
            g2d.setColor(Door.COLOR_CLOSED);
        } else {
            g2d.setColor(Door.COLOR_OPEN);
        }

        g2d.fill(set.transformCenterRectangle(new Rectangle2D.Double(door.getPosition().getX(), door.getPosition()
                .getY(), door.getWidth(), door.getHeight())));
    }

    /**
     * Process the labels for the different areas
     * 
     * @param g2d
     *            the graphics2d object
     */
    public void drawLabels(Graphics2D g2d) {
        MapRenderSettings set = getController().getRenderSettings();

        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        for (Zone zone : getController().getZones()) {
            Rectangle2D bbox = zone.getBoundingbox().getRectangle();
            g2d.drawString(zone.getName(),
                    (int) (set.scale(bbox.getCenterX()) - (g2d.getFontMetrics().stringWidth(zone.getName()) / 2.0)),
                    (int) set.scale(bbox.getCenterY()));
        }
    }

    /**
     * Process the drop zone and connected doors and display them in the panel
     * 
     * @param g2d
     *            the graphics2d object
     */
    public void drawDropZone(Graphics2D g2d) {
        MapRenderSettings set = getController().getRenderSettings();
        Zone dropZone = getController().getDropZone();

        g2d.setColor(Color.DARK_GRAY);
        Rectangle2D rect = set.transformRectangle(dropZone.getBoundingbox().getRectangle());
        g2d.fill(rect);
        g2d.setColor(Color.BLACK);
        g2d.draw(rect);

        boolean occupied = getController().isOccupied(dropZone);

        for (Door door : dropZone.getDoors()) {
            drawDoor(g2d, door, occupied);
        }
    }

    /**
     * Process all blocks that are visible and display them with their color
     * 
     * @param g2d
     *            the graphics2d object
     */
    public void drawBlocks(Graphics2D g2d) {
        MapRenderSettings set = getController().getRenderSettings();

        for (ViewBlock box : getController().getVisibleBlocks()) {
            g2d.setColor(box.getColor().getColor());
            g2d.fill(set.transformCenterRectangle(new Rectangle2D.Double(box.getPosition().getX(), box.getPosition()
                    .getY(), ViewBlock.BLOCK_SIZE, ViewBlock.BLOCK_SIZE)));
        }
    }

    /**
     * Display the robot this panel represents. The color is adapted depending on whether it holds a block or not.
     * 
     * @param g2d
     *            the graphics2d object
     */
    public void drawEntity(Graphics2D g2d) {
        MapRenderSettings set = getController().getRenderSettings();

        for (ViewEntity e : getController().getVisibleEntities()) {
            g2d.setColor(e.getColor());
            Point2D loc = e.getLocation();
            g2d.fill(set.transformCenterRectangle(new Rectangle2D.Double(loc.getX(), loc.getY(), e.getRobotSize(),
                    e.getRobotSize())));

            if (set.isRenderEntityName()) {
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString(e.getName(),
                        (int) set.scale(e.getLocation().getX()) - g2d.getFontMetrics().stringWidth(e.getName())/2,
                        (int) set.scale(e.getLocation().getY()) + set.getEntityNameOffset());
            }
        }
    }
    
    public void drawEPartners(Graphics2D g2d) {
    	MapRenderSettings set = getController().getRenderSettings();
    	
    	for (ViewEPartner eP : getController().getVisibleEPartners()) {
    		g2d.setColor(eP.getColor());
    		Point2D loc = eP.getLocation();
    		int x = (int) set.scale(loc.getX());
    		int y = (int) set.scale(loc.getY());
    		final int size = set.scale(eP.EPARTNER_SIZE);
            int[] xpoints = new int[] { x, x - size, x + size };
            int[] ypoints = new int[] { y + size, y - size, y - size };
            g2d.fillPolygon(xpoints, ypoints, 3);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xpoints, ypoints, 3);;
    	}
    }
}
