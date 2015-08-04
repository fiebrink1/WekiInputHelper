/*
 * Handles incoming OSC control messages
 */
package wekiinputhelper.osc;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import wekiinputhelper.WekiInputHelper;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class OSCControlReceiver {

    private final WekiInputHelper w;
    private final PropertyChangeListener oscReceiverListener;
    private final String setInputNamesMessage = "/wekinator/control/setInputNames";
    
    public OSCControlReceiver(WekiInputHelper w) {
        this.w = w;

        oscReceiverListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                oscReceiverPropertyChanged(evt);
            }
        };
        //oscReceiverListener = this::oscReceiverPropertyChanged;
        w.getOSCReceiver().addPropertyChangeListener(oscReceiverListener);
    }

    private void oscReceiverPropertyChanged(PropertyChangeEvent e) {
        if (e.getPropertyName() == OSCReceiver.PROP_CONNECTIONSTATE) {
            if (e.getNewValue() == OSCReceiver.ConnectionState.CONNECTED) {
                addOSCControlListeners();
            }
        }
    }

    private void addOSCControlListeners() {
        w.getOSCReceiver().addOSCListener(setInputNamesMessage, createInputNamesListener());
    }

    private OSCListener createInputNamesListener() {
        OSCListener l = new OSCListener() {
            @Override
            public void acceptMessage(Date date, OSCMessage oscm) {                
                Object[] o = oscm.getArguments();
                try {
                    if (w.getInputManager().hasValidInputs() && o.length != w.getInputManager().getNumInputs()) {
                        w.getStatusUpdateCenter().warn(this,
                                "Received wrong number of input names "
                                + "(received " + o.length + ", expected " + w.getInputManager().getNumInputs()+ ")");
                        return;
                    }

                    String[] names = new String[o.length];
                    for (int i = 0; i < o.length; i++) {
                        if (!(o[i] instanceof String)) {
                            w.getStatusUpdateCenter().warn(this,
                                    "Received non-string argument(s) for OSC message " + oscm);
                            return;
                        }
                        names[i] = (String) o[i];
                    }
                    if (Util.checkAllUnique(names)) {
                        w.getWekiInputHelperController().setInputNames(names);
                    } else {
                        w.getStatusUpdateCenter().warn(this,
                                "Input names not unique (received via OSC message " + setInputNamesMessage
                                + ")");
                    }
                } catch (IllegalArgumentException ex) {
                }
            }

        };
        return l;
    }
}
