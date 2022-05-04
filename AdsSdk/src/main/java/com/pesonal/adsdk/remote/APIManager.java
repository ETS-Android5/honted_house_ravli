package com.pesonal.adsdk.remote;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.video.VideoSize;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marozzi.shimmerview.ShimmerView;
import com.pesonal.adsdk.AppOpenManager;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.customAd.CustomAppOpenAds;
import com.pesonal.adsdk.customAd.CustomIntAds;
import com.pesonal.adsdk.dialog.RatingDialog;
import com.pesonal.adsdk.model.AdvertiseList;
import com.pesonal.adsdk.model.MOREAPPEXIT;
import com.pesonal.adsdk.model.MOREAPPSPLASH;
import com.pesonal.adsdk.model.ResponseRoot;
import com.pesonal.adsdk.model.promo.AppItemPromo;
import com.pesonal.adsdk.model.promo.ResponsePromo;
import com.pesonal.adsdk.model.vpnmodel.CountryListItem;
import com.pesonal.adsdk.model.vpnmodel.ResponseVpn;
import com.pesonal.adsdk.qureka.BannerUtils;
import com.pesonal.adsdk.qureka.CustomiseinterActivity;
import com.pesonal.adsdk.qureka.Glob;
import com.pesonal.adsdk.qureka.Nativeutils;
import com.pesonal.adsdk.utils.Inflate_ADS;
import com.pesonal.adsdk.utils.getDataListner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class APIManager {


    public static boolean isLog = false;
    public static boolean AD_VISIBLE = true;
    public String ADMOB = "Admob";
    public static String QUREKALINK = "Admob";
    public static String[] ADMOB_B = new String[]{};
    public static String[] ADMOB_I = new String[]{};
    public static String[] ADMOB_N = new String[]{};
    public static String[] ADMOB_R = new String[]{};
    public static String[] ADMOB_O = new String[]{};
    public static int whichShowReward = 0;
    public static int whichShowNative = 0;
    public static int whichShowInter = 0;
    public static int whichShowBanner = 0;
    public static int whichShowOpenAd = 0;
    private static Activity activity;
    private static APIManager mInstance;
    private static ResponseRoot responseRoot;
    public static int ad_dialog_time_in_second = 2;

    private static InterstitialAd mInterstitialAd;
    private static boolean showCustom = false;
    private InterCallback interCallback;
    private Dialog dialog;

    public static List<AdvertiseList> myAppMarketingList = new ArrayList<>();
    public static int count_custBannerAd = 0;
    public static int count_custNBAd = 0;
    public static int count_custNativeAd = 0;
    public static int count_custIntAd = 0;
    public static int count_custAppOpenAd = 0;

    public static int adStatus = 2;
    public static int adInterVal = 0;
    public static boolean aBoolean = false;
    public static AppOpenManager manager;
    private static RewardedAd rewardedAd;
    private RewardCallback rewardCallback;
    private static boolean showCustomReward = false;
    final boolean[] booleans = new boolean[]{false};
    public static AnalyticsCallback callbackForAnalytics;

    public static boolean sequenceQureka = false;

    public APIManager(Activity activity) {
        APIManager.activity = activity;
    }

    public static void setIsLog(boolean isLog) {
        APIManager.isLog = isLog;
    }

    public static APIManager getInstance(Activity activity) {
        APIManager.activity = activity;
        if (mInstance == null) {
            mInstance = new APIManager(activity);
        }
        return mInstance;
    }

    public static void setAdListner(AnalyticsCallback interCallback) {
        callbackForAnalytics = interCallback;
    }

    public boolean getQureka() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getQUREKA() == null)
            return false;
        return responseRoot.getAPPSETTINGS().getQUREKA().equals("ON");
    }

    public boolean getVpnStatus() {
        if (!setResponseRoot())
            return false;
        if (isLog)
            Log.e(TAG, "getVpnStatus: " + responseRoot.getAPPSETTINGS().getVpnStatus().equals("ON"));
        if (responseRoot.getAPPSETTINGS().getVpnStatus() == null)
            return false;
        return responseRoot.getAPPSETTINGS().getVpnStatus().equals("ON");
    }

    public String getVpnLocation() {
        if (!setResponseRoot())
            return "US";
        if (responseRoot.getAPPSETTINGS().getVpnLocation() == null)
            return "US";
        return responseRoot.getAPPSETTINGS().getVpnLocation();
    }

    public String getVpnUser() {
        if (!setResponseRoot())
            return "US";
        if (responseRoot.getAPPSETTINGS().getVpnUser() == null)
            return "US";
        return responseRoot.getAPPSETTINGS().getVpnUser();
    }

    public String getVpnPass() {
        if (!setResponseRoot())
            return "US";
        if (responseRoot.getAPPSETTINGS().getVpnPass() == null)
            return "US";
        return responseRoot.getAPPSETTINGS().getVpnPass();
    }


    public boolean getVpnMenuStatus() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getVPNMENU() == null)
            return true;
        return responseRoot.getAPPSETTINGS().getVPNMENU().equalsIgnoreCase("ON");
    }

    public String getVpnServer() {
        if (!setResponseRoot())
            return "";
        File file = new File(activity.getCacheDir(), "server.ovpn");
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return "";
    }

    public ResponseRoot getResponseRoot() {
        return responseRoot;
    }

    public void setAdStatus(String status) {
        if (responseRoot != null) {
            if (responseRoot.getAPPSETTINGS() != null)
                responseRoot.getAPPSETTINGS().setAppAdShowStatus(status);
        }
    }

    public static HashMap<String, Object> getAllAppSettingsData(Context context) {
        String response = new TinyDB(context).getString("response");
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class).getAsJsonObject("APP_SETTINGS");
        HashMap<String, Object> yourHashMap = new Gson().fromJson(jsonObject.toString(), HashMap.class);
        return yourHashMap;
    }

    public List<CountryListItem> getVpnServerList() {
        if (!setResponseRoot())
            return new ArrayList<>();
        File file = new File(activity.getCacheDir(), "vpnList.json");
        if (file.exists()) {
            String jsonFromFile = loadJsonFromFile(file);
            Gson gson = new GsonBuilder().create();
            ResponseVpn responseVpnList = gson.fromJson(jsonFromFile, ResponseVpn.class);
            return responseVpnList.getCountryList();
        }
        return new ArrayList<>();
    }

    public boolean isExitScreen() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getExitScreen() == null)
            return false;
        return responseRoot.getAPPSETTINGS().getExitScreen().equalsIgnoreCase("ON");
    }

    public boolean getScreenStatus() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getStartScreen() == null)
            return true;
        return responseRoot.getAPPSETTINGS().getStartScreen().equalsIgnoreCase("ON");
    }

    public boolean getBottomAd() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getBottomAd() == null)
            return true;
        return responseRoot.getAPPSETTINGS().getBottomAd().equalsIgnoreCase("ON");
    }

    public boolean getFirstTimeAd() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getFirstTimeAd() == null)
            return false;
        if (responseRoot.getAPPSETTINGS().getFirstTimeAd().equalsIgnoreCase("OFF")) {
            if (!new TinyDB(activity).getBoolean("appIsFirstAd")) {
                new TinyDB(activity).putBoolean("appIsFirstAd", true);
                return true;
            } else {
                return false;
            }
        } else return false;
    }

    public boolean getRatingDialog() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getRatingDialog() == null)
            return false;
        return responseRoot.getAPPSETTINGS().getRatingDialog().equalsIgnoreCase("ON");
    }

    public boolean getInterAdStatus() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getInterAD() == null)
            return true;
        return responseRoot.getAPPSETTINGS().getInterAD().equalsIgnoreCase("ON");
    }

    public boolean getPromoAdStatus() {
        if (!setResponseRoot())
            return false;
        if (responseRoot.getAPPSETTINGS().getPromoAD() == null)
            return false;
        return responseRoot.getAPPSETTINGS().getPromoAD().equalsIgnoreCase("ON");
    }

    public String getPromoAdJson() {
        if (!setResponseRoot())
            return "";
        if (responseRoot.getAPPSETTINGS().getPromoADJson() == null)
            return "";
        return responseRoot.getAPPSETTINGS().getPromoADJson();
    }


    public static int getApp_adShowStatus() {
        try {
            return Integer.parseInt(responseRoot.getAPPSETTINGS().getAppAdShowStatus());
        } catch (Exception e) {
            return 1;
        }
    }

    public JsonElement getExtraData() {
        if (!setResponseRoot())
            return null;
        if (responseRoot != null) {
            return responseRoot.getEXTRADATA();
        } else return null;
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

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public boolean setResponseRoot() {
        if (responseRoot == null) {
            if (activity != null && !activity.isFinishing()) {
                init(false, null, -1);
                if (responseRoot == null)
                    return false;
                else
                    return true;
            }
        }
        return true;
    }

    public void init(boolean isFirstAD, getDataListner listner, int cversion) {
        if (cversion != -1)
            new TinyDB(activity).putBoolean("isUpdateCall", false);
        String response = new TinyDB(activity).getString("response");
        responseRoot = new Gson().fromJson(response, ResponseRoot.class);
        if (responseRoot == null)
            return;
        if (!responseRoot.isSTATUS())
            return;

        new TinyDB(activity).putString("app_name", responseRoot.getAPPSETTINGS().getAppName());
        new TinyDB(activity).putString("app_logo", responseRoot.getAPPSETTINGS().getAppLogo());
        QUREKALINK = responseRoot.getAPPSETTINGS().getQUREKALINK();

        if (isFirstAD) {
            AD_VISIBLE = !getFirstTimeAd();
            if (APIManager.isLog)
                Log.e(TAG, "init:getFirstTimeAd " + AD_VISIBLE);
        }

        String app_howShowAdInterstitial = responseRoot.getAPPSETTINGS().getAppHowShowAdInterstitial();

        List<String> platformArrayList = new ArrayList<>();
        String app_adPlatformSequenceInterstitial = responseRoot.getAPPSETTINGS().getAppAdPlatformSequenceInterstitial();
        String[] adSequence = app_adPlatformSequenceInterstitial.split(",");


        String app_alernateAdShowInterstitial = responseRoot.getAPPSETTINGS().getAppAlernateAdShowInterstitial();
        String[] alernateAd = app_alernateAdShowInterstitial.split(",");

        if (app_howShowAdInterstitial.equals("0")) {
            List<String> app_adPlatformSequence = new ArrayList<>(Arrays.asList(adSequence));
            for (int i = 0; i < app_adPlatformSequence.size(); i++) {
                if (app_adPlatformSequence.get(i).contains(ADMOB)) {
                    platformArrayList.add(app_adPlatformSequence.get(i));
                } else if (app_adPlatformSequence.get(i).contains("Qureka")) {
                    sequenceQureka = true;
                }
            }
            ADMOB_B = new String[platformArrayList.size()];
            ADMOB_I = new String[platformArrayList.size()];
            ADMOB_N = new String[platformArrayList.size()];
            ADMOB_R = new String[platformArrayList.size()];
            ADMOB_O = new String[platformArrayList.size()];
            for (int i = 0; i < platformArrayList.size(); i++) {
                ADMOB_B[i] = platformArrayList.get(i);
                ADMOB_I[i] = platformArrayList.get(i);
                ADMOB_N[i] = platformArrayList.get(i);
                ADMOB_R[i] = platformArrayList.get(i);
                ADMOB_O[i] = platformArrayList.get(i);
            }
            whichShowReward = 0;
            whichShowNative = 0;
            whichShowInter = 0;
            whichShowBanner = 0;
            whichShowOpenAd = 0;
        } else if (app_howShowAdInterstitial.equals("1") && alernateAd.length != 0) {
            for (int i = 0; i <= 10; i++) {
                int min = 0;
                int max = alernateAd.length;
                int random = new Random().nextInt((max - min) + 1) + min;
                if (random < alernateAd.length)
                    if (alernateAd[random].contains(ADMOB)) {
                        platformArrayList.add(alernateAd[random]);
                    }
            }
            for (String s : adSequence) {
                if (platformArrayList.size() != 0) {
                    if (!platformArrayList.get(0).equals(s)) {
                        if (s.contains(ADMOB)) {
                            platformArrayList.add(s);
                        } else if (s.contains("Qureka")) {
                            sequenceQureka = true;
                        }
                    }
                }
            }
            ADMOB_B = new String[platformArrayList.size()];
            ADMOB_I = new String[platformArrayList.size()];
            ADMOB_N = new String[platformArrayList.size()];
            ADMOB_R = new String[platformArrayList.size()];
            ADMOB_O = new String[platformArrayList.size()];
            for (int i = 0; i < platformArrayList.size(); i++) {
                ADMOB_B[i] = platformArrayList.get(i);
                ADMOB_I[i] = platformArrayList.get(i);
                ADMOB_N[i] = platformArrayList.get(i);
                ADMOB_R[i] = platformArrayList.get(i);
                ADMOB_O[i] = platformArrayList.get(i);
            }
            whichShowReward = 0;
            whichShowNative = 0;
            whichShowInter = 0;
            whichShowBanner = 0;
            whichShowOpenAd = 0;
        }
        if (responseRoot.getAPPSETTINGS().getAppRedirectOtherAppStatus().equals("1")) {
            String redirectNewPackage = responseRoot.getAPPSETTINGS().getAppNewPackageName();
            if (listner != null)
                listner.onRedirect(redirectNewPackage);
        } else {
            if (cversion != -1)
                if (responseRoot.getAPPSETTINGS().getAppUpdateAppDialogStatus().equals("1") && checkUpdate(cversion)) {
                    new TinyDB(activity).putBoolean("isUpdateCall", true);
                }
            if (listner != null)
                listner.onSuccess();
            if (responseRoot.getEXTRADATA() != null) {
                if (listner != null)
                    listner.onGetExtradata(responseRoot.getEXTRADATA());
            }

            RequestConfiguration conf = new RequestConfiguration.Builder().setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
            MobileAds.setRequestConfiguration(conf);

            MobileAds.initialize(activity, initializationStatus -> {
            });

            if (ADMOB_O.length > 0) {
                loadAppOpenAd(activity, new AppOpenManager.splshADlistner() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
            }

        }
    }

    private boolean checkUpdate(int cversion) {
        if (responseRoot.getAPPSETTINGS().getAppUpdateAppDialogStatus().equals("1")) {
            String versions = responseRoot.getAPPSETTINGS().getAppVersionCode();
            try {
                return Integer.parseInt(versions) > cversion;
            } catch (Exception e) {
            }
        }
        return false;
    }

    public String getPlatFormName(String whichOne) {
        String returnPlatForm = "";

        switch (whichOne) {
            case "I":
                if (ADMOB_I.length > 0)
                    if (whichShowInter != ADMOB_I.length - 1) {
                        returnPlatForm = ADMOB_I[whichShowInter];
                        whichShowInter++;
                    } else {
                        returnPlatForm = ADMOB_I[whichShowInter];
                        whichShowInter = 0;
                    }
                break;
            case "BN":
            case "N":
                if (ADMOB_N.length > 0)
                    if (whichShowNative != ADMOB_N.length - 1) {
                        returnPlatForm = ADMOB_N[whichShowNative];
                        whichShowNative++;
                    } else {
                        returnPlatForm = ADMOB_N[whichShowNative];
                        whichShowNative = 0;
                    }
                break;
            case "B":
                if (ADMOB_B.length > 0)
                    if (whichShowBanner != ADMOB_B.length - 1) {
                        returnPlatForm = ADMOB_B[whichShowBanner];
                        whichShowBanner++;
                    } else {
                        returnPlatForm = ADMOB_B[whichShowBanner];
                        whichShowBanner = 0;
                    }
                break;
            case "R":
                if (ADMOB_R.length > 0)
                    if (whichShowReward != ADMOB_R.length - 1) {
                        returnPlatForm = ADMOB_R[whichShowReward];
                        whichShowReward++;
                    } else {
                        returnPlatForm = ADMOB_R[whichShowReward];
                        whichShowReward = 0;
                    }

                break;
            case "O":
                if (ADMOB_O.length > 0) {
                    if (whichShowOpenAd != ADMOB_O.length - 1) {
                        returnPlatForm = ADMOB_O[whichShowOpenAd];
                        whichShowOpenAd++;
                    } else {
                        returnPlatForm = ADMOB_O[whichShowOpenAd];
                        whichShowOpenAd = 0;
                    }
                }
                break;
        }

        return returnPlatForm;
    }

    public String getUnitID(String platform, String whichAd, String whichOneInter) {
        String returnId = "";
        switch (platform) {
            case "Admob1":
                if (responseRoot.getPLACEMENT().getAdmob1() != null && !responseRoot.getPLACEMENT().getAdmob1().getAdShowAdStatus().equals("0")) {
                    switch (whichAd) {
                        case "I":
                            if (responseRoot.getPLACEMENT().getAdmob1().getAdLoadAdIdsType().equals("2")) {
                                returnId = getUnitIDForCPM(getHigheCPMAdId(whichOneInter));
                            } else {
                                returnId = responseRoot.getPLACEMENT().getAdmob1().getInterstitial1();
                            }
                            break;
                        case "B":
                            returnId = responseRoot.getPLACEMENT().getAdmob1().getBanner1();
                            break;
                        case "BN":
                        case "N":
                            returnId = responseRoot.getPLACEMENT().getAdmob1().getNative1();
                            break;
                        case "R":
                            returnId = responseRoot.getPLACEMENT().getAdmob1().getRewardedVideo1();
                            break;
                        case "O":
                            returnId = responseRoot.getPLACEMENT().getAdmob1().getAppOpen1();
                            break;
                    }
                }

                break;
            case "Admob2":
                if (responseRoot.getPLACEMENT().getAdmob2() != null && !responseRoot.getPLACEMENT().getAdmob2().getAdShowAdStatus().equals("0")) {
                    switch (whichAd) {
                        case "I":
                            if (responseRoot.getPLACEMENT().getAdmob2().getAdLoadAdIdsType().equals("2")) {
                                returnId = getUnitIDForCPM(getHigheCPMAdId(whichOneInter));
                            } else {
                                returnId = responseRoot.getPLACEMENT().getAdmob2().getInterstitial1();
                            }
                            break;
                        case "B":
                            returnId = responseRoot.getPLACEMENT().getAdmob2().getBanner1();
                            break;
                        case "BN":
                        case "N":
                            returnId = responseRoot.getPLACEMENT().getAdmob2().getNative1();
                            break;
                        case "R":
                            returnId = responseRoot.getPLACEMENT().getAdmob2().getRewardedVideo1();
                            break;
                        case "O":
                            returnId = responseRoot.getPLACEMENT().getAdmob2().getAppOpen1();
                            break;
                    }
                }

                break;
            case "Admob3":
                if (responseRoot.getPLACEMENT().getAdmob3() != null && !responseRoot.getPLACEMENT().getAdmob3().getAdShowAdStatus().equals("0")) {
                    switch (whichAd) {
                        case "I":
                            if (responseRoot.getPLACEMENT().getAdmob3().getAdLoadAdIdsType().equals("2")) {
                                returnId = getUnitIDForCPM(getHigheCPMAdId(whichOneInter));
                            } else {
                                returnId = responseRoot.getPLACEMENT().getAdmob3().getInterstitial1();
                            }
                            break;
                        case "B":
                            returnId = responseRoot.getPLACEMENT().getAdmob3().getBanner1();
                            break;
                        case "BN":
                        case "N":
                            returnId = responseRoot.getPLACEMENT().getAdmob3().getNative1();
                            break;
                        case "R":
                            returnId = responseRoot.getPLACEMENT().getAdmob3().getRewardedVideo1();
                            break;
                        case "O":
                            returnId = responseRoot.getPLACEMENT().getAdmob3().getAppOpen1();
                            break;
                    }
                }

                break;
        }

        return returnId;
    }

    public String getUnitIDForCPM(String platform) {
        String returnId = "";
        switch (platform) {
            case "Admob1":
                if (responseRoot.getPLACEMENT().getAdmob1() != null && !responseRoot.getPLACEMENT().getAdmob1().getAdShowAdStatus().equals("0")) {
                    returnId = responseRoot.getPLACEMENT().getAdmob1().getInterstitial1();
                }
                break;
            case "Admob2":
                if (responseRoot.getPLACEMENT().getAdmob2() != null && !responseRoot.getPLACEMENT().getAdmob2().getAdShowAdStatus().equals("0")) {
                    returnId = responseRoot.getPLACEMENT().getAdmob2().getInterstitial1();
                }
                break;
            case "Admob3":
                if (responseRoot.getPLACEMENT().getAdmob3() != null && !responseRoot.getPLACEMENT().getAdmob3().getAdShowAdStatus().equals("0")) {
                    returnId = responseRoot.getPLACEMENT().getAdmob3().getInterstitial1();
                }
                break;
        }
        return returnId;
    }

    private String getHigheCPMAdId(String whichOne) {
        String adId = "";

        if (whichOne.equals("First")) {
            adId = ADMOB_I[0];
            String ADMOB_I_list = implode(ADMOB_I);
            new TinyDB(activity).putString("ADMOB_I", ADMOB_I_list);
        } else if (whichOne.equals("Next")) {
            String ADMOB_I_list = new TinyDB(activity).getString("ADMOB_I");
            if (!ADMOB_I_list.isEmpty()) {
                String[] intA_list = ADMOB_I_list.split(",");
                adId = intA_list[0];
                ADMOB_I_list = implode(intA_list);
                new TinyDB(activity).putString("ADMOB_I", ADMOB_I_list);
            } else {
                adId = ADMOB_I[0];
            }
        }
        return adId;
    }

    private String implode(String[] placementList) {
        String str_ads = "";
        for (int i = 1; i < placementList.length; i++) {
            if (!placementList[i].equals("")) {
                if (str_ads.equals("")) {
                    str_ads = placementList[i];
                } else {
                    str_ads = str_ads + "," + placementList[i];
                }
            }
        }
        return str_ads;
    }

    public void loadInterstitialAd() {
        requestInterstitial("First");
    }

    public void requestInterstitial(String whichOne) {
        if (!setResponseRoot())
            return;
        if (responseRoot.getAPPSETTINGS() == null) {
            return;
        }
        String platform = getPlatFormName("I");
        String adUnitId = getUnitID(platform, "I", whichOne);
        if (isLog)
            Log.e("TAG", "RequestInterstitial: " + adUnitId + "  " + platform);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                showCustom = false;
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mInterstitialAd = null;
                        requestInterstitial("Next");
                        interstitialCallBack(AdvertisementState.INTER_AD_CLOSE);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        mInterstitialAd = null;
                        requestInterstitial("Next");
                        interstitialCallBack(AdvertisementState.INTER_AD_FAILED_TO_SHOW);
                        Log.d("TAG", "The ad failed to show.");
                        if (callbackForAnalytics != null) {
                            callbackForAnalytics.onState("Inter Show  " + adError.getCode() + " : " + adError.getMessage());
                        }
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
                showCustom = true;
                if (callbackForAnalytics != null) {
                    callbackForAnalytics.onState("Inter Load  " + loadAdError.getCode() + " : " + loadAdError.getMessage());
                }
            }
        });
    }

    public void showInterstitial(InterCallback callback) {
        this.interCallback = callback;
        if (!setResponseRoot()) {
            if (interCallback != null)
                interCallback.onClose(AdvertisementState.INTER_AD_RESPONSE_NULL);
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (interCallback != null)
                interCallback.onClose(AdvertisementState.INTER_AD_APPSETTING_NULL);
            return;
        }
        if (APIManager.getApp_adShowStatus() == 0) {
            if (interCallback != null)
                interCallback.onClose(AdvertisementState.AD_STATUS_FALSE);
            return;
        }
        if (!getInterAdStatus()) {
            if (interCallback != null)
                interCallback.onClose(AdvertisementState.INTER_AD_STATUS_FALSE);
            return;
        }

        if (!getQureka()) {
            dialog = new Dialog(activity);
            View view = LayoutInflater.from(activity).inflate(R.layout.ad_progress_dialog, null);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (showCustom || mInterstitialAd == null) {
                if (sequenceQureka) {
                    CustomiseinterActivity.H(activity, () -> {
                        loadInter();
                        interstitialCallBack(AdvertisementState.QUREKA_INTER_AD_CLOSE);
                    }, Glob.dataset(activity));
                } else {
                    showCustomInter(activity);
                }
            } else {
                if (responseRoot.getAPPSETTINGS().getAppDialogBeforeAdShow().equals("1")) {
                    if (!activity.isFinishing())
                        dialog.show();
                    new CountDownTimer(ad_dialog_time_in_second * 1000L, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(activity);
                                if (!activity.isFinishing() && dialog != null && dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }
                    }.start();
                } else {
                    mInterstitialAd.show(activity);
                    dialog.dismiss();
                }
            }
        } else {
            CustomiseinterActivity.H(activity, () -> interstitialCallBack(AdvertisementState.QUREKA_INTER_AD_CLOSE), Glob.dataset(activity));
        }

    }

    void interstitialCallBack(AdvertisementState state) {
        if (interCallback != null) {
            interCallback.onClose(state);
            interCallback = null;
        }
    }

    private void showCustomInter(final Activity activity) {
        if (!activity.isFinishing())
            dialog.show();
        AdvertiseList advertiseList = getMyCustomAd("Interstitial");
        if (advertiseList != null) {
            try {
                final CustomIntAds customIntAds = new CustomIntAds(activity, advertiseList);
                customIntAds.setCanceledOnTouchOutside(false);
                customIntAds.setCancelable(false);
                customIntAds.setOnCloseListener(new CustomIntAds.OnCloseListener() {
                    public void onAdsCloseClick() {
                        customIntAds.dismiss();
                        loadInter();
                        interstitialCallBack(AdvertisementState.CUSTOM_INTER_AD_CLOSE);
                    }

                    public void setOnKeyListener() {
                        customIntAds.dismiss();
                        loadInter();
                        interstitialCallBack(AdvertisementState.CUSTOM_INTER_AD_CLOSE);
                    }
                });
                if (!activity.isFinishing()) {
                    dialog.dismiss();
                    customIntAds.show();
                }
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                loadInter();
                interstitialCallBack(AdvertisementState.CUSTOM_INTER_AD_FAIL);
            }
        } else {
            dialog.dismiss();
            loadInter();
            interstitialCallBack(AdvertisementState.CUSTOM_INTER_AD_FAIL_FROM_RESPONSE);
        }
    }

    private void loadInter() {
        showCustom = false;
        requestInterstitial("Next");
    }

    public List<AdvertiseList> getAdvertiseLists() {
        return responseRoot.getAdvertiseList();
    }

    public AdvertiseList getMyCustomAd(String adFormat) {
        if (myAppMarketingList.size() == 0) {
            myAppMarketingList = getAdvertiseLists();
        }
        List<AdvertiseList> adFormatWiseAd = new ArrayList<>();
        if (myAppMarketingList.size() != 0) {
            for (int i = 0; i < myAppMarketingList.size(); i++) {
                if (!myAppMarketingList.get(i).getApp_AdFormat().isEmpty()) {
                    String[] adFormat_list = myAppMarketingList.get(i).getApp_AdFormat().split(",");
                    if (Arrays.asList(adFormat_list).contains(adFormat)) {
                        adFormatWiseAd.add(myAppMarketingList.get(i));
                    }
                }
            }
        }

        int count_myAd = 0;
        switch (adFormat) {
            case "Banner":
                count_myAd = count_custBannerAd;
                break;
            case "NativeBanner":
                count_myAd = count_custNBAd;
                break;
            case "Native":
                count_myAd = count_custNativeAd;
                break;
            case "Interstitial":
                count_myAd = count_custIntAd;
                break;
            case "AppOpen":
                count_myAd = count_custAppOpenAd;
                break;
        }
        AdvertiseList customAdModel = null;
        if (adFormatWiseAd.size() != 0) {
            for (int j = 0; j <= adFormatWiseAd.size(); j++) {
                if (count_myAd % adFormatWiseAd.size() == j) {
                    customAdModel = adFormatWiseAd.get(j);
                }
            }
        }
        return customAdModel;
    }

    public void showAds(boolean isBack, final InterCallback callback) {
        if (!setResponseRoot()) {
            if (callback != null)
                callback.onClose(AdvertisementState.INTER_AD_RESPONSE_NULL);
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (callback != null)
                callback.onClose(AdvertisementState.INTER_AD_APPSETTING_NULL);
            return;
        }
        if (!AD_VISIBLE) {
            if (callback != null)
                callback.onClose(AdvertisementState.FIRST_TIME_AD_OFF);
            return;
        }

        if (!getQureka()) {
            if (isBack) {
                if (responseRoot.getAPPSETTINGS().getAPPOPENBACK().equalsIgnoreCase("ON")) {
                    showOpenCall(activity, (state) -> {

                        if (getInter(true)) {
                            showInterstitial(callback);
                        } else {
                            if (callback != null)
                                callback.onClose(AdvertisementState.AD_TYPE_OFF);
                        }
                    });
                } else {
                    if (getInter(true)) {
                        showInterstitial(callback);
                    } else {
                        if (callback != null)
                            callback.onClose(AdvertisementState.AD_TYPE_OFF);
                    }
                }
            } else {
                if (getInter(false)) {
                    showInterstitial(callback);
                } else {
                    if (callback != null)
                        callback.onClose(AdvertisementState.AD_TYPE_OFF);
                }
            }
        } else {
            if (getInter(isBack)) {
                showInterstitial(callback);
            } else {
                if (callback != null)
                    callback.onClose(AdvertisementState.AD_TYPE_OFF);
            }
        }
    }

    public void showSplashAD(final Activity context, final InterCallback callback) {

        if (!setResponseRoot()) {
            if (callback != null)
                callback.onClose(AdvertisementState.SPLASH_INTER_AD_RESPONSE_NULL);
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (callback != null)
                callback.onClose(AdvertisementState.SPLASH_INTER_AD_APPSETTING_NULL);
            return;
        }
        if (APIManager.getApp_adShowStatus() == 0) {
            if (callback != null)
                callback.onClose(AdvertisementState.AD_STATUS_FALSE);
            return;
        }
        if (!AD_VISIBLE) {
            if (callback != null)
                callback.onClose(AdvertisementState.FIRST_TIME_AD_OFF);
            return;
        }
        if (!getQureka()) {
            if (responseRoot.getAPPSETTINGS().getAFTERSPLASH().equalsIgnoreCase("INTER")) {
                if (getInter(false)) {
                    showInterstitial(callback);
                } else {
                    if (callback != null)
                        callback.onClose(AdvertisementState.AD_TYPE_OFF);
                }
            } else {
                showOpenCall(context, (state) -> {
                    if (callback != null)
                        callback.onClose(state);
                });
            }
        } else {
            if (getInter(false)) {
                showInterstitial(callback);
            } else {
                if (callback != null)
                    callback.onClose(AdvertisementState.AD_TYPE_OFF);
            }
        }
    }

    public void showAdsStart(final Activity context, final InterCallback callback) {
        if (!setResponseRoot()) {
            if (callback != null)
                callback.onClose(AdvertisementState.STARTSCREEN_INTER_AD_RESPONSE_NULL);
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (callback != null)
                callback.onClose(AdvertisementState.STARTSCREEN_INTER_AD_APPSETTING_NULL);
            return;
        }
        if (APIManager.getApp_adShowStatus() == 0) {
            if (callback != null)
                callback.onClose(AdvertisementState.AD_STATUS_FALSE);
            return;
        }

        if (!AD_VISIBLE) {
            if (callback != null)
                callback.onClose(AdvertisementState.FIRST_TIME_AD_OFF);
            return;
        }

        if (!getQureka()) {
            if (responseRoot.getAPPSETTINGS().getAPPOPENINTER().equalsIgnoreCase("ON")) {
                showOpenCall(context, (state) -> {
                    if (getInter(false)) {
                        showInterstitial(callback);
                    } else {
                        if (callback != null)
                            callback.onClose(AdvertisementState.AD_TYPE_OFF);
                    }
                });
            } else {
                if (getInter(false)) {
                    showInterstitial(callback);
                } else {
                    if (callback != null)
                        callback.onClose(AdvertisementState.AD_TYPE_OFF);
                }
            }
        } else {
            if (getInter(false)) {
                showInterstitial(callback);
            } else {
                if (callback != null)
                    callback.onClose(AdvertisementState.AD_TYPE_OFF);
            }
        }
    }

    private boolean getInter(boolean isBack) {
        boolean ad = getAd(isBack);
        if (isLog)
            Log.e(TAG, "getInter: " + ad + "  status:  " + getInterAdStatus());
        return ad;
    }

    boolean getAd(boolean isBack) {
        if (!setResponseRoot()) {
            return false;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            return false;
        }
        switch (responseRoot.getAPPSETTINGS().getADTYPE()) {
            case "ON-BACKOFF":
                if (isBack)
                    return false;
                else {
                    adStatus = 1;
                }
                break;
            case "LWPAd":
            case "ON":
                adStatus = 1;
                break;
            case "ON-OFF":
                adStatus = 2;
                break;
            case "ON-OFF-OFF":
                adStatus = 3;
                break;
            case "ON-ON-OFF":
                adStatus = 4;
                break;
        }


        if (adStatus == 4) {
            if (adInterVal % 3 == 0 || aBoolean) {
                adInterVal++;
                aBoolean = !aBoolean;
                return true;
            } else {
                adInterVal++;
                return false;
            }
        } else {
            if (adInterVal % adStatus == 0) {
                adInterVal++;
                return true;
            } else {
                adInterVal++;
                return false;
            }
        }
    }

    public void showOpenCall(Activity context, InterCallback myCallback) {
        if (!setResponseRoot()) {
            if (myCallback != null)
                myCallback.onClose(AdvertisementState.OPEN_AD_RESPONSE_NULL);
            return;
        }

        if (responseRoot.getAPPSETTINGS() == null) {
            if (myCallback != null)
                myCallback.onClose(AdvertisementState.OPEN_AD_APPSETTING_NULL);
            return;
        }

        if (!AD_VISIBLE) {
            myCallback.onClose(AdvertisementState.FIRST_TIME_AD_OFF);
            return;
        }


        if (APIManager.getApp_adShowStatus() == 0) {
            if (myCallback != null)
                myCallback.onClose(AdvertisementState.AD_STATUS_FALSE);
            return;
        }

        String app_AppOpenAdStatus = responseRoot.getAPPSETTINGS().getAppAppOpenAdStatus();
        if (app_AppOpenAdStatus.equals("false")) {
            if (myCallback != null) {
                myCallback.onClose(AdvertisementState.OPEN_AD_STATUS_FALSE);
            }
            return;
        }


        if (manager != null) {
            manager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                @Override
                public void onSuccess() {
                    myCallback.onClose(AdvertisementState.OPEN_AD_CLOSE);
                    loadAppOpenAd(context, new AppOpenManager.splshADlistner() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }

                @Override
                public void onError(String error) {
                    showCustomAppOpenAd(myCallback);
                    loadAppOpenAd(context, new AppOpenManager.splshADlistner() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            });
        } else {
            loadAppOpenAd(context, new AppOpenManager.splshADlistner() {
                @Override
                public void onSuccess() {
                    showOpenCall(context, myCallback);
                }

                @Override
                public void onError(String error) {
                    showCustomAppOpenAd(myCallback);
                }
            });
        }
    }

    public void loadAppOpenAd(Activity activity, AppOpenManager.splshADlistner myCallback1) {
        new TinyDB(activity).putString("AppOpenID", getUnitID(getPlatFormName("O"), "O", ""));
        manager = new AppOpenManager(activity);
        manager.fetchAd(myCallback1);
    }

    public void showCustomAppOpenAd(InterCallback myCallback) {
        if (!setResponseRoot()) {
            if (myCallback != null)
                myCallback.onClose(AdvertisementState.CUSTOM_OPEN_AD_RESPONSE_NULL);
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (myCallback != null)
                myCallback.onClose(AdvertisementState.CUSTOM_OPEN_AD_APPSETTING_NULL);
            return;
        }
        if (APIManager.getApp_adShowStatus() == 0) {
            if (myCallback != null)
                myCallback.onClose(AdvertisementState.AD_STATUS_FALSE);
            return;
        }
        if (!AD_VISIBLE) {
            if (myCallback != null)
                myCallback.onClose(AdvertisementState.FIRST_TIME_AD_OFF);
            return;
        }


        AdvertiseList customAdModel = getMyCustomAd("AppOpen");
        if (customAdModel != null) {
            try {
                final CustomAppOpenAds customAppOpenAds = new CustomAppOpenAds(activity, R.style.Theme_AppOpen_Dialog, customAdModel);
                customAppOpenAds.setCanceledOnTouchOutside(false);
                customAppOpenAds.setCancelable(false);
                customAppOpenAds.setOnCloseListener(new CustomAppOpenAds.OnCloseListener() {
                    public void onAdsCloseClick() {
                        customAppOpenAds.dismiss();
                        if (myCallback != null) {
                            myCallback.onClose(AdvertisementState.CUSTOM_OPEN_AD_CLOSE);
                        }
                    }

                    public void setOnKeyListener() {
                        customAppOpenAds.dismiss();
                        if (myCallback != null) {
                            myCallback.onClose(AdvertisementState.CUSTOM_OPEN_AD_CLOSE);
                        }
                    }
                });
                if (!activity.isFinishing()) {
                    customAppOpenAds.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (myCallback != null) {
                    myCallback.onClose(AdvertisementState.CUSTOM_OPEN_AD_FAIL);
                }
            }
        } else {
            if (myCallback != null) {
                myCallback.onClose(AdvertisementState.CUSTOM_OPEN_AD_FAIL_FROM_RESPONSE);
            }
        }
    }

    public void showBanner(ViewGroup viewGroup) {
        showBanner(viewGroup, null);
    }

    public void showBanner(ViewGroup viewGroup, InterCallback interCallback) {
        if (!setResponseRoot()) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.BANNER_AD_RESPONSE_NULL);
            }
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.BANNER_AD_APPSETTING_NULL);
            }
            return;
        }
        if (!AD_VISIBLE) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.FIRST_TIME_AD_OFF);
            }
            return;
        }
        if (!getBottomAd()) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.BANNER_AD_STATUS_FALSE);
            }
            return;
        }

        if (APIManager.getApp_adShowStatus() == 0) {
            if (interCallback != null)
                interCallback.onClose(AdvertisementState.AD_STATUS_FALSE);
            return;
        }

        if (!getQureka()) {
            if (responseRoot.getAPPSETTINGS().getNATIVEBANNER().equalsIgnoreCase("BANNER")) {
                String platform = getPlatFormName("B");
                String adUnitId = getUnitID(platform, "B", "");
                if (isLog)
                    Log.e(TAG, "showBanner: " + adUnitId + "  " + platform);
                turnShowBanner(viewGroup, adUnitId, interCallback);
            } else {
                String platform = getPlatFormName("BN");
                String adUnitId = getUnitID(platform, "BN", "");
                if (isLog)
                    Log.e(TAG, "showBanner:BN  " + adUnitId + "  " + platform);
                turnShowNativeBanner(viewGroup, adUnitId, interCallback);
            }
        } else {
            if (responseRoot.getAPPSETTINGS().getNATIVEBANNER().equalsIgnoreCase("BANNER"))
                BannerUtils.banner(viewGroup, activity, interCallback);
            else Nativeutils.banner(viewGroup, activity, interCallback);
        }
    }

    public void showNativeBanner(ViewGroup viewGroup) {
        showNativeBanner(viewGroup, null);
    }

    public void showNativeBanner(ViewGroup viewGroup, InterCallback interCallback) {
        if (!setResponseRoot()) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.NATIVE_BANNER_AD_RESPONSE_NULL);
            }
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.NATIVE_BANNER_AD_APPSETTING_NULL);
            }
            return;
        }
        if (!AD_VISIBLE) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.FIRST_TIME_AD_OFF);
            }
            return;
        }

        if (APIManager.getApp_adShowStatus() == 0) {
            if (interCallback != null)
                interCallback.onClose(AdvertisementState.AD_STATUS_FALSE);
            return;
        }

        if (!getQureka()) {
            String platform = getPlatFormName("BN");
            String adUnitId = getUnitID(platform, "BN", "");
            if (isLog)
                Log.e(TAG, "showBanner:BN  " + adUnitId + "  " + platform);
            turnShowNativeBanner(viewGroup, adUnitId, interCallback);
        } else {
            Nativeutils.banner(viewGroup, activity, interCallback);
        }
    }

    private void turnShowBanner(ViewGroup banner_container, String admob_b, InterCallback interCallback) {
        if (admob_b.isEmpty()) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.BANNER_AD_ID_EMPTY);
            }
            return;
        }

        final AdView mAdView = new AdView(activity);
        AdSize adSize = getAdSize();
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adSize.getHeight() + 5, activity.getResources().getDisplayMetrics());
        if (APIManager.isLog)
            Log.e(TAG, "turnShowBanner:size " + adSize.getHeight() + " ::: " + height + "   " + adSize.getWidth());
        ViewGroup.LayoutParams params = banner_container.getLayoutParams();
        params.height = height;
        banner_container.setLayoutParams(params);
        mAdView.setAdSize(adSize);
        mAdView.setAdUnitId(admob_b);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (callbackForAnalytics != null) {
                    callbackForAnalytics.onState("Banner Load  " + loadAdError.getCode() + " : " + loadAdError.getMessage());
                }
                if (interCallback != null) {
                    interCallback.onClose(AdvertisementState.BANNER_AD_FAIL);
                }
                banner_container.removeAllViews();
                if (sequenceQureka)
                    BannerUtils.banner(banner_container, activity, interCallback);
                else
                    showMyCustomBanner(banner_container, interCallback);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interCallback != null) {
                    interCallback.onClose(AdvertisementState.BANNER_AD_SHOW);
                }
                banner_container.removeAllViews();
                banner_container.addView(mAdView);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (interCallback != null) {
                    interCallback.onClose(AdvertisementState.BANNER_AD_CLICKED);
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                if (interCallback != null) {
                    interCallback.onClose(AdvertisementState.BANNER_AD_IMPRESSION);
                }
            }
        });
    }

    private AdSize getAdSize() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    private void showMyCustomBanner(final ViewGroup banner_container, InterCallback interCallback) {
        if (activity.isFinishing())
            return;
        final AdvertiseList appModal = getMyCustomAd("Banner");
        if (appModal != null) {
            View inflate2 = LayoutInflater.from(activity).inflate(R.layout.cust_banner, banner_container, false);
            ImageView imageView2 = inflate2.findViewById(R.id.icon);
            TextView textView = (TextView) inflate2.findViewById(R.id.primary);
            TextView textView2 = (TextView) inflate2.findViewById(R.id.secondary);

            Glide
                    .with(activity)
                    .load(appModal.getApp_logo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            banner_container.removeAllViews();
//                            nextBannerPlatform(banner_container);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView2);

            textView.setText(appModal.getApp_name());
            textView2.setText(appModal.getApp_shortDecription());
            inflate2.findViewById(R.id.cta).setOnClickListener(view -> {
                String action_str = appModal.getApp_packageName();
                if (action_str.contains("http")) {
                    Uri marketUri = Uri.parse(action_str);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    activity.startActivity(marketIntent);
                } else {
                    activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                }

            });
            banner_container.removeAllViews();
            banner_container.addView(inflate2);
            count_custBannerAd++;
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.CUSTOM_BANNER_AD_SHOW);
            }
        } else {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.CUSTOM_BANNER_AD_FAIL_FROM_RESPONSE);
            }
        }
    }

    void turnShowNativeBanner(ViewGroup banner_container, String admobB, InterCallback interCallback) {
        if (admobB.isEmpty()) {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.NATIVE_BANNER_AD_ID_EMPTY);
            }
            return;
        }

        final AdLoader adLoader = new AdLoader.Builder(activity, admobB)
                .forNativeAd(nativeAd -> {
                    new Inflate_ADS(activity).showSmall(nativeAd, banner_container);
                    if (interCallback != null) {
                        interCallback.onClose(AdvertisementState.NATIVE_BANNER_AD_SHOW);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        if (callbackForAnalytics != null) {
                            callbackForAnalytics.onState("NativeBanner Load  " + adError.getCode() + " : " + adError.getMessage());
                        }
                        if (interCallback != null) {
                            interCallback.onClose(AdvertisementState.NATIVE_BANNER_AD_FAIL);
                        }
                        if (sequenceQureka) {
                            Nativeutils.banner(banner_container, activity, interCallback);
                        } else
                            showMyCustomNativeBanner(banner_container, interCallback);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void showMyCustomSmallNative(final ViewGroup nbanner_container, NativeCallback nativeCallback) {
        if (activity.isFinishing())
            return;
        final AdvertiseList appModal = getMyCustomAd("NativeBanner");
        if (appModal != null) {

            View inflate2 = LayoutInflater.from(activity).inflate(R.layout.cust_small_native, nbanner_container, false);
            ImageView imageView2 = inflate2.findViewById(R.id.icon);
            TextView textView = (TextView) inflate2.findViewById(R.id.primary);
            TextView textView2 = (TextView) inflate2.findViewById(R.id.secondary);

            TextView txt_rate = (TextView) inflate2.findViewById(R.id.txt_rate);
            TextView txt_download = (TextView) inflate2.findViewById(R.id.txt_download);


            Glide
                    .with(activity)
                    .load(appModal.getApp_logo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            nbanner_container.removeAllViews();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView2);

            textView.setText(appModal.getApp_name());
            textView2.setText(appModal.getApp_shortDecription());
            txt_rate.setText(appModal.getApp_rating());
            txt_download.setText(appModal.getApp_download());

            inflate2.findViewById(R.id.cta).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    String action_str = appModal.getApp_packageName();
                    if (action_str.contains("http")) {
                        Uri marketUri = Uri.parse(action_str);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        activity.startActivity(marketIntent);
                    } else {
                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                    }

                }
            });
            nbanner_container.removeAllViews();
            nbanner_container.addView(inflate2);
            count_custNBAd++;
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.CUSTOM_NATIVE_AD_SHOW);
            }
        } else {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.CUSTOM_NATIVE_AD_FAIL_FROM_RESPONSE);
            }
        }
    }

    private void showMyCustomNativeBanner(final ViewGroup nbanner_container, InterCallback interCallback) {
        if (activity.isFinishing()) {
            return;
        }
        final AdvertiseList appModal = getMyCustomAd("NativeBanner");
        if (appModal != null) {

            View inflate2 = LayoutInflater.from(activity).inflate(R.layout.cust_native_banner, nbanner_container, false);
            ImageView imageView2 = inflate2.findViewById(R.id.icon);
            TextView textView = (TextView) inflate2.findViewById(R.id.primary);
            TextView textView2 = (TextView) inflate2.findViewById(R.id.secondary);

            TextView txt_rate = (TextView) inflate2.findViewById(R.id.txt_rate);
            TextView txt_download = (TextView) inflate2.findViewById(R.id.txt_download);


            Glide
                    .with(activity)
                    .load(appModal.getApp_logo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            nbanner_container.removeAllViews();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView2);

            textView.setText(appModal.getApp_name());
            textView2.setText(appModal.getApp_shortDecription());
            txt_rate.setText(appModal.getApp_rating());
            txt_download.setText(appModal.getApp_download());

            inflate2.findViewById(R.id.cta).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    String action_str = appModal.getApp_packageName();
                    if (action_str.contains("http")) {
                        Uri marketUri = Uri.parse(action_str);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        activity.startActivity(marketIntent);
                    } else {
                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                    }

                }
            });
            nbanner_container.removeAllViews();
            nbanner_container.addView(inflate2);
            count_custNBAd++;
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.CUSTOM_NATIVE_AD_SHOW);
            }
        } else {
            if (interCallback != null) {
                interCallback.onClose(AdvertisementState.CUSTOM_NATIVE_AD_FAIL_FROM_RESPONSE);
            }
        }
    }

    public void showNative(ViewGroup nativeAdContainer) {
        showNative(nativeAdContainer, null);
    }

    public void showNative(ViewGroup nativeAdContainer, NativeCallback nativeCallback) {
        if (!setResponseRoot()) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.NATIVE_AD_RESPONSE_NULL);
                nativeCallback.onLoad(true);
            }
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.NATIVE_AD_APPSETTING_NULL);
                nativeCallback.onLoad(true);
            }
            return;
        }
        if (!AD_VISIBLE) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.FIRST_TIME_AD_OFF);
                nativeCallback.onLoad(true);
            }
            return;
        }
        if (APIManager.getApp_adShowStatus() == 0) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.AD_STATUS_FALSE);
                nativeCallback.onLoad(true);
            }
            return;
        }

        if (!getQureka()) {
            String platform = getPlatFormName("N");
            String adUnitId = getUnitID(platform, "N", "");
            if (isLog)
                Log.e(TAG, "showNative: " + adUnitId + "  " + platform);
            showAdmobNative(nativeAdContainer, false, adUnitId, nativeCallback);
        } else {
            Nativeutils.mediam(nativeAdContainer, activity, nativeCallback);
        }
    }

    public void showSmallNative(ViewGroup nativeAdContainer) {
        showSmallNative(nativeAdContainer, null);
    }

    public void showSmallNative(ViewGroup nativeAdContainer, NativeCallback nativeCallback) {
        if (!setResponseRoot()) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.NATIVE_AD_RESPONSE_NULL);
                nativeCallback.onLoad(true);
            }
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.NATIVE_AD_APPSETTING_NULL);
                nativeCallback.onLoad(true);
            }
            return;
        }
        if (!AD_VISIBLE) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.FIRST_TIME_AD_OFF);
                nativeCallback.onLoad(true);
            }
            return;
        }

        if (APIManager.getApp_adShowStatus() == 0) {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.AD_STATUS_FALSE);
                nativeCallback.onLoad(true);
            }
            return;
        }

        if (!getQureka()) {
            String platform = getPlatFormName("N");
            String adUnitId = getUnitID(platform, "N", "");
            if (isLog)
                Log.e(TAG, "showSmallNative: " + adUnitId + "  " + platform);
            showAdmobNative(nativeAdContainer, true, adUnitId, nativeCallback);
        } else {
            Nativeutils.small(nativeAdContainer, activity, nativeCallback);
        }
    }

    private void showAdmobNative(final ViewGroup nativeAdContainer, boolean small, String
            id, NativeCallback nativeCallback) {
        if (id.isEmpty()) {
            if (nativeCallback != null) {
                nativeCallback.onLoad(true);
                nativeCallback.onState(AdvertisementState.NATIVE_AD_ID_EMPTY);
            }
            return;
        }

        final AdLoader adLoader = new AdLoader.Builder(activity, id)
                .forNativeAd(nativeAd -> {
                    if (!small)
                        new Inflate_ADS(activity).inflate_NATIV_ADMOB(nativeAd, nativeAdContainer);
                    else
                        new Inflate_ADS(activity).inflate_NATIV_ADMOB_SMALL(nativeAd, nativeAdContainer);
                    if (nativeCallback != null) {
                        nativeCallback.onLoad(false);
                        nativeCallback.onState(AdvertisementState.NATIVE_AD_SHOW);
                    }

                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        if (callbackForAnalytics != null) {
                            callbackForAnalytics.onState("Native Load  " + adError.getCode() + " : " + adError.getMessage());
                        }
                        if (isLog)
                            Log.e(TAG, "onAdFailedToLoad: " + adError.getMessage());

                        if (sequenceQureka) {
                            if (!small)
                                Nativeutils.mediam(nativeAdContainer, activity, nativeCallback);
                            else
                                Nativeutils.small(nativeAdContainer, activity, nativeCallback);

                        } else {
                            if (!small)
                                showMyCustomNative(nativeAdContainer, nativeCallback);
                            else
                                showMyCustomSmallNative(nativeAdContainer, nativeCallback);
                        }
                        if (nativeCallback != null) {
                            nativeCallback.onLoad(true);
                            nativeCallback.onState(AdvertisementState.NATIVE_AD_FAIL);
                        }
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void showMyCustomNative(final ViewGroup nativeAdContainer, NativeCallback nativeCallback) {
        if (activity.isFinishing())
            return;
        final AdvertiseList appModal = getMyCustomAd("Native");
        if (appModal != null) {
            final View inflate = LayoutInflater.from(activity).inflate(R.layout.cust_native, nativeAdContainer, false);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.media_view);
            ImageView imageView2 = (ImageView) inflate.findViewById(R.id.icon);
            TextView textView = (TextView) inflate.findViewById(R.id.primary);
            TextView textView2 = (TextView) inflate.findViewById(R.id.body);
            TextView txt_rate = (TextView) inflate.findViewById(R.id.txt_rate);
            TextView txt_download = (TextView) inflate.findViewById(R.id.txt_download);

            textView.setText(appModal.getApp_name());
            textView2.setText(appModal.getApp_shortDecription());
            txt_rate.setText(appModal.getApp_rating());
            txt_download.setText(appModal.getApp_download());
            inflate.findViewById(R.id.cta).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String action_str = appModal.getApp_packageName();
                    if (action_str.contains("http")) {
                        Uri marketUri = Uri.parse(action_str);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        activity.startActivity(marketIntent);
                    } else {
                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                    }
                }
            });

            Glide
                    .with(activity)
                    .load(appModal.getApp_banner())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            nativeAdContainer.removeAllViews();
//                            nextNativePlatform(nativeAdContainer, small);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                            return false;
                        }
                    })
                    .into(imageView);

            Glide
                    .with(activity)
                    .load(appModal.getApp_logo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView2);

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String action_str = appModal.getApp_packageName();
                    if (action_str.contains("http")) {
                        Uri marketUri = Uri.parse(action_str);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        activity.startActivity(marketIntent);
                    } else {
                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                    }
                }
            });

            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(inflate);
            count_custNativeAd++;

            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.CUSTOM_NATIVE_AD_SHOW);
            }
        } else {
            if (nativeCallback != null) {
                nativeCallback.onState(AdvertisementState.CUSTOM_NATIVE_AD_FAIL_FROM_RESPONSE);
            }
        }
    }

    public List<MOREAPPEXIT> get_EXITMoreAppData() {
        if (!setResponseRoot())
            return new ArrayList<>();
        return responseRoot.getMOREAPPEXIT();
    }

    public List<MOREAPPSPLASH> get_SPLASHMoreAppData() {
        if (!setResponseRoot())
            return new ArrayList<>();
        return responseRoot.getMOREAPPSPLASH();
    }

    public boolean isUpdate() {
        return new TinyDB(activity).getBoolean("isUpdateCall");
    }

    public void loadRewardAd() {
        if (!setResponseRoot())
            return;
        if (responseRoot.getAPPSETTINGS() == null) {
            return;
        }
        String platform = getPlatFormName("R");
        String adUnitId = getUnitID(platform, "R", "");
        if (isLog)
            Log.e(TAG, "loadRewardAd: " + adUnitId + "  " + platform);
        RewardedAd.load(activity, adUnitId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                showCustomReward = true;
                rewardedAd = null;
                if (callbackForAnalytics != null) {
                    callbackForAnalytics.onState("Reward Load  " + loadAdError.getCode() + " : " + loadAdError.getMessage());
                }
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewarded) {
                showCustomReward = false;
                rewardedAd = rewarded;
                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        rewardedAd = null;
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        try {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (Exception e) {

                        }
                        if (callbackForAnalytics != null) {
                            callbackForAnalytics.onState("Reward Show  " + adError.getCode() + " : " + adError.getMessage());
                        }
                        if (rewardCallback != null) {
                            rewardCallback.onFail();
                            rewardCallback.onState(AdvertisementState.REWARD_AD_SHOW_FAIL);
                        }
                        loadRewardAd();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        rewardCallback.onClose(booleans[0]);
                        rewardCallback.onState(AdvertisementState.REWARD_AD_CLOSE);
                        loadRewardAd();
                    }
                });

            }
        });
    }

    public void showRewardAd(RewardCallback rewardCallback) {
        this.rewardCallback = rewardCallback;
        if (!setResponseRoot()) {
            if (rewardCallback != null) {
                rewardCallback.onClose(false);
                rewardCallback.onState(AdvertisementState.REWARD_AD_RESPONSE_NULL);
            }
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (rewardCallback != null) {
                rewardCallback.onClose(false);
                rewardCallback.onState(AdvertisementState.REWARD_AD_APPSETTING_NULL);
            }
            return;
        }

//        if (!AD_VISIBLE) {
//            if (rewardCallback != null) {
//                rewardCallback.onClose(true);
//                rewardCallback.onState(AdvertisementState.FIRST_TIME_AD_OFF);
//            }
//            return;
//        }

        if (APIManager.getApp_adShowStatus() == 0) {
            if (rewardCallback != null) {
                rewardCallback.onClose(true);
                rewardCallback.onState(AdvertisementState.AD_STATUS_FALSE);
            }
            return;
        }

        if (!getQureka()) {
            dialog = new Dialog(activity);
            View view = LayoutInflater.from(activity).inflate(R.layout.ad_progress_dialog, null);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (showCustomReward || rewardedAd == null) {
                if (sequenceQureka) {
                    CustomiseinterActivity.H(activity, () -> {
                        loadRewardAd();
                        if (rewardCallback != null) {
                            rewardCallback.onClose(true);
                            rewardCallback.onState(AdvertisementState.QUREKA_REWARD_AD_CLOSE);
                        }
                    }, Glob.dataset(activity));
                } else {
                    showCustomReward(activity);
                }
            } else {
                if (responseRoot.getAPPSETTINGS().getAppDialogBeforeAdShow().equals("1")) {
                    if (!activity.isFinishing())
                        dialog.show();
                    new CountDownTimer(ad_dialog_time_in_second * 1000L, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            if (rewardedAd != null) {
                                rewardedAd.show(activity, rewardItem -> booleans[0] = true);
                                dialog.dismiss();
                            }
                        }
                    }.start();
                } else {
                    rewardedAd.show(activity, rewardItem -> booleans[0] = true);
                    dialog.dismiss();
                }
            }
        } else {
            CustomiseinterActivity.H(activity, () -> {
                rewardCallback.onClose(true);
                rewardCallback.onState(AdvertisementState.QUREKA_REWARD_AD_CLOSE);
            }, Glob.dataset(activity));
        }
    }

    private void showCustomReward(final Activity activity) {
        if (activity.isFinishing()) {
            if (rewardCallback != null) {
                rewardCallback.onClose(false);
                rewardCallback.onState(AdvertisementState.CUSTOM_REWARD_AD_FAIL);
            }
            return;
        }
        if (!activity.isFinishing())
            dialog.show();
        AdvertiseList advertiseList = getMyCustomAd("Interstitial");
        if (advertiseList != null) {
            try {
                final CustomIntAds customIntAds = new CustomIntAds(activity, advertiseList);
                customIntAds.setCanceledOnTouchOutside(false);
                customIntAds.setCancelable(false);
                customIntAds.setOnCloseListener(new CustomIntAds.OnCloseListener() {
                    public void onAdsCloseClick() {
                        customIntAds.dismiss();
                        loadRewardAd();
                        if (rewardCallback != null) {
                            rewardCallback.onClose(true);
                            rewardCallback.onState(AdvertisementState.CUSTOM_REWARD_AD_CLOSE);
                        }
                    }

                    public void setOnKeyListener() {
                        customIntAds.dismiss();
                        loadRewardAd();
                        if (rewardCallback != null) {
                            rewardCallback.onClose(true);
                            rewardCallback.onState(AdvertisementState.CUSTOM_REWARD_AD_CLOSE);
                        }
                    }
                });
                if (!activity.isFinishing()) {
                    dialog.dismiss();
                    customIntAds.show();
                }
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                loadRewardAd();
                if (rewardCallback != null) {
                    rewardCallback.onClose(false);
                    rewardCallback.onState(AdvertisementState.CUSTOM_REWARD_AD_FAIL);
                }
            }
        } else {
            dialog.dismiss();
            loadRewardAd();
            if (rewardCallback != null) {
                rewardCallback.onClose(false);
                rewardCallback.onState(AdvertisementState.CUSTOM_REWARD_AD_FAIL_FROM_RESPONSE);
            }
        }
    }

    public BottomSheetDialog showExitDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_exit_dialog);
        RelativeLayout adContainerBack = bottomSheetDialog.findViewById(R.id.adContainerBack);
        TextView update = bottomSheetDialog.findViewById(R.id.update);
        TextView later = bottomSheetDialog.findViewById(R.id.later);
        showSmallNative(adContainerBack);
        bottomSheetDialog.show();
        later.setOnClickListener(view -> bottomSheetDialog.dismiss());
        update.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            activity.finish();
        });
        return bottomSheetDialog;
    }

    public void showRatingDialog(DialogCallback dialogCallback) {
        if (APIManager.getInstance(activity).getRatingDialog()) {
            final RatingDialog ratingDialog = new RatingDialog.Builder(activity)
                    .session(1)
                    .threshold(5)
                    .ratingBarColor(R.color.colorAdColor)
                    .playstoreUrl("https://play.google.com/store/apps/details?id=" + activity.getPackageName())
                    .onRatingBarFormSumbit((feedback, rate) -> {
                        if (dialogCallback != null)
                            dialogCallback.onClose(feedback, rate);
                    }).onDialogList(rate -> {
                        if (dialogCallback != null)
                            dialogCallback.onClose("", rate);
                    })
                    .build();
            ratingDialog.show();
        } else {
            if (dialogCallback != null)
                dialogCallback.onClose("", 5);
        }
    }

    public void showPromoAdDialog(boolean startScreen) {
        if (getPromoAdStatus() && !activity.isFinishing()) {
            File file = new File(activity.getCacheDir(), "promoAD.json");
            String json = loadJsonFromFile(file);
            if (json != null && !json.equals("")) {
                try {
                    ResponsePromo responsePromo = new Gson().fromJson(json, ResponsePromo.class);
                    if (responsePromo != null) {
                        if (responsePromo.getApp() != null) {
                            if (responsePromo.getApp().size() > 0) {
                                if (responsePromo.getApp().get(0).getAdPosition().equals("STARTSCREEN") && startScreen) {
                                    showPromo(responsePromo.getApp().get(0));
                                } else if (responsePromo.getApp().get(0).getAdPosition().equals("EXITSCREEN") && !startScreen) {
                                    showPromo(responsePromo.getApp().get(0));
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }


    private void showPromo(AppItemPromo appItemPromo) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().addFlags(2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View inflate = LayoutInflater.from(dialog.getContext()).inflate(R.layout.promo_dialog, null, false);
        dialog.setContentView(inflate);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView idClose = (ImageView) inflate.findViewById(R.id.id_close);
        ImageView idLogo = (ImageView) inflate.findViewById(R.id.id_logo);
        TextView idTitle = (TextView) inflate.findViewById(R.id.id_title);
        TextView idDownload = (TextView) inflate.findViewById(R.id.id_download);
        StyledPlayerView playerView = (StyledPlayerView) inflate.findViewById(R.id.playerView);
        ShimmerView ivShimmerView = (ShimmerView) inflate.findViewById(R.id.ivShimmerView);

        idTitle.setText(appItemPromo.getAdTitle());
        Log.e(TAG, "showPromoAdDialog: " + appItemPromo.getAdImage());
        ExoPlayer player = new ExoPlayer.Builder(activity).build();
        if (isImageFile(appItemPromo.getAdImage())) {
            Glide.with(activity).load(appItemPromo.getAdImage()).into(idLogo);
            ivShimmerView.setVisibility(View.GONE);
            playerView.setVisibility(View.GONE);
        } else {
            playerView.setVisibility(View.VISIBLE);
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(appItemPromo.getAdImage());
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
            player.addListener(new Player.Listener() {
                @Override
                public void onVideoSizeChanged(VideoSize videoSize) {
                    Player.Listener.super.onVideoSizeChanged(videoSize);
                    Log.e(TAG, "onVideoSizeChanged: " + videoSize.width + "  " + videoSize.height + "  " + videoSize.pixelWidthHeightRatio);
                    ivShimmerView.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    if (videoSize.width < videoSize.height) {
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
                        ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
                        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, activity.getResources().getDisplayMetrics());
                        playerView.setLayoutParams(layoutParams);
                    } else {
                        ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
                        layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                        playerView.setLayoutParams(layoutParams);
                    }
                }

                @Override
                public void onPlayerError(PlaybackException error) {
                    Player.Listener.super.onPlayerError(error);
                    Log.e(TAG, "onPlayerError: " + error.getMessage());
                }
            });
        }
        idClose.setOnClickListener(v -> {
            dialog.dismiss();
            if (player.isPlaying())
                player.stop();
            player.setPlayWhenReady(false);
            player.release();
        });
        dialog.setOnDismissListener(dialogInterface -> {
            if (player.isPlaying())
                player.stop();
            player.setPlayWhenReady(false);
            player.release();
        });
        idDownload.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(appItemPromo.getAppLink()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(appItemPromo.getAppLink()));
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent2);
            }
            dialog.dismiss();
            if (player.isPlaying())
                player.stop();
            player.setPlayWhenReady(false);
            player.release();
        });
        dialog.show();
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

}
