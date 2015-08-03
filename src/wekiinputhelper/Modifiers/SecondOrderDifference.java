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
public class SecondOrderDifference implements ModifiedInput, UsesOnlyOriginalInputs{
    String name;
    int index;
    double x_n2 = 0;
    double x_n1 = 0;
    double x_n = 0;
    double y_n = 0;
    
    public SecondOrderDifference(String originalName, int index) {
        this.name = originalName + "_2ndOrderDiff";
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
