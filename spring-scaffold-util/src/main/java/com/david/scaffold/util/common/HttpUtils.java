package com.david.scaffold.util.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.david.scaffold.util.constants.Constants;
import com.david.scaffold.util.exception.SimpleException;

/**
 * http工具类
 * 
 * @author dailiwei
 *
 */
public class HttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	public static final String DEFAULT_ENCODING = "utf-8";
	private static final Object syncObj = new Object();
	private static volatile CloseableHttpClient httpClient = null;
	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static SSLConnectionSocketFactory sslsf = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static SSLContextBuilder builder = null;

	static {
		try {
			// 全部信任 不做身份鉴定
			builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			sslsf = new SSLConnectionSocketFactory(builder.build(), new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null,
					NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register(HTTP, new PlainConnectionSocketFactory())
					.register(HTTPS, sslsf).build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(200);// max connection
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String httpGet(String url) {
		return _httpGet(url, new HashMap<String, String>());
	}

	public static String httpGet(String url, Map<String, String> queryParams) {
		return _httpGet(url, queryParams);
	}

	public static String httpPostNoBody(String url, Map<String, String> queryParams) {
		return _httpPost(url, queryParams, null, null);
	}

	public static String httpPost(String url, Map<String, String> formParams) {
		return _httpPost(url, null, formParams, null);
	}

	public static String httpPost(String url, Map<String, String> queryParams, String body) {
		return _httpPost(url, queryParams, null, body);
	}

	public static String httpPost(String url, Map<String, String> queryParams, String body, String charSet) {
		return _httpPost(url, queryParams, null, body, charSet);
	}

	public static String httpPost(String url, String body) {
		return _httpPost(url, null, null, body);
	}

	public static String httpXmlPost(String url, String body, Map<String, String> headerMap) {
		return _httpPost(url, headerMap, null, null, body, Constants.UTF_8, true);
	}

	public static String httpPost(String url, String body, String charSet) {
		return _httpPost(url, null, null, body, charSet);
	}

	private static String _httpPost(String url, Map<String, String> queryParams, Map<String, String> formParams, String body) {
		return _httpPost(url, queryParams, formParams, body, DEFAULT_ENCODING);
	}

	public static String encodeURL(String returnURL) {
		String encode_returnURL = "";
		try {
			encode_returnURL = URLEncoder.encode(returnURL, Constants.UTF_8);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}

		return encode_returnURL;
	}

	/**
	 * http get请求
	 * 
	 * @param url
	 *            请求url
	 * @param queryParams
	 *            请求参数
	 * @return 响应结果
	 */
	public static String _httpGet(String url, Map<String, String> queryParams) {
		String queryStr = buildQueryString(queryParams, DEFAULT_ENCODING);

		if (queryStr != null) {
			url = url + "?" + queryStr;
		}

		logger.info("httpGet request: " + JSON.toJSONString(queryParams));
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;

		try {
			response = getHttpClient().execute(httpGet);
			String resp = getResponseStr(response, DEFAULT_ENCODING);
			logger.info("httpPost response: " + resp);
			return resp;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			String msg = "request_url: " + url + "occurs error:" + ex.getMessage();
			throw new SimpleException(msg, ex);
		} finally {
			httpGet.releaseConnection();
		}
	}

	private static String _httpPost(String url, Map<String, String> queryParams, Map<String, String> formParams, String body, String charset) {
		return _httpPost(url, null, queryParams, formParams, body, charset, false);
	}

	/**
	 * http post请求
	 * 
	 * @param url
	 *            请求url不能为空
	 * @param queryParams
	 *            查询参数，位于url的“?”之后的参数
	 * @param formParams
	 *            form表单数据
	 * @param body
	 * @param charset
	 * @return
	 */
	private static String _httpPost(String url, Map<String, String> headerMap, Map<String, String> queryParams, Map<String, String> formParams, String body,
			String charset, boolean contentTypeXML) {

		if (charset == null) {
			charset = DEFAULT_ENCODING;
		}

		String queryStr = buildQueryString(queryParams, charset);
		if (queryStr != null) {
			url = url + "?" + queryStr;
		}

		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> list = toNameValuePairs(formParams);

		if (list != null && list.isEmpty() == false) {
			httpPost.addHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(list, Charset.forName(charset)));
		} else if (body != null) {
			if (contentTypeXML) {
				httpPost.addHeader("Content-Type", "appcliation/xml;charset=utf-8");
				for (Entry<String, String> headerKv : headerMap.entrySet()) {
					httpPost.addHeader(headerKv.getKey(), headerKv.getValue());
				}
				httpPost.setEntity(new StringEntity(body, Charset.forName(Constants.UTF_8)));
			} else {
				httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
				httpPost.setEntity(new StringEntity(body, Charset.forName(charset)));
			}
		}

		HttpResponse responseBody = null;

		try {
			responseBody = getHttpClient().execute(httpPost);
			if (isSuccess(responseBody) == false) {
				String msg = "request_url: " + url + " occurs error: " + responseBody.getStatusLine().getReasonPhrase();
				throw new SimpleException("9999", msg);
			}

			return getResponseStr(responseBody, charset);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			String msg = "request_url: " + url + " occurs error: " + ex.getMessage();
			throw new SimpleException(msg, ex);
		} finally {
			httpPost.releaseConnection();
		}
	}

	private static boolean isSuccess(HttpResponse responseBody) {
		System.out.println("response statusCode=" + responseBody.getStatusLine().getStatusCode());
		return responseBody.getStatusLine().getStatusCode() < 400;
	}

	private static String getResponseStr(HttpResponse responseBody, String charSet) throws IOException {
		HttpEntity entity = responseBody.getEntity();
		if (entity == null)
			return null;
		String result = IOUtils.toString(entity.getContent(), charSet);
		EntityUtils.consume(entity);
		return result;
	}

	private static String buildQueryString(Map<String, String> params, String charSet) {
		List<NameValuePair> list = toNameValuePairs(params);
		if (list == null || list.isEmpty())
			return null;
		return URLEncodedUtils.format(list, charSet);
	}

	private static List<NameValuePair> toNameValuePairs(Map<String, String> params) {
		if (params == null || params.isEmpty())
			return null;
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String value = entry.getValue();
			pairs.add(new BasicNameValuePair(entry.getKey(), value));
		}

		return pairs;
	}

	private static CloseableHttpClient getHttpClient() {
		if (httpClient == null) {
			synchronized (syncObj) {
				if (httpClient == null) {
					httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm).setConnectionManagerShared(true).build();
					return httpClient;
				}
			}
		}

		logger.info("httpclient=" + httpClient);
		return httpClient;
	}
}
