import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class WeatherForecastItem {
    @SerializedName("main")
    private Map<String, Float> measurements;
    private List<WeatherDescription> weather;
    private Map<String, Float> wind;
    private String dt_txt;

    public float getTemperature() {
        return measurements.get("temp");
    }

    public String getDescription() {
        return weather.get(0).getDescription();
    }

    public float getWindSpeed() {
        return wind.get("speed");
    }

    public String getDateTime() {
        return dt_txt;
    }
}
