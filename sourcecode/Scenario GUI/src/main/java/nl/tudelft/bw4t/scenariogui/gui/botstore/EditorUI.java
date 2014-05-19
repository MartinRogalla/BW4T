package nl.tudelft.bw4t.scenariogui.gui.botstore;


import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSlider;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * @author Tim
 */
public class EditorUI extends javax.swing.JFrame {

    /**
     * Creates new form EditorUI
     */
    public EditorUI() {
        initComponents();
        setResizable(false);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        sizeSlider = new javax.swing.JSlider();
        sizeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                calculateBatteryUse();
            }
        });


        applyButton = new javax.swing.JButton();
        sizeLabel = new javax.swing.JLabel();
        sizeLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        speedSlider = new javax.swing.JSlider();
        speedSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                calculateBatteryUse();
            }
        });
        speedSlider.setMaximum(75);
        speedSlider.setMinimum(25);
        SpeedLabel = new javax.swing.JLabel();
        SpeedLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        botTitleLabel = new javax.swing.JLabel();
        botTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleSizeSeparator = new javax.swing.JSeparator();

        gripperCheckbox = new javax.swing.JCheckBox();
        gripperCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!gripperCheckbox.isSelected()){
                    //robot.setGripper(false);
                }
                //else robot.setGripper(true);
            }
        });
        colorblindCheckbox = new javax.swing.JCheckBox();
        walkingCheckbox = new javax.swing.JCheckBox();
        jumpingCheckbox = new javax.swing.JCheckBox();
        checkablesLabel = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        botTypeSelector = new javax.swing.JComboBox();

        jScrollPane2.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BotStore");

        sizeSlider.setMajorTickSpacing(1);
        sizeSlider.setMaximum(5);
        sizeSlider.setMinimum(1);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setSnapToTicks(true);
        sizeSlider.setToolTipText("");
        sizeSlider.setValue(2);
        sizeSlider.setValueIsAdjusting(true);

        applyButton.setText("Apply");

        sizeLabel.setText("Size (2 is default)");

        speedSlider.setMajorTickSpacing(5);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setToolTipText("");

        SpeedLabel.setText("Speed (50 is default)");

        botTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        botTitleLabel.setText("Bot X");

        gripperCheckbox.setSelected(true);
        gripperCheckbox.setText("Gripper enabled");

        colorblindCheckbox.setText("Color blindness enabled");

        walkingCheckbox.setSelected(true);
        walkingCheckbox.setText("Walking enabled");

        jumpingCheckbox.setSelected(true);
        jumpingCheckbox.setText("Jumping enabled");

        checkablesLabel.setFont(new Font("Tahoma", Font.PLAIN, 24)); // NOI18N
        checkablesLabel.setText("Checkables");

        resetButton.setText("Reset");
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetButtonMouseClicked(evt);
            }
        });

        botTypeSelector.setModel(new DefaultComboBoxModel(new String[] {"Regular Agent", "E-Partner", "Human User"}));
        botTypeSelector.setToolTipText("");

        batterySlider = new JSlider();
        batterySlider.setValue(0);
        batterySlider.setToolTipText("");
        batterySlider.setSnapToTicks(true);
        batterySlider.setPaintTicks(true);
        batterySlider.setPaintLabels(true);
        batterySlider.setMajorTickSpacing(10);

        lblBatteryCapacity = new JLabel();
        lblBatteryCapacity.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblBatteryCapacity.setText("Battery Capacity (0 is infinity)");

        batteryUseLabel = new JLabel("Average Battery use:");

        batteryUseValueLabel = new JLabel("0.9");

        perTickLabel = new JLabel("per tick");
        perTickLabel.setFont(new Font("Tahoma", Font.ITALIC, 13));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(titleSizeSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGap(18)
                                                                                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                .addComponent(botTitleLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                        .addComponent(speedSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(batterySlider, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                                                                        .addComponent(sizeSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGap(77)
                                                                                .addComponent(sizeLabel))))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(58)
                                                                .addComponent(botTypeSelector, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(64)
                                                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(resetButton)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(applyButton))
                                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                                .addComponent(gripperCheckbox)
                                                                .addComponent(colorblindCheckbox)
                                                                .addComponent(walkingCheckbox)
                                                                .addComponent(checkablesLabel)
                                                                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                                                        .addComponent(jumpingCheckbox)
                                                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                        .addComponent(batteryUseValueLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                                                        .addComponent(perTickLabel))
                                                                                .addComponent(batteryUseLabel))))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(38)
                                                .addComponent(lblBatteryCapacity))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(67)
                                                .addComponent(SpeedLabel)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(134)
                                                .addComponent(titleSizeSeparator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(8)
                                                                .addComponent(botTitleLabel, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18)
                                                                .addComponent(botTypeSelector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18)
                                                                .addComponent(sizeLabel)
                                                                .addGap(2)
                                                                .addComponent(sizeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(SpeedLabel))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(27)
                                                                .addComponent(checkablesLabel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18)
                                                                .addComponent(gripperCheckbox)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(colorblindCheckbox)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(walkingCheckbox)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(jumpingCheckbox)))
                                                .addGap(1)
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(batteryUseLabel)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                                                        .addComponent(batteryUseValueLabel)
                                                                        .addComponent(perTickLabel))
                                                                .addPreferredGap(ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                                                        .addComponent(applyButton)
                                                                        .addComponent(resetButton)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(speedSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18)
                                                                .addComponent(lblBatteryCapacity)
                                                                .addGap(2)
                                                                .addComponent(batterySlider, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap())
        );
        getContentPane().setLayout(layout);

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents



    /**
     * @param evt
     * This method resets all the checkboxes, sliders and average battery use to default values.
     */
    private void resetButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetButtonMouseClicked
        speedSlider.setValue(50);
        sizeSlider.setValue(2);
        batterySlider.setValue(0);
        gripperCheckbox.setSelected(true);
        colorblindCheckbox.setSelected(false);
        jumpingCheckbox.setSelected(true);
        walkingCheckbox.setSelected(true);
        botTypeSelector.setSelectedIndex(0);
        calculateBatteryUse();
    }//GEN-LAST:event_resetButtonMouseClicked

    /**
     * This method should recalculate the average battery use per tick.
     * After calculation, it should update the batteryUseValueLabel label in this GUI.
     */
    private void calculateBatteryUse() {
        int speed = speedSlider.getValue();
        int size = sizeSlider.getValue();
        // Calculate average battery use result
        double res = 0.01*speed + 0.2*size;
        // Set label
        batteryUseValueLabel.setText(String.valueOf(res));
    }

    /**
     * @param args the command line arguments
     */
    private static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               // new EditorUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel SpeedLabel;
    private javax.swing.JButton applyButton;
    private javax.swing.JLabel botTitleLabel;
    private javax.swing.JLabel checkablesLabel;
    private javax.swing.JCheckBox colorblindCheckbox;
    private javax.swing.JCheckBox gripperCheckbox;
    private javax.swing.JComboBox botTypeSelector;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JCheckBox jumpingCheckbox;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JSlider sizeSlider;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JSeparator titleSizeSeparator;
    private javax.swing.JCheckBox walkingCheckbox;
    private JSlider batterySlider;
    private JLabel lblBatteryCapacity;
    private JLabel batteryUseLabel;
    private JLabel batteryUseValueLabel;
    private JLabel perTickLabel;
    // End of variables declaration//GEN-END:variables
}
