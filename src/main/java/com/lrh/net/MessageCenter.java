package com.lrh.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MessageCenter
{

	static private MessageCenter instance = new MessageCenter();

	static public MessageCenter getInstance()
	{
		return instance;
	}

	private ExecutorService pool;

	public MessageCenter()
	{
		pool = Executors.newCachedThreadPool();
	}

	/**
	 * 解析消息;
	 * 
	 * @param smd
	 * @param pc
	 */
	public void parseMessage(SocketMsgData smd, PlayerConnect pc)
	{

		PlayerMessageThread thread = new PlayerMessageThread(false);
		thread.addMessage(smd, pc);
		pool.execute(thread);

	}

}
