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
public class FirstOrderDifference implements ModifiedInputSingle, UsesOnlyOriginalInputs{
    private final String name;
    private final int index;
    private transient double x_n1 = 0;
    private transient double x_n = 0;
    private transient double y_n = 0;
    
    public static String makeName(String originalName, int increment) {
        if (increment == 1) {
            return originalName + "_1stOrderDiff";
        } else {
            return originalName + "_1stOrderDiff(" + increment + ")";
        }
    }

    public int getIndex() {
        return index;
    }
    
    public FirstOrderDifference(String originalName, int index, int increment) {
        this.name = makeName(originalName, increment);
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

    @Override
    public InputModifierBuilderPanel getBuildPanel(WekiInputHelper w) {
        return new FirstOrderDifferenceEditor(this, w);
    }
}
