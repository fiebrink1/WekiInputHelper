/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.gui;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicComboBoxUI;
import wekiinputhelper.InputManager;
import wekiinputhelper.OutputManager;
import wekiinputhelper.RunningManager;
import wekiinputhelper.WekiInputHelper;
import wekiinputhelper.osc.OSCMonitor;
import wekiinputhelper.osc.OSCReceiver;
import wekiinputhelper.osc.OSCSender;

/**
 *
 * @author rebecca
 */
public class SendAndMonitorPanel extends javax.swing.JPanel {

    private WekiInputHelper w;
    private static final Logger logger = Logger.getLogger(SendAndMonitorPanel.class.getName());
    private final ImageIcon onIcon = new ImageIcon(getClass().getResource("/wekiinputhelper/icons/green3.png")); // NOI18N
    private final ImageIcon offIcon = new ImageIcon(getClass().getResource("/wekiinputhelper/icons/yellow2.png")); // NOI18N
    private final ImageIcon problemIcon = new ImageIcon(getClass().getResource("/wekiinputhelper/icons/redx1.png")); // NOI18N
    private final ImageIcon problemIcon2 = new ImageIcon(getClass().getResource("/wekiinputhelper/icons/red1.png")); // NOI18N
    private final List<NameValueRow> inputRows = new ArrayList<>();
    private final List<NameValueRow> outputRows = new ArrayList<>();
    private final InputManager.InputListener inputValueListener;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture scheduledFuture;

    /**
     *
     * Creates new form AddInputsPanel
     */
    public SendAndMonitorPanel() {
        initComponents();
        inputValueListener = new InputManager.InputListener() {

            @Override
            public void update(double[] vals) {
                newInputsReceived(vals);
            }

            @Override
            public void notifyInputError() {
            }
        };

        scheduledFuture = scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    setOutIcon(false);
                }
            }, 100, TimeUnit.MILLISECONDS);
    }

    private void newInputsReceived(double[] vals) {
        if (vals.length <= inputRows.size()) {
            for (int i = 0; i < vals.length; i++) {
                inputRows.get(i).setValue(vals[i]);
            }
        }
    }

    private void newOutputsSent(double[] inputs, double[] vals) {
        int index = 0;
        if (w.getOSCSender().isSendInputs()) {
            for (int i = 0; i < inputs.length; i++) {
                outputRows.get(index).setValue(inputs[i]);
                index++;
            }
        }

        for (int i = 0; i < vals.length; i++) {
            outputRows.get(index).setValue(vals[i]);
            index++;
        }
        setOutIcon(true);
    }

    public boolean prepareToAdvance() {
        w.getRunningManager().stop();
        return true;
    }

    public void prepareToSave() {

    }

    public void setWekiInputHelper(WekiInputHelper w) {
        this.w = w;
        updateFormForWeki();
    }

    private void oscMonitorChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == OSCMonitor.PROP_RECEIVE_STATE) {
            setInIcon((OSCMonitor.OSCReceiveState) evt.getNewValue());

        }
    }

    /*private void newOutputsSent() {
     setOutIcon(true);
     } */
    //XXX call this when send happens, release it when not sending
    private void setOutIcon(boolean isSending) {
        if (isSending) {
            scheduledFuture.cancel(true);
            indicatorOscOut1.setIcon(onIcon);
            //indicatorOscOut1.setToolTipText("Sending outputs");
            scheduledFuture = scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    setOutIcon(false);
                }
            }, 100, TimeUnit.MILLISECONDS);

        } else {
            indicatorOscOut1.setIcon(offIcon);
            //indicatorOscOut1.setToolTipText("Not sending outputs");
        }
    }

    private void setInIcon(OSCMonitor.OSCReceiveState rstate) {
        if (rstate == OSCMonitor.OSCReceiveState.NOT_CONNECTED) {
            indicatorOscIn.setIcon(problemIcon);
            indicatorOscIn.setToolTipText("OSC receiver is not listening");
        } else if (rstate == OSCMonitor.OSCReceiveState.CONNECTED_NODATA) {
            indicatorOscIn.setIcon(offIcon);
            indicatorOscIn.setToolTipText("Listening, but no data arriving");
        } else if (rstate == OSCMonitor.OSCReceiveState.RECEIVING) {
            indicatorOscIn.setIcon(onIcon);
            indicatorOscIn.setToolTipText("Receiving inputs");
        } else {
            //There's a problem!
            indicatorOscIn.setIcon(problemIcon2);
            indicatorOscIn.setToolTipText("Wrong number of inputs received");
        }
    }

    private void updateFormForWeki() {
        updateInputInformation();
        setupInputs();
        setupButtons();

        //Set up output panels
        if (w.getOutputManager().hasValidOutputGroup()) {
            setupOutputs();
        }

        //Input value listener:
        w.getInputManager().addInputValueListener(inputValueListener);

        //Input group change listener
        w.getInputManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(InputManager.PROP_INPUTGROUP)) {
                    inputGroupChanged();
                }
            }
        });

        //Output send listener:
        w.getOutputManager().addOutputGroupComputedListener(new OutputManager.OutputValueListener() {
            @Override
            public void update(double[] inputs, double[] vals) {
                newOutputsSent(inputs, vals);
            }
        });

        //Sender: listen if inputs should be/not be included
        w.getOSCSender().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(OSCSender.PROP_SENDINPUTS)) {
                    sendInputsChanged();
                }
            }
        });

        //Running state listener:
        w.getRunningManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                runningStateChanged(evt);
            }
        });

        //Output change listener:
        w.getOutputManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(OutputManager.PROP_OUTPUTGROUP)) {
                    outputGroupChanged();
                }
            }
        });

        //Add listeners: OSC in, send out, outputManager change    
        w.getOSCReceiver().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(OSCReceiver.PROP_CONNECTIONSTATE)) {
                    oscReceiverChanged();
                }
            }
        });

        w.getOSCMonitor().startMonitoring();
        w.getOSCMonitor().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                oscMonitorChanged(evt);
            }
        });
        setInIcon(w.getOSCMonitor().getReceiveState());
        // setOutIcon(w.getOSCMonitor().isSending());
    }

    private void oscReceiverChanged() {
        updateListeningButton();
    }

    private void setupButtons() {
        updateListeningButton();
        updateRunningButton();
    }

    private void sendInputsChanged() {
        setupOutputs();
        updateOutputInformation();
    }

    private void updateListeningButton() {
        if (w.getOSCReceiver().getConnectionState() == OSCReceiver.ConnectionState.CONNECTED) {
            buttonListen.setText("Stop listening");
        } else {
            buttonListen.setText("Start listening");
        }
    }

    private void runningStateChanged(PropertyChangeEvent evt) {
        updateRunningButton();
    }

    private void updateRunningButton() {
        if (w.getRunningManager().getRunningState() == RunningManager.RunningState.RUNNING) {
            buttonSend.setText("Stop sending");
        } else {
            buttonSend.setText("Start sending");
        }
        if (w.getRunningManager().isAbleToRun()) {
            buttonSend.setEnabled(true);
        } else {
            buttonSend.setEnabled(false);
        }
    }

    private void setupInputs() {
        //Add panels
        int numInputs = w.getInputManager().getNumInputs();
        inputRowsParent.removeAll();
        inputRows.clear();
        double[] inputValues = w.getInputManager().getInputValues();
        for (int i = 0; i < numInputs; i++) {
            NameValueRow r = new NameValueRow((i + 1) + ". " + w.getInputManager().getInputNames()[i]);
            inputRows.add(r);
            inputRowsParent.add(r);
            r.setValue(inputValues[i]);
        }
        inputRowsParent.setPreferredSize(new Dimension(270, 25 * numInputs));
        inputRowsParent.revalidate(); //needed to update scrollpane slider
        scrollInputs.validate();
    }

    private void setupOutputs() {
        outputRowsParent.removeAll();
        outputRows.clear();
        int index = 0;
        int numRows = w.getOutputManager().getDimensionality();

        if (w.getOSCSender().isSendInputs()) {
            int numInputs = w.getInputManager().getNumInputs();
            numRows += numInputs;
            double[] inputValues = w.getInputManager().getInputValues();
            for (int i = 0; i < numInputs; i++) {
                NameValueRow r = new NameValueRow((index + 1) + ". " + w.getInputManager().getInputNames()[i]);
                index++;
                outputRows.add(r);
                outputRowsParent.add(r);
                if (inputValues != null && i < inputValues.length) {
                    r.setValue(inputValues[i]);
                } else {
                    r.setValue(0);
                }
            }
        }

        int numOutputs = w.getOutputManager().getDimensionality();
        double[] outputValues = w.getOutputManager().getOutputGroup().getCurrentOutputs();
        for (int i = 0; i < numOutputs; i++) {
            NameValueRow r = new NameValueRow((index + 1) + ". " + w.getOutputManager().getOutputNames()[i]);
            index++;
            outputRows.add(r);
            outputRowsParent.add(r);
            if (outputValues != null && i < outputValues.length) {
                r.setValue(outputValues[i]);
            } else {
                r.setValue(0);
            }
        }
        outputRowsParent.setPreferredSize(new Dimension(270, 25 * numRows));
        outputRowsParent.revalidate(); //needed to update scrollpane slider
        scrollOutputs.validate();

        repaint();
    }

    private void outputGroupChanged() {
        updateOutputInformation();
        setupOutputs();
    }

    private void inputGroupChanged() {
        updateInputInformation();
        setupInputs();
    }

    private void updateOutputInformation() {
        int numOutputs = w.getOutputManager().getDimensionality();
        if (w.getOSCSender().isSendInputs()) {
            numOutputs += w.getInputManager().getNumInputs();
        }
        int outputPort = w.getOSCSender().getPort();
        String hostname = w.getOSCSender().getHostname().getHostName();
        String msg = w.getOSCSender().getOscMessage();

        labelNumOutputs.setText(numOutputs + " values");
        labelOutputHostPort.setText("Sending to " + hostname + " at port " + outputPort);
        labelOutputMessage.setText("Message: " + msg);

    }

    private void updateInputInformation() {
        int numInputs = w.getInputManager().getNumInputs();
        if (!w.getInputManager().hasValidInputs()) {
            return;
        }
        String inputMessage = w.getInputManager().getOSCInputGroup().getOscMessage();
        int inPort = w.getOSCReceiver().getReceivePort();

        labelNumInputs.setText(numInputs + " inputs");
        labelInputPort.setText("Listening on port " + inPort);
        labelInputMessage.setText("Message " + inputMessage);
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
        jPanel1 = new javax.swing.JPanel();
        inputParent = new javax.swing.JPanel();
        labelNumInputs = new javax.swing.JLabel();
        buttonListen = new javax.swing.JButton();
        labelInputMessage = new javax.swing.JLabel();
        labelInputPort = new javax.swing.JLabel();
        scrollInputs = new javax.swing.JScrollPane();
        inputRowsParent = new javax.swing.JPanel();
        nameValueRow1 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow2 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow3 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow4 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow5 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow6 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow7 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow8 = new wekiinputhelper.gui.NameValueRow();
        jSeparator1 = new javax.swing.JSeparator();
        indicatorOscIn = new javax.swing.JLabel();
        outputParent = new javax.swing.JPanel();
        labelNumOutputs = new javax.swing.JLabel();
        labelOutputHostPort = new javax.swing.JLabel();
        labelOutputMessage = new javax.swing.JLabel();
        buttonSend = new javax.swing.JButton();
        scrollOutputs = new javax.swing.JScrollPane();
        outputRowsParent = new javax.swing.JPanel();
        nameValueRow9 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow10 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow11 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow12 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow13 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow14 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow15 = new wekiinputhelper.gui.NameValueRow();
        nameValueRow16 = new wekiinputhelper.gui.NameValueRow();
        indicatorOscOut1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        inputParent.setBackground(new java.awt.Color(255, 255, 255));

        labelNumInputs.setText("5 values");

        buttonListen.setText("Stop listening");
        buttonListen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonListenActionPerformed(evt);
            }
        });

        labelInputMessage.setText("Message /abc/1234");

        labelInputPort.setText("Listening on port 6448");

        scrollInputs.setPreferredSize(new java.awt.Dimension(260, 205));

        inputRowsParent.setBackground(new java.awt.Color(255, 255, 255));
        inputRowsParent.setPreferredSize(new java.awt.Dimension(255, 200));
        inputRowsParent.setLayout(new javax.swing.BoxLayout(inputRowsParent, javax.swing.BoxLayout.Y_AXIS));
        inputRowsParent.add(nameValueRow1);
        inputRowsParent.add(nameValueRow2);
        inputRowsParent.add(nameValueRow3);
        inputRowsParent.add(nameValueRow4);
        inputRowsParent.add(nameValueRow5);
        inputRowsParent.add(nameValueRow6);
        inputRowsParent.add(nameValueRow7);
        inputRowsParent.add(nameValueRow8);

        scrollInputs.setViewportView(inputRowsParent);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setMinimumSize(new java.awt.Dimension(10, 10));
        jSeparator1.setPreferredSize(new java.awt.Dimension(10, 10));
        jSeparator1.setSize(new java.awt.Dimension(10, 10));

        indicatorOscIn.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        indicatorOscIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wekiinputhelper/icons/green3.png"))); // NOI18N
        indicatorOscIn.setText("OSC In");
        indicatorOscIn.setToolTipText("Receiver is not listening");
        indicatorOscIn.setPreferredSize(new java.awt.Dimension(45, 28));
        indicatorOscIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                indicatorOscInMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout inputParentLayout = new javax.swing.GroupLayout(inputParent);
        inputParent.setLayout(inputParentLayout);
        inputParentLayout.setHorizontalGroup(
            inputParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputParentLayout.createSequentialGroup()
                .addComponent(buttonListen, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(inputParentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollInputs, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                    .addGroup(inputParentLayout.createSequentialGroup()
                        .addComponent(indicatorOscIn, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labelNumInputs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelInputMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelInputPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        inputParentLayout.setVerticalGroup(
            inputParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputParentLayout.createSequentialGroup()
                .addGroup(inputParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputParentLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(inputParentLayout.createSequentialGroup()
                        .addComponent(indicatorOscIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelNumInputs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelInputMessage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelInputPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonListen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollInputs, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1.add(inputParent);

        outputParent.setBackground(new java.awt.Color(255, 255, 255));

        labelNumOutputs.setText("17 values");

        labelOutputHostPort.setText("Sending to localhost on port 6449");

        labelOutputMessage.setText("Message /osc/outputs/lalalalala");

        buttonSend.setText("Start sending");
        buttonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendActionPerformed(evt);
            }
        });

        outputRowsParent.setBackground(new java.awt.Color(255, 255, 255));
        outputRowsParent.setPreferredSize(new java.awt.Dimension(255, 200));
        outputRowsParent.setLayout(new javax.swing.BoxLayout(outputRowsParent, javax.swing.BoxLayout.Y_AXIS));
        outputRowsParent.add(nameValueRow9);
        outputRowsParent.add(nameValueRow10);
        outputRowsParent.add(nameValueRow11);
        outputRowsParent.add(nameValueRow12);
        outputRowsParent.add(nameValueRow13);
        outputRowsParent.add(nameValueRow14);
        outputRowsParent.add(nameValueRow15);
        outputRowsParent.add(nameValueRow16);

        scrollOutputs.setViewportView(outputRowsParent);

        indicatorOscOut1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        indicatorOscOut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wekiinputhelper/icons/green3.png"))); // NOI18N
        indicatorOscOut1.setText("OSC Out");
        indicatorOscOut1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                indicatorOscOut1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout outputParentLayout = new javax.swing.GroupLayout(outputParent);
        outputParent.setLayout(outputParentLayout);
        outputParentLayout.setHorizontalGroup(
            outputParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputParentLayout.createSequentialGroup()
                .addComponent(buttonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(outputParentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollOutputs, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addGroup(outputParentLayout.createSequentialGroup()
                        .addComponent(indicatorOscOut1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labelNumOutputs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelOutputMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelOutputHostPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        outputParentLayout.setVerticalGroup(
            outputParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputParentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(indicatorOscOut1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNumOutputs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelOutputMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelOutputHostPort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollOutputs, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(outputParent);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void indicatorOscInMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_indicatorOscInMouseClicked
        // w.getMainHelperGUI().showOSCReceiverWindow();
    }//GEN-LAST:event_indicatorOscInMouseClicked

    private void indicatorOscOut1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_indicatorOscOut1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_indicatorOscOut1MouseClicked

    private void buttonListenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonListenActionPerformed
        if (w.getOSCReceiver().getConnectionState() == OSCReceiver.ConnectionState.CONNECTED) {
            w.getOSCReceiver().stopListening();
        } else {
            w.getOSCReceiver().startListening();
        }
    }//GEN-LAST:event_buttonListenActionPerformed

    private void buttonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendActionPerformed
        if (w.getRunningManager().getRunningState() == RunningManager.RunningState.RUNNING) {
            w.getRunningManager().setRunningState(RunningManager.RunningState.NOT_RUNNING);
        } else {
            if (w.getRunningManager().isAbleToRun()) {
                w.getRunningManager().setRunningState(RunningManager.RunningState.RUNNING);
            }
        }
    }//GEN-LAST:event_buttonSendActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSendWhen;
    private javax.swing.ButtonGroup buttonGroupTriggerStop;
    private javax.swing.JButton buttonListen;
    private javax.swing.JButton buttonSend;
    private javax.swing.JLabel indicatorOscIn;
    private javax.swing.JLabel indicatorOscOut1;
    private javax.swing.JPanel inputParent;
    private javax.swing.JPanel inputRowsParent;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelInputMessage;
    private javax.swing.JLabel labelInputPort;
    private javax.swing.JLabel labelNumInputs;
    private javax.swing.JLabel labelNumOutputs;
    private javax.swing.JLabel labelOutputHostPort;
    private javax.swing.JLabel labelOutputMessage;
    private wekiinputhelper.gui.NameValueRow nameValueRow1;
    private wekiinputhelper.gui.NameValueRow nameValueRow10;
    private wekiinputhelper.gui.NameValueRow nameValueRow11;
    private wekiinputhelper.gui.NameValueRow nameValueRow12;
    private wekiinputhelper.gui.NameValueRow nameValueRow13;
    private wekiinputhelper.gui.NameValueRow nameValueRow14;
    private wekiinputhelper.gui.NameValueRow nameValueRow15;
    private wekiinputhelper.gui.NameValueRow nameValueRow16;
    private wekiinputhelper.gui.NameValueRow nameValueRow2;
    private wekiinputhelper.gui.NameValueRow nameValueRow3;
    private wekiinputhelper.gui.NameValueRow nameValueRow4;
    private wekiinputhelper.gui.NameValueRow nameValueRow5;
    private wekiinputhelper.gui.NameValueRow nameValueRow6;
    private wekiinputhelper.gui.NameValueRow nameValueRow7;
    private wekiinputhelper.gui.NameValueRow nameValueRow8;
    private wekiinputhelper.gui.NameValueRow nameValueRow9;
    private javax.swing.JPanel outputParent;
    private javax.swing.JPanel outputRowsParent;
    private javax.swing.JScrollPane scrollInputs;
    private javax.swing.JScrollPane scrollOutputs;
    // End of variables declaration//GEN-END:variables

}
