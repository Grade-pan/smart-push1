package com.sed.config;

import com.sed.config.interfaces.ExceptionSendComponentConfigure;
import com.sed.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.sed.message.EmailNoticeSendComponent;
import com.sed.properties.EmailExceptionNoticeProperty;
import com.sed.properties.ExceptionNoticeProperty;
import com.sed.text.ExceptionNoticeResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;

@Configuration
@AutoConfigureAfter({MailSenderAutoConfiguration.class})
@EnableConfigurationProperties(MailProperties.class)
@ConditionalOnClass({ExceptionNoticeHandlerDecoration.class, MailProperties.class})

public class ExceptionNoticeEmailConfig implements ExceptionSendComponentConfigure {

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private ExceptionNoticeProperty exceptionNoticeProperty;
    @Autowired
    private ExceptionNoticeResolverFactory exceptionNoticeResolverFactory;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Override
    public void addSendComponent(ExceptionNoticeHandlerDecoration exceptionNoticeHandlerDecoration, MailProperties mailProperties) {
        Map<String, EmailExceptionNoticeProperty> emails = exceptionNoticeProperty.getEmail();
        if (emails != null && emails.size() > 0) {

            EmailNoticeSendComponent component = new EmailNoticeSendComponent(mailSender, mailProperties, emails,
                    exceptionNoticeResolverFactory);
            exceptionNoticeHandlerDecoration.getExceptionHandler().registerNoticeSendComponent(component);
            logger.info("????????????????????????{}", emails);
        }

    }
}
