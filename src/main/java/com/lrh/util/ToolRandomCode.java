package com.lrh.util;

import java.util.Random;

public class ToolRandomCode {
	
	private static char[] char1={'a','b','c','d','e','f','g','h','i','j','k','m','n','p','q','r',
			's','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','P',
			'Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
	private static char[] char2={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r',
			's','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',
			'Q','R','S','T','U','V','W','X','Y','Z'};
	private static char[] char3={'0','1','2','3','4','5','6','7','8','9'};
	
	/**
	 * 获取随机字符
	 * @author Sailing_LRH
	 * @since 2015年9月23日
	 * @param length
	 * @return string 只有字母
	 */
	public static String getRandomChar(int length){
		Random r=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
			sb.append(char2[r.nextInt(char2.length)]);
		}
		return sb.toString();
	}
	/**
	 * 获取随机数字
	 * @author Sailing_LRH
	 * @since 2015年9月23日
	 * @param length
	 * @return string 只有数字
	 */
	public static String getRandomNumber(int length){
		Random r=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
			sb.append(char3[r.nextInt(char3.length)]);
		}
		return sb.toString();
	}
	/**
	 * 获取随机字母和数字组合
	 * @author Sailing_LRH
	 * @since 2015年9月23日
	 * @param length
	 * @return string 既有数字又有字母
	 */
	public static String getRandomCharOrNumber(int length){
		Random r=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
			sb.append(char1[r.nextInt(char1.length)]);
		}
		return sb.toString();
	}

}
