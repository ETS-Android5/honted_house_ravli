package com.pesonal.adsdk;


import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.TinyDB;
import com.pesonal.adsdk.model.ResponseRoot;
import com.pesonal.adsdk.utils.AESSUtils;
import com.pesonal.adsdk.utils.getDataListner;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ADS_SplashActivity extends AppCompatActivity {

    public static boolean need_internet = false;
    String bytemode = "";
    boolean is_retry;
    private Runnable runnable;
    private Handler refreshHandler;
    private SharedPreferences mysharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void ADSinit(final Activity activity, final int cversion, final getDataListner myCallback1) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.retry_layout, null);
        dialog.setContentView(view);
        final TextView retry_buttton = view.findViewById(R.id.retry_buttton);

        final SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        final SharedPreferences.Editor editor_AD_PREF = preferences.edit();

        need_internet = preferences.getBoolean("need_internet", need_internet);

        if (!isNetworkAvailable() && need_internet) {
            is_retry = false;
            dialog.show();
        }

        mysharedpreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);

        dialog.dismiss();
        refreshHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {
                    is_retry = true;
                    retry_buttton.setText(activity.getString(R.string.retry));
                } else if (need_internet) {
                    dialog.show();
                    is_retry = false;
                    retry_buttton.setText(activity.getString(R.string.connect_internet));
                }
                refreshHandler.postDelayed(this, 1000);
            }
        };

        refreshHandler.postDelayed(runnable, 1000);

        retry_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_retry) {
                    if (need_internet) {
                        myCallback1.onReload();
                    } else {
                        myCallback1.onSuccess();
                    }
                } else {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            }
        });


        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String currentDate = df.format(calender.getTime());


        final int addfdsf123;
        String status = mysharedpreferences.getString("firsttime", "true");
        final SharedPreferences.Editor editor = mysharedpreferences.edit();
        if (status.equals("true")) {
            editor.putString("date", currentDate).apply();
            editor.putString("firsttime", "false").apply();
            addfdsf123 = 13421;
        } else {
            String date = mysharedpreferences.getString("date", "");
            if (!currentDate.equals(date)) {
                editor.putString("date", currentDate).apply();
                addfdsf123 = 26894;
            } else {
                addfdsf123 = 87332;
            }
            new APIManager(ADS_SplashActivity.this).init(new getDataListner() {
                @Override
                public void onSuccess() {
                    APIManager.getInstance(ADS_SplashActivity.this).loadInterstitialAd();
                    APIManager.getInstance(ADS_SplashActivity.this).loadRewardAd();
                }

                @Override
                public void onUpdate(String url) {
                }

                @Override
                public void onRedirect(String url) {
                }

                @Override
                public void onReload() {
                }

                @Override
                public void onGetExtradata(JSONObject extraData) {
                }
            }, cversion);
        }

        String akbsvl679056 = "A7DB10BCE241120B24959FE3F8DF8C2F5AE65D58CB42376D8BC644E3771D93326905B81FB6BB7A8D10FCFCFCA361E8A6";
        try {
            bytemode = AESSUtils.decryptA(activity, akbsvl679056);
            bytemode = bytemode + "v1/get_app.php";

        } catch (Exception e) {
            Log.e("TAG", "ADSinit: " + e.getMessage());
            e.printStackTrace();
        }


        final String sdfsdf;
        if (BuildConfig.DEBUG) {
            sdfsdf = "TRSOFTAG12789I";
        } else {
            sdfsdf = "TRSOFTAG82382I";
        }


        Log.e("TAG", "sdfsdf: " + sdfsdf);
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest strRequest = new StringRequest(Request.Method.POST, bytemode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        Log.e("TAG", "onResponse: " + response1);
                        try {
                            ResponseRoot responseRoot = new Gson().fromJson(response1, ResponseRoot.class);
                            if(responseRoot!=null){
                                if (responseRoot.isSTATUS()) {
                                    String need_in = responseRoot.getAPPSETTINGS().getAppNeedInternet();
                                    if (need_in.endsWith("1")) {
                                        need_internet = true;
                                    } else {
                                        need_internet = false;
                                    }
                                    editor_AD_PREF.putBoolean("need_internet", need_internet).apply();
                                    new TinyDB(ADS_SplashActivity.this).putString("response", response1.toString());
                                }
                            }
                        } catch (Exception e) {
                            if (need_internet) {
                                dialog.dismiss();
                                refreshHandler = new Handler();
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isNetworkAvailable()) {
                                            is_retry = true;
                                            retry_buttton.setText(activity.getString(R.string.retry));
                                        } else {
                                            dialog.show();
                                            is_retry = false;
                                            retry_buttton.setText(activity.getString(R.string.connect_internet));
                                        }
                                        refreshHandler.postDelayed(this, 1000);
                                    }
                                };
                            } else {
                                myCallback1.onSuccess();
                            }
                        }

                        new APIManager(ADS_SplashActivity.this).init(new getDataListner() {
                            @Override
                            public void onSuccess() {
                                if (status.equals("true")) {
                                    APIManager.getInstance(ADS_SplashActivity.this).loadInterstitialAd();
                                    APIManager.getInstance(ADS_SplashActivity.this).loadRewardAd();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            myCallback1.onSuccess();
                                        }
                                    },4000);
                                }else {
                                    myCallback1.onSuccess();
                                }
                            }

                            @Override
                            public void onUpdate(String url) {
                                myCallback1.onUpdate(url);
                            }

                            @Override
                            public void onRedirect(String url) {
                                myCallback1.onRedirect(url);
                            }

                            @Override
                            public void onReload() {
                            }

                            @Override
                            public void onGetExtradata(JSONObject extraData) {
                                myCallback1.onGetExtradata(extraData);

                            }
                        }, cversion);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (need_internet) {
                            dialog.dismiss();
                            refreshHandler = new Handler();
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (isNetworkAvailable()) {
                                        is_retry = true;
                                        retry_buttton.setText(activity.getString(R.string.retry));
                                    } else {
                                        dialog.show();
                                        is_retry = false;
                                        retry_buttton.setText(activity.getString(R.string.connect_internet));
                                    }
                                    refreshHandler.postDelayed(this, 1000);
                                }
                            };
                        } else {
                            myCallback1.onReload();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PHSUGSG6783019KG", activity.getPackageName());
                params.put("AFHJNTGDGD563200K", getKeyHash(activity));
                params.put("DTNHGNH7843DFGHBSA", String.valueOf(addfdsf123));
                params.put("DBMNBXRY4500991G", sdfsdf);
                return params;
            }

        };

        strRequest.setShouldCache(false);
        requestQueue.add(strRequest);

    }

    private String getKeyHash(Activity activity) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = (Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                return something.replace("+", "*");
            }
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

        } catch (Exception e) {

        }
        return null;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacks(runnable);
    }
}