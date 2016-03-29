/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;
import wekiinputhelper.Modifiers.ModifiedInput;

/**
 *
 * @author rebecca
 */
public abstract class InputModifierBuilderPanel extends JPanel {
    public abstract boolean validateForm();
    public abstract ModifiedInput getModifiedInput();
    public int increment = 1;
    
    public void incrementName() {
        increment++;
    }
    
    public abstract String[] getNames();
    
    private int dimensionality = 0;

    public static final String PROP_DIMENSIONALITY = "dimensionality";

    /**
     * Get the value of dimensionality
     *
     * @return the value of dimensionality
     */
    public int getDimensionality() {
        return dimensionality;
    }

    /**
     * Set the value of dimensionality
     *
     * @param dimensionality new value of dimensionality
     */
    public void setDimensionality(int dimensionality) {
        int oldDimensionality = this.dimensionality;
        this.dimensionality = dimensionality;
        propertyChangeSupport.firePropertyChange(PROP_DIMENSIONALITY, oldDimensionality, dimensionality);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        
        //TODO THIS IS WHA"TS CAUSING PROBLEMS
       if (propertyChangeSupport != null) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
