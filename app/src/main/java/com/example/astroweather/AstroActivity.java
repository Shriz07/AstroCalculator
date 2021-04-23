package com.example.astroweather;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;
import java.util.TimeZone;
import com.astrocalculator.AstroCalculator;


public class AstroActivity extends AppCompatActivity
{
    private FragmentStateAdapter fragmentStateAdapter;

    public static Calendar calendar;

    private static AstroInfo astroInfo;

    private TextView time, position;

    private Handler refreshTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.astro_screen);
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));

        time = findViewById(R.id.tvTime);
        position = findViewById(R.id.tvPosition);

        position.setText("Position: " + SettingsActivity.latitude + " , " + SettingsActivity.longitude);
        time.setText("Time: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

        AstroCalculator.Location location = new AstroCalculator.Location(SettingsActivity.latitude, SettingsActivity.longitude);

        astroInfo = new AstroInfo(calendar, location, isTablet);
        refreshTime = new Handler(getMainLooper());

        refreshTime.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshTime.postDelayed(this, 1000);
                calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
                astroInfo.refreshDate();
                time.setText("Time: " + getTime(calendar));
            }
        }, 1);

        if(isTablet == false)
        {
            ViewPager2 viewPager = findViewById(R.id.vp);
            fragmentStateAdapter = new MyPagerAdapter(this, astroInfo);
            viewPager.setAdapter(fragmentStateAdapter);
        }
        else
        {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer1, SunFragment.newInstance(astroInfo)).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer2, MoonFragment.newInstance(astroInfo)).commit();
        }
    }

    private String getTime(Calendar calendar)
    {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String res = parseNumber(hour) + ":" + parseNumber(minute) + ":" + parseNumber(second);
        return res;
    }

    private String parseNumber(int number)
    {
        String sec;
        if(number < 10)
            sec = "0" + number;
        else
            sec = "" + number;
        return sec;
    }

    @Override
    public void onPause() {
        refreshTime.removeCallbacksAndMessages(null);
        super.onPause();
    }
}
