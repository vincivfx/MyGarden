module com.mygarden.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires ormlite.jdbc;
    requires java.sql;
    requires javafx.graphics;

    exports com.mygarden.app;
    exports com.mygarden.app.models;
    opens com.mygarden.app.controllers to javafx.fxml;
    opens com.mygarden.app.temp to javafx.fxml;
    opens com.mygarden.app.models to ormlite.jdbc;
    opens com.mygarden.app to javafx.fxml, ormlite.jdbc;
}