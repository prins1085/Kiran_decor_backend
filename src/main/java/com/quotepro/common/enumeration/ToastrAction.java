package com.quotepro.common.enumeration;

public enum ToastrAction {

	INFO("info"),

	SUCCESS("success"),

	WARNING("warning"),

	ERROR("error");

	private String value;

	ToastrAction(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}