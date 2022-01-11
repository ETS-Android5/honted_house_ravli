package com.pesonal.adsdk.model;

import com.google.gson.annotations.SerializedName;

public class PLACEMENT{

	@SerializedName("MyCustomAds")
	private MyCustomAds myCustomAds;

	@SerializedName("Admob3")
	private Admob3 admob3;

	@SerializedName("Admob2")
	private Admob2 admob2;

	@SerializedName("Admob1")
	private Admob1 admob1;

	public void setMyCustomAds(MyCustomAds myCustomAds){
		this.myCustomAds = myCustomAds;
	}

	public MyCustomAds getMyCustomAds(){
		return myCustomAds;
	}

	public void setAdmob3(Admob3 admob3){
		this.admob3 = admob3;
	}

	public Admob3 getAdmob3(){
		return admob3;
	}

	public void setAdmob2(Admob2 admob2){
		this.admob2 = admob2;
	}

	public Admob2 getAdmob2(){
		return admob2;
	}

	public void setAdmob1(Admob1 admob1){
		this.admob1 = admob1;
	}

	public Admob1 getAdmob1(){
		return admob1;
	}


	@Override
 	public String toString(){
		return 
			"PLACEMENT{" +
			",myCustomAds = '" + myCustomAds + '\'' +
			",admob3 = '" + admob3 + '\'' + 
			",admob2 = '" + admob2 + '\'' + 
			",admob1 = '" + admob1 + '\'' +
			"}";
		}
}