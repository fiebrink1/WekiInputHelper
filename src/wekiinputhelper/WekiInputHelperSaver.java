/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author rebecca
 */
public class WekiInputHelperSaver {
    private static final Logger logger = Logger.getLogger(WekiInputHelperSaver.class.getName());

    public static WekiInputHelper loadWekiInputHelperFromFile(String wekFilename) throws Exception {
        File wekFile = new File(wekFilename);
        WekiInputHelperFileData wfd = WekiInputHelperFileData.readFromFile(wekFilename);
        WekiInputHelper w = instantiateWekiInputHelper(wfd, wekFilename);
        return w;
    }

    public static void createNewProject(String name, File projectDir, WekiInputHelper w) throws IOException {
        createProjectFiles(projectDir);
        saveWekiInputHelperFile(name, projectDir, w);
    }

    private static void saveWekiInputHelperFile(String name, File projectDir, WekiInputHelper w) throws IOException {
        WekiInputHelperFileData wfd;
        if (w == null) {
            wfd = new WekiInputHelperFileData(name);
        } else {
            wfd = new WekiInputHelperFileData(w);
        }
        String filename = projectDir + File.separator + name + "." + WekiInputHelperFileData.FILENAME_EXTENSION;
        wfd.writeToFile(filename);
    }

    private static void createProjectFiles(File f) throws SecurityException {
        f.mkdirs();
    }

    static void saveExistingProject(WekiInputHelper w) throws IOException {
        String name = w.getProjectName();
        File projectDir = new File(w.getProjectLocation());
        saveWekiInputHelperFile(name, projectDir, w);
    }

    private static WekiInputHelper instantiateWekiInputHelper(WekiInputHelperFileData wfd, String projectFile) throws IOException {
        WekiInputHelper w = new WekiInputHelper();
        w.setProjectLocation(projectFile);
        w.setHasSaveLocation(true);
        wfd.applySettings(w);
        w.getMainHelperGUI().initializeInputs();
        w.getStatusUpdateCenter().update(null, "Successfully loaded Wekinator Input Helper project from file.");
        return w;
    }
}
