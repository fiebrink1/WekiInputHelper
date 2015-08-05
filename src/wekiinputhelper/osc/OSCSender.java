/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.osc;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import wekiinputhelper.OutputManager;

/**
 *
 * @author rebecca
 */
public class OSCSender {
    private String oscMessage = null;
    private OSCPortOut sender = null;
    private InetAddress hostname = null;
    private int port = -1;
    private final int DEFAULT_SEND_PORT = 6453;
    private boolean isValidState = false;

    protected EventListenerList listenerList = new EventListenerList();
    private ChangeEvent changeEvent = null;
    private static final Logger logger = Logger.getLogger(OSCSender.class.getName());

        private boolean sendInputs = true;

    public static final String PROP_SENDINPUTS = "sendInputs";

    /**
     * Get the value of sendInputs
     *
     * @return the value of sendInputs
     */
    public boolean isSendInputs() {
        return sendInputs;
    }

    /**
     * Set the value of sendInputs
     *
     * @param sendInputs new value of sendInputs
     */
    public void setSendInputs(boolean sendInputs) {
        boolean oldSendInputs = this.sendInputs;
        this.sendInputs = sendInputs;
        propertyChangeSupport.firePropertyChange(PROP_SENDINPUTS, oldSendInputs, sendInputs);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    
    public boolean hasValidHostAndPort() {
        return isValidState;
    }

    public OSCSender() throws UnknownHostException, SocketException {

    }

    public void setDefaultHostAndPort() throws SocketException, UnknownHostException {
        setHostnameAndPort(InetAddress.getByName("localhost"), DEFAULT_SEND_PORT);
    }

    public InetAddress getHostname() {
        return hostname;
    }

    public void setHostnameAndPort(InetAddress hostname, int port) throws SocketException {
        sender = new OSCPortOut(hostname, port);
        this.port = port;
        this.hostname = hostname;
        isValidState = true;
    }

    public int getPort() {
        return port;
    }

    public String getOscMessage() {
        return oscMessage;
    }

    public void setOscMessage(String oscMessage) {
        this.oscMessage = oscMessage;
    }
    
    

    //Does not establish long-term sender
    //Use for connection testing
    public static void sendTestMessage(String message, InetAddress hostname, int port, int numFloats) throws SocketException, IOException {
        OSCPortOut s = new OSCPortOut(hostname, port);
        Object[] o = new Object[numFloats];
        for (int i = 0; i < o.length; i++) {
            o[i] = new Float(i);
        }
        OSCMessage msg = new OSCMessage(message, o);
        s.send(msg);
    }

     public static void sendNamesMessage(InetAddress hostname, int port, String[] names) throws SocketException, IOException {
        String message = OSCControlReceiver.setInputNamesMessage;
        OSCPortOut s = new OSCPortOut(hostname, port);
        /*Object[] o = new Object[names.length];
        for (int i = 0; i < o.length; i++) {
            o[i] = new
        } */
        OSCMessage msg = new OSCMessage(message, names);
        s.send(msg);
    }

    
    public void sendOutputValuesMessage(double[] data) throws IOException {
        if (isValidState) {
            Object[] o = new Object[data.length];
            try {
                for (int i = 0; i < data.length; i++) {
                    o[i] = (float) data[i];
                }

                OSCMessage msg = new OSCMessage(oscMessage, o);
                sender.send(msg);
                fireSendEvent();
            } catch (IOException ex) {
                Logger.getLogger(OSCSender.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } else {
            logger.log(Level.WARNING, "Could not send OSC message: Invalid state");
        }

    }

     public void sendOutputValuesMessageWithInputs(double[] inputs, double[] data) throws IOException {
        if (isValidState) {
            Object[] o = new Object[inputs.length + data.length];
            try {
                int index = 0;

                for (int i = 0; i < inputs.length; i++){
                    o[index] = (float)inputs[i];
                    index++;
                }
                
                for (int i = 0; i < data.length; i++) {
                    o[index] = (float) data[i];
                    index++;
                }

                OSCMessage msg = new OSCMessage(oscMessage, o);
                sender.send(msg);
                fireSendEvent();
            } catch (IOException ex) {
                Logger.getLogger(OSCSender.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } else {
            logger.log(Level.WARNING, "Could not send OSC message: Invalid state");
        }

    }

    
    public void addSendEventListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeSendEventListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    private void fireSendEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public void attachToOutput(OutputManager outputManager) {
        outputManager.addOutputGroupComputedListener(new OutputManager.OutputValueListener() {

            @Override
            public void update(double[] inputs, double[] vals) {
                if (sendInputs) {
                    try {
                        sendOutputValuesMessageWithInputs(inputs, vals);
                    } catch (IOException ex) {
                        Logger.getLogger(OSCSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        sendOutputValuesMessage(vals);
                    } catch (IOException ex) {
                        Logger.getLogger(OSCSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

}
