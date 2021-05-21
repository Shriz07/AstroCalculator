package com.example.astroweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class ActualWeatherFragment extends Fragment
{
    private static WeatherInfo weatherInfo;

    private TextView tvTemperature;
    private TextView tvPressure;
    private TextView tvDescription;
    private TextView tvCity;
    private ImageView weatherIcon;
    private TextView tvLongitude;
    private TextView tvLatitude;
    private TextView tvCollected;

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

    public static ActualWeatherFragment newInstance(WeatherInfo info)
    {
        weatherInfo = info;
        ActualWeatherFragment actualWeatherFragment = new ActualWeatherFragment();
        return actualWeatherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.actual_weather_fragment, container, false);

        loadLayout(view);
        setData();

        int id = getResources().getIdentifier("com.example.astroweather:drawable/" + weatherInfo.getIcon(), null, null);
        weatherIcon.setImageResource(id);

        return view;
    }

    private void setData()
    {
        tvCity.setText(weatherInfo.getCity());
        tvTemperature.setText(String.valueOf(weatherInfo.getTemperature()));
        tvPressure.setText(String.valueOf(weatherInfo.getPressure()));
        tvDescription.setText(weatherInfo.getDescription());
        tvLatitude.setText(weatherInfo.getLatitude());
        tvLongitude.setText(weatherInfo.getLongitude());
        tvCollected.setText(weatherInfo.getCollectedDate());
    }

    private void loadLayout(View view)
    {
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvPressure = view.findViewById(R.id.tvPressure);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvCity = view.findViewById(R.id.tvCity);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        tvLatitude = view.findViewById(R.id.tvLatitude);
        tvLongitude = view.findViewById(R.id.tvLongitude);
        tvCollected = view.findViewById(R.id.tvCollected);
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
