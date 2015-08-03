/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.osc;

import com.illposed.osc.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class OSCReceiver {
    private int receivePort = 6448;
    private OSCPortIn receiver;    
        
    public OSCReceiver() {
    }
    
    public enum ConnectionState {
        NOT_CONNECTED, CONNECTING, CONNECTED, FAIL
    };
    public static final String PROP_CONNECTIONSTATE = "connectionState";
    private ConnectionState connectionState = ConnectionState.NOT_CONNECTED;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    public int getReceivePort() {
        return receivePort;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }
   
    private void setConnectionState(ConnectionState connectionState) {
        ConnectionState oldConnectionState = this.connectionState;
        this.connectionState = connectionState;
        propertyChangeSupport.firePropertyChange(PROP_CONNECTIONSTATE, oldConnectionState, connectionState);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    public void setReceivePort(int port) {
        receivePort = port;
    }
    
    public void startListening() {
        try {
            receiver = new OSCPortIn(receivePort);
            receiver.startListening();
            setConnectionState(ConnectionState.CONNECTED);   
        } catch (Exception ex) {
            Util.showPrettyErrorPane(null, 
                    "Could not bind to port " + receivePort 
                            + ". Please ensure that nothing else is trying to listen on this same port.");
            
        }
    }
    
    public void stopListening() {
        //need to notify listeners that receiver has changed?
        if (receiver != null) {
            receiver.stopListening();
            receiver.close(); //this line causes errors!! No way to get rid of them, nothing to worry about.
        }
        setConnectionState(ConnectionState.NOT_CONNECTED);
    }

    public void addOSCListener(String message, OSCListener listener) {
        if (receiver != null) {
            receiver.addListener(message, listener);
        }
    }
    
    public static void main(String[] args) {
       /* try {
            OSCPortIn receiver = new OSCPortIn(6448);
            OSCListener listener = new OSCListener() {
                public void acceptMessage(java.util.Date time, OSCMessage message) {
                    System.out.println("Message 1 received!");
                }
            };
            OSCListener listener2 = new OSCListener() {
                public void acceptMessage(java.util.Date time, OSCMessage message) {
                    System.out.println("Message 2 received!");
                }
            };


            receiver.startListening();
            receiver.addListener("/m1", listener);
            receiver.addListener("/m2", listener2);
        } catch (SocketException ex) {
            Logger.getLogger(OSCHandler.class.getName()).log(Level.SEVERE, null, ex);
        } */
        
    }
}
