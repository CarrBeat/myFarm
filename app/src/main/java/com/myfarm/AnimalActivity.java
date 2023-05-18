package com.myfarm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            animal = (Animal) arguments.getSerializable(Animal.class.getSimpleName());
            TextView animalType = findViewById(R.id.animal_type_text);
            animalType.setText(MyFarmDatabase.getDatabase(this)
                    .animalDao().getAnimalTypeName(animal.getAnimalTypeID()));
            TextView animalAge = findViewById(R.id.age_text);
            animalAge.setText(animal.getBirthdate());
            animalName.setText(animal.getAnimalName());
            animalWeight.setText(String.valueOf(animal.getWeight()));
        }
        // работаю над удалением
        Button removeButton = findViewById(R.id.delete_animal);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFarmDatabase.getDatabase(getApplication())
                        .animalDao().delete(animal);
                finishActivity();
            }
        });

    }

    void finishActivity(){
        finishAffinity();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("animalFragment", true);
        startActivity(intent);
    }
}