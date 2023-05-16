package com.myfarm.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myfarm.AnimalsFragment;
import com.myfarm.R;
import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;

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
        if (!Objects.equals(currentAnimal.getAnimalName(), "")){
            holder.mainText.setText(MyFarmDatabase.getDatabase(animalsFragment.getContext())
                    .animalDao().getAnimalTypeName(currentAnimal.getAnimalTypeID()) + " | " +
                    currentAnimal.getAnimalName());
        } else {
            holder.mainText.setText(MyFarmDatabase.getDatabase(animalsFragment.getContext())
                    .animalDao().getAnimalTypeName(currentAnimal.getAnimalTypeID()));
        }
        if(currentAnimal.getWeight() > 0.0){
            holder.rightTopText.setText(currentAnimal.getWeight() + " кг");
        } else {
            holder.rightTopText.setText("- кг");
        }
        if (currentAnimal.getFemale()){
            if (currentAnimal.getPregnancyID() > 0) {
                holder.bottom_text.setText("Самка | " + currentAnimal.getBirthdate()
                        + " | Ожидает потомство!");
            } else {
                holder.bottom_text.setText("Самка | " + currentAnimal.getBirthdate());
            }
        } else {
            holder.bottom_text.setText("Самец | " + currentAnimal.getBirthdate());
        }
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
