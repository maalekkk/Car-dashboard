package org.Presentation;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The type Main controller.
 */
public class MainController {

    @FXML
    private void mousePressed(MouseEvent mouseEvent) {
        VBox choice = (VBox) mouseEvent.getSource();
        String id = choice.getId();
        Stage stage = (Stage) choice.getScene().getWindow();
//        stage.close();
        choice.getScene().getWindow().hide();
        if (id.equals("tui")) {
            try {
                Tui.main(null);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
//            Platform.startup(Gui::main);

            Gui gui = new Gui();
            try {
                gui.start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}