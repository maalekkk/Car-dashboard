package org.Logic;

import javafx.collections.ObservableList;
import javafx.scene.media.AudioClip;
import org.Data.*;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is the main object in the app. Dashboard is responsible for keep all of the data related with dashboard.
 */
public class Dashboard implements Serializable {
    private transient OnBoardComputer onBoardComputer;
    private transient short speed;
    private transient boolean leftTurnSignal;
    private transient boolean rightTurnSignal;
    private boolean positionLights;
    private boolean lowBeamLights;
    private boolean highBeamLights;
    private boolean frontFogLights;
    private boolean rearFogLights;
    private transient boolean cruiseControlLights;
    private transient short cruiseControlSpeed;
    private float counter;
    private float dayCounter1;
    private float dayCounter2;
    private transient float revs;
    private byte actualGear;
    private Settings settings;
    private List<Short> maxSpeedOnGear;
    private transient boolean keyUp;
    private transient boolean keyDown;
    private transient MusicPlayer musicPlayer;

    /**
     * Instantiates a new Dashboard.
     */
    public Dashboard() {
        this.onBoardComputer = new OnBoardComputer();
        this.speed = 0;
        this.leftTurnSignal = false;
        this.rightTurnSignal = false;
        this.positionLights = false;
        this.lowBeamLights = false;
        this.highBeamLights = false;
        this.frontFogLights = false;
        this.rearFogLights = false;
        this.cruiseControlLights = false;
        this.counter = 0;
        this.dayCounter1 = 0;
        this.dayCounter2 = 0;
        this.revs = 0;
        this.actualGear = 0;
        this.settings = new Settings();
        this.maxSpeedOnGears(this.settings.getMaxSpeed(), this.settings.getNumberOfGears());
        this.keyUp = false;
        this.keyDown = false;
        this.musicPlayer = new MusicPlayer();
    }

    /**
     * The method sets a few information like turn off the signals in the start of app.
     */
    public void DashboardAfterSerialization() {
        this.onBoardComputer = new OnBoardComputer();
        this.speed = 0;
        this.leftTurnSignal = false;
        this.rightTurnSignal = false;
        this.cruiseControlLights = false;
        this.revs = 0;
        this.keyUp = false;
        this.keyDown = false;
        this.musicPlayer = new MusicPlayer();
    }

    /**
     * Gets the list with values of th maximum speed on gears.
     *
     * @return list with the maximum speed on gears
     */
    public List<Short> getMaxSpeedOnGear() {
        return maxSpeedOnGear;
    }

    /**
     * Update dashboard after import from file or the database.
     *
     * @param dashboardSerializationModel the DashboardSerializationModel
     */
    public void updateDashboardFromFile(DashboardSerializationModel dashboardSerializationModel) {
        this.onBoardComputer.setAvgSpeed(dashboardSerializationModel.getAvgSpeed());
        this.onBoardComputer.setMaxSpeed(dashboardSerializationModel.getMaxSpeed());
        this.onBoardComputer.setAvgCombustion(dashboardSerializationModel.getAvgFuel());
        this.onBoardComputer.setMaxCombustion(dashboardSerializationModel.getMaxFuel());
        this.onBoardComputer.setMaxSpeed(dashboardSerializationModel.getMaxSpeed());
        this.onBoardComputer.setJourneyDistance(dashboardSerializationModel.getJourneyDistance());
        this.onBoardComputer.setStartJourney(LocalDateTime.now().minusMinutes(dashboardSerializationModel.getJourneyMinutes()));
        this.onBoardComputer.setJourneyTime(LocalDateTime.now());
        this.dayCounter1 = dashboardSerializationModel.getDayCounter1();
        this.dayCounter2 = dashboardSerializationModel.getDayCounter2();
    }

    /**
     * Read from the database information.
     *
     * @return the observable list with the all of records from the database.
     */
    public ObservableList<DashboardSerializationModel> readFromDatabase() {
        SQL sql = new SQL();
        ObservableList<DashboardSerializationModel> observableList = null;
        try {
            observableList = sql.read(null);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return observableList;
    }

    /**
     * Read from XML file the DashboardSerializationModel.
     *
     * @param path the path to the file
     * @return the DashboardSerializationModel object
     */
    public DashboardSerializationModel readFromXML(Path path) {
        XML xmlReader = new XML();
        DashboardSerializationModel dashboardSerializationModel = null;
        try {
            dashboardSerializationModel = xmlReader.readFromXML(path);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return dashboardSerializationModel;
    }

    /**
     * Generate information from the app to export to the database.
     *
     * @return the DashboardSerializationModel object
     */
    public DashboardSerializationModel generateDatabaseRecord() {
        int min = 0;
        if (onBoardComputer.getStartJourney() != null && onBoardComputer.getJourneyTimeLocalDateTime() != null)
            min = (int)(Duration.between(onBoardComputer.getStartJourney(), onBoardComputer.getJourneyTimeLocalDateTime()).getSeconds() / 60);
        return new DashboardSerializationModel(0, onBoardComputer.getAvgSpeed(), onBoardComputer.getMaxSpeed(), onBoardComputer.getAvgCombustion(), onBoardComputer.getMaxCombustion(), onBoardComputer.getJourneyDistance(), min, (int)counter, dayCounter1, dayCounter2, new Date(System.currentTimeMillis()));
    }

    /**
     * Write to database actual state of information.
     */
    public void writeToDatabase() {
        SQL sql = new SQL();
        try {
            sql.write(null, generateDatabaseRecord());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write to XML file actual state of information.
     *
     * @param path the path to the file
     */
    public void writeToXml(Path path) {
        XML xml = new XML();
        try {
            xml.writeToXml(path, generateDatabaseRecord());
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     * Information if the up button on the keyboard is pressed.
     *
     * @return the boolean
     */
    public boolean isKeyUp() {
        return keyUp;
    }

    /**
     * Set the up button when is pressed or released.
     *
     * @param keyUp the key up
     */
    public void setKeyUp(boolean keyUp) {
        this.keyUp = keyUp;
    }

    /**
     * Information if the down button on the keyboard is pressed.
     *
     * @return the boolean
     */
    public boolean isKeyDown() {
        return keyDown;
    }

    /**
     * Set the down button when is pressed or released.
     *
     * @param keyDown the key down
     */
    public void setKeyDown(boolean keyDown) {
        this.keyDown = keyDown;
    }

    /**
     * Calculate the maximum speed in each gear.
     *
     * @param maxSpeed      the maximum speed of the car
     * @param numberOfGears the number of gears in the car
     */
    public void maxSpeedOnGears(short maxSpeed, byte numberOfGears) {
        this.maxSpeedOnGear = new ArrayList<Short>();
        this.maxSpeedOnGear.add((short) 0);
        this.maxSpeedOnGear.add(((short)(maxSpeed * 0.1)));
        this.maxSpeedOnGear.add(((short)(maxSpeed * 0.15)));
        this.maxSpeedOnGear.add(((short)(maxSpeed * 0.3)));
        if (numberOfGears == 5) {
            this.maxSpeedOnGear.add(((short)(maxSpeed * 0.5)));
        } else if (numberOfGears == 6) {
            this.maxSpeedOnGear.add(((short)(maxSpeed * 0.4)));
            this.maxSpeedOnGear.add(((short)(maxSpeed * 0.6)));
        }
        this.maxSpeedOnGear.add((maxSpeed));
    }

    /**
     * Gets maximum of speed of the actual gear.
     *
     * @return the maximum of speed in actual gear
     */
    public short getCurrentGearMaxSpeed() {
        return maxSpeedOnGear.get(actualGear);
    }

    /**
     * Gets minimum of speed of the actual gear.
     *
     * @return the minimum of speed in actual gear
     */
    public short getActualGearMinSpeed() {
        short speed = 0;
        if (actualGear > 1) {
            speed = maxSpeedOnGear.get(actualGear - 1);
            return (short) (speed - 0.6 * speed);
        }
        return speed;
    }

    /**
     * Gets lower gear maximum of speed. When you drive on for example 5 gear, method returns max value dor 4 gear.
     *
     * @return the maximum of speed in lower gear
     */
    public short getLowerGearMaxSpeed() {
        if(actualGear != 0)
            return maxSpeedOnGear.get(actualGear-1);
        return 0;
    }

    /**
     * Gets cruise control set speed.
     *
     * @return the cruise control speed
     */
    public short getCruiseControlSpeed() {
        return cruiseControlSpeed;
    }

    /**
     * Sets cruise control speed.
     *
     * @param cruiseControlSpeed the cruise control speed
     * @throws CruiseControlException the cruise control exception
     */
    public void setCruiseControlSpeed(short cruiseControlSpeed) throws CruiseControlException {
        if (cruiseControlSpeed < 80)
            throw new CruiseControlException("Too low speed. Cruise control works with minimal speed on 80 km/h.");
        this.cruiseControlSpeed = cruiseControlSpeed;
    }

    /**
     * Gets settings.
     *
     * @return the Settings object
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Is cruise control lights boolean.
     *
     * @return the boolean
     */
    public boolean isCruiseControlLights() {
        return cruiseControlLights;
    }

    /**
     * Sets cruise control lights.
     *
     * @param cruiseControlLights the cruise control lights boolean
     */
    public void setCruiseControlLights(boolean cruiseControlLights) {
        this.cruiseControlLights = cruiseControlLights;
    }

    /**
     * Sets settings.
     *
     * @param settings the Settings object
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * Gets actual speed.
     *
     * @return the actual speed
     */
    public short getSpeed() {
        return speed;
    }

    /**
     * Is left turn signal boolean.
     *
     * @return the boolean
     */
    public boolean isLeftTurnSignal() {
        return leftTurnSignal;
    }

    /**
     * Is right turn signal boolean.
     *
     * @return the boolean
     */
    public boolean isRightTurnSignal() {
        return rightTurnSignal;
    }

    /**
     * Is position lights boolean.
     *
     * @return the boolean
     */
    public boolean isPositionLights() {
        return positionLights;
    }


    /**
     * Is front fog lights boolean.
     *
     * @return the boolean
     */
    public boolean isFrontFogLights() {
        return frontFogLights;
    }

    /**
     * Is rear fog lights boolean.
     *
     * @return the boolean
     */
    public boolean isRearFogLights() {
        return rearFogLights;
    }

    /**
     * Gets total counter.
     *
     * @return the counter
     */
    public float getCounter() {
        return counter;
    }

    /**
     * Gets daily counter 1.
     *
     * @return the daily counter 1
     */
    public float getDayCounter1() {
        return dayCounter1;
    }

    /**
     * Gets daily counter 2.
     *
     * @return the daily counter 2
     */
    public float getDayCounter2() {
        return dayCounter2;
    }

    /**
     * Gets actual revs.
     *
     * @return the actual revs
     */
    public float getRevs() {
        return revs;
    }

    /**
     * Gets actual gear.
     *
     * @return the actual gear
     */
    public byte getActualGear() {
        return actualGear;
    }

    /**
     * Sets speed.
     *
     * @param speed the speed
     * @throws NegativeValueException the negative value exception
     */
    public void setSpeed(short speed) throws NegativeValueException {
        if (speed<0) {
            throw new NegativeValueException("Speed must be positive.");
        }
        this.speed = speed;
    }

    /**
     * Sets left turn signal (switch on/off).
     *
     * @param leftTurnSignal the left turn signal boolean
     * @throws TurnSignalException the turn signal exception
     */
    public void setLeftTurnSignal(boolean leftTurnSignal) throws TurnSignalException {
        if (rightTurnSignal) {
            throw new TurnSignalException("Right signal is on.");
        }
        this.leftTurnSignal = leftTurnSignal;
    }

    /**
     * Sets right turn signal (switch on/off).
     *
     * @param rightTurnSignal the right turn signal boolean
     * @throws TurnSignalException the turn signal exception
     */
    public void setRightTurnSignal(boolean rightTurnSignal) throws TurnSignalException {
        if (leftTurnSignal) {
            throw new TurnSignalException("Left signal is on.");
        }
        this.rightTurnSignal = rightTurnSignal;
    }

    /**
     * Sets position lights (switch on/off).
     *
     * @param positionLights the position lights
     */
    public void setPositionLights(boolean positionLights) {
        this.positionLights = positionLights;
    }

    /**
     * Sets front fog lights (switch on/off).
     *
     * @param frontFogLights the front fog lights
     */
    public void setFrontFogLights(boolean frontFogLights) {
        this.frontFogLights = frontFogLights;
    }

    /**
     * Sets rear fog lights (switch on/off).
     *
     * @param rearFogLights the rear fog lights
     */
    public void setRearFogLights(boolean rearFogLights) {
        this.rearFogLights = rearFogLights;
    }

    /**
     * Is low beam lights.
     *
     * @return the boolean
     */
    public boolean isLowBeamLights() {
        return lowBeamLights;
    }

    /**
     * Sets low beam lights (switch on/off).
     *
     * @param lowBeamLights the low beam lights
     */
    public void setLowBeamLights(boolean lowBeamLights) {
        this.lowBeamLights = lowBeamLights;
    }

    /**
     * Is high beam lights.
     *
     * @return the boolean
     */
    public boolean isHighBeamLights() {
        return highBeamLights;
    }

    /**
     * Sets high beam lights (switch on/off).
     *
     * @param highBeamLights the high beam lights
     */
    public void setHighBeamLights(boolean highBeamLights) {
        this.highBeamLights = highBeamLights;
    }

    /**
     * Sets total counter.
     *
     * @param counter the counter
     * @throws NegativeValueException the negative value exception
     */
    public void setCounter(float counter) throws NegativeValueException {
        if (counter < 0) {
            throw new NegativeValueException("Counter must be positive.");
        }
        this.counter = counter;
    }

    /**
     * Sets daily counter 1.
     *
     * @param dayCounter1 the daily counter 1
     * @throws NegativeValueException the negative value exception
     */
    public void setDayCounter1(float dayCounter1) throws NegativeValueException {
        if (dayCounter1 < 0) {
            throw new NegativeValueException("Counter must be positive.");
        }
        this.dayCounter1 = dayCounter1;
    }

    /**
     * Sets daily counter 2.
     *
     * @param dayCounter2 the daily counter 2
     * @throws NegativeValueException the negative value exception
     */
    public void setDayCounter2(float dayCounter2) throws NegativeValueException {
        if (dayCounter2 < 0) {
            throw new NegativeValueException("Counter must be positive.");
        }
        this.dayCounter2 = dayCounter2;
    }

    /**
     * Sets revs.
     *
     * @param revs the revs
     * @throws NegativeValueException the negative value exception
     */
    public void setRevs(float revs) throws NegativeValueException {
        if (revs < 0) {
            throw new NegativeValueException("Revs must be positive.");
        }
        this.revs = revs;
    }

    /**
     * Sets actual gear.
     *
     * @param actualGear    the actual gear
     * @param engineDisable the engine state, true if engine is turned off
     * @throws GearException the gear exception
     */
    public void setActualGear(byte actualGear, boolean engineDisable) throws GearException {
        gearSound();
        if(actualGear < maxSpeedOnGear.size() && engineDisable){
            if (actualGear == 0)
                this.actualGear = 0;
            else if (this.speed > this.getMaxSpeedOnGear().get(actualGear)) {
                this.keyUp = false;
                this.keyDown = false;
                throw new GearException("You cannot change the gear to "+actualGear+" at this speed!");
            }
            if(actualGear <= 1)
                this.actualGear = actualGear;
            else if(revs >= 1.999)
                this.actualGear = actualGear;
            else {
                this.keyUp = false;
                this.keyDown = false;
                throw new GearException("You cannot change the gear to " + actualGear + " at this speed!");
            }
        } else {
                if (actualGear <= this.settings.getNumberOfGears() && this.speed <= this.getMaxSpeedOnGear().get(actualGear))
                    this.actualGear = actualGear;
                else {
                    this.keyUp = false;
                    this.keyDown = false;
                    if (actualGear > this.settings.getNumberOfGears())
                        throw new GearException("You cannot change the gear to " + actualGear + ". You have " + this.settings.getNumberOfGears() + " speed gearbox" + "!");
                    throw new GearException("You cannot change the gear to " + actualGear + " at this speed!");
                }
        }
    }

    /**
     * Change cruise control speed.
     *
     * @param add True if you want to add speed to cruise control speed, false if you subtract speed in cruise control
     */
    public void changeCruiseControlSpeed(boolean add) {
        if (add) {
            this.cruiseControlSpeed += 5;
            if(this.cruiseControlSpeed > this.getCurrentGearMaxSpeed())
                this.cruiseControlSpeed = this.getCurrentGearMaxSpeed();
        }
        else {
            this.cruiseControlSpeed -= 5;
            if (this.cruiseControlSpeed < 80) {
                this.cruiseControlSpeed = 80;
            }
        }
    }

    /**
     * Add speed.
     *
     * @param value the value that you add to actual speed
     */
    public void addSpeed(int value) {
        speed += value;
        if(speed > settings.getMaxSpeed())
            speed = settings.getMaxSpeed();
    }

    /**
     * Subtract speed.
     *
     * @param value the value that you subtract from actual speed
     */
    public void subSpeed(int value) {
        speed -= value;
        if(speed < 0)
            speed = 0;
    }


    /**
     * Gets OnBoardComputer object.
     *
     * @return the OnBoardComputer object
     */
    public OnBoardComputer getOnBoardComputer() {
        return onBoardComputer;
    }

    /**
     * Sets OnBoardComputer.
     *
     * @param onBoardComputer the OnBoardComputer object
     */
    public void setOnBoardComputer(OnBoardComputer onBoardComputer) {
        this.onBoardComputer = onBoardComputer;
    }

    /**
     * Engine sound.
     *
     * @param shortVersion true if you want the short version, false is extended
     */
    public void engineSound(boolean shortVersion) {
        String path = "src/main/resources/org/Presentation/sound/engine";
        if (shortVersion)
            path += "Short";
        path += ".mp3";
//        AudioClip audioClip = new AudioClip(Paths.get(path).toUri().toString());
        AudioClip audioClip = new AudioClip(new File("sound/engine.mp3").toURI().toString());
        audioClip.play();
    }

    /**
     * Gear sound.
     */
    public void gearSound() {
        String path = "src/main/resources/org/Presentation/sound/gear.mp3";
//        AudioClip audioClip = new AudioClip(Paths.get(path).toUri().toString());
        AudioClip audioClip = new AudioClip(new File("sound/gear.mp3").toURI().toString());
        audioClip.play();
    }

    /**
     * Gets music player.
     *
     * @return the MusicPlayer object
     */
    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

}
