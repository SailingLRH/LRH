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
    
    <title>欢迎访问~</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="refresh" content="0;url=/LRH">
	
	<link type="image/x-icon" rel="icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="shortcut icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="resources/default/img/icon/favicon.ico">
	<script type="text/javascript" src="resources/js/common.js"></script>
	<style type="text/css">
		*{margin: 0;padding: 0;}
		div.loading{background: url("resources/default/img/bg/loading.gif") 50% 50% #333333 no-repeat;
			width: 100%;height: 100%;
		}
	</style>
  </head>
  
  <body>
  	<div class="loading"></div>
  </body>
</html>
