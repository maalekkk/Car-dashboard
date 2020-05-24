package org.Presentation;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.Data.DashboardSerializationModel;
import java.sql.Date;

/**
 * The type Import database controller.
 */
public class ImportDatabaseController {

    private DashboardSerializationModel dashboardSerializationModel;
    @FXML private TableView<DashboardSerializationModel> tableView;
    @FXML private TableColumn<DashboardSerializationModel, Integer> recordId;
    @FXML private TableColumn<DashboardSerializationModel, Float> avgSpeed;
    @FXML private TableColumn<DashboardSerializationModel, Float> maxSpeed;
    @FXML private TableColumn<DashboardSerializationModel, Float> avgFuel;
    @FXML private TableColumn<DashboardSerializationModel, Float> maxFuel;
    @FXML private TableColumn<DashboardSerializationModel, Float> journeyDistance;
    @FXML private TableColumn<DashboardSerializationModel, Integer> journeyMinutes;
    @FXML private TableColumn<DashboardSerializationModel, Integer> counter;
    @FXML private TableColumn<DashboardSerializationModel, Float> dayCounter1;
    @FXML private TableColumn<DashboardSerializationModel, Float> dayCounter2;
    @FXML private TableColumn<DashboardSerializationModel, Date> recordDate;

    /**
     * Read from database.
     *
     * @param observableList the observable list
     */
    public void readFromDatabase(ObservableList<DashboardSerializationModel> observableList) {
        recordId.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        avgSpeed.setCellValueFactory(new PropertyValueFactory<>("avgSpeed"));
        maxSpeed.setCellValueFactory(new PropertyValueFactory<>("maxSpeed"));
        avgFuel.setCellValueFactory(new PropertyValueFactory<>("avgFuel"));
        maxFuel.setCellValueFactory(new PropertyValueFactory<>("maxFuel"));
        journeyDistance.setCellValueFactory(new PropertyValueFactory<>("journeyDistance"));
        journeyMinutes.setCellValueFactory(new PropertyValueFactory<>("journeyMinutes"));
        counter.setCellValueFactory(new PropertyValueFactory<>("counter"));
        dayCounter1.setCellValueFactory(new PropertyValueFactory<>("dayCounter1"));
        dayCounter2.setCellValueFactory(new PropertyValueFactory<>("dayCounter2"));
        recordDate.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
        tableView.setItems(observableList);
    }

    @FXML
    private void onMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            this.dashboardSerializationModel = tableView.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    /**
     * Gets dashboard serialization model.
     *
     * @return the dashboard serialization model
     */
    public DashboardSerializationModel getDashboardSerializationModel() {
        return dashboardSerializationModel;
    }
}
