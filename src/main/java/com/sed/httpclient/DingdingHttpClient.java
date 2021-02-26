package com.sed.httpclient;

import com.sed.pojos.dingding.DingDingNotice;
import com.sed.pojos.dingding.DingDingResult;

import java.net.URI;

@FunctionalInterface
public interface DingdingHttpClient {

    DingDingResult post(URI url, DingDingNotice body, Class<DingDingResult> clazz);

}
