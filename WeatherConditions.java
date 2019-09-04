import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

public class WeatherConditions {
    private int id;
    private String name;

    @SerializedName("main")
    private Map<String, Float> measurements;

    /**
     * Get the id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the temperature
     * @return the temperature
     */
    public float getTemperature() {
        return measurements.get("temp");
    }

    /**
     * Get the pressure
     * @return the pressure
     */
    public float getPressure() {
        return measurements.get("pressure");
    }

    /**
     * Get the humidity
     * @return the humidity
     */
    public float getHumidity() {
        return measurements.get("humidity");
    }

    /**
     * Get the min temperature
     * @return the min temperature
     */
    public float getMinTemperature() {
        return measurements.get("temp_min");
    }

    /**
     * Get the max temperature
     * @return the max temperature
     */
    public float getMaxTemperature() {
        return measurements.get("temp_max");
    }

    public static void main(String[] args) {
        // create a standard input stream to receive user's input
        Scanner sc = new Scanner(System.in);

        // set the querying url
        String url = "https://api.openweathermap.org/data/2.5/weather";
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
                    WeatherConditions conditions = gson.fromJson(responseBody, WeatherConditions.class);

                    // print out the information
                    System.out.println("ID: " + conditions.getId());
                    System.out.println("City Name: " + conditions.getName());
                    if (tempUnit.equalsIgnoreCase("C")) {
                        System.out.println("Temperature: " + conditions.getTemperature() + "C");
                    }
                    else {
                        System.out.println("Temperature: " + conditions.getTemperature() + "F");
                    }
                    System.out.println("Pressure: " + conditions.getPressure() + "hPa");
                    System.out.println("Humidity: " + conditions.getHumidity() + "%");
                    if (tempUnit.equalsIgnoreCase("C")) {
                        System.out.println("Min Temperature: " + conditions.getMinTemperature() + "C");
                    }
                    else {
                        System.out.println("Min Temperature: " + conditions.getMinTemperature() + "F");
                    }
                    if (tempUnit.equalsIgnoreCase("C")) {
                        System.out.println("Max Temperature: " + conditions.getMaxTemperature() + "C");
                    }
                    else {
                        System.out.println("Max Temperature: " + conditions.getMaxTemperature() + "F");
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
