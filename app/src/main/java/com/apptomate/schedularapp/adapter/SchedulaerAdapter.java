package com.apptomate.schedularapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.model.ScheduleModel;
import java.util.ArrayList;

public class SchedulaerAdapter extends RecyclerView.Adapter<SchedulaerAdapter.MyHolder>
{
    private ArrayList<ScheduleModel> al;
    private Context context;
    private int row_index=-1;
    private EventListener listener;
    private String currentdate;
    public interface EventListener
    {
        void onEvent(ScheduleModel res);
    }

    public SchedulaerAdapter(ArrayList<ScheduleModel> al, Context context, EventListener listener, String currentdate)
    {
        this.al = al;
        this.context = context;
        this.listener=listener;
        this.currentdate=currentdate;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v= null;
        if (li != null) {
            v = li.inflate(R.layout.business_hours_days_style,parent,false);
        }
        assert v != null;
        return new MyHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position)
    {
        holder.btn_days.setText(al.get(position).getDay());
       if (al.get(position).getIsActive().equalsIgnoreCase("true"))
       {
           holder.btn_days.setBackgroundColor(Color.parseColor("#a7d0e4"));
       }
       holder.btn_days.setOnClickListener(v -> {
           listener.onEvent(al.get(position));
           row_index=position;
           notifyDataSetChanged();
       });

        if(row_index==position)
        {
            holder.btn_days.setBackgroundColor(Color.BLACK);
           // holder.btn_days.setBackgroundColor(context.getColor(R.color.colorPrimary));
            holder.btn_days.setBackground(context.getDrawable(R.drawable.round_touch));
            holder.btn_days.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            holder.btn_days.setTextColor(Color.parseColor("#000000"));


            if (al.get(position).getIsActive().equalsIgnoreCase("true"))
            {
               // holder.btn_days.setBackgroundColor(Color.parseColor("#a7d0e4"));
                holder.btn_days.setBackground(context.getDrawable(R.drawable.round_active));

            }
            else {
               // holder.btn_days.setBackgroundColor(Color.parseColor("#6AF6F6F6"));
                holder.btn_days.setBackground(context.getDrawable(R.drawable.round_border));
            }
        }

        if (al.get(position).getDay().equalsIgnoreCase(currentdate))
        {
            //holder.btn_days.setBackgroundColor(Color.GREEN);
            holder.btn_days.setBackground(context.getDrawable(R.drawable.round_current));
        }
    }

    @Override
    public int getItemCount()
    {
        return al.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder
    {

        Button btn_days;

        MyHolder(@NonNull View v)
        {
            super(v);
            btn_days= v.findViewById(R.id.btn_days);

        }
    }
}
