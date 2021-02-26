package com.sed.config;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.sed.config.interfaces.ExceptionSendComponentConfigure;
import com.sed.exceptionhandle.ExceptionHandler;
import com.sed.httpclient.DingdingHttpClient;
import com.sed.message.DingDingNoticeSendComponent;
import com.sed.properties.DingDingExceptionNoticeProperty;
import com.sed.properties.ExceptionNoticeProperty;
import com.sed.text.ExceptionNoticeResolverFactory;

@Configuration
@ConditionalOnProperty(name = "exceptionnotice.open-notice", havingValue = "true", matchIfMissing = true)
public class ExceptionNoticeDingdingSendingConfig implements ExceptionSendComponentConfigure {

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@Autowired
	private DingdingHttpClient dingdingHttpClient;

	@Autowired
	private ExceptionNoticeResolverFactory exceptionNoticeResolverFactory;

	private final Log logger = LogFactory.getLog(ExceptionNoticeDingdingSendingConfig.class);

	@Override
	public void addSendComponent(ExceptionHandler exceptionHandler) {
		logger.debug("注册钉钉通知");
		Map<String, DingDingExceptionNoticeProperty> map = exceptionNoticeProperty.getDingding();
		DingDingNoticeSendComponent component = new DingDingNoticeSendComponent(dingdingHttpClient,
				exceptionNoticeProperty, map, exceptionNoticeResolverFactory);
		exceptionHandler.registerNoticeSendComponent(component);
	}
}
