/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.Modifiers;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author rebecca
 */
public class CircularBuffer {
    private final int length;
    private transient double[] vals;
    private transient int currentIndex = 0;
    
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
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        vals = new double[length];
        currentIndex = 0;
    }
   
}
