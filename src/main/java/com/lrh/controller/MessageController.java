package com.lrh.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lrh.model.NoticeMessage;
import com.lrh.model.User;
import com.lrh.service.NoticeMessageService;
import com.lrh.service.UserService;

@RequestMapping(value = "/msg")
@Controller
public class MessageController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	NoticeMessageService noticeService;
	
	//提到我的消息
	@RequestMapping(value = "/atMe")
	public String login(HttpServletRequest request,
			HttpServletResponse response) {
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			NoticeMessage msgParam =new NoticeMessage();
			msgParam.setUserId(user.getId());
			msgParam.setStart(0L);
			msgParam.setMax(10L);
			List<NoticeMessage> listMsg=noticeService.findNoticMessageByParam(msgParam);
			request.setAttribute("msgList", listMsg);
			return "noticeMessageList";
		}
		try {
			response.sendRedirect("/LRH");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//设为已读
	@RequestMapping(value = "/changeStatus")
	public String changeStatus(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dataId", required = true) long dataId) {
		
		User user=(User) request.getSession().getAttribute("user");
		if(user !=null && dataId>0){
			boolean f =noticeService.updateByIdAndUserId(dataId,user.getId(),true);
			if(f){
				this.outJsonResult(response, true, "已将通知设为已读!", null);
			}else{
				this.outJsonResult(response, false, "操作失败,可能数据不存在,请刷新试试!", null);
			}
		}else{
			this.outJsonResult(response, false, "操作失败,请刷新后重试!", null);
		}
		return null;
	}
	
	//异步加载数据
	@RequestMapping(value = "/more")
	public String loadMore(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = true) int page) {
		
		User user=(User) request.getSession().getAttribute("user");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("totalPage", 0);
		resultMap.put("nowPage",  0);
		if(user !=null){
			NoticeMessage msgParam =new NoticeMessage();
			msgParam.setUserId(user.getId());
			long count=noticeService.getNoticMessageCountByParam(msgParam);
			Map<String,Long> map = this.getTotalPage(count, 10, page);
			msgParam.setStart(map.get("start"));
			msgParam.setMax(10L);
			List<NoticeMessage> listMsg=noticeService.findNoticMessageByParam(msgParam);
			resultMap.put("totalPage", map.get("totalPage"));
			resultMap.put("nowPage",  map.get("nowPage"));
			if(listMsg==null || listMsg.size()<1)
				this.outJsonResult(response, false, "没有啦~", resultMap);
			else{
				resultMap.put("dataList",  listMsg);
				this.outJsonResult(response, true, "本次为您悄悄加载了"+listMsg.size()+"条数据~", resultMap);
			}	
		}else{
			this.outJsonResult(response, false, "您已掉线,请重新登录!", resultMap);
		}
		return null;
	}
}
