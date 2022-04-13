package com.pesonal.adsdk.remote;

public interface RewardCallback {

    void onClose(boolean isSuccess);

    void onState(AdvertisementState state);

    void onFail();
}
