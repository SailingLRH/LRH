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
    
    <title>影视推荐</title>
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
	<script type="text/javascript">thisModel=3;</script>
	<!-- 引入腾讯视频播放组件 -->
	<script type="text/javascript" src="http://qzs.qq.com/tencentvideo_v1/js/tvp/tvp.player.js"></script>
  </head>
  
  <body>
    <div class="all">
	    <jsp:include page="head.jsp"></jsp:include>
	    <jsp:include page="leftMenu.jsp"></jsp:include>
	    <div class="right_all">
	    	<div class="one_video win" id="video_div_1" >
				<div class="win_head">
					<p class="win_head_icon">
						<img src="user/loadImg?imgPath=none"/>
					<p>
					<p class="win_head_title"><span class="red_and_shadow">Sailing_LRH</span>&nbsp;&nbsp;分享了一个视频&nbsp;--《捉妖记》</p>

					<p class="win_controll_close_btn win_controll_btn">
						<a href="javaScript:void(0)" class="able"></a>
					<p>
					<p class="win_controll_max_btn win_controll_btn">
						<a href="javaScript:void(0)"></a>
					</p>
					<p class="win_controll_min_btn win_controll_btn">
						<a href="javaScript:void(0)" class="able"></a>
					<p>
					<div class="clear"></div>
				</div>

				<div class="win_body">
					<div class="win_body_all">
						<!-- 内容部分 左浮动 -->
						<div class="content_and_imgs content_all" id="video_content_1">
							<div class="content">
								<div id="mod_player">
									<!-- 这个div是播放器准备输出的位置 -->
								</div>
							</div>
						</div>
						
						<!-- 评论列表 右浮动 -->
						<div class="comments_and_publish_div" id="comment_list_1" onmouseover="showZoom(event,this)" onmouseout="hideZoom(event,this)">
							<div class="comments">
								<div class="zoom"><a onclick="changeCommentDivHight(this,'video_content_1')" class="smaller" title="高一点"></a></div>
								<div class="comment">
									<div class="comment_content">
										<p class="float_left">
											<img src="user/loadImg?imgPath=none" class="user_head_img"/>
										</p>
										
										<div class="float_left comment_reply_content ">
											<p><a class="user_name">Sailing_LRH</a></p>
											<p class="reply_text">很好看哟~</p>
											<p class="time_reply_p">
												<span class="float_left italic grey">2016-01-01 00:00:00</span>
												<a class="float_right reply_a" onclick="prePublishComment('1','1',this,'Sailing_LRH')">
													回复(<span>0</span>)
												</a>
												<label class="clear"></label>
											</p>
										</div>
										<div class="clear"></div>
									</div>
								</div>
							</div>
							
							<div class="publish_comment">
								<p class="float_left input">
									<span id="mark_1" class="mark"></span>
									<input id="comment_publish_input_1" onkeydown="addMark(this);textLengthLimit(this,'',500,1)" onkeyup="addMark(this);textLengthLimit(this,'',500,1)" 
										onblur="addMark(this);textLengthLimit(this,'',500,1)" onfocus="createFloatEmojiDiv(this);addMark(this);textLengthLimit(this,'',500,1)"/>
								</p>
								<p class="float_right btn">
									<a onclick="publishComment('1')" id="comment_publish_btn_1">发表</a>
								</p>
								<div class="clear"></div>
							</div>
						</div>
						
						<div class="clear"></div>
					</div>
				</div>

				<div class="win_bottom">
					<div class="data_btns">
						<p class="delete"><a onclick="preDeleteSpitslot('1')">删除</a></p>
						<p class="praise">
							<a onclick="addPraise('1')">
								点赞(<span id="data_praise_count_1">99</span>)
							</a>
						</p>
						<p class="comment">
							<a onclick="showOrHideComments('1')">
								评论(<span id="data_comment_count_1">1</span>)
							</a>
						</p>
					</div>
					<div class="publish_time">2015-12-31 23:59:59</div>
					<div class="clear"></div>
				</div>
			</div>
	    	
	    </div>
	    <div class="clear"></div>
    </div>
  </body>
  <script type="text/javascript" src="/resources/default/js/video.js"></script>
</html>
