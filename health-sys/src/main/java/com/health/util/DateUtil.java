package com.health.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期工具类
 * 
 * @author
 * 
 */
@Setter
@Getter
@ToString
@Slf4j
public class DateUtil {

	private Calendar			calendar	= Calendar.getInstance();

	// 当前日期
	private String				currentDate;

	// 前一天
	private String				beforeDate;

	// 后一天
	private String				afterDate;

	// 周几
	private String				weekName;

	private SimpleDateFormat	sf			= new SimpleDateFormat("yyyy/MM/dd");

	private String[]			weeks		= { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };

	public DateUtil() {
	}

	public DateUtil(String currentDate) {
		if (StringUtils.isNotEmpty(currentDate)) {
			try {
				Date parseDate = sf.parse(currentDate);
				calendar.setTime(parseDate);
			} catch (ParseException e) {
				log.error("转换日期异常", e);
				return;
			}
		}
		this.currentDate = sf.format(calendar.getTime());
		setDate();
		getDayOfMonth();
	}

	/**
	 * 设置前一天后一天
	 */
	public void setDate() {
		Calendar leftCa = Calendar.getInstance();
		leftCa.setTime(this.calendar.getTime());
		Calendar rightCa = Calendar.getInstance();
		rightCa.setTime(this.calendar.getTime());
		leftCa.add(Calendar.DATE, -1);
		rightCa.add(Calendar.DATE, 1);
		this.beforeDate = sf.format(leftCa.getTime());
		this.afterDate = sf.format(rightCa.getTime());
	}

	/**
	 * 获取当前对象时间对应的星期
	 * 
	 * @return
	 */
	public void getDayOfMonth() {
		int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		this.weekName = weeks[week_index];
	}

	public static void main(String[] args) throws Exception {
		DateUtil dt = new DateUtil("");
		// Calendar leftCa=dt.getCalendar();
		// leftCa.add(Calendar.DATE,-1);
		// SimpleDateFormat ff=new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(ff.format(leftCa.getTime()));
		/*
		 * dt.getCalendar().add(Calendar.DATE,-14); SimpleDateFormat sf=new
		 * SimpleDateFormat("yyyy-MM-dd"); Date dd=sf.parse("2015-10-15");
		 * dt.getCalendar().setTime(dd);
		 * System.out.println(sf.format(dt.getCalendar().getTime()));
		 */

		System.out.println(dt);
		// System.out.println(sf.format(dt.getCalendar().getTime()));
		// System.out.println(dt.getDayOfMonth(2015, 12, 31));
		// System.out.println(dt.getCalendar().get(Calendar.YEAR)+"/"+dt.getCalendar().get(Calendar.MONTH)+"/"+dt.getCalendar().get(Calendar.DATE));
		// System.out.println(dt.getDayOfMonth());
	}
}
