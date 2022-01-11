package com.pesonal.adsdk.qureka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;

import com.pesonal.adsdk.remote.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Glob {

    private static ArrayList<Adsresponse> arrayList = new ArrayList<>();
    private static Adsresponse adsresponseVar;
    public static int f2126r;

    public static void b(Context context, String str) {
        Intent intent;
        Bundle bundle;
        boolean z7 = false;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
//            Bundle bundle2 = new Bundle();
//            bundle2.putBinder("android.support.customtabs.extra.SESSION", null);
//            intent.putExtras(bundle2);
            intent.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", Color.parseColor("#66bb6a"));
//            bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
//            intent.putExtra("android.support.customtabs.extra.EXIT_ANIMATION_BUNDLE", ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle());
            intent.putExtra("android.support.customtabs.extra.EXTRA_ENABLE_INSTANT_APPS", true);
            intent.setPackage("com.android.chrome");
            intent.setData(Uri.parse(APIManager.QUREKALINK));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e8) {
            e8.printStackTrace();
            return;
        }
    }


    public static StringBuilder h(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }


    public static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }


    public static Adsresponse dataset(Context context) {

        try {
            JSONObject obj = new JSONObject(Glob.AssetJSONFile("ads_list.json", context));
            JSONArray tutorialsArray = obj.getJSONArray("Advertise_List");

            for (int i9 = 0; i9 < tutorialsArray.length(); i9++) {
                JSONObject tutorialsObject = tutorialsArray.getJSONObject(i9);
                Adsresponse adsresponseVar2 = new Adsresponse();
                tutorialsObject.getInt("ad_id");
                adsresponseVar2.f18653a = tutorialsObject.getString("app_name");
                adsresponseVar2.f18654b = APIManager.QUREKALINK;//tutorialsObject.getString("app_packageName");
                adsresponseVar2.f18655c = tutorialsObject.getString("app_logo");
                adsresponseVar2.f18656d = tutorialsObject.getString("app_banner");
                adsresponseVar2.f18657e = tutorialsObject.getString("app_shortDecription");
                adsresponseVar2.f18658f = tutorialsObject.getString("app_rating");
                adsresponseVar2.f18659g = tutorialsObject.getString("app_download");
                adsresponseVar2.f18660h = tutorialsObject.getString("app_AdFormat");
                adsresponseVar2.banner = tutorialsObject.getString("banner_image");
                arrayList.add(adsresponseVar2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i11 = 0;

        Log.e("TAG", "dataset: " + arrayList.size());
        for (int i12 = 0; i12 <= arrayList.size(); i12++) {
            if (i11 % arrayList.size() == i12) {
                int index = (int) (Math.random() * arrayList.size());
                Log.e("TAG", "dataset: " + index);
                adsresponseVar = (Adsresponse) arrayList.get(index);
            }
        }
        return adsresponseVar;
    }

    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        // package name is the default package
        // for our custom chrome tab
        String packageName = "com.android.chrome";
        if (packageName != null) {

            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName);

            // in that custom tab intent we are passing
            // our url which we have to browse.
            customTabsIntent.launchUrl(activity, uri);
        } else {
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }


    public static void showPolicy(Context c) {
        String url =
                "https://contactextremesky.blogspot.com/2021/06/228-welcome-to-extreme-sky-privacy.html";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.build().intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.build().launchUrl(c, Uri.parse(url));
    }

}
