package com.kuding.config;

import com.kuding.config.interfaces.ExceptionNoticeResolverConfigure;
import com.kuding.config.interfaces.ExceptionSendComponentConfigure;
import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.exceptionhandle.decorated.AsyncExceptionNoticeHandler;
import com.kuding.exceptionhandle.decorated.DefaultExceptionNoticeHandler;
import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import com.kuding.properties.ExceptionNoticeAsyncProperties;
import com.kuding.text.ExceptionNoticeResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

@Configuration
@AutoConfigureAfter({ExceptionNoticeConfig.class})
@ConditionalOnBean({ExceptionHandler.class, ExceptionNoticeResolverFactory.class})
@EnableConfigurationProperties({ExceptionNoticeAsyncProperties.class})
public class ExceptionNoticeDecorationConfig {

    @Autowired
    private ExceptionNoticeAsyncProperties noticeAsyncProperties;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    public void setSendConfig(List<ExceptionSendComponentConfigure> configures, ExceptionNoticeHandlerDecoration exceptionNoticeHandlerDecoration) {
        configures.forEach(x -> x.addSendComponent(exceptionNoticeHandlerDecoration, null));
        logger.info("发送组件数量：" + configures.size());
    }

    @Autowired(required = false)
    public void setResolverConfig(List<ExceptionNoticeResolverConfigure> configures,
                                  ExceptionNoticeResolverFactory exceptionNoticeResolverFactory) {

        configures.forEach(x -> x.addResolver(exceptionNoticeResolverFactory));
        logger.info("解析组件数量：" + configures.size());
    }

    @Bean
    @ConditionalOnProperty(value = "exceptionnotice.enable-async-notice", havingValue = "true")
    public ExceptionNoticeHandlerDecoration getAsyncExceptionNoticeHandler(ExceptionHandler exceptionHandler) {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setMaxPoolSize(noticeAsyncProperties.getMaxPoolSize());
        poolTaskExecutor.setCorePoolSize(noticeAsyncProperties.getCorePoolSize());
        poolTaskExecutor.setQueueCapacity(noticeAsyncProperties.getQueueCapacity());
        poolTaskExecutor.setThreadNamePrefix(noticeAsyncProperties.getThreadNamePrefix());
        poolTaskExecutor.setDaemon(noticeAsyncProperties.isDaemon());
        poolTaskExecutor.setRejectedExecutionHandler(new CallerRunsPolicy());
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        poolTaskExecutor.initialize();
        AsyncExceptionNoticeHandler asyncExceptionNoticeHandler = new AsyncExceptionNoticeHandler(exceptionHandler, poolTaskExecutor);
        logger.info("创建异步信息AsyncExceptionNoticeHandler");
        return asyncExceptionNoticeHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "exceptionnotice.enable-async-notice", havingValue = "false", matchIfMissing = true)
    public ExceptionNoticeHandlerDecoration getDefaultExceptionNoticeHandler(ExceptionHandler exceptionHandler) {

        DefaultExceptionNoticeHandler defaultExceptionNoticeHandler = new DefaultExceptionNoticeHandler(exceptionHandler);
        logger.info("创建同步信息DefaultExceptionNoticeHandler");
        return defaultExceptionNoticeHandler;
    }


}
