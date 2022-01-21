package com.demo.mycommonapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.vpn.TubeVpnActivity;

public class MainActivity extends TubeVpnActivity implements View.OnClickListener {

    private Button btnNext;
    private RelativeLayout adContainer;
    private RelativeLayout adContainer1;
    private RelativeLayout adContainer2;
    private FrameLayout iVPN;
    private ConstraintLayout rootViewGuide;
    private LottieAnimationView guideVpn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_a);

        initView();
    }

    private void initView() {

        iVPN = (FrameLayout) findViewById(R.id.iVPN);
        if (APIManager.getInstance(this).getVpnStatus()) {
            rootViewGuide = (ConstraintLayout) findViewById(R.id.rootViewGuide);
            guideVpn = (LottieAnimationView) findViewById(R.id.guideVpn);
            guideVpn.setOnClickListener(view -> {
                setConnect();
                rootViewGuide.setVisibility(View.GONE);
            });
            if(isItFirstTime()){
                rootViewGuide.setVisibility(View.VISIBLE);
            }else {
                rootViewGuide.setVisibility(View.GONE);
            }
            addView(iVPN);
        }
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        adContainer1 = (RelativeLayout) findViewById(R.id.adContainer1);
        adContainer2 = (RelativeLayout) findViewById(R.id.adContainer2);
        APIManager.getInstance(MainActivity.this).showNative(adContainer);
        APIManager.getInstance(MainActivity.this).showBanner(adContainer1);
        APIManager.getInstance(MainActivity.this).showSmallNative(adContainer2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                APIManager.getInstance(MainActivity.this).showAds(false, () -> {
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                });

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(rootViewGuide.getVisibility()==View.VISIBLE)
            return;
        if (APIManager.getInstance(this).isExitScreen()) {
            APIManager.getInstance(this).showAdsStartExit(this, () -> {
                startActivity(new Intent(MainActivity.this, ExitActivity.class));
                finish();
            });
        } else {
            APIManager.getInstance(MainActivity.this).showExitDialog();
        }

    }
}