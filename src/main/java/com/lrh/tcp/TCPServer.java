package com.lrh.tcp;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.lrh.net.BaseIoHandler;
import com.lrh.net.MessageDecoder;
import com.lrh.net.MessageEncoder;
import com.lrh.util.ToolContext;

public class TCPServer
{
	private static TCPServer tcpServer = new TCPServer();

	private static Logger log = Logger.getLogger(TCPServer.class);

	private IoAcceptor acceptor = null; // 创建连接

	public static TCPServer getInstance()
	{
		return tcpServer;
	}

	public void init()
	{
		try
		{
			// 创建一个非阻塞的server 端 socket（TCP协议）;
			acceptor = new NioSocketAcceptor();
			// 设置读取数据的缓冲区大小；
			acceptor.getSessionConfig().setReadBufferSize(2048);
			// 读写通道无操作进入空闲状态;
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60); // 2分钟
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageEncoder(), new MessageDecoder()));

			BaseIoHandler tcpiohandler = new BaseIoHandler();
			acceptor.setHandler(tcpiohandler);
			// 绑定端口
			acceptor.bind(new InetSocketAddress(Integer.valueOf(ToolContext.getSysProp("tcp_port"))));

			int sessionCount = acceptor.getManagedSessionCount();
			System.out.println("TCP server init successful! port:" + ToolContext.getSysProp("tcp_port") + ", session count:"
					+ sessionCount);
		}
		catch (Exception e)
		{
			System.out.println("TCP server init fail!");
			log.error("TCP server init fail!" + e.getMessage());
		}
	}

	public static void main(String[] args)
	{
		TCPServer.getInstance().init();
	}

}
