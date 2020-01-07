package com.apptomate.schedularapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.adapter.BusinessHoursListAdapter;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.interfaces.SaveView;
import com.apptomate.schedularapp.model.BusinessHoursModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinesshoursFragment extends Fragment implements LoginView,BusinessHoursListAdapter.EventListenerRes,SaveView
{

    private Context context;
    private RecyclerView rv;
    private ProgressDialog progressDialog;
    private AppCompatTextView et_get,et_leave;
    private AppCompatButton btn_save;
    private Switch aSwitch;
    private TextView tv_b_id,tv_day;
    private String user_id;
    private String currentday;


    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    public BusinesshoursFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_businesshours, container, false);
         findIds(v);
         timeActions();
        tv_day=new TextView(context);
         getData();
        return v;

    }

    private void findIds(View v)
    {
        user_id = new SharedPrefs(context).getUserDetails().get(SharedPrefs.KEY_USERID);
        rv=v.findViewById(R.id.rv_bhours);
        et_get=v.findViewById(R.id.et_get_workby);
        et_leave=v.findViewById(R.id.et_leave_workby);
        btn_save=v.findViewById(R.id.btn_save_);
        aSwitch=v.findViewById(R.id.switch_mode);
        tv_b_id=new TextView(context);
        currentday=ApiConstants.getCurrentDay();
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void timeActions()
    {
        et_get.setOnClickListener(v -> new ApiConstants(context).showHourPicker(et_get));
        et_leave.setOnClickListener(v -> new ApiConstants(context).showHourPicker(et_leave));
        btn_save.setOnClickListener(v -> save());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void save()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("businesshourId",tv_b_id.getText().toString());
            jsonObject.put("userId",user_id);
            jsonObject.put("openTime",Objects.requireNonNull(et_get.getText()).toString());
            jsonObject.put("closeTime",Objects.requireNonNull(et_leave.getText()).toString());
            jsonObject.put("isActive",aSwitch.isChecked());
          } catch (JSONException e) {
            e.printStackTrace();
         }
        String from= getFromTime(et_get.getText().toString());
        String to= getToTime(et_leave.getText().toString());
        getClickingDay(tv_day.getText().toString(),from,to);
        progressDialog.show();
        new SaveImpl(this).handleSave(jsonObject,"BusinessHours/saveBusinessHours","PUT");
    }

    private void getData()
    {
       String user_id= new SharedPrefs(context).getUserDetails().get(SharedPrefs.KEY_USERID);
        progressDialog = ApiConstants.createProgressDialog(context);
        LoginImpl login=new LoginImpl(this);
        login.handleLogin(new JSONObject(),"BusinessHours/listBusinessHours?userid="+user_id,"GET");

    }

    @Override
    public void onSucess(String code, String response)
    {

        Log.e("Response",response);
         if (code.equalsIgnoreCase("200"))
         {
             assignData(response);
         }
    }
    private void assignData(String response)
    {
        progressDialog.dismiss();
        ArrayList<BusinessHoursModel> businessHoursModelArrayList=new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(response);
            for (int i = 0; i < ja.length(); i++)
            {
                JSONObject json= ja.getJSONObject(i);
                String name= json.getString("name");
                String day= json.getString("day");
                String businesshourId= json.getString("businesshourId");
                String userId= json.getString("userId");
                String openTime= json.getString("openTime");
                String closeTime= json.getString("closeTime");
                String isActive= json.getString("isActive");
                BusinessHoursModel bm=new BusinessHoursModel();
                bm.setName(name);
                bm.setDay(day);
                bm.setBusinesshourId(businesshourId);
                bm.setUserId(userId);
                bm.setOpenTime(openTime);
                bm.setCloseTime(closeTime);
                bm.setIsActive(isActive);
                businessHoursModelArrayList.add(bm);
                if (day.equalsIgnoreCase(currentday))
                {
                    tv_b_id.setText(businesshourId);
                    et_leave.setText(closeTime);
                    et_get.setText(openTime);
                    tv_day.setText(day);
                    aSwitch.setChecked(Boolean.parseBoolean(isActive));

                }
            }

            BusinessHoursListAdapter businessHoursListAdapter=new BusinessHoursListAdapter(this,businessHoursModelArrayList,context,currentday);
            rv.setAdapter(businessHoursListAdapter);
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(String error)
    {
        progressDialog.dismiss();
        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEvent(BusinessHoursModel res)
    {
        tv_b_id.setText(res.getBusinesshourId());
        et_leave.setText(res.getCloseTime());
        et_get.setText(res.getOpenTime());
        tv_day.setText(res.getDay());
        aSwitch.setChecked(Boolean.parseBoolean(res.getIsActive()));
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
       progressDialog.dismiss();
       if (code.equalsIgnoreCase("200"))
       {
           getData();
       }
       else {
           Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onSaveFailure(String error)
    {
        progressDialog.dismiss();
        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getClickingDay(String toString, String from, String to)
    {
        switch (toString)
        {
            case "mon":
                String date=  getWeekDates(0);
                syncToCalander(date,from,to);
                break;
            case "tue":
                syncToCalander(getWeekDates(1),from,to);
                break;
            case "wed":
                syncToCalander(getWeekDates(2),from,to);
                break;
            case "thu":
                syncToCalander(getWeekDates(3),from,to);
                break;
            case "fri":
                syncToCalander(getWeekDates(4),from,to);
                break;
            case "sat":
                syncToCalander(getWeekDates(5),from,to);
                break;
            case "sun":
                syncToCalander(getWeekDates(6),from,to);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void syncToCalander(String date,String from,String to)
    {

        int open_hour=Integer.parseInt(from.substring(0,2));
        int close_hour=Integer.parseInt(to.substring(0,2));
        int open_min=Integer.parseInt(from.substring(from.length()-2));
        int close_min=Integer.parseInt(to.substring(to.length()-2));
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
        int date_=cal.get(Calendar.DATE);
        Log.e("Month",""+date);
        Log.e("year",""+year);
        Log.e("date_",""+date_);
        long calID = 3; // Make sure to which calender you want to add event
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, date_, open_hour, open_min);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, date_, close_hour, close_min);
        endMillis = endTime.getTimeInMillis();
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "Business Hours");
        values.put(CalendarContract.Events.DESCRIPTION, "Business Hours");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        @SuppressLint("MissingPermission") Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
    }


    private String getWeekDates(int position)
    {
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


}
