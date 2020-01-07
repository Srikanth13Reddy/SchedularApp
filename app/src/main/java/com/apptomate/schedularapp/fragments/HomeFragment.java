package com.apptomate.schedularapp.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apptomate.schedularapp.adapter.CallListAdapter;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.model.CallListModel;
import com.apptomate.schedularapp.util.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Context context;
    private RecyclerView rv;
    private Button btn_all,btn_missed;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        rv=v.findViewById(R.id.rv_all);
        btn_all=v.findViewById(R.id.btn_all);
        btn_missed=v.findViewById(R.id.btn_missed);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), llm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        rv.setLayoutManager(llm);
        getData();
        btn_actions();
        return  v;
    }

    private void btn_actions()
    {
        btn_all.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_all.setBackgroundColor(context.getColor(R.color.colorPrimary));
                btn_all.setTextColor(context.getColor(R.color.white));
                btn_missed.setBackgroundColor(context.getColor(R.color.white));
                btn_missed.setTextColor(context.getColor(R.color.colorPrimary));
            }
            getData();
        });
        btn_missed.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_all.setBackgroundColor(context.getColor(R.color.white));
                btn_all.setTextColor(context.getColor(R.color.colorPrimary));
                btn_missed.setBackgroundColor(context.getColor(R.color.colorPrimary));
                btn_missed.setTextColor(context.getColor(R.color.white));
            }
            getMissedData();
        });
    }

    private void getMissedData()
    {
        ArrayList<CallListModel> al=new ArrayList<>();

        try {
            JSONArray ja=new JSONArray(ApiConstants.missed_call_data);
            for (int i=0;i<ja.length();i++)
            {
                JSONObject json= ja.getJSONObject(i);
                String name=json.getString("name");
                String time=json.getString("time");
                String type=json.getString("type");
                CallListModel ca=new CallListModel();
                ca.setName(name);
                ca.setTime(time);
                ca.setType(type);
                al.add(ca);
            }
            CallListAdapter ca=new CallListAdapter(context,al);
            rv.setAdapter(ca);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData() {

        ArrayList<CallListModel> al=new ArrayList<>();

        try {
            JSONArray ja=new JSONArray(ApiConstants.call_data);
            for (int i=0;i<ja.length();i++)
            {
               JSONObject json= ja.getJSONObject(i);
                String name=json.getString("name");
                String time=json.getString("time");
                String type=json.getString("type");
                CallListModel ca=new CallListModel();
                ca.setName(name);
                ca.setTime(time);
                ca.setType(type);
                al.add(ca);
            }
            CallListAdapter ca=new CallListAdapter(context,al);
            rv.setAdapter(ca);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
