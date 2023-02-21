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
    static List<AnimalType> animalList = new ArrayList<>();
    static List<AnimalType> fullAnimalList = new ArrayList<>();
    static AnimalTypeAdapter animalTypeAdapter;

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


        animalList.add(new AnimalType(1, "cow", "Корова/бык", "#b8860b", 2));
        animalList.add(new AnimalType(2, "sheep", "Овца/баран", "#b8860b", 2));
        animalList.add(new AnimalType(3, "goat", "Коза/козёл", "#b8860b", 2));
        animalList.add(new AnimalType(4, "chicken", "Курица/петух", "#00bfff", 1));
        animalList.add(new AnimalType(5, "quail", "Перепел", "#00bfff", 1));
        animalList.add(new AnimalType(6, "ducks", "Утка/селезень", "#00bfff", 1));
        animalList.add(new AnimalType(7, "geese", "Гусь", "#00bfff", 1));
        animalList.add(new AnimalType(8, "turckey", "Индейка", "#00bfff", 1));
        animalList.add(new AnimalType(9, "ostrich", "Страус", "#00bfff", 1));
        animalList.add(new AnimalType(10, "pig", "Свинья", "#90ee90",3));
        animalList.add(new AnimalType(12, "bees", "Пчёлы", "#90ee90",4));
        animalList.add(new AnimalType(13, "rabbit", "Кролик", "#90ee90", 3));

        fullAnimalList.addAll(animalList);

        setAnimalTypeRecycler(animalList);


    }

    private void setAnimalTypeRecycler(List<AnimalType> animalList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, false);
        animalsRecycler = findViewById(R.id.animal_recycler);
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

    @SuppressLint("NotifyDataSetChanged")
    public static void showSortedAnimalType(int category){

        animalList.clear();
        animalList.addAll(fullAnimalList);

        List<AnimalType> filterAnimalTypes = new ArrayList<>();
        for (AnimalType inWork : animalList){
            if (inWork.getCategory() == category){
                filterAnimalTypes.add(inWork);
            }
        }
        animalList.clear();
        animalList.addAll(filterAnimalTypes);
        animalTypeAdapter.notifyDataSetChanged();
    }

}