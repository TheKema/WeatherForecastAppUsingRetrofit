package ainullov.kamil.com.weatherforecastusingretrofit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import ainullov.kamil.com.weatherforecastusingretrofit.MainActivity;
import ainullov.kamil.com.weatherforecastusingretrofit.R;
import ainullov.kamil.com.weatherforecastusingretrofit.WeatherAPI;
import ainullov.kamil.com.weatherforecastusingretrofit.pojo.WeatherDay;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherWidget extends AppWidgetProvider {

    final static String LOG_TAG = "myLogs";

    static WeatherAPI.ApiInterface apiWidget;
    static String descWidget;
    static String tempWidget;
    static String cityWidget;
    static String iconWidget;
    static int[] finalAppWidgetIds = {};

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        finalAppWidgetIds = appWidgetIds;
        SharedPreferences sp = context.getSharedPreferences(
                ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        Editor editor = context.getSharedPreferences(
                ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(ConfigActivity.WIDGET_TEXT + widgetID);
        }
        editor.commit();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    static void updateWidget(final Context context, AppWidgetManager appWidgetManager,
                             SharedPreferences sp, final int widgetID) {
        final String widgetText = sp.getString(ConfigActivity.WIDGET_TEXT + widgetID, null);
        Log.d(LOG_TAG, "widgetText " + widgetText);

        if (widgetText == null) return;

        final Context finalContext = context;
        final AppWidgetManager finalAppWidgetManager = appWidgetManager;
        final int finalWidgetID = widgetID;
        final RemoteViews widgetView = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        String units = "metric";
        String key = WeatherAPI.KEY;
        apiWidget = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);

        Intent configIntent = new Intent(finalContext, ConfigActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, finalWidgetID);
        PendingIntent pIntent = PendingIntent.getActivity(finalContext, finalWidgetID,
                configIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.llWidget, pIntent);

        Call<WeatherDay> callToday = apiWidget.getToday(widgetText, units, key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {

                WeatherDay data = response.body();
                if (response.isSuccessful()) {

                    cityWidget = data.name;
                    descWidget = data.weather.get(0).description;
                    tempWidget = "" + (int) data.main.temp;
                    iconWidget = data.getIconUrl();

//                    widgetView.setTextViewText(R.id.tvCityWidget, cityWidget);
                    widgetView.setTextViewText(R.id.tvCityWidget, widgetText);
                    widgetView.setTextViewText(R.id.tvDescWidget, descWidget);
                    widgetView.setTextViewText(R.id.tvTempWidget, tempWidget + "°");
                    // Загрузка изображения, строка(ссылка в интернете)
                    Picasso.with(context).load(iconWidget).into(widgetView, R.id.ivIconWidget, finalAppWidgetIds);


                    finalAppWidgetManager.updateAppWidget(finalWidgetID, widgetView);
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
            }
        });


    }

}
