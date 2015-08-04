/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.Modifiers;

import wekiinputhelper.Modifiers.WindowedOperation.Operation;

/**
 *
 * @author rebecca
 */
public class MaxWindowOperation implements Operation {

    @Override
    public double doOperation(double[] vals) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] > max) {
                max = vals[i];
            }
        }
        return max;
    }

    @Override
    public String shortName() {
        return "Min";
    }

}
