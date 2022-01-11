package com.demo.mycommonapp;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.mycommonapp.newpk.MainAAActivity;
import com.pesonal.adsdk.ADS_SplashActivity;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.utils.getDataListner;

import org.json.JSONObject;

import java.util.Arrays;

public class SplashActivity extends ADS_SplashActivity {

    int doubleBackToExitPressedOnce = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ADSinit(SplashActivity.this,getCurrentVersionCode(), new getDataListner() {
            @Override
            public void onSuccess() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        APIManager.getInstance(SplashActivity.this).showSplashAD(SplashActivity.this, () -> {
                            Intent intent = new Intent(SplashActivity.this, StartSecondActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }
                },4000);

            }

            @Override
            public void onUpdate(String url) {

            }

            @Override
            public void onRedirect(String url) {
                showRedirectDialog(url);
            }

            @Override
            public void onReload() {
                startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                finish();
            }

            @Override
            public void onGetExtradata(JSONObject extraData) {
            }
        });

    }


    public void showRedirectDialog(final String url) {

        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.installnewappdialog, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView lable = view.findViewById(R.id.lable);
        LinearLayout layoutLater = view.findViewById(R.id.layoutLater);
        TextView txt_title = view.findViewById(R.id.txt_title);
        layoutLater.setVisibility(View.GONE);
        TextView txt_decription = view.findViewById(R.id.txt_decription);

        lable.setText("Install New App");
        update.setText("Install Now");
        txt_title.setText("Install our new app now and enjoy");
        txt_decription.setText("We have transferred our server, so install our new app by clicking the button below to enjoy the new features of app.");


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (doubleBackToExitPressedOnce == 3) {
                        dialog.dismiss();
                        finish();
                        return false;
                    }
                    doubleBackToExitPressedOnce++;
                    Toast.makeText(SplashActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = 0;
                        }
                    }, 4000);
                }
                return false;
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }



    public int getCurrentVersionCode()
    {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            return  info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }
}