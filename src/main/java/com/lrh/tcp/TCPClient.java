package com.lrh.tcp;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lrh.net.BaseIoHandler;
import com.lrh.net.MessageDecoder;
import com.lrh.net.MessageEncoder;
import com.lrh.net.SocketMsgData;


public class TCPClient
{
	private static IoConnector connector;

	public static void init()
	{
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new MessageEncoder(), new MessageDecoder()));
		connector.setHandler(new BaseIoHandler());
		connector.setConnectTimeoutMillis(30000);
	}

	private static IoSession connect(InetSocketAddress address)
	{
		ConnectFuture connectFuture = connector.connect(address);
		connectFuture.awaitUninterruptibly();
		IoSession session = connectFuture.getSession();
		System.out.println(address.getHostName()+"连接成功");
		return session;
	}

	public static void close()
	{
		connector.dispose(true);
	}

	private static ConcurrentHashMap<String,IoSession> sessionMap=new ConcurrentHashMap<String,IoSession>();

	public static IoSession connect(String hostname, int port)
	{
		String key=hostname+"_"+port;
		if(!sessionMap.containsKey(key)){
			InetSocketAddress address = new InetSocketAddress(hostname, port);
			IoSession session= connect(address);
			sessionMap.put(key, session);
			return session;
		}
		IoSession session=sessionMap.get(key);
		if(session.isConnected()&&!session.isClosing()){
			return session;
		}else{
			IoSession newsession=null;
			try{
			session.close(true);
			sessionMap.remove(key);
			InetSocketAddress address = new InetSocketAddress(hostname, port);
			newsession= connect(address);
			sessionMap.put(key, newsession);
			}catch(Exception e){
			}
			return newsession;
		}
	}

	
	///测试TCP连接并发送消息
	public static void main(String[] args){
		TCPClient.init();
		List<String> ucList = new ArrayList<String>();
		for (int i = 1; i < 40; i++)
		{
			ucList.add(i+"");
		}
		
		for (String uc : ucList)
		{
			IoSession session=connect("127.0.0.1",5656);
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
