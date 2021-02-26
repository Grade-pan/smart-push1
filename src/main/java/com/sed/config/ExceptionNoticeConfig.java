package com.sed.config;

import com.sed.exceptionhandle.ExceptionHandler;
import com.sed.httpclient.DefaultDingdingHttpClient;
import com.sed.httpclient.DingdingHttpClient;
import com.sed.properties.ExceptionNoticeFrequencyStrategy;
import com.sed.properties.ExceptionNoticeProperty;
import com.sed.text.ExceptionNoticeResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties({ ExceptionNoticeProperty.class, ExceptionNoticeFrequencyStrategy.class })
@ConditionalOnProperty(name = "exceptionnotice.open-notice", havingValue = "true", matchIfMissing = true)
@EnableScheduling
public class ExceptionNoticeConfig {

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@Autowired
	private ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy;

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	@Bean
	@ConditionalOnMissingBean
	public ExceptionHandler exceptionHandler() {
		ExceptionHandler exceptionHandler = new ExceptionHandler(exceptionNoticeProperty,
				exceptionNoticeFrequencyStrategy);
		logger.info("创建成功核心组件成功:ExceptionHandler");
		return exceptionHandler;
	}

	@Bean
	@ConditionalOnMissingBean
	public ExceptionNoticeResolverFactory exceptionNoticeResolverFactory() {
		ExceptionNoticeResolverFactory exceptionNoticeResolverFactory = new ExceptionNoticeResolverFactory();
		logger.info("创建resolverFactory工厂成功");
		return exceptionNoticeResolverFactory;
	}

	@Bean
	@ConditionalOnMissingBean
	public DingdingHttpClient dingdingHttpClient() {
		RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(20)).build();
		DingdingHttpClient dingdingHttpClient = new DefaultDingdingHttpClient(restTemplate);
		return dingdingHttpClient;
	}


}
