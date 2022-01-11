package com.pesonal.adsdk.dialog;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.pesonal.adsdk.remote.APIManager;

import java.util.Random;


public class GiftAds {
    Activity context;
    private Random randomGenerator;
    private int index;


    public GiftAds(Activity context) {
        this.context = context;
    }

    public void showGiftAds() {
        if (isNetworkAvailable(context)) {
            randomGenerator = new Random();
            index = randomGenerator.nextInt(APIManager.getInstance(context).get_SPLASHMoreAppData().size());
            AppADSDialog.show(context, index);
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}