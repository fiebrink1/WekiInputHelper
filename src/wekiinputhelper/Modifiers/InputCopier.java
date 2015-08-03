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
public class InputCopier implements ModifiedInput, UsesOnlyOriginalInputs{
    String name;
    int index;
    double value = 0;
    
    public InputCopier(String originalName, int index) {
        this.name = originalName;
        this.index = index;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        value = inputs[index];
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public double getValue() {
        return value;
    }
    
}
