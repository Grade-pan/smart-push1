package com.sed.httpclient;

import java.net.URI;

import com.sed.pojos.dingding.DingDingNotice;
import com.sed.pojos.dingding.DingDingResult;

@FunctionalInterface
public interface DingdingHttpClient {

	DingDingResult post(URI url, DingDingNotice body, Class<DingDingResult> clazz);

}
