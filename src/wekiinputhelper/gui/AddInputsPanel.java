/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import wekiinputhelper.InputManager;
import wekiinputhelper.Modifiers.ModifiedInput;
import wekiinputhelper.Modifiers.ModifiedInputSingle;
import wekiinputhelper.Modifiers.ModifiedInputVector;
import wekiinputhelper.WekiInputHelper;
import wekiinputhelper.gui.UpDownDeleteGUI.UpDownDeleteNotifiable;
import wekiinputhelper.osc.OSCModifiedInputGroup;
import wekiinputhelper.osc.OSCSender;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class AddInputsPanel extends javax.swing.JPanel implements UpDownDeleteNotifiable {

    private int numNewPanels = 0;
    private final ArrayList<JSeparator> separators = new ArrayList<>();
    private final ArrayList<ModifierConfigRow> outputPanels = new ArrayList<>();
    private final ArrayList<UpDownDeleteGUI> upDownDeletePanels = new ArrayList<>();
    private final ArrayList<JPanel> outputParentPanels = new ArrayList<>();
    private WekiInputHelper w;
    private static final Logger logger = Logger.getLogger(AddInputsPanel.class.getName());

    /**
     *
     * Creates new form AddInputsPanel
     */
    public AddInputsPanel() {
        initComponents();
    }

    public void setWekiInputHelper(WekiInputHelper w) {
        this.w = w;
        updateLengthLabel(computeNumTotalOutputs());
        updateSendInputs(w.getOSCSender().isSendInputs());
        w.getInputManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(InputManager.PROP_INPUTGROUP)) {
                    inputGroupChanged();
                }
            }

        });
        /*w.getOutputManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(OutputManager.PROP_OUTPUTGROUP)) {
                    outputGroupChanged();
                }
            }
        }); */

        w.getOSCSender().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(OSCSender.PROP_PORT)) {
                    updateSendPort((Integer) evt.getNewValue());
                } else if (evt.getPropertyName().equals(OSCSender.PROP_HOSTNAME)) {
                    updateHostname((String) ((InetAddress) evt.getNewValue()).getHostName());
                } else if (evt.getPropertyName().equals(OSCSender.PROP_OSCMESSAGE)) {
                    updateOscMessage((String) evt.getNewValue());
                } else if (evt.getPropertyName().equals(OSCSender.PROP_SENDINPUTS)) {
                    updateSendInputs((Boolean) evt.getNewValue());
                }
            }

        });
        /*w.addPropertyChangeListener(new PropertyChangeListener() {

         @Override
         public void propertyChange(PropertyChangeEvent evt) {
         if (evt.getPropertyName().equals(WekiInputHelper.PROP_INPUTTRIGGERER))
         triggerChanged();
         }
         }); */
    }

    private void updateSendInputs(boolean sendInputs) {
        checkIncludeOriginals.setSelected(sendInputs);
    }

    private void inputGroupChanged() {
        updateLengthLabel(computeNumTotalOutputs());

    }

    public void initializeForOutputs() {
        updateFormForOutputs();
        updateLengthLabel(computeNumTotalOutputs());
    }

    private void updateSendPort(int p) {
        textPort.setText(Integer.toString(p));
    }

    private void updateHostname(String name) {
        textHost.setText(name);
    }

    private void updateOscMessage(String msg) {
        textOSCMessage.setText(msg);
    }

    private void updateFormForOutputs() {
        OSCModifiedInputGroup mig = w.getOutputManager().getOutputGroup();
        panelOutputsContainer.removeAll();
        outputPanels.clear();
        upDownDeletePanels.clear();
        if (mig != null) {
            for (int i = 0; i < mig.getNumOutputTypes(); i++) {
                addInputPanelForExisting(mig.getOutput(i));
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

        panelTop = new javax.swing.JPanel();
        checkIncludeOriginals = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        textOSCMessage = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        textHost = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        textPort = new javax.swing.JTextField();
        buttonSendExample = new javax.swing.JButton();
        buttonSendNames = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        labelNumValues = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        buttomPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        scrollModifiers = new javax.swing.JScrollPane();
        panelOutputsContainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        panelTop.setBackground(new java.awt.Color(255, 255, 255));

        checkIncludeOriginals.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        checkIncludeOriginals.setSelected(true);
        checkIncludeOriginals.setText("Include original inputs in sent message");
        checkIncludeOriginals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkIncludeOriginalsActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel1.setText("Sending OSC messsage:");

        textOSCMessage.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        textOSCMessage.setText("/wek/inputs");
        textOSCMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textOSCMessageActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel28.setText("Sending to host (IP address or name):");

        textHost.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        textHost.setText("localhost");

        jLabel29.setText("Port:");

        textPort.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        textPort.setText("6449");
        textPort.setMinimumSize(new java.awt.Dimension(62, 28));
        textPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textPortKeyTyped(evt);
            }
        });

        buttonSendExample.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        buttonSendExample.setText("Send example OSC message");
        buttonSendExample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendExampleActionPerformed(evt);
            }
        });

        buttonSendNames.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        buttonSendNames.setText("Send new input names via OSC");
        buttonSendNames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendNamesActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        jLabel2.setText("Configure new inputs below:");

        labelNumValues.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        labelNumValues.setText("Based on the settings below, 17 values will be sent in total");

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addComponent(buttonSendExample)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonSendNames, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelNumValues, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textOSCMessage))
                    .addComponent(checkIncludeOriginals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textHost, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textPort, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelTopLayout.createSequentialGroup()
                        .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2))
                        .addContainerGap())))
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTopLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textOSCMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(textHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(textPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNumValues, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSendExample)
                    .addComponent(buttonSendNames))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkIncludeOriginals)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2))
        );

        buttomPanel.setBackground(new java.awt.Color(255, 255, 255));

        jButton3.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jButton3.setText("Add new");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttomPanelLayout = new javax.swing.GroupLayout(buttomPanel);
        buttomPanel.setLayout(buttomPanelLayout);
        buttomPanelLayout.setHorizontalGroup(
            buttomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttomPanelLayout.createSequentialGroup()
                .addComponent(jButton3)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        buttomPanelLayout.setVerticalGroup(
            buttomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttomPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3))
        );

        panelOutputsContainer.setBackground(new java.awt.Color(255, 255, 255));
        panelOutputsContainer.setLayout(new javax.swing.BoxLayout(panelOutputsContainer, javax.swing.BoxLayout.Y_AXIS));
        scrollModifiers.setViewportView(panelOutputsContainer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(buttomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollModifiers)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollModifiers, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textOSCMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textOSCMessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textOSCMessageActionPerformed

    private void textPortKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPortKeyTyped
        char enter = evt.getKeyChar();
        if (!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textPortKeyTyped

    private void buttonSendNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendNamesActionPerformed
        String hostName = textHost.getText().trim();
        InetAddress address;
        try {
            // try {
            address = InetAddress.getByName(hostName);
            //  } catch 
        } catch (UnknownHostException ex) {
            Util.showPrettyErrorPane(this, "Unknown host " + hostName);
            return;
        }

        String portString = textPort.getText().trim();

        if (!Util.checkIsPositiveNumber(textPort, "Port number", this)) {
            return;
        }
        int port = Integer.parseInt(textPort.getText());
        try {
            boolean v = validateForm();
            if (v) {
                String[] names = getAllOutputNames();
                w.getOSCSender().sendNamesMessage(address, port, names);
            }
        } catch (IOException ex) {
            Util.showPrettyErrorPane(this, "Could not send message: " + ex.getMessage());
        }

    }//GEN-LAST:event_buttonSendNamesActionPerformed

    private void checkIncludeOriginalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkIncludeOriginalsActionPerformed
        updateLengthLabel(computeNumTotalOutputs());
    }//GEN-LAST:event_checkIncludeOriginalsActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        addInputPanel();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void buttonSendExampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendExampleActionPerformed
        String message = textOSCMessage.getText().trim();
        String hostName = textHost.getText().trim();
        InetAddress address;
        try {
            // try {
            address = InetAddress.getByName(hostName);
            //  } catch 
        } catch (UnknownHostException ex) {
            Util.showPrettyErrorPane(this, "Unknown host " + hostName);
            return;
        }

        String portString = textPort.getText().trim();

        if (!Util.checkIsPositiveNumber(textPort, "Port number", this)) {
            return;
        }
        int port = Integer.parseInt(textPort.getText());
        try {
            boolean v = validateForm();
            if (v) {
                int i = computeNumTotalOutputs();
                w.getOSCSender().sendTestMessage(message, address, port, i);
            }
        } catch (IOException ex) {
            Util.showPrettyErrorPane(this, "Could not send message: " + ex.getMessage());
        }

    }//GEN-LAST:event_buttonSendExampleActionPerformed

    private boolean validateOscSettings() {
        return checkOscMessageValid() && checkOutputHostValid() && checkOutputPortValid();
    }

    private boolean checkOscMessageValid() {
        boolean notBlank = Util.checkNotBlank(textOSCMessage, "OSC message", this);
        if (notBlank) {
            return Util.checkNoSpace(textOSCMessage, "OSC message", this);
        } else {
            return false;
        }
    }

    private boolean checkOutputHostValid() {
        boolean isNotBlank = Util.checkNotBlank(textHost, "host name", this);
        if (!isNotBlank) {
            return false;
        }

        String hostname = textHost.getText().trim();
        try {
            InetAddress address = InetAddress.getByName(hostname);
        } catch (UnknownHostException ex) {
            Util.showPrettyErrorPane(this, "Invalid OSC hostname");
            return false;
        }
        return true;
    }

    private boolean checkOutputPortValid() {
        return Util.checkIsPositiveNumber(textPort, "OSC port", this);
    }

    private boolean validateForm() {
        if (!validateOscSettings()) {
            return false;
        }

        if (outputPanels.size() == 0 && !checkIncludeOriginals.isSelected()) {
            Util.showPrettyErrorPane(this, "You must choose at least one existing or new input value to send out");
            return false;
        }
        for (int i = 0; i < outputPanels.size(); i++) {
            ModifierConfigRow r = outputPanels.get(i);
            boolean b = r.validateForm();
            if (!b) {
                Util.showPrettyErrorPane(r, "Error in new input #" + (i + 1));
                return false;
            }
        }
        String[] names = getAllOutputNames();
        if (!Util.checkAllUniqueWithErrorPane(names, "name")) {
            return false;
        }
        return true;
    }

    private int computeNumTotalOutputs() {
        int s = 0;
        if (checkIncludeOriginals.isSelected()) {
            s = w.getInputManager().getNumInputs();
        }

        for (ModifierConfigRow r : outputPanels) {
            /* ModifiedInput i = r.getModifiedInput();
             if (i != null) {
             s += i.getSize();
             } else {
             logger.log(Level.WARNING, "input row is null unexpectedly");

             } */
            s += r.getDimensionality();
        }
        return s;
    }

    private void addInputPanelForExisting(ModifiedInput o) {
        numNewPanels++;
        JPanel p;
        p = new JPanel();
        p.setBackground(new java.awt.Color(255, 255, 255));
        //p.setPreferredSize(new Dimension(500, 50));
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        UpDownDeleteGUI upDown = new UpDownDeleteGUI(numNewPanels, this);

        p.add(upDown);
        upDownDeletePanels.add(upDown);

        ModifierConfigRow newEditorPanel;
        if (o == null) {
            newEditorPanel = new ModifierConfigRow(w);
        } else {
            newEditorPanel = new ModifierConfigRow(w, o);
        }

        p.add(newEditorPanel);
        outputPanels.add(newEditorPanel);

        newEditorPanel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ModifierConfigRow.PROP_DIMENSIONALITY)) {
                    dimensionalityChanged(numNewPanels, (Integer) evt.getNewValue());
                }
            }
        });

        panelOutputsContainer.add(p);
        outputParentPanels.add(p);

        JSeparator jsep = new JSeparator();
        /*jsep.setBackground(new java.awt.Color(255, 0, 0));
         jsep.setForeground(new java.awt.Color(255, 0, 0));
         jsep.setMaximumSize(new java.awt.Dimension(32767, 5));
         jsep.setPreferredSize(new java.awt.Dimension(50, 1)); */
        panelOutputsContainer.add(jsep);
        separators.add(jsep);

        //repaint and validate etc.
        panelOutputsContainer.revalidate(); //needed to update scrollpane slider
        scrollModifiers.validate();
        JScrollBar vertical = scrollModifiers.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        repaint();

        resetButtonEnabledStates();
        updateLengthLabel(computeNumTotalOutputs());

    }

    private void addInputPanel() {
        addInputPanelForExisting(null);
    }

    private void dimensionalityChanged(final int whichOutput, final Integer newDim) {
        updateLengthLabel(computeNumTotalOutputs());
    }

    private void updateLengthLabel(int len) {
        labelNumValues.setText("Based on the settings below, " + len + " values will be sent in total");
    }

    private void resetButtonEnabledStates() {
        if (numNewPanels == 0) {
            return;
        }
        if (numNewPanels == 1) {
            upDownDeletePanels.get(0).setDeleteEnabled(true);
            upDownDeletePanels.get(0).setUpEnabled(false);
            upDownDeletePanels.get(0).setDownEnabled(false);
        } else {
            upDownDeletePanels.get(0).setUpEnabled(false);
            for (int i = 0; i < upDownDeletePanels.size() - 1; i++) {
                upDownDeletePanels.get(i).setDownEnabled(true);
                upDownDeletePanels.get(i).setDeleteEnabled(true);
            }
            upDownDeletePanels.get(upDownDeletePanels.size() - 1).setDeleteEnabled(true);
            upDownDeletePanels.get(upDownDeletePanels.size() - 1).setDownEnabled(false);

            for (int i = 1; i < upDownDeletePanels.size(); i++) {
                upDownDeletePanels.get(i).setUpEnabled(true);
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttomPanel;
    private javax.swing.JButton buttonSendExample;
    private javax.swing.JButton buttonSendNames;
    private javax.swing.JCheckBox checkIncludeOriginals;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelNumValues;
    private javax.swing.JPanel panelOutputsContainer;
    private javax.swing.JPanel panelTop;
    private javax.swing.JScrollPane scrollModifiers;
    private javax.swing.JTextField textHost;
    private javax.swing.JTextField textOSCMessage;
    private javax.swing.JTextField textPort;
    // End of variables declaration//GEN-END:variables

    @Override
    public void up(int id) {
        swap(id, id - 1);
    }

    @Override
    public void down(int id) {
        swap(id, id + 1);
    }

    @Override
    public void delete(int id) {
        /*if (numNewPanels == 1) {
         return;
         } */

        //Last output? If no, then update everyone's numbering
        if (id != numNewPanels) {
            //Change numbering
            for (int i = id; i < numNewPanels; i++) {
                // outputPanels.get(input).setNum(input); XXX
                upDownDeletePanels.get(i).setID(i);
            }
        }

        numNewPanels--;
        JPanel parentPanel = outputParentPanels.get(id - 1);
        panelOutputsContainer.remove(parentPanel);
        JSeparator jsep = separators.get(id - 1);
        panelOutputsContainer.remove(jsep);

        //Remove from arrays
        outputParentPanels.remove(id - 1);
        separators.remove(id - 1);
        outputPanels.remove(id - 1);
        upDownDeletePanels.remove(id - 1);

        //Repaint
        panelOutputsContainer.revalidate(); //needed to update scrollpane slider
        scrollModifiers.validate();
        JScrollBar vertical = scrollModifiers.getVerticalScrollBar();
        //vertical.setValue( vertical.getMaximum() );
        repaint();

        resetButtonEnabledStates();
        updateLengthLabel(computeNumTotalOutputs());

    }

    private void swap(int id1, int id2) {
        //Tell panels themselves their ids are changing
        //outputPanels.get(id1 - 1).setNum(id2);
        //outputPanels.get(id2 - 1).setNum(id1);

        JPanel parent1 = outputParentPanels.get(id1 - 1);
        JPanel parent2 = outputParentPanels.get(id2 - 1);

        parent1.remove(outputPanels.get(id1 - 1));
        parent2.remove(outputPanels.get(id2 - 1));

        parent2.add(outputPanels.get(id1 - 1));
        parent1.add(outputPanels.get(id2 - 1));

        ModifierConfigRow opTemp = outputPanels.get(id1 - 1);
        outputPanels.set(id1 - 1, outputPanels.get(id2 - 1));
        outputPanels.set(id2 - 1, opTemp);

        repaint();
    }

    private String[] getAllOutputNames() {
        String[] allNames = new String[computeNumTotalOutputs()];
        int currentIndex = 0;
        if (checkIncludeOriginals.isSelected()) {
            String[] inputNames = w.getInputManager().getInputNames();
            System.arraycopy(inputNames, 0, allNames, 0, inputNames.length);
            currentIndex += inputNames.length;
        }
        for (int i = 0; i < outputPanels.size(); i++) {
            ModifierConfigRow r = outputPanels.get(i);
            ModifiedInput in = r.getModifiedInput();
            if (in instanceof ModifiedInputSingle) {
                allNames[currentIndex] = ((ModifiedInputSingle) in).getName();
                currentIndex++;
            } else {
                String[] theseNames = ((ModifiedInputVector) in).getNames();
                System.arraycopy(theseNames, 0, allNames, currentIndex, theseNames.length);
                currentIndex += theseNames.length;
            }
        }
        return allNames;
    }

    public void initializeForInputs() {
        updateLengthLabel(computeNumTotalOutputs());
    }

    public boolean prepareToSave() {
        if (!validateForm()) {
            return false;
        }
        configureOscSenderFromForm();

        configureOutputManagerFromForm();
        return true;
    }

    private void configureOscSenderFromForm() {
        int port = Integer.parseInt(textPort.getText().trim());
        String message = textOSCMessage.getText().trim();
        String host = textHost.getText().trim();

        try {
            w.getOSCSender().setHostnameAndPort(InetAddress.getByName(host), port);
        } catch (UnknownHostException ex) {
            Util.showPrettyErrorPane(this, "Unknown host: " + host);
        } catch (SocketException ex) {
            Util.showPrettyErrorPane(this, "Could not send to " + host + " at port " + port);
        }
        w.getOSCSender().setOscMessage(message);
    }

    public boolean prepareToAdvance() {
        return prepareToSave();
    }

    private void configureOutputManagerFromForm() {
        if (!validateForm()) {
            return;
        }
        List<ModifiedInput> newInputs = new LinkedList<ModifiedInput>();
        boolean includeOriginals = checkIncludeOriginals.isSelected();

        /*if (checkIncludeOriginals.isSelected()) {
         for (int i = 0; i < w.getInputManager().getNumInputs(); i++) {
         ModifiedInput m = new InputCopier(w.getInputManager().getInputNames()[i], i);
         newInputs.add(m);
         }
         } */
        for (ModifierConfigRow p : outputPanels) {
            ModifiedInput m = p.getModifiedInput();
            newInputs.add(m);
        }
        OSCModifiedInputGroup g = new OSCModifiedInputGroup(newInputs);
        w.getOSCSender().setSendInputs(includeOriginals);
        w.getOutputManager().setOutputGroup(g);
    }
}
