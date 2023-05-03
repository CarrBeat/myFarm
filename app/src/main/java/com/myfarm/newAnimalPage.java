package com.myfarm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.room.Room;

import com.myfarm.db.AnimalDatabase;
import com.myfarm.db.AnimalTypeDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class newAnimalPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_animal_page);

        AnimalTypeDatabase animalTypeDatabase = Room.databaseBuilder(getApplicationContext(),
                AnimalTypeDatabase.class, "animalType-database").allowMainThreadQueries().build();
        AnimalDatabase animalDatabase = Room.databaseBuilder(getApplicationContext(),
                AnimalDatabase.class, "animal-database").allowMainThreadQueries().build();

        setContentView(R.layout.activity_new_animal_page);

        TextView animalNameText = findViewById(R.id.editAnimalName);
        TextView fatYearsText = findViewById(R.id.fatYearsText);
        TextView monthBirthEditText = findViewById(R.id.monthBirthEditText);
        Switch sexSwitch = findViewById(R.id.animalSex);
        Button enterButton = findViewById(R.id.addNewAnimalButton);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.addAll(animalTypeDatabase.animalTypeDao().getAnimalTypeNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button datePickButton = findViewById(R.id.datePickerButton);
        datePickButton.setOnClickListener(view -> {
            if(!fatYearsText.getText().toString().isEmpty() |
                    !monthBirthEditText.getText().toString().isEmpty()){
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

        spinner.setSelection(animalTypeDatabase.animalTypeDao().getAnimalTypeNames().indexOf(
                getIntent().getStringExtra("animalTypeText")));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @SuppressLint("DiscouragedApi")
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                animalPageImage.setImageResource(getResources().getIdentifier(
                        animalTypeDatabase.animalTypeDao().getPhotoNameByAnimalTypeName(
                        (String) spinner.getSelectedItem()), "drawable", getPackageName()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        String animalBirthdate;

        enterButton.setOnClickListener(view -> {
            if(birthDateText.getText().toString().contains(".") |
                    ((fatYearsText.getText().toString().matches("\\b([1-9]|[1-9][0-5])\\b")
                            | fatYearsText.getText().toString().isEmpty()) |
                            monthBirthEditText.getText().toString().matches("\\b([1-9]|1[0-2])\\b"))){

                if ((fatYearsText.getText().toString().matches("\\b([1-9]|[1-9][0-5])\\b")
                        | fatYearsText.getText().toString().isEmpty()) &
                        monthBirthEditText.getText().toString().matches("\\b([1-9]|1[0-2])\\b")){
                    int days = 0;
                    if (!fatYearsText.getText().toString().isEmpty()){ // ищем количество дней жизни животного
                        days = Integer.parseInt(fatYearsText.getText().toString()) * 365;
                    }
                    days += Integer.parseInt(monthBirthEditText.getText().toString()) * 29;
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    cal.add(Calendar.DATE,-days);
                    System.out.println(sdf.format(cal.getTime()));
                } else {
                    animalNameText.setText("НЕ УКАЗАН ВОЗРАСТ!");
                }
            }


            /*

            com.myfarm.db.Animal newAnimal = new com.myfarm.db.Animal(String.valueOf(animalNameText.getText()),
                    Integer.parseInt(String.valueOf(spinner.getSelectedItemId())),
                    "20" + birthDateText.getText().toString().substring(
                            birthDateText.getText().toString().lastIndexOf(".") + 1)
                            + "-" + birthDateText.getText().toString().substring(
                                    birthDateText.getText().toString().indexOf(".") + 1,
                            birthDateText.getText().toString().lastIndexOf("."))
                            + "-" + birthDateText.getText().toString().substring(0,
                            birthDateText.getText().toString().indexOf(".")),
                    sexSwitch.isSelected());
            animalDatabase.animalDao().insertAll(newAnimal);
            */
            System.out.println(animalDatabase.animalDao().getAllAnimals());
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        TextView textView = findViewById(R.id.textAnimalBirthdate);
        textView.setText(selectedDate);
        System.out.println("20" + selectedDate.substring(selectedDate.lastIndexOf(".") + 1)
                + "-" + selectedDate.substring(selectedDate.indexOf(".") + 1, selectedDate.lastIndexOf("."))
                + "-" + selectedDate.substring(0, selectedDate.indexOf(".")));
        findViewById(R.id.fatYearsText).setEnabled(false);
        findViewById(R.id.monthBirthEditText).setEnabled(false);
    }
}