package com.lrh.handler;

import com.lrh.net.PlayerConnect;
import com.lrh.net.SocketMsgData;

/**
 * 模拟处理
 * 
 * @author Administrator 2015年6月9日
 */
public class DemoHandler implements IModuleHandler {
	protected PlayerConnect _connect;

	public DemoHandler(PlayerConnect connect) {
		this._connect = connect;
	}

	@Override
	public boolean parse(SocketMsgData smgData){
		String subType = smgData.getSubType();
		if(subType.equals("")){
			SocketMsgData smg = new SocketMsgData("mainType", "subType", 1);
			smg.send(_connect);
			return true;
		}
		return false;
	}

	@Override
	public String mainType() {
		return "mianType";
	}

}
