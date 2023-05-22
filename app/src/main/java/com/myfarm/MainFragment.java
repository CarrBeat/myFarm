package com.myfarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainFragment extends Fragment {
    String[] animalNames;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Spinner spinner = view.findViewById(R.id.select_animal_spinner);

        List<Animal> animals = MyFarmDatabase.getDatabase(getContext()).animalDao().getAllAnimals();
        List<String> animalTypes = MyFarmDatabase.getDatabase(getContext()).animalTypeDao().getAnimalTypeNames();
        Animal currentAnimal;

        Map<String, Integer> animalsMap = new HashMap<>();;
        ArrayList<String> animalArray = new ArrayList<>();

        for (int i = 0; i < animals.size(); i++){
            currentAnimal = animals.get(i);

            if(!Objects.equals(currentAnimal.getAnimalName(), "")){
                animalArray.add(animalTypes.get(currentAnimal.getAnimalTypeID()) + currentAnimal.getAnimalName());
            } else {
                animalArray.add(animalTypes.get(currentAnimal.getAnimalTypeID()));
            }
            animalsMap.put(animalArray.get(i), currentAnimal.getIdAnimal());

        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, animalArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return view;
    }
}