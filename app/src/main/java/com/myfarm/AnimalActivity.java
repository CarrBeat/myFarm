package com.myfarm;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.myfarm.db.Animal;

public class AnimalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_page);

        Bundle arguments = getIntent().getExtras();

        Animal animal;
        if(arguments!=null){
            animal = (Animal) arguments.getSerializable(Animal.class.getSimpleName());
            System.out.println(animal.getBirthdate());
        }

    }


}