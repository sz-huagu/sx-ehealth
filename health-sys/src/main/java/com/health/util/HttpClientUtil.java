package com.health.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient工具类
 * 
 * @Description
 * @author RickJou
 * @date 2016年10月9日 下午4:49:13
 */
@SuppressWarnings("deprecation")
@Slf4j
public class HttpClientUtil {

	// 连接池最大连接数
	private final static Integer						MAX_TOTAL					= 500;

	// 路由最大连接数
	private final static Integer						MAX_PER_ROUTE				= 500;

	// 连接建立后数据传输超时时长
	private final static Integer						socketTimeout				= 15000;

	// 建立连接超时时长
	private final static Integer						connectTimeout				= 15000;

	// 建立请求超时时长
	private final static Integer						connectionRequestTimeout	= 15000;

	private final static String							DEFAULT_CHARSET				= "UTF-8";

	private static PoolingHttpClientConnectionManager	connManager					= null;

	// 连接池
	static {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
		registryBuilder.register("http", new PlainConnectionSocketFactory());

		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			LayeredConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSf);
		} catch (KeyStoreException e) {
			log.error(e.getMessage());
		} catch (KeyManagementException e) {
			log.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		}

		Registry<ConnectionSocketFactory> registry = registryBuilder.build();

		connManager = new PoolingHttpClientConnectionManager(registry);
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		connManager.setMaxTotal(MAX_TOTAL);
		connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
	}

	/**
	 * 获取HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient getCloseableHttpClient() {
		HttpClientContext httpClientContext = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		httpClientContext.setCookieStore(cookieStore);

		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();

		return HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * URL带参数，需要编码后传入
	 * 
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String get(String url, String charset) {
		return get(url, null, charset);
	}

	/**
	 * GET请求，包含请求参数
	 * 
	 * @param url
	 * @param paramsMap
	 * @param charset
	 * @return
	 */
	public static String get(String url, Map<String, String> paramsMap, String charset) {
		// 访问地址不能为空
		if (StringUtils.isEmpty(url)) {
			return null;
		}

		// 将请求参数封装成List<NameValuePair>
		List<NameValuePair> paramslist = getParamsList(paramsMap);

		// 参数不为空
		if (paramslist != null && !paramslist.isEmpty()) {
			url = url + "?" + URLEncodedUtils.format(paramslist, getCharset(charset));
		}

		log.info("开始调用接口,接口地址[{}],参数[{}]", url, paramslist.toString());
		String resultStr = excute(new HttpGet(url), charset);
		log.info("结束调用接口,接口地址[{" + url + "}],参数[{" + paramslist.toString() + "}],返回结果[{" + resultStr + "}]");
		return resultStr;
	}

	/**
	 * POST请求，包含请求参数和字符集
	 * 
	 * @param url
	 * @param paramsMap
	 * @param charset
	 * @return
	 */
	public static String post(String url, Map<String, String> paramsMap, String charset) {
		// 访问地址不能为空
		if (StringUtils.isEmpty(url)) {
			return null;
		}

		// 将请求参数封装成List<NameValuePair>
		List<NameValuePair> paramslist = getParamsList(paramsMap);

		// httppost请求
		HttpPost httpPost = new HttpPost(url);
		try {
			// 如果参数不为空，则将参数设置到请求体中
			if (paramslist != null && !paramslist.isEmpty()) {
				httpPost.setEntity(new UrlEncodedFormEntity(paramslist, getCharset(charset)));
			}
		} catch (UnsupportedEncodingException e) {
			log.error("UrlEncodedFormEntity转换字符异常,{}", e.getMessage());
			return null;
		}
		log.info("开始调用接口,接口地址[{}],参数[{}]", url, paramslist.toString());
		String resultStr = excute(httpPost, charset);
		log.info("结束调用接口,接口地址[{" + url + "}],参数[{" + paramslist.toString() + "}],返回结果[{" + resultStr + "}]");
		return resultStr;
	}

	/**
	 * POST请求，包含请求参数和字符集
	 * 
	 * @param url
	 * @param paramStr
	 * @param charset
	 * @return
	 */
	public static String post(String url, String paramStr, String charset) {
		// 访问地址不能为空
		if (StringUtils.isEmpty(url)) {
			return null;
		}

		// httppost请求
		HttpPost httpPost = new HttpPost(url);

		if (StringUtils.isNotEmpty(paramStr)) {
			httpPost.setEntity(new StringEntity(paramStr, ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), Charset.forName(getCharset(charset)))));
		}

		log.info("开始调用接口,接口地址[{}],参数[{}]", url, paramStr);
		String resultStr = excute(httpPost, charset);
		log.info("结束调用接口,接口地址[{" + url + "}],参数[{" + paramStr.toString() + "}],返回结果[{" + resultStr + "}]");
		return resultStr;
	}

	/**
	 * 通用返回类型为String的执行
	 * 
	 * @param request
	 * @param charset
	 * @return
	 */
	private static String excute(HttpRequestBase request, String charset) {
		// 请求对象
		CloseableHttpClient httpClient = getCloseableHttpClient();
		request.setConfig(getRequestConfig());
		// 响 应对象
		CloseableHttpResponse httpResponse = null;
		// 返回结果
		String responseStr = null;
		try {
			// 调用远程接口
			httpResponse = httpClient.execute(request);
			// 查看返回的状态码是否调用成功
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				log.error("接口调用失败，状态码：" + httpResponse.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的结果数据(String类型)
			responseStr = EntityUtils.toString(httpResponse.getEntity(), getCharset(charset));
		} catch (ClientProtocolException e) {
			log.error("客户端连接异常，" + e.getMessage());
			return null;
		} catch (IOException e) {
			log.error("IO异常，" + e.getMessage());
			return null;
		} finally {
			close(httpResponse);
		}
		return responseStr;
	}

	/**
	 * 关闭资源
	 * 
	 * @param httpResponse
	 */
	private static void close(CloseableHttpResponse httpResponse) {
		if (httpResponse != null) {
			try {
				httpResponse.close();
			} catch (IOException e) {
				log.error("关闭CloseableHttpResponse异常," + e.getMessage());
			}
		}

	}

	/**
	 * 获取请求配置
	 * 
	 * @return
	 */
	private static RequestConfig getRequestConfig() {
		// 请求配置信息
		return RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
	}

	/**
	 * 将请求参数封装成List<NameValuePair>
	 * 
	 * @param paramsMap
	 * @return
	 */
	private static List<NameValuePair> getParamsList(Map<String, String> paramsMap) {
		List<NameValuePair> paramsList = null;
		if (paramsMap != null && !paramsMap.isEmpty()) {
			paramsList = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		return paramsList;
	}

	/**
	 * 获取字符集
	 * 
	 * @param charset
	 * @return
	 */
	private static String getCharset(String charset) {
		if (StringUtils.isEmpty(charset))
			return DEFAULT_CHARSET;
		return charset;
	}
}