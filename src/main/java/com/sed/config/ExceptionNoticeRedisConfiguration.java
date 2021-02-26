package com.sed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.sed.exceptionhandle.ExceptionHandler;
import com.sed.properties.ExceptionNoticeProperty;
import com.sed.redis.ExceptionRedisStorageComponent;

@Configuration
@ConditionalOnClass({ StringRedisTemplate.class })
@ConditionalOnProperty(name = "exceptionnotice.enable-redis-storage", havingValue = "true")
@ConditionalOnMissingBean(value = { ExceptionRedisStorageComponent.class })
@ConditionalOnBean({ ExceptionHandler.class })
@AutoConfigureAfter({ ExceptionNoticeConfig.class })
public class ExceptionNoticeRedisConfiguration {

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@Bean
	public ExceptionRedisStorageComponent exceptionRedisStorageComponent(StringRedisTemplate stringRedisTemplate,
			Gson gson, ExceptionHandler exceptionHandler) {
		ExceptionRedisStorageComponent exceptionRedisStorageComponent = new ExceptionRedisStorageComponent(
				exceptionNoticeProperty, stringRedisTemplate, gson);
		exceptionHandler.setExceptionRedisStorageComponent(exceptionRedisStorageComponent);
		return exceptionRedisStorageComponent;
	}

}
