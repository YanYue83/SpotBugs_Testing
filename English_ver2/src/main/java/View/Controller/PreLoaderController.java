package View.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PreLoaderController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane bg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new PreLoader().start();

    }

    class PreLoader extends Thread {
        @Override
        public void run() {

            try {
                Thread.sleep(2300);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/View/HomeUI.fxml"));

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("English learning application");

                        // giúp kéo thả vị trí cửa sổ
                        root.setOnMousePressed(event -> {
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });

                        root.setOnMouseDragged(event -> {
                            stage.setX(event.getScreenX() - xOffset);
                            stage.setY(event.getScreenY() - yOffset);
                        });

                        stage.setScene(scene);
                        scene.getStylesheets().add(getClass().getResource("/View/style.css").toExternalForm());
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getRoot().setStyle("-fx-background-radius: 50; -fx-border-radius: 50;");
                        scene.setFill(Color.TRANSPARENT);
                        stage.show();
                        bg.getScene().getWindow().hide();
                    }
                });

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
