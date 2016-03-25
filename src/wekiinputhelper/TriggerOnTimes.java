/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



/**
 *
 * @author rebecca
 */
public class TriggerOnTimes extends InputTriggerer {
    private final int numMs;
   // private final Criterion c;
    private transient boolean isRunning = false;
    private transient double[] lastInputs;
    private transient double[] lastOutputs;

    private final transient ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private transient ScheduledFuture scheduledFuture;
    
    public TriggerOnTimes(WekiInputHelper w, int ms, Criterion c) {
        this.numMs = ms;
        this.c = c;
        attachRunningListener(w); //in order to start & stop timer according to whether running
    }
    
    public int getTime() {
        return numMs;
    }
    
    @Override
    public void updateAllValues(double[] inputs, double[] outputs) {
        lastInputs = inputs;
        lastOutputs = outputs;
    }

    @Override
    protected void runningStateChanged(RunningManager.RunningState newState) {
        if (newState == RunningManager.RunningState.RUNNING) {
            startTimer();
        } else {
            stopTimer();
        }
    }    
    
    private void startTimer() {
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (c.shouldTrigger(lastInputs, lastOutputs)) {
                    triggerSend();
                }
            }
        }, numMs, numMs, TimeUnit.MILLISECONDS);
        isRunning = true;
    }
    
    private void stopTimer() {
        if (isRunning) {
            scheduledFuture.cancel(true);
        } 
        isRunning = false;
    }
    
    private Object readResolve() {
        listenerList = new LinkedList<>();
        return this;
    }
}
