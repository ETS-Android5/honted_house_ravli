package com.pesonal.adsdk.utils;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public interface SplashListner {

    void onSuccess();

    void onExtra(JsonObject extraData);
}
