package ainullov.kamil.com.weatherforecastusingretrofit.mvp;

import android.content.Context;

import java.util.List;

import ainullov.kamil.com.weatherforecastusingretrofit.adapter.ItemInWeatherAdapter;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherDay;

public class MainPresenter implements MainContract.Presenter, MainContract.Model.OnFinishedListener {
    private MainContract.View mView;
    private MainContract.Model mRepository;

    public MainPresenter(MainContract.View mView, MainContract.Model mRepository) {
        this.mView = mView;
        this.mRepository = mRepository;
    }

    @Override
    public void onGetWeatherButtonWasClicked(String cityName) {
        mRepository.getWeatherDayNoticeArrayList(this,cityName);
        mRepository.getWeatherForecastNoticeArrayList(this, cityName);

    }

    @Override
    public void load(Context context) {
       String city = "";
       city = mRepository.load(context);
        mView.load(city);
    }

    @Override
    public void save(Context context, String city) {
        mRepository.save(context, city);
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onFinishedGetWeatherDay(WeatherDay weatherDay) {
        if(mView != null){
            mView.showWeatherDayData(weatherDay);
        }
    }

    @Override
    public void onFinishedGetWeatherForecast(List<ItemInWeatherAdapter> itemInWeatherAdapters) {
        if(mView != null){
            mView.showWeatherForecastData(itemInWeatherAdapters);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
