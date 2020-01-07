package com.apptomate.schedularapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.interfaces.SaveView;
import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.LoginImpl;
import com.apptomate.schedularapp.util.SaveImpl;
import com.apptomate.schedularapp.util.SharedPrefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

import petrov.kristiyan.colorpicker.ColorPicker;
public class AlertActivity extends AppCompatActivity implements LoginView, SaveView
{

    AppCompatTextView tv_duration,tv_color,tv_tone;
     AlertDialog alertDialog;
    ProgressDialog progressDialog;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Alert");
        user_id=new SharedPrefs(this).getUserDetails().get(SharedPrefs.KEY_USERID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout ll_alert=findViewById(R.id.ll_alert);
         tv_duration=findViewById(R.id.tv_aduration);
         tv_color=findViewById(R.id.tv_acolor);
         tv_tone=findViewById(R.id.tv_a_tone);

        ll_alert.setOnClickListener(v -> {

            LayoutInflater layoutInflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View vv= layoutInflater != null ? layoutInflater.inflate(R.layout.alert_duration_view, null, false) : null;
            if (vv!=null)
            {
                TextView tv_cancel=vv.findViewById(R.id.tv_cancel);
                TextView tv_ok=vv.findViewById(R.id.tv_ok);
                RadioGroup rg= vv.findViewById(R.id.rg);
                AlertDialog.Builder alb=new AlertDialog.Builder(AlertActivity.this);
                alb.setView(vv);
                alertDialog=alb.create();
                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Animation transition_in_view = AnimationUtils.loadAnimation(AlertActivity.this, R.anim.customer_anim);//customer animation appearance
                vv.setAnimation( transition_in_view );
                vv.startAnimation( transition_in_view );
                alertDialog.show();
                findRadioButtons(vv);
                // String text=findRadioButtons(vv);
                //    btn_1.setOnClickListener(v1 -> Toast.makeText(AlertActivity.this, ""+btn_1.getText().toString(), Toast.LENGTH_SHORT).show());
//
//               int id= rg.getCheckedRadioButtonId();
//                AppCompatRadioButton radioButton=findViewById(id);
//                tv_ok.setOnClickListener(v1 ->
//                {
//
//                    alertDialog.cancel();
//                    Toast.makeText(AlertActivity.this, ""+radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
//
//
//                });

                tv_cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.cancel();
                    }
                });
            }

        });
        getData();
    }

    @SuppressLint("SetTextI18n")
    private void findRadioButtons(View vv)
    {
        RadioButton radioButton=vv.findViewById(R.id.radioButton);
        RadioButton radioButton1=vv.findViewById(R.id.radioButton1);
        RadioButton radioButton2=vv.findViewById(R.id.radioButton2);
        RadioButton radioButton3=vv.findViewById(R.id.radioButton3);
        RadioButton radioButton4=vv.findViewById(R.id.radioButton4);
        RadioButton radioButton5=vv.findViewById(R.id.radioButton5);
        RadioButton radioButton6=vv.findViewById(R.id.radioButton6);

        radioButton.setOnClickListener(v -> {
            alertDialog.cancel();
            tv_duration.setText(""+radioButton.getText().toString());
        });
        radioButton1.setOnClickListener(v -> {
            alertDialog.cancel();
            tv_duration.setText(""+radioButton1.getText().toString());
        });
        radioButton2.setOnClickListener(v -> {
            alertDialog.cancel();
            tv_duration.setText(""+radioButton2.getText().toString());
        });
        radioButton3.setOnClickListener(v -> {
            alertDialog.cancel();
            tv_duration.setText(""+radioButton3.getText().toString());
        });
        radioButton4.setOnClickListener(v -> {
            alertDialog.cancel();
            tv_duration.setText(""+radioButton4.getText().toString());
        });
        radioButton5.setOnClickListener(v -> {
            alertDialog.cancel();
            tv_duration.setText(""+radioButton5.getText().toString());
        });
        radioButton6.setOnClickListener(v -> {
            alertDialog.cancel();
            tv_duration.setText("");
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void getData()
    {
        progressDialog = ApiConstants.createProgressDialog(AlertActivity.this);
        LoginImpl login=new LoginImpl(this);
        login.handleLogin(new JSONObject(),"UserSettings/selectUserSettingById?userId="+user_id,"GET");

    }

    public void openColorDialog(View view)
    {
         ColorPicker colorPicker = new ColorPicker(this);
         colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChooseColor(int position,int color)
            {
                tv_color.setBackgroundColor(color);
                tv_color.setText(""+color);

            }

            @Override
            public void onCancel(){

                colorPicker.dismissDialog();
            }
        });
    }

    public void save(View view)
    {
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("userId", user_id);
            jsonObject.put("alertcolor", tv_color.getText().toString());
            jsonObject.put("alerttone", tv_tone.getText().toString());
            jsonObject.put("alertDuration", tv_duration.getText().toString());
            jsonObject.put("doNotDisturbStatus", "true");



        } catch (JSONException e) {
            e.printStackTrace();
        }

        SaveImpl login = new SaveImpl(this);
        login.handleSave(jsonObject, "UserSettings/saveUserSettings","PUT");
    }

    public void getTone(View view)
    {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
                tv_tone.setText(""+uri.getPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSucess(String code, String response)
    {
        progressDialog.dismiss();
      if (code.equalsIgnoreCase("200"))
      {
          assignData(response);
      }
      else {
          Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
      }
    }

    @SuppressLint("SetTextI18n")
    private void assignData(String response)
    {

        try {
            JSONArray ja=new JSONArray(response);
            JSONObject json=ja.getJSONObject(0);
            int settingId= json.getInt("settingId");
            int userId= json.getInt("userId");
            String name=json.getString("name");
            String alertcolor=json.getString("alertcolor");
            String alerttone=json.getString("alerttone");
            String alertDuration=json.getString("alertDuration");
            String doNotDisturbStatus=json.getString("doNotDisturbStatus");
            tv_duration.setText(""+alertDuration);
            tv_tone.setText(""+alerttone);
            if (alertcolor.charAt(0)=='#')
            {
                tv_color.setBackgroundColor(Color.parseColor(alertcolor));
                tv_color.setText(""+alertcolor);
            }
            else {
                tv_color.setBackgroundColor(Integer.parseInt(alertcolor));
                tv_color.setText(""+alertcolor);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String error)
    {
        progressDialog.dismiss();
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        progressDialog.dismiss();
        if (code.equalsIgnoreCase("200"))
        {
            getData();
        }
    }

    @Override
    public void onSaveFailure(String error)
    {
        progressDialog.dismiss();
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }
}
