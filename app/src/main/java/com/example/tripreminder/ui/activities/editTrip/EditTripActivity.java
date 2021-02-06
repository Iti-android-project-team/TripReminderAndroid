package com.example.tripreminder.ui.activities.editTrip;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.activities.MainActivity;
import com.example.tripreminder.ui.activities.addTrip.AddTripActivity;
import com.example.tripreminder.ui.activities.addTrip.AddTripViewModel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTripActivity extends AppCompatActivity {
    TextView editTripTitle,editTripStartPoint,editTripEndPoint,editTripTime,editTripDate;
    ImageView back;
    Button edit, cancel;
    Spinner editSpinner;
    EditText editTripName;
    CheckBox rounded;

    private String tName;
    private String tripRepeat;
    private String[] repeats = {"No Repeat", "Repeat Daily", "Repeat Monthly", "Repeat Weekly"};
    private final String tripStatus = "Upcoming";
    private boolean tripIsRound = false;
    private int mYear, mMonth, mDay, currentHour, currentMin;
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
    private String amPm;
    private long time;
    Calendar currentCalendar = Calendar.getInstance();
    private boolean isStart;
    private String userEmail;

    String eN,eS,eE,eT,eD,eSp;
    int eId;
    Boolean eDir;

    private EditTripViewModel editViewModel;

    int editId;
    String  editName,editStart , editEnd , editTime, editDate , editSpinn ;
    Boolean direction ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        initialize();
        Intent intent=getIntent();
        editId=intent.getIntExtra("EDITID",0);
        editName =intent.getStringExtra("EDITName");
        editStart =intent.getStringExtra("EDITSTART");
        editStart =intent.getStringExtra("EDITSTART");
        editEnd =intent.getStringExtra("EDITEND");
        editTime =intent.getStringExtra("EDITTIME");
        editDate =intent.getStringExtra("EDITDATE");
        editSpinn =intent.getStringExtra("EDITSPINNER");
        direction =intent.getBooleanExtra("EDITROUND",false);

        editTripName.setText(editName);
        editTripStartPoint.setText(editStart);
        editTripEndPoint.setText(editEnd);
        editTripTime.setText(editTime);
        editTripDate.setText(editDate);
        rounded.setChecked(direction);

        edit.setOnClickListener(new View.OnClickListener() {
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


                if (editTripName.getText().toString().isEmpty()) {
                    //  mProgress.cancel();
                    editTripName.setBackgroundResource(R.drawable.background_input_empty);
                    editTripName.setError(getString(R.string.name_required));
                    return;
                }

                if (editTripStartPoint.getText().toString().isEmpty()) {
                    // mProgress.cancel();
                    editTripStartPoint.setBackgroundResource(R.drawable.background_input_empty);
                    editTripStartPoint.setError(getString(R.string.startPoint_required));
                    return;
                }

                if (editTripEndPoint.getText().toString().isEmpty()) {
                    // mProgress.cancel();
                    editTripEndPoint.setBackgroundResource(R.drawable.background_input_empty);
                    editTripEndPoint.setError(getString(R.string.endPoint_required));
                    return;
                }

                if (editTripDate.getText().toString().isEmpty()) {
                    //   mProgress.cancel();
                    editTripDate.setBackgroundResource(R.drawable.background_input_empty);
                    editTripDate.setError(getString(R.string.date_required));
                    return;
                }

                if (editTripTime.getText().toString().isEmpty()) {
                    // mProgress.cancel();
                    editTripTime.setBackgroundResource(R.drawable.background_input_empty);
                    editTripTime.setError(getString(R.string.time_required));
                    return;
                }

                if (time < currentCalendar.getTimeInMillis()) {
                    // mProgress.cancel();
                    editTripDate.setBackgroundResource(R.drawable.background_input_empty);
                    editTripTime.setBackgroundResource(R.drawable.background_input_empty);
                    editTripTime.setError(getString(R.string.time_expired));
                    Toast.makeText(EditTripActivity.this, "Time Expired )", Toast.LENGTH_LONG).show();
                    editTripDate.setError(getString(R.string.date_expired));
                    return;
                }

                Trips trips = new Trips();
                eN =editTripName.getText().toString();
                eD = editTripDate.getText().toString();
                trips.setStatus("upComing");
                eDir = false;
                eE = editTripEndPoint.getText().toString();
                trips.setRepeated(tripRepeat);
                eT = editTripTime.getText().toString();
                eS = editTripStartPoint.getText().toString();
                editTrip(eN,eS,eE,eD,eT,tripRepeat,eDir,editId);
                Toast.makeText(v.getContext(), "Trip Edit", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(EditTripActivity.this, MainActivity.class));

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTripActivity.this, MainActivity.class));

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTripActivity.this, MainActivity.class));
            }
        });
        editTripStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  placeOuto();
                //  isStart = true;
                editTripStartPoint.setText("EditStart");

            }
        });
        editTripEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // placeOuto();
                editTripEndPoint.setText("EditEnd");
            }
        });
        editTripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        editTripTime.setOnClickListener(new View.OnClickListener() {
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

        editSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripRepeat = repeats[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tripRepeat = repeats[0];
            }
        });
        ArrayAdapter sp = new ArrayAdapter(EditTripActivity.this, android.R.layout.simple_spinner_item, repeats);
        sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSpinner.setAdapter(sp);
        sp.notifyDataSetChanged();

    }
    private void showTimePickerDialog() {

        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMin = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(EditTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                editTripTime.setText(covertTimeTo12Hours(time12) + amPm);
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

    public String covertTimeTo12Hours(String time) {
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
                    editTripDate.setText(new SimpleDateFormat("dd / MM / yyyy").format(date));
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditTripActivity.this, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }
    private void editTrip(String s1, String s2,String s3,String s4,String s5,String s6, Boolean b,int i  ){
        //TripListViewModel listViewModel = ViewModelProviders.of(AddTripActivity.this).get(TripListViewModel.class);
        editViewModel.updateTrip(s1,s2,s3,s4,s5,s6,b,i);
//        mProgress.dismiss();
//         listViewModel.getId();
    }

    public void initialize(){
        editTripTitle = findViewById(R.id.txt_title);
        editTripStartPoint = findViewById(R.id.edit_tripStartPoint);
        editTripEndPoint = findViewById(R.id.edit_tripEndPoint);
        editTripTime = findViewById(R.id.edit_tripTime);
        editTripDate = findViewById(R.id.edit_tripDate);
        back = findViewById(R.id.img_back);
        edit = findViewById(R.id.btn_edit);
        cancel = findViewById(R.id.btn_cancel);
        editSpinner = findViewById(R.id.spin_choose);
        editTripName = findViewById(R.id.edit_tripName);
        rounded = findViewById(R.id.chBox_rounded);

        SharedPref.createPrefObject(EditTripActivity.this);
        userEmail = SharedPref.getUserEmail();
        if (!userEmail.equals(" ")) {
            editViewModel = new ViewModelProvider(this, new EditTripViewModelFactory(getApplication(),
                    userEmail)).get(EditTripViewModel.class);
        }
    }
}