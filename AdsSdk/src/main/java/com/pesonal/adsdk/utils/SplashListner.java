package com.pesonal.adsdk.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public interface SplashListner {

    void onSuccess();

    void onExtra(JsonElement extraData);
}
