package com.myfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class CalculatorActivity extends AppCompatActivity {
    Animal animal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calc_page);

        int animalTypeID = 0;
        Bundle arguments = getIntent().getExtras();

        if(arguments!=null) {
            animal = (Animal) arguments.getSerializable(Animal.class.getSimpleName());
            animalTypeID = animal.getAnimalTypeID();
        }

        TextView weightText = findViewById(R.id.weight);
        EditText chestGirth = findViewById(R.id.chest_girth_edittext);
        EditText bodyLengthEdittext = findViewById(R.id.body_length_edittext);
        Button calcWeightButton = findViewById(R.id.calc_weight_button);
        Button saveWeightButton = findViewById(R.id.save_weight_button);
        Switch calcWaySwitch = findViewById(R.id.calc_way_switch);
        Spinner spinner = findViewById(R.id.spinner);
        ImageView imageView = findViewById(R.id.calc_image);

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
        chestGirthWarning.setGravity(Gravity.BOTTOM, 0, 160);
        Toast calcSecondWayWarning = Toast.makeText(getApplication(),
                "Укажите обхват груди \nи длину туловища, \nа также выберите породу!",
                Toast.LENGTH_LONG);
        calcSecondWayWarning.setGravity(Gravity.BOTTOM, 0, 160);

        Toast pigDataWarning = Toast.makeText(getApplication(),
                "Укажите обхват груди за лопатками\n и длину туловища, в см!",
                Toast.LENGTH_LONG);
        pigDataWarning.setGravity(Gravity.BOTTOM, 0, 160);
        Toast pigNumWarning = Toast.makeText(getApplication(),
                "Обхват груди от 50 см, \nдлина тела от 75 см!",
                Toast.LENGTH_LONG);
        pigNumWarning.setGravity(Gravity.BOTTOM, 0, 160);
        Toast calcSecondWayPigWarning = Toast.makeText(getApplication(),
                "Для данного способа \nнеобходимо выбрать упитанность!",
                Toast.LENGTH_LONG);
        calcSecondWayPigWarning.setGravity(Gravity.BOTTOM, 0, 160);

        Toast saveWarning = Toast.makeText(getApplication(),
                "Масса не должна быть пустой \nи более 2555 кг!",
                Toast.LENGTH_LONG);
        saveWarning.setGravity(Gravity.BOTTOM, 0, 160);

        if (animalTypeID == 1){
            // если тип животного крупный рогатый скот
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<>());
            adapter.addAll("Выберите породу!", "Молочная порода", "Молочно-мясная или мясная порода");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            imageView.setImageResource(getResources().getIdentifier("cow_calc_guide",
                    "drawable", getPackageName()));
            isPig = false;
        } else {
            // если тип животного свинья
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<>());
            adapter.addAll("Укажите упитанность!", "Тощая упитанность", "Средняя упитанность", "Высшая упитанность");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            imageView.setImageResource(getResources().getIdentifier("pig_calc_guide",
                    "drawable", getPackageName()));
            isPig = true;
        }
        if (!isPig){
            spinner.setEnabled(false);
            bodyLengthEdittext.setEnabled(false);
        } else {
            spinner.setEnabled(false);
        }

        saveWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!weightText.getText().toString().equals("")){
                    if (Integer.parseInt(weightText.getText().toString()) <= 2555){
                        saveNewWeight(Float.parseFloat(weightText.getText().toString()));
                    } else {
                        saveWarning.show();
                    }
                } else {
                    saveWarning.show();
                }
            }
        });


        calcWaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPig){
                    spinner.setEnabled(calcWaySwitch.isChecked());
                    bodyLengthEdittext.setEnabled(calcWaySwitch.isChecked());
                } else {
                    spinner.setEnabled(calcWaySwitch.isChecked());
                }
            }
        });

        calcWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    if (!chestGirth.getText().toString().equals("") & !bodyLengthEdittext.getText().toString().equals("")){
                        if (Integer.parseInt(chestGirth.getText().toString()) >= 50 &
                                Integer.parseInt(bodyLengthEdittext.getText().toString()) >= 75){
                            if(!calcWaySwitch.isChecked()){
                                // первый способ расчета
                                weightText.setText(String.valueOf(getPigWeightFirstWay(Float.parseFloat(chestGirth.getText().toString()),
                                        Float.parseFloat(bodyLengthEdittext.getText().toString()))));
                            } else {
                               // второй способ расчета + проверка
                                if (calcWaySwitch.isChecked() &
                                        !spinner.getSelectedItem().toString().contains("!")){
                                    weightText.setText(String.valueOf(getPigWeightSecondWay(Float.parseFloat(chestGirth.getText().toString()),
                                            Float.parseFloat(bodyLengthEdittext.getText().toString()),
                                            spinner.getSelectedItem().toString())));
                                } else {
                                    calcSecondWayPigWarning.show();
                                }
                            }
                        } else {
                            pigNumWarning.show();
                        }
                    } else {
                        pigDataWarning.show();
                    }
                }
            }
        });
    }

    float getPigWeightFirstWay(float chestGirth, float bodyLength){
        float weight = 0;
        weight = (float) (1.54 * chestGirth + 0.99 * bodyLength - 150);
        return weight;
    }

    int getPigWeightSecondWay(float chestGirth, float bodyLength, String fatness){
        int weight = 0;
        if (Objects.equals(fatness, "Тощая упитанность")){
            weight = (int) (chestGirth * bodyLength / 162);
        }
        if (Objects.equals(fatness, "Средняя упитанность")){
            weight = (int) (chestGirth * bodyLength / 156);
        }
        if (Objects.equals(fatness, "Высшая упитанность")){
            weight = (int) (chestGirth * bodyLength / 142);
        }
        return weight;
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
        }
        if (Objects.equals(animalType, "Молочно-мясная или мясная порода")){
            weight = (int) (chestGirth * bodyLength / 100 * 2.5);
        }
        return weight;
    }

    void saveNewWeight(float weight){
        animal.setWeight(weight);
        MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);
        finishAffinity();
        Intent intent = new Intent(getApplication(), AnimalActivity.class);
        intent.putExtra(Animal.class.getSimpleName(), animal);
        startActivity(intent);
    }

}