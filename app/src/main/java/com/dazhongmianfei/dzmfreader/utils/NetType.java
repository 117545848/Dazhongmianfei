package com.dazhongmianfei.dzmfreader.utils;

/**
 * 当前网络类型
 */
public enum NetType {
	TYPE_WIFI("wifi"), TYPE_2G("2G"), TYPE_3G("3G"), TYPE_4G("4G"), TYPE_UNKNOWN("未知网络"), TYPE_NONE("无可用网络");

	private String desc;

	NetType(String desc) {
		this.desc = desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
