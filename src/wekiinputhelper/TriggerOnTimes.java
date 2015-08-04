/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



/**
 *
 * @author rebecca
 */
public class TriggerOnTimes extends InputTriggerer {
    private int numMs = 10;
    private final Criterion c;
    private boolean isRunning = false;
    private double[] lastInputs;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture scheduledFuture;
    
    public TriggerOnTimes(int ms, Criterion c) {
        this.numMs = ms;
        this.c = c;
    }
    
    @Override
    public void updateAllValues(double[] vals) {
        lastInputs = vals;
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
                if (c.shouldTrigger(lastInputs)) {
                    triggerSend();
                }
            }
        }, numMs, numMs, TimeUnit.MILLISECONDS);
    }
    
    private void stopTimer() {
        scheduledFuture.cancel(true);
    }
}
