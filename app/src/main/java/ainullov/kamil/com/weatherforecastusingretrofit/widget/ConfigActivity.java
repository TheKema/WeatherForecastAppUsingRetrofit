package ainullov.kamil.com.weatherforecastusingretrofit.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ainullov.kamil.com.weatherforecastusingretrofit.R;

public class ConfigActivity extends Activity {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;


    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_TEXT = "widget_text_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.config);
    }


    public void onClick(View v) {
        EditText etText = (EditText) findViewById(R.id.etText);

        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(WIDGET_TEXT + widgetID, etText.getText()
                .toString());
        editor.commit();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        WeatherWidget.updateWidget(this, appWidgetManager, sp, widgetID);

        // положительный ответ
        setResult(RESULT_OK, resultValue);

        finish();
    }
}
