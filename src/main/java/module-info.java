module com.example.mapsocialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.mapsocialnetworkgui to javafx.fxml;
    exports com.example.mapsocialnetworkgui;
    exports com.example.mapsocialnetworkgui.controller;
    opens com.example.mapsocialnetworkgui.controller to javafx.fxml;
    opens com.example.mapsocialnetworkgui.domain to javafx.base;
}