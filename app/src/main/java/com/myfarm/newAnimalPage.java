package com.myfarm;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class newAnimalPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_animal_page);

        ImageView animalPageImage = findViewById(R.id.addAnimalImage);
        TextView animalTypeText = findViewById(R.id.editAnimalType);

        animalPageImage.setImageResource(getIntent().getIntExtra("animalPageImage", 0));
        animalTypeText.setText(getIntent().getStringExtra("animalTypeText"));

    }
}