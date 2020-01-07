package com.apptomate.schedularapp.interfaces;

import org.json.JSONObject;

public interface SavePresenter
{
    void handleSave(JSONObject jsonObject, String connectionID, String type);
}
