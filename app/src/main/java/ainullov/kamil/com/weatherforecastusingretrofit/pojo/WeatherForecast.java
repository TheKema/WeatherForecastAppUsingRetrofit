package ainullov.kamil.com.weatherforecastusingretrofit.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherForecast {
    @SerializedName("list")
    private List<WeatherDay> items;

    public WeatherForecast(List<WeatherDay> items) {
        this.items = items;
    }

    public List<WeatherDay> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < items.size(); i++) {
            string += items.get(i).dtTxt + ", " + items.get(i).main.temp + "\n";
        }
        return string;
    }
}
