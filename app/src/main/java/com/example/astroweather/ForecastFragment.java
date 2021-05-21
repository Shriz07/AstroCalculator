package com.example.astroweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class ForecastFragment extends Fragment
{
    private static WeatherInfo weatherInfo;

    private TextView tvDay1, tvDay2, tvDay3, tvDay4, tvDay5, tvDay6;
    private TextView tvTmp1, tvTmp2, tvTmp3, tvTmp4, tvTmp5, tvTmp6;
    private ImageView fImage1, fImage2, fImage3, fImage4, fImage5, fImage6;

    private volatile LinkedList<TextView> days = new LinkedList<>();
    private volatile LinkedList<TextView> temperatures = new LinkedList<>();
    private volatile LinkedList<ImageView> images = new LinkedList<>();

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

    public static ForecastFragment newInstance(WeatherInfo info)
    {
        weatherInfo = info;
        ForecastFragment forecastFragment = new ForecastFragment();
        return forecastFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.forecast_fragment, container, false);

        loadLayout(view);
        setTemperatures();
        setImages();
        return view;
    }

    private void setTemperatures()
    {
        Iterator it = weatherInfo.forecastTemperatures.entrySet().iterator();
        int i = 0;
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            days.get(i).setText(pair.getKey().toString());
            temperatures.get(i).setText(pair.getValue().toString());
            i++;
        }
    }

    private void setImages()
    {
        Iterator it = weatherInfo.forecastImages.entrySet().iterator();
        int i = 0;
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            int id = getResources().getIdentifier("com.example.astroweather:drawable/" + pair.getValue().toString(), null, null);
            images.get(i).setImageResource(id);
            i++;
        }
    }

    private void loadLayout(View v)
    {
        tvDay1 = v.findViewById(R.id.tvDay1);
        tvDay2 = v.findViewById(R.id.tvDay2);
        tvDay3 = v.findViewById(R.id.tvDay3);
        tvDay4 = v.findViewById(R.id.tvDay4);
        tvDay5 = v.findViewById(R.id.tvDay5);
        tvDay6 = v.findViewById(R.id.tvDay6);
        Collections.addAll(days, tvDay1, tvDay2, tvDay3, tvDay4, tvDay5, tvDay6);

        tvTmp1 = v.findViewById(R.id.tvTemperature1);
        tvTmp2 = v.findViewById(R.id.tvTemperature2);
        tvTmp3 = v.findViewById(R.id.tvTemperature3);
        tvTmp4 = v.findViewById(R.id.tvTemperature4);
        tvTmp5 = v.findViewById(R.id.tvTemperature5);
        tvTmp6 = v.findViewById(R.id.tvTemperature6);
        Collections.addAll(temperatures, tvTmp1, tvTmp2, tvTmp3, tvTmp4, tvTmp5, tvTmp6);

        fImage1 = v.findViewById(R.id.fImage1);
        fImage2 = v.findViewById(R.id.fImage2);
        fImage3 = v.findViewById(R.id.fImage3);
        fImage4 = v.findViewById(R.id.fImage4);
        fImage5 = v.findViewById(R.id.fImage5);
        fImage6 = v.findViewById(R.id.fImage6);
        Collections.addAll(images, fImage1, fImage2, fImage3, fImage4, fImage5, fImage6);
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
        days.clear();
        temperatures.clear();
        images.clear();
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
