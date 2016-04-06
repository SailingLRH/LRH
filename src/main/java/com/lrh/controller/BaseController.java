package com.lrh.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
public class BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	public void outHTML(HttpServletResponse response,String html) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(html);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public void outHTMLP(HttpServletResponse response,String callback,String html) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(callback+"("+html+")");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	public void outJson(HttpServletResponse response,Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteEnumUsingToString,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outJson2(HttpServletResponse response, Object object) {
		try {
			response.setContentType("application/json;charset=utf-8");
			String json = JSON.toJSONString(object,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void outJsonResult(HttpServletResponse response, Boolean isSuccess,String msg,Object data) {
		try {
			Map<String, Object> object=new HashMap<String, Object>();
			object.put("isSuccess", isSuccess);
			object.put("msg", msg);
			object.put("data", data);
			
			response.setContentType("application/json;charset=utf-8");
			String json = JSON.toJSONString(object,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outJson(HttpServletResponse response,List<?> list) {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSON.toJSONString(list.toArray(),SerializerFeature.WriteDateUseDateFormat));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outJson(HttpServletResponse response,List<?> list,PropertyFilter propertyFilter) {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSON.toJSONString(list.toArray(),propertyFilter,SerializerFeature.WriteDateUseDateFormat));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出JSONP(跨域响应)
	 * @param object
	 */
	public void outJsonP(HttpServletResponse response,String callback,Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(callback+"("+json+")");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void outJsonP(HttpServletResponse response, String callback, List<?> list) {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(callback+"("+JSON.toJSONString(list.toArray(),SerializerFeature.WriteDateUseDateFormat)+")");			
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微店铺前端页面调用时输出
	 * 
	 * @author Xiadi
	 *
	 * @param request
	 * @param response
	 * @param data
	 */
	public void outWebFrontData(HttpServletRequest request, HttpServletResponse response, Object data) {
		String callback = request.getParameter("callback");
		if (StringUtils.isEmpty(callback)) {
			this.myOutJson(response, data);
		} else {
			this.myOutJsonP(response, callback, data);
		}
	}
	
	private void myOutJson(HttpServletResponse response,Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss",
					SerializerFeature.WriteEnumUsingToString,
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.WriteMapNullValue,
					SerializerFeature.DisableCircularReferenceDetect);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出JSONP(跨域响应)
	 * @param object
	 */
	private void myOutJsonP(HttpServletResponse response,String callback,Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss",
					SerializerFeature.WriteEnumUsingToString,
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.WriteMapNullValue,
					SerializerFeature.DisableCircularReferenceDetect);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(callback+"("+json+")");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加cookies
	 * */
	public void addCookie(String key,String value,int maxAge,HttpServletResponse response){
	    Cookie cookie = new Cookie(key,value);
	    cookie.setPath("/");
	    if(maxAge>0)  cookie.setMaxAge(maxAge);
	    response.addCookie(cookie);
	}
	
	/**
	 * 用流将上传后的图片加载到页面
	 * @author Sailing_LRH
	 * @since 2015年10月7日
	 * @param response
	 * @param path 图片的绝对路径
	 * @throws IOException
	 */
	public void loadImage(HttpServletRequest request,HttpServletResponse response,String path) throws IOException{
		
		File photo=new File(path);
		if( (!photo.exists() ) || (!photo.isFile() )){//如果文件不存在,那么读取默认图片
			path=request.getSession().getServletContext().getRealPath("/resources/default/img/icon/default_photo.png");
		}
		System.out.println("正在加载图片...文件绝对路径为:"+path);
        FileInputStream FIS = new FileInputStream(path); // 以byte流的方式打开文件  
        int fileSize=FIS.available(); //得到文件大小   
        byte data[]=new byte[fileSize];   
        FIS.read(data);  //读数据   
        response.setContentType("image/*"); //设置返回的文件类型   
        OutputStream toClient=response.getOutputStream(); //得到向客户端输出二进制数据的对象   
        toClient.write(data);  //输出数据   
        toClient.flush();  
        toClient.close();   
        FIS.close(); 
	}
	
	public void loadFile(HttpServletResponse response,String filePath,String fileType) throws IOException{
		File file=new File(filePath);
		if( file.exists() && file.isFile() ){//如果文件不存在
			System.out.println("正在加载文件...文件绝对路径为:"+filePath+",文件类型:"+fileType);
			FileInputStream FIS = new FileInputStream(filePath); // 以byte流的方式打开文件  
			int fileSize=FIS.available(); //得到文件大小   
			byte data[]=new byte[fileSize];   
			FIS.read(data);  //读数据   
			response.setContentType(fileType+"/*"); //设置返回的文件类型   
			OutputStream toClient=response.getOutputStream(); //得到向客户端输出二进制数据的对象   
			toClient.write(data);  //输出数据   
			toClient.flush();  
			toClient.close();   
			FIS.close(); 
		}
	}
	
	/**
	 * 获取访问者ip
	 * @author Sailing_LRH
	 * @since 2015年10月10日
	 * @param request
	 * @return ip
	 */
	public String getIpAddr(HttpServletRequest request) {   
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress= inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
			if(ipAddress.indexOf(",")>0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress; 
	}
	
	/**
	 * 用于分页,求总页数和查询起始位置
	 * @author Sailing_LRH
	 * @since 2015年12月27日
	 * @param count
	 * @param max
	 * @param page 目标页
	 * @return totalPage start nowPage
	 */
	public Map<String,Long> getTotalPage(long count,long max,long page){
		Map<String,Long> map = new HashMap<String , Long>();
		long totalPage=count%max==0?count/max:count/max+1;
		page=page<1?1:page;
		//page=page>totalPage?totalPage:page;
		long start=(page-1)*max;
		map.put("start", start);
		map.put("totalPage", totalPage);
		map.put("nowPage", page);
		return map;
	}
	
	/**
	 * 跳转到错误页面
	 * @author Sailing_LRH
	 * @since 2016年2月3日
	 * @param msg 错误信息
	 * @param request
	 * @return
	 */
	public String openErrorPage(String msg,HttpServletRequest request){
		request.setAttribute("errMsg", msg);
		return "error";
	}
}
