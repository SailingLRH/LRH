package com.lrh.udp;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lrh.net.BaseIoHandler;
import com.lrh.net.MessageDecoder;
import com.lrh.net.MessageEncoder;
import com.lrh.net.SocketMsgData;

public class UDPClient
{
	private static IoConnector connector;

	public static void init()
	{
		connector = new NioDatagramConnector();
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new MessageEncoder(), new MessageDecoder()));
		connector.setHandler(new BaseIoHandler());
	}

	private static IoSession connect(InetSocketAddress address)
	{
		ConnectFuture connectFuture = connector.connect(address);
		connectFuture.awaitUninterruptibly();
		System.out.println(address.getHostName()+"连接成功");
		IoSession session = connectFuture.getSession();
		return session;
	}

	public static void close()
	{
		connector.dispose(true);
	}

	public static IoSession connect(String hostname, int port)
	{
		InetSocketAddress address = new InetSocketAddress(hostname, port);
		return connect(address);
	}
	///测试UDP连接并发送消息
	public static void main(String[] args)
	{
		UDPClient.init();
		List<String> ucList = new ArrayList<String>();
		for (int i = 1; i < 40; i++)
		{
			ucList.add(i+"");
		}
		
		for (String uc : ucList)
		{
			IoSession session=connect("127.0.0.1",7878);
			SocketMsgData smg = new SocketMsgData("", "", 1);
			try
			{
				JSONObject linkInfo = new JSONObject();
				linkInfo.put("uc", uc);
				linkInfo.put("LinkNo", "0");
				smg.writeJSONObject("LinkInfo", linkInfo);
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(smg.toString());
			smg.send(session);
		}
	}

}
