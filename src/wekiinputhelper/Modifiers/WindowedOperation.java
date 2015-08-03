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
public class WindowedOperation implements ModifiedInput, UsesOnlyOriginalInputs{
    String name;
    int index;
    int windowSize;
    double[] history;
    int startPointer;
    Operation op;
    
    public WindowedOperation(String originalName, Operation op, int index, int windowSize) {
        name = originalName + "_" + op.shortName() + Integer.toString(windowSize);        
        this.index = index;
        this.windowSize = windowSize;
        this.op = op;
        history = new double[windowSize];
        startPointer = 0;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        history[startPointer] = inputs[index];
        startPointer++;
        if (startPointer == windowSize) {
            startPointer = 0;
        }
    }

    @Override
    public int getSize() {
        return 1;
    }
    
    public int getWindowSize() {
        return windowSize;
    }

    @Override
    public double getValue() {
        return op.doOperation(history);
    }
    
    public static void main(String[] args) {
        
        Operation avg = new Operation() {

            @Override
            public double doOperation(double[] vals) {
                double sum = 0;
                for (int i = 0; i < vals.length; i++) {
                    sum += vals[i];
                }
                return sum / vals.length;
            }

            @Override
            public String shortName() {
                return "Avg";
            }
        };
        WindowedOperation bi = new WindowedOperation("feat1", avg, 0, 3);
   
        for (int i = 0; i < 10; i++) {
            System.out.print(i + ": ");
            bi.updateForInputs(new double[] {i});
            double d = bi.getValue();
            System.out.println(d);
        }
    }
    
    public interface Operation {
        public double doOperation(double[] vals);
        public String shortName();
    }
}
