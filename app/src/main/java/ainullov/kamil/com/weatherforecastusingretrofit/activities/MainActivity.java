package ainullov.kamil.com.weatherforecastusingretrofit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ainullov.kamil.com.weatherforecastusingretrofit.R;
import ainullov.kamil.com.weatherforecastusingretrofit.adapter.ItemInWeatherAdapter;
import ainullov.kamil.com.weatherforecastusingretrofit.adapter.WeatherAdapter;
import ainullov.kamil.com.weatherforecastusingretrofit.api.WeatherAPI;
import ainullov.kamil.com.weatherforecastusingretrofit.mvp.MainContract;
import ainullov.kamil.com.weatherforecastusingretrofit.mvp.MainModel;
import ainullov.kamil.com.weatherforecastusingretrofit.mvp.MainPresenter;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherDay;

// KEY - 9a829d5b59156cffd5ae083d9eb0a0d5

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainContract.View {

    private MainContract.Presenter mPresenter;

    private String etInstanceState = "";
    private TextView tvTemp;
    private TextView tvDesc;
    private TextView tvWind;
    private TextView tvPressure;
    private TextView tvHumidity;
    private ImageView ivIcon;
    private EditText etPutCity;
    private Button btnGet;

    List<ItemInWeatherAdapter> itemInAdapterList;
    WeatherAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this, new MainModel());

        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvWind = (TextView) findViewById(R.id.tvWind);
        tvPressure = (TextView) findViewById(R.id.tvPressure);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        etPutCity = (EditText) findViewById(R.id.etPutCity);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnGet.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemInAdapterList = new ArrayList<>();
        adapter = new WeatherAdapter(this, itemInAdapterList);
        recyclerView.setAdapter(adapter);

        mPresenter.load(this);
    }

    public void getWeather() {
        final String cityName = etPutCity.getText().toString();
        mPresenter.onGetWeatherButtonWasClicked(cityName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGet:
                if (etPutCity.getText().length()!=0) {
                    itemInAdapterList.clear();
                    getWeather();
                }
        }
    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("etCityName", etPutCity.getText().toString());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        etInstanceState = savedInstanceState.getString("etCityName");
        etPutCity.setText(etInstanceState);

        if (!etInstanceState.isEmpty()) {
            itemInAdapterList.clear();
            getWeather();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (etPutCity.getText().length() != 0)
            mPresenter.save(this, etPutCity.getText().toString());
    }

    @Override
    public void load(String city) {
        if (city.length() != 0) {
            etPutCity.setText(city);
            getWeather();
        }
    }

    @Override
    public void showWeatherDayData(WeatherDay weatherDay) {
        WeatherDay _weatherDay = weatherDay;
        tvTemp.setText((int) _weatherDay.main.temp + "°");
        tvDesc.setText(_weatherDay.weather.get(0).description);
        tvWind.setText((int) _weatherDay.wind.speed + " m/s");
        tvPressure.setText((int) _weatherDay.main.pressure + " hpa");
        tvHumidity.setText(_weatherDay.main.humidity + " %");
        // Работа Glide и Picasso
        Glide.with(MainActivity.this).load(_weatherDay.getIconUrl()).into(ivIcon);
//        Picasso.with(MainActivity.this).load(data.getIconUrl()).into(ivImage);

    }

    @Override
    public void showWeatherForecastData(List<ItemInWeatherAdapter> itemInWeatherAdapters) {
        itemInAdapterList = itemInWeatherAdapters;
        adapter = new WeatherAdapter(this, itemInAdapterList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
