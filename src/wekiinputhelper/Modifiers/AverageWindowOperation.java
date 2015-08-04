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
public class AverageWindowOperation implements Operation {

    @Override
    public double doOperation(double[] vals) {
        double sum = 0;
        for (int i = 0; i < vals.length; i++) {
            sum += vals[i];
        }
        return sum / vals.length;
    }

    @Override
    public String shortName() {
        return "Avg";
    }

}
