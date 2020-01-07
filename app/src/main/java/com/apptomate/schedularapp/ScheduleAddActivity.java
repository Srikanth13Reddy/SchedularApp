package com.apptomate.schedularapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apptomate.schedularapp.adapter.SchedulaerAdapter;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.interfaces.SaveView;
import com.apptomate.schedularapp.model.ScheduleModel;
import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.LoginImpl;
import com.apptomate.schedularapp.util.SaveImpl;
import com.apptomate.schedularapp.util.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import static com.apptomate.schedularapp.util.ApiConstants.getFromTime;
import static com.apptomate.schedularapp.util.ApiConstants.getToTime;

public class ScheduleAddActivity extends AppCompatActivity implements LoginView, SchedulaerAdapter.EventListener, SaveView {

    RecyclerView rv;
    TextView tv_id, tv_day;
    ProgressDialog progressDialog;
    EditText et_don_name;
    String currentdate;
    Switch use_schedule_switch, switch_sc_allarm;
    AppCompatTextView tv_scstart, tv_scend;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = findViewById(R.id.rv_sheours);
        user_id = new SharedPrefs(this).getUserDetails().get(SharedPrefs.KEY_USERID);
        switch_sc_allarm = findViewById(R.id.switch_sc_allarm);
        et_don_name = findViewById(R.id.et_don_name);
        use_schedule_switch = findViewById(R.id.use_schedule_switch);
        tv_scstart = findViewById(R.id.tv_scstart);
        tv_scend = findViewById(R.id.tv_scend);
        tv_id = new TextView(this);
        tv_day = new TextView(this);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        currentdate = dayOfTheWeek.substring(0, 3).toLowerCase();
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getScheduledData();

    }

    private void getScheduledData() {
        progressDialog = ApiConstants.createProgressDialog(this);
        LoginImpl login = new LoginImpl(this);
        login.handleLogin(new JSONObject(), "DoNotDisturb/listDoNotDisturbByUserId?userId=" + user_id, "GET");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    @Override
    public void onSucess(String code, String response) {
        progressDialog.dismiss();
        if (code.equalsIgnoreCase("200")) {
            assignData(response);
        }
    }

    private void assignData(String response) {
        ArrayList<String> al_active = new ArrayList<>();
        ArrayList<ScheduleModel> arrayList = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject js = ja.getJSONObject(i);
                String name = js.getString("name");
                String phonenumber = js.getString("phonenumber");
                String doNotDisturbId = js.getString("doNotDisturbId");
                String reason = js.getString("reason");
                String userId = js.getString("userId");
                String day = js.getString("day");
                String openTime = js.getString("openTime");
                String closeTime = js.getString("closeTime");
                String isAllowAlarmRings = js.getString("isAllowAlarmRings");
                String isActive = js.getString("isActive");
                ScheduleModel scheduleModel = new ScheduleModel();
                scheduleModel.setName(name);
                scheduleModel.setPhonenumber(phonenumber);
                scheduleModel.setDoNotDisturbId(doNotDisturbId);
                scheduleModel.setReason(reason);
                scheduleModel.setUserId(userId);
                scheduleModel.setDay(day);
                scheduleModel.setOpenTime(openTime);
                scheduleModel.setCloseTime(closeTime);
                scheduleModel.setIsAllowAlarmRings(isAllowAlarmRings);
                scheduleModel.setIsActive(isActive);
                arrayList.add(scheduleModel);
                if (isActive.equalsIgnoreCase("true")) {
                    al_active.add(isActive);
                }

                if (day.equalsIgnoreCase(currentdate)) {
                    tv_id.setText(doNotDisturbId);
                    tv_day.setText(day);
                    tv_scend.setText(closeTime);
                    tv_scstart.setText(openTime);
                    et_don_name.setText(reason);
                    switch_sc_allarm.setChecked(Boolean.parseBoolean(isAllowAlarmRings));
                    use_schedule_switch.setChecked(Boolean.parseBoolean(isActive));
                }


            }
            new SharedPrefs(this).setScheduleDays("" + al_active.size());

            SchedulaerAdapter schedulaerAdapter = new SchedulaerAdapter(arrayList, this, this, currentdate);
            rv.setAdapter(schedulaerAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String error) {
        progressDialog.dismiss();
    }

    @Override
    public void onEvent(ScheduleModel res) {

        tv_id.setText(res.getDoNotDisturbId());
        tv_day.setText(res.getDay());
        // Toast.makeText(this, "" + res.getDay(), Toast.LENGTH_SHORT).show();
        et_don_name.setText(res.getReason());
        use_schedule_switch.setChecked(Boolean.parseBoolean(res.getIsActive()));
        switch_sc_allarm.setChecked(Boolean.parseBoolean(res.getIsAllowAlarmRings()));
        tv_scstart.setText(res.getOpenTime());
        tv_scend.setText(res.getCloseTime());
    }


    public void save(View view) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("doNotDisturbId", tv_id.getText().toString());
            jsonObject.put("reason", et_don_name.getText().toString());
            jsonObject.put("openTime", tv_scstart.getText().toString());
            jsonObject.put("closeTime", tv_scend.getText().toString());
            jsonObject.put("isAllowAlarmRings", String.valueOf(switch_sc_allarm.isChecked()));
            jsonObject.put("isActive", String.valueOf(use_schedule_switch.isChecked()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        String from = getFromTime(tv_scstart.getText().toString());
        String to = getToTime(tv_scend.getText().toString());
        getClickingDay(tv_day.getText().toString(), from, to);
        new SaveImpl(this).handleSave(jsonObject, "DoNotDisturb/saveDoNotDisturb", "PUT");
    }


    private void getClickingDay(String toString, String from, String to) {
        switch (toString) {
            case "mon":
                String date = getWeekDates(0);
                syncToCalander(date, from, to);
                break;
            case "tue":
                syncToCalander(getWeekDates(1), from, to);
                break;
            case "wed":
                syncToCalander(getWeekDates(2), from, to);
                break;
            case "thu":
                syncToCalander(getWeekDates(3), from, to);
                break;
            case "fri":
                syncToCalander(getWeekDates(4), from, to);
                break;
            case "sat":
                syncToCalander(getWeekDates(5), from, to);
                break;
            case "sun":
                syncToCalander(getWeekDates(6), from, to);
                break;
        }
    }

//    private void showTimePicker(AppCompatTextView et) {
//        final Calendar c = Calendar.getInstance();
//        int mHour = c.get(Calendar.HOUR_OF_DAY);
//        int mMinute = c.get(Calendar.MINUTE);
//        int se = c.get(Calendar.SECOND);
//
//        // Launch Time Picker Dialog
//        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(this,
//                (view, hourOfDay, minute) -> {
//
//                    et.setText("" + String.format("%02d\t:\t%02d", hourOfDay, minute));
//
////                        if (hourOfDay > 12) {
////                            et.setText("0"+(hourOfDay - 12) + "\t\t:\t\t" + (minute + "\t\tPm"));
////                        } else if (hourOfDay == 12) {
////                            et.setText("12" + "\t:\t" + (minute + "\t\tPm"));
////                        } else {
////                            if (hourOfDay != 0) {
////                                et.setText("0"+hourOfDay + "\t\t:\t\t" + (minute + "\t\tAm"));
////                            } else {
////                                et.setText("12" + "\t\t:\t\t " + (minute + "\t\tAm"));
////                            }
//
//
//                    // }
//                }, mHour, mMinute, false);
//        timePickerDialog.show();
//    }

    public void start_time(View view) {
        new ApiConstants(this).showHourPicker(tv_scstart);
    }

    public void close_time(View view) {
        new ApiConstants(this).showHourPicker(tv_scend);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSaveSucess(String code, String response) {
        progressDialog.dismiss();
        if (code.equalsIgnoreCase("200")) {
            getScheduledData();

        } else {
            Toast.makeText(this, "" + response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveFailure(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

    }

    private String getWeekDates(int position) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.e("Days", "" + days.length);
        return days[position];

    }


    void syncToCalander(String date, String from, String to) {

        int open_hour = Integer.parseInt(from.substring(0, 2));
        int close_hour = Integer.parseInt(to.substring(0, 2));
        int open_min = Integer.parseInt(from.substring(from.length() - 2));
        int close_min = Integer.parseInt(to.substring(to.length() - 2));
        Date d = null;
        try {
            d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int date_ = cal.get(Calendar.DATE);
        Log.e("Month", "" + date);
        Log.e("year", "" + year);
        Log.e("date_", "" + date_);
        long calID = 3; // Make sure to which calender you want to add event
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, date_, open_hour, open_min);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, date_, close_hour, close_min);
        endMillis = endTime.getTimeInMillis();
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, et_don_name.getText().toString());
        values.put(CalendarContract.Events.DESCRIPTION, "Schedule");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
        } else {
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
        }
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
    }







}
