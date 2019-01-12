package ainullov.kamil.com.weatherforecastusingretrofit.mvp;

import android.content.Context;

import java.util.List;

import ainullov.kamil.com.weatherforecastusingretrofit.adapter.ItemInWeatherAdapter;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherDay;

public interface MainContract {
    interface Model {

        interface OnFinishedListener {
            void onFinishedGetWeatherDay(WeatherDay weatherDay);
            void onFinishedGetWeatherForecast(List<ItemInWeatherAdapter> itemInWeatherAdapters);

            void onFailure(Throwable t);
        }

        void getWeatherDayNoticeArrayList(OnFinishedListener onFinishedListener, String cityName);

        void getWeatherForecastNoticeArrayList(OnFinishedListener onFinishedListener, String cityName);

        String load(Context context);

        void save(Context context, String city);
    }

    interface View {
        void showWeatherDayData(WeatherDay weatherDay);

        void showWeatherForecastData(List<ItemInWeatherAdapter> itemInWeatherAdapters);

        void load(String city);
    }

    interface Presenter {
        void onGetWeatherButtonWasClicked(String cityName);

        void load(Context context);

        void save(Context context, String city);

        void onDestroy();
    }
}
