/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wekiinputhelper.osc.OSCInputGroup;
import wekiinputhelper.osc.OSCModifiedInputGroup;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class WekiInputHelperFileData {
    private int oscReceivePort = 6448;
    private int oscSendPort = 6449;
    private String sendHostname;
    private String sendOscMessage;
    private String projectName = "";
    private OSCInputGroup inputs;
    private OSCModifiedInputGroup outputs;
    private InputTriggerer inputTriggerer;
    private static final Logger logger = Logger.getLogger(WekiInputHelperFileData.class.getName());
    
    public static final String FILENAME_EXTENSION = "inputproj";

    public int getOscReceivePort() {
        return oscReceivePort;
    }

    public void setOscReceivePort(int oscReceivePort) {
        this.oscReceivePort = oscReceivePort;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
        
    public WekiInputHelperFileData(WekiInputHelper w) {
        this.oscReceivePort = w.getOSCReceiver().getReceivePort();
        this.projectName = w.getProjectName();
        this.inputs = w.getInputManager().getOSCInputGroup();
        this.outputs = w.getOutputManager().getOutputGroup();
        this.sendHostname = w.getOSCSender().getHostname().getHostName();
        this.sendOscMessage = w.getOSCSender().getOscMessage();
        this.oscSendPort = w.getOSCSender().getPort();
        this.inputTriggerer = w.getInputTriggerer();
    }
    
    public void applySettings(WekiInputHelper w) throws SocketException, UnknownHostException {
        w.getOSCReceiver().setReceivePort(oscReceivePort);
        w.setProjectName(projectName);
        w.getInputManager().setOSCInputGroup(inputs);
        w.getOutputManager().setOutputGroup(outputs);
        w.getOSCSender().setOscMessage(sendOscMessage);
        w.setInputTriggerer(inputTriggerer);

        try {
            w.getOSCSender().setHostnameAndPort(InetAddress.getByName(sendHostname), oscSendPort);
        } catch (SocketException ex) {
            w.getOSCSender().setDefaultHostAndPort();
            logger.log(Level.WARNING, "Invalid host or port: setting host name and port to default values");
            Logger.getLogger(WekiInputHelperFileData.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public WekiInputHelperFileData(String projectName) {
        this.projectName = projectName;
    }
    
    @Override
    public String toString() {
        return Util.toXMLString(this, "WekiInputHelperFileData", WekiInputHelperFileData.class);
    }
    
    public static WekiInputHelperFileData readFromFile(String filename) throws Exception {
        WekiInputHelperFileData w = (WekiInputHelperFileData) Util.readFromXMLFile("WekiInputHelperFileData", WekiInputHelperFileData.class, filename);
        return w;
    }
    
    public void writeToFile(String filename) throws IOException {
        Util.writeToXMLFile(this, "WekiInputHelperFileData", WekiInputHelperFileData.class, filename);
    }
}
