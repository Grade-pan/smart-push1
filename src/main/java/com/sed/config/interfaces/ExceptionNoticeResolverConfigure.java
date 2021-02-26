package com.sed.config.interfaces;

import com.sed.text.ExceptionNoticeResolverFactory;

@FunctionalInterface
public interface ExceptionNoticeResolverConfigure {

	public void addResolver(ExceptionNoticeResolverFactory factory);

}
