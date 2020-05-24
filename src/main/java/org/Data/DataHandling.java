package org.Data;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * The interface Data handling. This interface allows you to save and read the data.
 */
public interface DataHandling {
    /**
     * Method allows to write the information to file or database.
     *
     * @param fileName                    the file name, if you write to database, param should be null
     * @param dashboardSerializationModel the dashboard serialization model
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    void write(Path fileName, DashboardSerializationModel dashboardSerializationModel) throws IOException, SQLException;

    /**
     * Method allows to read the information to file or database.
     *
     * @param fileName the file name
     * @return the observable list with all Dashboard Serialization Model from file or database
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException           the sql exception
     */
    ObservableList<DashboardSerializationModel> read(Path fileName) throws IOException, ClassNotFoundException, SQLException;
}
