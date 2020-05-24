package org.Logic;

import org.Presentation.UiController;

/**
 * The Speed thread class.
 */
public class SpeedThread extends Thread {
    private final UiController uiController;
    private boolean engineRunning;
    private boolean animateEngineSpeedRevsToZero;
    private final Dashboard dashboard;
    private final OnBoardComputer onBoardComputer;
    private final int startAfter;

    /**
     * Instantiates a new Speed thread.
     *
     * @param uiController the ui controller (tui/gui)
     * @param startAfter   the start after, after the time we can drive the car
     */
    public SpeedThread(UiController uiController, int startAfter) {
        this.uiController = uiController;
        this.startAfter = startAfter;
        this.dashboard = uiController.getDashboard();
        this.onBoardComputer = this.dashboard.getOnBoardComputer();
    }

    /**
     * Sets engine running.
     *
     * @param running the running
     */
    public void setEngineRunning(boolean running) {
        this.engineRunning = running;
    }


    /**
     * Gets engine running boolean
     *
     * @return boolean
     */
    public boolean isEngineRunning() {
        return engineRunning;
    }

    /**
     * Sets animate engine speed revs to zero.
     *
     * @param animateEngineSpeedToZero the animate engine speed to zero
     */
    public void setAnimateEngineSpeedRevsToZero(boolean animateEngineSpeedToZero) {
        this.animateEngineSpeedRevsToZero = animateEngineSpeedToZero;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        float distance;
        try {
            Thread.sleep(startAfter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!engineRunning) {
            while((dashboard.getSpeed() > 0 || dashboard.getRevs() > 0 ) && animateEngineSpeedRevsToZero)  {
                if (dashboard.getSpeed() > 0) {
                    dashboard.subSpeed(1);
                }
                if (dashboard.getRevs() > 0 && dashboard.getRevs() < 1) {
                    try {
                        float newRevs = dashboard.getRevs() - 0.1f;
                        if (newRevs < 0)
                            newRevs = 0;
                        dashboard.setRevs(newRevs);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        dashboard.setRevs(((dashboard.getSettings().getMaxRevs() - 1) * (dashboard.getSpeed() / (float) dashboard.getCurrentGearMaxSpeed())));
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                }
                if (System.currentTimeMillis() - startTime >= 1000) {
                    startTime = System.currentTimeMillis();
                    distance = dashboard.getSpeed() * (1f / 3600f);
                    try {
                        dashboard.setCounter(dashboard.getCounter() + distance);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                    try {
                        dashboard.setDayCounter1(dashboard.getDayCounter1() + distance);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                    try {
                        dashboard.setDayCounter2(dashboard.getDayCounter2() + distance);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (uiController) {
                    uiController.refresh();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }
        float coefficient = dashboard.getSettings().getEngineType() == 'P' ? 3.5f : 4.4f;
        float maxSpeed = 0.0f;
        float maxCombustion = 0.0f;
        long avgSpeed = 0;
        long avgRevs = 0;
        float divideIter = 1;
        float revs = 0.0f;
        float gearMaxSpeed = 0;
        while(engineRunning) {
            synchronized (uiController) {
                if ((dashboard.isKeyUp() && dashboard.getSpeed() < dashboard.getCurrentGearMaxSpeed() && dashboard.getSpeed() >= dashboard.getActualGearMinSpeed()) || (dashboard.getCruiseControlSpeed() > dashboard.getSpeed()) && dashboard.isCruiseControlLights()) {
                    dashboard.addSpeed(1);
                } else if (dashboard.isKeyDown() && dashboard.getSpeed() >= 3) {
                    dashboard.subSpeed(3);
                    if (dashboard.isCruiseControlLights())
                        uiController.switchOffCruiseControl();
                } else {
                    if (!uiController.getDashboard().isCruiseControlLights())
                        dashboard.subSpeed(1);
                    else if (dashboard.getSpeed() > dashboard.getCruiseControlSpeed())
                        dashboard.subSpeed(1);
                    else
                        dashboard.addSpeed(1);
                }
                try {
                    if(dashboard.getActualGear() != 0) {
                        gearMaxSpeed = dashboard.getCurrentGearMaxSpeed();
                    }
                    if (dashboard.getSpeed() != 0) {
                        revs = (dashboard.getSettings().getMaxRevs()) * (dashboard.getSpeed() / gearMaxSpeed);
                        if (revs < 0.9f)
                            revs = 0.9f;
                        if (dashboard.getRevs() < 1.0f && dashboard.getActualGear() > 1) {
                            uiController.engineStartStop(false, true);
                        }
                    }
                    else {
                        if (revs < 0.9f) {;
                            revs += 0.1f;
                        }
                        else {
                            revs -= 0.04f;
                        }
                    }
                    dashboard.setRevs(revs);
                } catch (InterruptedException ignored) {
                } catch (TurnSignalException | NegativeValueException e) {
                    e.printStackTrace();
                }
                if(System.currentTimeMillis() - startTime >= 1000) {
                    startTime = System.currentTimeMillis();
                    distance = dashboard.getSpeed() * (1f/3600f);
                    try {
                        dashboard.setCounter(dashboard.getCounter() + distance);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                    try {
                        dashboard.setDayCounter1(dashboard.getDayCounter1() + distance);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                    try {
                        dashboard.setDayCounter2(dashboard.getDayCounter2() + distance);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                    onBoardComputer.setJourneyDistance(onBoardComputer.getJourneyDistance() + distance);
                    onBoardComputer.setJourneyTime(onBoardComputer.getJourneyTimeLocalDateTime().plusSeconds(1));

                    float actualSpeed = dashboard.getSpeed();
                    if (actualSpeed > maxSpeed) {
                        maxSpeed = actualSpeed;
                        onBoardComputer.setMaxSpeed(maxSpeed);
                    }
                    avgSpeed += actualSpeed;
                    onBoardComputer.setAvgSpeed(avgSpeed / divideIter);

                    float actualCombustion = coefficient * dashboard.getRevs();
                    if (actualCombustion > maxCombustion) {
                        maxCombustion = actualCombustion;
                        onBoardComputer.setMaxCombustion(maxCombustion);
                    }
                    avgRevs += actualCombustion;
                    onBoardComputer.setAvgCombustion(avgRevs / divideIter);
                    divideIter += 1;
                }
                uiController.refresh();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
    }
}