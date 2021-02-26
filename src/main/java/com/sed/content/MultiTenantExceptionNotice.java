//package com.kuding.content;
//
//import java.util.Map;
//
//public class MultiTenantExceptionNotice extends HttpExceptionNotice {
//
//	public MultiTenantExceptionNotice(RuntimeException exception, String filter, String url, Map<String, String> param,
//			String requestBody, Map<String, String> headers, String tenantId) {
//		super(exception, filter, url, param, requestBody, headers);
//		this.tenantId = tenantId;
//	}
//
//	private String tenantId;
//
//	/**
//	 * @return the tenantId
//	 */
//	public String getTenantId() {
//		return tenantId;
//	}
//
//	/**
//	 * @param tenantId the tenantId to set
//	 */
//	public void setTenantId(String tenantId) {
//		this.tenantId = tenantId;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.kuding.content.HttpExceptionNotice#createText()
//	 */
//	@Override
//	public String createText() {
//		return String.format("租户信息：%s\r\n%s", tenantId, super.createText());
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		return "MultiTenantExceptionNotice [tenantId=" + tenantId + ", url=" + url + ", paramInfo=" + paramInfo
//				+ ", requestBody=" + requestBody + ", project=" + project + ", uid=" + uid + ", methodName="
//				+ methodName + ", parames=" + parames + ", classPath=" + classPath + ", exceptionMessage="
//				+ exceptionMessage + ", traceInfo=" + traceInfo + "]";
//	}
//
//}
