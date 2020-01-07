package com.apptomate.schedularapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.LoginImpl;
import com.apptomate.schedularapp.util.SharedPrefs;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginView {

    ProgressDialog progressDialog;
    CallbackManager callbackManager;
    AppCompatEditText et_email, et_password;
    SharedPrefs sharedPrefs;
    private SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPrefs = new SharedPrefs(this);
        et_email = findViewById(R.id.et_lmail);
        et_password = findViewById(R.id.et_lpass);
        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

    }

    public void login(View view) {
        if (Objects.requireNonNull(et_email.getText()).toString().isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        } else {
            if (Objects.requireNonNull(et_password.getText()).toString().isEmpty()) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            } else {
                loginData();
            }
        }
    }

    private void loginData() {
        progressDialog = ApiConstants.createProgressDialog(this);

        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("email", Objects.requireNonNull(et_email.getText()).toString());
            jsonObject.put("password", Objects.requireNonNull(et_password.getText()).toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        LoginImpl login = new LoginImpl(this);
        login.handleLogin(jsonObject, "JWTAuthentication/login", "POST");
    }

    public void signUp(View view) {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
    }

    public void forgotPassword(View view) {

        Intent i = new Intent(LoginActivity.this, ForgatPasswordActivity.class);
        startActivity(i);
    }

    @Override
    public void onSucess(String code, String response) {
        Log.e("Login Response---->", "" + response);
        progressDialog.dismiss();
        if (code.equalsIgnoreCase("200")) {
            storeData(response);
        } else {
            try {
                JSONObject js = new JSONObject(response);
                Toast.makeText(this, "" + js.getString("errorMessage"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onFailure(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    private void storeData(String response) {
        try {
            JSONObject js = new JSONObject(response);
            JSONObject json = js.getJSONObject("userDetails");
            Log.e("LoginJSONObject", "" + json);
            String userId = json.getString("userId");
            String name = json.getString("name");
            String searchUserId = json.getString("searchUserId");
            String phoneNumber = json.getString("phoneNumber");
            String email = json.getString("email");
            String password = json.getString("password");
            boolean isPremiumUser = json.getBoolean("isPremiumUser");
            boolean isPhoneNumberVerified = json.getBoolean("isPhoneNumberVerified");
            boolean isTermsConditionAccepted = json.getBoolean("isTermsConditionAccepted");
            String lastCalenderSync = json.getString("lastCalenderSync");
            String lastContactSync = json.getString("lastContactSync");
            String createdDate = json.getString("createdDate");
            sharedPrefs.LoginSuccess();
            sharedPrefs.setLoginSession(userId, name, searchUserId, phoneNumber, email, password, isPremiumUser, isPhoneNumberVerified, isTermsConditionAccepted, createdDate);
            finish();
            if (sharedPrefs.isCalctSyncing()&&sharedPrefs.isContactSyncing())
            {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
            else if (sharedPrefs.isCalctSyncing()&&!sharedPrefs.isContactSyncing())
            {
                Intent i = new Intent(LoginActivity.this, SyncContactsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
            else {
                Intent i = new Intent(LoginActivity.this, SyncCalenderActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode)
            {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w("Tag", "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }


    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
//        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra(ProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount);
//
//        startActivity(intent);
//        finish();
        Toast.makeText(this, ""+googleSignInAccount.getEmail(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d("TAG", "Not logged in");
        }
    }





}
