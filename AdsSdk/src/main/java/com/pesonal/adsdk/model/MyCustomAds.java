package com.pesonal.adsdk.model;

import com.google.gson.annotations.SerializedName;

public class MyCustomAds{

	@SerializedName("ad_loadAdIdsType")
	private String adLoadAdIdsType;

	@SerializedName("ad_showAdStatus")
	private String adShowAdStatus;

	public void setAdLoadAdIdsType(String adLoadAdIdsType){
		this.adLoadAdIdsType = adLoadAdIdsType;
	}

	public String getAdLoadAdIdsType(){
		return adLoadAdIdsType;
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
			"MyCustomAds{" + 
			"ad_loadAdIdsType = '" + adLoadAdIdsType + '\'' + 
			",ad_showAdStatus = '" + adShowAdStatus + '\'' + 
			"}";
		}
}