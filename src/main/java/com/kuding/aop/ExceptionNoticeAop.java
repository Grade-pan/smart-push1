package com.kuding.aop;

import java.util.Arrays;


import com.kuding.properties.ExceptionNoticeProperty;

import com.kuding.properties.enums.ListenType;
import com.kuding.text.ExceptionNoticeResolver;
import com.kuding.web.ExceptionHttpNoticeResolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import com.kuding.anno.ExceptionListener;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class ExceptionNoticeAop {

	private ExceptionNoticeHandlerDecoration exceptionHandler;
    private ExceptionHttpNoticeResolver exceptionHttpNoticeResolver;
	private ExceptionNoticeProperty exceptionNoticeProperty;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ExceptionNoticeAop(ExceptionNoticeHandlerDecoration exceptionHandler, ExceptionNoticeProperty exceptionNoticeProperty, ExceptionHttpNoticeResolver exceptionHttpNoticeResolver) {
		this.exceptionHandler = exceptionHandler;
		this.exceptionNoticeProperty = exceptionNoticeProperty;
		this.exceptionHttpNoticeResolver = exceptionHttpNoticeResolver;
	}

	@AfterThrowing(value = "@within(listener)", throwing = "e", argNames = "listener,e")
	public void exceptionNoticeWithClass(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
		logger.info("进入AOP的exceptionNoticeClass");
		if(exceptionNoticeProperty.getListenType() != ListenType.WEB_MVC) {
			handleException(listener.value(), e, joinPoint.getSignature().getName(), joinPoint.getArgs());
		}
		else {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			exceptionHandler.createHttpNotice(listener.value(), e, request.getRequestURI(), exceptionHttpNoticeResolver.getParames(request),
					exceptionHttpNoticeResolver.getRequestBody(), exceptionHttpNoticeResolver.getHeader(request));
		}
	}

	@AfterThrowing(value = "@annotation(listener)", throwing = "e", argNames = "listener,e")
	public void exceptionNoticeWithMethod(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
		logger.info("进入AOP的exceptionNoticeWithMethod");
		if(exceptionNoticeProperty.getListenType() != ListenType.WEB_MVC) {
			handleException(listener.value(), e, joinPoint.getSignature().getName(), joinPoint.getArgs());
		}
		else {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			exceptionHandler.createHttpNotice(listener.value(), e, request.getRequestURI(), exceptionHttpNoticeResolver.getParames(request),
					exceptionHttpNoticeResolver.getRequestBody(), exceptionHttpNoticeResolver.getHeader(request));
		}
	}

	private void handleException(String blameFor, RuntimeException exception, String methodName, Object[] args) {
			exceptionHandler.createNotice(blameFor, exception, methodName, args);
	}
}
