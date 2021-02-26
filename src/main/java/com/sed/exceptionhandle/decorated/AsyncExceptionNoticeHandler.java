package com.sed.exceptionhandle.decorated;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.sed.exceptionhandle.ExceptionHandler;
import com.sed.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.sed.exceptions.PrometheusException;

public class AsyncExceptionNoticeHandler implements ExceptionNoticeHandlerDecoration, InitializingBean {

	private final ExceptionHandler exceptionHandler;

	private final ThreadPoolTaskExecutor poolTaskExecutor;

	private final Log logger = LogFactory.getLog(getClass());

	public AsyncExceptionNoticeHandler(ExceptionHandler exceptionHandler, ThreadPoolTaskExecutor poolTaskExecutor) {
		this.exceptionHandler = exceptionHandler;
		this.poolTaskExecutor = poolTaskExecutor;
	}

	@Override
	public void createNotice(String blamedFor, RuntimeException exception) {
		poolTaskExecutor.execute(new createNoticeRunnable(blamedFor, exception, null, null));
	}

	@Override
	public void createNotice(String blamedFor, RuntimeException ex, String method, Object[] args) {
		poolTaskExecutor.execute(new createNoticeRunnable(blamedFor, ex, method, args));
	}

	@Override
	public void createHttpNotice(String blamedFor, RuntimeException exception, String url, Map<String, String> param,
			String requesBody, Map<String, String> headers) {
		poolTaskExecutor.execute(new CreateHttpNoticeRunnable(blamedFor, exception, url, param, requesBody, headers));
	}

	class createNoticeRunnable implements Runnable {

		private String blamedFor;

		private RuntimeException exception;

		private String method;

		private Object[] args;

		public createNoticeRunnable(String blamedFor, RuntimeException exception, String method, Object[] args) {
			this.blamedFor = blamedFor;
			this.exception = exception;
			this.method = method;
			this.args = args;
		}

		@Override
		public void run() {
			try {
//				TimeUnit.SECONDS.sleep(10);
				getExceptionHandler().createNotice(blamedFor, exception, method, args);
			} catch (Exception e) {
				logger.warn("异常通知出错：", e);
			}
		}
	}

	class CreateHttpNoticeRunnable implements Runnable {

		private String blamedFor;
		private RuntimeException exception;
		private String url;
		private Map<String, String> param;
		private String requesBody;
		private Map<String, String> headers;

		public CreateHttpNoticeRunnable(String blamedFor, RuntimeException exception, String url,
				Map<String, String> param, String requesBody, Map<String, String> headers) {
			this.blamedFor = blamedFor;
			this.exception = exception;
			this.url = url;
			this.param = param;
			this.requesBody = requesBody;
			this.headers = headers;
		}

		@Override
		public void run() {
			try {
//				TimeUnit.SECONDS.sleep(10);
				getExceptionHandler().createHttpNotice(blamedFor, exception, url, param, requesBody, headers);
			} catch (Exception e) {
				logger.warn("异常通知出错：", e);
			}
		}
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (check())
			throw new PrometheusException("不存在异常通知的背锅侠，请设置后再试！！");
		getExceptionHandler().getBlameMap().forEach((x, y) -> logger.debug(x + "-->" + y));
	}
}
