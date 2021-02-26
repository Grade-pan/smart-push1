package com.sed.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sed.anno.ExceptionListener;
import com.sed.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.sed.properties.ExceptionNoticeProperty;

public class ExceptionNoticeResolver implements HandlerExceptionResolver {

	private final ExceptionNoticeHandlerDecoration exceptionHandler;

	private final ExceptionNoticeProperty exceptionNoticeProperty;

	private final CurrentRequetBodyResolver currentRequetBodyResolver;

	private final CurrentRequestHeaderResolver currentRequestHeaderResolver;

	private final Log logger = LogFactory.getLog(ExceptionNoticeResolver.class);

	public ExceptionNoticeResolver(ExceptionNoticeHandlerDecoration exceptionHandler,
			CurrentRequetBodyResolver currentRequetBodyResolver,
			CurrentRequestHeaderResolver currentRequestHeaderResolver,
			ExceptionNoticeProperty exceptionNoticeProperty) {
		this.exceptionHandler = exceptionHandler;
		this.currentRequestHeaderResolver = currentRequestHeaderResolver;
		this.currentRequetBodyResolver = currentRequetBodyResolver;
		this.exceptionNoticeProperty = exceptionNoticeProperty;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error("出现异常：", ex);
		RuntimeException e = null;
		if (ex instanceof RuntimeException)
			e = (RuntimeException) ex;
		HandlerMethod handlerMethod = null;
		logger.debug("handler:" + handler);
		if (handler instanceof HandlerMethod)
			handlerMethod = (HandlerMethod) handler;
		ExceptionListener listener = handlerMethod == null ? null : getListener(handlerMethod);
		if (listener != null && e != null && handler != null) {
			exceptionHandler.createHttpNotice(listener.value(), e, request.getRequestURI(), getParames(request),
					getRequestBody(), getHeader(request));
		}
		return null;
	}

	private Map<String, String> getParames(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		request.getParameterMap().forEach((x, y) -> map.put(x, String.join(" , ", Arrays.asList(y))));
		return map;
	}

	private String getRequestBody() {
		return currentRequetBodyResolver.getRequestBody();
	}

	private Map<String, String> getHeader(HttpServletRequest request) {
		return currentRequestHeaderResolver.headers(request, exceptionNoticeProperty.getIncludeHeaderName());
	}

	private ExceptionListener getListener(HandlerMethod handlerMethod) {
		ExceptionListener listener = handlerMethod.getMethodAnnotation(ExceptionListener.class);
		if (listener == null)
			listener = handlerMethod.getBeanType().getAnnotation(ExceptionListener.class);
		return listener;
	}

}
