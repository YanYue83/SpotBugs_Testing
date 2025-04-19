package Service;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataService {
    private final String apiKey;

    public WeatherDataService(String apiKey) {
        this.apiKey = apiKey;
    }

    public WeatherData getWeatherData(String city) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                // Sử dụng Gson để phân tích dữ liệu JSON
                Gson gson = new Gson();
                return gson.fromJson(reader, WeatherData.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
