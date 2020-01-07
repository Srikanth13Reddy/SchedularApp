package com.apptomate.schedularapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ForgatPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgat_password);
        getSupportActionBar().hide();
    }

    public void continue_(View view)
    {
        Intent i=new Intent(ForgatPasswordActivity.this,OtpVerificationActivity.class);
        startActivity(i);
    }
}
