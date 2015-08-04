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
public class TriggerOnNth extends InputTriggerer {
    private int currentCount = 0;
    private int n = 1;
    private final Criterion c;

    
    public TriggerOnNth(int n, Criterion c) {
        this.n =n;
        this.c = c;
    }
    
    @Override
    public void updateAllValues(double[] vals) {
        currentCount++;
        if (currentCount == n) {
            if (c.shouldTrigger(vals)) {
                this.triggerSend();  
            }
            currentCount = 0;
        }
    }

    @Override
    protected void runningStateChanged(RunningManager.RunningState newState) {
    }
    
}
