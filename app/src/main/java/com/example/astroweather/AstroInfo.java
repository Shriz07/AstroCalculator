package com.example.astroweather;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class AstroInfo
{
    private Calendar calendar;
    private AstroDateTime astroDateTime;
    private AstroCalculator astroCalculator;
    boolean isTablet;

    public AstroInfo(Calendar calendar, AstroCalculator.Location location, boolean isTablet)
    {
        this.isTablet = isTablet;
        this.calendar = calendar;
        astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), 2, false);
        astroCalculator = new AstroCalculator(astroDateTime, location);
    }

    public void refreshDate()
    {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), 2, false);
        astroCalculator.setDateTime(astroDateTime);
    }

    public String getSunrise()
    {
        String res;
        int hour = astroCalculator.getSunInfo().getSunrise().getHour();
        int minute = astroCalculator.getSunInfo().getSunrise().getMinute();
        int second = astroCalculator.getSunInfo().getSunrise().getSecond();
        double azimuth = astroCalculator.getSunInfo().getAzimuthRise();
        res = parseNumber(hour) + ":" + parseNumber(minute) + ":" + parseNumber(second) + "\nAzimuth: " + roundNumber(azimuth, 2);
        if(isTablet)
            res = parseNumber(hour) + ":" + parseNumber(minute) + ":" + parseNumber(second) + ", Azimuth: " + roundNumber(azimuth, 2);
        return res;
    }
    public String getSunset()
    {
        String res;
        int hour = astroCalculator.getSunInfo().getSunset().getHour();
        int minute = astroCalculator.getSunInfo().getSunset().getMinute();
        int second = astroCalculator.getSunInfo().getSunset().getSecond();
        double azimuth = astroCalculator.getSunInfo().getAzimuthSet();
        res = parseNumber(hour) + ":" + parseNumber(minute) + ":" + parseNumber(second) + "\nAzimuth: " + roundNumber(azimuth, 2);
        if(isTablet)
            res = parseNumber(hour) + ":" + parseNumber(minute) + ":" + parseNumber(second) + ", Azimuth: " + roundNumber(azimuth, 2);
        return res;
    }

    public String getCivilDawn()
    {
        int hour = astroCalculator.getSunInfo().getTwilightMorning().getHour();
        int minute = astroCalculator.getSunInfo().getTwilightMorning().getMinute();
        String res = parseNumber(hour) + ":" + parseNumber(minute);
        return res;
    }
    public String getTwilight()
    {
        int hour = astroCalculator.getSunInfo().getTwilightEvening().getHour();
        int minute = astroCalculator.getSunInfo().getTwilightEvening().getMinute();
        String res = parseNumber(hour) + ":" + parseNumber(minute);
        return res;
    }




    public String getMoonrise()
    {
        String res;
        int hour = astroCalculator.getMoonInfo().getMoonrise().getHour();
        int minute = astroCalculator.getMoonInfo().getMoonrise().getMinute();
        int second = astroCalculator.getMoonInfo().getMoonrise().getSecond();
        res = parseNumber(hour) + ":" + parseNumber(minute) + ":" + parseNumber(second);
        return res;
    }
    public String getMoonset()
    {
        String res;
        int hour = astroCalculator.getMoonInfo().getMoonset().getHour();
        int minute = astroCalculator.getMoonInfo().getMoonset().getMinute();
        int second = astroCalculator.getMoonInfo().getMoonset().getSecond();
        res = parseNumber(hour) + ":" + parseNumber(minute) + ":" + parseNumber(second);
        return res;
    }
    public double getSynodicMonth()
    {
        Double month = astroCalculator.getMoonInfo().getAge();
        month = roundNumber(month, 2);
        return month;
    }
    public double getPhase()
    {
        Double phase = astroCalculator.getMoonInfo().getIllumination();
        phase = roundNumber(phase, 2);
        return phase;
    }
    public String getNextNewMoon()
    {
        int year = astroCalculator.getMoonInfo().getNextNewMoon().getYear();
        int month = astroCalculator.getMoonInfo().getNextNewMoon().getMonth();
        int day = astroCalculator.getMoonInfo().getNextNewMoon().getDay();
        String res = year + "." + parseNumber(month) + "." + parseNumber(day);
        return res;
    }
    public String  getNextFullMoon()
    {
        int year = astroCalculator.getMoonInfo().getNextFullMoon().getYear();
        int month = astroCalculator.getMoonInfo().getNextFullMoon().getMonth();
        int day = astroCalculator.getMoonInfo().getNextFullMoon().getDay();
        String res = year + "." + parseNumber(month) + "." + parseNumber(day);
        return res;
    }

    private static double roundNumber(double value, int places)
    {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
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
}
