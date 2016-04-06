package com.lrh.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolDate {
	public static String JUST_NOW="刚刚";
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 以 yyyy-MM-dd HH:mm:ss 输出当前时间
	 * @author Sailing_LRH
	 * @since 2015年12月6日
	 * @return
	 */
	public static String getNowTime(){
		return sdf.format(new Date());
	}
	
	/**
	 * 如果时间刚过去不久,则以"xxx之前"输出
	 * @author Sailing_LRH
	 * @since 2015年12月6日
	 * @param time
	 * @return
	 */
	public static String formatTime(String time){
		try {
			Long date1=sdf.parse(time).getTime();
			Long date2=new Date().getTime();
			Long sec=(date2-date1)/1000;
			if(sec>0){
				if(sec<60) return JUST_NOW;
				else if(sec<(60*60)) return (sec/60)+"分钟前";
				else if(sec<(60*60*24)) return (sec/3600)+"小时前";
				else if(sec<(60*60*24*2)) return "1天前";
				else if(sec<(60*60*24*3)) return "2天前";
				else return time;
			}else{
				return time;
			}
		} catch (ParseException e) {
			System.err.println("DateUtil.formatTime() 方法在格式化时间时发生异常:"+e.getMessage());
			return time;
		}
	}
}
