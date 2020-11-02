package com.kuding.httpclient;

import com.kuding.pojos.dingding.DingDingNotice;
import com.kuding.pojos.dingding.DingDingResult;

import java.net.URI;

@FunctionalInterface
public interface DingdingHttpClient {

    DingDingResult post(URI url, DingDingNotice body, Class<DingDingResult> clazz);

}
