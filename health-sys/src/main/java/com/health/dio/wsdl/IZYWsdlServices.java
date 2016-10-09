package com.health.dio.wsdl;

import javax.jws.WebService;

/**
 * 众盈webservice接口定义
 * 
 * @Description
 * @author RickJou
 * @date 2016年10月9日 下午4:55:43
 */
@WebService
public interface IZYWsdlServices {
	/**
	 * 众盈对天网发布的webservice统一入口
	 * 
	 * @Description
	 * @author RickJou
	 * @param jsonParams
	 * @return
	 */
	public abstract String services(String jsonParams);
}
