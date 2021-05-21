package com.example.astroweather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.io.Console;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherActivity extends AppCompatActivity
{
    private FragmentStateAdapter fragmentStateAdapter;

    public static Calendar calendar;

    private WeatherInfo weatherInfo;
    private TextView time;
    private ImageButton btnFavourite, btnRefresh, btnSettings;
    private Spinner spFavourites;

    private boolean isConnected;

    private Handler refreshTime;

    private DatabaseHandler db = new DatabaseHandler(this);

    private List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_screen);
        calendar = Calendar.getInstance();

        readFavourites();

        checkInternet();
        weatherInfo = new WeatherInfo(getApplicationContext(), isConnected);
        refreshWeatherInfo();

        loadLayout();
        setOnChangeListener();

        btnFavourite.setOnClickListener(this::onFavouriteButtonClick);
        btnRefresh.setOnClickListener(this::onRefreshButtonClick);
        btnSettings.setOnClickListener(this::onSettingsButtonClick);

        time.setText("Time: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

        runTimeThread();
        setViewPager();
        setFavourites();
    }

    public void setFavourites()
    {
        cities.add(0, "Favourites");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, cities);
        spFavourites.setAdapter(dataAdapter);
        spFavourites.setPrompt("Favourites");
        setSpinnerAvailability();
    }

    public void setSpinnerAvailability()
    {
        if(!isConnected)
            spFavourites.setEnabled(false);
        else
            spFavourites.setEnabled(true);
    }

    public void setOnChangeListener()
    {
        spFavourites.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if(!selected.equals("Favourites")) {
                    SettingsActivity.city = selected;
                    refreshWeatherInfo();
                    setViewPager();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void runTimeThread()
    {
        refreshTime = new Handler(getMainLooper());
        refreshTime.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshTime.postDelayed(this, 1000);
                calendar = Calendar.getInstance();
                time.setText("Time: " + getTime(calendar));
            }
        }, 1);
    }

    public void refreshWeatherInfo()
    {
        if(SettingsActivity.city == null)
            SettingsActivity.city = SettingsActivity.defaultCity;
        if(weatherInfo.getWeatherInfo() == false && isConnected == true)
            Toast.makeText(getBaseContext(), "City name invalid. Displaying data for default city.", Toast.LENGTH_LONG).show();
    }

    public void setViewPager()
    {
        ViewPager2 viewPager = findViewById(R.id.weatherVP);
        fragmentStateAdapter = new WeatherPagerAdapter(this, weatherInfo);
        viewPager.setAdapter(fragmentStateAdapter);
    }

    public void readFavourites()
    {
        cities = db.getAllCities();
        if(cities.size() > 0)
            SettingsActivity.defaultCity = cities.get(0);
    }

    public void onSettingsButtonClick(View v)
    {
        Intent intent = new Intent(WeatherActivity.this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onFavouriteButtonClick(View v)
    {
        FavouriteCity city = new FavouriteCity(SettingsActivity.city);
        boolean check = db.checkIfExists(city);
        if(check) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Are you sure you want to remove current city from favourites ?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(), "City removed from favourites.", Toast.LENGTH_LONG).show();
                    db.deleteCity(city);
                    readFavourites();
                    setFavourites();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Toast.makeText(getBaseContext(), "City added to favourites.", Toast.LENGTH_LONG).show();
            db.addCity(city);
            readFavourites();
            setFavourites();
        }
    }

    public void onRefreshButtonClick(View v)
    {
        checkInternet();
        weatherInfo.setIsConnected(isConnected);
        refreshWeatherInfo();
        setViewPager();
        setSpinnerAvailability();
    }

    public void loadLayout()
    {
        time = findViewById(R.id.tvTime2);
        btnFavourite = findViewById(R.id.btnFavourite);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnSettings = findViewById(R.id.btnSettings);
        spFavourites = findViewById(R.id.spFavourites);
    }

    @Override
    public void onRestart()
    {
        checkInternet();
        refreshWeatherInfo();
        setViewPager();
        readFavourites();
        setFavourites();
        runTimeThread();
        super.onRestart();
    }

    @Override
    public void onPause() {
        refreshTime.removeCallbacksAndMessages(null);
        super.onPause();
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

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR");
                    isConnected = true;
                    return;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI");
                    isConnected = true;
                    return;
                }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET");
                    isConnected = true;
                    return;
                }
            }
        }
        Toast.makeText(getBaseContext(), "No internet connection, data may be old.", Toast.LENGTH_LONG).show();
        isConnected = false;
    }
}
