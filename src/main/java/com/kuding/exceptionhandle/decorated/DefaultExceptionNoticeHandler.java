package com.kuding.exceptionhandle.decorated;

import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;

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
