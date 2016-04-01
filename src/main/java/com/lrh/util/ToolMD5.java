package com.lrh.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ToolMD5 {

	/**
	 * MD5 加密
	 * @param str 要加密的字符串
	 * @param lenthIs16 true:16位长度;false:32位长度
	 * @return string 加密后的字符串
	 */
	public static String Md5(String str,boolean lenthIs16) {
		String result="";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			
			if(lenthIs16){
				result=buf.toString().substring(8, 24);
			}else{
				result=buf.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			System.err.println("MD5加密发生异常:"+e.getMessage());
		}
		//System.err.println("Md5加密前: "+str+"\t加密后: " + result);
		return result;
	}

	/**
	 * 用于校验密码是否正确
	 * @param str
	 * @param md5Str
	 * @param lenthIs16
	 * @return true/false
	 */
	public static Boolean isSame(String str,String md5Str,boolean lenthIs16){
		String md5Str1= Md5(str,lenthIs16);
		if(md5Str1!=null && md5Str1.equals(md5Str)){
			return true;
		}else{
			System.out.println(md5Str1+"  !=  "+md5Str);
			return false;
		}
	}
}