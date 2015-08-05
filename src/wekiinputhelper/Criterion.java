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

    public static enum CriterionType {

        NONE, LESS_THAN, GREATER_THAN, LESS_OR_EQUAL, GREATER_OR_EQUAL, EQUAL, CHANGE
    };

    public static enum HowLong {

        ONCE, REPEAT
    };
    private final CriterionType type;
    private final HowLong howLong;
    private final int index;
    private transient boolean isCurrentlySatisfied;
    private final double val;
    private transient double lastValue = Double.NaN;
    private static final Logger logger = Logger.getLogger(Criterion.class.getName());
    public static String[] descriptors = {
        "is greater than",
        "is less than",
        "is greater than or equal to",
        "is less than or equal to",
        "is equal to",
        "changes"
    };
    public static CriterionType[] typesForDescriptors = {
        CriterionType.GREATER_THAN,
        CriterionType.LESS_THAN,
        CriterionType.GREATER_OR_EQUAL,
        CriterionType.LESS_OR_EQUAL,
        CriterionType.EQUAL,
        CriterionType.CHANGE
    };

    public static enum AppliesTo {INPUT, OUTPUT};
    
    private final AppliesTo appliesTo;
    
    public Criterion() {
        this.type = CriterionType.NONE;
        this.appliesTo = AppliesTo.INPUT;
        this.index = 0;
        this.howLong = HowLong.ONCE;
        this.val = 0;
        this.isCurrentlySatisfied = true;
    }

    public AppliesTo getAppliesTo() {
        return appliesTo;
    }
    
    
    public Criterion(CriterionType type, HowLong howLong, int index, AppliesTo appliesTo, double val) {
        this.type = type;
        this.index = index;
        this.appliesTo = appliesTo;
        if (type == NONE || type == CHANGE) {
            this.howLong = REPEAT;
        } else {
            this.howLong = howLong;
        }
        
            
        this.isCurrentlySatisfied = false;
        this.val = val;
    }

    public boolean shouldTrigger(double[] inputs, double[] outputs) {
        boolean t;
        if (type == NONE) {
            return true;
        }

        if (howLong == REPEAT) {
            t = isCurrentlySatisfied(inputs, outputs);
        } else { //Just trigger first time
            boolean wasPreviouslySatisfied = isCurrentlySatisfied;
            isCurrentlySatisfied = isCurrentlySatisfied(inputs, outputs);
            t = (!wasPreviouslySatisfied && isCurrentlySatisfied);
        }
        if (type == CHANGE) {
            lastValue = inputs[index];
        }
        return t;
    }
    
    public CriterionType getType() {
        return type;
    }
    
    public HowLong getHowLong() {
        return howLong;
    }
    
    public int getInputIndex() {
        return index;
    }

    public double getCriterionValue() {
        return val;
    }

    //Assess independent of stop condition
    private boolean isCurrentlySatisfied(double[] inputs, double[] outputs) {
        double[] vals;
        if (appliesTo == AppliesTo.INPUT) {
            vals = inputs;
        } else {
            vals = outputs;
        }
        
        if (type == LESS_THAN) {
            return (vals[index] < val);
        } else if (type == GREATER_THAN) {
            return (vals[index] > val);
        } else if (type == LESS_OR_EQUAL) {
            return (vals[index] <= val);
        } else if (type == GREATER_OR_EQUAL) {
            return (vals[index] >= val);
        } else if (type == EQUAL) {
            return (vals[index] == val);
        } else if (type == CHANGE) {
            return vals[index] != lastValue;
        } else {
            logger.log(Level.WARNING, "No condition found");
            return true;
        }
    }
    
    
    public static int getIndexForDescriptor(CriterionType type) {
        for (int i = 0; i < typesForDescriptors.length; i++) {
            if (type == typesForDescriptors[i])
                return i;
        }
        logger.log(Level.SEVERE, "Criterion type {0} not found in typesForDescriptors array", type);
        throw new IllegalArgumentException("Criterion type " + type + " not found");
    }


}
