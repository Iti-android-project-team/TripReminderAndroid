package com.example.tripreminder.ui.activities.addTrip;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tripreminder.HasNote;
import com.example.tripreminder.R;
import com.example.tripreminder.ui.activities.MainActivity;
import com.example.tripreminder.ui.fragment.upcoming.UpComingViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AddTripActivity extends AppCompatActivity {
    TextView tripTitle,tripStartPoint,tripEndPoint,tripTime,tripDate;
    ImageView back;
    Button create, cancel;
    Spinner spinner;
    EditText tripName;
    CheckBox rounded;

    private AlarmManager alarmManager;
    private String amPm;
    private boolean isStart;
    private String[] repeats = {"No Repeat", "Repeat Daily", "Repeat Weekly","Repeat Monthly"};
    Calendar currentCalendar = Calendar.getInstance();
    private String tName;
    private Dialog mProgress;
    private String startAddress, endAddress;
    private long time;
    private final String tripStatus = "Upcoming";
    private boolean tripIsRound = false;
    private String tripRepeat;
    private int mYear, mMonth, mDay, currentHour, currentMin;
    private double startLatitude, startLongitude;
    private double endLatitude, endLongitude;
    private static int AUTOCOMPLETE_REQUEST_CODE = 100;
    private AddTripViewModel viewModel;
    private Date date = null;
    private String time12;
    private int selectDateYear;
    private int selectDateMonth;
    private int selectDateDay;
    private int selectDateTimeHou;
    private int selectDateTimeMin;
    private long selectedTimeInMilliSecond;
    private  String itemSelected;
    int currentItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        initialize();
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddTripActivity.this, MainActivity.class));
            }
        });
        tripStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //  placeOuto();
              //  isStart = true;
                tripStartPoint.setText("start");

            }
        });
        tripEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // placeOuto();
                tripEndPoint.setText("end");
            }
        });
        tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showDatePickerDialog();
        }
        });
        tripTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        rounded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tripIsRound = isChecked;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripRepeat = repeats[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tripRepeat = repeats[0];
            }
        });
        ArrayAdapter sp = new ArrayAdapter(AddTripActivity.this, android.R.layout.simple_spinner_item, repeats);
        sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sp);

        create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
              //  mProgress.show();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, mYear);
                calendar.set(Calendar.MONTH, mMonth);
                calendar.set(Calendar.DAY_OF_MONTH, mDay);
                calendar.set(Calendar.HOUR_OF_DAY, currentHour);
                calendar.set(Calendar.MINUTE, currentMin);
                calendar.set(Calendar.SECOND, 0);
                time = calendar.getTimeInMillis();
                Log.i("currentTime", String.valueOf(time));


                if (tripName.getText().toString().isEmpty()) {
                  //  mProgress.cancel();
                    tripName.setBackgroundResource(R.drawable.background_input_empty);
                    tripName.setError(getString(R.string.name_required));
                    return;
                }

                if (tripStartPoint.getText().toString().isEmpty()) {
                   // mProgress.cancel();
                    tripStartPoint.setBackgroundResource(R.drawable.background_input_empty);
                    tripStartPoint.setError(getString(R.string.startPoint_required));
                    return;
                }

                if (tripEndPoint.getText().toString().isEmpty()) {
                   // mProgress.cancel();
                    tripEndPoint.setBackgroundResource(R.drawable.background_input_empty);
                    tripEndPoint.setError(getString(R.string.endPoint_required));
                    return;
                }

                if (tripDate.getText().toString().isEmpty()) {
                 //   mProgress.cancel();
                    tripDate.setBackgroundResource(R.drawable.background_input_empty);
                    tripDate.setError(getString(R.string.date_required));
                    return;
                }

                if (tripTime.getText().toString().isEmpty()) {
                   // mProgress.cancel();
                    tripTime.setBackgroundResource(R.drawable.background_input_empty);
                    tripTime.setError(getString(R.string.time_required));
                    return;
                }

                if (time < currentCalendar.getTimeInMillis()) {
                   // mProgress.cancel();
                    tripDate.setBackgroundResource(R.drawable.background_input_empty);
                    tripTime.setBackgroundResource(R.drawable.background_input_empty);
                    tripTime.setError(getString(R.string.time_expired));
                     Toast.makeText(AddTripActivity.this, "Time Expired OR Date Expired)", Toast.LENGTH_LONG).show();
                    tripDate.setError(getString(R.string.date_expired));
                    return;
                }
                tName = tripName.getText().toString();
               // Location startLocation = new Location(startAddress, startLatitude, startLongitude);
                //Location endLocation = new Location(endAddress, endLatitude, endLongitude);
                List<HasNote> list = new ArrayList<>();
                final int idAlarm = (int) System.currentTimeMillis();
                //turnOnAlarmManager(time, idAlarm);
              //  Trip trip = new Trip(idAlarm, tripN, startLocation, endLocation, time, tripStatus, tripIsRound, tripRepeat, list);
                //insertTrip(trip);
                //Navigation.findNavController(view).popBackStack();
                Toast.makeText(v.getContext(), "Trip Saved", Toast.LENGTH_SHORT).show();
                CalTimeInMilliSecond();

            }
        });

     //"AIzaSyDnbtBwgXmFh-e3jDYu3ffqDpOEOb8vU3Y"   run
    }
    public void placeOuto(){
        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(),"AIzaSyAGI7YfmrD1yC5-khaLeqLr3-r6FMVQzjw", Locale.US);
        }
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(AddTripActivity.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
         if (resultCode == Activity.RESULT_OK && requestCode == AUTOCOMPLETE_REQUEST_CODE){
           Place  place = Autocomplete.getPlaceFromIntent(data);
             Log.i("TAG","place:" + place.getName()+","+place.getId());
         }else if (requestCode== AutocompleteActivity.RESULT_ERROR){
             Status status = Autocomplete.getStatusFromIntent(data);
             Toast.makeText(getApplicationContext(),status.getStatusMessage(), Toast.LENGTH_SHORT).show();
         }
        String address = Autocomplete.getPlaceFromIntent(data).getName();
        //LatLng latLng = new LatLng(((Point) Objects.requireNonNull(Autocomplete.getPlaceFromIntent(data).getAddress()).latitude(),
         //       ((Point) Objects.requireNonNull(Autocomplete.getPlaceFromIntent(data).getAddress()).longitude());


        if (isStart) {
            startAddress = address;
          //  startLatitude = latLng.getLatitude();
            //startLongitude = latLng.getLongitude();
            tripStartPoint.setText(startAddress);
            isStart = false;
        } else {
            endAddress = address;
          //  endLatitude = latLng.getLatitude();
            //endLongitude = latLng.getLongitude();
            tripEndPoint.setText(endAddress);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
   private void showTimePickerDialog() {

        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMin = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectDateTimeHou = hourOfDay;
                selectDateTimeMin = minute;
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                 time12 = String.format("%02d:%02d ", hourOfDay, minute);
                tripTime.setText(covertTimeTo12Hours(time12) + amPm);
                Log.e("currentTime",time12);
//                String d =getDateSelected();
//                long t =calTimeInMillisecond(d);
//                Log.i("currentTime13", String.valueOf(t));
//                tripTime.setText(covertTimeTo12Hours(time12) + amPm);



                currentHour = hourOfDay;
                currentMin = minute;

            }
        }, currentHour, currentMin, false);
            timePickerDialog.show();
    }

    private String covertTimeTo12Hours(String time) {
        String[] splitTime = time.split(":");
        String time12 = splitTime[1];

        switch (splitTime[0]) {
            case "12":
                return "12:" + time12;
            case "13":
                return "01:" + time12;

            case "14":
                return "02:" + time12;

            case "15":
                return "03:" + time12;

            case "16":
                return "04:" + time12;

            case "17":
                return "05:" + time12;
            case "18":
                return "06:" + time12;
            case "19":
                return "07:" + time12;
            case "20":
                return "08:" + time12;
            case "21":
                return "09:" + time12;
            case "22":
                return "10:" + time12;
            case "23":
                return "11:" + time12;
        }
        return time;
    }

    private void showDatePickerDialog()
    {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                int months = month + 1;
                selectDateMonth = month;
                selectDateYear = year;
                selectDateDay = dayOfMonth;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dayOfMonth + "/" + months + "/" + year);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date != null) {
                    tripDate.setText(new SimpleDateFormat("dd / MM / yyyy").format(date));
                }
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        };
        Calendar now = Calendar.getInstance();
        int year = now.get(java.util.Calendar.YEAR);
        int month = now.get(java.util.Calendar.MONTH);
        int day = now.get(java.util.Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, onDateSetListener, year, month, day);
        datePickerDialog.setTitle("Please select date.");
        datePickerDialog.show();
    }
   /* private void insertTrip(Trip trip) {
        // call observe
        TripListViewModel listViewModel = ViewModelProviders.of(this).get(TripListViewModel.class);
//         listViewModel.insert(trip);
        listViewModel.insert(trip);
        mProgress.dismiss();
//         listViewModel.getId();
    }*/

    public void initialize(){
        tripTitle = findViewById(R.id.txt_title);
        tripStartPoint = findViewById(R.id.edit_tripStartPoint);
        tripEndPoint = findViewById(R.id.edit_tripEndPoint);
        tripTime = findViewById(R.id.edit_tripTime);
        tripDate = findViewById(R.id.edit_tripDate);
        back = findViewById(R.id.img_back);
        create = findViewById(R.id.btn_save);
        cancel = findViewById(R.id.btn_close);
        spinner = findViewById(R.id.spin_choose);
        tripName = findViewById(R.id.edit_tripName);
        rounded = findViewById(R.id.chBox_rounded);
        viewModel =  new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(
                getApplication())).get(AddTripViewModel.class);
        spinnerSelected();
    }

    private void CalTimeInMilliSecond(){
        Calendar calendar1 =  Calendar.getInstance();
        calendar1.set(selectDateYear,selectDateMonth,selectDateDay,selectDateTimeHou,selectDateTimeMin,00);
        Log.e("current", String.valueOf(calendar1.getTimeInMillis()));
        selectedTimeInMilliSecond = calendar1.getTimeInMillis();
        createWorkManager(selectedTimeInMilliSecond);
    }
    private  void createWorkManager(long timeInMilliSecond ){
        Calendar calendar =  Calendar.getInstance();
        int durationTime = (int) ((int)timeInMilliSecond - calendar.getTimeInMillis());
        if(itemSelected.equals("No Repeat")){
            viewModel.addTripWorkOneTime(durationTime, TimeUnit.MILLISECONDS);
        }else{
            if(itemSelected.equals("Repeat Daily")){
                viewModel.addTripWorkOneTime(1, TimeUnit.DAYS);
            }else if(itemSelected.equals("Repeat Weekly")){
                viewModel.addTripWorkOneTime(7, TimeUnit.DAYS);
            }else if(itemSelected.equals("Repeat Monthly")){
                viewModel.addTripWorkOneTime(30, TimeUnit.DAYS);
            }

        }


    }

    private void spinnerSelected(){
        Log.e("itemSelected","spinnerSelected");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(currentItem == position){
                    Log.e("itemSelected",itemSelected);
                    return;
                }
                else
                {
                    itemSelected = repeats[position];
                    Log.e("itemSelected",itemSelected);
                }
                currentItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                itemSelected = repeats[0];
                Log.e("itemSelected",itemSelected);
            }
        });
    }

}
