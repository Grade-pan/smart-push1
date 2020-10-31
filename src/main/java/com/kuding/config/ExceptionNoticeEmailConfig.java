package com.kuding.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import com.kuding.config.interfaces.ExceptionSendComponentConfigure;
import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.message.EmailNoticeSendComponent;
import com.kuding.properties.EmailExceptionNoticeProperty;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.text.ExceptionNoticeResolverFactory;

@Configuration
@AutoConfigureAfter({ MailSenderAutoConfiguration.class })
@ConditionalOnBean({ MailSender.class, ExceptionHandler.class, ExceptionNoticeResolverFactory.class })
public class ExceptionNoticeEmailConfig implements ExceptionSendComponentConfigure {

	@Autowired
	private MailSender mailSender;
	@Autowired
	private MailProperties mailProperties;
	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;
	@Autowired
	private ExceptionNoticeResolverFactory exceptionNoticeResolverFactory;

	@Override
	public void addSendComponent(ExceptionHandler exceptionHandler) {
		Map<String, EmailExceptionNoticeProperty> emails = exceptionNoticeProperty.getEmail();
		if (emails != null && emails.size() > 0) {
			EmailNoticeSendComponent component = new EmailNoticeSendComponent(mailSender, mailProperties, emails,
					exceptionNoticeResolverFactory);
			exceptionHandler.registerNoticeSendComponent(component);
		}
	}
}
