module se.chalmers.agile.mygarden.mygarden {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens se.chalmers.agile.mygarden.mygarden to javafx.fxml;
    exports se.chalmers.agile.mygarden.mygarden;
}