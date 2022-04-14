package com.demo.mycommonapp;

import android.util.Log;

import com.pesonal.adsdk.AppClass;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.AdvertisementState;
import com.pesonal.adsdk.remote.AnalyticsCallback;
import com.pesonal.adsdk.remote.InterCallback;

public class MyApp extends AppClass {


    @Override
    public void onState(AdvertisementState state) {
        Log.e("MyApp", "state: "+state );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG)
            APIManager.setIsLog(true);
        setClass(SplashActivity.class);
        APIManager.setAdListner(s -> Log.e("TAG", "onState: "+s ));
    }
}
