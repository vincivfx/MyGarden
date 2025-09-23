module com.mygarden.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires ormlite.jdbc;
    requires java.sql;

    exports com.mygarden.app;
    exports com.mygarden.app.models;
    opens com.mygarden.app to javafx.fxml;
    opens com.mygarden.app.models to ormlite.jdbc;
}