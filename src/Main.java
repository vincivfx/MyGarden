import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button bouton = new Button("Clique moi !");
        bouton.setOnAction(e -> System.out.println("Bouton cliqué !"));
        StackPane root = new StackPane(bouton);
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setTitle("JavaFX VS Code");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
