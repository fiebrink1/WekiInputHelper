/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Date;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import wekiinputhelper.InputTriggerer.Triggerable;
import wekiinputhelper.Modifiers.ModifiedInput;
import wekiinputhelper.osc.OSCModifiedInputGroup;
import wekiinputhelper.util.WeakListenerSupport;
import wekiinputhelper.osc.OSCReceiver;

/**
 *
 * @author rebecca
 */
public class OutputManager {

    private static final Logger logger = Logger.getLogger(OutputManager.class.getName());
    private OSCModifiedInputGroup outputGroup = null;

    private final Triggerable trigger;

    private final WekiInputHelper w;
    private final WeakListenerSupport wls = new WeakListenerSupport();

    //Listeners for output group vectors computed internally
    private final List<OutputManager.OutputValueListener> valueComputedListeners;

    //XXX : look for when input group, output group, trigger change: remove old listeners!!
    //Listeners for individual output edits (e.g. change # classes)
    //private final List<OutputListEditListener> outputListEditListeners;
    //Listeners for single outputs computed internally
    //Listeners for my events
    private final EventListenerList listeners;

    private double[] currentValues = new double[0];
    public static final String PROP_OUTPUTGROUP = "outputGroup";
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public OutputManager(WekiInputHelper w) {

        //Make sure Wekinator initialises this after OSCReceiver 
        this.w = w;

        trigger = new InputTriggerer.Triggerable() {
            @Override
            public void triggerNow() {
                triggerCompute();
            }
        };

        //Listeners for outputGroupChange 
        listeners = new EventListenerList();
        valueComputedListeners = new LinkedList<>();

        addTriggerListener(w.getInputTriggerer());
        w.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(WekiInputHelper.PROP_INPUTTRIGGERER)) {
                    updateNewTriggerer((InputTriggerer) evt.getOldValue(), (InputTriggerer) evt.getNewValue());
                }
            }
        });
    }

    private void triggerCompute() {
        //This only really makes sense if triggers don't rely on modified inputs
        // in which case we'd need to rely on output values being computed as needed previously, and 
        //just send out our current outputs 
        //double[] vals = outputGroup.computeAndGetValuesForNewInputs(w.getInputManager().getInputValues());
        
        //This way, we assume outputs have already been computed:
        double[] vals = outputGroup.getCurrentOutputs();
        double[] inputs = outputGroup.getLastInputs();
        notifyOutputGroupComputedListeners(inputs, vals);
    }

    private void addTriggerListener(InputTriggerer t) {
        if (t != null) {
            t.addSendEventListener(trigger);
        }
    }

    private void removeTriggerListener(InputTriggerer t) {
        if (t != null) {
            t.removeSendEventListener(trigger);
        }
    }

    private void updateNewTriggerer(InputTriggerer oldT, InputTriggerer newT) {
        removeTriggerListener(oldT);
        addTriggerListener(newT);
    }

    /*public void updateOutputGroup(OSCModifiedInputGroup newOutputGroup) {
     OSCModifiedInputGroup oldOutput = outputGroup;
     outputGroup = newOutputGroup;
     notifyOutputEditListeners(outputGroup, oldOutput);
     } */
    public boolean hasValidOutputGroup() {
        return outputGroup != null;
    }

    //For now, no possibility to modify an output group: it's a totally new group.
    public void setOutputGroup(OSCModifiedInputGroup newG) throws IllegalArgumentException {
        OSCModifiedInputGroup oldGroup = outputGroup;
        outputGroup = newG;
        currentValues = new double[newG.getOutputDimensionality()];
        propertyChangeSupport.firePropertyChange(PROP_OUTPUTGROUP, oldGroup, outputGroup);
    }

    public OSCModifiedInputGroup getOutputGroup() {
        return outputGroup;
    }

    /*public double[] getCurrentValues() {
     return currentValues;
     } */
    //Call this when new values available
   /* public void setNewComputedValues(double[] values) {
        // System.out.println("IN SETTING NEW COMPUTED");
        if (values == null || values.length != currentValues.length) {
            throw new IllegalArgumentException("values is null or wrong length");
        }
        System.arraycopy(values, 0, currentValues, 0, values.length);
        notifyOutputGroupComputedListeners(values);

        //In future, may want OSC Sender to listen and respond rather than push
        try {
            w.getOSCSender().sendOutputValuesMessage(values);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Couldn't send message");
            logger.log(Level.SEVERE, null, ex);
        }
    } */

    public String outputsToString() {
        if (outputGroup == null) {
            return "none";
        } else {
            return outputGroup.toString();
        }
    }

    public int getDimensionality() {
        if (outputGroup != null) {
            return outputGroup.getOutputDimensionality();
        } else {
            return 0;
        }
    }

    public String[] getOutputNames() {
        return outputGroup.getOutputNames();
    }

    //Notifies listeners that we've got an internally-computed output vector
    private void notifyOutputGroupComputedListeners(double[] intputs, double[] data) {
        for (OutputManager.OutputValueListener l : valueComputedListeners) {
            l.update(intputs, data); //May have to include group name/ID here (problematic if it changes later)
        }
    }

    //Only for output values computed internally
    public void addOutputGroupComputedListener(OutputManager.OutputValueListener l) {
        valueComputedListeners.add(l);
    }

    //Only for output values computed internally
    public boolean removeOutputGroupComputedListener(OutputManager.OutputValueListener l) {
        return valueComputedListeners.remove(l);
    }


    /* private void notifyOutputEditListeners(OSCModifiedInputGroup newOutput, OSCModifiedInputGroup oldOutput) {
     for (OutputListEditListener l : outputListEditListeners) {
     l.outputListChanged(newOutput, oldOutput);
     }
     } */
    // TODO: Not sure if we need this?
    public interface OutputValueListener extends EventListener {

        public void update(double[] inputs, double[] vals);
    }

    /* public interface OutputListEditListener {
     public void outputListChanged(OSCModifiedInputGroup newGroup, OSCModifiedInputGroup oldGroup);   
     } */
}
