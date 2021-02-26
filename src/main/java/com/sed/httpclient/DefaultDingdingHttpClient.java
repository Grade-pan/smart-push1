package com.sed.httpclient;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.sed.pojos.dingding.DingDingNotice;
import com.sed.pojos.dingding.DingDingResult;

public class DefaultDingdingHttpClient implements DingdingHttpClient {

	private final RestTemplate restTemplate;

	private final Log logger = LogFactory.getLog(getClass());

	public DefaultDingdingHttpClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public DingDingResult post(URI url, DingDingNotice body, Class<DingDingResult> clazz) {
		ResponseEntity<DingDingResult> response = restTemplate.postForEntity(url, body, clazz);
		if (response.getStatusCode() == HttpStatus.OK) {
			if (logger.isDebugEnabled())
				logger.debug("钉钉发送成功：" + response);
			return response.getBody();
		} else
			logger.error("钉钉发送失败：" + response);
		return null;
	}

}
