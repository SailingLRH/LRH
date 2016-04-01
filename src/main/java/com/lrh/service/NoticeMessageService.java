package com.lrh.service;

import java.util.List;

import com.lrh.model.NoticeMessage;

public interface NoticeMessageService {
	
	public boolean deleteByPrimaryKey(Long id);

	public boolean insertSelective(NoticeMessage record);

	public NoticeMessage selectByPrimaryKey(Long id);

	public boolean updateByPrimaryKeySelective(NoticeMessage record);

    /**
     * 条件查询数量
     * @author Sailing_LRH
     * @since 2015年12月21日
     * @param param
     * @return
     */
	public Long getNoticMessageCountByParam(NoticeMessage param);
    
    /**
     * 条件+分页查询
     * @author Sailing_LRH
     * @since 2015年12月21日
     * @param param
     * @return
     */
	public List<NoticeMessage> findNoticMessageByParam(NoticeMessage param);

	/**
	 * 通过用户id和数据Id修改消息的阅读状态
	 * @author Sailing_LRH
	 * @since 2015年12月27日
	 * @param dataId
	 * @param userId
	 * @param isRead
	 */
	public boolean updateByIdAndUserId(long dataId, Integer userId,boolean isRead);

	/**
	 * 通过模块id和数据id删除通知消息
	 * @author Sailing_LRH
	 * @since 2015年12月27日
	 * @param model
	 * @param id
	 * @return
	 */
	public boolean deleteByModelAndDataId(short model, long id);

	/**
	 * 批量删除
	 * @author Sailing_LRH
	 * @since 2016年2月4日
	 * @param moduleId
	 * @param ids
	 */
	public boolean deleteByModelAndDataIds(short moduleId, String[] ids);
}
