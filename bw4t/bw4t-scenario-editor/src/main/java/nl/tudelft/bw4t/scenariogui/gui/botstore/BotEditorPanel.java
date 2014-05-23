package nl.tudelft.bw4t.scenariogui.gui.botstore;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import nl.tudelft.bw4t.scenariogui.config.BotConfig;

/**
 * BotEditorPanel which serves as the content pane for the BotEditor frame
 * @author Arun
 */
public class BotEditorPanel extends JPanel {
	
	/**
	 * Side notes from Valentine:
	 * It would be nice to have the value of speed and size automatically change when a MoveSpeed or SizeOverload
	 * handicap is checked. This way, the BotEditorData updates the botSpeed and botSize to the right values,
	 * and we can use these values to instantiate the two handicaps. 
	 * Also, once those values are automatically changed, we should disable user interaction with the sliders.
	 */
	private JPanel botCheckables = new JPanel();
	private JPanel botSliders = new JPanel();
	
	private JComboBox botTypeSelector = new JComboBox();
	
	private JButton applyButton = new JButton("Apply");
	private JButton resetButton = new JButton("Reset");
	private JButton cancelButton = new JButton("Cancel");
	
	private JCheckBox gripperCheckbox = new JCheckBox("Gripper Handicap");
	private JCheckBox colorblindCheckbox = new JCheckBox("Color Blind Handicap");
	private JCheckBox movespeedCheckbox = new JCheckBox("Move Speed Handicap");
	private JCheckBox sizeoverloadCheckbox = new JCheckBox("Size Overload Handicap");
	
	private JSlider sizeSlider = new JSlider();
	private JSlider speedSlider = new JSlider();
	private JSlider batterySlider = new JSlider();

	private JLabel batteryUseValueLabel = new JLabel("0.9");
	
	private BotConfig dataObject = new BotConfig();
	
	/**
	 * Create the botEditorPanel
	 */
	public BotEditorPanel(){
		setLayout(new BorderLayout(20,20));		
		
		createBotCheckablesPanel();
		createBotSlidersPanel();
		
		add(botSliders, BorderLayout.WEST);
		add(botCheckables, BorderLayout.EAST);
		
	}
	
	/**
	 * create the checkables panel
	 */
	public void createBotCheckablesPanel(){
		botCheckables.setLayout(new GridLayout(5,1));
		JLabel checkablesLabel = new JLabel("Checkables");
		JLabel batteryUseLabel = new JLabel("Average Battery use:");
		JLabel perTickLabel = new JLabel("per tick");
		
		checkablesLabel.setFont(new Font("Tahoma",Font.PLAIN,24));
		batteryUseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel buttonPanel = new JPanel();
		JPanel batteryCapPanel = new JPanel();
		JPanel checkablesPanel = new JPanel();
		JPanel empty = new JPanel();
		
		batteryCapPanel.add(batteryUseLabel);
		batteryCapPanel.add(batteryUseValueLabel);
		batteryCapPanel.add(perTickLabel);
		
		buttonPanel.add(applyButton);
		buttonPanel.add(resetButton);
		buttonPanel.add(cancelButton);
		
		botCheckables.add(checkablesLabel);
		checkablesPanel.setLayout(new BoxLayout(checkablesPanel,BoxLayout.PAGE_AXIS));
		checkablesPanel.add(gripperCheckbox);
		checkablesPanel.add(colorblindCheckbox);
		checkablesPanel.add(movespeedCheckbox);
		checkablesPanel.add(sizeoverloadCheckbox);
		botCheckables.add(checkablesPanel);
		botCheckables.add(empty);
		botCheckables.add(batteryCapPanel);
		botCheckables.add(buttonPanel);
		
	}
	/**
	 * creates the botSlidersPanel
	 */
	public void createBotSlidersPanel(){
		botSliders.setLayout(new GridLayout(8,1));
		JLabel botTitleLabel = new JLabel("Bot X");
		botTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		botTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 36));
		
		botTypeSelector.setModel(new DefaultComboBoxModel(new String[]{"Agent", "Human"}));
		
		JLabel sizeLabel = new JLabel("Bot Size");
		sizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sizeLabel.setToolTipText("default is 2");
		JLabel speedLabel = new JLabel("Bot speed");
		speedLabel.setToolTipText("This speed is relative to the bots. The default is 100%");
		speedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel batteryCapacity = new JLabel("Battery Capacity");
		batteryCapacity.setHorizontalAlignment(SwingConstants.CENTER);
		batteryCapacity.setToolTipText("Max capacity on a scale of 10-100");
		
		createSliders();
		
		botSliders.add(botTitleLabel);
		botSliders.add(botTypeSelector);
		botSliders.add(sizeLabel);
		botSliders.add(sizeSlider);
		botSliders.add(speedLabel);
		botSliders.add(speedSlider);
		botSliders.add(batteryCapacity);
		botSliders.add(batterySlider);
	}
	
	/**
	 * sets the default settings for the sliders
	 */
	public void createSliders(){
		sizeSlider.setMajorTickSpacing(1);
		sizeSlider.setMaximum(5);
		sizeSlider.setMinimum(1);
		sizeSlider.setPaintTicks(true);
		sizeSlider.setPaintLabels(true);
		sizeSlider.setSnapToTicks(true);
		sizeSlider.setValue(2);
		sizeSlider.setValueIsAdjusting(true);
		
		speedSlider.setMajorTickSpacing(25);
		speedSlider.setMaximum(200);
		speedSlider.setMinimum(0);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setValue(100);
        speedSlider.setValueIsAdjusting(true);
        
        batterySlider = new JSlider();
        batterySlider.setMinimum(10);
        batterySlider.setMaximum(100);
        batterySlider.setValue(10);
        batterySlider.setSnapToTicks(true);
        batterySlider.setPaintTicks(true);
        batterySlider.setPaintLabels(true);
        batterySlider.setMajorTickSpacing(10);
        
	}
	
	/**
	 * Executes action that needs to happen when  the "Apply" button is pressed.
	 * TODO save the bot
	 */
	public void applyAction() {
		setDataObject();
	}
	
	/**
	 * Executes action that needs to happen when  the "Reset" button is pressed.
	 * Resets to default settings
	 */
	public void resetAction(){
		speedSlider.setValue(100);
        sizeSlider.setValue(2);
        batterySlider.setValue(0);
        gripperCheckbox.setSelected(false);
        colorblindCheckbox.setSelected(false);
        sizeoverloadCheckbox.setSelected(false);
        movespeedCheckbox.setSelected(false);
        botTypeSelector.setSelectedIndex(0);
        calculateBatteryUse();
	}
	
	/**
	 * Executes action that needs to happen when  the "Cancel" button is pressed. 
	 * closes the BotEditor
	 */
	
	public void cancelAction(){	
		
	}
	
	/**
     * This method should recalculate the average battery use per tick.
     * After calculation, it should update the batteryUseValueLabel label in this GUI.
     */
	public void calculateBatteryUse(){
		int speed = speedSlider.getValue();
    	int size = sizeSlider.getValue();
    	// Calculate average battery use result
    	double res = 0.01*speed + 0.2*size;
    	// Set label
    	batteryUseValueLabel.setText(String.valueOf(res));
	}
	
	/**
	 * Returns the applybutton
	 * @return the applyButton
	 */
	public JButton getApplyButton() {
		return applyButton;
	}

	public void setApplyButton(JButton applyButton) {
		this.applyButton = applyButton;
	}

	public JButton getResetButton() {
		return resetButton;
	}

	public void setResetButton(JButton resetButton) {
		this.resetButton = resetButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public JComboBox getBotTypeSelector() {
		return botTypeSelector;
	}

	public void setBotTypeSelector(JComboBox botTypeSelector) {
		this.botTypeSelector = botTypeSelector;
	}

	public JCheckBox getGripperCheckbox() {
		return gripperCheckbox;
	}

	public void setGripperCheckbox(JCheckBox gripperCheckbox) {
		this.gripperCheckbox = gripperCheckbox;
	}

	public JCheckBox getColorblindCheckbox() {
		return colorblindCheckbox;
	}

	public void setColorblindCheckbox(JCheckBox colorblindCheckbox) {
		this.colorblindCheckbox = colorblindCheckbox;
	}

	public JCheckBox getmovespeedCheckbox() {
		return movespeedCheckbox;
	}

	public void setmovespeedCheckbox(JCheckBox movespeedCheckbox) {
		this.movespeedCheckbox = movespeedCheckbox;
	}

	public JCheckBox getsizeoverloadCheckbox() {
		return sizeoverloadCheckbox;
	}

	public void setsizeoverloadCheckbox(JCheckBox sizeoverloadCheckbox) {
		this.sizeoverloadCheckbox = sizeoverloadCheckbox;
	}

	public JSlider getSizeSlider() {
		return sizeSlider;
	}

	public void setSizeSlider(JSlider sizeSlider) {
		this.sizeSlider = sizeSlider;
	}

	public JSlider getSpeedSlider() {
		return speedSlider;
	}

	public void setSpeedSlider(JSlider speedSlider) {
		this.speedSlider = speedSlider;
	}

	public JSlider getBatterySlider() {
		return batterySlider;
	}

	public void setBatterySlider(JSlider batterySlider) {
		this.batterySlider = batterySlider;
	}

	public JLabel getBatteryUseValueLabel() {
		return batteryUseValueLabel;
	}

	public void setBatteryUseValueLabel(JLabel batteryUseValueLabel) {
		this.batteryUseValueLabel = batteryUseValueLabel;
	}
	
	/**
	 * This method plugs the GUI values into the data object.
	 */
	public void setDataObject() {
		dataObject.setBotSize(sizeSlider.getValue());
		dataObject.setBotSpeed(speedSlider.getValue());
		dataObject.setBotBatteryCapacity(batterySlider.getValue());
		dataObject.setColorBlindHandicap(colorblindCheckbox.isEnabled());
		dataObject.setGripperHandicap(gripperCheckbox.isEnabled());
		dataObject.setMoveSpeedHandicap(movespeedCheckbox.isEnabled());
		dataObject.setSizeOverloadHandicap(sizeoverloadCheckbox.isEnabled());
	}
	
	public BotConfig getDataObject() {
		return dataObject;
	}

}
