package com.lrh.mapper;

import java.util.List;

import com.lrh.model.UserHeadImg;

/**
 * 用户头像dao层
 * @author Sailing_LRH
 * @since 2015年10月24日
 */
public interface UserHeadImgMapper {
	
	/**
	 * 通过id删除头像
	 * @author Sailing_LRH
	 * @since 2015年10月24日
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存头像
     * @author Sailing_LRH
     * @since 2015年10月24日
     * @param record
     * @return id
     */
    int insert(UserHeadImg record);

    /**
     * 查询用户使用过的所有头像
     * @author Sailing_LRH
     * @since 2015年10月24日
     * @param userId
     * @return list
     */
    List<UserHeadImg> findAllByUserId(Integer userId);
    
    /**
     * 将默认头像移除
     * @author Sailing_LRH
     * @since 2015年10月24日
     * @param userId
     * @return int 影响行数
     */
    int removeDefaultImg(Integer userId);
    
    /**
     * 设置默认头像
     * @author Sailing_LRH
     * @since 2015年10月24日
     * @param imgId
     * @param userId
     * @param updateTime
     * @return int 影响行数
     */
    int setDefaultImg(UserHeadImg param);
    
    /**
     *修改
     * @author Sailing_LRH
     * @since 2015年10月26日
     * @param img
     * @return int 影响行数
     */
    int updateByPrimaryKeySelective(UserHeadImg img);
}