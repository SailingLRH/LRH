package com.lrh.mapper;

import com.lrh.model.BlogUpload;

public interface BlogUploadMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BlogUpload record);

    int insertSelective(BlogUpload record);

    BlogUpload selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BlogUpload record);

    int updateByPrimaryKey(BlogUpload record);
}