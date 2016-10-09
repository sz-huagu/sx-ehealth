package com.health.dio.wsdl;

import java.util.HashMap;

import javax.jws.WebService;

import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;

@Slf4j
@WebService(endpointInterface = "com.health.dio.wsdl.IZYWsdlServices")
public class ZYWsdlServicesImple implements IZYWsdlServices {

	@Override
	public String services(String jsonParams) {
		log.info(jsonParams);
		@SuppressWarnings("rawtypes")
		HashMap<String, String> map = new HashMap();
		map.put("code", "0");
		map.put("responseData", "退费提醒推送成功");
		return JSON.toJSONString(map);
	}

}
