package View.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Label game1;

    @FXML
    private Label game2;

    @FXML
    private Label game3;

    @FXML
    private Label game4;
    @FXML
    private ImageView scramble;

    private void openWebPage(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (Exception e) {
            // Xử lý lỗi nếu không thể mở trang web
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to open web page");
            alert.setContentText("Please visit " + url + " in your web browser.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game1.setOnMouseClicked(mouseEvent -> {
            openWebPage("https://www.gamestolearnenglish.com/grammar-bubbles/");
        });
        game2.setOnMouseClicked(mouseEvent -> {
            openWebPage("https://www.gamestolearnenglish.com/monster-vocab/");
        });
        game3.setOnMouseClicked(mouseEvent -> {
            openWebPage("https://www.gamestolearnenglish.com/monster-phrases/");
        });
        game4.setOnMouseClicked(mouseEvent -> {
            openWebPage("https://www.gamestolearnenglish.com/phrasal-verbs/");
        });
        scramble.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ScrambleStartUI.fxml"));
                Parent root = loader.load();

                Stage newStage = new Stage();
                Scene scene = new Scene(root);
                newStage.setTitle("Scramble Word");
                newStage.setScene(scene);
                newStage.initStyle(StageStyle.TRANSPARENT);
                scene.setFill(Color.TRANSPARENT);
                newStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}