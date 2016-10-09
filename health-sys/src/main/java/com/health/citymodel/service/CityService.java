package com.health.citymodel.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.health.citymodel.mapper.CityMapper;
import com.health.citymodel.model.City;
import com.health.util.XmemcachedUtil;

@Transactional
@Service
public class CityService {
	@Autowired
	private CityMapper	cityMapper;

	/**
	 * 
	 * @Description
	 * @author Administrator
	 * @param city
	 * @return
	 */
	public List<City> getAllCity(City city) {
		return cityMapper.findAllCity(city);
	}

	public void TestMemechaced() throws TimeoutException, InterruptedException, MemcachedException, IOException {
		// add
		XmemcachedUtil.add("username0", 0, "rick0");
		XmemcachedUtil.add("username1", 0, "rick1");
		XmemcachedUtil.add("username2", 0, "rick2");
		XmemcachedUtil.add("username3", 0, "rick3");
		// get
		System.out.println(XmemcachedUtil.get("username0"));
		// bulk get
		List<String> keys = new ArrayList<String>();
		keys.add("username0");
		keys.add("username1");
		keys.add("username2");
		keys.add("username3");
		Map<String, Object> result = XmemcachedUtil.get(keys);
		for (Map.Entry<String, Object> et : result.entrySet()) {
			System.out.println(et.getKey() + "   " + et.getValue());
		}
		// gets
		XmemcachedUtil.set("testGets", 0, "猴赛雷啊");
		Object[] objs = XmemcachedUtil.gets("testGets");
		System.out.println("gets flugs = " + objs[0]);
		System.out.println("gets value = " + objs[1]);
		// set
		XmemcachedUtil.set("username0", 0, "set username 0");
		System.out.println(XmemcachedUtil.get("username0"));

		// replace
		XmemcachedUtil.replace("username0", 0, "hello");
		System.out.println("replace " + XmemcachedUtil.get("username0"));

		// append
		XmemcachedUtil.append("username0", "----append");
		System.out.println("append   " + XmemcachedUtil.get("username0"));

		// prepend
		XmemcachedUtil.prepend("username0", "prepend----");
		System.out.println("prepend   " + XmemcachedUtil.get("username0"));

		// delete
		XmemcachedUtil.delete("username0");
		System.out.println("delete username0   " + XmemcachedUtil.get("username0"));

		// flush_all
		XmemcachedUtil.flushAll(true);
		System.out.println("flush all");
		// incr
		// incr和decr都有三个参数的方法，第一个参数指定递增的key名称，第二个参数指定递增的幅度大小，第三个参数指定当key不存在的情况下的初始值。两个参数的重载方法省略了第三个参数，即默认指定为0。
		XmemcachedUtil.incr("number", 1, 10);
		System.out.println("number = 10");
		XmemcachedUtil.incr("number", 3);
		System.out.println("incr 3 number = " + XmemcachedUtil.get("number"));
		// decr
		XmemcachedUtil.decr("number", 15);
		System.out.println("decr 15 number = " + XmemcachedUtil.get("number") + "decr 只能将值减少到0,并不能为负数,因为使用incr时声明的 numeric类型是无符号的");

	}
}
