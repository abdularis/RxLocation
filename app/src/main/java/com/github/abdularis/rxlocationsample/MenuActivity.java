package com.github.abdularis.rxlocationsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onCurrentPlaceClick(View view) {
        Intent i = new Intent(this, CurrentPlaceActivity.class);
        startActivity(i);
    }

    public void onLocMapsClick(View view) {
        Intent i = new Intent(this, LocationOnMapActivity.class);
        startActivity(i);
    }
}
