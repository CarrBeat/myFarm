package com.myfarm.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfarm.Common;
import com.myfarm.PregnancyFragment;
import com.myfarm.R;
import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.Pregnancy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PregnancyAdapter extends RecyclerView.Adapter<PregnancyAdapter.PregnancyHolder>{

    private List<Pregnancy> pregnancies = new ArrayList<>();
    private List<Animal> animals = new ArrayList<>();
    private final PregnancyFragment pregnancyFragment = new PregnancyFragment();


    @NonNull
    @Override
    public PregnancyAdapter.PregnancyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pregn_item, parent, false);
        return new PregnancyHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PregnancyAdapter.PregnancyHolder holder, int position) {
        Pregnancy currentPregnancy;
        Animal currentAnimal = animals.get(position);

        String animalTypeName = MyFarmDatabase.getDatabase(pregnancyFragment.getContext())
                .animalDao().getAnimalTypeName(currentAnimal.getAnimalTypeID());



        if (currentAnimal.getPregnancyID() >= 0){
           currentPregnancy = pregnancies.get(currentAnimal.getPregnancyID());
           animalTypeName = animalTypeName.substring(0, animalTypeName.indexOf("/")).toUpperCase();
           if (animalTypeName.contains("/")){
               if (!Objects.equals(currentAnimal.getAnimalName(), "")){
                   holder.mainText.setText(animalTypeName + " " + currentAnimal.getAnimalName()
                           + " ожидает потомство,");
               } else {
                   holder.mainText.setText(animalTypeName + " ожидает потомство,");
               }
           } else {
               if (!Objects.equals(currentAnimal.getAnimalName(), "")){
                   holder.mainText.setText(animalTypeName + " " + currentAnimal.getAnimalName()
                           + " ожидает потомство,");
               } else {
                   holder.mainText.setText(animalTypeName + " ожидает потомство,");
               }
           }
           try {
               holder.bottom_text.setText("роды с " + Common.getNormalDate(currentPregnancy.getApproximatelyChildbirth()));
           } catch (ParseException e) {
               throw new RuntimeException(e);
           }
        }

    }

    @Override
    public int getItemCount() {
        return pregnancies.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
        //notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPregnancies(List<Pregnancy> pregnancies) {
        this.pregnancies = pregnancies;
        //notifyDataSetChanged();
    }

    class PregnancyHolder extends RecyclerView.ViewHolder {
        private final TextView mainText;
        private final TextView bottom_text;
        private final TextView rightTopText;

        public PregnancyHolder(View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.top_left_text);
            bottom_text = itemView.findViewById(R.id.bottom_text);
            rightTopText = itemView.findViewById(R.id.top_right_text);
        }
    }
}
