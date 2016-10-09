package com.health.citymodel.model;

import lombok.Data;

@Data
public class City {
	private String	id;			// id

	private String	province_id;	// 省,直辖市 id

	private String	name;			// 名称

	private int		orderNum;		// 顺序
}
