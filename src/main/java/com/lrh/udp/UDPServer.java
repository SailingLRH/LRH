package com.lrh.udp;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.lrh.net.BaseIoHandler;
import com.lrh.net.MessageDecoder;
import com.lrh.net.MessageEncoder;
import com.lrh.util.ToolContext;


public class UDPServer
{
	private static UDPServer udpServer = new UDPServer();

	private NioDatagramAcceptor acceptor;

	public static UDPServer getInstance()
	{
		return udpServer;
	}

	public void init()
	{
		try
		{
			acceptor = new NioDatagramAcceptor();// 创建一个UDP的接收器
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60); // 1分钟
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new MessageEncoder(), new MessageDecoder()));
			BaseIoHandler tcpiohandler = new BaseIoHandler();
			acceptor.setHandler(tcpiohandler);
			// 绑定端口
			acceptor.bind(new InetSocketAddress(Integer.valueOf(ToolContext.getSysProp("udp_port"))));
			int sessionCount = acceptor.getManagedSessionCount();
			System.out.println("UDP server init successful! port:" + ToolContext.getSysProp("udp_port") + ", session count:"
					+ sessionCount);
		}
		catch (Exception e)
		{
			System.out.println("UDP server init fail!");
		}
	}
	public static void main(String[] args)
	{
		UDPServer.getInstance().init();
	}

}
