package com.lrh.mapper;

import java.util.List;
import java.util.Map;

import com.lrh.model.NoticeMessage;

public interface NoticeMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoticeMessage record);

    int insertSelective(NoticeMessage record);

    NoticeMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoticeMessage record);

    int updateByPrimaryKey(NoticeMessage record);
    
    /**
     * 通过模块和数据id删除
     * @author Sailing_LRH
     * @since 2015年12月27日
     * @param param
     * @return
     */
    int deleteByModelAndDataId(NoticeMessage param);
    
    /**
     * 条件查询数量
     * @author Sailing_LRH
     * @since 2015年12月21日
     * @param param
     * @return
     */
    Long getNoticMessageCountByParam(NoticeMessage param);
    
    /**
     * 条件+分页查询
     * @author Sailing_LRH
     * @since 2015年12月21日
     * @param param
     * @return
     */
    List<NoticeMessage> findNoticMessageByParam(NoticeMessage param);
    
    /**
	 * 通过用户id和数据Id修改消息的阅读状态
	 * @author Sailing_LRH
	 * @since 2015年12月27日
	 * @param dataId
	 * @param userId
	 * @param isRead
	 */
	int updateByIdAndUserId(NoticeMessage param);

	/**
	 * 通过模块id和数据ids批量删除
	 * @author Sailing_LRH
	 * @since 2016年2月4日
	 * @param param
	 */
	int deleteByModelAndDataIds(Map<String, Object> param);
}