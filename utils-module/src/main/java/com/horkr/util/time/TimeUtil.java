package com.horkr.util.time;

import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @description: 时间转换工具类
 * @author: lianghong.lu
 * @create: 2019-04-13 10:20
 **/

public class TimeUtil {

	private TimeUtil() {
	}

	/**
	 * date转为LocalDate
	 *
	 * @param date 日期类
	 * @return LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		ZoneId zone = ZoneId.systemDefault();
		if (Objects.isNull(date)) {
			return null;
		}
		return LocalDateTime.ofInstant(date.toInstant(), zone).toLocalDate();
	}

	/**
	 * date转为Stamp
	 *
	 * @param date 日期类
	 * @return String
	 */
	public static String dateToStamp(Date date) {
		long ts = date.getTime();
		return String.valueOf(ts);
	}

	/**
	 * 获取时间戳账所在月中最晚的时间点
	 *
	 * @param timeStamp timeStamp
	 * @return Date
	 */
	public static Date getLastDayOfMonth(Long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(timeStamp));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * 获取一天中最早的时间点
	 *
	 * @param timeStamp 时间戳
	 * @return Date
	 */
	public static Date getFirstDayOfMonth(Long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(timeStamp));
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}


	/**
	 * 获取一天中最开始的时间
	 *
	 * @param date 日期
	 * @return Date
	 */
	public static Date getStartTimeOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取一天中最开始的时间
	 *
	 * @param timeStamp 时间戳
	 * @return Date
	 */
	public static Date getStartTimeOfDay(Long timeStamp) {
		return getStartTimeOfDay(new Date(timeStamp));
	}


	/**
	 * 获取一天中最晚的时间
	 *
	 * @param timeStamp 时间戳
	 * @return Date
	 */
	public static Date getLastTimeOfDay(Long timeStamp) {
		return getLastTimeOfDay(new Date(timeStamp));
	}

	/**
	 * 获取一天中最晚的时间
	 *
	 * @param date 日期
	 * @return Date
	 */
	public static Date getLastTimeOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * 获取指定时间到周一的天数
	 *
	 * @param date  指定日期
	 * @return  int
	 */
	private static int getMondayPlus(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}

	/**
	 * 获取指定日期内所属周的周一
	 *
	 * @param timeStamp 指定日期
	 * @return Date
	 */
	public static Date getCurrentMonday(Long timeStamp) {
		int mondayPlus = getMondayPlus(new Date(timeStamp));
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTimeInMillis(timeStamp);
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		return getStartTimeOfDay(currentDate.getTime());
	}


	/**
	 * 获取指定日期内所属周的周日
	 *
	 * @param timeStamp 指定日期
	 * @return Date
	 */
	public static Date getCurrentSunday(Long timeStamp) {
		int mondayPlus = getMondayPlus(new Date(timeStamp));
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTimeInMillis(timeStamp);
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		return getLastTimeOfDay(currentDate.getTime());
	}


	/**
	 * 获取俩个时间点中相隔的所有周天，包括头和尾
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return List<Date>
	 */
	public static List<Date> getIntervalWeekDayList(Long startTime, Long endTime) {
		if (startTime > endTime) {
			throw new IllegalArgumentException("开始时间大于结束时间");
		}
		List<Date> dateList = new ArrayList<>();
		Date date = DateUtils.addDays(new Date(startTime), +7);
		Date monday = getCurrentMonday(endTime);
		while (date.getTime() < monday.getTime()) {
			dateList.add(date);
			date = DateUtils.addDays(date, +7);
		}
		dateList.add(new Date(startTime));
		dateList.add(new Date(endTime));
		return dateList;
	}


	/**
	 * 获取俩个时间点中相隔的所有月天
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return List<Date>
	 */
	public static List<Date> getIntervalMonthDayList(Long startTime, Long endTime) {
		if (startTime > endTime) {
			throw new IllegalArgumentException("开始时间大于结束时间");
		}
		List<Date> dateList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(startTime));
		calendar.add(Calendar.MONTH, +1);
		Date day = getFirstDayOfMonth(endTime);
		while (calendar.getTime().getTime() < day.getTime()) {
			dateList.add(calendar.getTime());
			calendar.add(Calendar.MONTH, +1);
		}
		dateList.add(new Date(startTime));
		dateList.add(new Date(endTime));
		return dateList;
	}




	/**
	 * 获取俩个时间点中相隔的天
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return List<Date>
	 */
	public static List<Long> getIntervalDayList(Long startTime, Long endTime) {
		if (startTime > endTime) {
			throw new IllegalArgumentException("开始时间大于结束时间");
		}
		List<Long> dateList = new ArrayList<>();
		Date date = DateUtils.addDays(new Date(startTime), +1);
		long time = getStartTimeOfDay(endTime).getTime();
		while (date.getTime() < time) {
			dateList.add(getStartTimeOfDay(date).getTime());
			date = DateUtils.addDays(date, +1);
		}
		dateList.add(getStartTimeOfDay(startTime).getTime());
		dateList.add(getStartTimeOfDay(endTime).getTime());
		return dateList;
	}


}
