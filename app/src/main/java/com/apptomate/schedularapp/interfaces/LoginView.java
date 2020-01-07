package com.apptomate.schedularapp.interfaces;

public interface LoginView
{
    void onSucess(String code,String response);
    void onFailure(String error);
}
