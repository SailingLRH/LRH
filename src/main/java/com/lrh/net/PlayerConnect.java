package com.lrh.net;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.lrh.handler.DemoHandler;
import com.lrh.handler.IModuleHandler;

/**
 * 消息处理的入口
 * @author Administrator
 * 2015年6月8日
 */
public class PlayerConnect implements Cloneable
{
	private Logger log = Logger.getLogger(PlayerConnect.class);
	
	public PlayerConnect(IoSession session)
	{
		this.session = session;

	}
	
	/**
	 * socket 连接出口引用;
	 */
	private IoSession session = null;
	
	/**
	 * 处理器列表
	 */
	public List<IModuleHandler> moduleList = new ArrayList<IModuleHandler>();
	
	
	
	public void init(){
		moduleList.add(new DemoHandler(this));
	}
	/**
	 * 解析
	 * */
	public void parse(SocketMsgData message, IoSession session) throws Exception
	{
		send(message, session);
	}
	
	
	/**
	 * 发送到各个模块解析 ，并执行相关逻辑
	 * */
	public void send(SocketMsgData message, IoSession session) throws Exception
	{

		if (session == null || session.isClosing())
		{
			return;
		}
		SocketMsgData data = (SocketMsgData) message;
//		// 穿墙命令，忽略它；
//		if (data.mainCmd == 116 && data.subCmd == 103)
//			return;
//		// 843端口 <policy-file-request/>
//		if (data.mainCmd == 60 && data.subCmd == 112)
//		{
//
//			String xml = "<?xml version=\"1.0\"?><cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";
//			byte[] bytes = xml.getBytes(Charset.forName("utf-8"));
//			IoBuffer buf = IoBuffer.allocate(bytes.length);
//			buf.put(bytes);
//			buf.flip();
//
//			session.write(buf);
//			return;
//		}

		MessageCenter.getInstance().parseMessage(data, this);
	}
	
	/**
	 * 取得客户端IP
	 * 
	 * @return
	 */
	public String getIp()
	{
		if (session == null)
			return "null";
		String clientIP = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		return clientIP;
	}
	
	public IoSession getSession()
	{
		return session;
	}

	public void setSession(IoSession session)
	{
		this.session = session;
	}
	
	public PlayerConnect clone()
	{
		PlayerConnect copy = null;
		try
        {
	        copy = (PlayerConnect) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
        	log.error(e.getMessage());
        }
		
		return copy;
	}

}
