package ainullov.kamil.com.weatherforecastusingretrofit.mvp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ainullov.kamil.com.weatherforecastusingretrofit.api.WeatherAPI;
import ainullov.kamil.com.weatherforecastusingretrofit.adapter.ItemInWeatherAdapter;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherDay;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherForecast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class MainModel implements MainContract.Model {

    WeatherAPI.ApiInterface api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);

    WeatherDay weatherDay = new WeatherDay();

    final String units = "metric";
    final String key = WeatherAPI.KEY;

    @Override
    public void getWeatherDayNoticeArrayList(final OnFinishedListener onFinishedListener, String cityName) {

        Call<WeatherDay> callToday = api.getToday(cityName, units, key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.e(TAG, "onResponse getWeatherDay");
                Log.d(TAG, response.toString());
                weatherDay = response.body();

                onFinishedListener.onFinishedGetWeatherDay(weatherDay);
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.e(TAG, "onFailure");
                onFinishedListener.onFailure(t);
            }
        });


    }

    @Override
    public void getWeatherForecastNoticeArrayList(final OnFinishedListener onFinishedListener, String cityName) {

        Call<WeatherForecast> callForecast = api.getForecast(cityName, units, key);
        callForecast.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                Log.e(TAG, "onResponse getWeatherForecast ");
                Log.d(TAG, response.toString());
                List<ItemInWeatherAdapter> itemInAdapterList = new ArrayList<>();
                WeatherForecast data = response.body();

                if (response.isSuccessful()) {
                    SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("E,MM.dd,HH:mm", Locale.ENGLISH);

                    for (int i = 0; i < data.getItems().size(); i++) {

                        // Параметры для айтема в адаптере
                        String dayOfWeeki = formatDayOfWeek.format(data.getItems().get(i).dt * 1000);
                        String desci = data.getItems().get(i).weather.get(0).description;
                        int tempi = (int) data.getItems().get(i).main.temp;
                        String iconi = data.getItems().get(i).getIconUrl();
                        itemInAdapterList.add(new ItemInWeatherAdapter(dayOfWeeki, desci, iconi, tempi));
                    }

                    onFinishedListener.onFinishedGetWeatherForecast(itemInAdapterList);
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e(TAG, "onFailure");
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public String load(Context context) {
        String PREF_NAME = "prefs";
        final String key = "Key";
        String city1 = "";

        SharedPreferences shref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //Если впервые запускаем
        boolean hasVisited = shref.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor e = shref.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
        } else {
            city1 = shref.getString(key, city1);
        }
        return city1;
    }

    @Override
    public void save(Context context, String city) {
        String PREF_NAME = "prefs";
        final String key = "Key";
        SharedPreferences shref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor;
        editor = shref.edit();
        editor.remove(key).commit();
        editor.putString(key, city);
        editor.commit();
    }
}
