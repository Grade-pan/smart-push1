package com.kuding.httpclient;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Deprecated
public class SimpleHttpClient {

	private Gson gson;

	public SimpleHttpClient(Gson gson) {
		this.gson = gson;
	}

	private final Log logger = LogFactory.getLog(getClass());

	private final String defaultContentType = "application/json; charset=utf-8";

	public <K> String doPost(String url, K jsonParam, Map<String, String> headers)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		headers.forEach((x, y) -> post.setHeader(x, y));
		StringEntity entity;
		CloseableHttpResponse response = null;
		try {
			if (jsonParam != null) {
				String jsonStr = gson.toJson(jsonParam);
				logger.debug("json:" + jsonStr);
				entity = new StringEntity(jsonStr, Charset.forName("utf-8"));
				entity.setContentType("application/json");
				post.setEntity(entity);
			}
			response = httpclient.execute(post);
			String respStr = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
			logger.debug(respStr);
			response.close();
			return respStr;
		} finally {
			response.close();
		}
	}

	public <T, K> T post(String url, K jsonParam, Class<T> clazz) {
		return post(url, jsonParam, clazz, null);
	}

	public <T, K> T post(String url, K jsonParam, Class<T> clazz, Map<String, String> header) {
		String json = null;
		header = header == null ? new HashMap<>() : header;
		if (!header.containsKey(HTTP.CONTENT_TYPE))
			header.put(HTTP.CONTENT_TYPE, defaultContentType);
		try {
			json = doPost(url, jsonParam, header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		T res = json == null ? null : gson.fromJson(json, clazz);
		return res;
	}

	public <K, T> T post(String url, K jsonParam, TypeToken<T> type) {
		return post(url, jsonParam, type, null);
	}

	public <K, T> T post(String url, K jsonParam, TypeToken<T> type, Map<String, String> header) {
		header = header == null ? new HashMap<>() : header;
		if (!header.containsKey(HTTP.CONTENT_TYPE))
			header.put(HTTP.CONTENT_TYPE, defaultContentType);
		String json = null;
		try {
			json = doPost(url, jsonParam, header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		T res = json == null ? null : gson.fromJson(json, type.getType());
		return res;
	}

	public <T> T doPost(String url, Class<T> clazz, Map<String, String> Param, Map<String, Object> headers) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		headers.forEach((x, y) -> post.setHeader(x, y.toString()));
		try {
			List<NameValuePair> list = Param.entrySet().stream()
					.map(x -> new BasicNameValuePair(x.getKey(), x.getValue())).collect(toList());
			post.setEntity(new UrlEncodedFormEntity(list));
			CloseableHttpResponse response = httpclient.execute(post);
			String respStr = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
			T result = gson.fromJson(respStr, clazz);
			response.close();
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T doPost(String url, TypeToken<T> type, Map<String, String> Param, Map<String, Object> headers) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		headers.forEach((x, y) -> post.setHeader(x, y.toString()));
		try {
			List<NameValuePair> list = Param.entrySet().stream()
					.map(x -> new BasicNameValuePair(x.getKey(), x.getValue())).collect(toList());
			post.setEntity(new UrlEncodedFormEntity(list));
			CloseableHttpResponse response = httpclient.execute(post);
			String respStr = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
			T result = gson.fromJson(respStr, type.getType());
			response.close();
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T doGet(String uri, Class<T> clazz) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet get = new HttpGet(uri);
		try {
			CloseableHttpResponse response = httpclient.execute(get);
			String respStr = EntityUtils.toString(response.getEntity());
			response.close();
			T result = gson.fromJson(respStr, clazz);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T doGet(String url, TypeToken<T> type, Map<String, String> param, Map<String, Object> header) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String uri = String.format("%s?%s", url, String.join("&", param.entrySet().stream()
				.map(x -> String.format("%s=%s", x.getKey(), x.getValue())).collect(toList())));
		HttpGet get = new HttpGet(uri);
		try {
			header.forEach((x, y) -> get.addHeader(x, y.toString()));
			CloseableHttpResponse response = httpclient.execute(get);
			String respStr = EntityUtils.toString(response.getEntity());
			response.close();
			T result = gson.fromJson(respStr, type.getType());
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
