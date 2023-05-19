package com.myfarm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class AnimalActivity extends AppCompatActivity {
    Animal animal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_page);
        EditText animalName = findViewById(R.id.edit_animal_name);
        EditText animalWeight = findViewById(R.id.weight_edit_text);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch animalSexSwitch = findViewById(R.id.animal_sex);
        Button pregnancyButton = findViewById(R.id.add_pregnancy_button);

        Toast deleteAnimalWarning = Toast.makeText(this,
                "Для удаления необходимо поставить галочку в верхнем правом углу, \n" +
                        "а затем зажать кнопку удаления на несколько секунд!",
                Toast.LENGTH_LONG);
        deleteAnimalWarning.setGravity(Gravity.BOTTOM, 0, 160);

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            animal = (Animal) arguments.getSerializable(Animal.class.getSimpleName());
            TextView animalType = findViewById(R.id.animal_type_text);
            animalType.setText(MyFarmDatabase.getDatabase(this)
                    .animalDao().getAnimalTypeName(animal.getAnimalTypeID()));
            TextView animalAge = findViewById(R.id.age_text);
            animalAge.setText(animal.getBirthdate());
            animalName.setText(animal.getAnimalName());
            ImageView imageView = findViewById(R.id.animal_page_image);
            imageView.setImageResource(getResources().getIdentifier(MyFarmDatabase.getDatabase(this).
                    animalTypeDao().getPhotoNameByIDAnimalType(animal.getAnimalTypeID()), "drawable", getPackageName()));


            if(animal.getPregnancyID() > 0 | !animal.getFemale()){
                pregnancyButton.setEnabled(false);
            }
            if (animal.getWeight() == 0.0){
                animalWeight.setText("");
            } else {
                animalWeight.setText(String.valueOf(animal.getWeight()));
            }
            if (animal.getFemale()) {
                animalSexSwitch.toggle();
            }

            Button saveAnimalButton = findViewById(R.id.save_animal_button);
            Button removeButton = findViewById(R.id.delete_animal);
            Button weightCalcButton = findViewById(R.id.open_calc_weight_button);
            CheckBox deleteConfirm = findViewById(R.id.delete_confirm);

            if (animal.getAnimalTypeID() != 1 & animal.getAnimalTypeID() != 10){
                System.out.println();
                weightCalcButton.setEnabled(false);
            }

            weightCalcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplication(), CalculatorActivity.class);
                    intent.putExtra(Animal.class.getSimpleName(), animal);
                    startActivity(intent);
                }
            });

            pregnancyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // вызов диалога с выбором даты + тост
                }
            });
            removeButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (deleteConfirm.isChecked()) {
                        MyFarmDatabase.getDatabase(getApplication()).animalDao().delete(animal);
                        finishActivity();
                    } else {
                        deleteAnimalWarning.show();
                    }
                    return false;
                }
            });
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteAnimalWarning.show();
                }
            });

            Toast weightWarningToast = Toast.makeText(this,
                    "Масса животного должна быть " +
                            "\nот 0.005 кг до 2555.999 кг! \n(точность до 1 гр)",
                    Toast.LENGTH_LONG);
            weightWarningToast.setGravity(Gravity.BOTTOM, 0, 160);

            saveAnimalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isWeightCorrect = false;
                    boolean weightWarning = false;
                    if (!animalWeight.getText().toString().isEmpty()) {
                            // проверка введённого массы на корректность
                            if (animalWeight.getText().toString().matches("^\\$?(\\d+|\\d*\\.\\d+)$")) {
                                // если масса начинается с 0
                                if (animalWeight.getText().toString().startsWith("0") &
                                        animalWeight.getText().toString().indexOf(".") == 1 &
                                        animalWeight.getText().toString().lastIndexOf(".") == 1) {
                                    if (Float.parseFloat(animalWeight.getText().toString()) >= 0.005 &
                                            (animalWeight.getText().toString().length() -
                                                    animalWeight.getText().toString().indexOf(".") - 1 <= 3)) {
                                        isWeightCorrect = true;
                                    } else {
                                        animalWeight.setText("");
                                        weightWarningToast.show();
                                    }
                                } else {
                                    // если масса не начинается с 0
                                    if (!animalWeight.getText().toString().startsWith("0") &
                                            Float.parseFloat(animalWeight.getText().toString()) <= 2555.999) {
                                        // если дробное число
                                        if (animalWeight.getText().toString().contains(".") &
                                                (animalWeight.getText().toString().length() -
                                                        animalWeight.getText().toString().indexOf(".") - 1 <= 3)) {
                                            isWeightCorrect = true;
                                            // если целое число
                                        } else if (!animalWeight.getText().toString().contains(".") &
                                                Float.parseFloat(animalWeight.getText().toString()) <= 2555.999) {
                                            isWeightCorrect = true;
                                        }
                                    } else {
                                        animalWeight.setText("");
                                        weightWarningToast.show();
                                        weightWarning = true;
                                    }
                                }
                            } else { // если некорректно указан вес
                                animalWeight.setText("");
                                weightWarningToast.show();
                                weightWarning = true;
                            }
                        }

                    if (!weightWarning | isWeightCorrect | (animalWeight.getText().toString().equals("")
                            & !weightWarning)){
                        weightWarningToast.cancel();
                        System.out.println("здессс");
                        if (animalWeight.getText().toString().equals("")){
                            animal.setWeight(0);
                        }
                        animal.setAnimalName(String.valueOf(animalName.getText()));
                        animal.setFemale(animalSexSwitch.isChecked());
                        if (isWeightCorrect) {
                            System.out.println("здесь");
                            animal.setWeight(Float.parseFloat(animalWeight.getText().toString()));
                            MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);
                        } else {
                            System.out.println("здесьв");
                            MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);
                        }
                        finishActivity();
                    }
                }
            });
        }
    }

    void finishActivity(){
        finishAffinity();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("animalFragment", true);
        startActivity(intent);
    }
}