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
    
    <title>游戏乐园</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is home page of OLD MAN AND SEA">
	
	<link type="image/x-icon" rel="icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="shortcut icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="resources/default/img/icon/favicon.ico">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="resources/default/css/game_shudu.css">
	<script type="text/javascript">thisModel=8;</script>
  </head>
  
  <body>
    <div class="all">
	    <jsp:include page="head.jsp"></jsp:include>
	    <jsp:include page="leftMenu.jsp"></jsp:include>
	    <div class="right_all">
	    	<div class="game_menu win">
	    		<div class="win_head">
    				<p class="win_head_icon"><p>
    				<p class="win_head_title">游戏清单</p>
	    			
    				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)"></a><p>
    				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
    				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
	    			
	    			<div class="clear"></div>
	    		</div>
	    		
	    		<div class="win_body">
	    			<div class="win_body_all">
		    			<p class="menu_item_p">
		    				<a class="menu_icon" onclick="findGame(1)"><img src="resources/default/img/shudu/shudu.png"/></a>
		    				<a class="menu_title" onclick="findGame(1)">经典数独</a>
		    			</p>
		    			
		    			<p class="menu_item_p">
		    				<a class="menu_icon"><img src="resources/default/img/shudu/game_icon.png"/></a>
		    				<a class="menu_title">汉诺塔</a>
		    			</p>
		    			
		    			<p class="menu_item_p">
		    				<a class="menu_icon"><img src="resources/default/img/shudu/game_icon.png"/></a>
		    				<a class="menu_title">中国象棋</a>
		    			</p>
		    			
		    			<p class="menu_item_p">
		    				<a class="menu_icon"><img src="resources/default/img/shudu/game_icon.png"/></a>
		    				<a class="menu_title">五子棋</a>
		    			</p>
		    			
		    			<p class="menu_item_p">
		    				<a class="menu_icon"><img src="resources/default/img/shudu/game_icon.png"/></a>
		    				<a class="menu_title">找不同</a>
		    			</p>
		    			
		    			<p class="menu_item_p">
		    				<a class="menu_icon"><img src="resources/default/img/shudu/game_icon.png"/></a>
		    				<a class="menu_title">假设有很多</a>
		    			</p>
	    			</div>
	    		</div>
	    		
	    		<div class="win_bottom">
	    		
	    		</div>
	    	</div>
	    	<div class="win win_parting_line"></div>
	    	<!-- 数独关卡列表 -->
	    	<div class="shudu_level win">
	    		<div class="win_head">
    				<p class="win_head_icon"><p>
    				<p class="win_head_title">数独关卡</p>
	    			
    				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
    				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
    				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
	    			
	    			<div class="clear"></div>
	    		</div>
	    		
	    		<div class="win_body">
	    			<div class="win_body_all">
						<p class="level_p"><a href="game/shudu/easy">简易 数独</a></p>
		    			<p class="level_p"><a href="game/shudu/101">101 数独</a></p>
		    			<p class="level_p"><a href="game/shudu/LRH">LRH 数独</a></p>
						<p class="level_p"><a href="game/shudu/heart">心形 数独</a></p>
						<p class="level_p"><a href="game/shudu/smile">笑脸 数独</a></p>
						<p class="level_p"><a href="game/shudu/libra">天秤座 数独</a></p>
						<p class="level_p more"><a href="javaScript:void(0)" onclick="showMore()">更多</a></p>
	    			</div>
	    		</div>
	    		
	    		<div class="win_bottom">
	    		
	    		</div>
	    	</div>
	    </div>
	    <div class="clear"></div>
    </div>
  </body>
<script type="text/javascript" src="resources/default/js/game_shudu.js"></script>
</html>
