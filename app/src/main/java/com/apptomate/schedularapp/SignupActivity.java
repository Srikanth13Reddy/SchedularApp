package com.apptomate.schedularapp;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.interfaces.SaveView;
import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.LoginImpl;
import com.apptomate.schedularapp.util.ProgressD;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements LoginView, SaveView
{

    AppCompatEditText et_name,et_phone,et_email,et_password;
    CheckBox cb_terms;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).hide();
        progressDialog=new ProgressD(this).p_show("Please wait ......");
        TextView tv_tc=findViewById(R.id.tv_tc);
        SpannableString content = new SpannableString("Terms&Conditions");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv_tc.setText(content);
        et_name=findViewById(R.id.et_sname);
        et_phone=findViewById(R.id.et_snumber);
        et_email=findViewById(R.id.et_smail);
        et_password=findViewById(R.id.et_spassword);
        cb_terms=findViewById(R.id.cb_terms);


    }

    public void login(View view)
    {
        Intent i=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(i);
    }


    @SuppressLint("InflateParams")
    public void termsandConditions(View view)
    {
        final AlertDialog alertDialog;
        LayoutInflater layoutInflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View v= null;
        if (layoutInflater != null) {
            v = layoutInflater.inflate(R.layout.termsandconditions_style,null,false);
        }
        if (v!=null)
        {
            TextView tv_terms=v.findViewById(R.id.tv_terms);
            tv_terms.setText(ApiConstants.tc);
            TextView btn_accept=v.findViewById(R.id.btn_acpt);
            AlertDialog.Builder alb=new AlertDialog.Builder(this);
            alb.setView(v);
            alertDialog=alb.create();
            Animation transition_in_view = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.customer_anim);//customer animation appearance
            v.setAnimation( transition_in_view );
            v.startAnimation( transition_in_view );
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.show();
            btn_accept.setOnClickListener(v1 -> alertDialog.cancel());
        }

    }

    @Override
    public void onSucess(String code,String response)
    {
        progressDialog.dismiss();
        Log.e("SignUp  Response----->",response);

        if (code.equalsIgnoreCase("200"))
        {

            finish();
            Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(i);
        }
        else {
            try {
                JSONObject js=new JSONObject(response);
                Toast.makeText(this, ""+js.getString("errorMessage"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(String error) {
        progressDialog.dismiss();
        Log.e("SignUp  Response----->",""+error);
    }

    public void signUp(View view)
    {
       if (Objects.requireNonNull(et_name.getText()).toString().isEmpty())
       {
           Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
       }
       else {
           if (Objects.requireNonNull(et_phone.getText()).toString().isEmpty())
           {
               Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
           }
           else {
               if (Objects.requireNonNull(et_email.getText()).toString().isEmpty())
               {
                   Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
               }
               else {
                   if (!et_email.getText().toString().matches(emailPattern))
                   {
                       Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();

                   }
                   else {
                       if (Objects.requireNonNull(et_password.getText()).toString().isEmpty())
                       {
                           Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                       }
                       else {
                           if (cb_terms.isChecked())
                           {
                               signUpData();
                           }
                           else {
                               Toast.makeText(this, "Before Sign up accept Terms and Conditions", Toast.LENGTH_SHORT).show();
                           }
                       }


                   }
               }
           }
       }
    }

    private void signUpData()
    {
        progressDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("name", Objects.requireNonNull(et_name.getText()).toString());
            jsonObject.put("phoneNumber", Objects.requireNonNull(et_phone.getText()).toString());
            jsonObject.put("email", Objects.requireNonNull(et_email.getText()).toString());
            jsonObject.put("password", Objects.requireNonNull(et_password.getText()).toString());
            jsonObject.put("isPremiumUser","false");
            jsonObject.put("isPhoneNumberVerified","false");
            jsonObject.put("isTermsConditionAccepted","true");
            jsonObject.put("lastCalenderSync","");
            jsonObject.put("lastContactSync","");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        LoginImpl login=new LoginImpl(this);
        login.handleLogin(jsonObject,"User/createUser","POST");
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        if (code.equalsIgnoreCase("200"))
        {
            try {
                JSONObject js=new JSONObject(response);
                js.getString("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveFailure(String error)
    {

    }
}
