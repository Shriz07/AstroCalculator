package com.example.astroweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WeatherDetailsFragment extends Fragment
{
    private static WeatherInfo weatherInfo;

    private TextView tvWindSpeed;
    private TextView tvWindDir;
    private TextView tvHumidity;
    private TextView tvVisibility;
    private ImageView weatherIcon;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static WeatherDetailsFragment newInstance(WeatherInfo info)
    {
        weatherInfo = info;
        WeatherDetailsFragment weatherDetailsFragment = new WeatherDetailsFragment();
        return weatherDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.weather_details_fragment, container, false);

        loadLayout(view);

        setData();

        int id = getResources().getIdentifier("com.example.astroweather:drawable/" + weatherInfo.getIcon(), null, null);
        weatherIcon.setImageResource(id);

        return view;
    }

    private void setData()
    {
        tvWindSpeed.setText(String.valueOf(weatherInfo.getWindSpeed()));
        tvWindDir.setText(String.valueOf(weatherInfo.getWindDir()));
        tvVisibility.setText(String.valueOf(weatherInfo.getVisibility()));
        tvHumidity.setText(String.valueOf(weatherInfo.getHumidity()));
    }

    private void loadLayout(View view)
    {
        tvWindSpeed = view.findViewById(R.id.tvWindSpeed);
        tvWindDir = view.findViewById(R.id.tvWindDir);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvVisibility = view.findViewById(R.id.tvVisibility);
        weatherIcon = view.findViewById(R.id.weatherImage2);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
