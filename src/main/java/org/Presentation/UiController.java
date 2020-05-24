package org.Presentation;

import org.Data.Serialization;
import org.Logic.Dashboard;
import org.Logic.TurnSignalException;
import java.io.IOException;

/**
 * The type Ui controller.
 */
public class UiController {
    /**
     * The Dashboard.
     */
    protected Dashboard dashboard;

    /**
     * Instantiates a new Ui controller.
     */
    public UiController() {
//        this.dashboard = new Dashboard();                     //fix the serialization
        try {
            this.dashboard = Serialization.readDashboard();
            this.dashboard.DashboardAfterSerialization();
        } catch (IOException | ClassNotFoundException e) {
            this.dashboard = new Dashboard();
        }
    }

    /**
     * Gets dashboard.
     *
     * @return the dashboard
     */
    public Dashboard getDashboard() {
        return dashboard;
    }

    /**
     * Refresh.
     */
    public void refresh() {}

    /**
     * Engine start stop.
     *
     * @param enable      the enable
     * @param engineError the engine error
     * @throws TurnSignalException  the turn signal exception
     * @throws InterruptedException the interrupted exception
     */
    public void engineStartStop(boolean enable, boolean engineError) throws TurnSignalException, InterruptedException {}

    /**
     * Reload dashboard after settings.
     *
     * @param editMusicPlayer the edit music player. True if you want to edit music player, false if not
     * @throws InterruptedException the interrupted exception
     */
    public void reloadDashboardAfterSettings(boolean editMusicPlayer) throws InterruptedException {}

    /**
     * Switch off cruise control.
     */
    public void switchOffCruiseControl() {}
}