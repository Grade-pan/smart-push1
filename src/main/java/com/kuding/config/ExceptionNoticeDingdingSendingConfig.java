package com.kuding.config;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.kuding.config.interfaces.ExceptionSendComponentConfigure;
import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.httpclient.DingdingHttpClient;
import com.kuding.message.DingDingNoticeSendComponent;
import com.kuding.properties.DingDingExceptionNoticeProperty;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.text.ExceptionNoticeResolverFactory;

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
