package com.lrh.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lrh.model.ChatOnline;
import com.lrh.model.NoticeMessage;
import com.lrh.model.User;
import com.lrh.service.NoticeMessageService;
import com.lrh.service.UserService;
import com.lrh.util.ToolContext;
import com.lrh.util.ToolDate;

/**
 * 在线聊天
 */
@RequestMapping(value = "/chat")
@Controller
public class ChatOnlineController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(ChatOnlineController.class);
	
	/**
	 * 用户在线聊天map<fromUserName,Map<toUserName,List>>
	 */
	public static ConcurrentHashMap<String,Map<String, List<ChatOnline>>> chatOnlineMap = new ConcurrentHashMap<String,Map<String, List<ChatOnline>>>();
	
	@Autowired
	UserService userService;
	@Autowired
	NoticeMessageService noticeService;
	
	//发送新消息
	@RequestMapping(value = "/send")
	public String send(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "content", required = true) String content,
			@RequestParam(value = "toUserName", required = true) String toUserName) {
		User toUser = userService.getUserByUserName(toUserName);
		if(null == toUser){
			this.outJsonResult(response, false, "聊天对象不存在,请刷新后重试!", null);
			return null;
		}
		
		User user = (User) request.getSession().getAttribute("user");
		
		if(null==user){
			this.outJsonResult(response, false, "您已掉线,请重新登录!", null);
			return null;
		}
		String fromUserName = user.getUserName();
		Map<String,List<ChatOnline>> messageMap = new HashMap<String,List<ChatOnline>>();
		if(chatOnlineMap.containsKey(fromUserName)){
			messageMap=chatOnlineMap.get(fromUserName);
		}
		
		ChatOnline message = new ChatOnline();
		message.setContent(content);
		message.setToUserName(toUserName);
		message.setTime(ToolDate.getNowTime());
		message.setFromUserName(fromUserName);
		
		List<ChatOnline> chatList = new ArrayList<ChatOnline>();
		if(messageMap.containsKey(toUserName)){
			chatList = messageMap.get(toUserName);
		}
		chatList.add(message);
		messageMap.put(toUserName, chatList);
		
		chatOnlineMap.put(fromUserName, messageMap);
		
		//生成一条动态
		NoticeMessage notice=new NoticeMessage();
		notice.setUserId(toUser.getId());
		notice.setIsRead(false);
		notice.setModel((short)ToolContext.getSysPropInt("module_spitslot"));
		notice.setDataId(Long.valueOf(user.getId()));
		notice.setInitiatorId(user.getId());
		notice.setTitle("您有新的在线消息!");
		String simpleContent=content.length()>450?(content.substring(0, 450)+"..."):content;
		notice.setContent("["+user.getUserName()+"] :<br/>\""+simpleContent+"\"");
		notice.setMessageType((short)ToolContext.getSysPropInt("message_interactive"));
		notice.setUrl("/data/tail?model="+ToolContext.getSysProp("chat_online")+"&dataId="+user.getId());
		noticeService.insertSelective(notice);
		
		this.outJsonResult(response, true, "操作成功!", null);
		return null;
	}
	
	//接收新消息
	@RequestMapping(value = "/receive")
	public String receive(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "fromUserName", required = true) String fromUserName) {
		
		User user = (User) request.getSession().getAttribute("user");
		
		if(null==user){
			this.outJsonResult(response, false, "您已掉线,请重新登录!", null);
			return null;
		}
		
		String userName = user.getUserName();
		if(!chatOnlineMap.containsKey(userName) || chatOnlineMap.get(userName).size()<1){
			this.outJsonResult(response, true, "暂无新消息!", 0);
			return null;
		}
		
		Map<String,List<ChatOnline>> messageMap = chatOnlineMap.get(userName);
		if(!messageMap.containsKey(fromUserName) || messageMap.get(fromUserName).size()<1){
			this.outJsonResult(response, true, "暂无新消息!", 0);
			return null;
		}
		this.outJsonResult(response, true, "获取了新消息!", messageMap.get(fromUserName));
		
		messageMap.remove(fromUserName);
		return null;
	}
}
