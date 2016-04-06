package com.lrh.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lrh.mapper.UserUploadFileMapper;
import com.lrh.model.UserUploadFile;
import com.lrh.service.UserUploadFileService;
@Service("userUploadFileService")
public class UserUploadFileServiceImpl implements UserUploadFileService{

	@Autowired
	UserUploadFileMapper dao;

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Transactional
	public long saveFile(UserUploadFile file) {
		file.setUploadTime(sdf.format(new Date()));
		dao.insertSelective(file);
		return file.getId();
	}

	@Override
	public Boolean deleteByFileUrl(String fileUrl) {
		int f=dao.deleteByFileUrl(fileUrl);
		return f>0;
	}

	@Override
	public Boolean deleteById(Long id) {
		int f=dao.deleteByPrimaryKey(id);
		return f>0;
	}
	
}
