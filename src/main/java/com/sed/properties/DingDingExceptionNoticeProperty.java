package com.sed.properties;

import java.util.Arrays;

public class DingDingExceptionNoticeProperty {

	/**
	 * 电话信息
	 */
	private String[] phoneNum;

	/**
	 * 钉钉机器人web钩子
	 */
	private String webHook;

	/**
	 * 是否开启验签
	 */
	private boolean enableSignatureCheck;

	/**
	 * 验签秘钥
	 */
	private String signSecret;

	/**
	 * @return the phoneNum
	 */
	public String[] getPhoneNum() {
		return phoneNum;
	}

	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String[] phoneNum) {
		this.phoneNum = phoneNum;
	}

	/**
	 * @return the webHook
	 */
	public String getWebHook() {
		return webHook;
	}

	/**
	 * @param webHook the webHook to set
	 */
	public void setWebHook(String webHook) {
		this.webHook = webHook;
	}

	public boolean isEnableSignatureCheck() {
		return enableSignatureCheck;
	}

	public void setEnableSignatureCheck(boolean enableSignatureCheck) {
		this.enableSignatureCheck = enableSignatureCheck;
	}

	public String getSignSecret() {
		return signSecret;
	}

	public void setSignSecret(String signSecret) {
		this.signSecret = signSecret;
	}

	@Override
	public String toString() {
		return "DingDingExceptionNoticeProperty [phoneNum=" + Arrays.toString(phoneNum) + ", webHook=" + webHook
				+ ", enableSignatureCheck=" + enableSignatureCheck + ", signSecret=" + signSecret + "]";
	}

}
