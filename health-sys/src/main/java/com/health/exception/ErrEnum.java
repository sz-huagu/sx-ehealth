package com.health.exception;

/**
 * 错误码枚举类,按模块分配错误码. 1 错误码都是负数 2 不同模块采用不懂的错误码区间,如系统全局错误采用 -10001 到 -19999.用户模块采用
 * -20001 到 -29999
 * 
 * @author zrk（Administrator）
 * @since 2016年10月7日
 */
public enum ErrEnum {
	/**
	 * 请求无效
	 */
	ERR10001(-10001, "请求无效"),
	/**
	 * 根据ID找不到对应的关注信息
	 */
	ERR50003(-10002, "根据ID找不到对应的关注信息"), ;

	private int		code;

	private String	name;

	private ErrEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
