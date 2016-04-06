package com.lrh.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lrh.mapper.GameMapper;
import com.lrh.model.Game;
import com.lrh.service.GameService;
@Service("gameService")
public class GameServiceImpl implements GameService{

	@Autowired
	GameMapper gameDao;

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public Boolean saveGameInfo(Game g) {
		//先判断是否已经存在该游戏进度
		List<Game> list=gameDao.findGameInfoByUser(g);
		if(list==null || list.isEmpty()){
			g.setAddTime(sdf.format(new Date()));
			int f=gameDao.insertSelective(g);
			return f>0?true:false;
		}else{
			g.setId(list.get(0).getId());
			g.setAddTime(sdf.format(new Date()));
			return updateGameInfo(g);
		}
	}

	@Override
	public Boolean updateGameInfo(Game g) {
		g.setAddTime(sdf.format(new Date()));
		int f=gameDao.updateByPrimaryKeySelective(g);
		return f>0?true:false;
	}

	@Override
	public List<Game> findGameInfoByParam(Game param) {
		return gameDao.findGameInfo(param);
	}

	@Override
	public List<Game> findGameInfoByUser(Game param) {
		return gameDao.findGameInfoByUser(param);
	}

	@Override
	public Boolean isSaved(Game param) {
		List<Game> list=gameDao.findGameInfoByUser(param);
		if(list==null || list.isEmpty())
			return true;
		else
			return false;
	}
	
}
