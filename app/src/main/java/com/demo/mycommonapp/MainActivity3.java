package com.demo.mycommonapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.pesonal.adsdk.BaseActivity;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.AdvertisementState;
import com.pesonal.adsdk.remote.NativeCallback;

public class MainActivity3 extends BaseActivity {

    private RelativeLayout adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initView();
    }

    private void initView() {
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        APIManager.getInstance(MainActivity3.this).showNative(adContainer, new NativeCallback() {
            @Override
            public void onLoad(boolean isFail) {

            }

            @Override
            public void onState(AdvertisementState state) {
                Log.e("MainActivity3", "state showNative: "+state );
            }
        });
    }

    @Override
    public void onBackPressed() {
        APIManager.getInstance(MainActivity3.this).showAds(true, state -> finish());
    }
}