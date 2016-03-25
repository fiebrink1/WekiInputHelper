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
public class Max1stWindowOperation implements Operation {
    
    @Override
    public double doOperation(double[] vals, int startPtr) {
        double[] fods = computeDifferences(vals, startPtr);
        
        double max = Double.MIN_VALUE;
        for (int i = 0; i < fods.length; i++) {
            if (fods[i] > max) {
                max = fods[i];
            }
        }
        return max;
    }
    
    private double[] computeDifferences(double[] vals, int startPtr) {
        double[] diffs = new double[vals.length-1];
        int j = 0;
        for (int i = startPtr+1; i < vals.length; i++) {
            diffs[j] = vals[i] - vals[i-1];
            j++;
        }
        for (int i = 0; i < startPtr; i++) {
            if (i > 0) {
                diffs[j] = vals[i] - vals[i-1];
            } else {
                diffs[j] = vals[0] - vals[vals.length-1];
            }
            j++;
        }
        return diffs;
    }

    @Override
    public String shortName() {
        return "Max1stDiff";
    }

}
