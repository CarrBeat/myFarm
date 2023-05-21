package com.myfarm;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myfarm.adapter.AnimalAdapter;
import com.myfarm.adapter.PregnancyAdapter;
import com.myfarm.db.AnimalDao;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.PregnancyDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PregnancyFragment extends Fragment {

    AnimalDao animalDao;
    PregnancyDao pregnancyDao;
    private ExecutorService executorService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pregnancy, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.pregn_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        PregnancyAdapter pregnancyAdapter = new PregnancyAdapter();

        animalDao = MyFarmDatabase.getDatabase(requireActivity().getApplication()).animalDao();
        pregnancyDao = MyFarmDatabase.getDatabase(requireActivity().getApplication()).pregnancyDao();
        executorService = Executors.newSingleThreadExecutor();

        if (!pregnancyDao.getAllPregnancies().isEmpty()){
            pregnancyAdapter.setAnimals(animalDao.getAllAnimals());
            pregnancyAdapter.setPregnancies(pregnancyDao.getAllPregnancies());
            recyclerView.setAdapter(pregnancyAdapter);
        } else {
            Toast pregnancyWarning = Toast.makeText(requireActivity(),
                    "Активных беременностей нет!", Toast.LENGTH_LONG);
            pregnancyWarning.setGravity(Gravity.BOTTOM, 0, 160);
            pregnancyWarning.show();
            recyclerView.setAdapter(pregnancyAdapter);
        }

        return view;
    }
}