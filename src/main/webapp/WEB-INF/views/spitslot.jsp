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

<title>吐槽天地</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description"
	content="This is spitslot page of OLD MAN AND SEA">

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
	thisModel = 7;
</script>
<link rel="stylesheet" type="text/css"
	href="/resources/default/css/spitslot.css">
</head>

<body>
	<div class="all">
		<jsp:include page="head.jsp"></jsp:include>
		<jsp:include page="leftMenu.jsp"></jsp:include>
		<div class="right_all">
			<c:if test="${!empty sessionScope.user}">
				<div class="publish_spitslot win">
					<div class="win_head">
						<p class="win_head_icon">
						<p>
						<p class="win_head_title">我来吐吐槽</p>
	
						<p class="win_controll_close_btn win_controll_btn">
							<a href="javaScript:void(0)"></a>
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
							<!-- 文本域和发表按钮 -->
							<div class="content_and_btn">
								<div class="spitslot_content h26_input">
									<textarea onkeyup="textLengthLimit(this,'spitslot_text_length_limit',500)"
									onfocus="textLengthLimit(this,'spitslot_text_length_limit',500)"
									onblur="textLengthLimit(this,'spitslot_text_length_limit',500)"
									onkeydown="textLengthLimit(this,'spitslot_text_length_limit',500)"
									id="spitslot_content_textarea"></textarea>
								</div>
								<div class="spitslot_btn">
									<a class="sure" onclick="publishSpitslot()"></a>
								</div>
								<div class="clear"></div>
							</div>
							<p class="text_limit text_limit_ok" id="spitslot_text_length_limit">您可以输入500个字符</p>
							<!-- 上传的图片 -->
							<div class="spitslot_upload_imgs">
								<!-- 表情栏 -->
								<div class="emoji_bar">
									<c:forEach var="n" step="1" begin="101" end="136">
										<p class="float_left"><a style="background: url('/resources/default/img/emoji/${n}.gif');" onclick="addEmojo('${n}','spitslot_content_textarea')"></a></p>
									</c:forEach>	
									<div class="clear"></div>
								</div>
									
								<input type="file" id="upload_img" name="file" style="display: none;" />
								<p class="add_img_bt">
									<a href="javaScript:void(0)" onclick="addNewImg()"></a>
								</p>
								<div class="clear"></div>
							</div>
						</div>
					</div>
	
					<div class="win_bottom">
						<div class="msg_div"></div>
					</div>
				</div>
				<div class="win win_parting_line"></div>
			</c:if>
			
			<!-- 吐槽数据 -->
			<c:if test="${!empty requestScope.spitslotList && requestScope.spitslotList.size()>0}">
				<c:forEach items="#{requestScope.spitslotList}" var="spitslot">
					<div class="spitslot_data win" id="spitslot_div_${spitslot.id}" onclick="initSpitslotReply('${spitslot.id}')">
						<div class="win_head">
							<p class="win_head_icon">
								<img src="/loadImg?imgPath=${spitslot.user.photoUrl}"/>
							<p>
							<p class="win_head_title"><span class="red_and_shadow">${spitslot.user.showName}</span></p>
		
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
								<div class="content_and_imgs content_all" id="spitslot_content_${spitslot.id}">
									<div class="content">
										<p>${spitslot.content}</p>
									</div>
									
									<c:if test="${!empty spitslot.imgList}">
										<div class="data_imgs">
											<c:forEach items="${spitslot.imgList}" var="img">
												<p class="needShadow">
													<img src="/loadImg?imgPath=${img.fileUrl}"/>
												</p>
											</c:forEach>
											<div class="clear"></div>
										</div>
									</c:if>
								</div>
								
								<!-- 评论列表 右浮动 -->
								<div class="comments_and_publish_div" id="comment_list_${spitslot.id}" onmouseover="showZoom(event,this)" onmouseout="hideZoom(event,this)">
									<div class="comments">
										<div class="zoom"><a onclick="changeCommentDivHight(this,'spitslot_div_${spitslot.id}')" class="smaller" title="高一点"></a></div>
										<c:forEach items="${spitslot.comments}" var="comment">
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
															<a class="float_right reply_a" onclick="prePublishComment('${spitslot.id}','${comment.id}',this,'${comment.commentUser.showName}')">
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
																		<a class="float_right reply_a" onclick="prePublishComment('${spitslot.id}','${reply.id}',this,'${reply.replyUser.showName}')">
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
																<a onclick="moreReply('${spitslot.id}',2,'${comment.id}',this,'${comment.replyCount}')">----------[查看更多回复]----------</a>
															</p>
														</c:if>
													</div>
												</c:if>
											</div>
										</c:forEach>
										<c:if test="${spitslot.commentsCount>5}">
											<p class="more_comment_p" id="more_comment_p_${spitslot.id}">
												<a onclick="showMoreComment('${spitslot.id}',2,'${spitslot.commentsCount}')">----------[查看更多评论]----------</a>
											</p>
										</c:if>
									</div>
									
									<div class="publish_comment">
										<p class="float_left input">
											<span id="mark_${spitslot.id}" class="mark"></span>
											<input id="comment_publish_input_${spitslot.id}" onkeydown="addMark(this);textLengthLimit(this,'',500,1)" onkeyup="addMark(this);textLengthLimit(this,'',500,1)" 
												onblur="addMark(this);textLengthLimit(this,'',500,1)" onfocus="createFloatEmojiDiv(this);addMark(this);textLengthLimit(this,'',500,1)"/>
										</p>
										<p class="float_right btn">
											<a onclick="publishComment('${spitslot.id}')" id="comment_publish_btn_${spitslot.id}">发表</a>
										</p>
										<div class="clear"></div>
									</div>
								</div>
								
								<div class="clear"></div>
							</div>
						</div>
		
						<div class="win_bottom">
							<div class="data_btns">
								<c:if test="${!empty sessionScope.user && sessionScope.user.id==spitslot.user.id}">
									<p class="delete"><a onclick="preDeleteSpitslot('${spitslot.id}')">删除</a></p>
								</c:if>
								<p class="praise">
									<a onclick="addPraise('${spitslot.id}')">
										点赞(<span id="data_praise_count_${spitslot.id}">${spitslot.praiseCount}</span>)
									</a>
								</p>
								<p class="comment">
									<a onclick="showOrHideComments('${spitslot.id}')">
										评论(<span id="data_comment_count_${spitslot.id}">${spitslot.commentsCount}</span>)
									</a>
								</p>
							</div>
							<div class="publish_time">${spitslot.addTime}</div>
							<div class="clear"></div>
						</div>
					</div>
				</c:forEach>
				
				<!-- 用于分页加载数据 -->
				<div id="more_spitslot" class="win more_data" onclick="getMore(false)" onmousedown="mouseIsDown(this)" onmouseup="mouseIsUp(this)">
					<span class="text">点击看看有没有了~</span>
					<!-- loading的Gif图片 -->
					<div class="loading"></div>
				</div>
			</c:if>
			<c:if test="${empty requestScope.spitslotList || requestScope.spitslotList.size()<1}">
				<div id="more_spitslot" class="win more_data">
					<span class="text">暂无任何吐槽数据,不如现在就来吐槽吐槽吧~</span>
				</div>
				</c:if>
		</div>
		<div class="clear"></div>
	</div>
</body>
<script type="text/javascript" src="/resources/default/js/spitslot.js"></script>
</html>
