package com.apptomate.schedularapp.interfaces;


import org.json.JSONObject;

public interface LoginPresenter
{
    void handleLogin(JSONObject jsonObject, String connectionID,String type);
}
