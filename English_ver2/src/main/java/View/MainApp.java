package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(final Stage primarystage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/View/PreLoaderUI.fxml"));

        primarystage.setTitle("English learning Application");

        Scene scene = new Scene(root);
        primarystage.setScene(scene);

        primarystage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        primarystage.show();
    }
}