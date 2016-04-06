package com.lrh.task;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.lrh.controller.UserController;

/**
 * 用户状态维护定时器
 *
 * @author Lai Rihai
 */
public class UserStatusTimerTask extends TimerTask
{
	private Logger log = Logger.getLogger(UserStatusTimerTask.class);

	@Override
	public void run()
	{
		try
		{
			//System.out.println("-----------------------[定时器启动了]------------------------");
			int times=0;
			for(String userName:UserController.userOnlineMap.keySet()){
				times = (Integer) UserController.userOnlineMap.get(userName).get("times")+1;
				if(times>UserController.maxTime){
					UserController.userOnlineMap.remove(userName);
					System.err.println("--------------->[ "+userName+" ]的用户离线了");
				}else{
					UserController.userOnlineMap.get(userName).put("times", times);
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}

}
