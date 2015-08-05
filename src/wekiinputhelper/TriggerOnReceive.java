/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

/**
 *
 * @author rebecca
 */
public class TriggerOnReceive extends InputTriggerer {
   // private final Criterion c;
    
    public TriggerOnReceive(Criterion c) {
        this.c = c;
    }
    
    @Override
    public void updateAllValues(double[] vals) {
        if (c.shouldTrigger(vals)) {
            this.triggerSend();
        }
    }

    @Override
    protected void runningStateChanged(RunningManager.RunningState newState) {
    }
    
}
