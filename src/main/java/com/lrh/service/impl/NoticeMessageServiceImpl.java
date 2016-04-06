package com.lrh.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lrh.mapper.NoticeMessageMapper;
import com.lrh.model.NoticeMessage;
import com.lrh.service.NoticeMessageService;
import com.lrh.util.ToolDate;
@Service("noticeMessageService")
public class NoticeMessageServiceImpl implements NoticeMessageService{

	@Autowired
	NoticeMessageMapper noticeDao;

	@Override
	public boolean deleteByPrimaryKey(Long id) {
		return noticeDao.deleteByPrimaryKey(id)>0;
	}

	@Override
	public boolean insertSelective(NoticeMessage record) {
		record.setTime(ToolDate.getNowTime());
		return noticeDao.insertSelective(record)>0;
	}

	@Override
	public NoticeMessage selectByPrimaryKey(Long id) {
		return noticeDao.selectByPrimaryKey(id);
	}

	@Override
	public boolean updateByPrimaryKeySelective(NoticeMessage record) {
		return noticeDao.updateByPrimaryKeySelective(record)>0;
	}

	@Override
	public Long getNoticMessageCountByParam(NoticeMessage param) {
		return noticeDao.getNoticMessageCountByParam(param);
	}

	@Override
	public List<NoticeMessage> findNoticMessageByParam(NoticeMessage param) {
		return noticeDao.findNoticMessageByParam(param);
	}

	@Override
	public boolean updateByIdAndUserId(long dataId, Integer userId,boolean isRead) {
		NoticeMessage param = new NoticeMessage();
		param.setId(dataId);
		param.setUserId(userId);
		param.setIsRead(isRead);
		return noticeDao.updateByIdAndUserId(param)>0;
	}

	@Override
	public boolean deleteByModelAndDataId(short model, long id) {
		NoticeMessage param = new NoticeMessage();
		param.setModel(model);
		param.setDataId(id);
		return noticeDao.deleteByModelAndDataId(param)>0;
	}

	@Override
	public boolean deleteByModelAndDataIds(short moduleId, String[] ids) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("ids", ids);
		param.put("moduleId", moduleId);
		return noticeDao.deleteByModelAndDataIds(param)>0;
	}
}
