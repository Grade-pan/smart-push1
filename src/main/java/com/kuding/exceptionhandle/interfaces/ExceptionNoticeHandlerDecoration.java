package com.kuding.exceptionhandle.interfaces;

import java.util.Map;

import com.kuding.exceptionhandle.ExceptionHandler;
/**
 * 异常信息处理装饰器
* */
public interface ExceptionNoticeHandlerDecoration {
     /**由实现类来完成ExceptionHandler对象
     * */
	public ExceptionHandler getExceptionHandler();

	/**
	 * 最基础的异常通知的创建方法
	 * 提供默认实现
	 * @param blamedFor 谁背锅？
	 * @param exception 异常信息
	 * 
	 * @return
	 */

	default public void createNotice(String blamedFor, RuntimeException exception) {
		getExceptionHandler().createNotice(blamedFor, exception);
	}

	/**
	 * 反射方式获取方法中出现的异常进行的通知
	 * 提供默认实现
	 * @param blamedFor 谁背锅？
	 * @param ex        异常信息
	 * @param method    方法名
	 * @param args      参数信息
	 * @return
	 */
	default public void createNotice(String blamedFor, RuntimeException ex, String method, Object[] args) {
		getExceptionHandler().createNotice(blamedFor, ex, method, args);
	}

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
	default public void createHttpNotice(String blamedFor, RuntimeException exception, String url,
			Map<String, String> param, String requesBody, Map<String, String> headers) {
		getExceptionHandler().createHttpNotice(blamedFor, exception, url, param, requesBody, headers);
	}

	default public boolean check() {
		return getExceptionHandler().getBlameMap().size() == 0;
	}
}
