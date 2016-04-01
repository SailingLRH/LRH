package com.lrh.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 * 获取资源文件值和WebApplicationContext的工具类
 * @author Sailing_LRH
 * @since 2015年10月26日
 */
public class ToolContext {
	private static WebApplicationContext springContext;
	private static Properties configProperties;

	static{
		InputStream in = ToolContext.class.getClassLoader().getResourceAsStream("attribute.properties");
		configProperties=new Properties();
		try {
			configProperties.load(in);
		} catch (IOException e) {
			System.err.println("读取资源文件发生异常:"+e.getMessage());
		}
	}
	
	public static WebApplicationContext getSpringContext() {
		return springContext;
	}

	public static void setSpringContext(WebApplicationContext springContext) {
		ToolContext.springContext = springContext;
	}


	public static Properties getConfigProperties() {
		return configProperties;
	}

	public static void setConfigProperties(Properties configProperties) {
		ToolContext.configProperties = configProperties;
	}

	public static String getSysProp(String key) {
		if (null != configProperties) {
			return configProperties.getProperty(key);
		}
		return null;
	}

	public static int getSysPropInt(String key) {
		if (null != configProperties) {
			String prop = configProperties.getProperty(key);
			return NumberUtils.toInt(prop, 0);
		}
		return 0;
	}

	public static int getSysPropInt(String key, int defaultValue) {
		if (null != configProperties) {
			String prop = configProperties.getProperty(key);
			return NumberUtils.toInt(prop, defaultValue);
		}
		return defaultValue;
	}

}
