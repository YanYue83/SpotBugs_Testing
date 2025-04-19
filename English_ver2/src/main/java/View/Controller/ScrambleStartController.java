package View.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ScrambleStartController implements Initializable {
    @FXML
    private Button btnHelp;

    @FXML
    private Button exit;

    @FXML
    private AnchorPane starterPane;
    @FXML
    private Pane guide;


    @FXML
    private void handleClickStart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ScrambleUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            starterPane.getChildren().setAll(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guide.setVisible(false);
        btnHelp.setOnAction(event -> {
            guide.setVisible(true);
        });
        exit.setOnAction(event -> {
            guide.setVisible(false);
        });
    }
}
