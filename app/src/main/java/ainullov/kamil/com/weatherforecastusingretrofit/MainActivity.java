package ainullov.kamil.com.weatherforecastusingretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ainullov.kamil.com.weatherforecastusingretrofit.adapter.ItemInWeatherAdapter;
import ainullov.kamil.com.weatherforecastusingretrofit.adapter.WeatherAdapter;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherDay;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherForecast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// KEY - 9a829d5b59156cffd5ae083d9eb0a0d5

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "WEATHER";
    String etInstanceState = "";
    TextView tvTemp;
    TextView tvDesc;
    TextView tvWind;
    TextView tvPressure;
    TextView tvHumidity;
    ImageView ivIcon;
    EditText etPutCity;
    Button btnGet;

    WeatherAPI.ApiInterface api;

    List<ItemInWeatherAdapter> itemInAdapterList;
    WeatherAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvWind = (TextView) findViewById(R.id.tvWind);
        tvPressure = (TextView) findViewById(R.id.tvPressure);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        etPutCity = (EditText) findViewById(R.id.etPutCity);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnGet.setOnClickListener(this);

        api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemInAdapterList = new ArrayList<>();
        adapter = new WeatherAdapter(this, itemInAdapterList);
        recyclerView.setAdapter(adapter);

    }

    public void getWeather() {
        String units = "metric";
        String cityName = etPutCity.getText().toString();
        String key = WeatherAPI.KEY;

        Log.d(TAG, "Начало вызова");

        Call<WeatherDay> callToday = api.getToday(cityName, units, key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.e(TAG, "onResponse");
                Log.d(TAG, response.toString());
                WeatherDay data = response.body();
                if (response.isSuccessful()) {
                    tvTemp.setText((int) data.main.temp + "°");
                    tvDesc.setText(data.weather.get(0).description);
                    tvWind.setText((int) data.wind.speed + " m/s");
                    tvPressure.setText((int) data.main.pressure + " hpa");
                    tvHumidity.setText(data.main.humidity + " %");
                    // Работа Glide и Picasso
                    Glide.with(MainActivity.this).load(data.getIconUrl()).into(ivIcon);
//                    Picasso.with(MainActivity.this).load(data.getIconUrl()).into(ivImage);

                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });


        Call<WeatherForecast> callForecast = api.getForecast(cityName, units, key);
        callForecast.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                Log.e(TAG, "onResponse");
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
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGet:
                itemInAdapterList.clear();
                getWeather();
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


}
