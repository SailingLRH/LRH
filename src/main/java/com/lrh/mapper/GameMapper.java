package com.lrh.mapper;

import java.util.List;

import com.lrh.model.Game;

public interface GameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Game record);

    int insertSelective(Game record);

    Game selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Game record);

    int updateByPrimaryKeyWithBLOBs(Game record);

    int updateByPrimaryKey(Game record);
    
    /**
     * 查询用户保存过的游戏
     * @author Sailing_LRH
     * @since 2015年10月14日
     * @param param
     * @return
     */
    List<Game> findGameInfoByUser(Game param);
    
    /**
     * 用于判断用户是否已经保存过该游戏,以及用于载入游戏进度
     * @author Sailing_LRH
     * @since 2015年10月14日
     * @param param
     * @return
     */
    List<Game> findGameInfo(Game param);
}