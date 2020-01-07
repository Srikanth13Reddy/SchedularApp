package com.apptomate.schedularapp.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.model.HolidayListModel;
import com.apptomate.schedularapp.util.ApiConstants;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.os.Looper.getMainLooper;

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.MyHoledr>
{
    private TextView tv_from,tv2_to;
   private ArrayList<HolidayListModel> al;
    private DateRangeCalendarView calendar;
   private Context context;
   private ProgressDialog progressDialog;
   private EventListener listener;
    public interface EventListener
    {
        void onEvent(String res,String code);
    }

    public HolidayListAdapter(ArrayList<HolidayListModel> al, Context context,EventListener listener) {
        this.al = al;
        this.context = context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyHoledr onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View v=layoutInflater.inflate(R.layout.holiday_style,parent,false);
        return new MyHoledr(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHoledr holder, int position)
    {
         if (al.get(position).getIsActive().equalsIgnoreCase("true"))
         {
             //holder.iv_check.setVisibility(View.VISIBLE);
             holder.iv_check.setColorFilter(Color.parseColor("#4CAF50"), android.graphics.PorterDuff.Mode.SRC_IN);
         }
         else {
            // holder.iv_check.setVisibility(View.GONE);
             holder.iv_check.setColorFilter(Color.parseColor("#004CAF50"), android.graphics.PorterDuff.Mode.SRC_IN);
         }

         holder.tv_name.setText(""+al.get(position).getHolidayName());
         holder.tv_date.setText(""+al.get(position).getFromDate()+"\t\t-\t\t"+al.get(position).getToDate());

         holder.iv_edit.setOnClickListener(v -> showAddHolidayDialog(position));
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void showAddHolidayDialog(int position)
    {
        tv_from =new TextView(context);
        tv2_to =new TextView(context);
        tv_from.setText(""+al.get(position).getFromDate());
        tv2_to.setText(""+al.get(position).getToDate());
        final AlertDialog alertDialog;
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View vv= null;
        if (layoutInflater != null) {
            vv = layoutInflater.inflate(R.layout.add_holiday_view,null,false);
        }
        if (vv!=null)
        {
            EditText et_holiday=vv.findViewById(R.id.et_holiday);
            EditText et_date=vv.findViewById(R.id.et_date);
            et_holiday.setText(al.get(position).getHolidayName());
            AppCompatTextView tv_start=vv.findViewById(R.id.tv_start);
            AppCompatTextView tv_end=vv.findViewById(R.id.tv_end);
            RelativeLayout rv_start=vv.findViewById(R.id.rl_start);
            RelativeLayout rv_end=vv.findViewById(R.id.rl_end);
            tv_start.setText(""+al.get(position).getFromDate());
            tv_end.setText(""+(al.get(position).getToDate()));
            et_date.setText(""+al.get(position).getFromDate()+"\t\tto\t\t"+(al.get(position).getToDate()));
            TextView btn_accept=vv.findViewById(R.id.btn_save);
            Switch holiday_switch=vv.findViewById(R.id.holiday_switch);
            holiday_switch.setChecked(Boolean.parseBoolean(al.get(position).getIsActive()));
            AlertDialog.Builder alb=new AlertDialog.Builder(context);
            alb.setView(vv);
            alertDialog=alb.create();
            Animation transition_in_view = AnimationUtils.loadAnimation(context, R.anim.customer_anim);//customer animation appearance
            vv.setAnimation( transition_in_view );
            vv.startAnimation( transition_in_view );
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.show();

            rv_end.setOnClickListener(v -> new ApiConstants(context).showdateDialog(tv_end));

            rv_start.setOnClickListener(v -> new ApiConstants(context).showdateDialog(tv_start));
            btn_accept.setOnClickListener(v22 ->
            {
                if (et_holiday.getText().toString().isEmpty())
                {
                    Toast.makeText(context, "Enter holiday name", Toast.LENGTH_SHORT).show();
                }

                    else {
                        alertDialog.cancel();
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("holidayId",al.get(position).getHolidayId());
                            jsonObject.put("userId",al.get(position).getUserId());
                            jsonObject.put("holidayName",et_holiday.getText().toString());
                            jsonObject.put("fromDate",tv_from.getText().toString());
                            jsonObject.put("toDate",tv2_to.getText().toString());
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
                //            final Calendar c = Calendar.getInstance();
//            final Calendar c = Calendar.getInstance();
//            mYear = c.get(Calendar.YEAR);
//            mMonth = c.get(Calendar.MONTH);
//            mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
//
//                et_date.setText(""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                         }, mYear, mMonth, mDay);
//            datePickerDialog.setTitle("Choose Date");
//            datePickerDialog.show();

                AlertDialog alertDialog1;
                LayoutInflater layoutInflater1= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                assert layoutInflater1 != null;
                @SuppressLint("InflateParams") View vvv= layoutInflater1.inflate(R.layout.date_range_picker,null,false);
                if (vvv!=null)
                {
                    AlertDialog.Builder alb1 =new AlertDialog.Builder(context);
                    alertDialog1= alb1.setView(vvv).create();
                    Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Objects.requireNonNull(alertDialog1.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
                    calendar = vvv.findViewById(R.id.calendar);
                    rangeCalender(calendar,alertDialog1,et_date);
                    alertDialog1.show();
                }



            });
        }

    }

    private void rangeCalender(DateRangeCalendarView calendar, AlertDialog dialog, EditText et_date)
    {
        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(@NonNull Calendar startDate) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Toast.makeText(context, "Start Date: " + dateFormat.format(startDate.getTime()), Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateRangeSelected(@NonNull Calendar startDate, @NonNull Calendar endDate) {
                dialog.cancel();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String str_date= dateFormat.format(startDate.getTime());
                String end_Date= dateFormat.format(endDate.getTime());
                et_date.setText(str_date + "\t\tto\t\t"+end_Date);
                tv_from =new TextView(context);
                tv2_to =new TextView(context);
                tv_from.setText(""+str_date);
                tv2_to.setText(""+end_Date);


                // Toast.makeText(context, "Start Date: " + dateFormat.format(startDate.getTime()), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    static class MyHoledr extends RecyclerView.ViewHolder
    {

        AppCompatTextView tv_name,tv_date;
        AppCompatImageView iv_check,iv_edit;

        MyHoledr(@NonNull View v) {
            super(v);
            iv_check=v.findViewById(R.id.iv_check);
            iv_edit=v.findViewById(R.id.iv_hedit);
            tv_name=v.findViewById(R.id.tv_hlname);
            tv_date=v.findViewById(R.id.tv_hldate);

        }
    }


    private void saveHoliday(JSONObject jsonObject)
    {
        progressDialog.show();
        OkHttpClient myOkHttpClient = new OkHttpClient.Builder()
                .build();
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request;

        request = new Request.Builder()
                .put(body)
                .url(ApiConstants.Base_Url+ "Holidays/updateHoliday")
                .build();




        Callback updateUICallback = new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String  res=  Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful()&&response.code()==200)
                {

                    Log.d("Tag", "Successfully authenticated");
                    // Toast.makeText(LoginActivity.this, ""+res, Toast.LENGTH_SHORT).show();
                    looper("Success",res);

                }
                else { //called if the credentials are incorrect
                    Log.d("Tag", "Registration failed " + response.networkResponse());
                    looper("500Error",res);

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                looper("Fail","");
            }



        };

        myOkHttpClient.newCall(request).enqueue(updateUICallback);
    }

    private void looper(final String message,String res)
    {
        Handler handler=new Handler(getMainLooper());
        handler.post(() -> {
            progressDialog.dismiss();
            //progressDialog.dismiss();
            if (message.equalsIgnoreCase("Success"))
            {
                listener.onEvent(res,"200");
                // Toast.makeText(context, ""+res, Toast.LENGTH_SHORT).show();

            }
            else if (message.equalsIgnoreCase("Fail"))
            {
                listener.onEvent(res,"400");
                //Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();

            }
            else if (message.equalsIgnoreCase("500Error"))
            {
                listener.onEvent(res,"500");
                //Toast.makeText(context, ""+res, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveData(JSONObject jsonObject)
    {
        progressDialog=ApiConstants.createProgressDialog(context);
        saveHoliday(jsonObject);
    }


}
