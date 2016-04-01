package com.lrh.mapper;

import java.util.List;
import java.util.Map;

import com.lrh.model.Dynamic;

public interface DynamicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Dynamic record);

    int insertSelective(Dynamic record);

    Dynamic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Dynamic record);

    int updateByPrimaryKey(Dynamic record);

    /**
     * 条件查询总数量
     * @author Sailing_LRH
     * @since 2016年1月8日
     * @param param
     * @return
     */
	long getDynamicCountByParam(Dynamic param);

	/**
	 * 添加+分页查询
	 * @author Sailing_LRH
	 * @since 2016年1月8日
	 * @param param
	 * @return
	 */
	List<Dynamic> findNoticDynamicByParam(Dynamic param);

	/**
	 * 通过模块id和数据id删除一条动态
	 * @author Sailing_LRH
	 * @since 2016年1月8日
	 * @param module
	 * @param dataId
	 * @return
	 */
	int deleteByModelAndDataId(Dynamic param);

	/**
	 * 通过模块id和数据ids批量删除
	 * @author Sailing_LRH
	 * @since 2016年2月4日
	 * @param param
	 */
	int deleteByModuleAndDataIds(Map<String, Object> param);
}