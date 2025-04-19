package Service;

public class WeatherData {
    String icon;
    String name;
    MainData main;
    Weather[] weather;
    WindData wind;

    public WindData getWind() {
        return wind;
    }

    public void setWind(WindData wind) {
        this.wind = wind;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainData getMain() {
        return main;
    }

    public void setMain(MainData main) {
        this.main = main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    /**
     * Dữ liệu chính.
     */
    public static class MainData {
        double temp;
        int humidity;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }

    /**
     * Dữ liệu kiểu thời tiết
     */
    public static class Weather {
        String description;
        String icon;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    /**
     * Dữ liệu về gió.
     */
    public static class WindData {
        double speed;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }
}
