package com.quotepro.common.utils;

public class LoginBean {
	private String txtusername;
	private String txtpassword;
	private String txtdeviceid;
	private String fcmid;
	private String ip;
	private Boolean isQr = false;

	public String getFcmid() {
		return fcmid;
	}

	public void setFcmid(String fcmid) {
		this.fcmid = fcmid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTxtusername() {
		return txtusername;
	}

	public void setTxtusername(String txtusername) {
		this.txtusername = txtusername;
	}

	public String getTxtpassword() {
		return txtpassword;
	}

	public void setTxtpassword(String txtpassword) {
		this.txtpassword = txtpassword;
	}

	public String getTxtdeviceid() {
		return txtdeviceid;
	}

	public void setTxtdeviceid(String txtdeviceid) {
		this.txtdeviceid = txtdeviceid;
	}

	public Boolean getIsQr() {
		return isQr;
	}

	public void setIsQr(Boolean isQr) {
		this.isQr = isQr;
	}

//	public String getIsQr() {
//		return isQr;
//	}
//
//	public void setIsQr(String isQr) {
//		this.isQr = isQr;
//	}

}
