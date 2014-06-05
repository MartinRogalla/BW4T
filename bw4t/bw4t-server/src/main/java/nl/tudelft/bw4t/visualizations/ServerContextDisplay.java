package nl.tudelft.bw4t.visualizations;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.ValidationException;

import nl.tudelft.bw4t.BW4TBuilder;
import nl.tudelft.bw4t.controller.ServerMapController;
import nl.tudelft.bw4t.server.environment.BW4TEnvironment;
import nl.tudelft.bw4t.server.environment.Launcher;
import nl.tudelft.bw4t.server.environment.Stepper;
import nl.tudelft.bw4t.view.MapRenderer;

import org.apache.log4j.Logger;

import repast.simphony.context.Context;
import eis.exceptions.ManagementException;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * Used for directly displaying the simulation from the context, unlike BW4TRenderer does not use percepts and can show
 * all entities. Only used on the server side (BW4TEnvironment side).
 * <p>
 * Note, this renderer is largely independent of repast, so even though Repast has its own rendering tools we don't use
 * that.
 * <p>
 * Also note that this is a runnable and runs in its own thread with a refresh rate of 10Hz, started by
 * {@link BW4TBuilder}, see {@link #run()}.
 */
@SuppressWarnings("serial")
public class ServerContextDisplay extends JFrame {

    /**
     * The log4j logger, logs to the console.
     */
    private static final Logger LOGGER = Logger.getLogger(ServerContextDisplay.class);

    private final MapRenderer myRenderer;
    private final ServerMapController controller;

    /**
     * Create a new instance of this class and initialize it
     * 
     * @param context
     *            the central data model of Repast
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws FileNotFoundException
     */
    public ServerContextDisplay(Context context) throws VisualizationsException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            LOGGER.warn("failed to setup java look and feel", e);
        }
        setTitle("BW4T");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        controller = new ServerMapController(Launcher.getEnvironment().getMap(), context);
        myRenderer = new MapRenderer(controller);
        add(new JScrollPane(myRenderer), BorderLayout.CENTER);
        try {
            add(new ControlPanel(this), BorderLayout.NORTH);
        } catch (FileNotFoundException e) {
            throw new VisualizationsException(e);
        }
        pack();
        setVisible(true);
    }

    /**
     * We close ourselves if user clicks on reset. We need to since we are created from {@link BW4TBuilder} which does
     * not assume ownership; and nobody else can create us it seems.
     */
    public void close() {
        myRenderer.getController().setRunning(false);
        setVisible(false);
    }

}

/**
 * local speed panel at top of the window.
 */
@SuppressWarnings("serial")
class ControlPanel extends JPanel {

    /**
     * The log4j logger, logs to the console.
     */
    private static final Logger LOGGER = Logger.getLogger(ControlPanel.class);

    /**
     * used to close the window when user presses reset.
     */
    final ServerContextDisplay displayer;

    final JLabel tpsDisplay = new JLabel("0.0 tps");

    private final JSlider slider;

    /**
     * @param disp
     *            is used to close the window when user presses reset.
     * @throws FileNotFoundException
     */
    public ControlPanel(ServerContextDisplay disp) throws FileNotFoundException {
        this.displayer = disp;
        setLayout(new BorderLayout());
        add(new JLabel("Speed"), BorderLayout.WEST);
        // slider goes in percentage, 100 is fastest
        slider = new JSlider(0, 100, 0);
        slider.setEnabled(false);
        final JButton resetbutton = new JButton("Reset");
        add(tpsDisplay, BorderLayout.WEST);
        add(slider, BorderLayout.CENTER);
        add(resetbutton, BorderLayout.EAST);
        add(new MapSelector(displayer), BorderLayout.NORTH);

        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                // now we have speed (on Hz axis) and we need delay (s axis).
                // first get interpolated speed.
                BW4TEnvironment.getInstance().setTps(calculateTpsFromSlider());
                updateTpsDisplay();
            }
        });

        resetbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    // displayer.close(); now part of reset
                    BW4TEnvironment.getInstance().reset();
                } catch (Exception e) {
                    LOGGER.error("failed to reset the environment", e);
                }
            }
        });

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        slider.setEnabled(true);
                        slider.setValue(calculateSliderValueFromDelay());
                        updateTpsDisplay();
                    }
                });
            }
        }, 1000);
        updateTpsDisplay();
    }

    public double calculateTpsFromSlider() {
        double percent = (slider.getValue() - slider.getMinimum())
                / (double) (slider.getMaximum() - slider.getMinimum());
        int value = (int) (Stepper.MIN_TPS + (Stepper.MAX_TPS - Stepper.MIN_TPS) * percent);
        LOGGER.trace("Delay Calculated from the slider: " + value);
        return value;
    }

    public int calculateSliderValueFromDelay() {
        double delay = BW4TEnvironment.getInstance().getTps();
        double percent = (delay - Stepper.MIN_TPS) / (Stepper.MAX_TPS - Stepper.MIN_TPS);
        int value = (int) (slider.getMinimum() + (slider.getMaximum() - slider.getMinimum()) * percent);
        LOGGER.trace("Slider value from delay: " + value);
        return value;
    }

    public void updateTpsDisplay() {
        tpsDisplay.setText(String.format("%3.1f tps", BW4TEnvironment.getInstance().getTps()));
    }
}

/**
 * This combo box allows user to select a new map. Doing that will reset the server and reload the new map.
 * <p>
 * We assume that a directory named "Maps" is available in the current directory, and that it only contains maps.
 */
@SuppressWarnings("serial")
class MapSelector extends JPanel {

    /**
     * The log4j logger, logs to the console.
     */
    private static final Logger LOGGER = Logger.getLogger(MapSelector.class);

    public MapSelector(final ServerContextDisplay displayer) throws FileNotFoundException {
        setLayout(new BorderLayout());
        add(new JLabel("Change Map"), BorderLayout.WEST);
        Vector<String> maps = getMaps();
        final JComboBox mapselector = new JComboBox(maps);

        // find the current map in the list and highlight it
        String mapname = BW4TEnvironment.getMapLocation();
        mapselector.setSelectedItem(mapname);

        add(mapselector, BorderLayout.CENTER);
        mapselector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Map<String, Parameter> parameters = new HashMap<String, Parameter>();
                parameters.put("map", new Identifier((String) mapselector.getSelectedItem()));
                try {
                    // displayer.close(); now part of reset
                    BW4TEnvironment.getInstance().reset(parameters);
                } catch (ManagementException e) {
                    LOGGER.error("failed to reset the environment", e);
                }
            }
        });
    }

    /**
     * get list of available map names.
     * 
     * @return vector with all available map names in the Maps directory.
     * @throws FileNotFoundException
     */
    private Vector<String> getMaps() throws FileNotFoundException {
        File f = new File(System.getProperty("user.dir") + "/maps");
        if (f.list() == null) {
            throw new FileNotFoundException("maps directory not found");
        }
        Vector<String> maps = new Vector<String>(Arrays.asList(f.list()));
        return maps;
    }
}
