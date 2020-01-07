package com.apptomate.schedularapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.apptomate.schedularapp.adapter.SmsListAdapter;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.interfaces.SaveView;
import com.apptomate.schedularapp.model.SmsListModel;
import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.LoginImpl;
import com.apptomate.schedularapp.util.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SmsActivity extends AppCompatActivity implements LoginView, SaveView
{

    ListView sms_list;
    public ProgressDialog progressDialog;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMS");
        user_id=new SharedPrefs(this).getUserDetails().get(SharedPrefs.KEY_USERID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sms_list=findViewById(R.id.sms_list);
        getListData();
    }

    public void getListData()
    {

        progressDialog = ApiConstants.createProgressDialog(this);
        LoginImpl login=new LoginImpl(this);
        login.handleLogin(new JSONObject(),"SmsSettings/userSMSSettingByUserId?userId="+user_id,"GET");

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


    @Override
    public void onSucess(String code, String response) {
        Log.e("Settings_Response", response);
        if (code.equalsIgnoreCase("200"))
        {
            assignData(response);
        }
        progressDialog.dismiss();
    }

    private void assignData(String response)
    {
        ArrayList<SmsListModel> al=new ArrayList<>();
                            Log.e("Settings_Response", response);
                    try {
                        JSONArray ja=new JSONArray(response);
                        for (int i=0;i<ja.length();i++)
                        {
                            JSONObject js= ja.getJSONObject(i);
                            String userSmsSettingId= js.getString("userSmsSettingId");
                            String userId= js.getString("userId");
                            String name= js.getString("name");
                            String phoneNumber= js.getString("phoneNumber");
                            String smsSettingId= js.getString("smsSettingId");
                            String smsUserText= js.getString("smsUserText");
                            String timeDurationforUser= js.getString("timeDurationforUser");
                            String isDefaultText= js.getString("isDefaultText");
                            String isDurationSettingApplicable=js.getString("isDurationSettingApplicable");
                            SmsListModel listModel=new SmsListModel();
                            listModel.setName(name);
                            listModel.setIsDefaultText(isDefaultText);
                            listModel.setUserId(userId);
                            listModel.setIsDurationSettingApplicable(isDurationSettingApplicable);
                            listModel.setPhoneNumber(phoneNumber);
                            listModel.setUserSmsSettingId(userSmsSettingId);
                            listModel.setSmsSettingId(smsSettingId);
                            listModel.setSmsUserText(smsUserText);
                            listModel.setTimeDurationforUser(timeDurationforUser);
                            al.add(listModel);
                        }
                        SmsListAdapter adapter=new SmsListAdapter(this,al);
                        sms_list.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
    }

    @Override
    public void onFailure(String error) {
        Log.e("Settings_Response", ""+error);
        progressDialog.dismiss();
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
       progressDialog.dismiss();
        getListData();
    }

    @Override
    public void onSaveFailure(String error) {
       progressDialog.dismiss();
    }
}
