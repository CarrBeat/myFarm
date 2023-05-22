package com.myfarm;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.Statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainFragment extends Fragment {
    Statistics currentStatistics;
    Boolean putData = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Spinner spinner = view.findViewById(R.id.select_animal_spinner);

        List<Animal> animals = MyFarmDatabase.getDatabase(getContext()).animalDao().getAllAnimals();
        List<String> animalTypes = MyFarmDatabase.getDatabase(getContext()).animalTypeDao().getAnimalTypeNames();
        List<Statistics> statistics = MyFarmDatabase.getDatabase(getContext()).statisticsDao().getAllStatistics();
        Animal currentAnimal;


        Map<String, Integer> animalsMap = new HashMap<>();;
        ArrayList<String> animalArray = new ArrayList<>();
        String animalType;

        for (int i = 0; i < animals.size(); i++){
            currentAnimal = animals.get(i);

            if(animalTypes.get(currentAnimal.getAnimalTypeID()).contains("/")){
                if (currentAnimal.getFemale()){
                    animalType = animalTypes.get(currentAnimal.getAnimalTypeID() - 1).substring(0,
                            animalTypes.get(currentAnimal.getAnimalTypeID() - 1).indexOf("/")).toUpperCase();
                } else {
                    animalType = animalTypes.get(currentAnimal.getAnimalTypeID() - 1)
                            .substring(animalTypes.get(currentAnimal.getAnimalTypeID() - 1)
                                    .lastIndexOf("/") + 1).toUpperCase();
                }
            } else {
                animalType = animalTypes.get(currentAnimal.getAnimalTypeID() - 1);
            }

            if(!Objects.equals(currentAnimal.getAnimalName(), "")){
                animalArray.add(animalType + " " +
                        currentAnimal.getAnimalName() + " № " + currentAnimal.getIdAnimal());
            } else {
                animalArray.add(animalType + " № " + currentAnimal.getIdAnimal());
            }
            animalsMap.put(animalArray.get(i), currentAnimal.getIdAnimal());

        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, animalArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        PieChart pieChart = view.findViewById(R.id.pie_chart);
        ArrayList<PieEntry> weightChart = new ArrayList<>();
        pieChart.setEnabled(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<Integer> statisticsIDList =
                        MyFarmDatabase.getDatabase(getContext()).statisticsDao()
                                .getIDStatisticsByIDAnimal(animalsMap.get(spinner.getSelectedItem()));
                if (statisticsIDList.size() >= 2){
                    for (int j = 0; j < statisticsIDList.size(); j++){
                        currentStatistics = statistics.get(statisticsIDList.get(j) - 1);
                        weightChart.add(new PieEntry(currentStatistics.getWeight(), currentStatistics.getDate()));
                    }
                    putData = true;
                } else {
                    putData = false;

                    return;
                }
                PieDataSet pieDataSet = new PieDataSet(weightChart, "- даты доб. показ.");
                pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(18f);
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Изменение массы");
                pieChart.animateY(2000);
                pieChart.setCenterTextSize(18f);
                pieChart.setEntryLabelTextSize(15f);
                pieChart.setData(null);
                pieChart.setNoDataText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}