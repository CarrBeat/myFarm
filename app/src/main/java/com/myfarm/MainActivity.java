package com.myfarm;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.myfarm.adapter.AnimalTypeAdapter;
import com.myfarm.adapter.CategoryAdapter;
import com.myfarm.model.AnimalType;
import com.myfarm.model.Category;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView categoryRecycler, animalsRecycler;
    CategoryAdapter categoryAdapter;
    AnimalTypeAdapter animalTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Птицы"));
        categoryList.add(new Category(2, "Рогатый скот"));
        categoryList.add(new Category(3, "Иные млекопитающие"));
        categoryList.add(new Category(4, "Прочие"));
        setCategoryRecycler(categoryList);

        List<AnimalType> animalList = new ArrayList<>();
        animalList.add(new AnimalType(1, "cow", "Корова", "#ffff00"));
        setAnimalTypeRecycler(animalList);


    }

    private void setAnimalTypeRecycler(List<AnimalType> animalList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, false);
        animalsRecycler = findViewById(R.id.animalsRecycler);
        animalsRecycler.setLayoutManager(layoutManager);

        animalTypeAdapter = new AnimalTypeAdapter(this, animalList);
        animalsRecycler.setAdapter(animalTypeAdapter);
    }


    private void setCategoryRecycler(List<Category> categoryList) {

        // организуем горизонтальный вывод:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, false);
        categoryRecycler = findViewById(R.id.recycler_view);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecycler.setAdapter(categoryAdapter);
    }
}