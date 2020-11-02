package com.kuding.config;

import com.kuding.aop.ExceptionNoticeAop;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.web.ExceptionHttpNoticeHandle;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({ExceptionNoticeWebListenConfig.class})
@ConditionalOnBean({ExceptionNoticeHandlerDecoration.class, ExceptionNoticeProperty.class, ExceptionHttpNoticeHandle.class})
public class ExceptionNoticeAOPConfig {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Bean
    @ConditionalOnMissingBean
    public ExceptionNoticeAop exceptionNoticeAop(ExceptionNoticeHandlerDecoration exceptionNoticeHandlerDecoration, ExceptionNoticeProperty ex, ExceptionHttpNoticeHandle exceptionHttpNoticeHandle) {
        ExceptionNoticeAop aop = new ExceptionNoticeAop(exceptionNoticeHandlerDecoration, ex, exceptionHttpNoticeHandle);
        logger.info("创建AOP异常监听切面成功");
        return aop;
    }
}
