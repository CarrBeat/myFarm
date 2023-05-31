package com.myfarm;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.myfarm.adapter.PregnancyAdapter;
import com.myfarm.db.Animal;
import com.myfarm.db.AnimalDao;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.Pregnancy;
import com.myfarm.db.PregnancyDao;

import org.w3c.dom.Text;

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

        CheckBox deleteBox = view.findViewById(R.id.delete_pregn_сheckbox);

        // нажатие на элемент с информацией о беременности
        PregnancyAdapter.OnPregnancyClickListener pregnancyClickListener = (pregnancy, position) -> {
            if (deleteBox.isChecked()){
                Animal animal = MyFarmDatabase.getDatabase(requireActivity().getApplication()).animalDao()
                                .getAnimalByPregnancy(pregnancy.getIdPregnancy());
                animal.setPregnancyID(1);
                MyFarmDatabase.getDatabase(requireActivity().getApplication()).animalDao().updateAnimal(animal);
                if (pregnancy.getNotify()){
                    try {
                        removeEvent(156);
                    } catch (Exception exception){
                        exception.printStackTrace();
                    }

                }
                MyFarmDatabase.getDatabase(requireActivity().getApplication()).pregnancyDao().delete(pregnancy);
                reopenFragment();
            }
        };

        final PregnancyAdapter pregnancyAdapter = new PregnancyAdapter(requireActivity().getApplication(),
                pregnancyClickListener);

        animalDao = MyFarmDatabase.getDatabase(requireActivity().getApplication()).animalDao();
        pregnancyDao = MyFarmDatabase.getDatabase(requireActivity().getApplication()).pregnancyDao();
        executorService = Executors.newSingleThreadExecutor();

        // заполнение списка информацией о беременности
        if (pregnancyDao.getAllPregnancies().size() > 1){
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

    void removeEvent(long eventID){
        MainActivity mainActivity = new MainActivity();
        ContentResolver cr = requireActivity().getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        cr.delete(deleteUri, null, null);
    }

    void reopenFragment(){
        Fragment newFragment = new PregnancyFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}