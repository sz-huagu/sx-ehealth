package com.health.application;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.health.common.ResultVo;
import com.health.exception.BusinessRuntimeException;

@Component
@Aspect
@Slf4j
public class ControllerParamsAspectJ {

	@Around("execution(* com.health.controller..*.*(..))")
	public Object controllerMetodBefore(ProceedingJoinPoint point) {
		Object[] args = point.getArgs();
		HttpServletRequest request = null;
		Enumeration<String> paramNames = null;
		String paramName = null;
		String paramValue = null;
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (Object obj : args) {
			if (!(obj instanceof HttpServletRequest)) {
				continue;
			}
			request = (HttpServletRequest) obj;
			paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = (String) paramNames.nextElement();
				paramValue = request.getParameter(paramName);
				sb.append(paramName).append("=").append(paramValue).append(",");
			}
			sb.setLength(sb.length() - 1);
		}
		if (sb.length() > 1) {
			sb.append("}");
		} else {
			sb.append("{}");
		}
		log.info("请求参数:" + sb.toString());
		Object res = null;
		try {
			// 执行业务方法
			res = point.proceed(args);
		} catch (BusinessRuntimeException bex) {
			bex.printStackTrace();
			// 返回业务异常
			return ResultVo.errVo(bex.getErr());
		} catch (Exception ex) {
			ex.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		log.info("返回值:\n" + ((res instanceof String) ? res : JSON.toJSON(res)));
		return res;
	}

}
