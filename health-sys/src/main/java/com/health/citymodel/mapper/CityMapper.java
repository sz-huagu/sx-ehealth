package com.health.citymodel.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.health.citymodel.model.City;

@Repository
public interface CityMapper {

	/**
	 * 获取所有市信息
	 * 
	 * @author zhourukang
	 * @param city
	 * @return
	 */

	public List<City> findAllCity(City city);

}
