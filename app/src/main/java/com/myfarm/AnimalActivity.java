package com.myfarm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

        Toast deleteAnimalWarning = Toast.makeText(this,
                "Для удаления необходимо поставить галочку в верхнем правом углу, \n" +
                        "а затем зажать кнопку удаления на несколько секунд!",
                Toast.LENGTH_LONG);
        deleteAnimalWarning.setGravity(Gravity.BOTTOM, 0, 160);

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

        Button removeButton = findViewById(R.id.delete_animal);
        CheckBox deleteConfirm = findViewById(R.id.delete_confirm);
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




    }

    void finishActivity(){
        finishAffinity();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("animalFragment", true);
        startActivity(intent);
    }
}