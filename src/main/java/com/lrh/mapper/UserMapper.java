package com.lrh.mapper;

import java.util.List;
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
    
    User getUserByUserName(String userName);
    
    List<User> findUserList(User u);
    
    int countUser(User u);
}