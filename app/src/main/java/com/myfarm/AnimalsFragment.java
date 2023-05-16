package com.myfarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myfarm.adapter.AnimalAdapter;
import com.myfarm.db.Animal;
import com.myfarm.db.AnimalDao;
import com.myfarm.db.MyFarmDatabase;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import io.reactivex.rxjava3.annotations.Nullable;


public class AnimalsFragment extends Fragment {
    private AnimalDao animalDao;
    private ExecutorService executorService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals, container, false);
        view.findViewById(R.id.recycler_view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final AnimalAdapter animalAdapter = new AnimalAdapter();
        animalDao = MyFarmDatabase.getDatabase(requireActivity().getApplication()).animalDao();
        executorService = Executors.newSingleThreadExecutor();
        animalAdapter.setAnimals(getAllAnimals());
        recyclerView.setAdapter(animalAdapter);

        Button addAnimalButton = view.findViewById(R.id.open_add_animal_activity_button);
        addAnimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), newAnimalPage.class);
                startActivity(intent);
            }
        });

        return view;
    }
    public List<Animal> getAllAnimals(){ return animalDao.getAllAnimals(); }

}