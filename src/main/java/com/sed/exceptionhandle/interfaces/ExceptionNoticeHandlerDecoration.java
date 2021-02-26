package com.sed.exceptionhandle.interfaces;

import java.util.Map;

import com.sed.exceptionhandle.ExceptionHandler;
/**
 * 异常信息处理装饰器
* */
public interface ExceptionNoticeHandlerDecoration {


	public ExceptionHandler getExceptionHandler();
	 public void createNotice(String blamedFor, RuntimeException exception);

	/**
	 * 反射方式获取方法中出现的异常进行的通知
	 * 提供默认实现
	 * @param blamedFor 谁背锅？
	 * @param ex        异常信息
	 * @param method    方法名
	 * @param args      参数信息
	 * @return
	 */
	 public void createNotice(String blamedFor, RuntimeException ex, String method, Object[] args);

	/**
	 * 创建一个http请求异常的通知
	 * 
	 * @param blamedFor
	 * @param exception
	 * @param url
	 * @param param
	 * @param requesBody
	 * @param headers
	 * @return
	 */
	 public void createHttpNotice(String blamedFor, RuntimeException exception, String url,
			Map<String, String> param, String requesBody, Map<String, String> headers);

	 public boolean check();
}
