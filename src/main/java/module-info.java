/**
 * Defines the dashboard application.
 */
module org.ProgKompo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires eu.hansolo.medusa;
    requires org.controlsfx.controls;
    requires javafx.media;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires com.jfoenix;
    requires CustomStage;
    requires FX.BorderlessScene;
    requires JCDP;

    opens org.Presentation to javafx.fxml;
    opens org.Data to javafx.base;
    exports org.Presentation;
}