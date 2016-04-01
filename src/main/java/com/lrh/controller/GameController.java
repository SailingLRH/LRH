package com.lrh.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lrh.model.Game;
import com.lrh.model.User;
import com.lrh.service.GameService;

@Controller
@RequestMapping("/game")
public class GameController extends BaseController{
	
	@Autowired
	GameService gameService;

	//保存游戏进度
	@RequestMapping(value = "/save")
	public String saveGameInfo(HttpServletRequest request,
			HttpServletResponse response,Game g){
		
		System.err.println("正在执行保存游戏进度操作");
		
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			if(g.getGameName()==null || g.getGameName().equals("") || g.getGameType()==null || g.getGameType().equals("") || g.getGameAll()==null || g.getGameAll().equals("")){
				this.outJsonResult(response, false, "游戏信息不全,不能保存!", null);
				return null;
			}else if(g.getGameAll()!=null && g.getGameAll().length()>50000){
				this.outJsonResult(response, false, "非常抱歉,游戏数据太过庞大,暂时保存不了!", null);
				return null;
			}
			
			g.setUserId(user.getId());
			if(gameService.saveGameInfo(g)){
				this.outJsonResult(response, true, "保存成功!", null);
			}else{
				this.outJsonResult(response, false, "保存失败,请重试!", null);
			}
		}else{
			this.outJsonResult(response, false, "登录后才能保存游戏进度哦~", null);
		}
		return null;
	}
	
	//加载游戏进度	@RequestParam(value = "isNotRemember", required = false) Boolean
	@RequestMapping(value = "/load")
	public String loadGameInfo(HttpServletRequest request,
			HttpServletResponse response,Game g){
		
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			if(g.getGameName()==null || g.getGameName().equals("") || g.getGameType()==null || g.getGameType().equals("")){
				this.outJsonResult(response, false, "游戏信息不全,加载进度失败!", null);
				return null;
			}
			
			g.setUserId(user.getId());
			List<Game> list=gameService.findGameInfoByParam(g);
			if(list==null || list.isEmpty()){
				this.outJsonResult(response, false, "抱歉,您并有保存过这个游戏的进度!", null);
			}else{
				this.outJsonResult(response, true, "进度加载成功!", list.get(0));
			}
		}else{
			this.outJsonResult(response, false, "登录后才能加载游戏进度哦~", null);
		}
		return null;
	}
	
	//判断是否存在游戏进度,主动向用户推送加载进度消息
	@RequestMapping(value = "/isSavedGameInfo")
	public String isSavedGameInfo(HttpServletRequest request,
			HttpServletResponse response,Game g){
		
		
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			if(g.getGameName()==null || g.getGameName().equals("") || g.getGameType()==null || g.getGameType().equals("")){
				this.outJsonResult(response, false, "", null);
				return null;
			}
			g.setUserId(user.getId());
			List<Game> list=gameService.findGameInfoByParam(g);
			if(list!=null && !list.isEmpty()){
				this.outJsonResult(response, true, "检测到您在"+list.get(0).getAddTime()+"有保存过该游戏进度,是否要加载该进度?", null);
			}else{
				this.outJsonResult(response, false, "", null);
			}
		}else{
			this.outJsonResult(response, false, "", null);
		}
		return null;
	}
//======================================链接到数独页面================================
	@RequestMapping(value = "shudu/LRH")
	public String showLRH(){
		return "shudu/LRH";
	}
	
	@RequestMapping(value = "shudu/easy")
	public String showEasy(){
		return "shudu/easy";
	}
	
	@RequestMapping(value = "shudu/heart")
	public String showHeart(){
		return "shudu/heart";
	}
	
	@RequestMapping(value = "shudu/index")
	public String showIndex(){
		return "shudu/index";
	}
	
	@RequestMapping(value = "shudu/libra")
	public String showLibra(){
		return "shudu/libra";
	}
	
	@RequestMapping(value = "shudu/smile")
	public String showSmile(){
		return "shudu/smile";
	}
	@RequestMapping(value = "shudu/101")
	public String show101(){
		return "shudu/101";
	}
}

