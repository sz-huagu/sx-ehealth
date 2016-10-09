package com.health.test.http;

import java.util.HashMap;
import java.util.Map;

import com.health.util.HttpClientUtil;

public class TestPost {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		String url = "http://127.0.0.1:80/health-sys/comFacade/service.do";
		Map map = new HashMap();
		map.put("modelCode", "10000");
		map.put("msgCode", "10001");

		String res = HttpClientUtil.post(url, map, null);
		System.out.println(res);
	}
}
