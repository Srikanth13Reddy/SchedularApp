package com.apptomate.schedularapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

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
    private TextView tv_b_id;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_businesshours, container, false);
         findIds(v);
         timeActions();
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

    private void timeActions()
    {
        et_get.setOnClickListener(v -> new ApiConstants(context).showHourPicker(et_get));
        et_leave.setOnClickListener(v -> new ApiConstants(context).showHourPicker(et_leave));
        btn_save.setOnClickListener(v -> save());
    }

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
        aSwitch.setChecked(Boolean.parseBoolean(res.getIsActive()));
    }

//    private void showTimePicker(AppCompatEditText et)
//    {
////        final Calendar c = Calendar.getInstance();
////        int mHour = c.get(Calendar.HOUR_OF_DAY);
////        int mMinute = c.get(Calendar.MINUTE);
////
////
////        // Launch Time Picker Dialog
////        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(context,
////                (view, hourOfDay, minute) ->
////                        et.setText(""+String.format("%02d\t:\t%02d", hourOfDay, minute)), mHour, mMinute, false);
////        timePickerDialog.show();
//
//
//        calendar = Calendar.getInstance();
//        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
//        CalendarMinute = calendar.get(Calendar.MINUTE);
//        int hour = CalendarHour % 12;
//
//        timepickerdialog = new TimePickerDialog(context,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    @SuppressLint("DefaultLocale")
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay,
//                                          int minute) {
//
//                        if (hourOfDay == 0) {
//
//                            hourOfDay += 12;
//
//                            format = "AM";
//                        }
//                        else if (hourOfDay == 12) {
//
//                            format = "PM";
//
//                        }
//                        else if (hourOfDay > 12) {
//
//                            hourOfDay -= 12;
//
//                            format = "PM";
//
//                        }
//                        else {
//
//                            format = "AM";
//                        }
//
//
//                        //et.setText(hourOfDay + "\t:\t" + minute + "\t\t"+format);
//                       // et.setText(String.format("%02d:%02d", hourOfDay, minute+format));
//                        int hour = hourOfDay % 12;
//                        et.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
//                                minute, hourOfDay < 12 ? "am" : "pm"));
//                    }
//                }, CalendarHour, CalendarMinute, false);
//        timepickerdialog.show();
//
//
//
//    }





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
}
