package com.lrh.task;

import java.util.Timer;
import org.apache.log4j.Logger;

public class CfgTimer
{
	private static CfgTimer instance = new CfgTimer();

	/**
	 * 定时器单例
	 */
	public static CfgTimer Instance()
	{
		return instance;
	}
	
	public void init()
	{
		Timer timer = new Timer();
		// 每3秒执行1次
		timer.scheduleAtFixedRate(new UserStatusTimerTask(), 1000, 3*1000);
	}
}
