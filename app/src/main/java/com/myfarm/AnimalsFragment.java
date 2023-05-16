package com.myfarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.myfarm.adapter.AnimalAdapter;
import com.myfarm.db.Animal;
import com.myfarm.db.AnimalDao;
import com.myfarm.db.MyFarmDatabase;
import java.util.List;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animals, container, false);
        view.findViewById(R.id.recycler_view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final AnimalAdapter animalAdapter = new AnimalAdapter();

        animalDao = MyFarmDatabase.getDatabase(requireActivity().getApplication()).animalDao();
        executorService = Executors.newSingleThreadExecutor();

        animalAdapter.setAnimals(getAllAnimals());
        System.out.println(getAllAnimals());

        recyclerView.setAdapter(animalAdapter);


        return view;
    }
    public List<Animal> getAllAnimals(){ return animalDao.getAllAnimals(); }

}