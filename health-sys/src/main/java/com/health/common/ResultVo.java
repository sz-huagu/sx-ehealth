package com.health.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.health.exception.ErrEnum;

@Data
public class ResultVo {
	private static String	ERR_STATE		= "failed";

	private static String	SUCCESS_STATE	= "success";

	private String			state;							// 消息状态
	private ResultMsg		msg;							// 返回消息内容

	private String			errInfo;						// 错误信息
	private String			errCode;						// 错误码

	/**
	 * 获得一个新的ResultVo实例
	 * 
	 * @Description
	 * @author Administrator
	 * @return
	 */
	public static ResultVo getSuccessVo(ResultMsg msg) {
		ResultVo res = new ResultVo();
		res.setState(ResultVo.SUCCESS_STATE);
		res.setMsg(msg);
		return res;
	}

	/**
	 * 根据异常信息返回错误ResultVo实例
	 * 
	 * @Description
	 * @author Administrator
	 * @param errEnum
	 * @return
	 */
	public static ResultVo errVo(ErrEnum errEnum) {
		ResultVo vo = new ResultVo();
		vo.setState(ResultVo.ERR_STATE);
		vo.setErrCode(errEnum.getCode() + "");
		vo.setErrInfo(errEnum.getName());
		return vo;
	}

	/**
	 * get spring mvc httpServletRequest
	 * 
	 * @Description
	 * @author Administrator
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * get spring mvc httpServletResponse
	 * 
	 * @Description
	 * @author Administrator
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
	}

}
