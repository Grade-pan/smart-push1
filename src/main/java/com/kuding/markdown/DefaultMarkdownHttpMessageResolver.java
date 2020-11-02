package com.kuding.markdown;

import com.kuding.content.ExceptionNotice;
import com.kuding.content.HttpExceptionNotice;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.text.ExceptionNoticeResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.format.DateTimeFormatter;

import static java.util.stream.Collectors.toList;

public class DefaultMarkdownHttpMessageResolver implements ExceptionNoticeResolver {

    private final ExceptionNoticeProperty exceptionNoticeProperty;

    private final Log logger = LogFactory.getLog(getClass());

    public DefaultMarkdownHttpMessageResolver(ExceptionNoticeProperty exceptionNoticeProperty) {
        this.exceptionNoticeProperty = exceptionNoticeProperty;
    }

    @Override
    public String resolve(ExceptionNotice exceptionNotice) {
        HttpExceptionNotice httpExceptionNotice = (HttpExceptionNotice) exceptionNotice;
        String title = String.format("%s(%s)", httpExceptionNotice.getProject(),
                exceptionNoticeProperty.getProjectEnviroment().getName());
        String markdown = SimpleMarkdownBuilder.create().title(SimpleMarkdownBuilder.bold(title), 1)
                .text(SimpleMarkdownBuilder.bold("请求地址："), false).text(httpExceptionNotice.getUrl(), true)
                .title("接口参数：", 2)
                .orderPoint(httpExceptionNotice.getParamInfo().entrySet().stream()
                        .map(x -> String.format("%s=%s", x.getKey(), x.getValue())).collect(toList()))
                .title("请求头信息：", 2)
                .orderPoint(httpExceptionNotice.getHeaders().entrySet().stream()
                        .map(x -> String.format("%s=%s", x.getKey(), x.getValue())).collect(toList()))
                .title("请求体：", 2).text(httpExceptionNotice.getRequestBody(), true).title("方法路径：", 2)
                .text(httpExceptionNotice.getClassPath(), true)
                .title("方法名：" + SimpleMarkdownBuilder.bold(httpExceptionNotice.getMethodName()), 2).title("参数信息：", 2)
                .point(httpExceptionNotice.getParames()).title("异常信息：", 2)
                .point(httpExceptionNotice.getExceptionMessage()).title("异常追踪：", 2)
                .point(httpExceptionNotice.getTraceInfo()).title("最后一次出现时间：", 2)
                .text(httpExceptionNotice.getLatestShowTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), true)
                .title("出现次数：" + SimpleMarkdownBuilder.bold(httpExceptionNotice.getShowCount().toString()), 2).build();
        logger.debug(markdown);
        return markdown;
    }

}
