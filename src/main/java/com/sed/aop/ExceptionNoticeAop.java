package com.sed.aop;

import com.sed.anno.ExceptionListener;
import com.sed.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.sed.properties.ExceptionNoticeProperty;
import com.sed.properties.enums.ListenType;
import com.sed.web.ExceptionHttpNoticeHandle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class ExceptionNoticeAop {

    private ExceptionNoticeHandlerDecoration exceptionHandler;
    private ExceptionHttpNoticeHandle exceptionHttpNoticeHandle;
    private ExceptionNoticeProperty exceptionNoticeProperty;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ExceptionNoticeAop(ExceptionNoticeHandlerDecoration exceptionHandler, ExceptionNoticeProperty exceptionNoticeProperty, ExceptionHttpNoticeHandle exceptionHttpNoticeHandle) {
        this.exceptionHandler = exceptionHandler;
        this.exceptionNoticeProperty = exceptionNoticeProperty;
        this.exceptionHttpNoticeHandle = exceptionHttpNoticeHandle;
    }

    @AfterThrowing(value = "@within(listener)", throwing = "e", argNames = "listener,e")
    public void exceptionNoticeWithClass(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
        logger.info("进入AOP的exceptionNoticeClass");
        if (exceptionNoticeProperty.getListenType() != ListenType.WEB_MVC) {
            handleException(listener.value(), e, joinPoint.getSignature().getName(), joinPoint.getArgs());
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            exceptionHandler.createHttpNotice(listener.value(), e, request.getRequestURI(), exceptionHttpNoticeHandle.getParames(request),
                    exceptionHttpNoticeHandle.getRequestBody(), exceptionHttpNoticeHandle.getHeader(request));
        }
    }

    @AfterThrowing(value = "@annotation(listener)", throwing = "e", argNames = "listener,e")
    public void exceptionNoticeWithMethod(JoinPoint joinPoint, ExceptionListener listener, RuntimeException e) {
        logger.info("进入AOP的exceptionNoticeWithMethod");
        if (exceptionNoticeProperty.getListenType() != ListenType.WEB_MVC) {
            handleException(listener.value(), e, joinPoint.getSignature().getName(), joinPoint.getArgs());
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            exceptionHandler.createHttpNotice(listener.value(), e, request.getRequestURI(), exceptionHttpNoticeHandle.getParames(request),
                    exceptionHttpNoticeHandle.getRequestBody(), exceptionHttpNoticeHandle.getHeader(request));
        }
    }

    private void handleException(String blameFor, RuntimeException exception, String methodName, Object[] args) {
        exceptionHandler.createNotice(blameFor, exception, methodName, args);
    }
}
