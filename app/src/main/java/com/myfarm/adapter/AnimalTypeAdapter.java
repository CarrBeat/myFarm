package com.myfarm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myfarm.R;
import com.myfarm.model.AnimalType;
import com.myfarm.newAnimalPage;
import java.util.List;
import io.reactivex.rxjava3.annotations.NonNull;

public class AnimalTypeAdapter extends RecyclerView.Adapter<AnimalTypeAdapter.AnimalTypeViewHolder> {

    Context context;
    List<AnimalType> animalTypesList;

    public AnimalTypeAdapter(Context context, List<AnimalType> animalTypesList) {
        this.context = context;
        this.animalTypesList = animalTypesList;
    }

    @NonNull
    @Override
    public AnimalTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View animalTypeItems = LayoutInflater.from(context).inflate(R.layout.animal_type_item,
                viewGroup, false);
        return new AnimalTypeAdapter.AnimalTypeViewHolder(animalTypeItems);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalTypeViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        @SuppressLint("DiscouragedApi")
        int imageId = context.getResources().getIdentifier(animalTypesList.get(position).getImg(),
                "drawable", context.getPackageName());
        holder.animalTypeImage.setImageResource(imageId);

        holder.animalTypeTitle.setText(animalTypesList.get(position).getTitle());

        holder.backgroundAnimalType.setBackgroundColor(
                Color.parseColor(animalTypesList.get(position).getBackgroundColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, newAnimalPage.class);

                intent.putExtra("animalPageImage", imageId);
                intent.putExtra("animalTypeText", animalTypesList.get(position).getTitle());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animalTypesList.size();
    }

    public static final class AnimalTypeViewHolder extends RecyclerView.ViewHolder {

        LinearLayout backgroundAnimalType;
        ImageView animalTypeImage;
        TextView animalTypeTitle;

        public AnimalTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundAnimalType = itemView.findViewById(R.id.animal_linear_layout);
            animalTypeImage = itemView.findViewById(R.id.animal_type_image);
            animalTypeTitle = itemView.findViewById(R.id.animal_type_text);
        }
    }

}
