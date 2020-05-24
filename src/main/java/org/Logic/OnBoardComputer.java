package org.Logic;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type On-board Computer.
 */
public class OnBoardComputer implements Comparable<OnBoardComputer>, Serializable {
    private float avgSpeed;
    private float maxSpeed;
    private LocalDateTime journeyTime = null;
    private LocalDateTime startJourney = null;
    private float journeyDistance;
    private float avgCombustion;
    private float maxCombustion;


    /**
     * Sets journey time from minutes.
     *
     * @param journeyMinutes the journey time in minutes
     */
    public void setJourneyTimeFromMinutes(int journeyMinutes) {
        this.startJourney = LocalDateTime.now().minusMinutes(journeyMinutes);
    }

    /**
     * Gets journey time in minutes.
     *
     * @return the journey time in minutes
     */
    public int getJourneyTimeInMinutes() {
        int min = 0;
        if (startJourney != null && journeyTime != null)
            min = (int)(Duration.between(startJourney, journeyTime).getSeconds() / 60);
        return min;
    }

    /**
     * Gets start journey time.
     *
     * @return the start journey time
     */
    public LocalDateTime getStartJourney() {
        return startJourney;
    }

    /**
     * Sets start journey time.
     *
     * @param startJourney the start journey time
     */
    public void setStartJourney(LocalDateTime startJourney) {
        this.startJourney = startJourney;
    }

    /**
     * Gets maximum of combustion.
     *
     * @return the maximum of combustion
     */
    public float getMaxCombustion() {
        return (Math.round(maxCombustion * 10f) / 10.0f);
    }

    /**
     * Sets maximum of combustion.
     *
     * @param maxCombustion the maximum of combustion
     */
    public void setMaxCombustion(float maxCombustion) {
        this.maxCombustion = maxCombustion;
    }

    /**
     * Gets average of speed.
     *
     * @return the average of speed
     */
    public float getAvgSpeed() {
        return (Math.round(avgSpeed * 10f) / 10.0f);
    }

    /**
     * Sets average of speed.
     *
     * @param avgSpeed the  average of speed
     */
    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    /**
     * Gets maximum of speed.
     *
     * @return the maximum of speed
     */
    public float getMaxSpeed() {
        return (Math.round(maxSpeed * 10f) / 10.0f);
    }

    /**
     * Sets maximum of speed.
     *
     * @param maxSpeed the maximum of speed
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Gets journey time.
     *
     * @return the journey time in LocalDateTime object.
     */
    public LocalDateTime getJourneyTimeLocalDateTime() {
        return this.journeyTime;
    }

    /**
     * Gets journey time.
     *
     * @return the journey time in string
     */
    public String getJourneyTime() {
        if(journeyTime != null)
        {
            int min = (int)(Duration.between(startJourney, journeyTime).getSeconds() / 60);
            int h = min / 60;
            min = min % 60;
            String hour = Integer.toString(h);
            String minute = Integer.toString(min);
            if (hour.length() == 1) {
                hour = '0' + hour;
            }
            if (minute.length() == 1) {
                minute = '0' + minute;
            }
            return hour + ":" + minute;
        }
        return "00:00";
    }

    /**
     * Start journey time.
     */
    public void startJourneyTime() {
        if(journeyTime == null && startJourney == null)
            this.startJourney = LocalDateTime.now();
            this.journeyTime = LocalDateTime.now();
    }

    /**
     * Gets journey distance.
     *
     * @return the journey distance
     */
    public float getJourneyDistance() {
        return journeyDistance;
    }

    /**
     * Sets journey time.
     *
     * @param journeyTime the journey time
     */
    public void setJourneyTime(LocalDateTime journeyTime) {
        this.journeyTime = journeyTime;
    }

    /**
     * Sets journey distance.
     *
     * @param journeyDistance the journey distance
     */
    public void setJourneyDistance(float journeyDistance) {
        this.journeyDistance = journeyDistance;
    }

    /**
     * Gets average of fuel combustion.
     *
     * @return the average of fuel combustion
     */
    public float getAvgCombustion() {
        return (Math.round(avgCombustion * 10f) / 10.0f);
    }

    /**
     * Sets average of fuel combustion.
     *
     * @param avgCombustion the average of fuel combustion
     */
    public void setAvgCombustion(float avgCombustion) {
        this.avgCombustion = avgCombustion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnBoardComputer obc = (OnBoardComputer) o;
        return Objects.equals(avgSpeed, obc.avgSpeed) &&
                Objects.equals(maxSpeed, obc.maxSpeed) &&
                Objects.equals(journeyTime, obc.journeyTime) &&
                Objects.equals(journeyDistance, obc.journeyDistance) &&
                Objects.equals(avgCombustion, obc.avgCombustion);
    }

    @Override
    public int compareTo(OnBoardComputer obc) {
        if (equals(obc)) {
            return 0;
        }
        int jd = Float.compare(journeyDistance, obc.journeyDistance);
        if (jd == 0) {
            int jt = journeyTime.compareTo(obc.journeyTime);
            if (jt == 0) {
                int as =  Float.compare(avgSpeed,obc.avgSpeed);
                if (as == 0) {
                    int ac = Float.compare(avgCombustion, obc.avgCombustion);
                    if (ac == 0) {
                            return Float.compare(maxSpeed, obc.maxSpeed);
                    }
                    return ac;
                }
                return as;
            }
            return jt;
        }
        return jd;
    }
}