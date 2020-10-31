package com.kuding.redis;

import java.util.Map;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.kuding.ExceptionNoticeException;
import com.kuding.content.ExceptionNotice;
import com.kuding.properties.ExceptionNoticeProperty;

public class ExceptionRedisStorageComponent {

	private ExceptionNoticeProperty exceptionNoticeProperty;

	private StringRedisTemplate stringRedisTemplate;

	public ExceptionRedisStorageComponent(ExceptionNoticeProperty exceptionNoticeProperty,
			StringRedisTemplate stringRedisTemplate, Gson gson) {
		if (exceptionNoticeProperty.getRedisKey() == null) {
			throw new ExceptionNoticeException("无法创建redis存储，没有redis键相对应");
		}
		this.exceptionNoticeProperty = exceptionNoticeProperty;
		this.stringRedisTemplate = stringRedisTemplate;
	}

	/**
	 * @return the stringRedisTemplate
	 */
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	/**
	 * @param stringRedisTemplate the stringRedisTemplate to set
	 */
	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	/**
	 * @return the exceptionNoticeProperty
	 */
	public ExceptionNoticeProperty getExceptionNoticeProperty() {
		return exceptionNoticeProperty;
	}

	/**
	 * @param exceptionNoticeProperty the exceptionNoticeProperty to set
	 */
	public void setExceptionNoticeProperty(ExceptionNoticeProperty exceptionNoticeProperty) {
		this.exceptionNoticeProperty = exceptionNoticeProperty;
	}

	private BoundHashOperations<String, String, String> getops() {
		return stringRedisTemplate.boundHashOps(exceptionNoticeProperty.getRedisKey());
	}

	public Boolean save(ExceptionNotice exception) {
		String uid = exception.getUid();
		BoundHashOperations<String, String, String> boundHashOperations = getops();
		Boolean notEx = boundHashOperations.putIfAbsent(uid, exception.createText());
		return notEx;
	}

	public Map<String, String> getAllException() {
		Map<String, String> map = getops().entries();
		return map;
	}
	
	public void del(String uid) {
		getops().delete(uid);
	}
}
