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
public class TriggerOnNth extends InputTriggerer {
    private transient int currentCount = 0;
    private final int n;

    
    public TriggerOnNth(int n, Criterion c) {
        this.n =n;
        this.c = c;
    }
    
    public int getN() {
        return n;
    }
    
    @Override
    public void updateAllValues(double[] inputs, double[] outputs) {
        currentCount++;
        if (currentCount == n) {
            if (c.shouldTrigger(inputs, outputs)) {
                this.triggerSend();  
            }
            currentCount = 0;
        }
    }

    @Override
    protected void runningStateChanged(RunningManager.RunningState newState) {
    }
    
    private Object readResolve() {
        System.out.println("HIIII");
        listenerList = new LinkedList<>();
        return this;
    }
    
}
