package com.lrh.service;

import java.util.List;

import com.lrh.model.User;
import com.lrh.model.UserHeadImg;

public interface UserService {

	/**
	 * 用户登录接口
	 * @author Sailing_LRH
	 * @since 2015年9月12日
	 * @param account
	 * @param password
	 * @return user 对象
	 */
	public User login(String account,String password);
	
	
	/**
	 * 注册账号时,检查账号或邮箱是否已经注册过
	 * @author Sailing_LRH
	 * @since 2015年9月12日
	 * @param account
	 * @param email
	 * @return true 存在;false 不存在
	 */
	public Boolean isExistAccount(String account,String email);
	
	
	/**
	 * 用户注册账号
	 * @author Sailing_LRH
	 * @since 2015年9月12日
	 * @param user
	 * @return user 对象
	 */
	public User regist(User user);
	
	
	/**
	 * 修改用户数据
	 * @author Sailing_LRH
	 * @since 2015年9月12日
	 * @param user
	 * @return user 对象
	 */
	public User updateUserInfo(User user);
	
	
	/**
	 * 通过邮箱号查询账号,用于忘记密码
	 * @author Sailing_LRH
	 * @since 2015年9月26日
	 * @param email
	 * @return user 对象
	 */
	public User getUserByEmail(String email);
	
	
	/**
	 * 通过用户名或邮箱号查询,用于忘记密码后用临时密码登录
	 * @author Sailing_LRH
	 * @since 2015年9月26日
	 * @param account
	 * @return user 对象
	 */
	public User getUserByUserNameOrEmail(String account);
	
	/**
     * 查询用户使用过的所有头像
     * @author Sailing_LRH
     * @since 2015年10月24日
     * @param userId
     * @return list
     */
	public List<UserHeadImg> findUserAllImg(Integer userId);
	
	/**
	 * 上传头像
	 * @author Sailing_LRH
	 * @since 2015年10月24日
	 * @param img
	 * @return boolean 成功与否
	 */
	public Boolean uploadImg(UserHeadImg img);
	
	/**
     * 设置默认头像
     * @author Sailing_LRH
     * @since 2015年10月24日
     * @param imgId
     * @param userId
     * @return int 影响行数
     */
	public Boolean setDefaultImg(Integer imgId,Integer userId);


	/**
	 * 通过头像id删除一条数据
	 * @author Sailing_LRH
	 * @since 2016年1月14日
	 * @param id
	 */
	public Boolean removeUserImgByImgId(Integer id);


	/**
	 * 通过用户名查询用户信息
	 * @author Sailing_LRH
	 * @since 2016年4月5日
	 * @param userName
	 * @return User
	 */
	public User getUserByUserName(String userName);
}
