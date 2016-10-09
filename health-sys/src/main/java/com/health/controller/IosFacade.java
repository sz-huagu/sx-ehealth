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
@RequestMapping("/iosFacade")
public class IosFacade {

	@ResponseBody
	@RequestMapping("/service.do")
	public ResultVo service(@ModelAttribute
	ParamVo param, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
