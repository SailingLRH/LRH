package com.lrh.util;


import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.mail.EmailException;

/**
 * 发送邮件工具类
 * @author LRH
 * @since 2015-09-22
 *
 */
public class ToolEmail {
	
	private static ResourceBundle resource = ResourceBundle.getBundle("application");  
	private static final String HOST = resource.getString("email.host");
	private static final String EMAIL_NAME = resource.getString("email.name");
	private static final String EMAIL_PASSWORD = resource.getString("email.password");
	private static final String NAME = resource.getString("project.name");
	
	/**
	 * 发送htmlEmail
	 * @param receiver	接收人邮箱
	 * @param subject	邮件主题
	 * @param action	邮件行为  1:注册,2:激活邮件,3:找修改密码
	 * @param actionCode 验证码
	 * @throws EmailException
	 * @throws MalformedURLException
	 */
	public static Map<String,Object> sendHtmlEmail(String receiver,String subject,String action,String actionCode) throws EmailException, MalformedURLException{
		
		ToolEmailSend email=new ToolEmailSend();
		email.setHost(HOST);
		email.setFrom(EMAIL_NAME);//发送者
		email.addTo(receiver);//接收者
		email.setUser(EMAIL_NAME);//用户名
		email.setPassword(EMAIL_PASSWORD);//密码
		email.setSubject(subject);//主题
		String htmlMsg = getHtmlMsg(receiver, action,actionCode);
		email.addHtmlContent(htmlMsg);//内容
		
		//上传附件
//		if(upload!=null){
//			System.out.println("附件名:------------------->"+uploadFileName);
//			//上传附件
//			try {
//				email.addAttachment(uploadFileName, new FileInputStream(upload), null);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
        String result = email.send();
        Map<String, Object> sendResult=new HashMap<String, Object>();
        if(result.equals("-1")){
        	sendResult.put("isSuccess", false);
        	sendResult.put("msg", "连接超时,邮件发送失败!");
        	sendResult.put("data", null);
        }else if(result.equals("1")){
        	sendResult.put("isSuccess", true);
        	sendResult.put("msg", "邮件已发送至您的邮箱,请登录邮箱查看!");
        	sendResult.put("data", null);
        }else if(result.equals("252")){
        	sendResult.put("isSuccess", false);
        	sendResult.put("msg", "您的邮箱不存在或拒绝访问,请核对!");
        	sendResult.put("data", null);
        }else if(result.equals("251") || result.equals("334")){
        	sendResult.put("isSuccess", false);
        	sendResult.put("msg", "系统内部错误,邮件发送失败!");
        	sendResult.put("data", null);
        }else if(result.equals("235")){
        	sendResult.put("isSuccess", false);
        	sendResult.put("msg", "系统内部错误,邮件发送失败!");
        	sendResult.put("data", null);
        }else if(result.equals("220")){
        	sendResult.put("isSuccess", false);
        	sendResult.put("msg", "系统连接邮箱服务器失败,邮件发送失败!");
        	sendResult.put("data", null);
        }else{
        	sendResult.put("isSuccess", false);
        	sendResult.put("msg", "未知错误,邮件发送失败,请重试!");
        	sendResult.put("data", null);
        }
        
        return sendResult;
	}
	
	private static String getHtmlMsg(String receiver,String action,String actionCode){
		String htmlMsg = "";
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if(action.equals("1")){//注册
			htmlMsg = (
				"<html>" +
						"<div style='padding:10px;border-bottom:1px solid #ccc;'>" +
						"</div>"
						+ "<div style='padding:10px 0;'>"
						+ "尊敬的用户，您好：<br>"
						+ "<p style='text-indent: 2em;'>感谢您加入"+NAME+"。为防止有些人滥注册,以及日后有忘记密码或有重要通知的需要,账号需要跟您的邮箱账号绑定。</p>"
						+ "<p style='text-indent: 2em;'>您本次获得的验证码是:<B style=\"color:#ff0000;font-size: 20px;\">"+actionCode+"</B><br/>请30分钟之内将红色的验证码输入到表单以完成注册。</p>"
						+ "<H3 style='color:#ff0000'>注意:</H3>"
						+ "<p style='text-indent: 2em;'>"
						+ "验证码仅本次会话有效,如果您在其它PC/移动端使用,或打开其它浏览器,或者关闭了浏览器后重新打开,验证码都是无效的!"
						+ "</p>"
						+ "<p style='text-indent: 2em;'>致敬!</p>"
						+ "</div>"
						+ "<div style='font-size:12px;border-top:1px solid #c0c0c0;text-align:right;color:#c0c0c0;font-style:italic;'>"
						+ "<p style='margin:10 0;padding:0;'>----from "+NAME+"</p>"
						+ "<p>"+time+"</p>"
						+ "</div>" +
				"</html>");
		}else if(action.equals("2")){//获取临时密码
			htmlMsg = (
					"<html>" +
							"<div style='padding:10px;border-bottom:1px solid #ccc;'>" +
							"</div>"
							+ "<div style='padding:10px 0;'>"
							+ "尊敬的用户，您好：<br>"
							+ "<p style='text-indent: 2em;'>忘记登录密码了吗?您的账号正在执行获取临时登录密码操作,如非本人操作,请忽视本邮件.</br>"
							+ "系统已生成一个临时密码给您了,登录后,把密码改下吧!<br/>"
							+ "您本次获得的临时登录密码是:<B style=\"color:#ff0000;font-size: 20px;\">"+actionCode+"</B></p>"
							+ "<H3 style='color:#ff0000'>注意:</H3>"
							+ "<p style='text-indent: 2em;'>"
							+ "临时密码仅本次会话有效,如果您在其它PC/移动端使用,或打开其它浏览器,或者关闭了浏览器后重新打开,临时密码都是无效的!哦,对了,密码区分大小写哦!"
							+ "</p>"
							+ "<p style='text-indent: 2em;'>致敬!</p>"
							+ "</div>"
							+ "<div style='font-size:12px;border-top:1px solid #c0c0c0;text-align:right;color:#c0c0c0;font-style:italic;'>"
							+ "<p style='margin:10 0;padding:0;'>----from "+NAME+"</p>"
							+ "<p>"+time+"</p>"
							+ "</div>" +
					"</html>");
		}else if(action.equals("3")){
			htmlMsg = (
					"<html>" +
							"<div style='padding:10px;border-bottom:1px solid #ccc;'>" +
							"</div>"
							+ "<div style='padding:10px 0;'>"
							+ "尊敬的用户，您好：<br>"
							+ "<p style='text-indent: 2em;'>您的账号正在执行修改密码操作,如非本人操作,请忽视本邮件.<br/>"
							+ "您本次获得的验证码是:<B style=\"color:#ff0000;font-size: 20px;\">"+actionCode+"</B></p>"
							+ "<H3 style='color:#ff0000'>注意:</H3>"
							+ "<p style='text-indent: 2em;'>"
							+ "验证码仅本次会话有效,如果您在其它PC/移动端使用,或打开其它浏览器,或者关闭了浏览器后重新打开,验证码都是无效的!"
							+ "</p>"
							+ "<p style='text-indent: 2em;'>致敬!</p>"
							+ "</div>"
							+ "<div style='font-size:12px;border-top:1px solid #c0c0c0;text-align:right;color:#c0c0c0;font-style:italic;'>"
							+ "<p style='margin:10 0;padding:0;'>----from "+NAME+"</p>"
							+ "<p>"+time+"</p>"
							+ "</div>" +
					"</html>");
		}
		return htmlMsg;
	}
	
}
