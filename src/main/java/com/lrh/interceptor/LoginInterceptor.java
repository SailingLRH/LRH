package com.lrh.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lrh.model.User;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
        logger.info("请求地址："+request.getRequestURI());
		String uri = request.getRequestURI();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			System.err.println("没有登录----------------->请求地址："+uri);
			try {
//				Map<String, Object> object=new HashMap<String, Object>();
//				object.put("isSuccess", false);
//				object.put("msg", "哎呦~真不好意思,要登录之后才能操作呢!");
//				object.put("data", null);
//				
//				response.setContentType("application/json;charset=utf-8");
//				String json = JSON.toJSONString(object,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
//				response.getWriter().write(json);
//				response.getWriter().flush();
//				response.getWriter().close();
				
				request.setAttribute("msg", "哎呦~真不好意思,要登录之后才能操作呢!");
				request.getRequestDispatcher("/LRH").forward(request, response);  
				return false;
			} catch (IOException e) {
				logger.error("发生异常:"+e.getMessage());
			}
			//response.sendRedirect("/LRH");
		}
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(  
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)  
            throws Exception {  
    }  
	@Override
    public void afterCompletion(  
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  
            throws Exception {
		if(ex!=null){
			System.err.println("有异常..."+ex.getMessage());
		}else{
			//System.err.println("没有异常!");
		}
    }  
}


    