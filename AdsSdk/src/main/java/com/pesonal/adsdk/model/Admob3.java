package com.pesonal.adsdk.model;

import com.google.gson.annotations.SerializedName;

public class Admob3{

	@SerializedName("RewardedVideo1")
	private String rewardedVideo1;

	@SerializedName("AppID")
	private String appID;

	@SerializedName("AppOpen1")
	private String appOpen1;

	@SerializedName("Interstitial1")
	private String interstitial1;

	@SerializedName("ad_loadAdIdsType")
	private String adLoadAdIdsType;

	@SerializedName("Banner1")
	private String banner1;

	@SerializedName("Native1")
	private String native1;

	@SerializedName("NativeBanner1")
	private String nativeBanner1;

	@SerializedName("ad_showAdStatus")
	private String adShowAdStatus;

	public void setRewardedVideo1(String rewardedVideo1){
		this.rewardedVideo1 = rewardedVideo1;
	}

	public String getRewardedVideo1(){
		return rewardedVideo1;
	}

	public void setAppID(String appID){
		this.appID = appID;
	}

	public String getAppID(){
		return appID;
	}

	public void setAppOpen1(String appOpen1){
		this.appOpen1 = appOpen1;
	}

	public String getAppOpen1(){
		return appOpen1;
	}

	public void setInterstitial1(String interstitial1){
		this.interstitial1 = interstitial1;
	}

	public String getInterstitial1(){
		return interstitial1;
	}

	public void setAdLoadAdIdsType(String adLoadAdIdsType){
		this.adLoadAdIdsType = adLoadAdIdsType;
	}

	public String getAdLoadAdIdsType(){
		return adLoadAdIdsType;
	}

	public void setBanner1(String banner1){
		this.banner1 = banner1;
	}

	public String getBanner1(){
		return banner1;
	}

	public void setNative1(String native1){
		this.native1 = native1;
	}

	public String getNative1(){
		return native1;
	}

	public void setNativeBanner1(String nativeBanner1){
		this.nativeBanner1 = nativeBanner1;
	}

	public String getNativeBanner1(){
		return nativeBanner1;
	}

	public void setAdShowAdStatus(String adShowAdStatus){
		this.adShowAdStatus = adShowAdStatus;
	}

	public String getAdShowAdStatus(){
		return adShowAdStatus;
	}

	@Override
 	public String toString(){
		return 
			"Admob3{" + 
			"rewardedVideo1 = '" + rewardedVideo1 + '\'' + 
			",appID = '" + appID + '\'' + 
			",appOpen1 = '" + appOpen1 + '\'' + 
			",interstitial1 = '" + interstitial1 + '\'' + 
			",ad_loadAdIdsType = '" + adLoadAdIdsType + '\'' + 
			",banner1 = '" + banner1 + '\'' + 
			",native1 = '" + native1 + '\'' + 
			",nativeBanner1 = '" + nativeBanner1 + '\'' + 
			",ad_showAdStatus = '" + adShowAdStatus + '\'' + 
			"}";
		}
}