package com.myfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.myfarm.db.MyFarmDatabase;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calc_page);
        int animalTypeID = getIntent().getIntExtra("animalTypeID", 0);

        EditText chestGirth = findViewById(R.id.chest_girth_edittext);
        EditText bodyLengthEdittext = findViewById(R.id.chest_girth_edittext);
        Button calcWeightButton = findViewById(R.id.calc_weight_button);
        Switch calcWaySwitch = findViewById(R.id.calc_way_switch);
        Spinner spinner = findViewById(R.id.spinner);
        final boolean isPig;

        if (animalTypeID == 1){
            // если тип животного крупный рогатый скот
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<>());
            adapter.addAll("Молочная порода", "Мясо-молочная или мясная порода");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            isPig = false;
        } else {
            // если тип животного свинья
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<>());
            adapter.addAll("Тощая упитанность", "Средняя упитанность", "Высшая упитанность");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            isPig = true;
        }

        calcWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPig){
                    // методы расчёт массы коров
                    if (!chestGirth.getText().toString().equals("") &
                            Integer.parseInt(String.valueOf(chestGirth.getText())) >= 170 & !calcWaySwitch.isChecked()){
                        // первый способ
                    }

                } else {
                    // расчёт массы свиньи
                    System.out.println("масса свиньи тут будет");
                }
            }
        });



    }
}