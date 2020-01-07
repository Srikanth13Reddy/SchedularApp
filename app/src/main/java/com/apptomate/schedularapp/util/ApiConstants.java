package com.apptomate.schedularapp.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.apptomate.schedularapp.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ApiConstants
{
   Context context;

    public ApiConstants(Context context) {
        this.context = context;
    }

    public static final String Base_Url="https://callscheduler.azurewebsites.net/api/";

    public static final String tc="Help protect your website and its users with clear and fair website terms and conditions. These terms and conditions for a website set out key issues such as acceptable use, privacy, cookies, registration and passwords, intellectual property, links to other sites, termination and disclaimers of responsibility. Terms and conditions are used and necessary to protect a website owner from liability of a user relying on the information or the goods provided from the site then suffering a loss.\n" +
            "\n" +
            "Making your own terms and conditions for your website is hard, not impossible, to do. It can take a few hours to few days for a person with no legal background to make. But worry no more; we are here to help you out" +
            "\n" +
            "All you need to do is fill up the blank spaces and then you will receive an email with your personalized terms and conditions.";
   public static final String cal_note="Sync your calender and we'll tell you who's in your next meeting - and what you should know about\n" +
           " them.Plus ,choose to get connect quickly if you are't yet";


    public static final String con_note="Sync your contact and we'll tell you who's in your next meeting - and what you should know about\n" +
            " them.Plus ,choose to get connect quickly if you are't yet";


    public static final String call_data="[\n" +
            "  {\n" +
            "    \"name\": \"Srikanth Reddy\",\n" +
            "    \"time\": \"Today 6 am\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Sunil\",\n" +
            "    \"time\": \"Today 9 pm\",\n" +
            "    \"type\": \"All\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"John\",\n" +
            "    \"time\": \"Yesterday 9 pm\",\n" +
            "    \"type\": \"All\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mahesh\",\n" +
            "    \"time\": \"25-11-2019\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Vijay Reddy\",\n" +
            "    \"time\": \"Today 6 am\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Ramu R\",\n" +
            "    \"time\": \"Today 9 pm\",\n" +
            "    \"type\": \"All\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Ravi\",\n" +
            "    \"time\": \"Yesterday 9 pm\",\n" +
            "    \"type\": \"All\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Naresh\",\n" +
            "    \"time\": \"25-11-2019\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  }\n" +
            "]";


    public static final String missed_call_data="[\n" +
            "  {\n" +
            "    \"name\": \"Srikanth Reddy\",\n" +
            "    \"time\": \"Today 6 am\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mahesh\",\n" +
            "    \"time\": \"25-11-2019\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Vijay Reddy\",\n" +
            "    \"time\": \"Today 6 am\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Naresh\",\n" +
            "    \"time\": \"25-11-2019\",\n" +
            "    \"type\": \"Missed\"\n" +
            "  }\n" +
            "]";


    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException ignored) {

        }
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
         dialog.setMessage("Please Wait----");
        return dialog;
    }

   public static String getCurrentDay()
   {
       @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
       Date d = new Date();
       String dayOfTheWeek = sdf.format(d);
       return dayOfTheWeek.substring(0,3).toLowerCase();
   }

    public static Date getCurrentDate()
    {

        Date c = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return c;
    }

    public static String getCurrentDate_()
    {

        Date c = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return date;
    }

    public static Date convertTodate(String date)
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    public static String getCurrentTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());

    }

    public void showdateDialog(AppCompatTextView tv)
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


    public void showHourPicker(AppCompatTextView et) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown())
                {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    int mHour=hourOfDay;
                    int  mMin=minute;
                    String AM_PM ;
                    if(hourOfDay < 12) {
                        AM_PM = "AM";

                    } else {
                        AM_PM = "PM";
                        mHour=mHour-12;
                    }



                    et.setText(convertDate(mHour) + "\t:\t" + convertDate(mMin) +"\t\t"+AM_PM);

                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false);
        timePickerDialog.setTitle("Choose Time :");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + input;
        }
    }



}

