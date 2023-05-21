package com.myfarm;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.myfarm.adapter.AnimalTypeAdapter;
import com.myfarm.adapter.CategoryAdapter;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.Pregnancy;
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
    private MainFragment mainFragment = new MainFragment();
    Boolean animalsInSystem = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // если нужно сразу открыть animalFragment (после добавления животного)
        boolean startAnimalFragment = getIntent().getBooleanExtra("animalFragment", false);

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Птицы"));
        categoryList.add(new Category(2, "Рогатый скот"));
        categoryList.add(new Category(3, "Иные млекопитающие"));
        categoryList.add(new Category(4, "Все"));

        animalList.add(new AnimalType(1, "cow", "Корова/бык", "#b8860b", 2));
        animalList.add(new AnimalType(2, "sheep", "Овца/баран", "#b8860b", 2));
        animalList.add(new AnimalType(3, "goat", "Коза/козёл", "#b8860b", 2));
        animalList.add(new AnimalType(4, "chicken", "Курица/петух", "#00bfff", 1));
        animalList.add(new AnimalType(5, "quail", "Перепел", "#00bfff", 1));
        animalList.add(new AnimalType(6, "ducks", "Утка/селезень", "#00bfff", 1));
        animalList.add(new AnimalType(7, "geese", "Гусь", "#00bfff", 1));
        animalList.add(new AnimalType(8, "turkey", "Индейка", "#00bfff", 1));
        animalList.add(new AnimalType(9, "ostrich", "Страус", "#00bfff", 1));
        animalList.add(new AnimalType(10, "pig", "Свинья", "#90ee90",3));
        animalList.add(new AnimalType(12, "nutria", "Нутрия", "#90ee90",3));
        animalList.add(new AnimalType(13, "rabbit", "Кролик", "#90ee90", 3));

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (prefs.getBoolean("isFirstRun", true)) {
            setContentView(R.layout.greetings_activity_main);

            com.myfarm.db.AnimalType cow = new com.myfarm.db.AnimalType("Корова/бык",
                    "250-310", "cow");
            com.myfarm.db.AnimalType sheep = new com.myfarm.db.AnimalType("Овца/баран",
                    "147-150", "sheep");
            com.myfarm.db.AnimalType goat = new com.myfarm.db.AnimalType("Коза/козёл",
                    "152","goat");
            com.myfarm.db.AnimalType chicken = new com.myfarm.db.AnimalType("Курица/петух",
                    "21", "chicken");
            com.myfarm.db.AnimalType quail = new com.myfarm.db.AnimalType("Перепёлка/перепел",
                    "17-23", "quail");
            com.myfarm.db.AnimalType duck = new com.myfarm.db.AnimalType("Утка/селезень",
                    "22-40", "ducks");
            com.myfarm.db.AnimalType goose = new com.myfarm.db.AnimalType("Гусыня/гусь",
                    "28-32", "geese");
            com.myfarm.db.AnimalType turkey = new com.myfarm.db.AnimalType("Индейка/индюк",
                    "28", "turkey");
            com.myfarm.db.AnimalType ostrich = new com.myfarm.db.AnimalType("Страус",
                    "35-45", "ostrich");
            com.myfarm.db.AnimalType pig = new com.myfarm.db.AnimalType("Свинья",
                    "117",  "pig");
            com.myfarm.db.AnimalType nutria = new com.myfarm.db.AnimalType("Нутрия",
                    "127-132", "nutria");
            com.myfarm.db.AnimalType rabbit = new com.myfarm.db.AnimalType("Кролик",
                    "28-35", "rabbit");

            MyFarmDatabase.getDatabase(this).animalTypeDao().insertAll(cow, sheep, goat, chicken, quail, duck, goose,
                    turkey, ostrich, pig, nutria, rabbit);

            Pregnancy pregnancy = new Pregnancy("-", false);
            MyFarmDatabase.getDatabase(this).pregnancyDao().insert(pregnancy);

        }
        prefs.edit().putBoolean("isFirstRun", false).apply();

        if (MyFarmDatabase.getDatabase(this).animalDao().getAllAnimals().toString().equals("[]")){

            setContentView(R.layout.greetings_activity_main);
            fullAnimalList.addAll(animalList);
            setAnimalTypeRecycler(animalList);
            setCategoryRecycler(categoryList);

            Button mainButton = findViewById(R.id.main_button);
            Button animalsButton = findViewById(R.id.animals_button);
            Button pregnancyButton = findViewById(R.id.pregnancy_button);
            Button settingsButton = findViewById(R.id.settings_button);

            Toast mainInfoToast = Toast.makeText(this,
                    "Здесь отображаются данные о животных в графическом виде.",
                    Toast.LENGTH_LONG);
            mainInfoToast.setGravity(Gravity.BOTTOM, 0, 160);
            Toast animalsInfoToast = Toast.makeText(this,
                    "Во вкладке \"Животные\" отображаются все добавленные животные в систему, " +
                            "\nесть возможность добавить и удалить животных, " +
                            "\nа также перейти в меню конкретного животного для работы с ним.",
                    Toast.LENGTH_LONG);
            animalsInfoToast.setGravity(Gravity.BOTTOM, 0, 160);
            Toast pregnancyInfoToast = Toast.makeText(this,
                    "В данной вкладке можно просмотреть все беременности, " +
                            "\nа также ознакомиться с подробной информация о каждой из них.",
                    Toast.LENGTH_LONG);
            pregnancyInfoToast.setGravity(Gravity.BOTTOM, 0, 160);
            Toast settingsInfoToast = Toast.makeText(this,
                    "Здесь можно настроить уведомления",
                    Toast.LENGTH_LONG);
            settingsInfoToast.setGravity(Gravity.BOTTOM, 0, 160);

            mainButton.setOnClickListener(view -> {
                animalsInfoToast.cancel();
                pregnancyInfoToast.cancel();
                settingsInfoToast.cancel();
                mainInfoToast.show();
            });
            animalsButton.setOnClickListener(view -> {
                mainInfoToast.cancel();
                pregnancyInfoToast.cancel();
                settingsInfoToast.cancel();
                animalsInfoToast.show();
            });
            pregnancyButton.setOnClickListener(view -> {
                mainInfoToast.cancel();
                animalsInfoToast.cancel();
                settingsInfoToast.cancel();
                pregnancyInfoToast.show();
            });
            settingsButton.setOnClickListener(view -> {
                mainInfoToast.cancel();
                animalsInfoToast.cancel();
                pregnancyInfoToast.cancel();
                settingsInfoToast.show();
            });
        } else {
            animalsInSystem = true;
            if (!startAnimalFragment){
                setContentView(R.layout.activity_main);
                setNewFragment(mainFragment);
            } else {
                setContentView(R.layout.activity_main);
                setAnimalFragment();
            }
        }

        TextView mainText = findViewById(R.id.main_column);
        TextView animalText = findViewById(R.id.animal_column);
        TextView pregnancyText = findViewById(R.id.pregnancy_column);
        TextView settingsText = findViewById(R.id.settings_column);

        mainText.setTypeface(null, Typeface.BOLD);
        Button mainButton = findViewById(R.id.main_button);
        Button animalsButton = findViewById(R.id.animals_button);
        Button pregnancyButton = findViewById(R.id.pregnancy_button);
        Button settingsButton = findViewById(R.id.settings_button);

        if (animalsInSystem){
            animalsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnimalFragment();
                    mainText.setTypeface(null, Typeface.NORMAL);
                    pregnancyText.setTypeface(null, Typeface.NORMAL);
                    settingsText.setTypeface(null, Typeface.NORMAL);
                    animalText.setTypeface(null, Typeface.BOLD);
                }
            });
            mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setNewFragment(mainFragment);
                    mainText.setTypeface(null, Typeface.BOLD);
                    pregnancyText.setTypeface(null, Typeface.NORMAL);
                    settingsText.setTypeface(null, Typeface.NORMAL);
                    animalText.setTypeface(null, Typeface.NORMAL);
                }
            });
            pregnancyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPregnancyFragment();
                    mainText.setTypeface(null, Typeface.NORMAL);
                    animalText.setTypeface(null, Typeface.NORMAL);
                    settingsText.setTypeface(null, Typeface.NORMAL);
                    pregnancyText.setTypeface(null, Typeface.BOLD);
                }
            });
        }
    }

    void setPregnancyFragment(){
        PregnancyFragment pregnancyFragment = new PregnancyFragment();
        setNewFragment(pregnancyFragment);
    }

    void setAnimalFragment(){
        AnimalsFragment animalsFragment = new AnimalsFragment();
        setNewFragment(animalsFragment);
    }

    public void setNewFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

        if (category == 4){
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