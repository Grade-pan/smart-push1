package com.kuding.exceptionhandle.decorated;

import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;

import java.util.Map;

public class DefaultExceptionNoticeHandler implements ExceptionNoticeHandlerDecoration {

    private final ExceptionHandler exceptionHandler;


    public DefaultExceptionNoticeHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }


    @Override
    public void createNotice(String blamedFor, RuntimeException exception) {
        getExceptionHandler().createNotice(blamedFor, exception);
    }

    @Override
    public void createNotice(String blamedFor, RuntimeException ex, String method, Object[] args) {
        getExceptionHandler().createNotice(blamedFor, ex, method, args);
    }

    @Override
    public void createHttpNotice(String blamedFor, RuntimeException exception, String url, Map<String, String> param, String requesBody, Map<String, String> headers) {
        getExceptionHandler().createHttpNotice(blamedFor, exception, url, param, requesBody, headers);
    }

    @Override
    public boolean check() {
        return getExceptionHandler().getBlameMap().size() == 0;
    }
}
