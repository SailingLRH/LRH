package com.lrh.mapper;

import java.util.List;
import java.util.Map;

import com.lrh.model.Blog;

public interface BlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKeyWithBLOBs(Blog record);

    int updateByPrimaryKey(Blog record);
    
    /**
     * 条件+分页查询
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param param
     * @return
     */
    List<Blog> findBlogByParam(Blog param);
    
    /**
     * 条件查询数量
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param param
     * @return
     */
    int getCountByParam(Blog param);

    /**
     * 通过id和userId删除
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param param
     * @return
     */
	boolean deleteByIdAndUserId(Blog param);
	
	/**
	 * 通过id查询阅读数量
	 * @author Sailing_LRH
	 * @since 2016年2月1日
	 * @param id
	 * @return
	 */
	int getReadNum(Long id);
	
	/**
	 * 通过ID list和userId批量删除
	 * @author Sailing_LRH
	 * @since 2016年2月2日
	 * @param param
	 * @return
	 */
	int deleteByIdsAndUserId(Map<String,Object> param);
}