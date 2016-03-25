/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import wekiinputhelper.gui.MainHelperGUI;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import wekiinputhelper.osc.OSCSender;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import wekiinputhelper.gui.Console;
import wekiinputhelper.gui.OSXAdapter;
import wekiinputhelper.osc.OSCControlReceiver;
import wekiinputhelper.osc.OSCMonitor;
import wekiinputhelper.osc.OSCReceiver;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class WekiInputHelper {
    public static final String version = "v1.0.1";

    private final OSCReceiver oscReceiver;
    private final OSCSender oscSender;
    private final InputManager inputManager;
    private final OutputManager outputManager;
    private final MainHelperGUI mainGUI;
    private final RunningManager runningManager;
    private final StatusUpdateCenter statusUpdateCenter;
    protected EventListenerList exitListenerList = new EventListenerList();
    private ChangeEvent changeEvent = null;
    private final OSCMonitor oscMonitor;
    private final LoggingManager loggingManager;
    private final OSCControlReceiver controlReceiver;
    private final WekiInputHelperController wekiInputHelperController;
    private Console myConsole = null;

    private InputTriggerer inputTriggerer = new TriggerOnReceive(new Criterion());

    public static final String PROP_INPUTTRIGGERER = "inputTriggerer";

    
    /*private String projectName = "New Project";

    public static final String PROP_PROJECT_NAME = "projectName"; */

    private String projectLocation = "New Input Helper Project";

    public static final String PROP_PROJECT_LOCATION = "projectLocation";

    private boolean hasSaveLocation = false;

    public static final String PROP_HAS_SAVE_LOCATION = "hasSaveLocation";

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private static final Logger logger = Logger.getLogger(WekiInputHelper.class.getName());
    
    
    public void addCloseListener(ChangeListener l) {
        exitListenerList.add(ChangeListener.class, l);
    }

    public void removeCloseListener(ChangeListener l) {
        exitListenerList.remove(ChangeListener.class, l);
    }

    private void fireCloseEvent() {
        Object[] listeners = exitListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public void close() {
        prepareToDie();
        fireCloseEvent(); 
    }
    
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
    
    public StatusUpdateCenter getStatusUpdateCenter() {
        return statusUpdateCenter;
    }
    
    public RunningManager getRunningManager() {
        return runningManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
    
    public OutputManager getOutputManager() {
        return outputManager;
    }
    
    public MainHelperGUI getMainHelperGUI() {
        return mainGUI;
    }
    
    public OSCMonitor getOSCMonitor() {
        return oscMonitor;
    }
    
    public OSCReceiver getOSCReceiver() {
        return oscReceiver;
    } 
    
    public OSCSender getOSCSender() {
        return oscSender;
    } 
    
    public WekiInputHelperController getWekiInputHelperController() {
        return wekiInputHelperController;
    }

    public static String getDefaultDirectory() {
        return System.getProperty("user.home"); 
    }
    
    public static String getDefaultNextProjectName() {
        String projectDefault = "WekiInputHelperProject";
        String homeDir = getDefaultDirectory();
        File f1 = new File(homeDir + File.separator + projectDefault);
        int numTries = 0;
        while (f1.exists() && numTries < 1000) {
            numTries++;
            f1 = new File(homeDir + File.separator + projectDefault + numTries);   
        }
        if (numTries == 0) {
            return projectDefault;
        } else {
            return (projectDefault + numTries);
        }
    }
    
    //Use only for testing
    public static WekiInputHelper TestingWekiInputHelper() throws IOException {
        return new WekiInputHelper();
    }
    
    public LoggingManager getLoggingManager() {
        return loggingManager;
    }
    
    public WekiInputHelper() throws IOException {
        loggingManager = new LoggingManager(this);
        loggingManager.startLoggingToFile();
        
        statusUpdateCenter = new StatusUpdateCenter(this);

        oscReceiver = new OSCReceiver();
        oscSender = new OSCSender();
        oscSender.setDefaultHostAndPort();
        
        inputManager = new InputManager(this);
        outputManager = new OutputManager(this);
        runningManager = new RunningManager(this);
                

        
        wekiInputHelperController = new WekiInputHelperController(this);
        oscMonitor = new OSCMonitor(oscReceiver, inputManager, oscSender);
        controlReceiver = new OSCControlReceiver(this);
        
        mainGUI = new MainHelperGUI(this);
        
        oscSender.attachToOutput(outputManager);
    }
    
    public final void registerForMacOSXEvents() {
       System.setProperty("apple.laf.useScreenMenuBar", "true");

       //TODO: Do we want to use flag for this to protect Windows/Linux?
        //if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[]) null));
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[]) null));
                OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[]) null));
                //  OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error while loading the OSXAdapter:", e);
                e.printStackTrace();
            }
        //}
    }
            // General info dialog; fed to the OSXAdapter as the method to call when
    // "About OSXAdapter" is selected from the application menu
    public void about() {
       // aboutBox.setLocation((int) this.getLocation().getX() + 22, (int) this.getLocation().getY() + 22);
       // aboutBox.setVisible(true);
    }

    // General preferences dialog; fed to the OSXAdapter as the method to call when
    // "Preferences..." is selected from the application menu
    public void preferences() {
       // System.out.println("HI WEKINATOR");
        //TODO: Some preferences!
        //prefs.setLocation((int) this.getLocation().getX() + 22, (int) this.getLocation().getY() + 22);
        //prefs.setVisible(true);
    }

    // General quit handler; fed to the OSXAdapter as the method to call when a system quit event occurs
    // A quit event is triggered by Cmd-Q, selecting Quit from the application or Dock menu, or logging out
    /*public boolean quit() {
       // int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
       // if (option == JOptionPane.YES_OPTION) {
       //     exit();
       // }
       // return (option == JOptionPane.YES_OPTION);
        return true;
    } */
    
        /**
     * Get the value of projectName
     *
     * @return the value of projectName
     */
    /*public String getProjectName() {
        return projectName;
    }*/

    /**
     * Set the value of projectName
     *
     * @param projectName new value of projectName
     */
    /*public void setProjectName(String projectName) {
        String oldProjectName = this.projectName;
        this.projectName = projectName;
        propertyChangeSupport.firePropertyChange(PROP_PROJECT_NAME, oldProjectName, projectName);
    }  */
    
        /**
     * Get the value of projectLocation
     *
     * @return the value of projectLocation
     */
    public String getProjectLocation() {
        return projectLocation;
    }

    /**
     * Set the value of projectLocation
     * i.e., the folder in which the .wekproj file is currently saved
     *
     * @param projectLocation new value of projectLocation
     */
    public void setProjectLocation(String projectLocation) {
        String oldProjectLocation = this.projectLocation;
        this.projectLocation = projectLocation;
        propertyChangeSupport.firePropertyChange(PROP_PROJECT_LOCATION, oldProjectLocation, projectLocation);
    }
    
    /**
     * Get the value of hasSaveLocation
     *
     * @return the value of hasSaveLocation
     */
    public boolean hasSaveLocation() {
        return hasSaveLocation;
    }

    public void setHasSaveLocation(boolean hasSaveLocation) {
        boolean oldHasSaveLocation= this.hasSaveLocation;
        this.hasSaveLocation = hasSaveLocation;
        propertyChangeSupport.firePropertyChange(PROP_HAS_SAVE_LOCATION, oldHasSaveLocation, hasSaveLocation);
    }

    public void saveAs(File projectFile) throws IOException {
       // String oldName = getProjectName();
        String oldLocation = getProjectLocation();
        try {
            //setProjectName(name);
            setProjectLocation(projectFile.getAbsolutePath());
            WekiInputHelperSaver.createNewProject(projectFile, this);
        } catch (IOException ex) {
            //setProjectName(oldName);
            setProjectLocation(oldLocation);
            throw ex;
        }
          
        setHasSaveLocation(true);
    }
    
    public void save() {
        try {
            WekiInputHelperSaver.saveExistingProject(this);
            setHasSaveLocation(true);

        } catch (IOException ex) {
            Util.showPrettyWarningPromptPane(null, "Error encountered in saving file: " + ex.getLocalizedMessage());
            logger.log(Level.SEVERE, "Could not save Wekinator Input Helper file:{0}", ex.getMessage());
        }
    }
    
    public void prepareToDie() {
        logger.log(Level.INFO, "Preparing to die");
        oscReceiver.stopListening();
        loggingManager.prepareToDie(); //Problem: getting here with X but not with close handler
    }

    public void showConsole() {
        if (myConsole == null) {
            myConsole = new Console(this);
        }
        myConsole.setVisible(true);
        myConsole.toFront();
        Util.callOnClosed(myConsole, new Util.CallableOnClosed() {

            @Override
            public void callMe() {
                myConsole = null;
            }
        });
    }

    public void saveAs() throws IOException {
        File f = Util.findSaveFile(WekiInputHelperFileData.FILENAME_EXTENSION, "InputHelper", "Input Helper save file", mainGUI);
        if (f != null) {
            //setProjectName(f.getName());
            //setProjectLocation(projectLocation);
            saveAs(f);
        }
    }
    
    /**
     * Get the value of inputTriggerer
     *
     * @return the value of inputTriggerer
     */
    public InputTriggerer getInputTriggerer() {
        return inputTriggerer;
    }

    /**
     * Set the value of inputTriggerer
     *
     * @param inputTriggerer new value of inputTriggerer
     */
    public void setInputTriggerer(InputTriggerer inputTriggerer) {
        InputTriggerer oldInputTriggerer = this.inputTriggerer;
        this.inputTriggerer = inputTriggerer;
        propertyChangeSupport.firePropertyChange(PROP_INPUTTRIGGERER, oldInputTriggerer, inputTriggerer);
    }

    

}
