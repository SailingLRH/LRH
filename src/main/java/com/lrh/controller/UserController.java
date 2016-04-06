package com.lrh.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lrh.model.NoticeMessage;
import com.lrh.model.User;
import com.lrh.model.UserHeadImg;
import com.lrh.service.NoticeMessageService;
import com.lrh.service.UserService;
import com.lrh.util.ToolContext;
import com.lrh.util.ToolEmail;
import com.lrh.util.ToolImage;
import com.lrh.util.ToolMD5;
import com.lrh.util.ToolRandomCode;

@RequestMapping(value = "/user")
@Controller
public class UserController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * 用户在线状态map<userName,失联次数>
	 */
	public static ConcurrentHashMap<String,Map<String,Object>> userOnlineMap = new ConcurrentHashMap<String,Map<String,Object>>();
	
	/**
	 * 存放需要强制下线的账号 key uuid	value userName
	 */
	public static ConcurrentHashMap<String,String> needOfflineMap = new ConcurrentHashMap<String,String>();
	/**
	 * 失联次数达到一定值后,将用户状态设置为离线
	 */
	public static final int maxTime = 5;
	
	@Autowired
	UserService userService;
	@Autowired
	NoticeMessageService noticeService;
	
	//登录
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "account", required = true) String account,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "isNotRemember", required = false) Boolean isNotRemember) {
		account=account.toLowerCase();//转成小写
		
		Boolean lenthIs16=ToolContext.getSysPropInt("md5_lentrh")==16?true:false;
		String pwd2=password;
		password=ToolMD5.Md5(password, lenthIs16);
		boolean b=false;
		User user=userService.login(account, password);
		if(user!=null){
			//开始计时,将时间放入session作用域
			request.getSession().setAttribute("visitStart", new Date());
			request.getSession().setAttribute("user", user);
			//判断是否要保存账号密码,默认要保存
			if(isNotRemember==null || !isNotRemember){
				Integer savetime=60*60*24*30;//cookie 保存30天
				this.addCookie("account", account, savetime,response);
				this.addCookie("password", pwd2, savetime,response);
			}else{//如果不要保存,那么就将之前保存的清除掉
				this.addCookie("account", null, 1,response);//设为0s无效
				this.addCookie("password", null, 1,response);
			}
			outJsonResult(response, true, "登录成功,欢迎回来!", null);
			b=true;
		}else{
			//正常登录没有成功后,尝试是否是忘记密码登录
			User user1=(User) request.getSession().getAttribute("user");
			if(user1!=null){
				User user2=userService.getUserByUserNameOrEmail(account);
				if(user2==null){
					outJsonResult(response, false, "啊哦~账号不存在,或已被封哦!", null);
					b=false;
				}else{
					if((user1.getEmail().toLowerCase().equals(user2.getEmail().toLowerCase())) && (user1.getPassword().equals(pwd2))){
						//开始计时,将时间放入session作用域
						request.getSession().setAttribute("visitStart", new Date());
						request.getSession().setAttribute("user", user2);
						//判断是否要保存账号密码,默认要保存
						if(isNotRemember==null || !isNotRemember){
							Integer savetime=60*60*24*30;//cookie 保存30天
							this.addCookie("account", account, savetime,response);
						}else{//如果不要保存,那么就将之前保存的清除掉
							this.addCookie("account", null, 1,response);//设为0s无效
						}
						this.addCookie("password", null, 1,response);
						outJsonResult(response, true, "登录成功,欢迎回来!", null);
						b=true;
					}else{
						outJsonResult(response, false, "啊哦~账号或密码错了哟,再试一次吧!", null);
						b=false;
					}
				}
			}else{
				outJsonResult(response, false, "啊哦~账号不存在或者密码错了哟,再试一次吧!", null);
				b=false;
			}
		}
		if(b){
			user=(User) request.getSession().getAttribute("user");
			List<UserHeadImg> imgs=userService.findUserAllImg(user.getId());
			String headImgPath=ToolContext.getSysProp("user_head_img_upload_path")+user.getUserName()+"/";
			File userImg=new File(headImgPath);
			File[] tempList = userImg.listFiles();
			if(tempList!=null){
				for (int i = 0; i < tempList.length; i++) {
					if (tempList[i].isFile()) {
						boolean f=true;
						//不能删除当前已使用的头像
						if(user.getPhotoUrl()!=null && (user.getPhotoUrl().equals(headImgPath+tempList[i].getName()))){
							f=false;
						}else{
							for(UserHeadImg img:imgs){
								if(img.getImgUrl().equals(headImgPath+tempList[i].getName())){
									f=false;
								}
							}
						}
						
						if(f){//表示该图片跟数据库数据没关联,应删除
							tempList[i].delete();
							System.err.println("已删除没有保存的图片:"+tempList[i].getName());
						}
					}
				}
			}
			
			//将用户上线处理
			String uuid = UUID.randomUUID().toString();
			if(userOnlineMap.containsKey(user.getUserName())){
				//如果检测到此账号已经在别处登录,那么将别处登录的账号下线
				String uuid2 = (String) userOnlineMap.get(user.getUserName()).get("uuid");
				needOfflineMap.put(uuid2, user.getUserName());
			}
			request.getSession().setAttribute("uuid", uuid);	
			userOnline(user, true,uuid);
		}
		return null;
	}
	
	//检查账号是否已存在
	@RequestMapping(value = "/checkAccount")
	public String isExistAccount(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "account", required = true) String account,
			@RequestParam(value = "isEmail", required = false) Boolean isEmail) {
		
		boolean exist=false;
		account=account.toLowerCase();//转成小写
		if(isEmail==null || isEmail){
			exist=userService.isExistAccount(null, account);
		}else{
			exist=userService.isExistAccount(account, null);
		}
		if(exist){
			if(isEmail==null || isEmail){
				outHTML(response, "哎呀~真是不巧,这个邮箱已经被其它账号绑定了&gt;_&lt;!");
			}else{
				outHTML(response, "哎呀~真是不巧,这个账号已经被别人注册了&gt;_&lt;!");
			}
		}else{
			outHTML(response, "1");
		}
		return null;
	}
	
	//退出登录
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response){
		
		String ip0=getIpAddr(request);
		Date visitStart=(Date) request.getSession().getAttribute("visitStart");
		if(visitStart!=null){
			String ip=(String) request.getSession().getServletContext().getAttribute(ip0);
			Date visitEnd=new Date();
			long diff = visitEnd.getTime() - visitStart.getTime();
			long sec = diff / (1000);
			String visitTime="0";
			if(sec<60){//一分钟之内
				visitTime=sec+"秒";
			}else if(sec<3600){//一小时之内
				int min=(int) sec/60;
				sec%=60;
				visitTime=min+"分"+sec+"秒";
			}else if(sec<86400){//一天之内
				int hou=(int) sec/3600;
				sec%=3600;
				int min=(int) sec/60;
				sec%=60;
				visitTime=hou+"小时"+min+"分"+sec+"秒";
			}else{
				int day=(int) sec/86400;
				sec%=86400;
				int hou=(int) sec/3600;
				sec%=3600;
				int min=(int) sec/60;
				sec%=60;
				visitTime=day+"天"+hou+"小时"+min+"分"+sec+"秒";
			}
			logger.info("ip="+ip+"的用户在本站逗留了: "+visitTime);
			System.err.println("---------->ip="+ip+"的用户在本站逗留了: "+visitTime);
			if(!visitTime.equals("0")){
				this.outHTML(response, "您本次登录时长为: "+visitTime+",欢迎再次登录!");
			}else{
				this.outHTML(response, "您已安全退出,欢迎再次登录!");
			}
		}else{
			this.outHTML(response, "您已安全退出,欢迎再次登录!");
		}
		
		User user = (User) request.getSession().getAttribute("user");
		if(user!=null){
			//将用户离线处理
			userOnline(user, false,null);
		}
		
		//既然要离开,自然要清除数据
		request.getSession().getServletContext().removeAttribute(ip0);
		request.getSession().removeAttribute("visitStart");
		
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("uuid");
		request.getSession().invalidate();
		
		return null;
	}
	
	//获取验证码
	@RequestMapping(value = "/getActionCode")
	public String getActionCode(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "email", required = true) String email){
		String actionCode=ToolRandomCode.getRandomNumber(6);
		try {
			Map<String, Object> result=ToolEmail.sendHtmlEmail(email, "来自OLD MAN AND SEA的获取验证码邮件","1",actionCode);
			
			if((boolean) result.get("isSuccess")){
				User user=new User();
				user.setEmail(email);
				user.setActionCode(actionCode);
				request.getSession().setAttribute("user", user);
		    }
		    this.outJson(response, result);
		} catch (Exception e) {
			logger.error("发送邮件时发生异常:"+e.getMessage());
			System.err.println("发送邮件发生异常!");
		}
		return null;
	}
	
	//注册
	@RequestMapping(value = "/regist")
	public String regist(HttpServletRequest request,
			HttpServletResponse response,User user){
		
		if(user==null ){
			outJsonResult(response, false, "对不起,您的信息填写不完整,拒绝注册!", null);
			return null;
		}
		
		String pwd=user.getPassword();
		String userName=user.getUserName();
		String email=user.getEmail();
		if(userName==null || userName.equals("") || pwd==null || pwd.equals("") || email==null || email.equals("")){
			outJsonResult(response, false, "对不起,您的信息填写不完整,拒绝注册!", null);
			return null;
		}
		
		User user0=(User) request.getSession().getAttribute("user");
		
		if(user0==null){//更本就没有去获取验证码
			outJsonResult(response, false, "亲,您还没有获取到验证码呢,验证码可不是随便输的哦!", null);
			return null;
		}
		
		if(!user0.getActionCode().equals(user.getActionCode())){//验证码不匹配,不允许注册
			outJsonResult(response, false, "您输入的验证码有误或已失效,请重新输入!", null);
			return null;
		}
		
		if(!user0.getEmail().toLowerCase().equals(user.getEmail().toLowerCase())){//获取验证码的邮箱号与注册的邮箱号不匹配,不允许注册
			outJsonResult(response, false, "您获取验证码的邮箱号与注册的邮箱号不匹配!", null);
			return null;
		}
		
		//再做一次唯一校验
		if(userService.isExistAccount(user.getUserName().toLowerCase(), null) ){
			outJsonResult(response, false, "亲,您下手太慢啦,这个账号已经被别人注册了!&gt;_&lt;", null);
			return null;
		}
		if(userService.isExistAccount(null, user.getEmail().toLowerCase()) ){
			outJsonResult(response, false, "亲,您下手太慢啦,这个邮箱已经被别的账号绑定了!&gt;_&lt;", null);
			return null;
		}
		
		Boolean lenthIs16=ToolContext.getSysPropInt("md5_lentrh")==16?true:false;
		pwd=ToolMD5.Md5(pwd, lenthIs16);
		
		user.setActionCode(ToolRandomCode.getRandomNumber(6));
		user.setIsAction(true);
		user.setRegistTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		try {
			userService.regist(user);
			request.getSession().setAttribute("user", user);
			outJsonResult(response, true, "恭喜注册成功!您的账号已激活,3秒后自动帮您刷新登录...^_^", null);
			//将用户上线处理
			String uuid = UUID.randomUUID().toString();
			request.getSession().setAttribute("uuid", uuid);
			userOnline(user, true,uuid);
		} catch (Exception e) {
			logger.error("用户注册发生异常:"+e.getMessage());
		}
		return null;
	}
	
	//获取临时密码
	@RequestMapping(value = "/forgotPwd")
	public String getTempPwd(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "email", required = true) String email){
		email=email.toLowerCase();
		User user=userService.getUserByEmail(email);
		if(user != null && user.getIsAction()){
			try {
				String tempPwd=ToolRandomCode.getRandomCharOrNumber(8);
				Map<String,Object> result=ToolEmail.sendHtmlEmail(email, "来自OLD MAN AND SEA的获取临时登录密码邮件","2",tempPwd);
			    if((boolean) result.get("isSuccess")){
			    	user.setEmail(email);
					user.setPassword(tempPwd);
			    	request.getSession().setAttribute("user", user);
			    }
			    this.outJson(response, result);
			} catch (Exception e) {
				logger.error("发送邮件时发生异常:"+e.getMessage());
				System.err.println("发送邮件发生异常!");
			}
		}else{
			outJsonResult(response, false, "啊哦~该账号不存在或已被封,请检查邮箱号是否填写正确!", null);
		}
		return null;
	}
	
	//上传头像
	@RequestMapping(value = "/uploadPhoto")
	public String uploadPhoto(HttpServletRequest request,
			HttpServletResponse response,MultipartFile file){
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			try {
				String path=ToolContext.getSysProp("user_head_img_upload_path")+user.getUserName()+"/";
				//创建目录
				File userFile=new File(path);
				if (!userFile.mkdir()) {
					userFile.mkdirs();
				}
				
				if(file.isEmpty()){ 
					System.err.println("文件未上传"); 
				}else{ 
					String originalFileName=file.getOriginalFilename();
					long fileSize=file.getSize();
					String fileType=file.getContentType();
					
					Map<String, Object> data=new HashMap<String, Object>();
					if(fileType.indexOf("image")==-1){
						outJsonResult(response, false, "咦~您选择的好像不是图片文件呢,请重新选择!", null);
						return null;
					}
					if(fileSize>2097152){
						outJsonResult(response, false, "哦~您选择的图片是在太大了,最多支持2MB的图片文件,请重新选择!", null);
						return null;
					}
					
					//用时间加以命名
					String fileName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".";
					//System.out.println("文件名前缀为:"+fileNameStartWith);
					String [] sp=originalFileName.split("\\.");
					if(sp.length>1){
						fileName+=sp[sp.length-1];
					}else{
						System.err.println("文件没有后缀");
						fileName+="jpg";
					}
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path, fileName));
					
					//调整图片的大小
					ToolImage.resizeImage(path+fileName, path+fileName, 150, 150);
					
					data.put("fileUrl", path+fileName);
					outJsonResult(response, true, "文件已上传,您可以保存或重新选择文件上传", data);
					
					//查询session作用域是否已经存在路径
					String imgUrl=(String) request.getSession().getAttribute("userPhoto");
					if(imgUrl!=null && !imgUrl.equals("")){
						File imgFile=new File(imgUrl);
						if(imgFile.exists() && imgFile.isFile()){
							imgFile.delete();
							System.err.println("已删除上传成功却未保存的图片!");
						}
					}
					//将文件路径保存到session
					request.getSession().setAttribute("userPhoto", path+fileName);
					
				} 
			} catch (IOException e){
				logger.error("上传头像发生异常:"+e.getMessage());
			} 
		}else{
			outJsonResult(response, false, "您需要重新登录后才能上传头像哦!", null);
		}
		
		return null;
	}
	
	//保存新头像
	@RequestMapping(value = "/savePhoto")
	public String savePhoto(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "imgId", required = false) String imgId,@RequestParam(value = "imgUrl", required = false) String imgUrl){
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			if(imgId!=null && !imgId.equals("")){//切换到旧头像
				userService.setDefaultImg(Integer.valueOf(imgId), user.getId());
				user.setPhotoUrl(imgUrl);
				request.getSession().setAttribute("user", user);
				Map<String, Object> data=new HashMap<String, Object>();
				data.put("photoUrl", imgUrl);
				this.outJsonResult(response, true, "保存成功!", data);
			}else{//保存新头像
				String path=(String) request.getSession().getAttribute("userPhoto");
				if(path==null || path.equals("")){
					this.outJsonResult(response, false, "对不起,您上传的头像已失效,请重新上传!", null);
				}else{
					UserHeadImg img=new UserHeadImg();
					img.setImgUrl(path);
					img.setIsDefault(true);
					img.setUserId(user.getId());
					userService.uploadImg(img);
					
					user.setPhotoUrl(path);
					request.getSession().setAttribute("user", user);
					
					Map<String, Object> data=new HashMap<String, Object>();
					data.put("photoUrl", path);
					this.outJsonResult(response, true, "保存成功!", data);
				}
			}
		}else{
			outJsonResult(response, false, "您需要重新登录后才能上传头像哦!", null);
		}
		request.getSession().removeAttribute("userPhoto");
		return null;
	}
	
	//修改密码时,获取验证码
	@RequestMapping(value = "/editPwdActionCode")
	public String editPwdActionCode(HttpServletRequest request,
			HttpServletResponse response){
		User user=(User) request.getSession().getAttribute("user");
		if(user != null){
			try {
				String actionCode=ToolRandomCode.getRandomNumber(6);
				Map<String,Object> result=ToolEmail.sendHtmlEmail(user.getEmail(), "来自OLD MAN AND SEA的获取验证码的邮件","3",actionCode);
			    if((boolean) result.get("isSuccess")){
			    	request.getSession().setAttribute("editPwdActionCode", actionCode);
			    }
			    this.outJson(response, result);
			} catch (Exception e) {
				logger.error("发送邮件时发生异常:"+e.getMessage());
				System.err.println("发送邮件发生异常!");
			}
		}else{
			outJsonResult(response, false, "您的登录信息已失效,请重新登录后操作!", null);
		}
		return null;
	}
	
	//更新密码
	@RequestMapping(value = "/editPwd")
	public String editPwd(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "actionCode", required = true) String actionCode){
		String ac=(String) request.getSession().getAttribute("editPwdActionCode");
		if(ac==null || ac.equals("")){
			this.outJsonResult(response, false, "亲,您还没有获取到验证码呢,验证码可不是随便输的哦!", null);
			return null;
		}
		if(!ac.equals(actionCode)){
			this.outJsonResult(response, false, "哎呀,验证码错了哟!", null);
		}else{
			User user=(User) request.getSession().getAttribute("user");
			if(user==null){
				this.outJsonResult(response, false, "亲,您的登录信息已失效,需要重新登录后才能操作!", null);
			}else{
				Boolean lenthIs16=ToolContext.getSysPropInt("md5_lentrh")==16?true:false;
				password=ToolMD5.Md5(password, lenthIs16);
				
				user.setPassword(password);
				userService.updateUserInfo(user);
				//注销用户信息,让用户重新登录
				request.getSession().removeAttribute("editPwdActionCode");
				request.getSession().removeAttribute("user");
				request.getSession().invalidate();
				this.outJsonResult(response, true, "密码修改成功,请重新登录!", null);
			}
		}
		return null;
	}
	
	//获取用户曾今使用过的头像
	@RequestMapping(value = "/getOldHeadImg")
	public String getOldHeadImg(HttpServletRequest request,
			HttpServletResponse response){
		//先判断用户是否在线
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			this.outJsonResult(response, false, "对不起,您已掉线,请重新登录后操作!", null);
		}else{
			List<UserHeadImg> imgs=userService.findUserAllImg(user.getId());
			for(int i=0;i<imgs.size();i++){
				UserHeadImg img = imgs.get(i);
				File imgFile=new File(img.getImgUrl());
				if(!imgFile.exists() || !imgFile.isFile()){
					imgs.remove(i);
					i--;//list的size因为remove而减少了一个,因此i--来同步改变
					userService.removeUserImgByImgId(img.getId());
				}
			}
			this.outJsonResult(response, imgs.size()>0, "", imgs);
		}
		return null;
	}
	
	//获取与我相关的消息数量
	@RequestMapping(value = "/getNoticeCount")
	public String demo(HttpServletRequest request,
			HttpServletResponse response){
		
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			this.outJsonResult(response, false, "未登录!", null);
		}else{
			String uuid = (String) request.getSession().getAttribute("uuid");
			if(uuid !=null && needOfflineMap.containsKey(uuid)){
				//强制下线
				request.getSession().getServletContext().removeAttribute(getIpAddr(request));
				request.getSession().removeAttribute("visitStart");
				request.getSession().removeAttribute("user");
				request.getSession().removeAttribute("uuid");
				request.getSession().invalidate();
				needOfflineMap.remove(uuid);
				
				this.outJsonResult(response, null, "抱歉,您的账号已在别处登录,您被迫下线!如不是您本人操作,请重新登录后修改密码!", null);
			}else{
				if(userOnlineMap.containsKey(user.getUserName()) && !userOnlineMap.get(user.getUserName()).get("uuid").equals(uuid)){
					//此账号已在别处登录,记录对方的uuid,使其被迫下线
					needOfflineMap.put((String) userOnlineMap.get(user.getUserName()).get("uuid"), user.getUserName());
				}
				//表示用户在线
				Map<String,Object> onlineMap = new HashMap<String,Object>();
				if(!userOnlineMap.containsKey(user.getUserName()) || !userOnlineMap.get(user.getUserName()).get("uuid").equals(uuid)){
					System.err.println("用户["+user.getUserName()+"]掉线后又上线了...");
				}
				onlineMap.put("uuid", uuid);
				onlineMap.put("times", 0);
				userOnlineMap.put(user.getUserName(),onlineMap);
				
				NoticeMessage msg=new NoticeMessage();
				msg.setUserId(user.getId());
				msg.setIsRead(false);
				long noticeCount=noticeService.getNoticMessageCountByParam(msg);
				if(noticeCount<1)
					this.outJsonResult(response, false, "无新消息", null);
				else if(noticeCount>99)
					this.outJsonResult(response, true, "无新消息", "99+");
				else 
					this.outJsonResult(response, true, "无新消息", noticeCount);
			}
			
		}
		return null;
	}
	
	/**
	 * 用户上下线设置
	 * @param user 
	 * @param isOnline
	 * @param uuid
	 */
	public void userOnline(User user,boolean isOnline,String uuid){
		if(isOnline){
			Map<String,Object> onlineMap = new HashMap<String,Object>();
			onlineMap.put("uuid", uuid);
			onlineMap.put("times", 0);
			userOnlineMap.put(user.getUserName(), onlineMap);
		}else{
			userOnlineMap.remove(user.getUserName());
		}
	}
	
	//demo
	@RequestMapping(value = "/queryUser")
	public String demo(HttpServletRequest request,
			HttpServletResponse response,Model model,
			@RequestParam(value = "id", required = true) Integer id){
		
		System.err.println("正在执行查询账号操作------------>id="+id);
		return null;
	}
	
	//----------------------------------get/set--------------------------
}
