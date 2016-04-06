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
    
    <title>提到我的</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is home page of OLD MAN AND SEA">
	
	<link type="image/x-icon" rel="icon" href="/resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="shortcut icon" href="/resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="/resources/default/img/icon/favicon.ico">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">thisModel=1;sonModel=2;</script>
	<link rel="stylesheet" type="text/css"
		href="/resources/default/css/spitslot.css">
	</head>
  </head>
  
  <body>
    <div class="all">
	    <jsp:include page="head.jsp"></jsp:include>
	    <jsp:include page="leftMenu.jsp"></jsp:include>
	    <div class="right_all">
	    	<c:choose>
		    	<c:when test="${!empty requestScope.msgList && requestScope.msgList.size()>0}">
					<c:forEach items="#{requestScope.msgList}" var="msg">
						<c:if test="${msg.isRead}">
							<div class="win one_msg_win one_msg_all_old" id="one_msg_win_${msg.id}">
								<p class="user_head_img">
									<img src="/loadImg?imgPath=${msg.initiator.photoUrl}"/>
								</p>
								<div class="one_msg_all one_msg_all_old">
									<h5>${msg.title}</h5>
									<p class="tail">${msg.content}</p>
									<p class="time">${msg.formateTime}</p>
									<p class="url">
										<c:if test="${msg.model==9}">
											<a href="${msg.url}" target="_blank" onclick="hasRead('${msg.id}')">查看详情&gt;&gt;</a>
										</c:if>
										<c:if test="${msg.model!=9}">
											<a onclick="showTailWin('${msg.url}','${msg.id}')">查看详情&gt;&gt;</a>
										</c:if>
									</p>
								</div>
							</div>
						</c:if>
						<c:if test="${!msg.isRead}">
							<div class="win one_msg_win one_msg_all_new" id="one_msg_win_${msg.id}">
								<span class="new"></span>
								<p class="user_head_img">
									<img src="/loadImg?imgPath=${msg.initiator.photoUrl}"/>
								</p>
								<div class="one_msg_all one_msg_all_new">
									<h5>${msg.title}</h5>
									<p class="tail">${msg.content}</p>
									<p class="time">${msg.formateTime}</p>
									<p class="url">
										<c:if test="${msg.model==9}">
											<a href="${msg.url}" target="_blank" onclick="hasRead('${msg.id}')">查看详情&gt;&gt;</a>
										</c:if>
										<c:if test="${msg.model!=9}">
											<a onclick="showTailWin('${msg.url}','${msg.id}')">查看详情&gt;&gt;</a>
										</c:if>
									</p>
								</div>
							</div>
						</c:if>
					</c:forEach>	    	
				    <!-- 用于分页加载数据 -->
					<div id="more_notice_message" class="win more_data" onclick="updateNoticeData()" onmousedown="mouseIsDown(this)" onmouseup="mouseIsUp(this)">
						<span id="nowPage" class="needHide"></span>
						<span id="totalPage" class="needHide"></span>
						<span class="text">点击看看有没有了~</span>
						<!-- loading的Gif图片 -->
						<div class="loading"></div>
					</div>
		    	</c:when>
		    	<c:otherwise>
					<div id="more_notice_message" class="win more_data">
						<span class="text">目前还没有与我相关的数据~</span>
					</div>
		    	</c:otherwise>
		    </c:choose>
	    </div>
    </div>
  </body>
  <script type="text/javascript" src="/resources/default/js/notice.js"></script>
  <script type="text/javascript" src="/resources/default/js/spitslot.js"></script>
</html>