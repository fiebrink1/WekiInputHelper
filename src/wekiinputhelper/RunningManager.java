/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;
import wekiinputhelper.osc.OSCReceiver;
import wekiinputhelper.util.WeakListenerSupport;

/**
 *
 * @author rebecca
 */
public class RunningManager {

    public void setIncludeOriginals(boolean includeOriginals) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static enum RunningState {

        RUNNING, NOT_RUNNING
    };

    private boolean ableToRun = false;

    private final WekiInputHelper w;
    private final WeakListenerSupport wls = new WeakListenerSupport();

    private RunningState runningState = RunningState.NOT_RUNNING;
    public static final String PROP_RUNNINGSTATE = "runningState";

    private static final Logger logger = Logger.getLogger(RunningManager.class.getName());

    public static final String PROP_ABLE_TO_RUN = "ableToRun";

    /**
     * Get the value of runningState
     *
     * @return the value of runningState
     */
    public RunningState getRunningState() {
        return runningState;
    }

    /**
     * Set the value of runningState
     *
     * @param runningState new value of runningState
     */
    public void setRunningState(RunningState runningState) {
        RunningState oldRunningState = this.runningState;
        this.runningState = runningState;
        propertyChangeSupport.firePropertyChange(PROP_RUNNINGSTATE, oldRunningState, runningState);
    }

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

    public RunningManager(WekiInputHelper w) {
        this.w = w;

        w.getInputManager().addInputValueListener(new InputManager.InputListener() {

            @Override
            public void update(double[] vals) {
                newInputsReceived(vals);
            }

            @Override
            public void notifyInputError() {
            }
        });

        w.getOSCReceiver().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                oscReceiverPropertyChanged(evt);
            }
        });

    }

    private void oscReceiverPropertyChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == OSCReceiver.PROP_CONNECTIONSTATE) {
            updateAbleToRun();
        }
    }

    private void updateAbleToRun() {
        //Requires models in runnable state (at least some)
        if (w.getOSCReceiver().getConnectionState() != OSCReceiver.ConnectionState.CONNECTED) {
            setAbleToRun(false);
            return;
        }
        setAbleToRun(true);
    }

    /*private double[] computeValues(double[] inputs) {
     //TODO: Compute XXX
     return new double[0];
     } */
    public void newInputsReceived(double[] inputs) {
        if (runningState == RunningState.RUNNING) {
            double[] d = w.getOutputManager().getOutputGroup().computeAndGetValuesForNewInputs(inputs);
            w.getInputTriggerer().updateAllValues(inputs, d);
        }
    }

    public void stop() {
        if (getRunningState() == RunningState.RUNNING) {
            setRunningState(RunningState.NOT_RUNNING);
        }
    }

    public void start() {
        if (getRunningState() == RunningState.NOT_RUNNING) {
            setRunningState(RunningState.RUNNING);
        }
    }

    /**
     * Get the value of ableToRun
     *
     * @return the value of ableToRun
     */
    public boolean isAbleToRun() {
        return ableToRun;
    }

    /**
     * Set the value of ableToRun
     *
     * @param ableToRun new value of ableToRun
     */
    public void setAbleToRun(boolean ableToRun) {
        boolean oldAbleToRun = this.ableToRun;
        this.ableToRun = ableToRun;
        propertyChangeSupport.firePropertyChange(PROP_ABLE_TO_RUN, oldAbleToRun, ableToRun);
    }
}
