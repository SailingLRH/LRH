package com.lrh.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
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

import com.lrh.model.Dynamic;
import com.lrh.model.User;
import com.lrh.service.DynamicService;
import com.lrh.service.UserService;


@RequestMapping(value = "/")
@Controller
public class IndexController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	DynamicService dynamicService;
	
	
	/**
	 * 访问首页
	 * @author Sailing_LRH
	 * @since 2015年10月9日
	 * @param request
	 * @param response
	 * @return LRH 首页
	 */
	@RequestMapping(value = "/LRH")
	public String showIndex(HttpServletRequest request,HttpServletResponse response) {
		Dynamic param =new Dynamic();
		param.setStart(0L);
		param.setMax(10L);//一次获取10条数据
		List<Dynamic> dynamicList = dynamicService.findNoticDynamicByParam(param);
		request.setAttribute("dynamicList", dynamicList);
		return "LRH";
	}
	
	//异步加载数据
	@RequestMapping(value = "/moreDynamic")
	public String loadMore(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = true) int page) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		Dynamic param =new Dynamic();
		long count=dynamicService.getDynamicCountByParam(param);
		
		Map<String,Long> map = this.getTotalPage(count, 10, page);
		param.setStart(map.get("start"));
		param.setMax(10L);
		
		List<Dynamic> dynamicList = dynamicService.findNoticDynamicByParam(param);
		resultMap.put("totalPage", map.get("totalPage"));
		resultMap.put("nowPage",  map.get("nowPage"));
		
		User user = (User) request.getSession().getAttribute("user");
		resultMap.put("me",  user==null?-1:user.getId());
		
		if(dynamicList==null || dynamicList.size()<1)
			this.outJsonResult(response, false, "没有啦~", resultMap);
		else{
			resultMap.put("dataList",  dynamicList);
			this.outJsonResult(response, true, "本次为您悄悄加载了"+dynamicList.size()+"条数据~", resultMap);
		}	
		return null;
	}
	
	//加载图片
	@RequestMapping(value = "/loadImg")
	public String loadImg(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "imgPath", required = true) String imgPath){
		try {
			this.loadImage(request,response, imgPath);
		} catch (IOException e) {
			logger.error("加载头像发生异常:"+e.getMessage());
		}
		return null;
	}
	
	//加载其它文件
	@RequestMapping(value = "/loadFile")
	public String loadFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "filePath", required = true) String filePath,
			@RequestParam(value = "fileType", required = true) String fileType){
		
		if(!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(fileType)){
			return null;
		}
		try {
			this.loadFile(response, filePath,fileType);
		} catch (IOException e) {
			logger.error("加载头像发生异常:"+e.getMessage());
		}
		return null;
	}
	
	//开始计时
	@RequestMapping(value = "/visitStart")
	public String visitStart(HttpServletRequest request,HttpServletResponse response){
		String ip0=getIpAddr(request);
		//从application作用域取出ip
		String ip=(String) request.getSession().getServletContext().getAttribute(ip0);
		if(ip==null){//如果为空,表示是第一次请求这个方法
			ip=ip0;
			request.getSession().getServletContext().setAttribute(ip0, ip);
			logger.info("---------->ip="+ip+"的用户开始访问...");
		}
		System.err.println("访问者ip="+ip);
		
		this.outHTML(response, ip);
		return null;
	}
	
	/**
	 * 用户离开
	 * @author Sailing_LRH
	 * @since 2015年10月9日
	 * @param request
	 * @param response
	 * @return null
	 */
	@RequestMapping(value = "/leave")
	public String leave(HttpServletRequest request,HttpServletResponse response) {
		String ip0=getIpAddr(request);
		Date visitStart=(Date) request.getSession().getAttribute("visitStart");
		if(visitStart!=null){
			String ip=(String) request.getSession().getServletContext().getAttribute(ip0);
			Date visitEnd=new Date();
			long diff = visitEnd.getTime() - visitStart.getTime();
			long sec = diff / (1000);
			String visitTime="0";
			if(sec<60){//一分钟之内
				visitTime=sec+"秒";
			}else if(sec<3600){//一小时之内
				int min=(int) sec/60;
				sec%=60;
				visitTime=min+"分"+sec+"秒";
			}else if(sec<86400){//一天之内
				int hou=(int) sec/3600;
				sec%=3600;
				int min=(int) sec/60;
				sec%=60;
				visitTime=hou+"小时"+min+"分"+sec+"秒";
			}else{
				int day=(int) sec/86400;
				sec%=86400;
				int hou=(int) sec/3600;
				sec%=3600;
				int min=(int) sec/60;
				sec%=60;
				visitTime=day+"天"+hou+"小时"+min+"分"+sec+"秒";
			}
			logger.info("ip="+ip+"的用户在本站逗留了: "+visitTime);
			System.err.println("---------->ip="+ip+"的用户在本站逗留了: "+visitTime);
			if(!visitTime.equals("0")){
				this.outHTML(response, "您本次登录时长为: "+visitTime+",欢迎再次登录!");
			}else{
				this.outHTML(response, "欢迎再次登录!");
			}
		}
		//既然要离开,自然要清除数据
		request.getSession().getServletContext().removeAttribute(ip0);
		request.getSession().removeAttribute("visitStart");
		return null;
	}
//------------------------------------------------------------------------------------------------	
	@RequestMapping(value = "/game")
	public String showGame(HttpServletRequest request) {
		return "game";
	}
	
	@RequestMapping(value = "/music")
	public String showMusic(HttpServletRequest request) {
		return "music";
	}
	
	@RequestMapping(value = "/movie")
	public String showMovie(HttpServletRequest request) {
		return "movie";
	}
	

}
