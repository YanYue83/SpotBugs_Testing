package View.Controller;

import Service.DateTimeApi;
import Service.DateTimeService;
import Service.WeatherData;
import Service.WeatherDataService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private final DateTimeApi dateTimeApi = new DateTimeService();
    private final WeatherDataService weatherDataService = new WeatherDataService("062d92a2646152d39eb7845a608226cb");

    @FXML
    private Button btnClose;
    @FXML
    private Button btnDic;
    @FXML
    private Button btnGame;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnMore;
    @FXML
    private Button btnTrans;
    @FXML
    private Label dateTimeLabel;

    @FXML
    private Label cityLabel, temperatureLabel, weatherTypeLabel, humidityLabel, windLabel;
    @FXML
    private ImageView iconWeather;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;

    @FXML
    private AnchorPane homePane;
    @FXML
    private Rectangle shadow;
    private Node currentComponent;

    /**
     * Xử lí ẩn hiện các pane chức năng trên giao diện chính.
     */
    @FXML
    private void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(getClass().getResource(path));
            setNode(component);
            currentComponent = component;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setNode(Node node) {
        hideComponent(); // Ẩn component hiện tại trước khi hiển thị component mới
        homePane.getChildren().add(node);

    }

    private void hideComponent() {
        if (currentComponent != null) {
            homePane.getChildren().remove(currentComponent);
            currentComponent = null;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchWeatherData("Hanoi");
        updateTime();

        setupButtonEvent(btnDic, "/View/DictionaryUI.fxml", 90);
        setupButtonEvent(btnTrans, "/View/TranslateUI.fxml", 250);
        setupButtonEvent(btnGame, "/View/HomeGameUI.fxml", 330);
        setupButtonEvent(btnMore, "/View/AddWordUI.fxml", 170);

        btnHome.setOnAction(actionEvent -> hideComponent());
        btnHome.setOnMouseReleased(event -> shadow.setLayoutY(10));
        btnClose.setOnMouseClicked(e -> System.exit(0));

        searchButton.setOnAction(event -> {
            String city = searchTextField.getText();
            if (!city.isEmpty()) {
                fetchWeatherData(city);
            }
        });

    }

    /**
     * Xử lí dữ liệu thời tiết từ api.
     */
    private void fetchWeatherData(String city) {
        WeatherData weatherData = weatherDataService.getWeatherData(city);
        if (weatherData != null) {
            displayWeatherData(weatherData);
        } else {
            // Xử lý trường hợp lỗi
            cityLabel.setText("");
            temperatureLabel.setText("");
            weatherTypeLabel.setText("City not found!");
            humidityLabel.setVisible(false);
            windLabel.setVisible(false);
            iconWeather.setVisible(false);
        }
    }

    private void displayWeatherData(WeatherData weatherData) {
        cityLabel.setText(weatherData.getName());
        // xu li nhiet do vi api tra ve do F
        double temperatureFahrenheit = weatherData.getMain().getTemp();
        double temperatureCelsius = temperatureFahrenheit - 273.15;

        temperatureLabel.setText(String.format("%.1f", temperatureCelsius) + " °C");
        weatherTypeLabel.setText(weatherData.getWeather()[0].getDescription());
        humidityLabel.setText("Humidity: " + weatherData.getMain().getHumidity() + "%");
        windLabel.setText("Wind Speed: " + weatherData.getWind().getSpeed() + " m/s");

        String iconCode = weatherData.getWeather()[0].getIcon();
        updateWeatherIcon(iconCode);

        humidityLabel.setVisible(true);
        windLabel.setVisible(true);
        iconWeather.setVisible(true);

    }

    private void updateWeatherIcon(String iconCode) {
        String apiKey = "062d92a2646152d39eb7845a608226cb";
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + ".png?appid=" + apiKey;

        // Tạo ImageView và thiết lập ảnh từ URL
        iconWeather.setImage(new Image(iconUrl));
    }

    /**
     * Hàm set bóng thanh bên.
     */
    private void setupButtonEvent(Button button, String path, double layoutY) {
        button.setOnAction(event -> showComponent(path));
        button.setOnMouseReleased(event -> shadow.setLayoutY(layoutY));
    }

    /**
     * Cập nhật ngày.
     */
    private void updateTime() {
        String date = this.dateTimeApi.getDateTime();
        dateTimeLabel.setText(date);
        dateTimeLabel.setStyle("-fx-alignment: center;");
    }

    /**
     * Xử lí khi nhấn vào tips ở bên phải.
     */
    @FXML
    private void handleClickHelp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HelpUI.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setTitle("Tips");
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


