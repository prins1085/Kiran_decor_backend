package com.quotepro.common.utils;

import java.io.File;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceBean {
	String M;
	String I1;
	String I2;
	String I3;
	String I4;
	String I5;
	String T1;
	String T2;
	String T3;
	String T4;
	String T5;
	File f;
	String B1;
	String B2;
	String B3;
	String B4;
	String B5;
	String BT1;
	String BT2;
	String R1;
	String S;
	String V;
	String request;

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getV() {
		return V;
	}

	public void setV(String v) {
		V = v;
	}

	public String getS() {
		return S;
	}

	public void setS(String S) {
		this.S = S;
	}

	public String getB1() {
		return B1;
	}

	public void setB1(String B1) {
		this.B1 = B1;
	}

	public String getB2() {
		return B2;
	}

	public void setB2(String B2) {
		this.B2 = B2;
	}

	public String getB3() {
		return B3;
	}

	public void setB3(String B3) {
		this.B3 = B3;
	}

	public String getB4() {
		return B4;
	}

	public void setB4(String B4) {
		this.B4 = B4;
	}

	public String getB5() {
		return B5;
	}

	public void setB5(String B5) {
		this.B5 = B5;
	}

	public String getBT1() {
		return BT1;
	}

	public void setBT1(String BT1) {
		this.BT1 = BT1;
	}

	public String getBT2() {
		return BT2;
	}

	public void setBT2(String BT2) {
		this.BT2 = BT2;
	}

	public String getBT3() {
		return BT3;
	}

	public void setBT3(String BT3) {
		this.BT3 = BT3;
	}

	public String getBT4() {
		return BT4;
	}

	public void setBT4(String BT4) {
		this.BT4 = BT4;
	}

	public String getBT5() {
		return BT5;
	}

	public void setBT5(String BT5) {
		this.BT5 = BT5;
	}

	String BT3;
	String BT4;
	String BT5;

	public File getF() {
		return f;
	}

	public String getT1() {
		return T1;
	}

	public void setT1(String T1) {
		this.T1 = T1;
	}

	public String getT2() {
		return T2;
	}

	public void setT2(String T2) {
		this.T2 = T2;
	}

	public String getT3() {
		return T3;
	}

	public void setT3(String T3) {
		this.T3 = T3;
	}

	public String getT4() {
		return T4;
	}

	public void setT4(String T4) {
		this.T4 = T4;
	}

	public String getT5() {
		return T5;
	}

	public void setT5(String T5) {
		this.T5 = T5;
	}

	public void setF(File f) {
		this.f = f;
	}

	public String getM() {
		return M;
	}

	public void setM(String M) {
		this.M = M;
	}

	public String getI5() {
		return I5;
	}

	public void setI5(String I5) {
		this.I5 = I5;
	}

	public String getI1() {
		return I1;
	}

	public void setI1(String I1) {
		this.I1 = I1;
	}

	public String getI2() {
		return I2;
	}

	public void setI2(String I2) {
		this.I2 = I2;
	}

	public String getI3() {
		return I3;
	}

	public void setI3(String I3) {
		this.I3 = I3;
	}

	public String getI4() {
		return I4;
	}

	public void setI4(String I4) {
		this.I4 = I4;
	}

	public String getR1() {
		return R1;
	}

	public void setR1(String R1) {
		this.R1 = R1;
	}

	public String toJson() throws JsonProcessingException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public static ServiceBean fromJson(String json) throws Exception {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json, ServiceBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
