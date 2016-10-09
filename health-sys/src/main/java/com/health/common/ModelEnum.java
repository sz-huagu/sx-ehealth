package com.health.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求码枚举类,按模块分配请求码. 1 请求码都是负数 2 不同模块采用不懂的请求码区间,如系统全局错误采用 -10001 到 -19999.用户模块采用
 * -20001 到 -29999
 * 
 * @author zrk（Administrator）
 * @since 2016年10月7日
 */
@Getter
public enum ModelEnum {
	/**
	 * 系统模块
	 */
	CODE10000("10000", "系统模块"),
	/**
	 * 系统模块-测试方法1
	 */
	CODE10001("10001", "测试方法1"),
	/**
	 * 系统模块-测试方法2
	 */
	CODE10002("10002", "测试方法2"),
	/**
	 * 用户模块
	 */
	CODE20000("20000", "用户模块");

	private String	code;

	private String	name;

	private ModelEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
