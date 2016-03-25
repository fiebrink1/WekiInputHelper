/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.util.LinkedList;

/**
 *
 * @author rebecca
 */
public class TriggerOnReceive extends InputTriggerer {
    
    public TriggerOnReceive(Criterion c) {
        this.c = c;
    }
    
    @Override
    public void updateAllValues(double[] inputs, double[] outputs) {
        if (c.shouldTrigger(inputs, outputs)) {
            this.triggerSend();
        }
    }

    @Override
    protected void runningStateChanged(RunningManager.RunningState newState) {
    }
    
    private Object readResolve() {
        listenerList = new LinkedList<>();
        return this;
    }
    
}
