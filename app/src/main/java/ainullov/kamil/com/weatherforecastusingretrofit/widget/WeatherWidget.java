package ainullov.kamil.com.weatherforecastusingretrofit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.RemoteViews;

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

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

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

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             SharedPreferences sp, int widgetID) {
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


        Call<WeatherDay> callToday = apiWidget.getToday(widgetText, units, key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {

                WeatherDay data = response.body();
                if (response.isSuccessful()) {

                    cityWidget = data.name;
                    descWidget = data.weather.get(0).description;
                    tempWidget = "" + (int) data.main.temp;

//                    widgetView.setTextViewText(R.id.tvCityWidget, cityWidget);
                    widgetView.setTextViewText(R.id.tvCityWidget, widgetText);
                    widgetView.setTextViewText(R.id.tvDescWidget, descWidget);
                    widgetView.setTextViewText(R.id.tvTempWidget, tempWidget + "°");

                    Intent mainActivityIntent = new Intent(finalContext, ConfigActivity.class);
                    mainActivityIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
                    mainActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, finalWidgetID);
                    PendingIntent pIntent = PendingIntent.getActivity(finalContext, finalWidgetID,
                            mainActivityIntent, 0);
                    widgetView.setOnClickPendingIntent(R.id.llWidget, pIntent);

                    finalAppWidgetManager.updateAppWidget(finalWidgetID, widgetView);
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
            }
        });


    }

}
