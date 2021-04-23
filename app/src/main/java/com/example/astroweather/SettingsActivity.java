package com.example.astroweather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.astrocalculator.AstroDateTime;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity
{
    public static double longitude = 19.458599;
    public static double latitude = 51.759048;
    public static double refreshTime = 5;

    TextInputEditText tiLongitude;
    TextInputEditText tiLatitude;
    TextInputEditText tiRefresh;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        tiLatitude = findViewById(R.id.tiLatitude);
        tiLongitude = findViewById(R.id.tiLongitude);
        tiRefresh = findViewById(R.id.tiRefresh);
        btnSave = findViewById(R.id.btnSave);

        tiRefresh.setText(refreshTime + "");
        tiLatitude.setText(latitude + "");
        tiLongitude.setText(longitude + "");

        btnSave.setOnClickListener(this::onSaveButtonClick);
    }

    public void onSaveButtonClick(View v)
    {
        try {

            if (tiRefresh.getText().toString().length() > 0) {
                double tmp = Double.parseDouble(tiRefresh.getText().toString());
                if (tmp >= 1) {
                    refreshTime = tmp;
                } else {
                    Toast.makeText(getBaseContext(), "Refresh time should be grater than 0", Toast.LENGTH_LONG).show();
                }
            }

            if (tiLongitude.getText().toString().length() > 0) {
                double tmp = Double.parseDouble(tiLongitude.getText().toString());
                if (tmp >= -180 && tmp <= 180) {
                    longitude = tmp;
                } else {
                    Toast.makeText(getBaseContext(), "Valid longitudes are from -180 to 180 degrees.", Toast.LENGTH_LONG).show();
                }
            }

            if (tiLatitude.getText().toString().length() > 0) {
                double tmp = Double.parseDouble(tiLatitude.getText().toString());
                if (tmp >= -90 && tmp <= 90) {
                    latitude = tmp;
                } else {
                    Toast.makeText(getBaseContext(), "Valid latitudes are from -90 to 90 degrees.", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(), "Please enter valid data", Toast.LENGTH_LONG).show();
        }
    }
}
