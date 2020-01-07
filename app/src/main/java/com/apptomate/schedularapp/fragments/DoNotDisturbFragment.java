package com.apptomate.schedularapp.fragments;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.ScheduleAddActivity;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.interfaces.SaveView;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import static com.apptomate.schedularapp.util.SharedPrefs.KEY_SC_DAYS;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoNotDisturbFragment extends Fragment implements LoginView, SaveView
{

    private Context context;
    private ProgressDialog progressDialog;
    private Switch aSwitch;
    private boolean firstVisit;
    private TextView tv_schedule;
    String user_id;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        this.context=context;
    }

    public DoNotDisturbFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_do_not_disturb, container, false);
        firstVisit = true;
        aSwitch=v.findViewById(R.id.switch_donb);
        tv_schedule=v.findViewById(R.id.tv_schedule);
        user_id=new SharedPrefs(context).getUserDetails().get(SharedPrefs.KEY_USERID);
        LinearLayout linearLayout_scadd=v.findViewById(R.id.ll_scadd);
        linearLayout_scadd.setOnClickListener(v1 ->
        {
            Intent i=new Intent(context,ScheduleAddActivity.class);
            startActivity(i);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.right_in, R.anim.left_out);

        });

        getStatus();
        getScheduledList();
       // getScheduledList();
        return  v;
    }

//    private void showAddScheduleDialog()
//    {
//        AlertDialog alertDialog;
//       LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//       View vv=layoutInflater.inflate(R.layout.schedule_style,null,false);
//        AlertDialog.Builder alb=new AlertDialog.Builder(context);
//        alb.setView(vv);
//        AppCompatEditText et_don_name=  vv.findViewById(R.id.et_don_name);
//        Switch use_schedule_switch=vv.findViewById(R.id.use_schedule_switch);
//        RecyclerView rv_sheours=vv.findViewById(R.id.rv_sheours);
//        LinearLayout ll_start=vv.findViewById(R.id.ll_start);
//        LinearLayout ll_end=vv.findViewById(R.id.ll_end);
//        AppCompatTextView tv_scstart=vv.findViewById(R.id.tv_scstart);
//        AppCompatTextView tv_scend=vv.findViewById(R.id.tv_scend);
//        Switch switch_sc_allarm=vv.findViewById(R.id.switch_sc_allarm);
//        AppCompatButton btn_save_schedule=vv.findViewById(R.id.btn_save_schedule);
//        alertDialog=alb.create();
//        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        alertDialog.show();
//
//    }

    private void getStatus()
    {

        String user_id= new SharedPrefs(context).getUserDetails().get(SharedPrefs.KEY_USERID);
        progressDialog= ApiConstants.createProgressDialog(context);
        LoginImpl impl=new LoginImpl(this);
        impl.handleLogin(new JSONObject(),"Holidays/listHolidayByUserId?userId="+user_id,"GET");
    }

    @Override
    public void onSucess(String code, String response)
    {

        progressDialog.dismiss();
        if (code.equalsIgnoreCase("200"))
        {
            try {
                JSONArray ja=new JSONArray(response);
                for (int i=0;i<ja.length();i++)
                {
                    JSONObject json= ja.getJSONObject(i);
                    String holidayName = json.getString("holidayName");
                    String fromDate = json.getString("fromDate");
                    String toDate = json.getString("toDate");
                    boolean isActive = json.getBoolean("isActive");
                    List<Date> dates = getDates(fromDate, toDate);
                    for(Date date:dates)
                    {
                        if (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date).equalsIgnoreCase(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(ApiConstants.getCurrentDate()))&&isActive)
                        {
                            Toast.makeText(context, ""+holidayName, Toast.LENGTH_SHORT).show();
                            aSwitch.setChecked(true);
                            break;
                        }

                    }


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void save(int userId, String alertcolor, String alerttone, String alertDuration)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("alertcolor",alertcolor);
            jsonObject.put("alerttone",alerttone);
            jsonObject.put("alertDuration",alertDuration);
            jsonObject.put("doNotDisturbStatus",""+aSwitch.isChecked());

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // saveStatus(jsonObject,"UserSettings/saveUserSettings");
        progressDialog.show();
        new SaveImpl(this).handleSave(jsonObject,"UserSettings/saveUserSettings","PUT");
    }

    @Override
    public void onFailure(String error)
    {
        progressDialog.dismiss();
        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        //other stuff
        super.onResume();
        if (firstVisit) {
            //do stuff for first visit only
            firstVisit = false;

        }
        else {
           // Toast.makeText(context, "Restart", Toast.LENGTH_SHORT).show();
            //getScheduledList();
           String days= new SharedPrefs(context).getSchedularDays().get(KEY_SC_DAYS);
           tv_schedule.setText(""+days+"\tScheduled");
           getStatus();

        }
    }


     private void getScheduledList()
    {
       // progressDialog=ApiConstants.createProgressDialog(context);
        ArrayList<Boolean> al_active=new ArrayList<>();
        RequestQueue requestQueue=Volley.newRequestQueue(context);
        @SuppressLint("SetTextI18n") StringRequest stringRequest=new StringRequest(Request.Method.GET, ApiConstants.Base_Url + "DoNotDisturb/listDoNotDisturbByUserId?userId="+user_id, response -> {

             Log.e("Scheduled_Res",response);
          //  progressDialog.dismiss();


            try {
                JSONArray jsonArray=new JSONArray(response);
                for (int i=0;i<jsonArray.length();i++)
                {
                   JSONObject json= jsonArray.getJSONObject(i);
                  boolean isActive= json.getBoolean("isActive");
                    String openTime= json.getString("openTime");
                    String closeTime= json.getString("closeTime");
                    String day= json.getString("day");
                    if (isActive)
                    {
                        al_active.add(isActive);
                    }
                    if (day.equalsIgnoreCase(ApiConstants.getCurrentDay())&&isActive)
                    {
                        compareTime(openTime,closeTime,ApiConstants.getCurrentTime());
                    }

                }
                tv_schedule.setText(""+al_active.size()+"\tScheduled");
               // progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->
                Log.e("Scheduled_Res",""+error));
        //progressDialog.dismiss();
        requestQueue.add(stringRequest);
    }


    @Override
    public void onSaveSucess(String code, String response)
    {
        progressDialog.dismiss();
        if (code.equalsIgnoreCase("200"))
        {
            getStatus();
        }
        else
        {
            Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveFailure(String error)
    {
        progressDialog.dismiss();
        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
    }

    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }


    void compareTime(String string1,String string2,String current)
    {
        try {

            Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);



            Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);


            Date d = new SimpleDateFormat("HH:mm").parse(current);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                System.out.println(true);
                aSwitch.setChecked(true);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
