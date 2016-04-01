package com.lrh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lrh.mapper.DataTypeMapper;
import com.lrh.model.DataType;
import com.lrh.service.TypeService;
@Service("typeService")
public class TypeServiceImpl implements TypeService{

	@Autowired
	DataTypeMapper dao;

	@Override
	public boolean deleteByTypeCode(String typeCode) {
		return dao.deleteByTypeCode(typeCode)>0;
	}

	@Override
	public boolean insert(DataType record) {
		return dao.insertSelective(record)>0;
	}

	@Override
	public boolean updateType(DataType record) {
		return dao.updateByPrimaryKeySelective(record)>0;
	}

	@Override
	public DataType selectByTypeCode(String typeCode) {
		return dao.selectByTypeCode(typeCode);
	}

	@Override
	public List<DataType> selectByMainType(String mainType) {
		return dao.selectByMainType(mainType);
	}


}
