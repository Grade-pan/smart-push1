package com.sed.text;

import com.sed.content.ExceptionNotice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class ExceptionNoticeResolverFactory {

    private final ExceptionNoticeResolver defaultResolver = e -> e.createText();

    private final Log logger = LogFactory.getLog(getClass());

    private final Map<String, ExceptionNoticeResolver> resolverMap = new HashMap<String, ExceptionNoticeResolver>();

    public String resolve(String resolverKey, ExceptionNotice exceptionNotice) {
        ExceptionNoticeResolver noticeResolver = resolverMap.getOrDefault(resolverKey, defaultResolver);
        String notice = noticeResolver.resolve(exceptionNotice);
        return notice;
    }

    public void addNoticeResolver(String resolveKey, ExceptionNoticeResolver resolver) {
        logger.info("添加解析器-->" + resolveKey + "---" + resolver.getClass());
        resolverMap.putIfAbsent(resolveKey, resolver);
    }

    public Map<String, ExceptionNoticeResolver> getResolverMap() {
        return resolverMap;
    }

}
