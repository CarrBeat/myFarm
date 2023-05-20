package com.myfarm.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.myfarm.PregnancyFragment;
import com.myfarm.R;
import com.myfarm.db.Animal;
import com.myfarm.db.Pregnancy;

import java.util.ArrayList;
import java.util.List;

public class PregnancyAdapter extends RecyclerView.Adapter<PregnancyAdapter.PregnancyHolder>{

    private List<Pregnancy> pregnancies = new ArrayList<>();
    private List<Animal> animals = new ArrayList<>();
    private PregnancyFragment pregnancyFragment = new PregnancyFragment();


    @NonNull
    @Override
    public PregnancyAdapter.PregnancyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pregn_item, parent, false);
        return new PregnancyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PregnancyAdapter.PregnancyHolder holder, int position) {
        Pregnancy currentPregnancy;
        Animal currentAnimal = animals.get(position);

        if (currentAnimal.getPregnancyID() > 0){
           currentPregnancy = pregnancies.get(currentAnimal.getPregnancyID());
           System.out.println(currentPregnancy.getApproximatelyChildbirth());
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
