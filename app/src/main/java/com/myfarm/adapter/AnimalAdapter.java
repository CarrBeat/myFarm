package com.myfarm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myfarm.AnimalsFragment;
import com.myfarm.Common;
import com.myfarm.R;
import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalHolder> {
    private List<Animal> animals = new ArrayList<>();
    private AnimalsFragment animalsFragment = new AnimalsFragment();


    @NonNull
    @Override
    public AnimalAdapter.AnimalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animal_item, parent, false);
        return new AnimalHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AnimalAdapter.AnimalHolder holder, int position) {
        Animal currentAnimal = animals.get(position);

        String animalTypeName = MyFarmDatabase.getDatabase(animalsFragment.getContext())
                .animalDao().getAnimalTypeName(currentAnimal.getAnimalTypeID());

        if (animalTypeName.contains("/")){
            if (currentAnimal.getFemale()){
                animalTypeName = animalTypeName.substring(0, animalTypeName.indexOf("/")).toUpperCase();
                if (!Objects.equals(currentAnimal.getAnimalName(), "")){
                    holder.mainText.setText(animalTypeName + " | " + currentAnimal.getAnimalName());
                } else {
                    holder.mainText.setText(animalTypeName);
                }
            } else {
                animalTypeName = animalTypeName.substring(animalTypeName.lastIndexOf("/") + 1).toUpperCase();
                if (!Objects.equals(currentAnimal.getAnimalName(), "")){
                    holder.mainText.setText(animalTypeName + " | " + currentAnimal.getAnimalName());
                } else {
                    holder.mainText.setText(animalTypeName);
                }
            }
        } else {
            if (!Objects.equals(currentAnimal.getAnimalName(), "")){
                holder.mainText.setText(animalTypeName.toUpperCase() + " | " + currentAnimal.getAnimalName());
            } else {
                holder.mainText.setText(animalTypeName.toUpperCase());
            }
        }


        if(currentAnimal.getWeight() > 0.0){
            holder.rightTopText.setText(currentAnimal.getWeight() + " кг");
        } else {
            holder.rightTopText.setText("- кг");
        }

        if (currentAnimal.getFemale()){
            if (currentAnimal.getPregnancyID() > 0) {
                try {
                    holder.bottom_text.setText("Самка | " + Common.getNormalDate(currentAnimal.getBirthdate())
                            + " | Ожидает потомство!");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    holder.bottom_text.setText("Самка | " + Common.getNormalDate(currentAnimal.getBirthdate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                holder.bottom_text.setText("Самец | " + Common.getNormalDate(currentAnimal.getBirthdate()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickListener.onAnimalClick(currentAnimal, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
        notifyDataSetChanged();
    }

    public interface OnAnimalClickListener{
        void onAnimalClick(Animal animal, int position);
    }

    private final OnAnimalClickListener onClickListener;

    public AnimalAdapter(Context context, OnAnimalClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class AnimalHolder extends RecyclerView.ViewHolder {
        private final TextView mainText;
        private final TextView bottom_text;
        private final TextView rightTopText;

        public AnimalHolder(View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.text_main_info);
            bottom_text = itemView.findViewById(R.id.text_add_info);
            rightTopText = itemView.findViewById(R.id.right_top_text);
        }
    }
}
