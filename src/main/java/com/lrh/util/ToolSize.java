package com.lrh.util;

import java.text.DecimalFormat;

/**
 * 一个单位换算的工具类.可将字节换算成KB,MB,GB
 * @author Sailing_LRH
 * @since 2015年11月4日
 */
public class ToolSize {
	private static DecimalFormat df=new java.text.DecimalFormat("#.00");  
	public static String conversion(Long size){
		if(size!=null){
			if(size<1024){
				return size+"B";
			}else if(size<(1024*1024)){
				return df.format(size/1024.0)+"KB";
			}else if(size<(1024*1024*1024)){
				return df.format(size/1048576.0)+"MB";
			}else{
				return df.format(size/1073741824.0)+"GB";
			}
		}
		return null;
	}
}
