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

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.myfarm.db.AnimalDatabase;
import com.myfarm.db.AnimalTypeDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class newAnimalPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_animal_page);

        AnimalTypeDatabase animalTypeDatabase = Room.databaseBuilder(getApplicationContext(),
                AnimalTypeDatabase.class, "animalType-database").allowMainThreadQueries().build();
        AnimalDatabase animalDatabase = Room.databaseBuilder(getApplicationContext(),
                AnimalDatabase.class, "animal-database").allowMainThreadQueries().build();

        setContentView(R.layout.activity_new_animal_page);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.addAll(animalTypeDatabase.animalTypeDao().getAnimalTypeNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button datePickButton = findViewById(R.id.datePickerButton);
        datePickButton.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        TextView birthDateText = findViewById(R.id.textAnimalBirthdate);

        Button resetButton = findViewById(R.id.resetBirthdate);
        resetButton.setOnClickListener(view -> {
            findViewById(R.id.editTextNumberSigned).setEnabled(true);
            findViewById(R.id.monthBirthTextSigned).setEnabled(true);
            birthDateText.setText("_______________");

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

        TextView animalNameText = findViewById(R.id.editAnimalName);
        Switch sexSwitch = findViewById(R.id.animalSex);

        Button enterButton = findViewById(R.id.addNewAnimalButton);
        enterButton.setOnClickListener(view -> {

            com.myfarm.db.Animal newAnimal = new com.myfarm.db.Animal(String.valueOf(animalNameText.getText()),
                    Integer.parseInt(String.valueOf(spinner.getSelectedItemId())),
                    "20" + birthDateText.getText().toString().substring(
                            birthDateText.getText().toString().lastIndexOf(".") + 1)
                            + "-" + birthDateText.getText().toString().substring( // ошибка с индексацией
                                    birthDateText.getText().toString().indexOf(".") + 1,
                            birthDateText.getText().toString().lastIndexOf("."))
                            + "-" + birthDateText.getText().toString().substring(0,
                            birthDateText.getText().toString().indexOf(".")),
                    sexSwitch.isSelected());
            animalDatabase.animalDao().insertAll(newAnimal);

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
        findViewById(R.id.editTextNumberSigned).setEnabled(false);
        findViewById(R.id.monthBirthTextSigned).setEnabled(false);
    }
}