package org.Presentation;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.jfoenix.controls.events.JFXDialogEvent;
import eu.hansolo.medusa.Gauge;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.Data.DashboardSerializationModel;
import org.Data.ReadFiles;
import org.Data.Serialization;
import org.Logic.*;
import com.jfoenix.controls.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * The type Gui controller.
 */
public class GuiController extends UiController {
    private FlashingSignalThread flashingSignalThread;
    private SpeedThread speedThread;
    private HashMap<String, Image[]> lights;
    private Timeline progressBar;
    @FXML private GridPane mainGridPaneGui;
    @FXML private Gauge speedometer;
    @FXML private Gauge revcounter;
    @FXML private ImageView ltsLights;
    @FXML private ImageView rtsLights;
    @FXML private ImageView pLights;
    @FXML private ImageView lbLights;
    @FXML private ImageView hbLights;
    @FXML private ImageView ffLights;
    @FXML private ImageView rfLights;
    @FXML private CheckMenuItem ltsLightsCb;
    @FXML private CheckMenuItem rtsLightsCb;
    @FXML private CheckMenuItem lbLightsCb;
    @FXML private CheckMenuItem ffLightsCb;
    @FXML private CheckMenuItem rfLightsCb;
    @FXML private CheckMenuItem pLightsCb;
    @FXML private CheckMenuItem hbLightsCb;
    @FXML private Text clockTimeT;
    @FXML private Text gearT;
    @FXML private Label songNameL;
    @FXML private Label songArtistL;
    @FXML private ProgressBar songB;
    @FXML private Slider songVolumeS;
    @FXML private Text avgSpeedT;
    @FXML private Text avgFuelT;
    @FXML private Text maxSpeedT;
    @FXML private Text maxFuelT;
    @FXML private Text journeyDistanceT;
    @FXML private Text journeyTimeT;
    @FXML private Text counterT;
    @FXML private Text dayCounter1T;
    @FXML private Text dayCounter2T;
    @FXML private Circle songPlayC;
    @FXML private MenuItem startEngineMi;
    @FXML private MenuItem stopEngineMi;
    @FXML private MenuItem xmlExportMi;
    @FXML private MenuItem dbExportMi;
    @FXML private MenuItem xmlImportMi;
    @FXML private MenuItem dbImportMi;
    @FXML private StackPane mainGuiStackPane;
    @FXML private ImageView ccLights;
    @FXML private MenuItem cruiseControlMinus;
    @FXML private MenuItem cruiseControlPlus;
    @FXML private CheckMenuItem ccLightsCb;
    @FXML private MenuBar menuBar;

    @FXML
    private void initialize() throws IOException, InterruptedException {
        ReadFiles readFiles = new ReadFiles();
        lights = readFiles.loadLights();
        this.dashboard.getMusicPlayer().setAutoPlayNext(this::nextSongMp);
        initClock();
        songNameL.setText("");
        songArtistL.setText("");
        stopEngineMi.setDisable(true);
        dashboard.setCruiseControlLights(false);
        ccLightsCb.setSelected(false);
        cruiseControlMinus.setDisable(true);
        cruiseControlPlus.setDisable(true);
        ccLightsCb.setDisable(true);
        reloadDashboardAfterSettings(true);
        refresh();
        ltsLightsCb.setDisable(true);
        rtsLightsCb.setDisable(true);
        progressBar = null;
    }

    private void animateEngineStart(boolean forward) throws TurnSignalException {
        final double revs = dashboard.getSettings().getMaxRevs() / ((forward) ? 100.0: -100.0);
        final double speed = dashboard.getSettings().getMaxSpeed() / ((forward) ? 100.0: -100.0);
        Timeline animEngineStartGrow = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            double newSpeed = speedometer.getValue() + speed;
            if (newSpeed < 0)
                newSpeed = 0;
            speedometer.setValue(newSpeed);
            double newRevs = revcounter.getValue() + revs;
            if (newRevs < 0)
                newRevs = 0;
            revcounter.setValue(newRevs);
        }), new KeyFrame(Duration.millis(15)));
        animEngineStartGrow.setCycleCount(100);
        animEngineStartGrow.setDelay(Duration.millis((forward) ? 2800 : 200));
        animEngineStartGrow.play();
        animEngineStartGrow.setOnFinished(f ->{
            if(forward) {
                try {
                    animateEngineStart(false);
                } catch (TurnSignalException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            Thread.sleep((forward) ? 300 : 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        animateLights(forward);
    }

    private void animateLights(boolean state) {
        switchAllLights(state, false);
        if (!state) {
            refreshLights();
            if(dashboard.getSettings().isAutoTurnOnLowBeam()) {
                dashboard.setLowBeamLights(true);
                lbLightsCb.setSelected(true);
                changeLight(lbLights, lights.get("lbLights")[1], true, true);
            }
        }
        float temp = (float) (state ? 0.1: -0.1);
        Timeline animLights = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            ltsLights.setOpacity(ltsLights.getOpacity() + temp);
            rtsLights.setOpacity(rtsLights.getOpacity() + temp);
            if (state) {
                lbLights.setOpacity(lbLights.getOpacity() + temp);
                hbLights.setOpacity(hbLights.getOpacity() + temp);
                ffLights.setOpacity(ffLights.getOpacity() + temp);
                rfLights.setOpacity(rfLights.getOpacity() + temp);
                pLights.setOpacity(pLights.getOpacity() + temp);
                ccLights.setOpacity(ccLights.getOpacity() + temp);
            } else {
                if (!dashboard.getSettings().isAutoTurnOnLowBeam())
                    lbLights.setOpacity(lbLights.getOpacity() + temp);
                if (!dashboard.isHighBeamLights())
                    hbLights.setOpacity(hbLights.getOpacity() + temp);
                if (!dashboard.isFrontFogLights())
                    ffLights.setOpacity(ffLights.getOpacity() + temp);
                if (!dashboard.isRearFogLights())
                    rfLights.setOpacity(rfLights.getOpacity() + temp);
                if (!dashboard.isPositionLights())
                    pLights.setOpacity(pLights.getOpacity() + temp);
                if (!dashboard.isCruiseControlLights())
                    ccLights.setOpacity(ccLights.getOpacity() + temp);
            }
        }), new KeyFrame(Duration.millis(120)));
        animLights.setCycleCount(8);
        animLights.setDelay(Duration.millis((state) ? 200 : 400));
        animLights.play();
        animLights.setOnFinished(event -> {
            if  (!state) {
                ltsLightsCb.setDisable(false);
                rtsLightsCb.setDisable(false);
                lbLightsCb.setDisable(false);
                hbLightsCb.setDisable(false);
                ffLightsCb.setDisable(false);
                rfLightsCb.setDisable(false);
                pLightsCb.setDisable(false);
                refreshLights();
            }
        });
    }

    public void engineStartStop(boolean enable, boolean engineError) throws TurnSignalException {
        lbLightsCb.setSelected(dashboard.getSettings().isAutoTurnOnLowBeam());
        if(enable) {
            if (speedThread != null)
                speedThread.setAnimateEngineSpeedRevsToZero(false);
            ltsLightsCb.setDisable(false);
            rtsLightsCb.setDisable(false);
            ccLightsCb.setDisable(false);
            stopEngineMi.setDisable(false);
            startEngineMi.setDisable(true);
            dbExportMi.setDisable(true);
            xmlExportMi.setDisable(true);
            dbImportMi.setDisable(true);
            xmlImportMi.setDisable(true);
            if (dashboard.getOnBoardComputer().getJourneyTimeLocalDateTime() == null)
                dashboard.getOnBoardComputer().startJourneyTime();
            int startAfter = 0;
            if (dashboard.getSpeed() == 0 && dashboard.getRevs() == 0) {
                ltsLightsCb.setDisable(true);
                rtsLightsCb.setDisable(true);
                lbLightsCb.setDisable(true);
                hbLightsCb.setDisable(true);
                ffLightsCb.setDisable(true);
                rfLightsCb.setDisable(true);
                pLightsCb.setDisable(true);
                animateEngineStart(true);
                dashboard.engineSound(false);
                startAfter = 5800;
            } else {
                dashboard.engineSound(true);
                refreshLights();
            }
            speedThread = new SpeedThread(this, startAfter);
            speedThread.setEngineRunning(true);
            speedThread.start();
            if(!dashboard.getMusicPlayer().isPlaying())
                playPauseMp();
        }
        else {
            switchAllLights(false, true);
            if (dashboard.getMusicPlayer().isPlaying())
                playPauseMp();
            try {
                dashboard.setCruiseControlSpeed((short) 0);
            } catch (CruiseControlException ignored) {}
            ltsLightsCb.setDisable(true);
            rtsLightsCb.setDisable(true);
            dashboard.setKeyUp(false);
            dashboard.setKeyDown(false);
            dashboard.setCruiseControlLights(false);
            ccLightsCb.setSelected(false);
            cruiseControlPlus.setDisable(true);
            cruiseControlMinus.setDisable(true);
            ccLightsCb.setDisable(true);
            startEngineMi.setDisable(false);
            stopEngineMi.setDisable(true);
            dbExportMi.setDisable(false);
            xmlExportMi.setDisable(false);
            dbImportMi.setDisable(false);
            xmlImportMi.setDisable(false);
            speedThread.setEngineRunning(false);
            if (engineError) {
                Platform.runLater(() -> {
                    openDialog("Engine problem", "Engine turned off", "Ok");
                    });
            }
            speedThread = new SpeedThread(this,0);
            speedThread.setAnimateEngineSpeedRevsToZero(true);
            speedThread.start();
        }
    }

    @FXML
    private void engineStatus() throws TurnSignalException, InterruptedException {
        if(!startEngineMi.isDisable())
            engineStartStop(true, false);
        else if (startEngineMi.isDisable())
            engineStartStop(false, false);
    }

    @FXML
    private void changeCruiseControlSpeed(ActionEvent event) {
        String id = ((MenuItem)event.getSource()).getId();
        if (id.equals("cruiseControlMinus")) {
            dashboard.changeCruiseControlSpeed(false);
        } else if (id.equals("cruiseControlPlus")) {
            dashboard.changeCruiseControlSpeed(true);
        }
    }

    public void switchOffCruiseControl() {
        dashboard.setCruiseControlLights(false);
        switchLight("ccLights", false);
    }

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            clockTimeT.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * Open new window.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void openNewWindow(ActionEvent actionEvent) throws IOException {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        String id = menuItem.getId();
        ImportDatabaseController importDatabaseController = null;
        String fileName = null;
        String title = null;
        switch (id) {
            case "about":
                fileName = "about.fxml";
                title = "About the program";
                break;
            case "settings":
                fileName = "settings2.fxml";
                title = "Settings";
                break;
            case "dbImportMi":
                fileName = "importDatabase.fxml";
                title = "Import from database";
                break;
            case "instruction":
                fileName = "instruction.fxml";
                title = "Instruction";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
        FXMLLoader root = new FXMLLoader(getClass().getResource(fileName));
        Parent parent = root.load();
        Stage stage = new Stage();
        BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, (StackPane)root.getNamespace().get("mainGui"), 250, 250);
        scene.removeDefaultCSS();

        //Close Button
        ImageView closeButton = (ImageView)root.getNamespace().get("cancelButton");
        closeButton.setOnMouseClicked(e -> stage.close());
        //Min Button
        ImageView minButton = (ImageView)root.getNamespace().get("MinButton");
        minButton.setOnMouseClicked(e -> scene.minimizeStage());
        //Max Button
        ImageView maxButton = (ImageView)root.getNamespace().get("MaxButton");
        maxButton.setOnMouseClicked(e -> scene.maximizeStage());

        scene.setMoveControl((Parent)root.getNamespace().get("topPanel"));
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();

        if (id.equals("settings")) {
            SettingsController settingsController = root.getController();
            settingsController.loadSettings(dashboard.getSettings(), this);
            settingsController.disableEnableSettings(startEngineMi.isDisable());
        } else if (id.equals("dbImportMi")) {
            importDatabaseController = root.getController();
            importDatabaseController.readFromDatabase(dashboard.readFromDatabase());
        }

        ImportDatabaseController finalImportDatabaseController = importDatabaseController;
        stage.setOnCloseRequest(event -> {
            if (id.equals("dbImportMi") && finalImportDatabaseController.getDashboardSerializationModel() != null) {
                this.dashboard.updateDashboardFromFile(finalImportDatabaseController.getDashboardSerializationModel());
                refresh();
            }
        });
    }

    private void openDialog(String header, String content, String buttonText) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setBody(new Text(content));
        dialogLayout.setHeading(new Text(header));
        JFXDialog dialog = new JFXDialog(mainGuiStackPane, dialogLayout, JFXDialog.DialogTransition.LEFT);
        JFXButton button = new JFXButton(buttonText);
        button.getStyleClass().add("dialog-button");
        dialogLayout.getStyleClass().add("dialog-layout");
        button.setOnAction(event -> dialog.close());
        dialogLayout.setActions(button);
        mainGridPaneGui.setDisable(true);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent e) -> {
            mainGridPaneGui.setDisable(false);
            mainGridPaneGui.setEffect(null);
        });
        BoxBlur blur = new BoxBlur(6,6,6);
        mainGridPaneGui.setEffect(blur);
    }

    private void changeLight(ImageView imageView, Image newImage, boolean enable, boolean opacity) {
        imageView.setImage(newImage);
        if (opacity)
            imageView.setOpacity(enable ? 0.88 : 0.1);
    }

    private void changeTurnSignal(ImageView imageView, Image newImage, boolean enable) {
        if (enable) {
            flashingSignalThread = new FlashingSignalThread(imageView, newImage);
            flashingSignalThread.start();
        } else {
            flashingSignalThread.stopFlashing();
            flashingSignalThread.interrupt();
            imageView.setImage(newImage);
            imageView.setOpacity(0.1);
        }
    }

    private void refreshLights() {
        if (dashboard.isLowBeamLights()) {
            switchLight("lbLights", true);
            lbLights.setOpacity(0.88);
        }
        if (dashboard.isHighBeamLights()) {
            switchLight("hbLights", true);
            lbLights.setOpacity(0.88);
        }
        if (dashboard.isFrontFogLights()) {
            switchLight("ffLights", true);
            lbLights.setOpacity(0.88);
        }
        if (dashboard.isRearFogLights()) {
            switchLight("rfLights", true);
            lbLights.setOpacity(0.88);
        }
        if (dashboard.isPositionLights()) {
            switchLight("pLights", true);
            lbLights.setOpacity(0.88);
        }
        if (dashboard.isCruiseControlLights()) {
            switchLight("ccLights", true);
            ccLights.setOpacity(0.88);
        }
    }

    private void switchAllLights(boolean state, boolean opacity) {
        int isOn = (state) ? 1 : 0;
        changeLight(ltsLights, lights.get("ltsLights")[isOn], state, opacity);
        if (ltsLightsCb.isSelected()) {
            ltsLightsCb.setSelected(state);
            changeTurnSignal(ltsLights, lights.get("ltsLights")[0], false);
        }
        changeLight(rtsLights, lights.get("rtsLights")[isOn], state, opacity);
        if (rtsLightsCb.isSelected()) {
            rtsLightsCb.setSelected(state);
            changeTurnSignal(rtsLights, lights.get("rtsLights")[0], false);
        }
        changeLight(pLights, lights.get("pLights")[isOn], state, opacity);
        pLightsCb.setSelected(state);
        changeLight(lbLights, lights.get("lbLights")[isOn], state, opacity);
        lbLightsCb.setSelected(state);
        changeLight(hbLights, lights.get("hbLights")[isOn], state, opacity);
        hbLightsCb.setSelected(state);
        changeLight(ffLights, lights.get("ffLights")[isOn], state, opacity);
        ffLightsCb.setSelected(state);
        changeLight(rfLights, lights.get("rfLights")[isOn], state, opacity);
        rfLightsCb.setSelected(state);
        changeLight(ccLights,lights.get("ccLights")[isOn], state, opacity);
    }

    private void switchLight(String nameOfLight, boolean enable) {
        javafx.scene.image.Image replaceImage = lights.get(nameOfLight)[(enable) ? 1 : 0];
        switch (nameOfLight) {
            case "ltsLights":
                try{
                    dashboard.setLeftTurnSignal(enable);
                    ltsLightsCb.setSelected(enable);
                } catch (TurnSignalException e) {
                    ltsLightsCb.setSelected(false);
                    break;
                }
                changeTurnSignal(ltsLights, replaceImage, enable);
                break;

            case "rtsLights":
                try{
                    dashboard.setRightTurnSignal(enable);
                    rtsLightsCb.setSelected(enable);
                } catch (TurnSignalException e) {
                    openDialog("Turn signal error", e.getMessage(), "Ok");
                    rtsLightsCb.setSelected(false);
                    break;
                }
                changeTurnSignal(rtsLights, replaceImage, enable);
                break;

            case "pLights":
                dashboard.setPositionLights(enable);
                pLightsCb.setSelected(enable);
                changeLight(pLights, replaceImage, enable, true);
                break;

            case "lbLights":
                dashboard.setLowBeamLights(enable);
                lbLightsCb.setSelected(enable);
                changeLight(lbLights, replaceImage, enable, true);
                break;

            case "hbLights":
                dashboard.setHighBeamLights(enable);
                hbLightsCb.setSelected(enable);
                changeLight(hbLights, replaceImage, enable, true);
                break;

            case "ffLights":
                dashboard.setFrontFogLights(enable);
                ffLightsCb.setSelected(enable);
                changeLight(ffLights, replaceImage, enable, true);
                break;

            case "rfLights":
                dashboard.setRearFogLights(enable);
                rfLightsCb.setSelected(enable);
                changeLight(rfLights, replaceImage, enable, true);
                break;
            case "ccLights":
                if (dashboard.getSpeed() != 0) {
                    try {
                        dashboard.setCruiseControlSpeed(dashboard.getSpeed());
                        dashboard.setCruiseControlLights(enable);
                        ccLightsCb.setSelected(enable);
                        cruiseControlMinus.setDisable(!enable);
                        cruiseControlPlus.setDisable(!enable);
                        changeLight(ccLights, replaceImage, enable, true);
                    } catch (CruiseControlException e) {
                        ccLightsCb.setSelected(false);
                        openDialog("Cruise control error", e.getMessage(), "Ok");
                    }
                } else {
                    ccLightsCb.setSelected(false);
                }
                break;
        }
    }

    /**
     * Switch light from menu.
     *
     * @param actionEvent the action event
     */
    public void switchLightCheckMenuItem(ActionEvent actionEvent) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) actionEvent.getSource();
        switchLight(checkMenuItem.getId().substring(0,checkMenuItem.getId().length()-2), checkMenuItem.isSelected());
    }

    public void reloadDashboardAfterSettings(boolean editMusicPlayer) {
        if (editMusicPlayer) {
            if (dashboard.getMusicPlayer() != null) {
                dashboard.getMusicPlayer().dispose();
                try {
                    dashboard.getMusicPlayer().loadSongs(dashboard.getSettings().getSongsPath());
                } catch (IOException e) {
                    openDialog("Music player error", e.getMessage(), "Ok");
                }
                songProgressBarMp(true, false);
                if (dashboard.getSettings().isShufflePlay())
                    dashboard.getMusicPlayer().shufflePlaylist();
                if (songNameL.getText().indexOf('%') > 0)
                    setTitleArtist();
            }
        }
        speedometer.setMaxValue(dashboard.getSettings().getMaxSpeed());
        short maxRevs = dashboard.getSettings().getMaxRevs();
        revcounter.setThreshold(maxRevs - 1.5);
        revcounter.setMaxValue(maxRevs);
        short lightingLevel = dashboard.getSettings().getDashboardLightingLevel();
        mainGridPaneGui.setStyle("-fx-background-color: rgb("+lightingLevel+", "+lightingLevel+", "+lightingLevel+");");
        menuBar.setStyle("-fx-background-color: rgb("+lightingLevel+", "+lightingLevel+", "+lightingLevel+");");
        if(lightingLevel != 0) {
            speedometer.setBackgroundPaint(Color.rgb(20 + lightingLevel, 20 + lightingLevel, 20 + lightingLevel));
            revcounter.setBackgroundPaint(Color.rgb(20 + lightingLevel, 20 + lightingLevel, 20 + lightingLevel));
        } else {
            speedometer.setBackgroundPaint(Color.rgb(15, 15, 15));
            revcounter.setBackgroundPaint(Color.rgb(15,15,15));
        }
        dashboard.maxSpeedOnGears(dashboard.getSettings().getMaxSpeed(), dashboard.getSettings().getNumberOfGears());
    }

    /**
     * Instructions when key is pressed.
     *
     * @param event the event
     */
    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
           dashboard.setKeyUp(true);
        } else if (event.getCode() == KeyCode.DOWN) {
            dashboard.setKeyDown(true);
        } else if ((event.getCode() == KeyCode.LEFT) && !this.dashboard.isLeftTurnSignal()) {
            try {
                dashboard.setLeftTurnSignal(true);
                ltsLightsCb.setSelected(true);
                changeTurnSignal(ltsLights, lights.get("ltsLights")[1], true);
            } catch (TurnSignalException e) {
                openDialog("Turn signal error", e.getMessage(), "Ok");
            }
        } else if ((event.getCode() == KeyCode.RIGHT) && (!this.dashboard.isRightTurnSignal())) {
            try {
                dashboard.setRightTurnSignal(true);
                rtsLightsCb.setSelected(true);
                changeTurnSignal(rtsLights, lights.get("rtsLights")[1], true);
            } catch (TurnSignalException e) {
                openDialog("Turn signal error", e.getMessage(), "Ok");
            }
        } else if(event.getText().compareTo("0") >= 0 && 0 >= event.getText().compareTo("6")) {
            try {
                dashboard.setActualGear((byte) Short.parseShort(event.getText()), this.startEngineMi.isDisable());
                refresh();
            } catch (GearException e) {
                openDialog("Bad change gear", e.getMessage(), "Ok");
            }
        } else if(event.getCode() == KeyCode.SPACE) {
            playPauseMp();
        } else if(event.getCode() == KeyCode.Q) {
            prevSongMp();
        } else if(event.getCode() == KeyCode.W) {
            nextSongMp();
        }
    }

    @FXML
    private void keyReleased(KeyEvent event) throws TurnSignalException {
        if (event.getCode() == KeyCode.UP) {
            dashboard.setKeyUp(false);
        } else if (event.getCode() == KeyCode.DOWN) {
            dashboard.setKeyDown(false);
        } else if (event.getCode() == KeyCode.LEFT) {
            dashboard.setLeftTurnSignal(false);
            ltsLightsCb.setSelected(false);
            changeTurnSignal(ltsLights, lights.get("ltsLights")[0], false);
        } else if (event.getCode() == KeyCode.RIGHT) {
            dashboard.setRightTurnSignal(false);
            rtsLightsCb.setSelected(false);
            changeTurnSignal(rtsLights, lights.get("rtsLights")[0], false);
        }
    }

    public synchronized void refresh() {
        speedometer.setValue(dashboard.getSpeed());
        revcounter.setValue(dashboard.getRevs());
        gearT.setText(String.valueOf(dashboard.getActualGear()));
        avgSpeedT.setText(String.valueOf(dashboard.getOnBoardComputer().getAvgSpeed()));
        maxSpeedT.setText(String.valueOf(dashboard.getOnBoardComputer().getMaxSpeed()));
        avgFuelT.setText(String.valueOf(dashboard.getOnBoardComputer().getAvgCombustion()));
        maxFuelT.setText(String.valueOf(dashboard.getOnBoardComputer().getMaxCombustion()));
        counterT.setText(String.valueOf((int)dashboard.getCounter()));
        dayCounter1T.setText(String.valueOf(Math.round(dashboard.getDayCounter1() * 10) / 10.0));
        dayCounter2T.setText(String.valueOf(Math.round(dashboard.getDayCounter2() * 10) / 10.0));
        journeyDistanceT.setText(String.valueOf(Math. round(dashboard.getOnBoardComputer().getJourneyDistance() * 10) / 10.0));
        journeyTimeT.setText(dashboard.getOnBoardComputer().getJourneyTime());
    }

    @FXML
    private void resetDailyCounter(MouseEvent event) throws NegativeValueException {
        Circle circle = (Circle) event.getSource();
        if(circle.getId().equals("dayCounter1R")) {
            dashboard.setDayCounter1(0);
            dayCounter1T.setText("0.0");
        } else if (circle.getId().equals("dayCounter2R")){
            dashboard.setDayCounter2(0);
            dayCounter2T.setText("0.0");
        }
    }


    @FXML
    private void playPauseMp() {
        if(dashboard.getMusicPlayer().isEmpty())
            return;
        if(!dashboard.getMusicPlayer().isPlaying()) {
            dashboard.getMusicPlayer().playSong();
            songProgressBarMp(false, false);
            dashboard.getMusicPlayer().autoPlayNext(this::nextSongMp);
        } else {
            dashboard.getMusicPlayer().pauseSong();
            songProgressBarMp(false, true);
        }
        setTitleArtist();
    }

    @FXML
    private void songProgressBarMp(boolean resetProgress, boolean pause) {
        if(resetProgress && progressBar!=null) {
            progressBar.stop();
            progressBar = null;
            songB.setProgress(0.0);
        }
        if(dashboard.getMusicPlayer().isEmpty()) {
            return;
        }
        if(!pause) {
            double progressValue = 1/dashboard.getMusicPlayer().getTotalDuration().toSeconds();
            if(progressBar == null && !Double.isNaN(progressValue)) {
                progressBar = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                    songB.setProgress(songB.getProgress() + progressValue);
                }), new KeyFrame(Duration.seconds(1)));
                progressBar.setCycleCount((int) dashboard.getMusicPlayer().getTotalDuration().toSeconds() + 1);
            }
            if(progressBar != null) {
                progressBar.play();
            }
        } else if(progressBar != null) {
            progressBar.pause();
        }
    }


    @FXML
    private void nextSongMp() {
        dashboard.getMusicPlayer().nextSong();
        dashboard.getMusicPlayer().autoPlayNext(this::nextSongMp);
        songProgressBarMp(true, false);
        setTitleArtist();
    }

    @FXML
    private void prevSongMp() {
        dashboard.getMusicPlayer().previousSong();
        dashboard.getMusicPlayer().autoPlayNext(this::nextSongMp);
        songProgressBarMp(true, songPlayC.isVisible());
        setTitleArtist();
    }

    @FXML
    private void changeVolumeMp() {
        dashboard.getMusicPlayer().changeVolume(songVolumeS.getValue()/100);
    }

    @FXML
    private void setTitleArtist() {
        float temp = (float) 0.05;
        Timeline animTitleArtist = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            songNameL.setOpacity(songNameL.getOpacity() - temp);
            songArtistL.setOpacity(songArtistL.getOpacity() - temp);
        }), new KeyFrame(Duration.millis(35)));
        animTitleArtist.setCycleCount(20);
        animTitleArtist.play();

        animTitleArtist.setOnFinished(f ->{
            songNameL.setText(dashboard.getMusicPlayer().getTitle());
            try {
                songArtistL.setText(dashboard.getMusicPlayer().getArtist());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Timeline animNewTitleArtist = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                songNameL.setOpacity(songNameL.getOpacity() + temp);
                songArtistL.setOpacity(songArtistL.getOpacity() + temp);
            }), new KeyFrame(Duration.millis(35)));
            animNewTitleArtist.setCycleCount(20);
            animNewTitleArtist.setDelay(Duration.millis(200));
            animNewTitleArtist.play();
        });
    }

    @FXML
    private void exportImportClicked(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        String id = menuItem.getId();
        switch (id) {
            case "xmlExportMi": {
                Path path = chooseDirectory(actionEvent, false);
                if (path != null) {
                    dashboard.writeToXml(path);
                    openDialog("Export to XML", "Successful export!", "Great!");
                }
                break;
            }
            case "dbExportMi":
                dashboard.writeToDatabase();
                openDialog("Export to database", "Successful export!", "Great!");
                break;
            case "xmlImportMi": {
                Path path = chooseDirectory(actionEvent, true);
                if (path != null) {
                    DashboardSerializationModel dashboardSerializationModel = dashboard.readFromXML(path);
                    this.dashboard.updateDashboardFromFile(dashboardSerializationModel);
                    refresh();
                    openDialog("Import from XML", "Successful import!", "Great!");
                }
                break;
            }
        }
    }

    @FXML
    private Path chooseDirectory(ActionEvent actionEvent, boolean read) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml"));
        File chooseFile = null;
        if (read)
            chooseFile = fileChooser.showOpenDialog((mainGridPaneGui.getScene().getWindow()));
        else
            chooseFile = fileChooser.showSaveDialog((mainGridPaneGui.getScene().getWindow()));
        if(chooseFile != null)
            return Path.of(chooseFile.getAbsolutePath());
        return null;
    }

    /**
     * On stage destruction.
     */
    public void onStageDestruction() {
        try {
            Serialization.writeDashboard(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(speedThread != null) {
            speedThread.setEngineRunning(false);
        }
    }

    /**
     * Exit app.
     */
    public void exitApp() {
        onStageDestruction();
        Platform.exit();
    }

    /**
     * Remove focus.
     */
    public void removeFocus() {
        mainGridPaneGui.requestFocus();
    }
}