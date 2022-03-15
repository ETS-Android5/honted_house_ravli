package com.pesonal.adsdk;


import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pesonal.adsdk.model.ResponseRoot;
import com.pesonal.adsdk.model.vpnmodel.CountryListItem;
import com.pesonal.adsdk.model.vpnmodel.ResponseVpn;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.TinyDB;
import com.pesonal.adsdk.utils.AESSUtils;
import com.pesonal.adsdk.utils.SplashListner;
import com.pesonal.adsdk.utils.getDataListner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class BaseAdsActivity extends BaseActivity {

    public static boolean need_internet = false;
    String bytemode = "";
    boolean is_retry;
    private Runnable runnable;
    private Handler refreshHandler;
    private SharedPreferences mysharedpreferences;
    int doubleBackToExitPressedOnce = 0;
    private Dialog dialog;
    private TextView retry_buttton;
    private getDataListner myCallback1;

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


    public void initializeSplash(Activity activity, SplashListner listner) {
        ADSinit(activity, getCurrentVersionCode(), new getDataListner() {
            @Override
            public void onSuccess() {
                new Handler(Looper.getMainLooper()).postDelayed(listner::onSuccess, 5000);
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
                need_internet = true;
                dialog.dismiss();
                sendRequest(activity, getCurrentVersionCode(), myCallback1);
            }

            @Override
            public void onGetExtradata(JsonObject extraData) {
                if (listner != null)
                    listner.onExtra(extraData);
            }
        });
    }

    private void ADSinit(final Activity activity, final int cversion, final getDataListner myCallback1) {
        dialog = new Dialog(activity);
        dialog.dismiss();
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.retry_layout, null);
        dialog.setContentView(view);
        retry_buttton = view.findViewById(R.id.retry_buttton);

        final SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        final SharedPreferences.Editor editor_AD_PREF = preferences.edit();

        need_internet = preferences.getBoolean("need_internet", need_internet);

        if (!isNetworkAvailable() && need_internet) {
            is_retry = false;
            if (!activity.isFinishing()) {
                dialog.show();
            }
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
                    if (!activity.isFinishing()) {
                        dialog.show();
                    }

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

        sendRequest(activity, getCurrentVersionCode(), myCallback1);


    }

    public void sendRequest(Activity activity, int currentVersionCode, getDataListner myCallback1) {
        this.myCallback1 = myCallback1;
        final SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        final SharedPreferences.Editor editor_AD_PREF = preferences.edit();
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
            new APIManager(BaseAdsActivity.this).init(new getDataListner() {
                @Override
                public void onSuccess() {
                    APIManager.getInstance(BaseAdsActivity.this).loadInterstitialAd();
                    APIManager.getInstance(BaseAdsActivity.this).loadRewardAd();
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
                public void onGetExtradata(JsonObject extraData) {
                }
            }, currentVersionCode);
        }

        String akbsvl679056 = "A7DB10BCE241120B24959FE3F8DF8C2F5AE65D58CB42376D8BC644E3771D93326905B81FB6BB7A8D10FCFCFCA361E8A6";
        try {
            bytemode = AESSUtils.decryptA(activity, akbsvl679056);
            bytemode = bytemode + "v1/get_app.php";

        } catch (Exception e) {
            if (APIManager.isLog)
                Log.e("TAG", "ADSinit: " + e.getMessage());
            e.printStackTrace();
        }


        final String sdfsdf;
        boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (APIManager.isLog)
            Log.e("TAG", "sendRequest:isDebuggable " + isDebuggable);
        if (isDebuggable) {
            sdfsdf = "TRSOFTAG12789I";
        } else {
            sdfsdf = "TRSOFTAG82382I";
        }

        if (APIManager.isLog)
            Log.e("TAG", "sdfsdf: " + sdfsdf);
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest strRequest = new StringRequest(Request.Method.POST, bytemode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        if (APIManager.isLog)
                            Log.e("TAG", "onResponse: " + response1);
                        try {
                            ResponseRoot responseRoot = new Gson().fromJson(response1, ResponseRoot.class);
                            if (responseRoot != null) {
                                if (responseRoot.isSTATUS()) {
                                    String need_in = responseRoot.getAPPSETTINGS().getAppNeedInternet();
                                    if (need_in.endsWith("1")) {
                                        need_internet = true;
                                    } else {
                                        need_internet = false;
                                    }
                                    editor_AD_PREF.putBoolean("need_internet", need_internet).apply();
                                    new TinyDB(BaseAdsActivity.this).putString("response", response1.toString());
                                    downloadVpn(responseRoot.getAPPSETTINGS().getVpnLink(),responseRoot.getAPPSETTINGS().getVpnLocation());
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
                                            if (!activity.isFinishing()) {
                                                dialog.show();
                                            }
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

                        new APIManager(BaseAdsActivity.this).init(new getDataListner() {
                            @Override
                            public void onSuccess() {
                                if (status.equals("true")) {
                                    APIManager.getInstance(BaseAdsActivity.this).loadInterstitialAd();
                                    APIManager.getInstance(BaseAdsActivity.this).loadRewardAd();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            myCallback1.onSuccess();
                                        }
                                    }, 4000);
                                } else {
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
                            public void onGetExtradata(JsonObject extraData) {
                                myCallback1.onGetExtradata(extraData);

                            }
                        }, currentVersionCode);
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
                                        if (!activity.isFinishing()) {
                                            dialog.show();
                                        }
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
                if (APIManager.isLog) {
                    Log.e("TAG", "getParams:getPackageName   " + activity.getPackageName());
                    Log.e("TAG", "getParams:getKeyHash   " + getKeyHash(activity));
                    Log.e("TAG", "getParams:addfdsf123   " + String.valueOf(addfdsf123));
                    Log.e("TAG", "getParams:sdfsdf   " + sdfsdf);
                }
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
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacks(runnable);
    }

    public void showRedirectDialog(final String url) {
        final Dialog dialog = new Dialog(this);
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
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (doubleBackToExitPressedOnce == 3) {
                    dialog1.dismiss();
                    finish();
                    return false;
                }
                doubleBackToExitPressedOnce++;
                Toast.makeText(BaseAdsActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = 0;
                    }
                }, 4000);
            }
            return false;
        });
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


    public int getCurrentVersionCode() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            if (APIManager.isLog)
                Log.e("TAG", "getCurrentVersionCode: " + info.versionCode);
            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (APIManager.isLog)
            Log.e("TAG", "getCurrentVersionCode:... " + 0);
        return 0;
    }


    public void downloadVpn(String url,String vpnlocation) {
        if (APIManager.isLog)
            Log.e("TAG", "progress url json: " + url+"  "+vpnlocation);
        PRDownloader.download(url, getCacheDir().getAbsolutePath(), "vpnList.json")
                .build()
                .setOnStartOrResumeListener(() -> {
                })
                .setOnPauseListener(() -> {
                })
                .setOnCancelListener(() -> {
                })
                .setOnProgressListener(progress -> {
                    if (APIManager.isLog)
                        Log.e("TAG", "progress:list " + (progress.currentBytes * 100 / progress.totalBytes));
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        if (APIManager.isLog)
                            Log.e("TAG", "progress onDownloadComplete: json  " + getCacheDir().getAbsolutePath() + "/vpnList.json");
                        File file = new File(getCacheDir(), "vpnList.json");
                        if (file.exists()) {
                            String jsonFromFile = loadJsonFromFile(file);
                            Gson gson = new GsonBuilder().create();
                            ResponseVpn responseVpnList = gson.fromJson(jsonFromFile, ResponseVpn.class);
                            for (CountryListItem countryListItem : responseVpnList.getCountryList()) {
                                if (countryListItem.getCountryName().equalsIgnoreCase(vpnlocation)) {
                                    int max = countryListItem.getServerList().size() - 1;
                                    int min = 0;
                                    int random = new Random().nextInt(max - min + 1) + min;
                                    Log.e("TAG", "onDownloadComplete:random " + random);
                                    new TinyDB(BaseAdsActivity.this).putString("vpnServer", countryListItem.getServerList().get(random).getCityName());
                                    new TinyDB(BaseAdsActivity.this).putString("vpnServerFlag", countryListItem.getFlagUrl());
                                    downloadConfigFile(countryListItem.getServerList().get(random).getConfig());
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(Error error) {
                        if (APIManager.isLog)
                            Log.e("TAG", "progress onError: error" + error.getServerErrorMessage());
                    }

                });
    }

    public void downloadConfigFile(String url) {
        if (APIManager.isLog)
            Log.e("TAG", "downloadConfigFile: " + url + "  " );
        PRDownloader.download(url, getCacheDir().getAbsolutePath(), "server.ovpn")
                .build()
                .setOnStartOrResumeListener(() -> {

                })
                .setOnPauseListener(() -> {

                })
                .setOnCancelListener(() -> {

                })
                .setOnProgressListener(progress -> {
                    if (APIManager.isLog)
                        Log.e("TAG", "progress: downloadConfigFile  " + (progress.currentBytes *100/ progress.totalBytes));
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        if (APIManager.isLog)
                            Log.e("TAG", "progress onDownloadComplete: downloadConfigFile  ");

                    }

                    @Override
                    public void onError(Error error) {
                        if (APIManager.isLog)
                            Log.e("TAG", "progress onError: downloadConfigFile" + error.getServerErrorMessage());
                    }
                });
    }

    public String loadJsonFromFile(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            return new String(bytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}