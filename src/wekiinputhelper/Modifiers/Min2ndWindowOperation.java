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
public class Min2ndWindowOperation implements Operation {
    
    @Override
    public double doOperation(double[] vals) {
        double[] fods = compute2ndDifferences(vals);
        
        double min = Double.MAX_VALUE;
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] < min) {
                min = vals[i];
            }
        }
        return min;
    }
    
    private double[] compute2ndDifferences(double[] vals) {
        double[] diffs = new double[vals.length-2];
        for (int i = 2; i < vals.length; i++) {
            diffs[i-2] = vals[i] - 2 *vals[i-1] + vals[i-2];
        }
        return diffs;
    }

    @Override
    public String shortName() {
        return "Min2ndDiff";
    }

}
