package com.demo.mycommonapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pesonal.adsdk.dialog.GiftAds;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.AdvertisementState;
import com.pesonal.adsdk.remote.DialogCallback;
import com.pesonal.adsdk.remote.InterCallback;
import com.pesonal.adsdk.remote.NativeCallback;
import com.pesonal.adsdk.vpn.BannerVpnActivity;
import com.pesonal.adsdk.vpn.CONNECTION_STATE;
import com.pesonal.adsdk.vpn.VanishVPNActivity;

public class MainActivity extends BannerVpnActivity implements View.OnClickListener {

    private Button btnNext;
    private Button btnRate;
    private RelativeLayout adContainer;
    private RelativeLayout adContainer1;
    private RelativeLayout adContainer2;
    private FrameLayout iVPN;
    private CircularRevealRelativeLayout rootViewGuide;
    private FrameLayout layoutGuideVPN;
    private LottieAnimationView guideVpn;
    private Button btnOpenVpnScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_a);
        initView();
    }

    private void initView() {
        btnOpenVpnScreen = (Button) findViewById(R.id.btnOpenVpnScreen);
        if (APIManager.getInstance(this).getVpnMenuStatus()) {
            btnOpenVpnScreen.setVisibility(View.VISIBLE);
        } else btnOpenVpnScreen.setVisibility(View.GONE);

        iVPN = (FrameLayout) findViewById(R.id.iVPN);
        rootViewGuide = (CircularRevealRelativeLayout) findViewById(R.id.rootViewGuide);
        guideVpn = (LottieAnimationView) findViewById(R.id.guideVpn);
        layoutGuideVPN = (FrameLayout) findViewById(R.id.layoutGuideVPN);
        if (APIManager.getInstance(this).getVpnStatus()) {
            setBannerView(iVPN);
            setGuideView(layoutGuideVPN);
            setBackgroundColor(getColor(R.color.colorAdBlack));
            setTextColor(getColor(R.color.colorAdWhite));
            guideVpn.setOnClickListener(view -> {
                connectVpnListener((isConnect, connectionState) -> {
                    if (isConnect == CONNECTION_STATE.CONNECTED) {
                        rootViewGuide.setVisibility(View.GONE);
                    } else if (isConnect == CONNECTION_STATE.CONNECTING) {
                        guideVpn.setVisibility(View.GONE);
                        layoutGuideVPN.setVisibility(View.INVISIBLE);
                    } else if (isConnect == CONNECTION_STATE.FAIL) {
                        rootViewGuide.setVisibility(View.GONE);
                    }else if (isConnect == CONNECTION_STATE.DISCONNECTED) {
                        rootViewGuide.setVisibility(View.GONE);
                    }
                    rootViewGuide.setVisibility(View.GONE);
                });
            });
            if (getConnection()) {
                rootViewGuide.setVisibility(View.GONE);
            } else {
                rootViewGuide.setVisibility(View.VISIBLE);
            }
        }
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
                Log.e("MainActivity", "state showNative: "+state );
            }
        });
        APIManager.getInstance(MainActivity.this).showBanner(adContainer1, new InterCallback() {
            @Override
            public void onClose(AdvertisementState state) {
                Log.e("MainActivity", "state showBanner: "+state );
            }
        });
        APIManager.getInstance(MainActivity.this).showSmallNative(adContainer2, new NativeCallback() {
            @Override
            public void onLoad(boolean isFail) {

            }

            @Override
            public void onState(AdvertisementState state) {
                Log.e("MainActivity", "state showSmallNative: "+state );
            }
        });
        btnOpenVpnScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VanishVPNActivity.class));
            }
        });
        JsonElement extraData = APIManager.getInstance(this).getExtraData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                APIManager.getInstance(MainActivity.this).showAds(false, (state) -> {
                    Log.e("MainActivity", "state:inter "+state );
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
        if (rootViewGuide.getVisibility() == View.VISIBLE)
            return;
        if (APIManager.getInstance(this).isExitScreen()) {
            startActivity(new Intent(MainActivity.this, ExitActivity.class));
        } else {
            APIManager.getInstance(MainActivity.this).showExitDialog();
        }

    }
}