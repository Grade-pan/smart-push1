package com.kuding.properties;

import com.kuding.properties.enums.DingdingTextType;
import com.kuding.properties.enums.EmailTextType;
import com.kuding.properties.enums.ListenType;
import com.kuding.properties.enums.ProjectEnviroment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**在application.yml或者在application.properties里面获取
 * 如果修改读取的配置文件可以在下方@ConfigurationPropertie注解里面修改里面参数信息
 * @author Administrator
 */
@ConfigurationProperties(prefix = "exceptionnotice")
public class ExceptionNoticeProperty {

	/**
	 * 是否开启异常通知
	 */
	private Boolean openNotice;

	/**
	 * 追踪信息的包含的包名
	 */
	private String includedTracePackage;

	/**
	 * 异常工程名
	 */
	@Value("${spring.application.name:${exceptionnotice.project-name:无名工程}}")
	private String projectName;

	/**
	 * 工程的发布环境，主要分为5个：开发环境、测试环境、预发环境、正式环境与回滚环境
	 */
	private ProjectEnviroment projectEnviroment;

	/**
	 * <p>
	 * 通过注解进行监控，目前提供两种方式：
	 * </p>
	 * <ol>
	 * 一种只是普通的监视方法中的异常，主要包含了方法名、方法参数等相关内容；
	 * </ol>
	 * <ol>
	 * 另一种是监视请求出现异常后的通知，额外包含了请求路径、请求参数（param、body）以及想要查询的头信息，对于头信息的过滤参看
	 * </ol>
	 */
	private ListenType listenType = ListenType.COMMON;

	/**
	 * 开启redis存储
	 */
	private boolean enableRedisStorage;

	/**
	 * redis的键
	 */
	private String redisKey = "prometheus-notice";

	/**
	 * 排除的需要统计的异常
	 */
	private List<Class<? extends RuntimeException>> excludeExceptions = new LinkedList<>();

	/**
	 * 当listenType为web-mvc时，处理请求异常通知时需要的header名称信息
	 */
	private List<String> includeHeaderName = new ArrayList<String>();

	/**
	 * 发送钉钉异常通知给谁
	 */
	private Map<String, DingDingExceptionNoticeProperty> dingding;

	/**
	 * 发送邮件异常通知给谁
	 */
	private Map<String, EmailExceptionNoticeProperty> email;

	/**
	 * 默认通知人，当异常通知找不到背锅侠时，就用默认背锅侠
	 */
	private String defaultNotice;

	/**
	 * 钉钉通知文本类型
	 */
	private DingdingTextType dingdingTextType = DingdingTextType.TEXT;

	/**
	 * 邮件的通知文本类型
	 */
	private EmailTextType emailTextType = EmailTextType.TEXT;

	/**
	 * 是否开启异步通知
	 */
	private boolean enableAsyncNotice = false;



	public ProjectEnviroment getProjectEnviroment() {
		return projectEnviroment;
	}

	public void setProjectEnviroment(ProjectEnviroment projectEnviroment) {
		this.projectEnviroment = projectEnviroment;
	}

	/**
	 * @return the openNotice
	 */
	public Boolean getOpenNotice() {
		return openNotice;
	}

	/**
	 * @param openNotice the openNotice to set
	 */
	public void setOpenNotice(Boolean openNotice) {
		this.openNotice = openNotice;
	}

	/**
	 * @return the includedTracePackage
	 */
	public String getIncludedTracePackage() {
		return includedTracePackage;
	}

	/**
	 * @param includedTracePackage the includedTracePackage to set
	 */
	public void setIncludedTracePackage(String includedTracePackage) {
		this.includedTracePackage = includedTracePackage;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public ListenType getListenType() {
		return listenType;
	}

	public void setListenType(ListenType listenType) {
		this.listenType = listenType;
	}

	/**
	 * @return the enableRedisStorage
	 */
	public boolean isEnableRedisStorage() {
		return enableRedisStorage;
	}

	/**
	 * @param enableRedisStorage the enableRedisStorage to set
	 */
	public void setEnableRedisStorage(boolean enableRedisStorage) {
		this.enableRedisStorage = enableRedisStorage;
	}

	/**
	 * @return the redisKey
	 */
	public String getRedisKey() {
		return redisKey;
	}

	/**
	 * @param redisKey the redisKey to set
	 */
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}

	/**
	 * @return the excludeExceptions
	 */
	public List<Class<? extends RuntimeException>> getExcludeExceptions() {
		return excludeExceptions;
	}

	/**
	 * @param excludeExceptions the excludeExceptions to set
	 */
	public void setExcludeExceptions(List<Class<? extends RuntimeException>> excludeExceptions) {
		this.excludeExceptions = excludeExceptions;
	}

	/**
	 * @return the dingding
	 */
	public Map<String, DingDingExceptionNoticeProperty> getDingding() {
		return dingding;
	}

	/**
	 * @param dingding the dingding to set
	 */
	public void setDingding(Map<String, DingDingExceptionNoticeProperty> dingding) {
		this.dingding = dingding;
	}

	/**
	 * @return the email
	 */
	public Map<String, EmailExceptionNoticeProperty> getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(Map<String, EmailExceptionNoticeProperty> email) {
		this.email = email;
	}

	/**
	 * @return the includeHeaderName
	 */
	public List<String> getIncludeHeaderName() {
		return includeHeaderName;
	}

	/**
	 * @param includeHeaderName the includeHeaderName to set
	 */
	public void setIncludeHeaderName(List<String> includeHeaderName) {
		this.includeHeaderName = includeHeaderName;
	}

	/**
	 * @return the defaultNotice
	 */
	public String getDefaultNotice() {
		return defaultNotice;
	}

	/**
	 * @param defaultNotice the defaultNotice to set
	 */
	public void setDefaultNotice(String defaultNotice) {
		this.defaultNotice = defaultNotice;
	}

	/**
	 * @return the dingdingTextType
	 */
	public DingdingTextType getDingdingTextType() {
		return dingdingTextType;
	}

	/**
	 * @param dingdingTextType the dingdingTextType to set
	 */
	public void setDingdingTextType(DingdingTextType dingdingTextType) {
		this.dingdingTextType = dingdingTextType;
	}

	/**
	 * @return the emailTextType
	 */
	public EmailTextType getEmailTextType() {
		return emailTextType;
	}

	/**
	 * @param emailTextType the emailTextType to set
	 */
	public void setEmailTextType(EmailTextType emailTextType) {
		this.emailTextType = emailTextType;
	}

	public boolean isEnableAsyncNotice() {
		return enableAsyncNotice;
	}

	public void setEnableAsyncNotice(boolean enableAsyncNotice) {
		this.enableAsyncNotice = enableAsyncNotice;
	}

}
