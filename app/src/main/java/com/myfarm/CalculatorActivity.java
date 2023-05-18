package com.myfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calc_page);
        int animalTypeID = getIntent().getIntExtra("animalTypeID", 0);
        System.out.println(animalTypeID);
    }
}