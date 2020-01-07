package com.apptomate.schedularapp.util;
import android.os.Handler;
import android.util.Log;
import com.apptomate.schedularapp.interfaces.LoginPresenter;
import com.apptomate.schedularapp.interfaces.LoginView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.os.Looper.getMainLooper;

public class LoginImpl implements LoginPresenter
{
    private LoginView loginView;
    private String res;
    private Request request_;
    private String myResult_;
    // ProgressDialog progressDialog;


    public LoginImpl(LoginView loginView)
    {
        this.loginView = loginView;

    }

    @Override
    public void handleLogin(JSONObject jsonObject,String connectionId,String type)
    {

       onRegister(jsonObject,connectionId,type);
      //  getData(jsonObject,connectionId,type);
    }

    private void onRegister(JSONObject jsonObject, String connectionId, String type)
    {

       // Log.d("", "In onRegister");
        OkHttpClient myOkHttpClient = new OkHttpClient.Builder().build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = null;
        
        
        switch (type)
        {
            case "POST":
                request = new Request.Builder()
                        .post(body)
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .url(ApiConstants.Base_Url+ connectionId)
                        .build();
                break;
            case "GET":
                request = new Request.Builder()
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .url(ApiConstants.Base_Url+ connectionId)
                        .build();
                break;
            case "PUT":
                request = new Request.Builder()
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .put(body)
                        .url(ApiConstants.Base_Url+ connectionId)
                        .build();
                break;
                
        }



        Callback updateUICallback = new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                res=  Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful()&&response.code()==200)
                {

                    Log.d("Tag", "Successfully authenticated");
                    // Toast.makeText(LoginActivity.this, ""+res, Toast.LENGTH_SHORT).show();
                    looper("Success");

                }
                else { //called if the credentials are incorrect
                    Log.d("Tag", "Registration failed " + response.networkResponse());
                    looper("500Error");

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                looper("Fail");
            }



        };

        if (request != null) {
            myOkHttpClient.newCall(request).enqueue(updateUICallback);
        }
    }

    private void looper(final String message)
    {
        Handler handler=new Handler(getMainLooper());
        handler.post(() -> {
            //progressDialog.dismiss();
            if (message.equalsIgnoreCase("Success"))
            {
                loginView.onSucess("200",res);
            }
            else if (message.equalsIgnoreCase("Fail"))
            {
                loginView.onFailure("Something Went Wrong");
            }
            else if (message.equalsIgnoreCase("500Error"))
            {
                loginView.onSucess("500",res);
            }
        });
    }


   

}

