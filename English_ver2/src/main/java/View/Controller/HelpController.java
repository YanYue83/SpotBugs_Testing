package View.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    private final Image[] images = new Image[]{
            new Image(getClass().getResourceAsStream("/GuideForUser/nha1.png")),
            new Image(getClass().getResourceAsStream("/GuideForUser/nha2.png")),
            new Image(getClass().getResourceAsStream("/GuideForUser/nha3.png")),
            new Image(getClass().getResourceAsStream("/GuideForUser/nha4.png")),
            new Image(getClass().getResourceAsStream("/GuideForUser/nha5.png"))
    };
    @FXML
    private Label pageNum;
    @FXML
    private Button exit;
    @FXML
    private ImageView imgView;
    @FXML
    private Button nextBtn;
    @FXML
    private Button preBtn;
    private int currentImageIndex = 0;


    public HelpController() {
    }

    private void closeCurrentWindow() {
        Window window = exit.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updatePageNumberLabel();

        // Khởi tạo ImageView để hiển thị hình ảnh
        imgView.setImage(images[currentImageIndex]);

        exit.setOnAction(event -> {
            closeCurrentWindow();
        });

        nextBtn.setOnAction(event -> {
            showNextImage();
        });

        preBtn.setOnAction(event -> {
            showPreviousImage();
        });
    }

    private void showNextImage() {
        if (currentImageIndex < images.length - 1) {
            currentImageIndex++;
            imgView.setImage(images[currentImageIndex]);
            updatePageNumberLabel();
        }
    }

    private void showPreviousImage() {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            imgView.setImage(images[currentImageIndex]);
            updatePageNumberLabel();
        }
    }

    private void updatePageNumberLabel() {
        pageNum.setText("Trang " + (currentImageIndex + 1) + "/" + images.length);
    }
}