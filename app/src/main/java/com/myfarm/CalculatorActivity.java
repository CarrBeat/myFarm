package com.myfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.myfarm.db.MyFarmDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calc_page);
        int animalTypeID = getIntent().getIntExtra("animalTypeID", 0);

        TextView weightText = findViewById(R.id.weight);
        EditText chestGirth = findViewById(R.id.chest_girth_edittext);
        EditText bodyLengthEdittext = findViewById(R.id.body_length_edittext);
        Button calcWeightButton = findViewById(R.id.calc_weight_button);
        Switch calcWaySwitch = findViewById(R.id.calc_way_switch);
        Spinner spinner = findViewById(R.id.spinner);
        final boolean isPig;

        Toast calcFirstWayWarning = Toast.makeText(getApplication(),
                "Данный способ может применяться \nтолько при обхвате \nгруди за лопатками от 170 см!",
                Toast.LENGTH_LONG);
        calcFirstWayWarning.setGravity(Gravity.BOTTOM, 0, 160);
        Toast calcSecondWayAnimalTypeWarning = Toast.makeText(getApplication(),
                "Для данного способа \nнеобходимо выбрать породу!",
                Toast.LENGTH_LONG);
        calcFirstWayWarning.setGravity(Gravity.BOTTOM, 0, 160);
        Toast calcSecondWayDataWarning = Toast.makeText(getApplication(),
                "Обхват груди за лопатками \nдолжен быть от 10 см, \nдлина толовища от 15 см!",
                Toast.LENGTH_LONG);
        calcFirstWayWarning.setGravity(Gravity.BOTTOM, 0, 160);
        Toast chestGirthWarning = Toast.makeText(getApplication(),
                "Укажите обхват груди за лопатками \nна расстоянии \nширины ладони от локтя!",
                Toast.LENGTH_LONG);
        Toast calcSecondWayWarning = Toast.makeText(getApplication(),
                "Укажите обхват груди \nи длину туловища, \nа также выберите породу!",
                Toast.LENGTH_LONG);

        if (animalTypeID == 1){
            // если тип животного крупный рогатый скот
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<>());
            adapter.addAll("Выберите породу!", "Молочная порода", "Молочно-мясная или мясная порода");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            isPig = false;
        } else {
            // если тип животного свинья
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<>());
            adapter.addAll("Укажите упитанность!", "Тощая упитанность", "Средняя упитанность", "Высшая упитанность");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            isPig = true;
        }
        spinner.setEnabled(false);

        calcWaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setEnabled(calcWaySwitch.isChecked());
            }
        });

        calcWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*calcFirstWayWarning.cancel();
                calcSecondWayAnimalTypeWarning.cancel();
                calcSecondWayDataWarning.cancel();*/
                weightText.setText("");

                if(!isPig){
                    if (!chestGirth.getText().toString().equals("")){
                        // методы расчёта массы коров
                        if (Integer.parseInt(chestGirth.getText().toString()) >= 170 & !calcWaySwitch.isChecked()){
                            // первый способ
                            weightText.setText(String.valueOf(getCowWeightFirstWay(
                                    Float.parseFloat(chestGirth.getText().toString()))));
                        } else if (Integer.parseInt(chestGirth.getText().toString()) < 170 & !calcWaySwitch.isChecked()) {
                            // предупреждение о невозможности использовать данный способ расчета
                            calcFirstWayWarning.show();
                        }
                        if (calcWaySwitch.isChecked() & !bodyLengthEdittext.getText().toString().equals("")){
                            if (Integer.parseInt(chestGirth.getText().toString()) >= 10 & calcWaySwitch.isChecked() &
                                    !spinner.getSelectedItem().toString().contains("!") &
                                    Integer.parseInt(bodyLengthEdittext.getText().toString()) >= 20){
                                weightText.setText(String.valueOf(getCowWeightSecondWay(Float.parseFloat(chestGirth.getText().toString()),
                                        Float.parseFloat(bodyLengthEdittext.getText().toString()),
                                        spinner.getSelectedItem().toString())));
                            } else {
                                if (calcWaySwitch.isChecked() &
                                        spinner.getSelectedItem().toString().contains("!")) {
                                    // предупреждение о необходимости выбора доп. данных
                                    calcSecondWayAnimalTypeWarning.show();
                                }
                                if (calcWaySwitch.isChecked() &
                                        Integer.parseInt(chestGirth.getText().toString()) < 10 &
                                        Integer.parseInt(bodyLengthEdittext.getText().toString()) < 20){
                                    calcSecondWayDataWarning.show();
                                }
                            }
                        } else if (calcWaySwitch.isChecked()) {
                            calcSecondWayWarning.show();
                        }
                    } else {
                        chestGirthWarning.show();
                    }
                } else {
                    // расчёт массы свиньи
                    System.out.println("масса свиньи тут будет вычисляться");
                }
            }
        });
    }
    int getCowWeightFirstWay(float chestGirth){
        int weight = 0;
        if (chestGirth >= 170 & chestGirth < 181){
            weight = (int) (5.3 * chestGirth - 507);
            return weight;
        }
        if (chestGirth >= 181 & chestGirth < 192){
            weight = (int) (5.3 * chestGirth - 486);
            return weight;
        }
        if (chestGirth >= 192){
            weight = (int) (5.3 * chestGirth - 465);
            return weight;
        }
        return weight;
    }

    int getCowWeightSecondWay(float chestGirth, float bodyLength, String animalType){
        int weight = 0;
        if (Objects.equals(animalType, "Молочная порода")){
            weight = (int) (chestGirth * bodyLength / 100 * 2);
            return weight;
        }
        if (Objects.equals(animalType, "Молочно-мясная или мясная порода")){
            weight = (int) (chestGirth * bodyLength / 100 * 2.5);
            return weight;
        }
        return weight;
    }
}