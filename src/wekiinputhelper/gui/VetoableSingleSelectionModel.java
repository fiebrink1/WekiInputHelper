/*
 * This code is from http://stackoverflow.com/questions/12389801/forbid-tab-change-in-a-jtabbedpane
 * Used to make change in GUI tab selection vetoable
 */
package wekiinputhelper.gui;

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import javax.swing.DefaultSingleSelectionModel;

/**
 *
 * @author rebecca
 */
public class VetoableSingleSelectionModel extends
        DefaultSingleSelectionModel {

    private VetoableChangeSupport vetoableChangeSupport;

    @Override
    public void setSelectedIndex(int index) {
        if (getSelectedIndex() == index)
            return;
        try {
            fireVetoableChange(getSelectedIndex(), index);
        } catch (PropertyVetoException e) {
            return;
        }
        super.setSelectedIndex(index);
    }

    private void fireVetoableChange(int oldSelectionIndex,
            int newSelectionIndex) throws PropertyVetoException {
        if (!isVetoable())
            return;
        vetoableChangeSupport.fireVetoableChange("selectedIndex",
                oldSelectionIndex, newSelectionIndex);

    }

    private boolean isVetoable() {
        if (vetoableChangeSupport == null)
            return false;
        return vetoableChangeSupport.hasListeners(null);
    }

    public void addVetoableChangeListener(VetoableChangeListener l) {
        if (vetoableChangeSupport == null) {
            vetoableChangeSupport = new VetoableChangeSupport(this);
        }
        vetoableChangeSupport.addVetoableChangeListener(l);
    }

    public void removeVetoableChangeListener(VetoableChangeListener l) {
        if (vetoableChangeSupport == null)
            return;
        vetoableChangeSupport.removeVetoableChangeListener(l);
    }

}