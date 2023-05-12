package com.myfarm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.myfarm.db.Animal;
import com.myfarm.db.AnimalDao;
import com.myfarm.db.AnimalDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.annotations.NonNull;

public class AnimalViewModel extends AndroidViewModel {
    private AnimalDao animalDao;
    private ExecutorService executorService;

    public AnimalViewModel(@NonNull Application application){
        super(application);
        animalDao = AnimalDatabase.getDatabase(application).animalDao();
        executorService = Executors.newSingleThreadExecutor();
    }
    public LiveData<List<Animal>> getAllAnimals(){
        return animalDao.getAllAnimals();
    }
    public void saveAnimal(Animal animal){
        executorService.execute(() -> animalDao.insertAll(animal));
    }
    public void deleteAnimal(Animal animal){
        executorService.execute(() -> animalDao.delete(animal));
    }





}
