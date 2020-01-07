package com.apptomate.schedularapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.SmsActivity;
import com.apptomate.schedularapp.model.SmsListModel;
import com.apptomate.schedularapp.util.SaveImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
public class SmsListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<SmsListModel> al;
    public SmsListAdapter(Context context, ArrayList<SmsListModel> al)
    {
        this.context = context;
        this.al = al;
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
         @SuppressLint("ViewHolder") View v= inflater != null ? inflater.inflate(R.layout.sms_list_style, parent, false) : null;
         if (v!=null)
         {
             AppCompatImageView iv_edit= v.findViewById(R.id.iv_sms_iv);
             AppCompatTextView tv_sms_name= v.findViewById(R.id.tv_sms_name);
             if (al.get(position).getIsDefaultText().equalsIgnoreCase("false"))
             {
                 iv_edit.setVisibility(View.VISIBLE);
             }
             else {
                 iv_edit.setVisibility(View.GONE);
             }
             tv_sms_name.setText(""+al.get(position).getSmsUserText());
             v.setOnClickListener(v1 ->
             {
                 if (al.get(position).getIsDefaultText().equalsIgnoreCase("false"))
                 {
                     changeSmsText(position);
                 }


             });
         }

        return v;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void changeSmsText(int position)
    {
        final AlertDialog alertDialog;
        TextView tv_min=new TextView(context);
        TextView tv_max=new TextView(context);
        tv_min.setText(""+30);
        tv_max.setText(""+0);
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View v= null;
        if (layoutInflater != null) {
            v = layoutInflater.inflate(R.layout.sms_alert_set_time,null,false);
        }
        if (v!=null)
        {
            LinearLayout ll=v.findViewById(R.id.ll_number);
            TextView tv_cancel_sms=v.findViewById(R.id.tv_cancel_sms);
            TextView tv_ok_sms=v.findViewById(R.id.tv_ok_sms);
            AppCompatEditText et_sms_txt= v.findViewById(R.id.et_sms_text);
            et_sms_txt.setText(""+al.get(position).getSmsUserText());
            NumberPicker hour_picker=v.findViewById(R.id.numberPicker_hour);
            TextView tv_text =v.findViewById(R.id.tv_tex);
            NumberPicker min_picker=v.findViewById(R.id.numberPicker_minute);
            if (al.get(position).getIsDurationSettingApplicable().equalsIgnoreCase("true"))
            {
                ll.setVisibility(View.VISIBLE);
                et_sms_txt.setFocusable(false);
                tv_text.setVisibility(View.GONE);
            }
            else {
                ll.setVisibility(View.GONE);
                tv_text.setVisibility(View.VISIBLE);
                et_sms_txt.setFocusable(true);
            }
            hour_picker.setMinValue(0);
            hour_picker.setMaxValue(24);
            min_picker.setMinValue(0);
            min_picker.setMaxValue(60);
            min_picker.setValue(30);
            min_picker.setOnValueChangedListener((picker, oldVal, newVal) -> tv_min.setText(""+newVal));
            hour_picker.setOnValueChangedListener((picker, oldVal, newVal) -> tv_max.setText(""+newVal));
            // int hour= hour_picker.getValue();
            // int min= min_picker.getValue();
            AlertDialog.Builder alb=new AlertDialog.Builder(context);
            alb.setView(v);
            alertDialog=alb.create();
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


//        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Animation transition_in_view = AnimationUtils.loadAnimation(context, R.anim.customer_anim);//customer animation appearance
            v.setAnimation( transition_in_view );
            v.startAnimation( transition_in_view );


            alertDialog.show();
            tv_cancel_sms.setOnClickListener(v12 -> alertDialog.cancel());
            tv_ok_sms.setOnClickListener(v1 ->
            {
                alertDialog.cancel();
                saveData(position,""+tv_max.getText().toString()+":"+tv_min.getText().toString(), Objects.requireNonNull(et_sms_txt.getText()).toString());
            });
        }


    }

    private void saveData(int position,String time,String text)
    {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("userSmsSettingId", al.get(position).getUserSmsSettingId());
            jsonObject.put("userId", al.get(position).getUserId());
            jsonObject.put("smsSettingId", al.get(position).getSmsSettingId());
            jsonObject.put("smsUserText", text);
            jsonObject.put("timeDurationforUser",time);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        ((SmsActivity)context).progressDialog.show();
        new SaveImpl(((SmsActivity)context)).handleSave(jsonObject,"SmsSettings/updateUserSMSSetting","PUT");
    }




}
