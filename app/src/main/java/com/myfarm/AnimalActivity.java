package com.myfarm;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.myfarm.db.Animal;
import com.myfarm.db.MyFarmDatabase;
import com.myfarm.db.Pregnancy;
import com.myfarm.db.Statistics;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.annotations.NonNull;

public class AnimalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Animal animal;
    boolean isNotify = true;
    private static final int REQUEST_CODE_PERMISSION_WRITE_CALENDAR = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_page);

        EditText animalName = findViewById(R.id.edit_animal_name);
        EditText animalWeight = findViewById(R.id.weight_edit_text);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch animalSexSwitch = findViewById(R.id.animal_sex);
        Button addPregnancyButton = findViewById(R.id.add_pregnancy_button);
        Button mainButton = findViewById(R.id.animal_main_button);
        Button animalsButton = findViewById(R.id.animal_animals_button);
        Button pregnancyButton = findViewById(R.id.animal_pregnancy_button);

        Toast pregnancyWarningToast = Toast.makeText(this,
                "Возраст животного должен быть не менее 150 дней!",
                Toast.LENGTH_LONG);
        pregnancyWarningToast.setGravity(Gravity.BOTTOM, 0, 160);

        Toast deleteAnimalWarning = Toast.makeText(this,
                "Вначале поставьте галку в правом верхнем углу!",
                Toast.LENGTH_LONG);
        deleteAnimalWarning.setGravity(Gravity.BOTTOM, 0, 160);

        mainButton.setOnClickListener(view -> openFragment(""));

        animalsButton.setOnClickListener(view -> openFragment("animalsFragment"));

        pregnancyButton.setOnClickListener(view -> openFragment("pregnancyFragment"));

        CheckBox notifyBox = findViewById(R.id.notify_checkbox);
        notifyBox.setChecked(true);
        notifyBox.setOnClickListener(view -> isNotify = notifyBox.isChecked());


        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            // передача данных о животном
            animal = (Animal) arguments.getSerializable(Animal.class.getSimpleName());
            TextView animalType = findViewById(R.id.animal_type_text);
            animalType.setText(MyFarmDatabase.getDatabase(this)
                    .animalDao().getAnimalTypeName(animal.getAnimalTypeID()));
            TextView animalAge = findViewById(R.id.age_text);
            animalAge.setText(animal.getBirthdate());
            animalName.setText(animal.getAnimalName());
            ImageView imageView = findViewById(R.id.animal_page_image);
            imageView.setImageResource(getResources().getIdentifier(MyFarmDatabase.getDatabase(this).
                    animalTypeDao().getPhotoNameByIDAnimalType(animal.getAnimalTypeID()), "drawable", getPackageName()));

            if(animal.getFemale()){
                // запрос разрешения к календарю
                if (ContextCompat.checkSelfPermission(AnimalActivity.this,
                        android.Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{ android.Manifest.permission.WRITE_CALENDAR},
                            REQUEST_CODE_PERMISSION_WRITE_CALENDAR);
                }
            }

            if(animal.getPregnancyID() > 1 | !animal.getFemale()){
                addPregnancyButton.setEnabled(false);
                if (animal.getPregnancyID() > 1){
                    // если животное беременно
                    try {
                        String childBirth = Common.getNormalDate(MyFarmDatabase.getDatabase(getApplication()).
                                pregnancyDao().getChildbirthDateByPregnancyID(animal.getPregnancyID()));
                        if (childBirth.contains("/")){
                            childBirth = childBirth.substring(0, childBirth.indexOf("-"));
                        }
                        addPregnancyButton.setText("Роды с " + childBirth);
                        if (MyFarmDatabase.getDatabase(this).pregnancyDao().getNotifyIDByPregnancyID(animal.getPregnancyID()) > 0){
                            notifyBox.setChecked(true);
                        } else {
                            notifyBox.setChecked(false);
                        }
                        notifyBox.setEnabled(false);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (animal.getWeight() == 0.0){
                animalWeight.setText("");
            } else {
                animalWeight.setText(String.valueOf(animal.getWeight()));
            }
            if (animal.getFemale()) {
                animalSexSwitch.toggle();
            }

            Button saveAnimalButton = findViewById(R.id.save_animal_button);
            Button removeButton = findViewById(R.id.delete_animal);
            Button weightCalcButton = findViewById(R.id.open_calc_weight_button);
            CheckBox deleteConfirm = findViewById(R.id.delete_confirm);

            if (animal.getAnimalTypeID() != 1 & animal.getAnimalTypeID() != 10){
                weightCalcButton.setEnabled(false);
            }


            addPregnancyButton.setOnClickListener(view -> {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            });


            weightCalcButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplication(), CalculatorActivity.class);
                intent.putExtra(Animal.class.getSimpleName(), animal);
                startActivity(intent);
            });

            removeButton.setOnLongClickListener(view -> {
                if (deleteConfirm.isChecked()) {
                    if (animal.getPregnancyID() > 1){
                        MyFarmDatabase.getDatabase(getApplication()).pregnancyDao().deletePregnancyById(animal.getPregnancyID());
                    }
                    MyFarmDatabase.getDatabase(getApplication()).animalDao().delete(animal);
                    openFragment("animalsFragment");
                } else {
                    deleteAnimalWarning.show();
                }
                return false;
            });
            removeButton.setOnClickListener(view -> {
                if (deleteConfirm.isChecked()) {
                    Toast.makeText(this, "Для удаления нужно удерживать кнопку!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    deleteAnimalWarning.show();
                }
            });

            Toast weightWarningToast = Toast.makeText(this,
                    "Масса от 0.005 кг до 2555.999 кг! \n(точность до 1 гр)",
                    Toast.LENGTH_LONG);
            weightWarningToast.setGravity(Gravity.BOTTOM, 0, 160);

            saveAnimalButton.setOnClickListener(view -> {
                boolean isWeightCorrect = false;
                boolean weightWarning = false;
                if (!animalWeight.getText().toString().isEmpty()) {
                        // проверка введённого массы на корректность
                        if (animalWeight.getText().toString().matches("^\\$?(\\d+|\\d*\\.\\d+)$")) {
                            // если масса начинается с 0
                            if (animalWeight.getText().toString().startsWith("0") &
                                    animalWeight.getText().toString().indexOf(".") == 1 &
                                    animalWeight.getText().toString().lastIndexOf(".") == 1) {
                                if (Float.parseFloat(animalWeight.getText().toString()) >= 0.005 &
                                        (animalWeight.getText().toString().length() -
                                                animalWeight.getText().toString().indexOf(".") - 1 <= 3)) {
                                    isWeightCorrect = true;
                                } else {
                                    animalWeight.setText("");
                                    weightWarningToast.show();
                                }
                            } else {
                                // если масса не начинается с 0
                                if (!animalWeight.getText().toString().startsWith("0") &
                                        Float.parseFloat(animalWeight.getText().toString()) <= 2555.999) {
                                    // если дробное число
                                    if (animalWeight.getText().toString().contains(".") &
                                            (animalWeight.getText().toString().length() -
                                                    animalWeight.getText().toString().indexOf(".") - 1 <= 3)) {
                                        isWeightCorrect = true;
                                        // если целое число
                                    } else if (!animalWeight.getText().toString().contains(".") &
                                            Float.parseFloat(animalWeight.getText().toString()) <= 2555.999) {
                                        isWeightCorrect = true;
                                    }
                                } else {
                                    animalWeight.setText("");
                                    weightWarningToast.show();
                                    weightWarning = true;
                                }
                            }
                        } else { // если некорректно указан вес
                            animalWeight.setText("");
                            weightWarningToast.show();
                            weightWarning = true;
                        }
                    }

                if (!weightWarning | isWeightCorrect | (animalWeight.getText().toString().equals("")
                        & !weightWarning)){
                    weightWarningToast.cancel();
                    if (animalWeight.getText().toString().equals("")){
                        animal.setWeight(0);
                    }
                    animal.setAnimalName(String.valueOf(animalName.getText()).replace("\n", " "));
                    animal.setFemale(animalSexSwitch.isChecked());
                    // сохранение изменений в БД
                    if (isWeightCorrect) {
                        animal.setWeight(Float.parseFloat(animalWeight.getText().toString()));
                        MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                        MyFarmDatabase.getDatabase(getApplication()).statisticsDao().insertAll(
                                new Statistics(sdf.format(calendar.getTime()),
                                        animal.getIdAnimal(), Float.parseFloat(animalWeight.getText().toString())));
                    } else {
                        MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);
                    }
                    openFragment("animalsFragment");
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String pregnancyStartDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        if (pregnancyStartDate.length() == 8){
            pregnancyStartDate = "20" + pregnancyStartDate.substring(pregnancyStartDate.lastIndexOf(".") + 1)
                    + "-" + pregnancyStartDate.substring(pregnancyStartDate.indexOf(".") + 1,
                    pregnancyStartDate.lastIndexOf(".")) + "-" + pregnancyStartDate.substring(0,
                    pregnancyStartDate.indexOf("."));
        } else {
            pregnancyStartDate = pregnancyStartDate.substring(pregnancyStartDate.lastIndexOf(".") + 1) + "-" +
                    pregnancyStartDate.substring(3, 5) + "-" + pregnancyStartDate.substring(0, 2);
        }

        if (daysBetweenCalc(pregnancyStartDate) > 150){
            try {
                setNewPregnancy(pregnancyStartDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast pregnancyWarningToast = Toast.makeText(this,
                    "Возраст животного должен быть не менее 150 дней!",
                    Toast.LENGTH_LONG);
            pregnancyWarningToast.setGravity(Gravity.BOTTOM, 0, 160);
            pregnancyWarningToast.show();
        }
    }

    int daysBetweenCalc(String pregnancyStart){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        int daysBetween;
        try {
            Date birthDate = sdf.parse(animal.getBirthdate());
            Date PregnancyStartDate = sdf.parse(pregnancyStart);
            assert PregnancyStartDate != null;
            assert birthDate != null;
            long diff = PregnancyStartDate.getTime() - birthDate.getTime();
            daysBetween = Integer.parseInt(""+(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return daysBetween;
    }

    // добавление новой беременности
    @SuppressLint("SetTextI18n")
    void setNewPregnancy(String startPregnancyDate) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(startPregnancyDate);
        Calendar startChildbirthDate = Calendar.getInstance();
        startChildbirthDate.setTime(date);
        String startChildbirth = "";
        String endChildbirth = "";

        String pregnancyPeriod = MyFarmDatabase.getDatabase(getApplication()).
                animalTypeDao().getPregnancyPeriodByAnimalTypeID(animal.getAnimalTypeID());

        // расчёт даты родов
        if (pregnancyPeriod.contains("-")){
            Calendar finishChildBirthDate = Calendar.getInstance();
            finishChildBirthDate.setTime(date);
            startChildbirthDate.add(Calendar.DAY_OF_MONTH, Integer.parseInt(
                    pregnancyPeriod.substring(0, pregnancyPeriod.indexOf("-"))));
            finishChildBirthDate.add(Calendar.DAY_OF_MONTH, Integer.parseInt((
                    pregnancyPeriod.substring(pregnancyPeriod.indexOf("-") + 1)
                    )));
            endChildbirth = formatter.format(finishChildBirthDate.getTime());
        } else {
            startChildbirthDate.add(Calendar.DAY_OF_MONTH, Integer.parseInt(pregnancyPeriod));
        }
        startChildbirth = formatter.format(startChildbirthDate.getTime());


        Pregnancy newPregnancy;


        if (isNotify){
            // создаём событие с напоминанием в календаре
            newPregnancy = new Pregnancy(startChildbirth + "/" + endChildbirth,
                    createEvent(startChildbirth, endChildbirth));
        } else {
            newPregnancy = new Pregnancy(startChildbirth + "/" + endChildbirth, 0);
        }

        // непосредственно добавление в БД
        animal.setPregnancyID((int) MyFarmDatabase.getDatabase(getApplication()).pregnancyDao().insert(newPregnancy));
        MyFarmDatabase.getDatabase(getApplication()).animalDao().updateAnimal(animal);

        openFragment("pregnancyFragment");
    }

    // создания события беременности в календаре
    long createEvent(String startData, String endData){
        // создаём уведомление
        long calID = 1;
        long startMillis;
        long endMillis;
        Calendar beginTime = Calendar.getInstance();

        // дата начала и мб конца
        int year = Integer.parseInt(startData.substring(0, startData.indexOf("-")));
        int month = Integer.parseInt(startData.substring(startData.indexOf("-") + 1, startData.lastIndexOf("-")));
        int day = Integer.parseInt(startData.substring(startData.lastIndexOf("-") + 1));

        beginTime.set(year, month - 1, day, 0, 1);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        if (!endData.equals("")){
            // если роды ожидаются не в один день
            endTime.set(Integer.parseInt(endData.substring(0, endData.indexOf("-"))),
                    Integer.parseInt(endData.substring(endData.indexOf("-") + 1, endData.lastIndexOf("-"))),
                    Integer.parseInt(endData.substring(endData.lastIndexOf("-") + 1)), 23, 59);
        } else {
            endTime.set(year, month - 1, day, 23, 59);
        }
        endMillis = endTime.getTimeInMillis();
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);

        String animalInfo = MyFarmDatabase.getDatabase(this)
                .animalDao().getAnimalTypeName(animal.getAnimalTypeID());

        // выбор названия типа животного cамки
        if (animalInfo.contains("/")){
            animalInfo = animalInfo.substring(0, animalInfo.indexOf("/")).toUpperCase();
                if (!Objects.equals(animal.getAnimalName(), "")){
                    animalInfo = animalInfo.toUpperCase() + " " + animal.getAnimalName() + " №" + animal.getIdAnimal();
                } else {
                    animalInfo = animalInfo.toUpperCase() + " №" + animal.getIdAnimal();
                }
        } else {
            if (!Objects.equals(animal.getAnimalName(), "")){
                animalInfo = animalInfo.toUpperCase() + " " + animal.getAnimalName() + " №" + animal.getIdAnimal();
            } else {
                animalInfo = animalInfo.toUpperCase() + " №" + animal.getIdAnimal();
            }
        }
        // создаём событие
        values.put(CalendarContract.Events.TITLE, animalInfo + " скоро родит!");
        values.put(CalendarContract.Events.DESCRIPTION, "Будьте внимательны, скоро роды!");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Russia/Moscow");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());
        System.out.println(uri.getLastPathSegment());
        values.clear();
        // создаём оповещение
        values.put(CalendarContract.Reminders.MINUTES, 15);
        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
        return eventID;
    }

    // проверка разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_CALENDAR){
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AnimalActivity.this, "Для получения уведомлений о родах" +
                        "\nтребуется разрешение!", Toast.LENGTH_SHORT).show();
                isNotify = false;
                CheckBox box = findViewById(R.id.notify_checkbox);
                box.setEnabled(false);
                box.setChecked(false);
                box.setSelected(false);
            }
        }
    }


    void openFragment(String fragmentName){
        finishAffinity();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.putExtra(fragmentName, true);
        startActivity(intent);
    }
}