package com.demo.mycommonapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.circularreveal.CircularRevealLinearLayout;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;

import com.pesonal.adsdk.model.MOREAPPEXIT;
import com.pesonal.adsdk.remote.APIManager;

import java.util.List;


public class ExitActivity extends AppCompatActivity implements View.OnClickListener {

    CircularRevealCardView yes, no;
    Intent intent;
    CircularRevealLinearLayout exitdialog;
    RecyclerView adRecyler;
    List<MOREAPPEXIT> adsResponces;
    RelativeLayout smallnative;
    RelativeLayout smallnative2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exit);
        exitdialog = findViewById(R.id.bottom_layout);
        yes = findViewById(R.id.card_ok);
        no = findViewById(R.id.card_cancle);
        adRecyler = findViewById(R.id.rvApplist);
        smallnative = findViewById(R.id.id_native);
//        smallnative2 = findViewById(R.id.id_native1);

        APIManager.getInstance(ExitActivity.this).showNative(smallnative);


        if (adsResponces != null) {
            adsResponces.clear();
        }

        adsResponces= APIManager.getInstance(ExitActivity.this).get_EXITMoreAppData();

        if (adsResponces.size() > 0) {
            adRecyler.setVisibility(View.VISIBLE);
            adRecyler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            adRecyler.setAdapter(new AppAdsAdapterExit(ExitActivity.this, adsResponces));
        }


        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_ok:
                finishAffinity();
                break;
            case R.id.card_cancle:
                APIManager.getInstance(ExitActivity.this).showAds(false, () -> {
                    intent = new Intent(ExitActivity.this, StartSecondActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}