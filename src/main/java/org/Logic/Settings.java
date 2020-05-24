package org.Logic;

import java.io.Serializable;

/**
 * The type Settings.
 */
public class Settings implements Serializable {
    private short maxSpeed;
    private byte numberOfGears;
    private char engineType;
    private boolean autoTurnOnLowBeam;
    private boolean shufflePlay;
    private short dashboardLightingLevel;
    private short maxRevs;
    private String songsPath;

    /**
     * Instantiates a new Settings.
     */
    public Settings() {
        this.maxSpeed = 240;
        this.numberOfGears = 5;
        this.engineType = 'P';
        this.autoTurnOnLowBeam = true;
        this.shufflePlay = false;
        this.dashboardLightingLevel = 0;
        this.maxRevs = 8;
        this.songsPath = System.getenv("USERPROFILE") + "\\Music";
    }

    /**
     * Gets songs path.
     *
     * @return the songs path
     */
    public String getSongsPath() {
        return songsPath;
    }

    /**
     * Sets songs path.
     *
     * @param songsPath the songs path
     */
    public void setSongsPath(String songsPath) {
        this.songsPath = songsPath;
    }

    /**
     * Gets maximum of revs.
     *
     * @return the maximum of revs
     */
    public short getMaxRevs() {
        return maxRevs;
    }

    /**
     * Gets maximum of speed.
     *
     * @return the maximum of speed
     */
    public short getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets maximum of speed.
     *
     * @param maxSpeed the maximum of speed
     */
    public void setMaxSpeed(short maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Gets number of gears.
     *
     * @return the number of gears
     */
    public byte getNumberOfGears() {
        return numberOfGears;
    }

    /**
     * Sets number of gears.
     *
     * @param numberOfGears the number of gears
     */
    public void setNumberOfGears(byte numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    /**
     * Gets engine type.
     *
     * @return the engine type
     */
    public char getEngineType() {
        return engineType;
    }

    /**
     * Sets engine type.
     *
     * @param engineType the engine type
     */
    public void setEngineType(char engineType) {
        this.engineType = engineType;
        if (engineType == 'P')
            this.maxRevs = 8;
        else this.maxRevs = 6;
    }

    /**
     * Is auto turn on low beam boolean.
     *
     * @return the boolean
     */
    public boolean isAutoTurnOnLowBeam() {
        return autoTurnOnLowBeam;
    }

    /**
     * Sets auto turn on low beam.
     *
     * @param autoTurnOnLowBeam the auto turn on low beam
     */
    public void setAutoTurnOnLowBeam(boolean autoTurnOnLowBeam) {
        this.autoTurnOnLowBeam = autoTurnOnLowBeam;
    }

    /**
     * Is shuffle play boolean.
     *
     * @return the boolean
     */
    public boolean isShufflePlay() {
        return shufflePlay;
    }

    /**
     * Sets shuffle play.
     *
     * @param shufflePlay the shuffle play
     */
    public void setShufflePlay(boolean shufflePlay) {
        this.shufflePlay = shufflePlay;
    }

    /**
     * Gets dashboard lighting level.
     *
     * @return the dashboard lighting level
     */
    public short getDashboardLightingLevel() {
        return dashboardLightingLevel;
    }

    /**
     * Sets dashboard lighting level.
     *
     * @param dashboardLightingLevel the dashboard lighting level
     */
    public void setDashboardLightingLevel(short dashboardLightingLevel) {
        this.dashboardLightingLevel = dashboardLightingLevel;
    }
}