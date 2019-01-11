package ainullov.kamil.com.weatherforecastusingretrofit.api;

import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherDay;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherForecast;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherAPI {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static String KEY = "9a829d5b59156cffd5ae083d9eb0a0d5";
    private static Retrofit retrofit = null;

    public interface ApiInterface {
        @GET("weather?")
        Call<WeatherDay> getToday(
                @Query("q") String cityName,
                @Query("units") String units,
                @Query("appid") String appid
        );

        @GET("forecast")
        Call<WeatherForecast> getForecast(
                @Query("q") String cityName,
                @Query("units") String units,
                @Query("appid") String appid
        );
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
