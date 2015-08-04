/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.util.logging.Level;
import java.util.logging.Logger;
import static wekiinputhelper.Criterion.CriterionType.*;
import static wekiinputhelper.Criterion.HowLong.*;

/**
 *
 * @author rebecca
 */
public class Criterion {
    
    public static enum CriterionType {NONE, LESS_THAN, GREATER_THAN, LESS_OR_EQUAL, GREATER_OR_EQUAL, EQUAL, CHANGE};
    public static enum HowLong {ONCE, REPEAT};
    private final CriterionType type;
    private final HowLong howLong;
    private final int index;
    private boolean isCurrentlySatisfied;
    private final double val;
    private double lastValue = Double.NaN;
    private static final Logger logger = Logger.getLogger(Criterion.class.getName());
    
    public Criterion(CriterionType type, HowLong howLong, int index, double val) {
        this.type = type;
        this.index = index;
        this.howLong = howLong;
        this.isCurrentlySatisfied = false;
        this.val = val;
    }
    
    public boolean shouldTrigger(double[] inputs) {
        boolean t;
        if (type == NONE) {
            return true;
        }
        
        if (howLong == REPEAT) {
            t = isCurrentlySatisfied(inputs);
        } else { //Just trigger first time
            boolean wasPreviouslySatisfied = isCurrentlySatisfied;
            isCurrentlySatisfied = isCurrentlySatisfied(inputs);
            t= (!wasPreviouslySatisfied && isCurrentlySatisfied);
        }
        if (type == CHANGE) {
            lastValue = inputs[index];
        }
        return t;
    }
    
    //Assess independent of stop condition
    private boolean isCurrentlySatisfied(double[] inputs) {
        if (type == LESS_THAN) {
            return (inputs[index] < val);
        } else if (type == GREATER_THAN) {
            return (inputs[index] > val);
        } else if (type == LESS_OR_EQUAL) {
            return (inputs[index] <= val);
        } else if (type == GREATER_OR_EQUAL) {
            return (inputs[index] >= val);
        } else if (type == EQUAL) {
            return (inputs[index] == val);
        } else if (type == CHANGE) {
            return inputs[index] != lastValue;
        } else {
            logger.log(Level.WARNING, "No condition found");
            return true;
        }
    }
    
}
