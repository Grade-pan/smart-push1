package com.kuding.config;

import com.kuding.config.interfaces.ExceptionSendComponentConfigure;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.kuding.message.EmailNoticeSendComponent;
import com.kuding.properties.EmailExceptionNoticeProperty;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.text.ExceptionNoticeResolverFactory;
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
            logger.info("注册邮件人员信息{}", emails);
        }

    }
}
