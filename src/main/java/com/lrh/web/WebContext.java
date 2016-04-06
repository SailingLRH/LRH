package com.lrh.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.lrh.task.CfgTimer;
import com.lrh.tcp.TCPClient;
import com.lrh.tcp.TCPServer;
import com.lrh.udp.UDPServer;


/**
 * @author zs 2014年5月3日
 */
public class WebContext implements ServletContextListener
{

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		try
		{
			// 初始化TCP客户端
			//TCPClient.init();
			// 初始化TCP服务器
			//TCPServer.getInstance().init();
			//初始化UDP服务端
			//UDPServer.getInstance().init();
			//CfgTimer初始化
			CfgTimer.Instance().init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
/*
	private static void initSysProp()
	{
		Properties prop = new Properties();
		try
		{
			String OS = System.getProperty("os.name");
			if(OS.toLowerCase().indexOf("linux") == -1)
			{
			  prop.load(WebContext.class.getClassLoader().getResourceAsStream("conf/windowsconf.properties"));
			}
			else{
				prop.load(WebContext.class.getClassLoader().getResourceAsStream("conf/linuxconf.properties"));
			}
			ContextUtil.setSysProp(prop);
		}
		catch (IOException e)
		{
			System.err.println("============>初始化加载配置文件发生异常:"+e.getMessage());
		}
	}
*/	
}
