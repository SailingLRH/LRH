package com.lrh.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lrh.mapper.UserHeadImgMapper;
import com.lrh.mapper.UserMapper;
import com.lrh.model.User;
import com.lrh.model.UserHeadImg;
import com.lrh.service.UserService;
import com.lrh.util.ToolContext;
@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	UserMapper userDao;
	@Autowired
	UserHeadImgMapper imgDao;
	
	@Override
	public User login(String account, String password) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("account", account);
		param.put("password", password);
		User user=userDao.selectByAccount(param);
		
		if(user!=null){
			User user1=user;
			String loginTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			user1.setLastLoginTime(loginTime);
			userDao.updateByPrimaryKeySelective(user1);
		}
		return user;
	}

	@Override
	public Boolean isExistAccount(String account,String email) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("userName", account);
		param.put("email", email);
		User user=userDao.checkAccount(param);
		return user==null?false:true;
	}

	@Transactional
	public User regist(User user) {
		int flag=userDao.insertSelective(user);
		return flag>0?user:null;
	}

	@Transactional
	public User updateUserInfo(User user) {
		int flag=userDao.updateByPrimaryKeySelective(user);
		return flag>0?userDao.selectByPrimaryKey(user.getId()):null;
	}

	@Override
	public User getUserByEmail(String email) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("userName", null);
		param.put("email", email);
		return userDao.checkAccount(param);
	}

	@Override
	public User getUserByUserNameOrEmail(String account) {
		return userDao.getUserByUserNameOrEmail(account);
	}
//-----------------------------------------用户头像------------------------------------------
	@Override
	public List<UserHeadImg> findUserAllImg(Integer userId) {
		return imgDao.findAllByUserId(userId);
	}

	@Transactional
	public Boolean uploadImg(UserHeadImg img) {
		img.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		img.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		int userId=img.getUserId();
		//先将默认的移除
		imgDao.removeDefaultImg(userId);
		List<UserHeadImg> allImg= imgDao.findAllByUserId(userId);
		int max=ToolContext.getSysPropInt("user_head_img_max");
		if(allImg!=null && allImg.size()>(max-1)){//如果用户的头像达到了5张,则先删除一张旧的再新增
			System.err.println("用户上传的头像已达上限,正在删除最旧的...");
			//修改最久没有设为默认的那一张
			UserHeadImg last=allImg.get(allImg.size()-1);
			img.setId(last.getId());
			int result=imgDao.updateByPrimaryKeySelective(img);
			if(result>0){
				File imgFile=new File(last.getImgUrl());
				if(imgFile.exists() && imgFile.isFile()){
					imgFile.delete();//将文件也删除
				}
			}
			return true;
		}else{
			int result=imgDao.insert(img);
			return result>0;
		}
	}

	@Transactional
	public Boolean setDefaultImg(Integer imgId, Integer userId) {
		imgDao.removeDefaultImg(userId);
		UserHeadImg param=new UserHeadImg();
		param.setId(imgId);
		param.setUserId(userId);
		param.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		int result = imgDao.setDefaultImg(param);
		return result>0;
	}

	@Override
	public Boolean removeUserImgByImgId(Integer id) {
		return imgDao.deleteByPrimaryKey(id)>0;
	}

	@Override
	public User getUserByUserName(String userName)
	{
		return userDao.getUserByUserName(userName);
	}

}
