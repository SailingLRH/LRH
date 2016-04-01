<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%--
    <base href="<%=basePath%>">
   --%>

<title>《${requestScope.blog.title}》</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description"
	content="This is blog page of OLD MAN AND SEA">

<link type="image/x-icon" rel="icon"
	href="/resources/default/img/icon/favicon.ico">
<link type="image/x-icon" rel="shortcut icon"
	href="/resources/default/img/icon/favicon.ico">
<link type="image/x-icon" rel="bookmark"
	href="/resources/default/img/icon/favicon.ico">

<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

<script type="text/javascript">
	thisModel = 9;
</script>
<link rel="stylesheet" type="text/css"
	href="/resources/default/css/blog.css">
</head>

<body>
	<div class="all">
		<jsp:include page="head.jsp"></jsp:include>
		<jsp:include page="leftMenu.jsp"></jsp:include>
		<div class="right_all">
			<!-- 列表区 -->
			<div class="blog_detail win">
	    		<div class="win_head">
	   				<p class="win_head_icon"><img src="/loadImg?imgPath=${requestScope.blog.user.photoUrl}"/><p>
	   				<p class="win_head_title"><span class="red_and_shadow">${requestScope.blog.user.showName}</span>&nbsp;&nbsp;的博文</p>
	    			
	   				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
	   				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
	   				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
	    			
	    			<div class="clear"></div>
	    		</div>
	    		
	    		<div class="win_body">
	    			<div class="win_body_all">
	    				<div class="blog_content_and_imgs">
		    				<center>
		    					<p class="write_blog_title">《${requestScope.blog.title}》</p>
		    					<p class="select_p">
									<label class="select">${requestScope.blog.typeName}</label>
									&nbsp;|&nbsp;
									<label class="select">
										<c:if test="${blog.publicType==1 }">对外公开</c:if>
				    					<c:if test="${blog.publicType==2 }">对好友公开</c:if>
				    					<c:if test="${blog.publicType==3 }">仅自己可见</c:if>
				    					<c:if test="${blog.publicType==4 }">指定人可见</c:if>
									</label>
								</p>
		    				</center>
		    				<div class="data_content_detail">
		    					${requestScope.blog.content}
		    				</div>
		    			</div>
		    			<!-- 评论区 -->
		    			<div class="data_detail_comments">
		    				<c:if test="${requestScope.blog.commentsCount<1}">
		    					<p class="grey nodata_msg">暂无评论</p>
		    				</c:if>
		    				<c:if test="${requestScope.blog.commentsCount>0}">
		    					<c:forEach items="${requestScope.blog.comments}" var="comment">
									<div class="data_detail_comment">
										<div class="data_detail_comment_content_all">
											<p class="float_left">
												<img src="/loadImg?imgPath=${comment.commentUser.photoUrl}" class="user_head_img"/>
											</p>
											
											<div class="float_left data_detail_comment_content ">
												<p><a class="data_detail_user_name">${comment.commentUser.showName}</a></p>
												<p class="reply_text">${comment.content}</p>
												<p class="time_reply_p">
													<span class="float_left italic grey">${comment.formateTime}</span>
													<a class="float_right reply_a" onclick="showOrHideBlogComments(0,'${requestScope.blog.id}',0);
														prePublishComment('${requestScope.blog.id}','${comment.id}',this,'${comment.commentUser.showName}')">
														回复(<span>${comment.replyCount}</span>)
													</a>
													<label class="clear"></label>
												</p>
											</div>
											<div class="clear"></div>
											
										</div>
										<c:if test="${!empty comment.replys}">
											<div class="data_detail_comment_replys">
												<c:forEach items="${comment.replys}" var="reply">
													<div class="data_detail_reply">
														<p class="float_left">
															<img src="/loadImg?imgPath=${reply.replyUser.photoUrl}" class="user_head_img"/>
														</p>
														
														<div class="float_left data_detail_comment_reply_content">
															<p>
																<a class="data_detail_user_name">${reply.replyUser.showName}</a>
																<span class="grey">回复  ${reply.toUser.showName}:</span>
															</p>
															<p class="reply_text">
																${reply.content}
															</p>
															<p class="time_reply_p">
																<span class="float_left italic grey">${reply.formateTime}</span>
																<a class="float_right reply_a" onclick="showOrHideBlogComments(0,'${requestScope.blog.id}',0);
																	prePublishComment('${requestScope.blog.id}','${reply.id}',this,'${reply.replyUser.showName}')">
																	回复
																</a>
																<label class="clear"></label>
															</p>
														</div>
														
														<div class="clear"></div>
													</div>
												</c:forEach>
												<c:if test="${comment.replyCount>5}">
													<p class="more_reply_p">
														<a onclick="moreReply('${requestScope.blog.id}',2,'${comment.id}',this,'${comment.replyCount}')">----------[查看更多回复]----------</a>
													</p>
												</c:if>
											</div>
										</c:if>
									</div>
								</c:forEach>
								<c:if test="${requestScope.blog.commentsCount>5}">
									<p class="more_comment_p" id="more_comment_p_${requestScope.blog.id}">
										<a onclick="showMoreComment('${requestScope.blog.id}',2,'${spitslot.commentsCount}')">----------[查看更多评论]----------</a>
									</p>
								</c:if>
		    				</c:if>
		    			</div>
	    			</div>
	    		</div>
	    		<div class="win_bottom">
	    			<div class="data_btns">
						<c:if test="${!empty sessionScope.user && sessionScope.user.id==blog.userId}">
							<p class="delete"><a onclick="preDeleteBlog('${blog.id}')">删除</a></p>
						</c:if>
						<p class="praise">
							<a onclick="addPraise('${requestScope.blog.id}')">
								点赞(<span id="data_praise_count_${requestScope.blog.id}">${requestScope.blog.praiseCount}</span>)
							</a>
						</p>
						<p class="comment">
							<a onclick="showOrHideBlogComments(this,'${requestScope.blog.id}')">
								评论(<span id="data_comment_count_${requestScope.blog.id}">${requestScope.blog.commentsCount}</span>)
							</a>
						</p>
					</div>
					<div class="publish_time">${requestScope.blog.addTime}</div>
					<div class="clear"></div>
	    		</div>
	    	</div>
		</div>
		<div class="clear"></div>
	</div>
</body>
<script type="text/javascript" src="/resources/default/js/blog.js"></script>
<script type="text/javascript" src="/resources/default/js/blog_detail.js"></script>
</html>
