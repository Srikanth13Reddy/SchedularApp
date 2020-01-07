package com.apptomate.schedularapp.fragments;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.adapter.HolidayListAdapter;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.interfaces.SaveView;
import com.apptomate.schedularapp.model.HolidayListModel;
import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.LoginImpl;
import com.apptomate.schedularapp.util.SaveImpl;
import com.apptomate.schedularapp.util.SharedPrefs;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HolidayScheduleFragment extends Fragment implements LoginView,HolidayListAdapter.EventListener, SaveView
{

    private Context context;
    private DateRangeCalendarView calendar;
    private TextView tv_from,tv2_to;
    private ProgressDialog progressDialog;
    private RecyclerView rv;
    private String user_id;
    public HolidayScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_holiday_schedule, container, false);
         user_id= new SharedPrefs(context).getUserDetails().get(SharedPrefs.KEY_USERID);
        FloatingActionButton floatingActionButton = v.findViewById(R.id.add_holiday);
        rv=v.findViewById(R.id.rv_holidays);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), llm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        rv.setLayoutManager(llm);
        getHolidayData();
        floatingActionButton.setOnClickListener((View v1) ->
                showAddHolidayDialog());
        return v;
    }

    @SuppressLint("InflateParams")
    private void showAddHolidayDialog()
    {
        final AlertDialog alertDialog;
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View vv= null;
        if (layoutInflater != null) {
            vv = layoutInflater.inflate(R.layout.add_holiday_view,null,false);
        }
        if (vv!=null)
        {
            EditText et_holiday=vv.findViewById(R.id.et_holiday);
            EditText et_date=vv.findViewById(R.id.et_date);
            AppCompatTextView tv_start=vv.findViewById(R.id.tv_start);
            AppCompatTextView tv_end=vv.findViewById(R.id.tv_end);
            RelativeLayout rv_start=vv.findViewById(R.id.rl_start);
            RelativeLayout rv_end=vv.findViewById(R.id.rl_end);
            TextView btn_accept=vv.findViewById(R.id.btn_save);
            Switch holiday_switch=vv.findViewById(R.id.holiday_switch);
            AlertDialog.Builder alb=new AlertDialog.Builder(context);
            alb.setView(vv);
            alertDialog=alb.create();
            tv_end.setText(ApiConstants.getCurrentDate_());
            tv_start.setText(ApiConstants.getCurrentDate_());
            Animation transition_in_view = AnimationUtils.loadAnimation(context, R.anim.customer_anim);//customer animation appearance
            vv.setAnimation( transition_in_view );
            vv.startAnimation( transition_in_view );
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            alertDialog.show();

            rv_end.setOnClickListener(v -> new ApiConstants(context).showdateDialog(tv_end));

            rv_start.setOnClickListener(v -> new ApiConstants(context).showdateDialog(tv_start));

            btn_accept.setOnClickListener(v22 -> {
                if (et_holiday.getText().toString().isEmpty())
                {
                    Toast.makeText(context, "Enter holiday name", Toast.LENGTH_SHORT).show();
                }


                    else {
                        alertDialog.cancel();
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("holidayId","0");
                            jsonObject.put("userId",user_id);
                            jsonObject.put("holidayName",et_holiday.getText().toString());
                            jsonObject.put("fromDate",tv_start.getText().toString());
                            jsonObject.put("toDate",tv_end.getText().toString());
                            jsonObject.put("notes","");
                            jsonObject.put("isActive",holiday_switch.isChecked());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        saveData(jsonObject);
                     }


            });
            et_date.setOnClickListener(v2 ->
            {

                AlertDialog alertDialog1;
                LayoutInflater layoutInflater1= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View vvv= null;
                if (layoutInflater1 != null) {
                    vvv = layoutInflater1.inflate(R.layout.date_range_picker,null,false);
                }
                AlertDialog.Builder alb1 =new AlertDialog.Builder(context);
                alertDialog1= alb1.setView(vvv).create();
                Objects.requireNonNull(alertDialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                // Animation transition_in_view_ = AnimationUtils.loadAnimation(context, R.anim.customer_anim);//customer animation appearance
                assert vvv != null;
                vvv.setAnimation( transition_in_view );
                vvv.startAnimation( transition_in_view );
                calendar = vvv.findViewById(R.id.calendar);
                rangeCalender(calendar,alertDialog1,et_date);
                alertDialog1.show();


            });
        }

    }

    void showdateDialog(AppCompatTextView tv)
    {
        final Calendar c = Calendar.getInstance();
       int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month= monthOfYear+1;
                        String fm=""+month;
                        String fd=""+dayOfMonth;
                        if(month<10){
                            fm ="0"+month;
                        }
                        if (dayOfMonth<10){
                            fd="0"+dayOfMonth;
                        }
                        String date= ""+fm+"-"+fd+"-"+year;
                        tv.setText(date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



    private void saveData(JSONObject jsonObject)
    {
         progressDialog.show();
        //saveHoliday(jsonObject,"Holidays/createHoliday");
        new SaveImpl(this).handleSave(jsonObject,"Holidays/createHoliday","POST");
    }

    private void getHolidayData()
    {
        progressDialog= ApiConstants.createProgressDialog(context);
        LoginImpl login=new LoginImpl(this);
        login.handleLogin(new JSONObject(),"Holidays/listHolidayByUserId?userId="+user_id,"GET");
    }

    private void rangeCalender(DateRangeCalendarView calendar,AlertDialog dialog,EditText editText)
    {


        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(@NonNull Calendar startDate) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                Toast.makeText(context, "Start Date: " + dateFormat.format(startDate.getTime()), Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateRangeSelected(@NonNull Calendar startDate, @NonNull Calendar endDate) {
                dialog.cancel();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                String str_date= dateFormat.format(startDate.getTime());
                String end_Date= dateFormat.format(endDate.getTime());
                editText.setText(str_date + "\t\tto\t\t"+end_Date);
                tv_from =new TextView(context);
                tv2_to =new TextView(context);
                tv_from.setText(""+str_date);
                tv2_to.setText(""+end_Date);


               // Toast.makeText(context, "Start Date: " + dateFormat.format(startDate.getTime()), Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onSucess(String code, String response)
    {
        progressDialog.dismiss();
        Log.e("Res",response);
        if (code.equalsIgnoreCase("200"))
        {
            assignData(response);
        }
    }

    private void assignData(String response)
    {
        ArrayList<HolidayListModel> holidayListModelArrayList=new ArrayList<>();
        try {
            JSONArray ja=new JSONArray(response);
            for (int i=0;i<ja.length();i++)
            {
               JSONObject json= ja.getJSONObject(i);
                String holidayId = json.getString("holidayId");
                String userId = json.getString("userId");
                String name = json.getString("name");
                String phoneNumber = json.getString("phoneNumber");
                String holidayName = json.getString("holidayName");
                String fromDate = json.getString("fromDate");
                String toDate = json.getString("toDate");
                String notes = json.getString("notes");
                String isActive = json.getString("isActive");
                HolidayListModel holidayListModel=new HolidayListModel();
                holidayListModel.setHolidayId(holidayId);
                holidayListModel.setUserId(userId);
                holidayListModel.setName(name);
                holidayListModel.setHolidayName(holidayName);
                holidayListModel.setPhoneNumber(phoneNumber);
                holidayListModel.setFromDate(fromDate);
                holidayListModel.setToDate(toDate);
                holidayListModel.setName(notes);
                holidayListModel.setIsActive(isActive);
                holidayListModelArrayList.add(holidayListModel);
//                List<Date> dates = getDates(fromDate, toDate);
//                for(Date date:dates)
//                {//System.out.println(date);
//                    if (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date).equalsIgnoreCase(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(ApiConstants.getCurrentDate())))
//                    {
//                        Toast.makeText(context, ""+holidayName, Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                }



//                if (ApiConstants.getCurrentDate().after(ApiConstants.convertTodate(fromDate))&&ApiConstants.getCurrentDate().before(ApiConstants.convertTodate(toDate)))
//                {
//                    Toast.makeText(context, ""+holidayName, Toast.LENGTH_LONG).show();
//                }


            }

            HolidayListAdapter holidayListAdapter=new HolidayListAdapter(holidayListModelArrayList,context,this);
            rv.setAdapter(holidayListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String error)
    {
        progressDialog.dismiss();
        Toast.makeText(context, ""+error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEvent(String res, String code)
    {
        if (code.equalsIgnoreCase("200"))
        {
            getHolidayData();
        }
        else if (code.equalsIgnoreCase(""))
        {
            Toast.makeText(context, "Check your internet", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        progressDialog.dismiss();
        if (code.equalsIgnoreCase("200"))
        {
            getHolidayData();
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
