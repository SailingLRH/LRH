package com.lrh.mapper;

import com.lrh.model.UserUploadFile;

public interface UserUploadFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserUploadFile record);

    int insertSelective(UserUploadFile record);

    UserUploadFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserUploadFile record);

    int updateByPrimaryKey(UserUploadFile record);
    
    int deleteByFileUrl(String fileUrl);
}