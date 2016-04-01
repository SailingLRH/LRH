package com.lrh.util;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 得到中文首字母
 **/ 


public class ToolGetFirstLetter{
	public static String getPinYinHeadChar(String str) {
		String convert = ""; 
		for (int j = 0; j < str.length(); j++) { 
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word); 
			if (pinyinArray != null) { 
				convert += pinyinArray[0].charAt(0);
			} else {  
				convert += word; 
			}         
		}          
		return convert;
	}
}