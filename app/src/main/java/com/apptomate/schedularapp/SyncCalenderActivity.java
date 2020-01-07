package com.apptomate.schedularapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.SharedPrefs;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class SyncCalenderActivity extends AppCompatActivity
{
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_calender);
        getSupportActionBar().hide();
        sharedPrefs=new SharedPrefs(this);
        TextView tv_cal_note=findViewById(R.id.tv_cal_note);
        tv_cal_note.setText(ApiConstants.cal_note);

        if (sharedPrefs.isContactSyncing())
        {
            finish();
            Intent i=new Intent(SyncCalenderActivity.this,MainActivity.class);
            startActivity(i);
           // overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    public void skip_cal(View view)
    {
        Intent i=new Intent(SyncCalenderActivity.this,SyncContactsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    public void syncCalender(View view)
    {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
        {
            permissions();
        }
        else {
            sharedPrefs.calSyncSuccess();
            Intent i=new Intent(SyncCalenderActivity.this,SyncContactsActivity.class);
            startActivity(i);
            finish();

            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

    }


    public void permissions()
    {
        Dexter.withActivity(this)

                .withPermission(Manifest.permission.WRITE_CALENDAR)

                .withListener(new PermissionListener() {

                    @Override

                    public void onPermissionGranted( PermissionGrantedResponse response)
                    {
                        sharedPrefs.calSyncSuccess();
                        finish();
                        Intent i=new Intent(SyncCalenderActivity.this,SyncContactsActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    }

                    @Override

                    public void onPermissionDenied( PermissionDeniedResponse response) {

                        // check for permanent denial of permission

                        if (response.isPermanentlyDenied())
                        {

                            // navigate user to app settings

                        }

                    }

                    @Override

                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();

                    }

                }).check();
    }
}
