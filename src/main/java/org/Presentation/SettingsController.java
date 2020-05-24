package org.Presentation;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.Logic.GearException;
import org.Logic.Settings;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;

/**
 * The Settings controller class.
 */
public class SettingsController {

    private Settings settings;
    private GuiController guiController;
    //    @FXML private LimitedTextField maxSpeedTextField;
    @FXML private TextField maxSpeedTextField;
    @FXML private RadioButton petrolRadioBtn;
    @FXML private RadioButton dieselRadioBtn;
    @FXML private ToggleSwitch shufflePlaySwitchBtn;
    @FXML private RadioButton fiveGearsRadioBtn;
    @FXML private RadioButton sixGearsRadioBtn;
    @FXML private ToggleSwitch autoTurnOnLightsSwitchBtn;
    @FXML private Slider dashboardLightingLevelSlider;
    @FXML private Button saveSettingsBtn;
    @FXML private TextField songsPathTf;


    /**
     * Load settings.
     *
     * @param settings      the settings
     * @param guiController the gui controller
     */
    public void loadSettings(Settings settings, GuiController guiController) {
        this.guiController = guiController;
        this.settings = settings;
        maxSpeedTextField.setText(Short.toString(this.settings.getMaxSpeed()));
        if (this.settings.getEngineType() == 'P') {
            petrolRadioBtn.setSelected(true);
            dieselRadioBtn.setSelected(false);
        }
        else if (this.settings.getEngineType() == 'D') {
            dieselRadioBtn.setSelected(true);
            petrolRadioBtn.setSelected(false);
        }
        shufflePlaySwitchBtn.setSelected(this.settings.isShufflePlay());
        if (this.settings.getNumberOfGears() == 5) {
            fiveGearsRadioBtn.setSelected(true);
            sixGearsRadioBtn.setSelected(false);
        } else if (this.settings.getNumberOfGears() == 6) {
            sixGearsRadioBtn.setSelected(true);
            fiveGearsRadioBtn.setSelected(false);
        }
        songsPathTf.setText(this.settings.getSongsPath());
        autoTurnOnLightsSwitchBtn.setSelected(this.settings.isAutoTurnOnLowBeam());
        dashboardLightingLevelSlider.setValue(this.settings.getDashboardLightingLevel());
    }

    /**
     * Save settings.
     *
     */
    public void saveSettings() {
        settings.setMaxSpeed(Short.parseShort(maxSpeedTextField.getText()));
        if (petrolRadioBtn.isSelected()) {
            settings.setEngineType('P');
        } else if (dieselRadioBtn.isSelected()) {
            settings.setEngineType('D');
        }
        settings.setShufflePlay(shufflePlaySwitchBtn.isSelected());
        settings.setNumberOfGears(fiveGearsRadioBtn.isSelected() ? (byte)5 : (byte)6);
        settings.setAutoTurnOnLowBeam(autoTurnOnLightsSwitchBtn.isSelected());
        settings.setDashboardLightingLevel((byte)dashboardLightingLevelSlider.getValue());
        boolean editMediaPlayer = !settings.getSongsPath().equals(songsPathTf.getText());
        settings.setSongsPath(songsPathTf.getText());
        if (guiController.getDashboard().getActualGear() == 6 && settings.getNumberOfGears() == 5) {
            try {
                guiController.getDashboard().setActualGear((byte) 5, false);
            } catch (GearException e) {
                e.printStackTrace();
            }
        }
        guiController.reloadDashboardAfterSettings(editMediaPlayer);
        guiController.refresh();
        Stage stage = (Stage) saveSettingsBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Disable or enable option in settings.
     *
     * @param enable the enable boolean
     */
    public void disableEnableSettings(boolean enable) {
        maxSpeedTextField.setDisable(enable);
        fiveGearsRadioBtn.setDisable(enable);
        sixGearsRadioBtn.setDisable(enable);
        petrolRadioBtn.setDisable(enable);
        dieselRadioBtn.setDisable(enable);
        autoTurnOnLightsSwitchBtn.setDisable(enable);
    }

    @FXML
    private void chooseDirectory(MouseEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node)e.getSource()).getScene().getWindow());
        if(selectedDirectory != null)
            songsPathTf.setText(selectedDirectory.getAbsolutePath());
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveSettings();
        }
    }
}