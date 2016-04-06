package com.lrh.handler;

import com.lrh.net.SocketMsgData;

/**
 * 处理模块接口 ，实现这个接口，就可以处理对应的消息
 * 
 * @author Administrator 2015年6月9日
 */
public interface IModuleHandler
{
	/**
	 * 消息解析处理
	 * 
	 * @param smgData
	 * @return
	 */
	boolean parse(SocketMsgData smgData);

	/**
	 * 主命令类型，这个方法的返回值，代表着本模块可以处理哪类消息，与数据包中的mainType对应
	 * 应避免不同处理模块使用相同返回值。
	 * @return
	 */
	String mainType();

}
