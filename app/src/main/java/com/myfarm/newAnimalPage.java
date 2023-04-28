package com.myfarm;

import android.app.DatePickerDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.room.Room;

import com.myfarm.db.AnimalType;
import com.myfarm.db.AnimalTypeDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class newAnimalPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_animal_page);

        AnimalTypeDatabase animalTypeDatabase = Room.databaseBuilder(getApplicationContext(),
                AnimalTypeDatabase.class, "animalType-database").allowMainThreadQueries().build();
        setContentView(R.layout.activity_new_animal_page);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, animalTypeDatabase.animalTypeDao().getAllAnimalTypes());
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        Button button = findViewById(R.id.datePickerButton);
        button.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        Button resetButton = findViewById(R.id.resetBirthdate);
        resetButton.setOnClickListener(view -> {
            findViewById(R.id.editTextNumberSigned).setEnabled(true);
            findViewById(R.id.monthBirthTextSigned).setEnabled(true);
            TextView textView = findViewById(R.id.textAnimalBirthdate);
            textView.setText("_______________");

        });

        ImageView animalPageImage = findViewById(R.id.addAnimalImage);
        TextView animalTypeText = findViewById(R.id.editAnimalType);

        animalPageImage.setImageResource(getIntent().getIntExtra("animalPageImage", 0));
        animalTypeText.setText(getIntent().getStringExtra("animalTypeText"));

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        TextView textView = findViewById(R.id.textAnimalBirthdate);
        textView.setText(currentDate);
        findViewById(R.id.editTextNumberSigned).setEnabled(false);
        findViewById(R.id.monthBirthTextSigned).setEnabled(false);
    }
}