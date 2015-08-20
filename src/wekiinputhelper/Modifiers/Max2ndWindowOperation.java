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
public class Max2ndWindowOperation implements Operation {
    
    @Override
    public double doOperation(double[] vals) {
        double[] fods = compute2ndDifferences(vals);
        
        double max = Double.MIN_VALUE;
        for (int i = 0; i < fods.length; i++) {
            if (fods[i] > max) {
                max = fods[i];
            }
        }
        return max;
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
        return "Max2ndDiff";
    }

}
