package ainullov.kamil.com.weatherforecastusingretrofit.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ainullov.kamil.com.weatherforecastusingretrofit.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void checkBtnGetWeather() {
        onView(withId(R.id.etPutCity)).perform(clearText());
        onView(withId(R.id.etPutCity)).perform(typeText("Kazan"), closeSoftKeyboard());
        onView(withId(R.id.btnGet)).check(matches(isClickable()));
    }

    @Test
    public void checkWeatherDayTextViewNotNull() {
        onView(withId(R.id.etPutCity)).perform(clearText());
        onView(withId(R.id.etPutCity)).perform(typeText("Kazan"), closeSoftKeyboard());
        onView(withId(R.id.btnGet)).perform(click());
        onView(withId(R.id.tvTemp)).check(matches(not(withText(""))));
        onView(withId(R.id.tvDesc)).check(matches(not(withText(""))));
        onView(withId(R.id.tvHumidity)).check(matches(not(withText(""))));
        onView(withId(R.id.tvPressure)).check(matches(not(withText(""))));
        onView(withId(R.id.tvWind)).check(matches(not(withText(""))));
    }
}