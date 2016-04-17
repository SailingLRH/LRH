<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	
	<link type="image/x-icon" rel="icon" href="/resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="shortcut icon" href="/resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="/resources/default/img/icon/favicon.ico">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">thisModel=12;</script>
	<link rel="stylesheet" type="text/css" href="/resources/default/css/chat.css">
  </head>
  
  <body>
    <div class="all">
	    <jsp:include page="head.jsp"></jsp:include>
	    <jsp:include page="leftMenu.jsp"></jsp:include>
	    <div class="right_all">
	    	<div class="search_user win">
	    		<div class="win_head">
	   				<p class="win_head_icon"></p>
	   				<p class="win_head_title">搜索网友</p>
	    			
	   				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)" class="able" onclick="openWriteBlogWin()"></a></p>
	   				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
	   				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a></p>
	    			
	    			<div class="clear"></div>
	    		</div>
	    		
	    		<div class="win_body">
	    			<div class="win_body_all">
	    				<div id="search-user-box">
		    				<p class="search_input float_left h26_input" onclick="clearMark(this)">
		    					<I class="help_text grey">输入网友账号/邮箱号/昵称 (后面加上"/男"或"/女"可以按性别过滤)</I>
		    					<input type="text" class="" onblur="addMark(this)" id="search-content"/>
		    				</p>
	    					<p class="float_left search-btn radius_5px"><a class="radius_5px" onclick="loadMoreUser()">搜索</a></p>
	    					<div class="clear"></div>
	    				</div>
	    				
	    				<div id="user-list" total="${requestScope.pageInfo.total}" totalPage="${requestScope.pageInfo.totalPage}" max="10">
	    					<ul class="head-ul data-ul">
	    						<li class="float_left head col1 dataInfo_table_th_a">
	    							头像
	    						</li>
	    					
	    						<li class="float_left head col2 dataInfo_table_th_b">
	    							用户名
	    						</li>
	    						
	    						<li class="float_left head col3 dataInfo_table_th_a">
	    							昵称
	    						</li>
	    						
	    						<li class="float_left head col4 dataInfo_table_th_b">
	    							性别
	    						</li>
	    						
	    						<li class="float_left head col5 dataInfo_table_th_a">
	    							是否在线
	    						</li>
	    					</ul>
	    					<div class="clear"></div>
	    					
	    					<div id="user-list-foreach">
		    					<c:if test="${empty requestScope.userList}">
		    						<p class="no_data">未能找到任何用户...</p>
		    					</c:if>
		    					<c:if test="${!empty requestScope.userList}">
		    						<c:forEach items="${requestScope.userList}" var="one">
			    						<c:choose>
			    							<c:when test="${!empty sessionScope.user && one.isOnline && one.id != sessionScope.user.id}">
					    						<ul class="one-user-ul data-ul able" title="双击与TA聊天" ondblclick="openChatWin('${one.id}','${one.showName}','${one.photoUrl}')">
			    							</c:when>
			    							<c:otherwise>
					    						<ul class="one-user-ul data-ul unable">
			    							</c:otherwise>
			    						</c:choose>
				    						<li class="float_left col1">
				    							<img src="/loadImg?imgPath=${one.photoUrl}"/>
				    						</li>
				    					
				    						<li class="float_left col2">
				    							${one.userName}
				    						</li>
				    						
				    						<li class="float_left col3">
				    							${one.nickName == null || one.nickName == ""?"[暂无昵称]":one.nickName}
				    						</li>
				    						
				    						<li class="float_left col4">
				    							${one.sexStr}
				    						</li>
				    						
				    						<li class="float_left col5">
				    							<c:if test="${one.isOnline==true}">
				    								<span style="color:green !important">在线</span>
				    							</c:if>
				    							<c:if test="${one.isOnline!=true}">
				    								<span style="color:red !important">离线</span>
				    							</c:if>
				    						</li>
				    					</ul>
				    					<div class="clear"></div>
		    						</c:forEach>
		    					</c:if>
	    					</div>
	    				</div>
   						<div id="pageInfo">
   							<!-- 生成的分页代码将插入这里 -->
   						</div>
	    			</div>
	    		</div>
	    		<div class="win_bottom"></div>
	    	</div>
	    </div>
	    <div class="clear"></div>
    </div>
  </body>
  <script type="text/javascript" src="/resources/default/js/chat.js"></script>
</html>
