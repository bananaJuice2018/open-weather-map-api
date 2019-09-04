import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

public class WeatherForecast {
    @SerializedName("list")
    private List<WeatherForecastItem> forecastItems;

    private List<WeatherForecastItem> getForecastItems() {
        return forecastItems;
    }

    public static void main(String[] args) {
        // create a standard input stream to receive user's input
        Scanner sc = new Scanner(System.in);

        // set the querying url
        String url = "https://api.openweathermap.org/data/2.5/forecast";
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();

        // get the city name
        System.out.print("Enter the city name: ");
        String cityName = sc.nextLine();

        // get the temperature unit
        System.out.print("Choose the temperature unit (C/F): ");
        String tempUnit = sc.nextLine();

        String unit;
        if (tempUnit.equalsIgnoreCase("C")) {
            unit = Tokens.CELSIUS_UNIT;
        }
        else {
            unit = Tokens.FAHRENHEIT_UNIT;
        }

        try {
            // construct the querying string
            String query = String.format("q=%s&apiKey=%s&units=%s",
                    URLEncoder.encode(cityName, charset),
                    URLEncoder.encode(Tokens.APP_ID, charset),
                    URLEncoder.encode(unit, charset));

            try {
                // connect to server and get response
                URLConnection connection = new URL(url + "?" + query).openConnection();
                connection.setRequestProperty("Accept-Charset", charset);
                InputStream response = connection.getInputStream();

                try (Scanner scanner = new Scanner(response)) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    // construct the WeatherConditions object from response json message
                    Gson gson = new Gson();
                    WeatherForecast forecast = gson.fromJson(responseBody, WeatherForecast.class);

                    System.out.println("The forecasts for 3 hour increments during following 5 days:");

                    for (WeatherForecastItem item : forecast.getForecastItems()) {
                        if (tempUnit.equalsIgnoreCase("C")) {
                            System.out.println(
                                    "[" + item.getDateTime() + "]" +
                                            "Temperature: " + item.getTemperature() + "C; " +
                                            "Status: " + item.getDescription() + "; " +
                                            "Wind speed: " + item.getWindSpeed() + "m/s");
                        }
                        else {
                            System.out.println(
                                    "[" + item.getDateTime() + "]" +
                                            "Temperature: " + item.getTemperature() + "F; " +
                                            "Status: " + item.getDescription() + "; " +
                                            "Wind speed: " + item.getWindSpeed() + "m/s");
                        }
                    }
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
