package com.lrh.mapper;

import com.lrh.model.Praise;

public interface SpitslotPraiseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Praise record);

    int insertSelective(Praise record);

    Praise selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Praise record);

    int updateByPrimaryKey(Praise record);
    
    
    /**
     * 通过吐槽id查询所有赞的数量
     * @author Sailing_LRH
     * @since 2015年11月15日
     * @param data_id
     * @return 点赞数量
     */
    int getSpitslotPraiseCount(Long dataId);
    
    /**
     * 查询是否点过赞
     * @author Sailing_LRH
     * @since 2015年11月22日
     * @param param
     * @return 点赞数量
     */
    int checkHasPraised(Praise param);
}