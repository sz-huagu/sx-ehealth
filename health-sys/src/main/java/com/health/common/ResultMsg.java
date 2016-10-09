package com.health.common;

import java.util.List;

import lombok.Data;

import com.health.citymodel.model.City;

/**
 * 
 * @Description 
 * @author Administrator
 * @date 2016年10月8日 下午3:37:23
 */
@Data
public class ResultMsg {
	private List<City> listCity;
}
