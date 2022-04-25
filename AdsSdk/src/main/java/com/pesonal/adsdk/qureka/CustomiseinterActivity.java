package com.pesonal.adsdk.qureka;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.collection.ArraySet;

import com.bumptech.glide.Glide;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.remote.APIManager;

public class CustomiseinterActivity extends AppCompatActivity {

    public static Adsresponse f2124p;
    public static OnClick f2125q;
    public LinearLayout A;
    public LinearLayout B;
    public String C;
    public ImageView f2127s;
    public TextView f2128t;
    public TextView f2129u;
    public TextView f2130v;
    public TextView f2131w;
    public TextView f2132x;
    public RatingBar f2133y;
    public LinearLayout f2134z;
    public ImageView native_ad_icon;
    private int V = 0;
    private ArraySet<Adsresponse> arrayList = new ArraySet<>();
    private RelativeLayout int_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView textView;
        String str;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        int i7 = Glob.f2126r;
        if (i7 == 0) {
            setContentView(R.layout.inter_3);
        } else if (i7 == 1) {
            setContentView(R.layout.inter_1);
        } else if (i7 == 2) {
            setContentView(R.layout.inter_2);
        }


        int_bg = (RelativeLayout) findViewById(R.id.int_bg);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llPersonalAd);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.llPersonalAdCenter);
        if (f2124p != null) {
            try {
                f2127s = (ImageView) findViewById(R.id.native_ad_media);
                native_ad_icon = (ImageView) findViewById(R.id.native_ad_icon);
                f2128t = (TextView) findViewById(R.id.native_ad_title);
                f2129u = (TextView) findViewById(R.id.native_ad_social_context);
                f2130v = (TextView) findViewById(R.id.txt_rate);
                f2131w = (TextView) findViewById(R.id.txt_download);
                f2132x = (TextView) findViewById(R.id.native_ad_call_to_action);
                f2134z = (LinearLayout) findViewById(R.id.adPersonalCloseBtn);
                A = (LinearLayout) findViewById(R.id.userCount);
                B = (LinearLayout) findViewById(R.id.adPersonalLlPlayStore);
                TextView textView2 = (TextView) findViewById(R.id.querkaText);
                f2133y = (RatingBar) findViewById(R.id.ad_stars);

                Glide.with(this).load("file:///android_asset/"+f2124p.f18655c).into(native_ad_icon);
                Glide.with(this).load("file:///android_asset/"+f2124p.f18656d).into(f2127s);
                f2128t.setText(f2124p.f18653a);
                f2129u.setText(f2124p.f18657e);
                f2130v.setText(f2124p.f18658f);
                f2133y.setRating(Float.parseFloat(f2124p.f18658f));
                f2131w.setText(f2124p.f18659g);
                String str2 = f2124p.f18654b;
                C = str2;
                if (str2.contains("http")) {
                    A.setVisibility(View.GONE);
                    f2134z.setVisibility(View.GONE);
                    B.setVisibility(View.GONE);
                    if (Glob.f2126r == 1) {
                        textView = f2132x;
                        str = "Play Game";
                    } else {
                        textView = f2132x;
                        str = "Play Now";
                    }
                } else {
                    A.setVisibility(View.VISIBLE);
                    f2134z.setVisibility(View.VISIBLE);
                    B.setVisibility(View.VISIBLE);
                    if (Glob.f2126r == 1) {
                        textView = f2132x;
                        str = "Install";
                    } else {
                        textView = f2132x;
                        str = "Download";
                    }
                }
                textView.setText(str);
                int i8 = Glob.f2126r;
                if (i8 == 2) {
                    Glob.f2126r = 0;
                    F(findViewById(R.id.llcus3), 1000);
                    new Handler().postDelayed(() -> {
                        int i71;
                        FadeIn(findViewById(R.id.llPersonalAd));
                        FadeIn(findViewById(R.id.main));
                        FadeIn(findViewById(R.id.aa));
                        String str1 = f2124p.f18654b;
                        C = str1;
                        if (str1.contains("http")) {
                            i71 = R.id.querkaText;
                        } else {
                            i71 = R.id.adPersonalLlPlayStore;
                        }
                        FadeIn(findViewById(i71));
                        FadeIn(findViewById(R.id.adPersonalLlCloseInstallBtns));
                    }, 500);
                } else if (i8 == 1) {
                    Glob.f2126r = i8 + 1;
                    final View findViewById = findViewById(R.id.native_ad_icon);
                    Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
                    loadAnimation.setFillAfter(true);
                    findViewById.startAnimation(loadAnimation);
                    TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.5f, 1, 0.0f);
                    translateAnimation.setDuration((long) 1000);
                    translateAnimation.setFillAfter(true);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            findViewById.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation loadAnimation = AnimationUtils.loadAnimation(CustomiseinterActivity.this, R.anim.zoom_out);
                            loadAnimation.setFillAfter(true);
                            findViewById.startAnimation(loadAnimation);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    translateAnimation.setFillEnabled(true);
                    findViewById.startAnimation(translateAnimation);
                    F(findViewById(R.id.cvTopAd), 1000);
                    String str3 = f2124p.f18654b;
                    C = str3;
                    if (str3.contains("http")) {
                        findViewById(R.id.querkaText).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.querkaText).setVisibility(View.GONE);
                    }
                    new Handler().postDelayed(() -> {
                        FadeIn(findViewById(R.id.aa));
                        FadeIn(findViewById(R.id.adPersonalLlCloseInstallBtnsCenter));
                    },1000);
                } else {
                    Glob.f2126r = i8 + 1;
                    final View findViewById2 = findViewById(R.id.native_ad_icon);
                    TranslateAnimation translateAnimation2 = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -2.0f, 1, 0.0f);
                    translateAnimation2.setDuration((long) 500);
                    translateAnimation2.setFillAfter(true);
                    translateAnimation2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            findViewById2.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    translateAnimation2.setFillEnabled(true);
                    findViewById2.startAnimation(translateAnimation2);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int i7;
                            View view;
                            E(findViewById(R.id.native_ad_title), 300);
                            E(findViewById(R.id.banner), 500);
                            E(findViewById(R.id.native_ad_social_context), 600);
                            String str = f2124p.f18654b;
                            C = str;
                            if (str.contains("http")) {
                                view = findViewById(R.id.querkaText);
                                i7 = 450;
                            } else {
                                E(findViewById(R.id.userCount), 400);
                                view = findViewById(R.id.adPersonalLlPlayStore);
                                i7 = 700;
                            }
                            E(view, i7);
                            E(findViewById(R.id.adPersonalLlCloseInstallBtns), 800);
                        }
                    }, 400);
                }
                findViewById(R.id.native_ad_call_to_action).setOnClickListener(view -> {
                    String url = APIManager.QUREKALINK;
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setShowTitle(true);
                    builder.build().launchUrl(CustomiseinterActivity.this, Uri.parse(url));
                });

                int_bg.setOnClickListener(view -> {
                    String url = APIManager.QUREKALINK;
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setShowTitle(true);
                    builder.build().launchUrl(CustomiseinterActivity.this, Uri.parse(url));
                });
                findViewById(R.id.ImgClose).setOnClickListener(view -> {
                    Log.e("TAG", "onClick: "+ f2125q);
                    if(f2125q!=null) {
                        f2125q.a();
                        f2125q=null;
                    }
                    finish();
                });
                f2134z.setOnClickListener(v -> G());
                V++;
                return;
            } catch (Exception unused) {
            }
        }
        G();


    }

    private void G() {
        finish();
        OnClick onClickVar = f2125q;
        if (onClickVar != null) {
            onClickVar.a();
            f2125q = null;
        }
    }


    public void E(final View view, int i7) {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 5.0f, 1, 0.0f);
        translateAnimation.setDuration((long) i7);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translateAnimation.setFillEnabled(true);
        view.startAnimation(translateAnimation);
    }

    public void F(final View view, int i7) {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.5f, 1, 0.0f);
        translateAnimation.setDuration((long) i7);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translateAnimation.setFillEnabled(true);
        view.startAnimation(translateAnimation);
    }

    public void FadeIn(final View view) {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        view.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public static void H(Activity activity, OnClick onClickVar, Adsresponse adsresponseVar) {
        f2124p = adsresponseVar;
        f2125q = onClickVar;
        activity.startActivity(new Intent(activity, CustomiseinterActivity.class));
    }


    @Override
    public void onBackPressed() {

    }

}