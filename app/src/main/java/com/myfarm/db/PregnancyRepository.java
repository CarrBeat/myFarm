package com.myfarm.db;

import android.app.Application;

import java.util.List;

public class PregnancyRepository {
    private PregnancyDao tPregnancyDao;
    private List<Pregnancy> pregnancyDaoList;

    PregnancyRepository(Application application){
        PregnancyDatabase db = PregnancyDatabase.getDatabase(application);
        tPregnancyDao = db.pregnancyDao();
        pregnancyDaoList = tPregnancyDao.getAllPregnancies();
    }
}
