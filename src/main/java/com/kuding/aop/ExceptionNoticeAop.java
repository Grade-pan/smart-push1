package com.kuding.aop;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import com.kuding.anno.ExceptionListener;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;

@Aspect
public class ExceptionNoticeAop {

	private ExceptionNoticeHandlerDecoration exceptionHandler;

	private final Log logger = LogFactory.getLog(getClass());

	public ExceptionNoticeAop(ExceptionNoticeHandlerDecoration exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@AfterThrowing(value = "@within(listener)", throwing = "e", argNames = "listener,e")
	public void exceptionNotice(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
		handleException(listener.value(), e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	@AfterThrowing(value = "@annotation(listener)", throwing = "e", argNames = "listener,e")
	public void exceptionNoticeWithMethod(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
		handleException(listener.value(), e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	private void handleException(String blameFor, RuntimeException exception, String methodName, Object[] args) {
		logger.debug("出现异常：" + methodName
				+ String.join(",", Arrays.stream(args).map(x -> x.toString()).toArray(String[]::new)));
		exceptionHandler.createNotice(blameFor, exception, methodName, args);
	}
}
