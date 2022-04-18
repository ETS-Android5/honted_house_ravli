package com.pesonal.adsdk.model.promo;

import com.google.gson.annotations.SerializedName;

public class AppItemPromo {

	@SerializedName("AdPosition")
	private String adPosition;

	@SerializedName("AdImage")
	private String adImage;

	@SerializedName("AppLink")
	private String appLink;

	@SerializedName("AdTitle")
	private String adTitle;

	public void setAdPosition(String adPosition){
		this.adPosition = adPosition;
	}

	public String getAdPosition(){
		return adPosition;
	}

	public void setAdImage(String adImage){
		this.adImage = adImage;
	}

	public String getAdImage(){
		return adImage;
	}

	public void setAppLink(String appLink){
		this.appLink = appLink;
	}

	public String getAppLink(){
		return appLink;
	}

	public void setAdTitle(String adTitle){
		this.adTitle = adTitle;
	}

	public String getAdTitle(){
		return adTitle;
	}

	@Override
 	public String toString(){
		return 
			"AppItem{" + 
			"adPosition = '" + adPosition + '\'' + 
			",adImage = '" + adImage + '\'' + 
			",appLink = '" + appLink + '\'' + 
			",adTitle = '" + adTitle + '\'' + 
			"}";
		}
}