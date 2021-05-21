package com.example.astroweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WeatherPagerAdapter extends FragmentStateAdapter
{
    private WeatherInfo weatherInfo;
    private static int NUM_ITEMS = 3;

    public WeatherPagerAdapter(FragmentActivity fragmentActivity, WeatherInfo weatherInfo)
    {
        super(fragmentActivity);
        this.weatherInfo = weatherInfo;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return ActualWeatherFragment.newInstance(weatherInfo);
        else if(position == 1)
            return WeatherDetailsFragment.newInstance(weatherInfo);
        else
            return ForecastFragment.newInstance(weatherInfo);
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}
