# Honted House

This is AdModule for Aani brothers infotech team members.


# Setup

This library requires `minSdkVersion` to `24` or above.

## Step #1. Add this line ```gradle.properties``` file

```android.bundle.enableUncompressedNativeLibs=false```

and Add below line `application` tag in `AndroidManifest.xml`
```
android:extractNativeLibs="true"
tools:replace="android:extractNativeLibs"
```

android.bundle.enableUncompressedNativeLibs=false

## Step #2. Add the JitPack repository to your build file

```gradle
allprojects {
    repositories {
	    ...
	    jcenter()
	    maven {
            url "https://jitpack.io"
        }
    }
}
```

## Step #3. Add the dependency.
![Release](https://jitpack.io/v/Aanibrothers-Infotech/honted_house_ravli.svg)


```groovy
dependencies {
    implementation 'com.github.Aanibrothers-Infotech:honted_house_ravli:[latest-release]'
}
```

## Step #4. Initialization.
### Splash Activity
Extend as ```BaseAdsActivity``` and call below method in ```onCreate()```.
```java
 initializeSplash(SplashActivity.this, new SplashListner() {
            @Override
            public void onSuccess() {
                 APIManager.getInstance(SplashActivity.this).showSplashAD(SplashActivity.this, () -> {
                     if (APIManager.getInstance(SplashActivity.this).getScreenStatus()) {
                         Intent intent = new Intent(SplashActivity.this, StartSecondActivity.class);
                         startActivity(intent);
                         finish();
                     } else {
                         Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                         startActivity(intent);
                         finish();
                     }
                 });
            }

            @Override
            public void onExtra(String extraData) {

            }
        });
```

### Application class
In ```setClass()``` method replace SplashActivity.class with your launcher activity.
```java
public class MyApp extends AppClass {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG)
            APIManager.setIsLog(true);
        setClass(SplashActivity.class);
    }
}
```

### StartSecondActivity
If you have Start Button Activity then use below method to goto ```MainActivity.java```.

```java
 APIManager.getInstance(StartSecondActivity.this).showAdsStart(StartSecondActivity.this, () -> {
    Intent intent = new Intent(StartSecondActivity.this, MainAAActivity.class);
    startActivity(intent);
    finish();
});
```

Check for app update.
```java
if (APIManager.getInstance(StartSecondActivity.this).isUpdate()) {
   new DialogUtils().showUpdateDialog(this, "https://play.google.com/store/apps/details?id=" + getPackageName());
}
```

Fetch Promo Apps list for ```StartActivity```.
```java
APIManager.getInstance(StartSecondActivity.this).get_SPLASHMoreAppData();
```

### MainActivity
Extend as ```BannerVpnActivity``` and call below method in ```onCreate()```.
```java

 if(APIManager.getInstance(this).getVpnMenuStatus()){
     btnOpenVpnScreen.setVisibility(View.VISIBLE);
 }else btnOpenVpnScreen.setVisibility(View.GONE);

 iVPN = (FrameLayout) findViewById(R.id.iVPN);
 rootViewGuide = (CircularRevealRelativeLayout) findViewById(R.id.rootViewGuide);
 guideVpn = (LottieAnimationView) findViewById(R.id.guideVpn);
 layoutGuideVPN = (FrameLayout) findViewById(R.id.layoutGuideVPN);
 if (APIManager.getInstance(this).getVpnStatus()) {
     setBannerView(iVPN);
     setGuideView(layoutGuideVPN);
     setBackgroundColor(getColor(R.color.colorAdBlack));
     setTextColor(getColor(R.color.colorAdWhite));
     guideVpn.setOnClickListener(view -> {
         connectVpnListener((isConnect, connectionState) -> {
              if (isConnect == CONNECTION_STATE.CONNECTED){
                  rootViewGuide.setVisibility(View.GONE);
              } else if (isConnect == CONNECTION_STATE.CONNECTING) {
                  guideVpn.setVisibility(View.GONE);
                  layoutGuideVPN.setVisibility(View.INVISIBLE);
              } else if (isConnect == CONNECTION_STATE.FAIL) {
                  rootViewGuide.setVisibility(View.GONE);
              }
         });
     });
     if (getConnection()) {
         rootViewGuide.setVisibility(View.GONE);
     } else {
         rootViewGuide.setVisibility(View.VISIBLE);
     }
 }
```

And call below in ```onBackPressed()```.
```java
 if (rootViewGuide.getVisibility() == View.VISIBLE)
    return;
```
And put below layout in your ```xml```.
```xml

   <FrameLayout
        android:id="@+id/iVPN"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

      <com.google.android.material.circularreveal.CircularRevealRelativeLayout
             android:id="@+id/rootViewGuide"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:clickable="true"
             android:visibility="gone">

             <View
                 android:layout_width="match_parent"
                 android:layout_height="match_parent"
                 android:layout_below="@+id/layoutGuideVPN"
                 android:background="#9D000000" />

             <RelativeLayout
                 android:layout_width="match_parent"
                 android:layout_height="@dimen/_50sdp">

                 <FrameLayout
                     android:id="@+id/layoutGuideVPN"
                     android:layout_width="match_parent"
                     android:layout_height="match_parent" />

                 <com.airbnb.lottie.LottieAnimationView
                     android:id="@+id/guideVpn"
                     android:layout_width="80dp"
                     android:layout_height="50dp"
                     android:layout_alignParentEnd="true"
                     android:layout_centerVertical="true"
                     android:layout_marginEnd="26dp"
                     app:lottie_autoPlay="true"
                     app:lottie_loop="true"
                     app:lottie_rawRes="@raw/guide" />

             </RelativeLayout>

     </com.google.android.material.circularreveal.CircularRevealRelativeLayout>
```

### For VPN Screen
```java
   startActivity(new Intent(MainActivity.this, VanishVPNActivity.class));
```

### NativeAd
```java
APIManager.getInstance(MainAAActivity.this).showNative(adContainer);  //size - @dimen/_200sdp
APIManager.getInstance(MainAAActivity.this).showSmallNative(adContainer1);  //size - @dimen/_130sdp
```

### BannerAd
```java
APIManager.getInstance(MainActivity2.this).showBanner(adContainer);   //size - @dimen/_65sdp
```

### InterstitialAd
If you call below method in ```onBackPressed()``` then pass boolean value as true otherwise pass false.
```java
APIManager.getInstance(MainActivity2.this).showAds( false, () -> {
    startActivity(new Intent(MainActivity2.this, MainActivity3.class));
});
```

### RewardAd
```java
APIManager.getInstance(MainActivity2.this).showRewardAd(new RewardCallback() {
    @Override
    public void onClose(boolean isSuccess) {

    }

    @Override
    public void onFail() {

    }
});
```

### Exit
Call this method when exit app in ```onBackPressed()```
```java
if(APIManager.getInstance(this).isExitScreen()){
  startActivity(new Intent(MainAAActivity.this, ExitActivity.class));
  finish();
}else {
   APIManager.getInstance(MainAAActivity.this).showExitDialog();
}
```

### Change Color of Ads Button
add below line in ```colors.xml```
```xml
   <color name="colorAdWhite">#ffffff</color>
   <color name="colorAdBlack">#000000</color>
   <color name="colorAdGrey">#797979</color>
   <color name="colorAdColor">#E91E63</color>
   <color name="colorAdCard">#FFFFFF</color>
```

### Privacy Dialog
```java
PrivacyDialog.show(StartSecondActivity.this,getResources().getStringArray(R.array.terms_of_service));
```

### Gift Dialog
```java
new GiftAds(StartSecondActivity.this).showGiftAds();
```


### Proguard Rules
```java
-keep class androidx.renderscript.** { *; }
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keep class com.yandex.metrica.** { *; }
-dontwarn com.yandex.metrica.**
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.pesonal.adsdk.model.** { <fields>; }
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**
```



## Support

For support, email ravinasukhadiya@aanibrothers.in
