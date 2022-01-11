package com.demo.mycommonapp.newpk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.mycommonapp.R;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.RewardCallback;

public class MainActivity2 extends AppCompatActivity {

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
        APIManager.getInstance(MainActivity2.this).showBanner(adContainer);
        APIManager.getInstance(MainActivity2.this).showBanner(adContainer1);
        APIManager.getInstance(MainActivity2.this).showBanner(adContainer2);
        APIManager.getInstance(MainActivity2.this).showBanner(adContainer3);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIManager.getInstance(MainActivity2.this).showAds( false, () -> {
                    startActivity(new Intent(MainActivity2.this, MainActivity3.class));
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        APIManager.getInstance(MainActivity2.this).showAds(true, this::finish);
    }
}