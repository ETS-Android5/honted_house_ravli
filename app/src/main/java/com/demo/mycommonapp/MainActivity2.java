package com.demo.mycommonapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.pesonal.adsdk.BaseActivity;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.AdvertisementState;
import com.pesonal.adsdk.remote.InterCallback;
import com.pesonal.adsdk.remote.NativeCallback;

public class MainActivity2 extends BaseActivity {

    private RelativeLayout adContainer;
    private Button btnNext;
    private RelativeLayout adContainer1;
    private RelativeLayout adContainer2;
    private RelativeLayout adContainer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
    }

    private void initView() {
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        adContainer1 = (RelativeLayout) findViewById(R.id.adContainer1);
        adContainer2 = (RelativeLayout) findViewById(R.id.adContainer2);
        adContainer3 = (RelativeLayout) findViewById(R.id.adContainer3);
        APIManager.getInstance(MainActivity2.this).showBanner(adContainer, new InterCallback() {
            @Override
            public void onClose(AdvertisementState state) {
                Log.e("MainActivity2", "state showBanner: "+state );
            }
        });
        APIManager.getInstance(MainActivity2.this).showSmallNative(adContainer2, new NativeCallback() {
            @Override
            public void onLoad(boolean isFail) {

            }

            @Override
            public void onState(AdvertisementState state) {
                Log.e("MainActivity2", "state showSmallNative: "+state );
            }
        });
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIManager.getInstance(MainActivity2.this).showAds( false, (state) -> {
                    Log.e("MainActivity2", "state:inter "+state );
                    startActivity(new Intent(MainActivity2.this, MainActivity3.class));
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        APIManager.getInstance(MainActivity2.this).showAds(true, state -> finish());
    }
}