package com.apptomate.schedularapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.apptomate.schedularapp.R;

public class NavMenuAdapter extends BaseAdapter
{
    Context context;
    String[] names={"Alert","SMS","Do Not Disturb","Holiday Schedule","Business Hours","Contacts","Calender"};
    int [] images={R.drawable.alert,R.drawable.email,R.drawable.donotdisturb,R.drawable.holidayschedule,R.drawable.bhours,R.drawable.holidayschedule,R.drawable.bhours};

    public NavMenuAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View v= null;
        if (layoutInflater != null) {
            v = layoutInflater.inflate(R.layout.nav_style,parent,false);
        }
        AppCompatImageView iv=v.findViewById(R.id.nav_iv);
        AppCompatTextView tv=v.findViewById(R.id.nav_tv);
        iv.setImageResource(images[position]);
        tv.setText(names[position]);

        return v;
    }
}
