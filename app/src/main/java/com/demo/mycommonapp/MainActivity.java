package com.demo.mycommonapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.JsonElement;
import com.pesonal.adsdk.BaseAdsActivity;
import com.pesonal.adsdk.dialog.GiftAds;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.AdvertisementState;
import com.pesonal.adsdk.remote.InterCallback;
import com.pesonal.adsdk.remote.NativeCallback;

public class MainActivity extends BaseAdsActivity implements View.OnClickListener {

    private Button btnNext;
    private Button btnRate;
    private RelativeLayout adContainer;
    private RelativeLayout adContainer1;
    private RelativeLayout adContainer2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_a);
        initView();
    }

    private void initView() {

        btnNext = (Button) findViewById(R.id.btnNext);
        btnRate = (Button) findViewById(R.id.btnRate);
        btnNext.setOnClickListener(this);
        btnRate.setOnClickListener(this);
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        adContainer1 = (RelativeLayout) findViewById(R.id.adContainer1);
        adContainer2 = (RelativeLayout) findViewById(R.id.adContainer2);
        APIManager.getInstance(MainActivity.this).showNative(adContainer, new NativeCallback() {
            @Override
            public void onLoad(boolean isFail) {

            }

            @Override
            public void onState(AdvertisementState state) {
                Log.e("MainActivity", "state showNative: " + state);
            }
        });
        APIManager.getInstance(MainActivity.this).showBanner(adContainer1, new InterCallback() {
            @Override
            public void onClose(AdvertisementState state) {
                Log.e("MainActivity", "state showBanner: " + state);
            }
        });
        APIManager.getInstance(MainActivity.this).showSmallNative(adContainer2, new NativeCallback() {
            @Override
            public void onLoad(boolean isFail) {

            }

            @Override
            public void onState(AdvertisementState state) {
                Log.e("MainActivity", "state showSmallNative: " + state);
            }
        });

        JsonElement extraData = APIManager.getInstance(this).getExtraData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                APIManager.getInstance(MainActivity.this).showAds(false, (state) -> {
                    Log.e("MainActivity", "state:inter " + state);
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                });
                break;
            case R.id.btnRate:
//                 APIManager.getInstance(this).showRatingDialog((feedBack,rate) -> {
//                     Log.e("TAG", "onClick: Back "+ feedBack+"  "+rate);
//                 });

                new GiftAds(this).showGiftAds();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (APIManager.getInstance(this).isExitScreen()) {
            startActivity(new Intent(MainActivity.this, ExitActivity.class));
        } else {
            APIManager.getInstance(MainActivity.this).showExitDialog();
        }

    }
}