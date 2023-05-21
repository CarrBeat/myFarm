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
        Pregnancy currentPregnancy = pregnancies.get(position);

        int idAnimal = MyFarmDatabase.getDatabase(pregnancyFragment.getContext()).animalDao()
                .getAnimalIDByPregnancy(currentPregnancy.getIdPregnancy());
        String animalName = MyFarmDatabase.getDatabase(pregnancyFragment.getContext()).animalDao()
                .getAnimalName(idAnimal);
        String animalTypeName = (MyFarmDatabase.getDatabase(pregnancyFragment.getContext()).animalDao()
                .getAnimalTypeNameByAnimalID(idAnimal)).toUpperCase();

        String animalID;
        if (Common.showAnimalID){
                animalID = " (№ " + idAnimal + "),";
        } else {
            animalID = "";
        }

        if (animalTypeName.contains("/")){
            animalTypeName = animalTypeName.substring(0, animalTypeName.indexOf("/")).toUpperCase();
            if (!Objects.equals(animalName, "")){
                holder.topText.setText(animalTypeName + " " + animalName + animalID);
               } else {
                   holder.topText.setText(animalTypeName + animalID);
               }
           } else {
               if (!Objects.equals(animalName, "")){
                   holder.topText.setText(animalTypeName + " " + animalName + animalID);
               } else {
                   holder.topText.setText(animalTypeName + animalID);
               }
           }
           if (currentPregnancy.getNotify()){
               try {
                   holder.bottomText.setText("родит с " + Common.getNormalDate(
                           currentPregnancy.getApproximatelyChildbirth()) + ", уведомление вкл.");
               } catch (ParseException e) {
                   throw new RuntimeException(e);
               }
           } else {
               try {
                   holder.bottomText.setText("родит с " + Common.getNormalDate(currentPregnancy.getApproximatelyChildbirth()));
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
    public void setPregnancies(List<Pregnancy> pregnancies) {
        this.pregnancies = pregnancies;
        //notifyDataSetChanged();
    }

    class PregnancyHolder extends RecyclerView.ViewHolder {
        private final TextView topText;
        private final TextView bottomText;

        public PregnancyHolder(View itemView) {
            super(itemView);
            topText = itemView.findViewById(R.id.top_text);
            bottomText = itemView.findViewById(R.id.bottom_text);
        }
    }
}
