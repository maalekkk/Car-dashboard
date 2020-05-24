package org.Data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;

/**
 * The SQL class allows to operate on the database.
 */
public class SQL implements DataHandling {
    /**
     * The connection string to the database.
     */
    static final String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=Dashboard";
    /**
     * The Driver to operate on the database.
     */
    static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    /**
     * The state of connection with the databse.
     */
    Connection conn = null;
    /**
     * The Statement field to sending SQL statement to the database.
     */
    Statement stsm = null;

    /**
     * Method connect allows to get connection with the database.
     */
    public void connect() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(connectionString, "dashboardGuest", "dashboard");
            stsm = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method disconnect allows to close the connection with the database.
     *
     * @throws SQLException the sql exception
     */
    public void disconnect() throws SQLException {
        if (stsm != null)
            stsm.close();
        if (conn != null)
            conn.close();
    }

    @Override
    public void write(Path fileName, DashboardSerializationModel dashboardSerializationModel) throws IOException, SQLException {
        connect();
        String query = "INSERT INTO dashboardData(avgSpeed, maxSpeed, avgFuel, maxFuel, journeyDistance, journeyMinutes, totalCounter, dayCounter1, dayCounter2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setFloat(1, Math.round(dashboardSerializationModel.getAvgSpeed() * 10f) / 10.0f);
        preparedStatement.setFloat(2, Math.round(dashboardSerializationModel.getMaxSpeed() * 10f) / 10.0f);
        preparedStatement.setFloat(3, Math.round(dashboardSerializationModel.getAvgFuel() * 10f) / 10.0f);
        preparedStatement.setFloat(4, Math.round(dashboardSerializationModel.getMaxFuel() * 10f) / 10.0f);
        preparedStatement.setFloat(5, Math.round(dashboardSerializationModel.getJourneyDistance() * 10f) / 10.0f);
        preparedStatement.setInt(6, dashboardSerializationModel.getJourneyMinutes());
        preparedStatement.setInt(7, dashboardSerializationModel.getCounter());
        preparedStatement.setFloat(8, Math.round(dashboardSerializationModel.getDayCounter1() * 10f) / 10.0f);
        preparedStatement.setFloat(9, Math.round(dashboardSerializationModel.getDayCounter2() * 10f) / 10.0f);
        preparedStatement.executeUpdate();
        disconnect();
    }

    @Override
    public ObservableList<DashboardSerializationModel> read(Path fileName) throws IOException, ClassNotFoundException, SQLException {
        connect();
        String query = "SELECT * FROM dashboardData";
        ResultSet resultSet = stsm.executeQuery(query);
        ObservableList<DashboardSerializationModel> observableList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            observableList.add(new DashboardSerializationModel(resultSet.getInt(1),
                    resultSet.getFloat(2),
                    resultSet.getFloat(3),
                    resultSet.getFloat(4),
                    resultSet.getFloat(5),
                    resultSet.getFloat(6),
                    resultSet.getInt(7),
                    resultSet.getInt(8),
                    resultSet.getFloat(9),
                    resultSet.getFloat(10),
                    resultSet.getDate(11)));
        }
        disconnect();
        return observableList;
    }
}
