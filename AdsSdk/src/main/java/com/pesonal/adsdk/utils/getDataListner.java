package com.pesonal.adsdk.utils;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public interface getDataListner {

    void onSuccess();

    void onUpdate(String url);

    void onRedirect(String url);

    void onReload();

    void onGetExtradata(JsonObject extraData);
}
