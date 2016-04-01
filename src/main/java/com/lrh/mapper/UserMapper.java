package com.lrh.mapper;

import java.util.Map;

import com.lrh.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    User selectByAccount(Map<String, Object> param);
    
    User checkAccount(Map<String, Object> param);
    
    User getUserByUserNameOrEmail(String account);
}