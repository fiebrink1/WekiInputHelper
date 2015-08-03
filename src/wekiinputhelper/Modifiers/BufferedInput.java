/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.Modifiers;

import wekiinputhelper.UsesOnlyOriginalInputs;

/**
 *
 * @author rebecca
 */
public class BufferedInput implements ModifiedInputVector, UsesOnlyOriginalInputs{
    String[] names;
    int index;
    int bufferSize;
    double[] history;
    int startPointer;
    double[] returnValues;
    
    public BufferedInput(String originalName, int index, int bufferSize) {
        names = new String[bufferSize];
        names[bufferSize-1] = originalName + "[n]";
        for (int i = 2; i <= bufferSize; i++) {
            names[bufferSize - i] = originalName + "[n-" + (i-1) + "]";
        }
        
        this.index = index;
        this.bufferSize = bufferSize;
        history = new double[bufferSize];
        returnValues = new double[bufferSize];
        startPointer = 0;
    }
    
    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        history[startPointer] = inputs[index];
        startPointer++;
        if (startPointer == bufferSize) {
            startPointer = 0;
        }
    }

    @Override
    public int getSize() {
        return bufferSize;
    }

    @Override
    public double[] getValues() {
        System.arraycopy(history, startPointer, returnValues, 0, history.length - startPointer);
        System.arraycopy(history, 0, returnValues, history.length - startPointer, startPointer);
        return returnValues;
    }
    
    public static void main(String[] args) {
        BufferedInput bi = new BufferedInput("feat1", 0, 3);
        String[] s = bi.getNames();
        for (int i= 0; i < s.length; i++) {
            System.out.print(s[i] + ",");
        }
        System.out.println("");
                
        for (int i = 0; i < 10; i++) {
            System.out.print(i + ": ");
            bi.updateForInputs(new double[] {i});
            double[] d = bi.getValues();
            for (int j = 0; j < bi.getSize(); j++) {
                System.out.print(d[j] + " ");
            }
            System.out.println("");
        }
    }
}
