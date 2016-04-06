package com.lrh.net;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.lrh.handler.IModuleHandler;


/**
 * 消息容器
 * 
 * @author Administrator
 * 
 */
public class PlayerMessageThread extends Thread
{
	private Logger log = Logger.getLogger(PlayerMessageThread.class);

	/**
	 * sleep 休眠毫秒
	 */
	protected int sleep = 1;

	/**
	 * 消息列表；1
	 */
	private ConcurrentLinkedQueue<PlayerMessageData> msgQueue = new ConcurrentLinkedQueue<PlayerMessageData>();

	/**
	 * 是否为常驻运行
	 */
	protected boolean isLoopRun = false;

	public PlayerMessageThread(boolean isLoopRun)
	{
		this.isLoopRun = isLoopRun;
	}

	public void run()
	{

		if (!isLoopRun)
		{
			handle();
		}
		else
		{

			while (true)
			{

				handle();

				try
				{
					Thread.sleep(sleep);
				}
				catch (InterruptedException e)
				{
					log.error(e.getMessage(), e);
				}

			}
		}

	}

	/**
	 * 收到消息;
	 */
	public synchronized boolean addMessage(SocketMsgData smd, PlayerConnect pc)
	{
		PlayerMessageData pmd = new PlayerMessageData();
		pmd.msg = smd;
		pmd.player = pc;
		return msgQueue.add(pmd);
	}

	/**
	 * 处理消息;
	 */
	protected void handle()
	{
		PlayerMessageData pmd = msgQueue.poll();
		if (null != pmd)
		{
			doMessage(pmd.msg, pmd.player.moduleList, pmd.player);
		}

	}


	/**
	 * 处理一条消息;
	 * 
	 * @param data
	 * @param moduleList
	 * @param pc
	 */
	private void doMessage(SocketMsgData data, List<IModuleHandler> moduleList, PlayerConnect pc)
	{

		// System.out.println(" ########### doMessage ###########" );

		boolean isHandler = false;
		try
		{
			for (IModuleHandler hdl : moduleList)
			{
				if (hdl != null && hdl.mainType().equals( data.getMainType()) /*&& hdl.appId() == data.appId*/)
				{
					isHandler = hdl.parse(data);
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		if (!isHandler)
		{
//			log.error("Receive unknow command! player name = " + pc.accountModule.getAccount().getAccount() + " cmd="
//					+ data.mainCmd + "," + data.subCmd);
		}
	}

}
