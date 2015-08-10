/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.Modifiers;

/**
 *
 * @author rebecca
 */
public class CircularBuffer {
    private final int length;
    private final double[] vals;
    private int currentIndex = 0;
    
    public CircularBuffer(int len) {
        length = len;
        vals = new double[len];
    }
    
    public void addNext(double d) {
        if (++currentIndex == length) {
            currentIndex = 0;
        }
        vals[currentIndex] = d;
    }
    
    public double getDelayedSample(int delay) {
        int delayIndex = currentIndex - delay;
        if (delayIndex < 0) {
            delayIndex += length;
        }
        return vals[delayIndex];
    }
   
}
