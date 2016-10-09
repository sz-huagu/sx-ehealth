package com.health.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.health.common.ParamVo;
import com.health.common.ResultVo;

@Controller
@RequestMapping("/adrFacade")
public class AdrFacade {

	/**
	 * 
	 * Description
	 * 
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 * @see com.health.controller.IFacade#service(com.health.common.ParamVo,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@ResponseBody
	@RequestMapping("/service.do")
	public ResultVo service(@ModelAttribute
	ParamVo param, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
