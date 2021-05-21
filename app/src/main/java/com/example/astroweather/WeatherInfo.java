package com.example.astroweather;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WeatherInfo
{
    private static String APIkey = "a61bf01a4b7481a2bb6728eadd71881f";

    private String temperature;
    private String pressure;
    private String longitude;
    private String latitude;
    private String windSpeed;
    private String description;
    private String city;
    private String icon;
    private String windDir;
    private String humidity;
    private String visibility;
    private String collectedDate;

    public LinkedHashMap<String, String> forecastImages = new LinkedHashMap<>();
    public LinkedHashMap<String, String> forecastTemperatures = new LinkedHashMap<>();

    private Context context;
    private boolean isConnected;
    private boolean correctCity;

    public WeatherInfo(Context context, boolean isInternet)
    {
        this.context = context;
        isConnected = isInternet;
    }

    public boolean getWeatherInfo()
    {
        boolean status = true;
        try {
            if (isConnected) {
                getWeatherInfoFromInternet();
                if(correctCity == false)
                {
                    SettingsActivity.city = SettingsActivity.defaultCity;
                    getWeatherInfoFromInternet();
                    status = false;
                }
                getForecastInfoFromIntenet();
            } else {
                restoreWeatherInfoFromFile();
                restoreForecastInfoFromFile();
            }
        }
        catch (IOException e)
        {
            System.out.println("Failed to restore data");
        }
        catch (JSONException e)
        {
            System.out.println("Failed while parsing data");
        }
        return status;
    }

    public void restoreWeatherInfoFromFile() throws IOException, JSONException
    {
        String result = readFromFile("weather.json");

        JSONObject jObject = new JSONObject(result);
        readWeatherDataFromJson(jObject);
    }

    public void restoreForecastInfoFromFile() throws IOException, JSONException
    {
        String result = readFromFile("forecast.json");

        JSONObject jObject = new JSONObject(result);
        readForcastDataFromJson(jObject);
    }

    public String readFromFile(String filename) throws IOException
    {
        FileInputStream fis;
        InputStreamReader inputStreamReader;
        StringBuilder stringBuilder = new StringBuilder();
        String result;

        fis = context.openFileInput(filename);
        inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while(line != null)
        {
            stringBuilder.append(line).append('\n');
            line = reader.readLine();
        }

        reader.close();
        inputStreamReader.close();
        fis.close();

        result = stringBuilder.toString();
        return result;
    }

    public void getForecastInfoFromIntenet()
    {
        correctCity = false;
        Thread thread = new Thread() {
            @Override public void run() {
                try
                {
                    URL url = new URL("https://api.openweathermap.org/data/2.5/forecast/daily?q=" + SettingsActivity.city + "&cnt=7&units=metric&appid=" + APIkey);
                    Scanner scanner = new Scanner(url.openStream(), "UTF-8");
                    String result = scanner.useDelimiter("\\A").next();
                    scanner.close();
                    JSONObject jObject = new JSONObject(result);

                    readForcastDataFromJson(jObject);
                    correctCity = true;

                    FileOutputStream fos = context.openFileOutput("forecast.json", Context.MODE_PRIVATE);
                    fos.write(result.getBytes());
                    fos.close();

                }
                catch (FileNotFoundException e)
                {
                    System.out.println("City doesnt exists");
                }
                catch (Exception e)
                {
                    System.out.println("Failed to get data from internet");
                }
            }
        };
        thread.start();
        try {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getWeatherInfoFromInternet()
    {
        correctCity = false;
        Thread thread = new Thread() {
            @Override public void run() {
                try
                {
                    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + SettingsActivity.city + "&units=metric&appid=" + APIkey);
                    Scanner scanner = new Scanner(url.openStream(), "UTF-8");
                    String result = scanner.useDelimiter("\\A").next();
                    scanner.close();
                    JSONObject jObject = new JSONObject(result);

                    correctCity = true;

                    readWeatherDataFromJson(jObject);

                    FileOutputStream fos = context.openFileOutput("weather.json", Context.MODE_PRIVATE);
                    fos.write(result.getBytes());
                    fos.close();

                }
                catch (FileNotFoundException e)
                {
                    System.out.println("City doesnt exists");
                }
                catch (Exception e)
                {
                    System.out.println("Failed to get data from internet");
                }
            }
        };
        thread.start();
        try {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void readForcastDataFromJson(JSONObject jObject)
    {
        try
        {
            String tmp = " °C";
            if(SettingsActivity.units.equals("imperial"))
                tmp = " °F";
            for(int i = 1; i < 7; i++)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date date = new Date(Long.parseLong(jObject.getJSONArray("list").getJSONObject(i).getString("dt")) * 1000);
                forecastImages.put(sdf.format(date), "i" + jObject.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                forecastTemperatures.put(sdf.format(date), jObject.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getString("day") + tmp);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to read data");
        }
    }

    public void readWeatherDataFromJson(JSONObject jObject)
    {
        String tmp;
        String wnd;
        DecimalFormat df = new DecimalFormat("#.##");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            if(SettingsActivity.units.equals("metric")) {
                tmp = " °C";
                wnd = " m/s";
                temperature = jObject.getJSONObject("main").getString("temp") + tmp;
                windSpeed = jObject.getJSONObject("wind").getString("speed") + wnd;
            }
            else
            {
                tmp = " °F";
                wnd = " miles/h";

                double tmpValue = Double.parseDouble(jObject.getJSONObject("main").getString("temp"));
                tmpValue = tmpValue * 1.8 + 32;
                temperature = df.format(tmpValue) + tmp;
                double windValue = Double.parseDouble(jObject.getJSONObject("wind").getString("speed"));
                windValue = windValue * 2.24;
                windSpeed = df.format(windValue) + wnd;
            }
            pressure = jObject.getJSONObject("main").getString("pressure") + " hPa";
            description = jObject.getJSONArray("weather").getJSONObject(0).getString("description");
            city = jObject.getString("name");
            longitude = jObject.getJSONObject("coord").getString("lon");
            latitude = jObject.getJSONObject("coord").getString("lat");
            visibility = jObject.getString("visibility") + "m";
            windDir = jObject.getJSONObject("wind").getString("deg") + "°";
            humidity = jObject.getJSONObject("main").getString("humidity") + "%";
            icon = "i" + jObject.getJSONArray("weather").getJSONObject(0).getString("icon");
            Date date = new Date(Long.parseLong(jObject.getString("dt")) * 1000);
            collectedDate = sdf.format(date);
        }
        catch (Exception e)
        {
            System.out.println("Failed to read data");
        }
    }

    public String getCollectedDate() { return collectedDate; }

    public LinkedHashMap<String, String> getForecastImages()
    {
        return forecastImages;
    }

    public LinkedHashMap<String, String> getForecastTemperatures()
    {
        return forecastTemperatures;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setIsConnected(boolean isConnected) { this.isConnected = isConnected; }
}
