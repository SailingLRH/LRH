package com.lrh.mapper;

import java.util.List;

import com.lrh.model.Spitslot;

public interface SpitslotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Spitslot record);

    int insertSelective(Spitslot record);

    Spitslot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Spitslot record);

    int updateByPrimaryKey(Spitslot record);
    
    List<Spitslot> findSpitslot(Spitslot param);
    
    long getSpitslotCount(Spitslot param);

	int deleteByIdAndUserId(Spitslot param);
}