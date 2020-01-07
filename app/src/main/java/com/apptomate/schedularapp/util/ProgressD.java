package com.apptomate.schedularapp.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressD
{
    private Context context;

    public ProgressD(Context context) {
        this.context = context;
    }

    public ProgressDialog p_show(String msg)
    {
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage(msg);
        return pd;
    }


}
