/*
 * A group of inputs, sent as a single OSC message
 */
package wekiinputhelper.osc;

import wekiinputhelper.Modifiers.ModifiedInputSingle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import wekiinputhelper.Modifiers.ModifiedInput;
import wekiinputhelper.Modifiers.ModifiedInputVector;
import wekiinputhelper.UsesInputsAndOutputs;
import wekiinputhelper.UsesOnlyOriginalInputs;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class OSCModifiedInputGroup {
    private final List<ModifiedInput> outputs; //Includes at least one for every original input
    private final int numOutputTypes;
    private final int dimensionality;
    private final boolean hasDependencies;
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private transient double[] currentValues;
    private transient double[] lastInputs;
    
    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public double[] getCurrentOutputs() {
         return currentValues;
    }
    
    //Doesn't do computation unless has dependencies that require it
    public void updateInputValues(double[] newInputs) {
        if (! hasDependencies) {
            for (ModifiedInput output : outputs) {
                ((UsesOnlyOriginalInputs)output).updateForInputs(newInputs);
            }
        } else {
            computeValuesForNewInputs(newInputs);
        }
    }
    
    public void computeValuesForNewInputs(double[] newInputs) {
         //Compute output values that
        int currentIndex = 0;
        
        //First do computations with no dependencies other than current inputs
        for (ModifiedInput output : outputs) {
            if (output instanceof UsesOnlyOriginalInputs) {
                ((UsesOnlyOriginalInputs)output).updateForInputs(newInputs);
                if (output instanceof ModifiedInputSingle) {
                    currentValues[currentIndex] = ((ModifiedInputSingle)output).getValue();
                } else {
                    System.arraycopy(((ModifiedInputVector)output).getValues(), 0, currentValues, currentIndex, output.getSize());
                }
            } 
            currentIndex += output.getSize();
        }
        
        //Do the rest of the computations now
        for (ModifiedInput output : outputs) {
            currentIndex = 0;
            if (output instanceof UsesInputsAndOutputs) {
                ((UsesOnlyOriginalInputs)output).updateForInputs(newInputs);
                if (output instanceof ModifiedInputSingle) {
                    currentValues[currentIndex] = ((ModifiedInputSingle)output).getValue();
                } else {
                    System.arraycopy(((ModifiedInputVector)output).getValues(), 0, currentValues, currentIndex, output.getSize());
                }
            } 
            currentIndex += output.getSize();
        }
    }

    public double[] computeAndGetValuesForNewInputs(double[] newInputs) {
        computeValuesForNewInputs(newInputs);
        lastInputs = newInputs;
        return currentValues;
    }
    
    public double[] getLastInputs() {
        return lastInputs;
    }
    
    public int getOutputDimensionality() {
        return dimensionality;
    }
    
    public int getNumOutputTypes() {
        return numOutputTypes;
    }
    
    public String[] getOutputNames() {
        int currentIndex = 0;
        String[] s = new String[getOutputDimensionality()];
        for (ModifiedInput o : outputs) {
            if (o instanceof ModifiedInputSingle) {
                s[currentIndex] = ((ModifiedInputSingle)o).getName();
                currentIndex++;
            } else {
                for (int i = 0 ; i < o.getSize(); i++) {
                    s[currentIndex] = ((ModifiedInputVector)o).getNames()[i];
                    currentIndex++;
                }
            }
        }
        return s;
    }  
    
    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public OSCModifiedInputGroup(List<ModifiedInput> outputs) 
    {
        /*if (outputs == null || outputs.isEmpty()) {
            throw new IllegalArgumentException("outputs must be a non-null list with at least one element");
        } */
        
        this.outputs = new LinkedList<>(outputs);
        this.numOutputTypes = outputs.size();
        int s = 0;
        boolean d = false;
        for (ModifiedInput output : outputs) {
            s += output.getSize();
            if (output instanceof UsesInputsAndOutputs) {
                d = true;
            } 
        }
        hasDependencies = d;
        dimensionality = s;
        currentValues = new double[s];
        
    }
    
    public OSCModifiedInputGroup(OSCModifiedInputGroup groupFromFile) {
        this.outputs = new LinkedList<>(groupFromFile.getOutputs());
        this.numOutputTypes = groupFromFile.numOutputTypes;
        this.dimensionality = groupFromFile.dimensionality;
        this.hasDependencies = groupFromFile.hasDependencies;
    }
    
    public int getOutputNumber(OSCModifiedInputGroup o) {
        for (int i= 0; i < outputs.size(); i++) {
            if (outputs.get(i).equals(o)) {
                return i;
            }
        }
        return -1;
    }
    

    
    public List<ModifiedInput> getOutputs() {
        return outputs;
    }
    
    public ModifiedInput getOutput(int which) {
        return outputs.get(which);
    }

    @Override
    public String toString() {
        return Util.toXMLString(this, "OSCModifiedInputGroup", OSCModifiedInputGroup.class);
    }

    public void writeToFile(String filename) throws IOException {
       Util.writeToXMLFile(this, "OSCModifiedInputGroup", OSCModifiedInputGroup.class, filename);
    }
    
    public static OSCModifiedInputGroup readFromFile(String filename) throws Exception {
        OSCModifiedInputGroup g = (OSCModifiedInputGroup) Util.readFromXMLFile("OSCModifiedInputGroup", OSCModifiedInputGroup.class, filename);
        return g;
    }

    
    public static void main(String[] args) {
       
    }
    
}
