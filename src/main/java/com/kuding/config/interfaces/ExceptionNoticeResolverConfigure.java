package com.kuding.config.interfaces;

import com.kuding.text.ExceptionNoticeResolverFactory;

@FunctionalInterface
public interface ExceptionNoticeResolverConfigure {

	public void addResolver(ExceptionNoticeResolverFactory factory);

}
