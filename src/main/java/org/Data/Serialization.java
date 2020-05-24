package org.Data;

import javafx.collections.ObservableList;
import org.Logic.Dashboard;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Serialization class that is use to serialization the main object in the app to file. Class is use at the start and at the end of the app.
 */
public class Serialization {

    /**
     * Write the main object Dashboard to file when user exit the app.
     *
     * @param db the Dashboard
     * @throws IOException the io exception
     */
    public static void writeDashboard(Dashboard db) throws IOException {
        Files.createDirectories(Paths.get(System.getenv("LOCALAPPDATA") + "\\Dashboard"));
        FileOutputStream fos = new FileOutputStream(System.getenv("LOCALAPPDATA") + "\\Dashboard\\serializedDashboard");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(db);
        oos.close();
        fos.close();
    }

    /**
     * Read the main object Dashboard when user start the app.
     *
     * @return the Dashboard
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static Dashboard readDashboard() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(System.getenv("LOCALAPPDATA") + "\\Dashboard\\serializedDashboard");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Dashboard db = (Dashboard) ois.readObject();
        ois.close();
        fis.close();
        return db;
    }

}
