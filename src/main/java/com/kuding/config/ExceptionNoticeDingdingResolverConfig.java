package com.kuding.config;

import com.kuding.config.interfaces.ExceptionNoticeResolverConfigure;
import com.kuding.markdown.DefaultMarkdownHttpMessageResolver;
import com.kuding.markdown.DefaultMarkdownMessageResolver;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.properties.enums.DingdingTextType;
import com.kuding.properties.enums.ListenType;
import com.kuding.text.ExceptionNoticeResolver;
import com.kuding.text.ExceptionNoticeResolverFactory;
import com.kuding.text.StandardResolverKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(ExceptionNoticeResolverFactory.class)
public class ExceptionNoticeDingdingResolverConfig implements ExceptionNoticeResolverConfigure {

    @Autowired
    private ExceptionNoticeProperty exceptionNoticeProperty;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Override
    public void addResolver(ExceptionNoticeResolverFactory exceptionNoticeResolverFactory) {
        logger.info("添加钉钉异常信息解析");
        if (exceptionNoticeProperty.getDingdingTextType() == DingdingTextType.MARKDOWN) {
            ExceptionNoticeResolver exceptionNoticeResolver = null;
            if (exceptionNoticeProperty.getListenType() == ListenType.COMMON) {
                exceptionNoticeResolver = new DefaultMarkdownMessageResolver(exceptionNoticeProperty);
            }
            if (exceptionNoticeProperty.getListenType() == ListenType.WEB_MVC) {
                exceptionNoticeResolver = new DefaultMarkdownHttpMessageResolver(exceptionNoticeProperty);
            }
            exceptionNoticeResolverFactory.addNoticeResolver(StandardResolverKey.DINGDING, exceptionNoticeResolver);
        }
    }
}
