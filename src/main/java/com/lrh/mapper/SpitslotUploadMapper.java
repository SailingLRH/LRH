package com.lrh.mapper;

import com.lrh.model.SpitslotUpload;

public interface SpitslotUploadMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpitslotUpload record);

    int insertSelective(SpitslotUpload record);

    SpitslotUpload selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpitslotUpload record);

    int updateByPrimaryKey(SpitslotUpload record);
    
}