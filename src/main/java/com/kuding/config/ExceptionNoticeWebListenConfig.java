package com.kuding.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.web.ClearBodyInterceptor;
import com.kuding.web.CurrentRequestHeaderResolver;
import com.kuding.web.CurrentRequetBodyResolver;
import com.kuding.web.DefaultRequestBodyResolver;
import com.kuding.web.DefaultRequestHeaderResolver;
import com.kuding.web.ExceptionHttpNoticeResolver;

@Configuration
@AutoConfigureAfter({ ExceptionNoticeDecorationConfig.class })
@ConditionalOnClass({ WebMvcConfigurer.class, RequestBodyAdvice.class, RequestMappingHandlerAdapter.class,
		ExceptionNoticeHandlerDecoration.class, ExceptionNoticeProperty.class})
public class ExceptionNoticeWebListenConfig  {



	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	@Bean
	public ExceptionHttpNoticeResolver exceptionNoticeResolver(ExceptionNoticeHandlerDecoration exceptionNoticeHandlerDecoration,ExceptionNoticeProperty exceptionNoticeProperty){
		logger.info("添加ExceptionHttpNoticeResolver");
		ExceptionHttpNoticeResolver exceptionNoticeResolver = new ExceptionHttpNoticeResolver( exceptionNoticeHandlerDecoration,
				currentRequetBodyResolver(), currentRequestHeaderResolver(), exceptionNoticeProperty);
		return exceptionNoticeResolver;
	}

	@Bean
	public ClearBodyInterceptor clearBodyInterceptor() {
		ClearBodyInterceptor bodyInterceptor = new ClearBodyInterceptor(currentRequetBodyResolver());
		return bodyInterceptor;

	}
	@Bean
	@ConditionalOnMissingBean(value = CurrentRequestHeaderResolver.class)
	public CurrentRequestHeaderResolver currentRequestHeaderResolver() {
		return new DefaultRequestHeaderResolver();
	}

	@Bean
	public CurrentRequetBodyResolver currentRequetBodyResolver() {
		return new DefaultRequestBodyResolver();
	}



}
