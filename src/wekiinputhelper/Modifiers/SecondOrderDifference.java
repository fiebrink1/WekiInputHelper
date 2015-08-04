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
public class SecondOrderDifference implements ModifiedInputSingle, UsesOnlyOriginalInputs{
    private final String name;
    private final int index;
    private transient double x_n2 = 0;
    private transient double x_n1 = 0;
    private transient double x_n = 0;
    private transient double y_n = 0;
    
    public static String makeName(String originalName) {
        return originalName + "_2ndOrderDiff";
    }
    
    public SecondOrderDifference(String originalName, int index) {
        this.name = makeName(originalName);
        this.index = index;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        x_n2 = x_n1;
        x_n1 = x_n;
        x_n = inputs[index];
        y_n = x_n - 2 * x_n1 + x_n2; 
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public double getValue() {
       return y_n;
    }
    
}
