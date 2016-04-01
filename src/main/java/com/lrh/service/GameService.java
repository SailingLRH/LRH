package com.lrh.service;

import java.util.List;

import com.lrh.model.Game;

public interface GameService {

	Boolean saveGameInfo(Game g);
	
	Boolean updateGameInfo(Game g);
	
	/**
	 * 根据用户id,游戏类型,游戏名称查询,理论上只有一条数据
	 * @author Sailing_LRH
	 * @since 2015年10月14日
	 * @param game对象
	 * @return list
	 */
	List<Game> findGameInfoByParam(Game param);
	
	/**
	 * 根据用户id,游戏类型,游戏名称查询,不查询游戏内容(gameAll)
	 * @author Sailing_LRH
	 * @since 2015年10月14日
	 * @param game对象
	 * @return list
	 */
	List<Game> findGameInfoByUser(Game param);
	
	/**
	 * 判断是否已经保存过了
	 * @author Sailing_LRH
	 * @since 2015年10月14日
	 * @param game对象
	 * @return true 已保存过;false 未保存过
	 */
	Boolean isSaved(Game param);
}
