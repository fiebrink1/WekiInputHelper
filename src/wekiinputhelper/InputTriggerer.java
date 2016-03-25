/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rebecca
 */
public abstract class InputTriggerer {
    public abstract void updateAllValues(double[] inputs, double[] outputs);
    protected Criterion c;
    protected PropertyChangeListener runningListener = null;
    
    public Criterion getCriterion() {
        return c;
    }

    
    public void attachRunningListener(WekiInputHelper w) {
        runningListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(RunningManager.PROP_RUNNINGSTATE)) {
                    runningStateChanged((RunningManager.RunningState)(evt.getNewValue()));
                }
            }
        };
        w.getRunningManager().addPropertyChangeListener(runningListener);
    }
    
    protected abstract void runningStateChanged(RunningManager.RunningState newState);
    
    protected transient List<Triggerable> listenerList = new LinkedList<>();
    
    public void addSendEventListener(Triggerable t) {
        listenerList.add(t);
    }

    public void removeSendEventListener(Triggerable t) {
        listenerList.remove(t);
    }

    protected void triggerSend() {
        for (Triggerable t : listenerList) { //empty...
            t.triggerNow();
        }
    }

    void removeListeners(WekiInputHelper w) {
        listenerList.clear();
        w.getRunningManager().removePropertyChangeListener(runningListener);
    }
   
    public interface Triggerable {
        public void triggerNow();
    }
}
