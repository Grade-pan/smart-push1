package com.sed.exceptionhandle.decorated;

import com.sed.exceptionhandle.ExceptionHandler;
import com.sed.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;

public class DefaultExceptionNoticeHandler implements ExceptionNoticeHandlerDecoration{

	
	private final ExceptionHandler exceptionHandler;
	
	
	
	public DefaultExceptionNoticeHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}


	@Override
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

}
