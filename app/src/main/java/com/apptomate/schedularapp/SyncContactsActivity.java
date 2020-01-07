package com.apptomate.schedularapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apptomate.schedularapp.interfaces.LoginView;
import com.apptomate.schedularapp.util.ApiConstants;
import com.apptomate.schedularapp.util.LoginImpl;
import com.apptomate.schedularapp.util.ProgressD;
import com.apptomate.schedularapp.util.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncContactsActivity extends AppCompatActivity implements LoginView {

    ProgressDialog progressDialog;
    public static final int REQUEST_READ_CONTACTS = 79;
    JSONArray recipients;
    SharedPrefs sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_contacts);
        getSupportActionBar().hide();
        sharedPrefs=new SharedPrefs(this);
        progressDialog = new ProgressD(this).p_show("Please wait your contacts syncing......");
        TextView tv_con = findViewById(R.id.tv_con_note);
        tv_con.setText(ApiConstants.con_note);
//        if (sharedPrefs.isContactSyncing())
//        {
//            finish();
//            Intent i = new Intent(SyncContactsActivity.this, MainActivity.class);
//            startActivity(i);
//           // overridePendingTransition(R.anim.right_in, R.anim.left_out);
//        }

    }

    public void skip_con(View view) {
        finish();
        Intent i = new Intent(SyncContactsActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onSucess(String code, String response)
    {
        progressDialog.dismiss();
        Log.e("ContactsSyncResponse-->",response);
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        if (code.equalsIgnoreCase("200"))
        {
            sharedPrefs.conSyncSuccess();
            finish();
            Intent i=new Intent(SyncContactsActivity.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public void onFailure(String error)
    {
        progressDialog.dismiss();
        Log.e("ContactsSyncResponse-->",""+error);
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    public void syncContacts(View view)

    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getAllContacts();
        } else {
            requestPermission();
        }


    }

    void syncToServer()
    {


        progressDialog.show();
        String user_id= new SharedPrefs(this).getUserDetails().get(SharedPrefs.KEY_USERID);


        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("userID", user_id);
            jsonObject.put("contact",recipients.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LoginImpl login = new LoginImpl(this);
        login.handleLogin(jsonObject, "Contact/syncContacts","PUT");
    }

    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   getAllContacts();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    private void getAllContacts()
    {
        //syncToServer();
        ProgressDialog pd=new ProgressD(SyncContactsActivity.this).p_show("Loading your contacts-----");
        pd.show();
         recipients = new JSONArray();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur!= null ? cur.getCount() : 0) > 0) {
            while (cur!=null && cur.moveToNext())
            {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
//                nameList.add(name);
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext())
                    {


                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));



                        try {
                            JSONObject idsJsonObject = new JSONObject();
                            idsJsonObject.put("contactName", name);
                            idsJsonObject.put("contactNumber", phoneNo);
                            recipients.put(idsJsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                    pCur.close();

                }
            }
        }
        if (cur!= null) {
            cur.close();
            pd.dismiss();
            Log.e("Phone",""+recipients.length());
            syncToServer();
        }

    }
}
