/*
 * Executes basic control over learning actions
 */
package wekiinputhelper;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author rebecca
 */
public class WekiInputHelperController {
    private final WekiInputHelper w;
    //private final OSCController oscController;
    private final List<NamesListener> inputNamesListeners;
    //private final List<InputOutputConnectionsListener> inputOutputConnectionsListeners;
    
    public WekiInputHelperController(WekiInputHelper w) {
        this.w = w;
        inputNamesListeners = new LinkedList<NamesListener>();
    }
    
    public boolean isRunning() {
        return (w.getRunningManager().getRunningState() == RunningManager.RunningState.RUNNING);
    }

    public void startRun() {
        if (w.getRunningManager().getRunningState() == RunningManager.RunningState.NOT_RUNNING) {
           w.getRunningManager().setRunningState(RunningManager.RunningState.RUNNING);
           w.getStatusUpdateCenter().update(this, "Running - waiting for inputs to arrive");
        }
    }

    public void stopRun() {
        w.getRunningManager().setRunningState(RunningManager.RunningState.NOT_RUNNING);
        w.getStatusUpdateCenter().update(this, "Running stopped");
    }
 
    public boolean canRun() {
        return w.getRunningManager().isAbleToRun();
    }  

    //Requires either no input group set up yet, or length matches # inputs
    //Listeners can register in case wekinator isn't set up yet (e.g. initial project config screen)
    public void setInputNames(String[] inputNames) {
        if (w.getInputManager().hasValidInputs()) {
            //w.getInputManager().getOSCInputGroup().setInputNames(inputNames);
            //TODO: Fix this, with attention to how Path, LearningManager use input names.
            w.getStatusUpdateCenter().warn(this, "Input names cannot be changed after project is created (will be implemented in later version)");
        } else {
            notifyNewInputNames(inputNames);
        }
    }
    
    public void addInputNamesListener(NamesListener l) {
        inputNamesListeners.add(l);
    }

    public boolean removeInputNamesListener(NamesListener l) {
        return inputNamesListeners.remove(l);
    }

    private void notifyNewInputNames(String[] newNames) {
        for (NamesListener nl: inputNamesListeners) {
            nl.newNamesReceived(newNames);
        }
    }
    
    public interface NamesListener {
        public void newNamesReceived(String[] names);
    } 
}
