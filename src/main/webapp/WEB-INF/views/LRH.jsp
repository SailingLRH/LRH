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
    
    <title>老人与海</title>
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
	
	<script type="text/javascript">thisModel=1;sonModel=1;</script>
	<link rel="stylesheet" type="text/css" href="resources/default/css/spitslot.css">
	<link rel="stylesheet" type="text/css" href="resources/default/css/blog.css">
  </head>
  
  <body>
    <div class="all">
	    <jsp:include page="head.jsp"></jsp:include>
	    <jsp:include page="leftMenu.jsp"></jsp:include>
	    <div class="right_all">
	    	<c:choose>
		    	<c:when test="${!empty requestScope.dynamicList && requestScope.dynamicList.size()>0}">
		    		<c:forEach items="#{requestScope.dynamicList}" var="dynamic">
		    			<c:if test="${dynamic.moduleId==7}">
		    				<div class="spitslot_data win" id="spitslot_div_${dynamic.data.id}" onclick="initSpitslotReply('${dynamic.data.id}')">
								<div class="win_head">
									<p class="win_head_icon">
										<img src="/loadImg?imgPath=${dynamic.data.user.photoUrl}"/>
									<p>
									<p class="win_head_title"><span class="red_and_shadow">${dynamic.data.user.showName}</span>&nbsp;&nbsp;发表了一条吐槽</p>
				
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
										<div class="content_and_imgs content_all" id="spitslot_content_${dynamic.data.id}">
											<div class="content">
												<p>${dynamic.data.content}</p>
											</div>
											
											<c:if test="${!empty dynamic.data.imgList}">
												<div class="data_imgs">
													<c:forEach items="${dynamic.data.imgList}" var="img">
														<p class="needShadow">
															<img src="/loadImg?imgPath=${img.fileUrl}"/>
														</p>
													</c:forEach>
													<div class="clear"></div>
												</div>
											</c:if>
										</div>
										
										<!-- 评论列表 右浮动 -->
										<div class="comments_and_publish_div" id="comment_list_${dynamic.data.id}" onmouseover="showZoom(event,this)" onmouseout="hideZoom(event,this)">
											<div class="comments">
												<div class="zoom"><a onclick="changeCommentDivHight(this,'spitslot_div_${dynamic.data.id}')" class="smaller" title="高一点"></a></div>
												<c:forEach items="${dynamic.data.comments}" var="comment">
													<div class="comment">
														<div class="comment_content">
															<p class="float_left">
																<img src="/loadImg?imgPath=${comment.commentUser.photoUrl}" class="user_head_img"/>
															</p>
															
															<div class="float_left comment_reply_content ">
																<p><a class="user_name">${comment.commentUser.showName}</a></p>
																<p class="reply_text">${comment.content}</p>
																<p class="time_reply_p">
																	<span class="float_left italic grey">${comment.formateTime}</span>
																	<a class="float_right reply_a" onclick="prePublishComment('${dynamic.data.id}','${comment.id}',this,'${comment.commentUser.showName}')">
																		回复(<span>${comment.replyCount}</span>)
																	</a>
																	<label class="clear"></label>
																</p>
															</div>
															<div class="clear"></div>
															
														</div>
														<c:if test="${!empty comment.replys}">
															<div class="comment_replys">
																<c:forEach items="${comment.replys}" var="reply">
																	<div class="reply">
																		<p class="float_left">
																			<img src="/loadImg?imgPath=${reply.replyUser.photoUrl}" class="user_head_img"/>
																		</p>
																		
																		<div class="float_left comment_reply_content">
																			<p>
																				<a class="user_name">${reply.replyUser.showName}</a>
																				<span class="grey">回复  ${reply.toUser.showName}:</span>
																			</p>
																			<p class="reply_text">
																				${reply.content}
																			</p>
																			<p class="time_reply_p">
																				<span class="float_left italic grey">${reply.formateTime}</span>
																				<a class="float_right reply_a" onclick="prePublishComment('${dynamic.data.id}','${reply.id}',this,'${reply.replyUser.showName}')">
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
																		<a onclick="moreReply('${dynamic.data.id}',2,'${comment.id}',this,'${comment.replyCount}')">----------[查看更多回复]----------</a>
																	</p>
																</c:if>
															</div>
														</c:if>
													</div>
												</c:forEach>
												<c:if test="${dynamic.data.commentsCount>5}">
													<p class="more_comment_p" id="more_comment_p_${dynamic.data.id}">
														<a onclick="showMoreComment('${dynamic.data.id}',2,'${dynamic.data.commentsCount}')">----------[查看更多评论]----------</a>
													</p>
												</c:if>
											</div>
											
											<div class="publish_comment">
												<p class="float_left input">
													<span id="mark_${dynamic.data.id}" class="mark"></span>
													<input id="comment_publish_input_${dynamic.data.id}" onkeydown="addMark(this);textLengthLimit(this,'',500,1)" onkeyup="addMark(this);textLengthLimit(this,'',500,1)" 
														onblur="addMark(this);textLengthLimit(this,'',500,1)" onfocus="createFloatEmojiDiv(this);addMark(this);textLengthLimit(this,'',500,1)"/>
												</p>
												<p class="float_right btn">
													<a onclick="publishComment('${dynamic.data.id}',7)" id="comment_publish_btn_${dynamic.data.id}">发表</a>
												</p>
												<div class="clear"></div>
											</div>
										</div>
										
										<div class="clear"></div>
									</div>
								</div>
				
								<div class="win_bottom">
									<div class="data_btns">
										<c:if test="${!empty sessionScope.user && sessionScope.user.id==dynamic.data.user.id}">
											<p class="delete"><a onclick="preDeleteSpitslot('${dynamic.data.id}')">删除</a></p>
										</c:if>
										<p class="praise">
											<a onclick="addPraise('${dynamic.data.id}',7)">
												点赞(<span id="data_praise_count_${dynamic.data.id}">${dynamic.data.praiseCount}</span>)
											</a>
										</p>
										<p class="comment">
											<a onclick="showOrHideComments('${dynamic.data.id}')">
												评论(<span id="data_comment_count_${dynamic.data.id}">${dynamic.data.commentsCount}</span>)
											</a>
										</p>
									</div>
									<div class="publish_time">${dynamic.data.addTime}</div>
									<div class="clear"></div>
								</div>
							</div>
		    			</c:if>
<!-- ############## 以上是吐槽,以下是博文 ############## -->		    			
		    			<c:if test="${dynamic.moduleId==9}">
		    				<div class="blog_data win" id="blog_div_${dynamic.data.id}" onclick="initSpitslotReply('${dynamic.data.id}')">
								<div class="win_head">
									<p class="win_head_icon">
										<img src="/loadImg?imgPath=${dynamic.data.user.photoUrl}"/>
									<p>
									<p class="win_head_title"><span class="red_and_shadow">${dynamic.data.user.showName}</span>&nbsp;&nbsp;发表了一篇博文&nbsp;&nbsp;《${dynamic.data.title}》</p>
				
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
										<div class="content_and_imgs content_all" id="blog_content_${dynamic.data.id}">
											<div class="content">
												<p>
													${dynamic.data.simpleContent}
													<c:if test="${empty dynamic.data.simpleContent || dynamic.data.simpleContent==''}">${dynamic.data.title}</c:if>
													<a href="/blog/blogDetail/${dynamic.data.id}" class="show_detail">[查看详情]</a>
												</p>
											</div>
											<c:if test="${!empty dynamic.data.firstImg && dynamic.data.firstImg != ''}">
												<div class="data_imgs">
													<p class="needShadow">
														<img src="${dynamic.data.firstImg}"/>
													</p>
													<div class="clear"></div>
												</div>
											</c:if>
											
										</div>
										
										<!-- 评论列表 右浮动 -->
										<div class="comments_and_publish_div" id="comment_list_${dynamic.data.id}" onmouseover="showZoom(event,this)" onmouseout="hideZoom(event,this)">
											<div class="comments">
												<div class="zoom"><a onclick="changeCommentDivHight(this,'spitslot_div_${dynamic.data.id}')" class="smaller" title="高一点"></a></div>
												<c:forEach items="${dynamic.data.comments}" var="comment">
													<div class="comment">
														<div class="comment_content">
															<p class="float_left">
																<img src="/loadImg?imgPath=${comment.commentUser.photoUrl}" class="user_head_img"/>
															</p>
															
															<div class="float_left comment_reply_content ">
																<p><a class="user_name">${comment.commentUser.showName}</a></p>
																<p class="reply_text">${comment.content}</p>
																<p class="time_reply_p">
																	<span class="float_left italic grey">${comment.formateTime}</span>
																	<a class="float_right reply_a" onclick="prePublishComment('${dynamic.data.id}','${comment.id}',this,'${comment.commentUser.showName}')">
																		回复(<span>${comment.replyCount}</span>)
																	</a>
																	<label class="clear"></label>
																</p>
															</div>
															<div class="clear"></div>
															
														</div>
														<c:if test="${!empty comment.replys}">
															<div class="comment_replys">
																<c:forEach items="${comment.replys}" var="reply">
																	<div class="reply">
																		<p class="float_left">
																			<img src="/loadImg?imgPath=${reply.replyUser.photoUrl}" class="user_head_img"/>
																		</p>
																		
																		<div class="float_left comment_reply_content">
																			<p>
																				<a class="user_name">${reply.replyUser.showName}</a>
																				<span class="grey">回复  ${reply.toUser.showName}:</span>
																			</p>
																			<p class="reply_text">
																				${reply.content}
																			</p>
																			<p class="time_reply_p">
																				<span class="float_left italic grey">${reply.formateTime}</span>
																				<a class="float_right reply_a" onclick="prePublishComment('${dynamic.data.id}','${reply.id}',this,'${reply.replyUser.showName}')">
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
																		<a onclick="moreReply('${dynamic.data.id}',2,'${comment.id}',this,'${comment.replyCount}')">----------[查看更多回复]----------</a>
																	</p>
																</c:if>
															</div>
														</c:if>
													</div>
												</c:forEach>
												<c:if test="${dynamic.data.commentsCount>5}">
													<p class="more_comment_p" id="more_comment_p_${dynamic.data.id}">
														<a onclick="showMoreComment('${dynamic.data.id}',2,'${dynamic.data.commentsCount}')">----------[查看更多评论]----------</a>
													</p>
												</c:if>
											</div>
											
											<div class="publish_comment">
												<p class="float_left input">
													<span id="mark_${dynamic.data.id}" class="mark"></span>
													<input id="comment_publish_input_${dynamic.data.id}" onkeydown="addMark(this);textLengthLimit(this,'',500,1)" onkeyup="addMark(this);textLengthLimit(this,'',500,1)" 
														onblur="addMark(this);textLengthLimit(this,'',500,1)" onfocus="createFloatEmojiDiv(this);addMark(this);textLengthLimit(this,'',500,1)"/>
												</p>
												<p class="float_right btn">
													<a onclick="publishComment('${dynamic.data.id}',9)" id="comment_publish_btn_${dynamic.data.id}">发表</a>
												</p>
												<div class="clear"></div>
											</div>
										</div>
										
										<div class="clear"></div>
									</div>
								</div>
				
								<div class="win_bottom">
									<div class="data_btns">
										<c:if test="${!empty sessionScope.user && sessionScope.user.id==dynamic.data.user.id}">
											<p class="delete"><a onclick="preDeleteBlog('${dynamic.data.id}')">删除</a></p>
										</c:if>
										<p class="praise">
											<a onclick="addPraise('${dynamic.data.id}',9)">
												点赞(<span id="data_praise_count_${dynamic.data.id}">${dynamic.data.praiseCount}</span>)
											</a>
										</p>
										<p class="comment">
											<a onclick="showOrHideComments('${dynamic.data.id}')">
												评论(<span id="data_comment_count_${dynamic.data.id}">${dynamic.data.commentsCount}</span>)
											</a>
										</p>
									</div>
									<div class="publish_time">${dynamic.data.addTime}</div>
									<div class="clear"></div>
								</div>
							</div>
		    			</c:if>
		    		</c:forEach>
		    		<!-- 用于分页加载数据 -->
					<div id="more_dynamic_message" class="win more_data" onclick="getMoreDynamicData()" onmousedown="mouseIsDown(this)" onmouseup="mouseIsUp(this)">
						<span id="nowPage" class="needHide"></span>
						<span id="totalPage" class="needHide"></span>
						<span class="text">点击看看有没有了~</span>
						<!-- loading的Gif图片 -->
						<div class="loading"></div>
					</div>
		    	</c:when>
	    		<c:otherwise>
					<div class="win more_data" >
						<span class="text">目前还没有任何动态~</span>
					</div>
	    		</c:otherwise>
	    	</c:choose>
	    </div>
	    <div class="clear"></div>
    </div>
  </body>
  <script type="text/javascript" src="resources/default/js/spitslot.js"></script>
  <script type="text/javascript" src="resources/default/js/blog.js"></script>
</html>
