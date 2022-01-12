# Honted House

This is AdModule for Aani brothers infotech team members.


# Setup

This library requires `minSdkVersion` to `19` or above.

## Step #1. Add this line ```gradle.properties``` file

```authToken=jp_62qh7p9ium74pbl2t38o2qdahp```

## Step #2. Add the JitPack repository to your build file

```gradle
allprojects {
    repositories {
	    ...
	    maven {
                url "https://jitpack.io"
                credentials { username authToken }
              }
    }
}
```

## Step #3. Add the dependency.

```groovy
dependencies {
    implementation 'com.github.Aanibrothers-Infotech:honted_house_ravli:1.0.0'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-runtime:2.2.0'
    annotationProcessor 'androidx.lifecycle:lifecycle-compiler:2.2.0'
}
```

## Step #4. Initialization.
### Splash Activity
Extend as Ads_SplashActivity and call below method in ```onCreate()```.
```java
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
```

### Application class
In ```onMoveToForeground()``` method replace SplashActivity.class with your launcher activity.
```java
public class MyApplication extends Application
        implements ActivityLifecycleCallbacks, LifecycleObserver {

    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    @OnLifecycleEvent(Event.ON_START)
    protected void onMoveToForeground() {
        if (APIManager.getApp_adShowStatus() == 1 && !APIManager.getInstance(currentActivity).getQureka() && !SplashActivity.class.getName().contains(currentActivity.getLocalClassName()))
            APIManager.getInstance(currentActivity).showOpenCall(currentActivity, () -> {
            });
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
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
if(APIManager.getInstance(StartSecondActivity.this).isUpdate()){
    showUpdateDialog("https://play.google.com/store/apps/details?id=" + getPackageName());
}
```

Fetch Promo Apps list for ```StartActivity```.
```java
APIManager.getInstance(StartSecondActivity.this).get_SPLASHMoreAppData();
```

### NativeAd
```java
APIManager.getInstance(MainAAActivity.this).showNative(adContainer);
APIManager.getInstance(MainAAActivity.this).showSmallNative(adContainer1);
```

### BannerAd
```java
APIManager.getInstance(MainActivity2.this).showBanner(adContainer);
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
APIManager.getInstance(MainAAActivity.this).showAdsStartExit(this, () -> {
    startActivity(new Intent(MainAAActivity.this, ExitActivity.class));
});
```

### Change Color of Ads Button
add below line in ```colors.xml```
```xml
    <color name="white">#ffffff</color>
    <color name="black">#000000</color>
    <color name="adColor">#E91E63</color>
```

### Privacy Dialog
```java
PrivacyDialog.show(StartSecondActivity.this,getResources().getStringArray(R.array.terms_of_service));
```

### Gift Dialog
```java
new GiftAds(StartSecondActivity.this).showGiftAds();
```



## Support

For support, email ravinasukhadiya@aanibrothers.in