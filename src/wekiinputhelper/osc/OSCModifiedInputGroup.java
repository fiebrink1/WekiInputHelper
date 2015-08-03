/*
 * A group of inputs, sent as a single OSC message
 */
package wekiinputhelper.osc;

import wekiinputhelper.Modifiers.ModifiedInput;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class OSCModifiedInputGroup {
    private final List<ModifiedInput> outputs; //Includes at least one for every original input
    
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public int getNumOutputs() {
        return outputs.size();
    }
    
    public String[] getOutputNames() {
        String[] s = new String[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            s[i] = outputs.get(i).getName();
        }
        return s;
    }
    
    //Danger: values can be modified by caller
    public double[] getValues() {
        double[] d = new double[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            d[i] = outputs.get(i).getValue();
        }
        return d;
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
        if (outputs == null || outputs.isEmpty()) {
            throw new IllegalArgumentException("outputs must be a non-null list with at least one element");
        }
        this.outputs = new LinkedList<>(outputs);
    }
    
    public OSCModifiedInputGroup(OSCModifiedInputGroup groupFromFile) {
        this.outputs = new LinkedList<>(groupFromFile.getOutputs());
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
