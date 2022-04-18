package com.pesonal.adsdk.model.promo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponsePromo{

	@SerializedName("app")
	private List<AppItemPromo> app;

	public void setApp(List<AppItemPromo> app){
		this.app = app;
	}

	public List<AppItemPromo> getApp(){
		return app;
	}

	@Override
 	public String toString(){
		return 
			"ResponsePromo{" + 
			"app = '" + app + '\'' + 
			"}";
		}
}