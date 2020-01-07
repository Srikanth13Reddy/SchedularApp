package com.apptomate.schedularapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;

import com.apptomate.schedularapp.adapter.ContactsListAdapter;
import com.apptomate.schedularapp.adapter.ContactsModel;
import com.apptomate.schedularapp.util.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class ContactsActivity extends AppCompatActivity
{
    RecyclerView rv;
    public static final int REQUEST_READ_CONTACTS = 89;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv=findViewById(R.id.rv_con);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), llm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        rv.setLayoutManager(llm);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getAllContacts();
        } else {
            requestPermission();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getAllContacts()
    {
        ArrayList<ContactsModel> al=new ArrayList<>();
        ArrayList<ContactsModel> listWithoutDuplicates = null;
        //syncToServer();
        ProgressDialog pd=new ProgressD(ContactsActivity.this).p_show("Loading your contacts-----");
        pd.show();
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

                        ContactsModel contactsModel=new ContactsModel(name,phoneNo);
//                        contactsModel.setName(name);
//                        contactsModel.setNumber(phoneNo);
                        al.add(contactsModel);
                        HashSet<ContactsModel> listToSet = new HashSet<ContactsModel>(al);
                         listWithoutDuplicates = new ArrayList<ContactsModel>(listToSet);





                    }
                    pCur.close();

                }
            }
        }
        if (cur!= null)
        {
            cur.close();
            pd.dismiss();
          //  Log.e("Phone",""+recipients.length());
           // syncToServer();
            if (listWithoutDuplicates != null) {
                Collections.sort(listWithoutDuplicates, Comparator.comparing(ContactsModel::getName));
            }
            ContactsListAdapter adapter=new ContactsListAdapter(this,listWithoutDuplicates);
            rv.setAdapter(adapter);

        }

    }
}
