package org.Data;

import java.sql.Date;

/**
 * The class with data model used to serialize the main object into an XML file and database.
 */
public class DashboardSerializationModel {
    private int recordId;
    private float avgSpeed;
    private float maxSpeed;
    private float avgFuel;
    private float maxFuel;
    private float journeyDistance;
    private int journeyMinutes;
    private int counter;
    private float dayCounter1;
    private float dayCounter2;
    private Date recordDate;


    /**
     * Instantiates a new dashboard serialization model.
     *
     * @param recordId        the record id
     * @param avgSpeed        the average of speed
     * @param maxSpeed        the maximum of speed
     * @param avgFuel         the average fuel usage
     * @param maxFuel         the maximum fuel usage
     * @param journeyDistance the journey distance in kilometers
     * @param journeyMinutes  the journey time in minutes
     * @param counter         the total counter
     * @param dayCounter1     the daily counter 1
     * @param dayCounter2     the daily counter 2
     * @param recordDate      the record date
     */
    public DashboardSerializationModel(int recordId, float avgSpeed, float maxSpeed, float avgFuel, float maxFuel, float journeyDistance, int journeyMinutes, int counter, float dayCounter1, float dayCounter2, Date recordDate) {
        this.recordId = recordId;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.avgFuel = avgFuel;
        this.maxFuel = maxFuel;
        this.journeyDistance = journeyDistance;
        this.journeyMinutes = journeyMinutes;
        this.counter = counter;
        this.dayCounter1 = dayCounter1;
        this.dayCounter2 = dayCounter2;
        this.recordDate = recordDate;
    }

    /**
     * Instantiates a new Dashboard serialization model.
     */
    public DashboardSerializationModel() {
    }

    /**
     * Gets ID of record.
     *
     * @return the ID of record
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * Sets ID of record.
     *
     * @param recordId ID of record.
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * Gets average of speed.
     *
     * @return the average of speed
     */
    public float getAvgSpeed() {
        return avgSpeed;
    }

    /**
     * Sets average speed.
     *
     * @param avgSpeed the average of speed
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
        return maxSpeed;
    }

    /**
     * Gets average fuel usage.
     *
     * @return the average fuel usage
     */
    public float getAvgFuel() {
        return avgFuel;
    }

    /**
     * Gets maximum fuel usage.
     *
     * @return the maximum fuel usage
     */
    public float getMaxFuel() {
        return maxFuel;
    }

    /**
     * Gets journey distance in kilometers.
     *
     * @return the journey distance
     */
    public float getJourneyDistance() {
        return journeyDistance;
    }

    /**
     * Gets journey time in minutes.
     *
     * @return the journey time in minutes
     */
    public int getJourneyMinutes() {
        return journeyMinutes;
    }

    /**
     * Gets total counter.
     *
     * @return the total counter
     */
    public int getCounter() {
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
     * Gets record date.
     *
     * @return the record date
     */
    public Date getRecordDate() {
        return recordDate;
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
     * Sets average fuel usage.
     *
     * @param avgFuel the average fuel
     */
    public void setAvgFuel(float avgFuel) {
        this.avgFuel = avgFuel;
    }

    /**
     * Sets maximum fuel usage.
     *
     * @param maxFuel the maximum fuel usage
     */
    public void setMaxFuel(float maxFuel) {
        this.maxFuel = maxFuel;
    }

    /**
     * Sets journey distance in kilometers.
     *
     * @param journeyDistance the journey distance in kilometers
     */
    public void setJourneyDistance(float journeyDistance) {
        this.journeyDistance = journeyDistance;
    }

    /**
     * Sets journey time in minutes.
     *
     * @param journeyMinutes the journey time in minutes
     */
    public void setJourneyMinutes(int journeyMinutes) {
        this.journeyMinutes = journeyMinutes;
    }

    /**
     * Sets total counter.
     *
     * @param counter the total counter
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Sets daily counter 1.
     *
     * @param dayCounter1 the daily counter 1
     */
    public void setDayCounter1(float dayCounter1) {
        this.dayCounter1 = dayCounter1;
    }

    /**
     * Sets daily counter 2.
     *
     * @param dayCounter2 the daily counter 2
     */
    public void setDayCounter2(float dayCounter2) {
        this.dayCounter2 = dayCounter2;
    }

    /**
     * Sets record date.
     *
     * @param recordDate the record date
     */
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return "avgSpeed=" + avgSpeed +
                ", maxSpeed=" + maxSpeed +
                ", avgFuel=" + avgFuel +
                ", maxFuel=" + maxFuel +
                ", journeyDistance=" + journeyDistance +
                ", journeyMinutes=" + journeyMinutes +
                ", counter=" + counter +
                ", dayCounter1=" + dayCounter1 +
                ", dayCounter2=" + dayCounter2 +
                ", recordDate=" + recordDate;
    }
}