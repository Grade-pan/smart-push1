package com.kuding.web;

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

import com.kuding.anno.ExceptionListener;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.kuding.properties.ExceptionNoticeProperty;

public class ExceptionHttpNoticeResolver implements HandlerExceptionResolver {

	private final ExceptionNoticeHandlerDecoration exceptionHandler;

	private final ExceptionNoticeProperty exceptionNoticeProperty;

	private final CurrentRequetBodyResolver currentRequetBodyResolver;

	private final CurrentRequestHeaderResolver currentRequestHeaderResolver;

	private final Log logger = LogFactory.getLog(ExceptionHttpNoticeResolver.class);

	public ExceptionHttpNoticeResolver(ExceptionNoticeHandlerDecoration exceptionHandler,
									   CurrentRequetBodyResolver currentRequetBodyResolver,
									   CurrentRequestHeaderResolver currentRequestHeaderResolver,
									   ExceptionNoticeProperty exceptionNoticeProperty) {
		this.exceptionHandler = exceptionHandler;
		this.currentRequestHeaderResolver = currentRequestHeaderResolver;
		this.currentRequetBodyResolver = currentRequetBodyResolver;
		this.exceptionNoticeProperty = exceptionNoticeProperty;
	}






	public Map<String, String> getParames(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		request.getParameterMap().forEach((x, y) -> map.put(x, String.join(" , ", Arrays.asList(y))));
		return map;
	}

	public String getRequestBody() {
		return currentRequetBodyResolver.getRequestBody();
	}

	public Map<String, String> getHeader(HttpServletRequest request) {
		logger.info(exceptionNoticeProperty);
		return currentRequestHeaderResolver.headers(request, exceptionNoticeProperty.getIncludeHeaderName());
	}


	@Override
	public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
		return null;
	}
}
