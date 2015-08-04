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
public class FirstOrderDifference implements ModifiedInputSingle, UsesOnlyOriginalInputs{
    private final String name;
    private final int index;
    private transient double x_n1 = 0;
    private transient double x_n = 0;
    private transient double y_n = 0;
    
    public FirstOrderDifference(String originalName, int index) {
        this.name = originalName + "_1stOrderDiff";
        this.index = index;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        x_n1 = x_n;
        x_n = inputs[index];
        y_n = x_n - x_n1;
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
