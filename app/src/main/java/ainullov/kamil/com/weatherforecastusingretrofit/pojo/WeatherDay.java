package ainullov.kamil.com.weatherforecastusingretrofit.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherDay {
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("weather")
    public List<Weather> weather = null;
    @SerializedName("base")
    public String base;
    @SerializedName("main")
    public Main main;
    @SerializedName("visibility")
    public int visibility;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public long dt;
    @SerializedName("dt_txt")
    public String dtTxt;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public int cod;

    public class Coord {
        @SerializedName("lon")
        public float lon;
        @SerializedName("lat")
        public float lat;
    }

    public class Clouds {
        @SerializedName("all")
        public int all;
    }

    public class Main {
        @SerializedName("temp")
        public float temp;
        @SerializedName("pressure")
        public float pressure;
        @SerializedName("humidity")
        public int humidity;
        @SerializedName("temp_min")
        public float tempMin;
        @SerializedName("temp_max")
        public float tempMax;
    }

    public class Weather {
        @SerializedName("id")
        public int id;
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;
    }

    public class Wind {
        @SerializedName("speed")
        public float speed;
        @SerializedName("deg")
        public float deg;
    }

    public class Sys {
        @SerializedName("type")
        public int type;
        @SerializedName("id")
        public int id;
        @SerializedName("message")
        public float message;
        @SerializedName("country")
        public String country;
        @SerializedName("sunrise")
        public int sunrise;
        @SerializedName("sunset")
        public int sunset;
    }

    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + weather.get(0).icon + ".png";
    }
}




