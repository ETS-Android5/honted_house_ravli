package com.pesonal.adsdk.remote;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.pesonal.adsdk.AppOpenManager;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.customAd.CustomAppOpenAds;
import com.pesonal.adsdk.customAd.CustomIntAds;
import com.pesonal.adsdk.model.AdvertiseList;
import com.pesonal.adsdk.model.MOREAPPEXIT;
import com.pesonal.adsdk.model.MOREAPPSPLASH;
import com.pesonal.adsdk.model.ResponseRoot;
import com.pesonal.adsdk.qureka.BannerUtils;
import com.pesonal.adsdk.qureka.CustomiseinterActivity;
import com.pesonal.adsdk.qureka.Glob;
import com.pesonal.adsdk.qureka.Nativeutils;
import com.pesonal.adsdk.utils.Inflate_ADS;
import com.pesonal.adsdk.utils.getDataListner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class APIManager {

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
    private static boolean showCustomreward = false;
    private RewardCallback rewardCallback;
    final boolean[] booleans = new boolean[]{false};

    public APIManager(Activity activity) {
        APIManager.activity = activity;
    }

    public static APIManager getInstance(Activity activity) {
        APIManager.activity = activity;
        if (mInstance == null) {
            mInstance = new APIManager(activity);
        }
        return mInstance;
    }

    public boolean getQureka() {
        if (responseRoot == null)
            return false;
        return responseRoot.getAPPSETTINGS().getQUREKA().equals("ON");
    }

    public boolean isExitScreen() {
        if (responseRoot == null)
            return false;
        return responseRoot.getAPPSETTINGS().getExitScreen().equalsIgnoreCase("ON");
    }

    public static int getApp_adShowStatus() {
        try {
            return Integer.parseInt(responseRoot.getAPPSETTINGS().getAppAdShowStatus());
        } catch (Exception e) {
            return 1;
        }
    }

    public void init(getDataListner listner, int cversion) {
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
        } else if (app_howShowAdInterstitial.equals("1") && alernateAd.length != 0) {
            for (int i = 0; i <= 10; i++) {
                int min = 0;
                int max = alernateAd.length;
                int random = new Random().nextInt((max - min) + 1) + min;
                if (alernateAd[random].contains(ADMOB)) {
                    platformArrayList.add(alernateAd[random]);
                }
            }
            for (String s : adSequence) {
                if (platformArrayList.size() != 0) {
                    if (!platformArrayList.get(0).equals(s)) {
                        if (s.contains(ADMOB)) {
                            platformArrayList.add(s);
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
        }
        if (responseRoot.getAPPSETTINGS().getAppRedirectOtherAppStatus().equals("1")) {
            String redirectNewPackage = responseRoot.getAPPSETTINGS().getAppNewPackageName();
            listner.onRedirect(redirectNewPackage);
        } else {
            if (responseRoot.getAPPSETTINGS().getAppUpdateAppDialogStatus().equals("1") && checkUpdate(cversion)) {
                new TinyDB(activity).putBoolean("isUpdateCall", true);
            }
            listner.onSuccess();
            MobileAds.initialize(activity, initializationStatus -> {
            });

            if (ADMOB_O.length > 0) {
                new TinyDB(activity).putString("AppOpenID", getUnitID(getPlatFormName("O"), "O", ""));
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
                if (ADMOB_O.length > 0)
                    if (whichShowOpenAd != ADMOB_O.length - 1) {
                        returnPlatForm = ADMOB_O[whichShowOpenAd];
                        whichShowOpenAd++;
                    } else {
                        returnPlatForm = ADMOB_O[whichShowOpenAd];
                        whichShowOpenAd = 0;
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
        if (responseRoot == null)
            return;
        if (responseRoot.getAPPSETTINGS() == null) {
            return;
        }
        String platform = getPlatFormName("I");
        String adUnitId = getUnitID(platform, "I", whichOne);
//        Log.e("TAG", "RequestInterstitial: " + adUnitId + "  " + platform);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                showCustom = false;
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        requestInterstitial("Next");
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
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
            }
        });
    }

    public void showInterstitial(InterCallback interCallback) {
        this.interCallback = interCallback;
        if (responseRoot == null) {
            if (interCallback != null)
                interCallback.onClose();
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (interCallback != null)
                interCallback.onClose();
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            dialog = new Dialog(activity);
            View view = LayoutInflater.from(activity).inflate(R.layout.ad_progress_dialog, null);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (showCustom || mInterstitialAd == null) {
                showCustomInter(activity);
            } else {
                if (responseRoot.getAPPSETTINGS().getAppDialogBeforeAdShow().equals("1")) {
                    dialog.show();
                    new CountDownTimer(ad_dialog_time_in_second * 1000L, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(activity);
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
            CustomiseinterActivity.H(activity, this::interstitialCallBack, Glob.dataset(activity));
        }

    }

    void interstitialCallBack() {
        if (interCallback != null) {
            interCallback.onClose();
            interCallback = null;
        }
    }

    private void showCustomInter(final Activity activity) {
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
                        interstitialCallBack();
                    }

                    public void setOnKeyListener() {
                        customIntAds.dismiss();
                        loadInter();
                        interstitialCallBack();
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
                interstitialCallBack();
            }
        } else {
            dialog.dismiss();
            loadInter();
            interstitialCallBack();
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
        if (responseRoot == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            if (isBack && responseRoot.getAPPSETTINGS().getAPPOPENBACK().equalsIgnoreCase("ON")) {
                showOpenCall(activity, () -> {
                    if (getInter(true)) {
                        showInterstitial(callback);
                    } else {
                        if (callback != null)
                            callback.onClose();
                    }
                });
            } else {
                if (getInter(false)) {
                    showInterstitial(callback);
                } else {
                    if (callback != null)
                        callback.onClose();
                }
            }
        } else {
            if (getInter(isBack)) {
                showInterstitial(callback);
            } else {
                if (callback != null)
                    callback.onClose();
            }
        }
    }

    public void showSplashAD(final Activity context, final InterCallback callback) {
        if (responseRoot == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            if (responseRoot.getAPPSETTINGS().getAFTERSPLASH().equalsIgnoreCase("INTER")) {
                if (getInter(false)) {
                    showInterstitial(callback);
                } else {
                    if (callback != null)
                        callback.onClose();
                }
            } else {
                showOpenCall(context, () -> {
                    if (callback != null)
                        callback.onClose();
                });
            }
        } else {
            if (getInter(false)) {
                showInterstitial(callback);
            } else {
                if (callback != null)
                    callback.onClose();
            }
        }
    }

    public void showAdsStart(final Activity context, final InterCallback callback) {
        if (responseRoot == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            if (responseRoot.getAPPSETTINGS().getAPPOPENINTER().equalsIgnoreCase("ON")) {
                showOpenCall(context, () -> {
                    if (getInter(false)) {
                        showInterstitial(callback);
                    } else {
                        if (callback != null)
                            callback.onClose();
                    }
                });
            } else {
                if (getInter(false)) {
                    showInterstitial(callback);
                } else {
                    if (callback != null)
                        callback.onClose();
                }
            }
        } else {
            if (getInter(false)) {
                showInterstitial(callback);
            } else {
                if (callback != null)
                    callback.onClose();
            }
        }
    }

    public void showAdsStartExit(final Activity context, final InterCallback callback) {
        if (responseRoot == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (callback != null)
                callback.onClose();
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            if (responseRoot.getAPPSETTINGS().getAPPOPENINTER().equalsIgnoreCase("ON")) {
                showOpenCall(context, () -> showInterstitial(callback));
            } else {
                showInterstitial(callback);
            }
        } else {
            showInterstitial(callback);
        }
    }

    private boolean getInter(boolean isBack) {
        boolean ad = getAd(isBack);
        Log.e(TAG, "getInter: " + ad);
        return ad;
    }

    boolean getAd(boolean isBack) {
        if (responseRoot == null) {
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
        if (manager != null) {
            manager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                @Override
                public void onSuccess() {
                    myCallback.onClose();
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
        if (responseRoot == null) {
            if (myCallback != null)
                myCallback.onClose();
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (myCallback != null)
                myCallback.onClose();
            return;
        }
        String app_AppOpenAdStatus = responseRoot.getAPPSETTINGS().getAppAppOpenAdStatus();
        if (app_AppOpenAdStatus.equals("false")) {
            if (myCallback != null) {
                myCallback.onClose();
            }
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
                            myCallback.onClose();
                        }
                    }

                    public void setOnKeyListener() {
                        customAppOpenAds.dismiss();
                        if (myCallback != null) {
                            myCallback.onClose();
                        }
                    }
                });
                customAppOpenAds.show();
            } catch (Exception e) {
                e.printStackTrace();
                if (myCallback != null) {
                    myCallback.onClose();
                }
            }
        } else {
            if (myCallback != null) {
                myCallback.onClose();
            }
        }
    }

    public void showBanner(ViewGroup viewGroup) {
        if (responseRoot == null) {
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            if (responseRoot.getAPPSETTINGS().getNATIVEBANNER().equalsIgnoreCase("BANNER")) {
                String platform = getPlatFormName("B");
                String adUnitId = getUnitID(platform, "B", "");
                turnShowBanner(viewGroup, adUnitId);
            } else {
                String platform = getPlatFormName("BN");
                String adUnitId = getUnitID(platform, "BN", "");
                turnShowNativeBanner(viewGroup, adUnitId);
            }
        } else {
            if (responseRoot.getAPPSETTINGS().getNATIVEBANNER().equalsIgnoreCase("BANNER"))
                BannerUtils.banner(viewGroup, activity);
            else Nativeutils.banner(viewGroup, activity);
        }
    }

    private void turnShowBanner(ViewGroup banner_container, String admob_b) {
        if (admob_b.isEmpty()) {
            return;
        }

        final AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.SMART_BANNER);
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
                banner_container.removeAllViews();
                showMyCustomBanner(banner_container);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                banner_container.removeAllViews();
                banner_container.addView(mAdView);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }

    private void showMyCustomBanner(final ViewGroup banner_container) {
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
        }
    }

    void turnShowNativeBanner(ViewGroup banner_container, String admobB) {

        final AdLoader adLoader = new AdLoader.Builder(activity, admobB)
                .forNativeAd(nativeAd -> new Inflate_ADS(activity).showSmall(nativeAd, banner_container))
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        showMyCustomNativeBanner(banner_container);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void showMyCustomSmallNative(final ViewGroup nbanner_container) {
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
        }
    }

    private void showMyCustomNativeBanner(final ViewGroup nbanner_container) {
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
        }
    }

    public void showNative(ViewGroup nativeAdContainer) {
        if (responseRoot == null) {
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            String platform = getPlatFormName("N");
            String adUnitId = getUnitID(platform, "N", "");
//            Log.e("TAG", "showNative: " + adUnitId + "  " + platform);
            showAdmobNative(nativeAdContainer, false, adUnitId);
        } else {
            Nativeutils.mediam(nativeAdContainer, activity);
        }
    }

    public void showSmallNative(ViewGroup nativeAdContainer) {
        if (responseRoot == null) {
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            String platform = getPlatFormName("N");
            String adUnitId = getUnitID(platform, "N", "");
            showAdmobNative(nativeAdContainer, true, adUnitId);
        } else {
            Nativeutils.small(nativeAdContainer, activity);
        }
    }

    private void showAdmobNative(final ViewGroup nativeAdContainer, boolean small, String id) {
        if (id.isEmpty()) {
            return;
        }

        final AdLoader adLoader = new AdLoader.Builder(activity, id)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        if (!small)
                            new Inflate_ADS(activity).inflate_NATIV_ADMOB(nativeAd, nativeAdContainer);
                        else
                            new Inflate_ADS(activity).inflate_NATIV_ADMOB_SMALL(nativeAd, nativeAdContainer);

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Log.e(TAG, "onAdFailedToLoad: " + adError.getMessage());
                        if (!small)
                            showMyCustomNative(nativeAdContainer);
                        else
                            showMyCustomSmallNative(nativeAdContainer);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void showMyCustomNative(final ViewGroup nativeAdContainer) {

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


        } else {
        }
    }

    public List<MOREAPPEXIT> get_EXITMoreAppData() {
        if (responseRoot == null)
            return new ArrayList<>();
        return responseRoot.getMOREAPPEXIT();
    }

    public List<MOREAPPSPLASH> get_SPLASHMoreAppData() {
        if (responseRoot == null)
            return new ArrayList<>();
        return responseRoot.getMOREAPPSPLASH();
    }

    public boolean isUpdate() {
        return new TinyDB(activity).getBoolean("isUpdateCall");
    }

    public void loadRewardAd() {
        if (responseRoot == null)
            return;
        if (responseRoot.getAPPSETTINGS() == null) {
            return;
        }
        String platform = getPlatFormName("R");
        String adUnitId = getUnitID(platform, "R", "");

        RewardedAd.load(activity, adUnitId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                showCustomreward = true;
                rewardedAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewarded) {
                showCustomreward = false;
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
                        if (rewardCallback != null)
                            rewardCallback.onFail();
                        loadRewardAd();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        rewardCallback.onClose(booleans[0]);
                        loadRewardAd();
                    }
                });

            }
        });
    }

    public void showRewardAd(RewardCallback rewardCallback) {
        this.rewardCallback = rewardCallback;
        if (responseRoot == null) {
            if (rewardCallback != null)
                rewardCallback.onClose(false);
            return;
        }
        if (responseRoot.getAPPSETTINGS() == null) {
            if (rewardCallback != null)
                rewardCallback.onClose(false);
            return;
        }
        if (!responseRoot.getAPPSETTINGS().getQUREKA().equalsIgnoreCase("ON")) {
            dialog = new Dialog(activity);
            View view = LayoutInflater.from(activity).inflate(R.layout.ad_progress_dialog, null);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (showCustomreward || rewardedAd == null) {
                showCustomReward(activity);
            } else {
                if (responseRoot.getAPPSETTINGS().getAppDialogBeforeAdShow().equals("1")) {
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
            CustomiseinterActivity.H(activity, () -> rewardCallback.onClose(true), Glob.dataset(activity));
        }
    }

    private void showCustomReward(final Activity activity) {
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
                        if (rewardCallback != null)
                            rewardCallback.onClose(true);
                    }

                    public void setOnKeyListener() {
                        customIntAds.dismiss();
                        loadRewardAd();
                        if (rewardCallback != null)
                            rewardCallback.onClose(true);
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
                if (rewardCallback != null)
                    rewardCallback.onClose(false);
            }
        } else {
            dialog.dismiss();
            loadRewardAd();
            if (rewardCallback != null)
                rewardCallback.onClose(false);
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
        update.setOnClickListener(view -> activity.finish());
        return bottomSheetDialog;
    }
}
