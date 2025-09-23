module com.mygarden.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    exports com.mygarden.app;
    opens com.mygarden.app to javafx.fxml;
}