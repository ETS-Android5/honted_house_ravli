# Honted House

This is AdModule for Aani brothers infotech team members.


# Setup

This library requires `minSdkVersion` to `19` or above.


## Step #1. Add the JitPack repository to your build file

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

## Step #2. Add the dependency.
![Release](https://jitpack.io/v/Aanibrothers-Infotech/honted_house_ravli.svg)


```groovy
dependencies {
    implementation 'com.github.Aanibrothers-Infotech:honted_house_ravli:[latest-release]'
}
```

## Step #3. Initialization.
### Splash Activity
Extend as ```BaseAdsActivity``` and call below method in ```onCreate()```.
```java
 initializeSplash(SplashActivity.this, new SplashListner() {
            @Override
            public void onSuccess() {
                APIManager.getInstance(SplashActivity.this).showSplashAD(SplashActivity.this, (state) -> {
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
            public void onExtra(JsonElement extraData) {
                Log.e("TAG", "onExtra: "+extraData.toString() );
            }
        });

```

### Application class
In ```setClass()``` method replace SplashActivity.class with your launcher activity.
```java
public class MyApp extends AppClass {

   @Override
    public void onState(AdvertisementState state) {
    }

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
 APIManager.getInstance(StartSecondActivity.this).showAdsStart(StartSecondActivity.this, (state) -> {
    Intent intent = new Intent(StartSecondActivity.this, MainAAActivity.class);
    startActivity(intent);
    finish();
});
```

Check for app update.
```java
if (APIManager.getInstance(this).isUpdate()) {
   new DialogUtils().showUpdateDialog(this, "https://play.google.com/store/apps/details?id=" + getPackageName());
}
```

And Add PromotionDialog in ```StartActivity```.
```java
APIManager.getInstance(this).showPromoAdDialog(true);
```

Fetch Promo Apps list for ```StartActivity```.
```java
APIManager.getInstance(StartSecondActivity.this).get_SPLASHMoreAppData();
```

[comment]: <> (### MainActivity)

[comment]: <> (Extend as ```BannerVpnActivity``` and call below method in ```onCreate&#40;&#41;```.)

[comment]: <> (```java)

[comment]: <> ( if&#40;APIManager.getInstance&#40;this&#41;.getVpnMenuStatus&#40;&#41;&#41;{)

[comment]: <> (     btnOpenVpnScreen.setVisibility&#40;View.VISIBLE&#41;;)

[comment]: <> ( }else btnOpenVpnScreen.setVisibility&#40;View.GONE&#41;;)

[comment]: <> ( iVPN = &#40;FrameLayout&#41; findViewById&#40;R.id.iVPN&#41;;)

[comment]: <> ( rootViewGuide = &#40;CircularRevealRelativeLayout&#41; findViewById&#40;R.id.rootViewGuide&#41;;)

[comment]: <> ( guideVpn = &#40;LottieAnimationView&#41; findViewById&#40;R.id.guideVpn&#41;;)

[comment]: <> ( layoutGuideVPN = &#40;FrameLayout&#41; findViewById&#40;R.id.layoutGuideVPN&#41;;)

[comment]: <> ( if &#40;APIManager.getInstance&#40;this&#41;.getVpnStatus&#40;&#41;&#41; {)

[comment]: <> (     setBannerView&#40;iVPN&#41;;)

[comment]: <> (     setGuideView&#40;layoutGuideVPN&#41;;)

[comment]: <> (     setBackgroundColor&#40;getColor&#40;R.color.colorAdBlack&#41;&#41;;)

[comment]: <> (     setTextColor&#40;getColor&#40;R.color.colorAdWhite&#41;&#41;;)

[comment]: <> (     guideVpn.setOnClickListener&#40;view -> {)

[comment]: <> (         connectVpnListener&#40;&#40;isConnect, connectionState&#41; -> {)

[comment]: <> (              if &#40;isConnect == CONNECTION_STATE.CONNECTED&#41;{)

[comment]: <> (                  rootViewGuide.setVisibility&#40;View.GONE&#41;;)

[comment]: <> (              } else if &#40;isConnect == CONNECTION_STATE.CONNECTING&#41; {)

[comment]: <> (                  guideVpn.setVisibility&#40;View.GONE&#41;;)

[comment]: <> (                  layoutGuideVPN.setVisibility&#40;View.INVISIBLE&#41;;)

[comment]: <> (              } else if &#40;isConnect == CONNECTION_STATE.FAIL&#41; {)

[comment]: <> (                  rootViewGuide.setVisibility&#40;View.GONE&#41;;)

[comment]: <> (              }else if &#40;isConnect == CONNECTION_STATE.DISCONNECTED&#41; {)

[comment]: <> (                  rootViewGuide.setVisibility&#40;View.GONE&#41;;)

[comment]: <> (              })

[comment]: <> (         }&#41;;)

[comment]: <> (     }&#41;;)

[comment]: <> (     if &#40;getConnection&#40;&#41;&#41; {)

[comment]: <> (         rootViewGuide.setVisibility&#40;View.GONE&#41;;)

[comment]: <> (     } else {)

[comment]: <> (         rootViewGuide.setVisibility&#40;View.VISIBLE&#41;;)

[comment]: <> (     })

[comment]: <> ( })

[comment]: <> (```)

[comment]: <> (And call below in ```onBackPressed&#40;&#41;```.)

[comment]: <> (```java)

[comment]: <> ( if &#40;rootViewGuide.getVisibility&#40;&#41; == View.VISIBLE&#41;)

[comment]: <> (    return;)

[comment]: <> (```)

[comment]: <> (And put below layout in your ```xml```.)

[comment]: <> (```xml)

[comment]: <> (   <FrameLayout)

[comment]: <> (        android:id="@+id/iVPN")

[comment]: <> (        android:layout_width="match_parent")

[comment]: <> (        android:layout_height="wrap_content" />)

[comment]: <> (   <com.google.android.material.circularreveal.CircularRevealRelativeLayout)

[comment]: <> (          android:id="@+id/rootViewGuide")

[comment]: <> (          android:layout_width="match_parent")

[comment]: <> (          android:layout_height="match_parent")

[comment]: <> (          android:clickable="true")

[comment]: <> (          android:visibility="gone">)

[comment]: <> (          <View)

[comment]: <> (              android:layout_width="match_parent")

[comment]: <> (              android:layout_height="match_parent")

[comment]: <> (              android:layout_below="@+id/xyz")

[comment]: <> (              android:background="#9D000000" />)

[comment]: <> (          <RelativeLayout)

[comment]: <> (              android:id="@+id/xyz")

[comment]: <> (              android:layout_width="match_parent")

[comment]: <> (              android:layout_height="@dimen/_50sdp">)

[comment]: <> (              <FrameLayout)

[comment]: <> (                  android:id="@+id/layoutGuideVPN")

[comment]: <> (                  android:layout_width="match_parent")

[comment]: <> (                  android:layout_height="match_parent" />)

[comment]: <> (              <com.airbnb.lottie.LottieAnimationView)

[comment]: <> (                  android:id="@+id/guideVpn")

[comment]: <> (                  android:layout_width="80dp")

[comment]: <> (                  android:layout_height="50dp")

[comment]: <> (                  android:layout_alignParentEnd="true")

[comment]: <> (                  android:layout_centerVertical="true")

[comment]: <> (                  android:layout_marginEnd="26dp")

[comment]: <> (                  app:lottie_autoPlay="true")

[comment]: <> (                  app:lottie_loop="true")

[comment]: <> (                  app:lottie_rawRes="@raw/guide" />)

[comment]: <> (          </RelativeLayout>)

[comment]: <> (   </com.google.android.material.circularreveal.CircularRevealRelativeLayout>)

[comment]: <> (```)

[comment]: <> (### For VPN Screen)

[comment]: <> (```java)

[comment]: <> (   startActivity&#40;new Intent&#40;MainActivity.this, VanishVPNActivity.class&#41;&#41;;)

[comment]: <> (```)

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
APIManager.getInstance(MainActivity2.this).showAds( false, (state) -> {
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
    public void onState(AdvertisementState state){
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
And Add PromotionDialog in ```ExitActivity```.
```java
APIManager.getInstance(this).showPromoAdDialog(false);
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

### Rate Dialog
```java
  APIManager.getInstance(this).showRatingDialog((feedBack,rate) -> {
      Log.e("TAG", "onClick: rate "+ feedBack+"  "+rate);
  });
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
-keep class androidx.mediarouter.app.MediaRouteActionProvider {
  *;
}
```



## Support

For support, email ravinasukhadiya@aanibrothers.in
