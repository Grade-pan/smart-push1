package com.kuding.config;

import com.kuding.config.interfaces.ExceptionSendComponentConfigure;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.kuding.httpclient.DingdingHttpClient;
import com.kuding.message.DingDingNoticeSendComponent;
import com.kuding.properties.DingDingExceptionNoticeProperty;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.text.ExceptionNoticeResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
@ConditionalOnProperty(name = "exceptionnotice.notice", havingValue = "true", matchIfMissing = true)
public class ExceptionNoticeDingdingSendingConfig implements ExceptionSendComponentConfigure {


    @Autowired
    private ExceptionNoticeProperty exceptionNoticeProperty;

    @Autowired
    private DingdingHttpClient dingdingHttpClient;

    @Autowired
    private ExceptionNoticeResolverFactory exceptionNoticeResolverFactory;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Override
    public void addSendComponent(ExceptionNoticeHandlerDecoration exceptionNoticeHandlerDecoration, MailProperties mailProperties) {

        Map<String, DingDingExceptionNoticeProperty> map = exceptionNoticeProperty.getDingding();
        if (map != null) {
            DingDingNoticeSendComponent component = new DingDingNoticeSendComponent(dingdingHttpClient,
                    exceptionNoticeProperty, map, exceptionNoticeResolverFactory);
            exceptionNoticeHandlerDecoration.getExceptionHandler().registerNoticeSendComponent(component);
            logger.info("注册钉钉人员信息{}", component);
        }
    }
}
