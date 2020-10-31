package com.kuding.httpclient;

import java.net.URI;

import com.kuding.pojos.dingding.DingDingNotice;
import com.kuding.pojos.dingding.DingDingResult;

@FunctionalInterface
public interface DingdingHttpClient {

	DingDingResult post(URI url, DingDingNotice body, Class<DingDingResult> clazz);

}
