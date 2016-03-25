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
    public double doOperation(double[] vals, int startPtr) {
        double[] fods = compute2ndDifferences(vals, startPtr);
        
        double min = Double.MAX_VALUE;
        for (int i = 0; i < fods.length; i++) {
            if (fods[i] < min) {
                min = fods[i];
            }
        }
        return min;
    }
    
    private double[] compute2ndDifferences(double[] vals, int startPtr) {
        double[] diffs = new double[vals.length-2];
        /* for (int i = 2; i < vals.length; i++) {
            diffs[i-2] = vals[i] - 2 *vals[i-1] + vals[i-2];
        } */
        int j = 0;
        for (int i = startPtr+2; i < vals.length; i++) {
            diffs[j] = vals[i] - 2 *vals[i-1] + vals[i-2];
            j++;
        }
        int startIndex = 0;
        if (startPtr == vals.length - 1) {
            startIndex = 1;
        }
        for (int i = startIndex; i < startPtr; i++) {
            if (i >= 2) {
                diffs[j] = vals[i] - 2 *vals[i-1] + vals[i-2];
            } else if (i == 1) {
                diffs[j] = vals[i] - 2 *vals[0] + vals[vals.length-1];
            } else {
                //i == 0
                diffs[j] = vals[0] - 2 *vals[vals.length-1] + vals[vals.length-2];
            }
            j++;
        }
        return diffs;
    }

    @Override
    public String shortName() {
        return "Min2ndDiff";
    }

}
