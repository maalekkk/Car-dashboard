package org.Presentation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import java.io.IOException;


/**
 * The class is related with Graphical User Interface.
 */
public class Gui extends Application {

    private GuiController guiController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard2.fxml"));
        Parent root = fxmlLoader.load();
        guiController = fxmlLoader.getController();

        BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, (StackPane)fxmlLoader.getNamespace().get("mainGuiStackPane"), 250, 250);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/logo.png")));

        //Close Button
        ImageView closeButton = (ImageView)fxmlLoader.getNamespace().get("cancelButton");
        closeButton.setOnMouseClicked(e -> stage.close());

        //Min Button
        ImageView minButton = (ImageView)fxmlLoader.getNamespace().get("MinButton");
        minButton.setOnMouseClicked(e -> scene.minimizeStage());

        //Max Button
        ImageView maxButton = (ImageView)fxmlLoader.getNamespace().get("MaxButton");
        maxButton.setOnMouseClicked(e -> scene.maximizeStage());

        scene.removeDefaultCSS();
        scene.setMoveControl((GridPane)fxmlLoader.getNamespace().get("topPanelGridPane"));
        stage.setTitle("Dashboard");
        stage.show();
    }

    @Override
    public void stop() {
        this.guiController.onStageDestruction();
    }

    /**
     * Main of the Gui.
     */
    public static void main() {
        launch();
    }

}