/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.Modifiers;

import wekiinputhelper.UsesOnlyOriginalInputs;
import wekiinputhelper.WekiInputHelper;
import wekiinputhelper.gui.InputModifierBuilderPanel;

/**
 *
 * @author rebecca
 */
public class WindowedOperation implements ModifiedInputSingle, UsesOnlyOriginalInputs{
    private final String name;
    private final int index;
    private final int windowSize;
    private final transient double[] history;
    private transient int startPointer;
    private final Operation op;
    
    public static String makeName(String originalName, String shortName, int windowSize) {
        return originalName + "_" + shortName + Integer.toString(windowSize);
    }

    public Operation getOp() {
        return op;
    }

    public int getIndex() {
        return index;
    }
    
    
    public WindowedOperation(String originalName, Operation op, int index, int windowSize) {
        name =  makeName(originalName, op.shortName(), windowSize);       
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
        return op.doOperation(history, startPointer);
    }
    
    public static void main(String[] args) {
        
        Operation avg = new Operation() {

            @Override
            public double doOperation(double[] vals, int startPtr) {
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

    @Override
    public InputModifierBuilderPanel getBuildPanel(WekiInputHelper w) {
        return new WindowedOperationEditor(w, this);
    }
    
    public interface Operation {
        public double doOperation(double[] vals, int startPointer);
        public String shortName();
    }
}
