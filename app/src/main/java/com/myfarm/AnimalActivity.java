package com.myfarm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.Pregnancy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AnimalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Animal animal;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_page);
        EditText animalName = findViewById(R.id.edit_animal_name);
        EditText animalWeight = findViewById(R.id.weight_edit_text);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch animalSexSwitch = findViewById(R.id.animal_sex);
        Button pregnancyButton = findViewById(R.id.add_pregnancy_button);

        Toast pregnancyWarningToast = Toast.makeText(this,
                "Возраст животного должен быть не менее 150 дней!",
                Toast.LENGTH_LONG);
        pregnancyWarningToast.setGravity(Gravity.BOTTOM, 0, 160);

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
                if (animal.getPregnancyID() > 0){
                    try {
                        pregnancyButton.setText("Роды с " + Common.getNormalDate(MyFarmDatabase.getDatabase(getApplication()).
                                pregnancyDao().getPregnancyById(animal.getPregnancyID())));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
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


            pregnancyButton.setOnClickListener(view -> {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                pregnancyButton.setEnabled(false);
                try {
                    pregnancyButton.setText("Роды с " + Common.getNormalDate(MyFarmDatabase.getDatabase(getApplication()).
                            pregnancyDao().getPregnancyById(animal.getPregnancyID())));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });


            weightCalcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplication(), CalculatorActivity.class);
                    intent.putExtra(Animal.class.getSimpleName(), animal);
                    startActivity(intent);
                }
            });

            removeButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (deleteConfirm.isChecked()) {
                        if (animal.getPregnancyID() > 0){
                            MyFarmDatabase.getDatabase(getApplication()).pregnancyDao().deletePregnancyById(animal.getPregnancyID());
                        }
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
                        if (animalWeight.getText().toString().equals("")){
                            animal.setWeight(0);
                        }
                        animal.setAnimalName(String.valueOf(animalName.getText()));
                        animal.setFemale(animalSexSwitch.isChecked());
                        if (isWeightCorrect) {
                            animal.setWeight(Float.parseFloat(animalWeight.getText().toString()));
                            MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);
                        } else {
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String pregnancyStartDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        pregnancyStartDate = "20" + pregnancyStartDate.substring(pregnancyStartDate.lastIndexOf(".") + 1)
                + "-" + pregnancyStartDate.substring(pregnancyStartDate.indexOf(".") + 1,
                pregnancyStartDate.lastIndexOf(".")) + "-" + pregnancyStartDate.substring(0,
                pregnancyStartDate.indexOf("."));
        if (daysBetweenCalc(pregnancyStartDate) > 150){
            try {
                setNewPregnancy(pregnancyStartDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast pregnancyWarningToast = Toast.makeText(this,
                    "Возраст животного должен быть не менее 150 дней!",
                    Toast.LENGTH_LONG);
            pregnancyWarningToast.setGravity(Gravity.BOTTOM, 0, 160);
            pregnancyWarningToast.show();
        }
        // нужно сравнить дату начала беременности с возрастом и предупреждение сделать, а затем уж добавлять беременность
    }

    int daysBetweenCalc(String pregnancyStart){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        int daysBetween;
        try {
            Date birthDate = sdf.parse(animal.getBirthdate());
            Date PregnancyStartDate = sdf.parse(pregnancyStart);
            assert PregnancyStartDate != null;
            assert birthDate != null;
            long diff = PregnancyStartDate.getTime() - birthDate.getTime();
            daysBetween = Integer.parseInt(""+(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return daysBetween;
    }

    // добавление новой беременности
    void setNewPregnancy(String startPregnancyDate) throws ParseException {
        // получили срок беременности
        int pregnancyPeriod = MyFarmDatabase.getDatabase(getApplication()).
                animalTypeDao().getPregnancyPeriodByAnimalTypeID(animal.getAnimalTypeID());

        // расчёт даты беременности
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(startPregnancyDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, pregnancyPeriod);

        // непосредственно добавление в БД
        Pregnancy newPregnancy = new Pregnancy(formatter.format(c.getTime()), true);
        animal.setPregnancyID((int) MyFarmDatabase.getDatabase(getApplication()).pregnancyDao().insert(newPregnancy));
        MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);
    }

}