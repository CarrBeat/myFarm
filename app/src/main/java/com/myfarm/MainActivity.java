package com.myfarm;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.room.Room;

import com.myfarm.adapter.AnimalTypeAdapter;
import com.myfarm.adapter.CategoryAdapter;
import com.myfarm.db.AnimalTypeDatabase;
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
        categoryList.add(new Category(5, "Все"));
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
        animalList.add(new AnimalType(12, "nutria", "Нутрия", "#90ee90",4));
        animalList.add(new AnimalType(13, "rabbit", "Кролик", "#90ee90", 3));

        fullAnimalList.addAll(animalList);

        setAnimalTypeRecycler(animalList);

        AnimalTypeDatabase animalTypeDatabase = Room.databaseBuilder(getApplicationContext(),
                AnimalTypeDatabase.class, "animalType-database").allowMainThreadQueries().build();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (prefs.getBoolean("isFirstRun", true)) {
            com.myfarm.db.AnimalType cow = new com.myfarm.db.AnimalType("Корова/бык",
                    "250-310", 9, "cow");
            com.myfarm.db.AnimalType sheep = new com.myfarm.db.AnimalType("Овца/баран",
                    "147-150", 0, "sheep");
            com.myfarm.db.AnimalType goat = new com.myfarm.db.AnimalType("Коза/козёл",
                    "152",8, "goat");
            com.myfarm.db.AnimalType chicken = new com.myfarm.db.AnimalType("Курица/петух",
                    "21", 1, "chicken");
            com.myfarm.db.AnimalType quail = new com.myfarm.db.AnimalType("Перепел",
                    "17-23", 1, "quail");
            com.myfarm.db.AnimalType duck = new com.myfarm.db.AnimalType("Утка/селезень",
                    "22-40", 1, "ducks");
            com.myfarm.db.AnimalType goose = new com.myfarm.db.AnimalType("Гусь",
                    "28-32", 1, "geese");
            com.myfarm.db.AnimalType turkey = new com.myfarm.db.AnimalType("Индейка",
                    "28", 1, "turkey");
            com.myfarm.db.AnimalType ostrich = new com.myfarm.db.AnimalType("Страус",
                    "35-45", 0, "ostrich");
            com.myfarm.db.AnimalType pig = new com.myfarm.db.AnimalType("Свинья",
                    "117", 0, "pig");
            com.myfarm.db.AnimalType nutria = new com.myfarm.db.AnimalType("Нутрия",
                    "127-132", 0, "nutria");
            com.myfarm.db.AnimalType rabbit = new com.myfarm.db.AnimalType("Кролик",
                    "28-35", 0, "rabbit");

            animalTypeDatabase.animalTypeDao().insertAll(cow, sheep, goat, chicken, quail, duck, goose,
                    turkey, ostrich, pig, nutria, rabbit);

        }
        prefs.edit().putBoolean("isFirstRun", false).apply();


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

        System.out.println(category);

        if (category == 5){
            filterAnimalTypes.addAll(animalList);
        } else {
            for (AnimalType inWork : animalList){
                if (inWork.getCategory() == category){
                    filterAnimalTypes.add(inWork);
                }
            }
        }

        animalList.clear();
        animalList.addAll(filterAnimalTypes);
        animalTypeAdapter.notifyDataSetChanged();
    }

}