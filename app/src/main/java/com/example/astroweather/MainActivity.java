package com.example.astroweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAstro = findViewById(R.id.btnApp);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnExit = findViewById(R.id.btnExit);

        btnAstro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AstroActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}