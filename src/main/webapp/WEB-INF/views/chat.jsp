<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <%--
    <base href="<%=basePath%>">
   --%>
    
    <title>在线聊天</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link type="image/x-icon" rel="icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="shortcut icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="resources/default/img/icon/favicon.ico">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">thisModel=12;</script>
  </head>
  
  <body>
    <div class="all">
	    <jsp:include page="head.jsp"></jsp:include>
	    <jsp:include page="leftMenu.jsp"></jsp:include>
	    <div class="right_all">
	    	<div class="search_user win">
	    		<div class="win_head">
	   				<p class="win_head_icon"><p>
	   				<p class="win_head_title">搜索网友</p>
	    			
	   				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)" class="able" onclick="openWriteBlogWin()"></a><p>
	   				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
	   				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
	    			
	    			<div class="clear"></div>
	    		</div>
	    		
	    		<div class="win_body">
	    			<div class="win_body_all">
	    				<div class="search_input">
	    					<input type="text"/>
	    					<p><a>搜索</a></p>
	    					<I class="help_text">输入网友账号/邮箱号/昵称</I>
	    				</div>
	    			</div>
	    		</div>
	    		<div class="win_bottom"></div>
	    	</div>
	    </div>
	    <div class="clear"></div>
    </div>
  </body>
</html>
