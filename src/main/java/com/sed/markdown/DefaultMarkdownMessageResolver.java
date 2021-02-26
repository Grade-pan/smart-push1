package com.sed.markdown;

import com.sed.content.ExceptionNotice;
import com.sed.properties.ExceptionNoticeProperty;
import com.sed.text.ExceptionNoticeResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.format.DateTimeFormatter;

public class DefaultMarkdownMessageResolver implements ExceptionNoticeResolver {

    private final ExceptionNoticeProperty exceptionNoticeProperty;

    private final Log logger = LogFactory.getLog(getClass());

    public DefaultMarkdownMessageResolver(ExceptionNoticeProperty exceptionNoticeProperty) {
        this.exceptionNoticeProperty = exceptionNoticeProperty;
    }

    @Override
    public String resolve(ExceptionNotice exceptionNotice) {
        String title = String.format("%s(%s)", exceptionNotice.getProject(),
                exceptionNoticeProperty.getProjectEnviroment().getName());
        String markdown = SimpleMarkdownBuilder.create().title(title, 1).title("路径：", 2)
                .text(exceptionNotice.getClassPath(), true)
                .title("方法名：" + SimpleMarkdownBuilder.bold(exceptionNotice.getMethodName()), 2).title("参数信息：", 2)
                .orderPoint(exceptionNotice.getParames()).title("异常信息：", 2).point(exceptionNotice.getExceptionMessage())
                .title("追踪信息：", 2).orderPoint(exceptionNotice.getTraceInfo()).title("最后一次出现时间：", 2)
                .text(exceptionNotice.getLatestShowTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        true)
                .title("出现次数：" + SimpleMarkdownBuilder.bold(exceptionNotice.getShowCount().toString()), 2).build();
        logger.debug(markdown);
        return markdown;
    }


}
