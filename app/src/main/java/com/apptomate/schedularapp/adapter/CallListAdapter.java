package com.apptomate.schedularapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.apptomate.schedularapp.R;
import com.apptomate.schedularapp.model.CallListModel;

import java.util.ArrayList;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.MyHolder>
{
    private Context context;
    private ArrayList<CallListModel> al;

    public CallListAdapter(Context context, ArrayList<CallListModel> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v= null;
        if (li != null) {
            v = li.inflate(R.layout.call_list_style,parent,false);
        }
        assert v != null;
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position)
    {

        if (al.get(position).getType().equalsIgnoreCase("Missed"))
        {
            holder.tv_cname.setTextColor(Color.parseColor("#dd0a2d"));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                holder.btn_name.setBackground(context.getDrawable(R.drawable.missed_color_circle));
//            }

           // holder.iv_call.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        }

        holder.btn_name.setText(""+al.get(position).getName().charAt(0));
        holder.tv_cname.setText(al.get(position).getName());
        holder.tv_ctime.setText(al.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder
    {

        Button btn_name;
        TextView tv_cname,tv_ctime;
        ImageView iv_call;

        MyHolder(@NonNull View v)
        {
            super(v);
            btn_name=v.findViewById(R.id.btn_name);
            tv_cname=v.findViewById(R.id.tv_cname);
            tv_ctime=v.findViewById(R.id.tv_ctime);
            iv_call=v.findViewById(R.id.iv_call);

        }
    }

}
