package com.health.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Fastjson工具类
 * 
 * @author
 * 
 */
public class FastjsonUtil {

	/**
	 * 将json字符串转换为java.util.List
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonStr, Class<T> clazz) {
		return JSON.parseArray(jsonStr, clazz);
	}

	/**
	 * 将java.util.List转换为json字符串
	 * 
	 * @param T
	 * @return
	 */
	public static <T> String listToJson(List<T> T) {
		return JSON.toJSONString(T);
	}

	/**
	 * Json转换成java.util.Map
	 * 
	 * @param jsonStr
	 * @param T
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String jsonStr) {
		return (Map<String, Object>) JSON.parse(jsonStr);
	}

	/**
	 * java.util.Map转换成java.lang.String
	 * 
	 * @param map
	 * @return
	 */
	public static <T> String mapToJson(Map<String, T> map) {
		return JSON.toJSONString(map);
	}

	/**
	 * 将JSONObject转换成JavaBean
	 * 
	 * @param jsonObject
	 * @param T
	 * @return
	 */
	@SuppressWarnings("unused")
	private static <T> T jsonObjectToJavaBean(JSONObject jsonObject, Class<T> T) {
		return JSON.parseObject(jsonObject.toJSONString(), T);
	}

	/**
	 * 将JavaBean转换成java.lang.String
	 * 
	 * @param t
	 * @return
	 */
	public static <T> String javaBeanToJsonString(T t) {
		return JSON.toJSONString(t);
	}

	/**
	 * 将json字符串转换成JavaBean
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> T jsonStringToJavaBean(String jsonStr, Class<T> clazz) {
		return JSON.parseObject(jsonStr, clazz);
	}
}