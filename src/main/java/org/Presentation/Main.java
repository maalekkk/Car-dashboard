package org.Presentation;


import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

/**
 * The type Main.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();

        BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, (StackPane)fxmlLoader.getNamespace().get("mainGui"), 250, 250);
        stage.setScene(scene);

//        // the wumpus doesn't leave when the last stage is hidden.
//        Platform.setImplicitExit(false);

        //Close Button
        ImageView closeButton = (ImageView)fxmlLoader.getNamespace().get("cancelButton");
        closeButton.setOnMouseClicked(e -> Platform.exit());

        //Min Button
        ImageView minButton = (ImageView)fxmlLoader.getNamespace().get("MinButton");
        minButton.setOnMouseClicked(e -> scene.minimizeStage());

        //Max Button
        ImageView maxButton = (ImageView)fxmlLoader.getNamespace().get("MaxButton");
        maxButton.setOnMouseClicked(e -> scene.maximizeStage());

        scene.removeDefaultCSS();
        scene.setMoveControl((VBox)fxmlLoader.getNamespace().get("topPanel"));
        stage.setTitle("Dashboard");
        stage.show();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }
}

