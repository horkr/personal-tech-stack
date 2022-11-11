package com.horkr.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lulianghong
 * @ClassName: ScheduleUtil
 * @Description: TODO
 * @date 2019/12/30 14:08
 */
public class ScheduleUtil {

	private static Logger logger = LogManager.getLogger(ScheduleUtil.class);

	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(80);

	/**
	 * 定时更新缓存
	 *
	 * @param executor executor
	 * @param period   间隔时间
	 * @param timeUnit 时间单位
	 * @param taskName 任务名
	 */
	public static void scheduleUpdate(Runnable executor, long period, TimeUnit timeUnit, String taskName) {
		scheduleUpdate(executor, 0, period, timeUnit, taskName);
	}

	/**
	 * 定时更新缓存
	 *
	 * @param executor executor
	 * @param dealy    初次延迟
	 * @param period   间隔时间
	 * @param timeUnit 时间单位
	 * @param taskName 任务名
	 */
	public static void scheduleUpdate(Runnable executor, long dealy, long period, TimeUnit timeUnit, String taskName) {
		pool.scheduleAtFixedRate(() -> {
			try {
				executor.run();
			} catch (Exception e) {
				logger.error("定时更新缓存出错,{}", taskName, e);
			}
		}, dealy, period, timeUnit);
	}
}
