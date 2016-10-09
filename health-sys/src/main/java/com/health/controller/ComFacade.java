package com.health.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.health.citymodel.model.City;
import com.health.citymodel.service.CityService;
import com.health.common.ModelEnum;
import com.health.common.ParamVo;
import com.health.common.ResultMsg;
import com.health.common.ResultVo;
import com.health.exception.ErrEnum;

@Slf4j
@Controller
@RequestMapping("/comFacade")
public class ComFacade {
	@Autowired
	private CityService	cityService;

	@ResponseBody
	@RequestMapping(value = "/service.do", method = RequestMethod.POST)
	public ResultVo facade(@ModelAttribute
	ParamVo param, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 系统模块
		if (ModelEnum.CODE10000.getCode().equals(param.getModelCode())) {
			// 测试方法1
			if (ModelEnum.CODE10001.getCode().equals(param.getMsgCode())) {
				List<City> list = cityService.getAllCity(null);
				ResultMsg msg = new ResultMsg();
				msg.setListCity(list);
				return ResultVo.getSuccessVo(msg);
			}
			// 测试方法2
			if (ModelEnum.CODE10002.getCode().equals(param.getMsgCode())) {
				cityService.TestMemechaced();
				return ResultVo.getSuccessVo(null);
			}

			return null;
		}

		if (ModelEnum.CODE20000.getCode().equals(param.getModelCode())) {

			return null;
		}

		return ResultVo.errVo(ErrEnum.ERR10001);
	}

}
