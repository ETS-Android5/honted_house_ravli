package com.pesonal.adsdk.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseRoot{

	@SerializedName("MSG")
	private String mSG;

	@SerializedName("EXTRA_DATA")
	private String eXTRADATA="";

	@SerializedName("STATUS")
	private boolean sTATUS;

	@SerializedName("PLACEMENT")
	private PLACEMENT pLACEMENT;

	@SerializedName("Advertise_List")
	private List<AdvertiseList> advertiseList;

	@SerializedName("MORE_APP_SPLASH")
	private List<MOREAPPSPLASH> mOREAPPSPLASH;

	@SerializedName("APP_SETTINGS")
	private APPSETTINGS aPPSETTINGS;

	@SerializedName("MORE_APP_EXIT")
	private List<MOREAPPEXIT> mOREAPPEXIT=new ArrayList<>();

	public void setMSG(String mSG){
		this.mSG = mSG;
	}

	public String getMSG(){
		return mSG;
	}

	public void setEXTRADATA(String eXTRADATA){
		this.eXTRADATA = eXTRADATA;
	}

	public String getEXTRADATA(){
		return eXTRADATA;
	}

	public void setSTATUS(boolean sTATUS){
		this.sTATUS = sTATUS;
	}

	public boolean isSTATUS(){
		return sTATUS;
	}

	public void setPLACEMENT(PLACEMENT pLACEMENT){
		this.pLACEMENT = pLACEMENT;
	}

	public PLACEMENT getPLACEMENT(){
		return pLACEMENT;
	}

	public void setAdvertiseList(List<AdvertiseList> advertiseList){
		this.advertiseList = advertiseList;
	}

	public List<AdvertiseList> getAdvertiseList(){
		return advertiseList;
	}

	public void setMOREAPPSPLASH(List<MOREAPPSPLASH> mOREAPPSPLASH){
		this.mOREAPPSPLASH = mOREAPPSPLASH;
	}

	public List<MOREAPPSPLASH> getMOREAPPSPLASH(){
		return mOREAPPSPLASH;
	}

	public void setAPPSETTINGS(APPSETTINGS aPPSETTINGS){
		this.aPPSETTINGS = aPPSETTINGS;
	}

	public APPSETTINGS getAPPSETTINGS(){
		return aPPSETTINGS;
	}

	public void setMOREAPPEXIT(List<MOREAPPEXIT> mOREAPPEXIT){
		this.mOREAPPEXIT = mOREAPPEXIT;
	}

	public List<MOREAPPEXIT> getMOREAPPEXIT(){
		return mOREAPPEXIT;
	}

	@Override
 	public String toString(){
		return 
			"ResponseRoot{" + 
			"mSG = '" + mSG + '\'' + 
			",eXTRA_DATA = '" + eXTRADATA + '\'' + 
			",sTATUS = '" + sTATUS + '\'' + 
			",pLACEMENT = '" + pLACEMENT + '\'' + 
			",advertise_List = '" + advertiseList + '\'' + 
			",mORE_APP_SPLASH = '" + mOREAPPSPLASH + '\'' + 
			",aPP_SETTINGS = '" + aPPSETTINGS + '\'' + 
			",mORE_APP_EXIT = '" + mOREAPPEXIT + '\'' + 
			"}";
		}
}