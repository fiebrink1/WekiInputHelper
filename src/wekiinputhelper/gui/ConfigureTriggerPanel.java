/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.gui;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import wekiinputhelper.Criterion;
import wekiinputhelper.Criterion.AppliesTo;
import wekiinputhelper.Criterion.CriterionType;
import wekiinputhelper.Criterion.HowLong;
import wekiinputhelper.InputTriggerer;
import wekiinputhelper.OutputManager;
import wekiinputhelper.TriggerOnNth;
import wekiinputhelper.TriggerOnReceive;
import wekiinputhelper.TriggerOnTimes;
import wekiinputhelper.WekiInputHelper;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class ConfigureTriggerPanel extends javax.swing.JPanel {

    private WekiInputHelper w;
    private static final Logger logger = Logger.getLogger(ConfigureTriggerPanel.class.getName());

    /**
     *
     * Creates new form AddInputsPanel
     */
    public ConfigureTriggerPanel() {
        initComponents();
        populateTriggerTypes();
    }

    private void populateTriggerTypes() {
        comboCriterion.setModel(new javax.swing.DefaultComboBoxModel(Criterion.triggerDescriptors));
    }

    public void setWekiInputHelper(WekiInputHelper w) {
        this.w = w;
        updateComboInput();
        updateFormForCurrentTrigger();
        w.getOutputManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(OutputManager.PROP_OUTPUTGROUP)) {
                    outputGroupChanged();
                }
            }
        });
    }
    
    private void outputGroupChanged() {
        updateComboInput();
    }
    
    private void updateComboInput() {
        //maintain name if possible
        int lastSelected = comboInput.getSelectedIndex();
        String lastName = (String)comboInput.getModel().getSelectedItem();
        
        
        String[] inputNames = w.getInputManager().getInputNames();
        String[] outputNames;
        if (w.getOutputManager().hasValidOutputGroup()) {
             outputNames = w.getOutputManager().getOutputNames();
        } else {
            outputNames = new String[0];
        }
        String[] allNames = new String[inputNames.length + outputNames.length];
        System.arraycopy(inputNames, 0, allNames, 0, inputNames.length);
        System.arraycopy(outputNames, 0, allNames, inputNames.length, outputNames.length);
        DefaultComboBoxModel model = new DefaultComboBoxModel(allNames);
        comboInput.setModel(model);
        
        if (allNames.length == 0 || lastSelected == -1) {
            return;
        }
        if (allNames[lastSelected].equals(lastName)) {
            comboInput.setSelectedIndex(lastSelected);
        } else {
            tryToSelectInputName(lastName);
        }
    }
    
    private void tryToSelectInputName(String name) {
        int index = ((DefaultComboBoxModel)comboInput.getModel()).getIndexOf(name);
        if (index != -1) {
            comboInput.setSelectedIndex(index);
        }

    }
    

    private void updateFormForCurrentTrigger() {
        InputTriggerer t = w.getInputTriggerer();
        Criterion c = t.getCriterion();
        if (t instanceof TriggerOnReceive) {
            buttonGroupSendWhen.setSelected(radioInputArrives.getModel(), true);
        } else if (t instanceof TriggerOnNth) {
            buttonGroupSendWhen.setSelected(radioConstantRateMessages.getModel(), true);
            textRateNumMessages.setText(Integer.toString(((TriggerOnNth) t).getN()));
        } else {
            buttonGroupSendWhen.setSelected(radioConstantRateMS.getModel(), true);
            textRateNumMessages.setText(Integer.toString(((TriggerOnTimes)t).getTime()));
        }
        
        if (c.getType() == CriterionType.NONE) {
            checkConstraint.setSelected(false);
            updateCriterion();
        } else {
            checkConstraint.setSelected(true);
            updateCriterion();
            int i = c.getInputIndex();
            if (c.getAppliesTo() == AppliesTo.INPUT) {
                comboInput.setSelectedIndex(i);
            } else {
                comboInput.setSelectedIndex(w.getInputManager().getNumInputs() +i);
            }
            
            int typeIndex = Criterion.getIndexForDescriptor(c.getType());
            comboCriterion.setSelectedIndex(typeIndex);
            updateCriterionCard();
            if (c.getType() != CriterionType.CHANGE) {
                textCriterionValue.setText(Double.toString(c.getCriterionValue()));
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupSendWhen = new javax.swing.ButtonGroup();
        buttonGroupTriggerStop = new javax.swing.ButtonGroup();
        panelTop = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        radioInputArrives = new javax.swing.JRadioButton();
        radioConstantRateMessages = new javax.swing.JRadioButton();
        radioConstantRateMS = new javax.swing.JRadioButton();
        textRateNumMessages = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        textRateMS = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        comboInput = new javax.swing.JComboBox();
        comboCriterion = new javax.swing.JComboBox();
        panelCriterionValueCardParent = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        textCriterionValue = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        radioSendOnce = new javax.swing.JRadioButton();
        checkConstraint = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        radioKeepSending = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));

        panelTop.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Send values:");

        buttonGroupSendWhen.add(radioInputArrives);
        radioInputArrives.setSelected(true);
        radioInputArrives.setText("When an input message arrives");

        buttonGroupSendWhen.add(radioConstantRateMessages);
        radioConstantRateMessages.setText("At a constant rate of once per every ");
        radioConstantRateMessages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioConstantRateMessagesActionPerformed(evt);
            }
        });

        buttonGroupSendWhen.add(radioConstantRateMS);
        radioConstantRateMS.setText("At a constant rate of ");

        textRateNumMessages.setText("5");
        textRateNumMessages.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textRateNumMessagesKeyTyped(evt);
            }
        });

        jLabel3.setText("ms");

        textRateMS.setText("100");
        textRateMS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textRateMSKeyTyped(evt);
            }
        });

        jLabel4.setText("messages received");

        comboInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "input-1", "input-2", "input-3" }));

        comboCriterion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "is greater than or equal to", "is less than or equal to", "changes" }));
        comboCriterion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCriterionActionPerformed(evt);
            }
        });

        panelCriterionValueCardParent.setLayout(new java.awt.CardLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        textCriterionValue.setText("0");
        textCriterionValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textCriterionValueKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(textCriterionValue, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textCriterionValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelCriterionValueCardParent.add(jPanel2, "cardCriterion");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        panelCriterionValueCardParent.add(jPanel3, "cardBlank");

        buttonGroupTriggerStop.add(radioSendOnce);
        radioSendOnce.setSelected(true);
        radioSendOnce.setText("Send just once when this condition is met");
        radioSendOnce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioSendOnceActionPerformed(evt);
            }
        });

        checkConstraint.setText("Optionally, also impose the following constraint:");
        checkConstraint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkConstraintActionPerformed(evt);
            }
        });

        jLabel5.setText("Only send when");

        buttonGroupTriggerStop.add(radioKeepSending);
        radioKeepSending.setText("Keep sending as long as this condition is true");
        radioKeepSending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioKeepSendingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelTopLayout.createSequentialGroup()
                        .addComponent(radioConstantRateMS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textRateMS))
                    .addComponent(radioInputArrives, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioConstantRateMessages, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addComponent(textRateNumMessages, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))))
            .addGroup(panelTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2))
            .addComponent(checkConstraint)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioKeepSending)
                    .addComponent(radioSendOnce)
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, 0)
                        .addComponent(comboInput, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(comboCriterion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(panelCriterionValueCardParent, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioInputArrives)
                .addGap(0, 0, 0)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioConstantRateMS)
                    .addComponent(textRateMS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(0, 0, 0)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioConstantRateMessages)
                    .addComponent(textRateNumMessages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(27, 27, 27)
                .addComponent(checkConstraint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboCriterion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(panelCriterionValueCardParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioSendOnce)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioKeepSending)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void radioConstantRateMessagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioConstantRateMessagesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioConstantRateMessagesActionPerformed

    private void radioSendOnceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioSendOnceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioSendOnceActionPerformed

    private void radioKeepSendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioKeepSendingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioKeepSendingActionPerformed

    private void comboCriterionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCriterionActionPerformed
        updateCriterionCard();
        updateCriterion();
    }//GEN-LAST:event_comboCriterionActionPerformed

    private void updateCriterionCard() {
                int i = comboCriterion.getSelectedIndex();
        if (Criterion.typesForTriggerDescriptors[i] == CriterionType.CHANGE) {
            ((CardLayout)panelCriterionValueCardParent.getLayout()).show(panelCriterionValueCardParent, "cardBlank");
        } else {
            ((CardLayout)panelCriterionValueCardParent.getLayout()).show(panelCriterionValueCardParent, "cardCriterion");
        }
    }
    
    private void textRateMSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRateMSKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textRateMSKeyTyped

    private void textRateNumMessagesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRateNumMessagesKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textRateNumMessagesKeyTyped

    private void textCriterionValueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textCriterionValueKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter)) && !(enter == '.') && !(enter == '-')) {
            evt.consume();
        }
    }//GEN-LAST:event_textCriterionValueKeyTyped

    private void checkConstraintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkConstraintActionPerformed
        updateCriterion();
    }//GEN-LAST:event_checkConstraintActionPerformed

    private void updateCriterion() {
        boolean isConstraint = checkConstraint.isSelected();
        if (! isConstraint) {
            radioSendOnce.setEnabled(false);
            radioKeepSending.setEnabled(false);
            return;
        }
        
        boolean isChange = (Criterion.typesForTriggerDescriptors[comboCriterion.getSelectedIndex()] == CriterionType.CHANGE);
        radioSendOnce.setEnabled(!isChange);
        radioKeepSending.setEnabled(!isChange); 
    }

    private boolean validateForm() {
        if (buttonGroupSendWhen.isSelected(radioConstantRateMS.getModel())) {
            try {
                int i = Integer.parseInt(textRateMS.getText());
                if (i < 1) {
                    Util.showPrettyErrorPane(this, "Rate must be at least 1 ms");
                    return false;
                }
            } catch (NumberFormatException ex) {
                Util.showPrettyErrorPane(this, "Rate must be at least 1 ms");
                return false;
            }
        } else if (buttonGroupSendWhen.isSelected(radioConstantRateMessages.getModel())) {
            try {
                int i = Integer.parseInt(textRateNumMessages.getText());
                if (i < 1) {
                    Util.showPrettyErrorPane(this, "Rate must be at least 1 message");
                    return false;
                }
            } catch (NumberFormatException ex) {
                Util.showPrettyErrorPane(this, "Rate must be at least 1 message");
                return false;
            }
        }
        if (checkConstraint.isSelected()) {
            int selected = comboCriterion.getSelectedIndex();
            Criterion.CriterionType type = Criterion.typesForTriggerDescriptors[selected];
            if (type != CriterionType.CHANGE) {
                try {
                    double val = Double.parseDouble(textCriterionValue.getText());
                } catch (NumberFormatException ex) {
                    Util.showPrettyErrorPane(this, "Send criterion must be a valid number; "
                            + textCriterionValue.getText() + " is invalid");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean prepareToSave() {
        if (!validateForm()) {
            return false;
        }
        InputTriggerer trig = makeTriggererFromForm();
        w.setInputTriggerer(trig);
        return true;
    }

    public boolean prepareToAdvance() {
        return prepareToSave();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSendWhen;
    private javax.swing.ButtonGroup buttonGroupTriggerStop;
    private javax.swing.JCheckBox checkConstraint;
    private javax.swing.JComboBox comboCriterion;
    private javax.swing.JComboBox comboInput;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel panelCriterionValueCardParent;
    private javax.swing.JPanel panelTop;
    private javax.swing.JRadioButton radioConstantRateMS;
    private javax.swing.JRadioButton radioConstantRateMessages;
    private javax.swing.JRadioButton radioInputArrives;
    private javax.swing.JRadioButton radioKeepSending;
    private javax.swing.JRadioButton radioSendOnce;
    private javax.swing.JTextField textCriterionValue;
    private javax.swing.JTextField textRateMS;
    private javax.swing.JTextField textRateNumMessages;
    // End of variables declaration//GEN-END:variables

    //Requires form to be validated first!
    private InputTriggerer makeTriggererFromForm() {
        Criterion c;
        if (checkConstraint.isSelected()) {
            int inputIndex = comboInput.getSelectedIndex();
            int typeIndex = comboCriterion.getSelectedIndex();
            boolean isInput = inputIndex < w.getInputManager().getNumInputs();
            if (! isInput) {
                inputIndex = inputIndex - w.getInputManager().getNumInputs();
            }
            
            double val = 0.;
            Criterion.CriterionType type = Criterion.typesForTriggerDescriptors[typeIndex];
            if (type != CriterionType.CHANGE) {
                try {
                    val = Double.parseDouble(textCriterionValue.getText());
                } catch (NumberFormatException ex) {
                    Util.showPrettyErrorPane(this, "Invalid criterion value of "
                            + textCriterionValue.getText() + " will not be used");
                }
            }
            Criterion.HowLong howLong = HowLong.REPEAT;
            if (buttonGroupTriggerStop.isSelected(radioSendOnce.getModel())) {
                howLong = HowLong.ONCE;
            }
            
            Criterion.AppliesTo appliesTo = isInput ? AppliesTo.INPUT : AppliesTo.OUTPUT;
            
            c = new Criterion(type, howLong, inputIndex, appliesTo, val);
        } else {
            c = new Criterion();
        }

        if (buttonGroupSendWhen.isSelected(radioInputArrives.getModel())) {
            return new TriggerOnReceive(c);
        } else if (buttonGroupSendWhen.isSelected(radioConstantRateMS.getModel())) {
            try {
                int rate = Integer.parseInt(textRateMS.getText());
                return new TriggerOnTimes(w, rate, c);
            } catch (NumberFormatException ex) {
                Util.showPrettyErrorPane(this, "Invalid rate selected; using 10ms instead");
                return new TriggerOnTimes(w, 10, c);
            }
        } else {
            //send every nth message
            try {
                int rate = Integer.parseInt(textRateNumMessages.getText());
                return new TriggerOnNth(rate, c);
            } catch (NumberFormatException ex) {
                Util.showPrettyErrorPane(this, "Invalid rate selected; using every 10th message instead");
                return new TriggerOnNth(10, c);
            }
        }
    }

    void initializeFromExisting() {
        InputTriggerer t = w.getInputTriggerer();
        if (t == null) {
            logger.log(Level.INFO, "Null triggerer");
            return;
        }
        updateComboInput();
        if (t instanceof TriggerOnReceive) {
            radioInputArrives.setSelected(true);
        } else if (t instanceof TriggerOnNth) {
            radioConstantRateMessages.setSelected(true);
            textRateNumMessages.setText(Integer.toString(((TriggerOnNth)t).getN()));
        } else {
            radioConstantRateMS.setSelected(true);
            textRateMS.setText(Integer.toString(((TriggerOnTimes)t).getTime()));
        }
        Criterion c = t.getCriterion();
        if (c.getType() == CriterionType.NONE) {
            checkConstraint.setSelected(false);
        } else {
            checkConstraint.setSelected(true);
            int i = c.getInputIndex();
            if (c.getAppliesTo() == AppliesTo.INPUT) {
                comboInput.setSelectedIndex(i);
            } else {
                int tmp = w.getInputManager().getNumInputs() + i;
                System.out.println("Tmp = " + tmp);
                comboInput.setSelectedIndex(w.getInputManager().getNumInputs() + i);
            }
            if (c.getType() != CriterionType.CHANGE) {
                textCriterionValue.setText(Double.toString(c.getCriterionValue()));
            }
            int whichIndex = Criterion.getIndexForDescriptor(c.getType());
            comboCriterion.setSelectedIndex(whichIndex);  
            
            if (c.getHowLong() ==  HowLong.ONCE) {
                radioSendOnce.setSelected(true);
            } else {
                radioKeepSending.setSelected(true);
            }
            
        }
        updateCriterion();
        updateCriterionCard();

    }

}
