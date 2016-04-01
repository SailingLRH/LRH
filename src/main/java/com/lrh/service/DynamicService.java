package com.lrh.service;

import java.util.List;

import com.lrh.model.Dynamic;
public interface DynamicService {
	
	public boolean deleteById(Long id);

	/**
	 * 插入一条数据并且返回插入数据的ID
	 * @author Sailing_LRH
	 * @since 2016年1月8日
	 * @param record
	 * @return
	 */
	public long addDynamic(Dynamic record);

    public Dynamic getDynamicById(Long id);

    public boolean updateDynamicById(Dynamic record);

    /**
     * 条件查询数量
     * @author Sailing_LRH
     * @since 2016年1月8日
     * @param param
     * @return
     */
	public long getDynamicCountByParam(Dynamic param);
    
    /**
     * 条件+分页查询
     * @author Sailing_LRH
     * @since 2016年1月8日
     * @param param
     * @return
     */
	public List<Dynamic> findNoticDynamicByParam(Dynamic param);

	/**
	 * 通过模块id和数据id删除通知消息
	 * @author Sailing_LRH
	 * @since 2016年1月8日
	 * @param moduleId
	 * @param dataId
	 * @return
	 */
	public boolean deleteByModuleAndDataId(short moduleId, long dataId);

	/**
	 * 批量删除
	 * @author Sailing_LRH
	 * @since 2016年2月4日
	 * @param moduleId
	 * @param ids
	 */
	public boolean deleteByModuleAndDataIds(short moduleId, String[] ids);
}
