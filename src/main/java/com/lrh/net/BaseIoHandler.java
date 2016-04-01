package com.lrh.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;


public class BaseIoHandler extends IoHandlerAdapter
{
	@Override
	public void sessionOpened(IoSession session) throws Exception
	{
		// 打开一个socket连接
		try
		{
			PlayerConnect connect = new PlayerConnect(session);
			session.setAttribute("playerConnectSession", connect);
			System.out.println("open" + session.getId());
			// 解析并处理；
			connect.init();
		}
		catch (Exception e)
		{
			session.close(true);
		}
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		PlayerConnect pc = (PlayerConnect) session.getAttribute("playerConnectSession");
		SocketMsgData data = (SocketMsgData) message;
		System.out.println("收到消息：" + data.getMainType());
		pc.parse(data, session);
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception
	{
		// 发送消息
		System.out.println("发送消息：" + ((byte[]) arg1)[0]);
		byte[] bs = (byte[]) arg1;
		for (int i = 0; i < bs.length; i++)
		{
			System.out.print(bs[i] + " ");
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		System.out.println("IP:" + session.getRemoteAddress().toString() + "断开连接");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		// session创建
		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
		cfg.setKeepAlive(true);
		cfg.setSoLinger(0);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception
	{
		System.out.println("IDLE " + session.getIdleCount(status)+",关闭连接执行...");
		session.close(true);
	}

}
