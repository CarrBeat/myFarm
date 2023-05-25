package com.myfarm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.Statistics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class newAnimalPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_animal_page);

        TextView animalNameText = findViewById(R.id.editAnimalName);
        TextView fatYearsText = findViewById(R.id.fatYearsText);
        TextView monthBirthEditText = findViewById(R.id.monthBirthEditText);
        TextView animalWeight = findViewById(R.id.weightEditText);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch sexSwitch = findViewById(R.id.animal_sex);
        Button enterButton = findViewById(R.id.add_animal_button);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.addAll(MyFarmDatabase.getDatabase(this).animalTypeDao().getAnimalTypeNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Toast selectedBirthdateWarningToast = Toast.makeText(this,
                "Дата рождения \nне может быть в будущем!", Toast.LENGTH_LONG);
        selectedBirthdateWarningToast.setGravity(Gravity.BOTTOM, 0, 160);

        Button datePickButton = findViewById(R.id.datePickerButton);
        datePickButton.setOnClickListener(view -> {
            if (!fatYearsText.getText().toString().isEmpty() |
                    !monthBirthEditText.getText().toString().isEmpty()) {
                fatYearsText.setText("");
                monthBirthEditText.setText("");
            }
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        TextView birthDateText = findViewById(R.id.textAnimalBirthdate);

        Button resetButton = findViewById(R.id.resetBirthdate);
        resetButton.setOnClickListener(view -> {
            findViewById(R.id.fatYearsText).setEnabled(true);
            findViewById(R.id.monthBirthEditText).setEnabled(true);
            birthDateText.setText("_______________");
            animalNameText.setText("");
        });

        ImageView animalPageImage = findViewById(R.id.addAnimalImage);

        animalPageImage.setImageResource(getIntent().getIntExtra("animalPageImage", 0));

        spinner.setSelection(MyFarmDatabase.getDatabase(this).animalTypeDao().getAnimalTypeNames().indexOf(
                getIntent().getStringExtra("animalTypeText")));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("DiscouragedApi")
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                animalPageImage.setImageResource(getResources().getIdentifier(
                        MyFarmDatabase.getDatabase(getApplication()).animalTypeDao().getPhotoNameByAnimalTypeName(
                                (String) spinner.getSelectedItem()), "drawable", getPackageName()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        enterButton.setOnClickListener(view -> {
            @SuppressLint("CutPasteId")
            TextView animalTextView = findViewById(R.id.textAnimalBirthdate);
            String animalBirthdate = null;
            boolean isWeightCorrect = false;
            boolean weightWarning = false;
            Toast birthdateWarningToast = Toast.makeText(this,
                    "Выберите дату рождения животного \nили введите месяц рождения " +
                            "\nи возраст (если он более 1 года)!",
                    Toast.LENGTH_LONG);
            birthdateWarningToast.setGravity(Gravity.BOTTOM, 0, 160);

            // проверка ввода даты рождения
            if (birthDateText.getText().toString().matches("[0-9]{4}-(0[1-9]|1[012])-(0[1-9]" +
                    "|1[0-9]|2[0-9]|3[01])") |
                    ((fatYearsText.getText().toString().matches("\\b([1-9]|[1-9][0-5])\\b")
                            | fatYearsText.getText().toString().isEmpty()) &
                            monthBirthEditText.getText().toString().matches("\\b([1-9]|1[0-2])\\b"))) {
                // проверка лет жизни и месяца рождения на корректность
                if ((fatYearsText.getText().toString().matches("\\b([1-9]|[1-9][0-5])\\b")
                        | fatYearsText.getText().toString().isEmpty()) &
                        monthBirthEditText.getText().toString().matches("\\b([1-9]|1[0-2])\\b")) {

                    int days = 0;
                    // ищем количество дней жизни животного

                    if (!fatYearsText.getText().toString().isEmpty()) {
                        days = Integer.parseInt(fatYearsText.getText().toString()) * 365;
                    }
                    days += 365 - Integer.parseInt(monthBirthEditText.getText().toString()) * 29;
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    cal.add(Calendar.DATE, -days);
                    animalBirthdate = sdf.format(cal.getTime());
                } else {
                    if (daysBetweenCalc(animalTextView.getText().toString()) <= 0){
                        animalBirthdate = animalTextView.getText().toString();
                    }
                }

                Toast weightWarningToast = Toast.makeText(this,
                        "Масса животного должна быть " +
                                "\nот 0.005 кг до 2555.999 кг! \n(точность до 1 гр)",
                        Toast.LENGTH_LONG);
                weightWarningToast.setGravity(Gravity.BOTTOM, 0, 160);

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
                                if(animalWeight.getText().toString().contains(".") &
                                        (animalWeight.getText().toString().length() -
                                                animalWeight.getText().toString().indexOf(".") - 1 <= 3)){
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

                // заключительная проверка перед добавлением
                if (animalBirthdate != null){
                    if (animalBirthdate.matches("[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])")){
                        if (isWeightCorrect){
                            Animal newAnimal = new Animal(String.valueOf(animalNameText.getText()),
                                    Integer.parseInt(String.valueOf(spinner.getSelectedItemId() + 1)),
                                    animalBirthdate, sexSwitch.isChecked(),
                                    Float.parseFloat(animalWeight.getText().toString()));
                            MyFarmDatabase.getDatabase(this).animalDao().insertAll(newAnimal);
                            Calendar calendar = Calendar.getInstance();
                            MyFarmDatabase.getDatabase(this).statisticsDao().insertAll(
                                    new Statistics(DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()),
                                    newAnimal.getIdAnimal(), Float.parseFloat(animalWeight.getText().toString())));
                        } else if (!weightWarning) {
                            Animal newAnimal = new Animal(String.valueOf(animalNameText.getText()),
                                    Integer.parseInt(String.valueOf(spinner.getSelectedItemId() + 1)),
                                    animalBirthdate, sexSwitch.isChecked());
                            MyFarmDatabase.getDatabase(this).animalDao().insertAll(newAnimal);
                        }
                        finishActivity();
                    } else {
                        birthdateWarningToast.show();
                    }
                } else {
                    selectedBirthdateWarningToast.show();
                    animalTextView.setText("___________");
                    findViewById(R.id.fatYearsText).setEnabled(true);
                    findViewById(R.id.monthBirthEditText).setEnabled(true);
                }
            } else {
                birthdateWarningToast.show();
            }
        });
    }

    int daysBetweenCalc(String selectedDate){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        int daysBetween;
        try {
            String currentDateS = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
            Date currentDate = sdf.parse("20" + currentDateS.substring(currentDateS.lastIndexOf(".") + 1)
                    + "-" + currentDateS.substring(currentDateS.indexOf(".") + 1,
                    currentDateS.lastIndexOf(".")) + "-" + currentDateS.substring(0,
                    currentDateS.indexOf(".")));
            Date animalBirthdateToCheck = sdf.parse(selectedDate);
            assert animalBirthdateToCheck != null;
            assert currentDate != null;
            long diff = animalBirthdateToCheck.getTime() - currentDate.getTime();
            daysBetween = Integer.parseInt(""+(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return daysBetween;
    }

    void finishActivity(){
        finishAffinity();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("animalFragment", true);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        System.out.println(selectedDate);
        TextView textView = findViewById(R.id.textAnimalBirthdate);
        textView.setText("20" + selectedDate.substring(selectedDate.lastIndexOf(".") + 1)
                + "-" + selectedDate.substring(selectedDate.indexOf(".") + 1,
                selectedDate.lastIndexOf(".")) + "-" + selectedDate.substring(0,
                selectedDate.indexOf(".")));
        findViewById(R.id.fatYearsText).setEnabled(false);
        findViewById(R.id.monthBirthEditText).setEnabled(false);
    }
}