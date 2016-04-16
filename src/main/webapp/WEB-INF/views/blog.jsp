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

<title>日志博客</title>
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
<link rel="stylesheet" type="text/css" href="/resources/default/css/blog.css">
</head>

<body>
	<div class="all">
		<jsp:include page="head.jsp"></jsp:include>
		<jsp:include page="leftMenu.jsp"></jsp:include>
		<div class="right_all">
		
		
		
		
			<c:if test="${!empty sessionScope.user}">
				<!-- 功能区,搜索区 -->
				<div class="win white_area">
					<center>
						<div id="blog_functions1" class="blog_function_content">
							<div class="float_left blog_function write" onmouseover="addShadow(this);hideBlogTypeSelect()" onmouseout="removeShadow(this)" onclick="openWriteBlogWin()">写新博文</div>
							<div class="float_left blog_function type" onmouseover="addShadow(this);loadBlogType(1,this,event)" onmouseout="removeShadow(this);loadBlogType(0,this,event)">
								<span class="selected_type_name">
									${requestScope.blogTypeName}
								</span>
								
								<div class="search_blog_select_div">
									<p class="option_p" >
										<a href="/blog/list?type=&status=${requestScope.status}&max=${requestScope.max }">全部分类</a>
									</p>
									<p class="option_p" >
										<a href="/blog/list?type=0&status=${requestScope.status}&max=${requestScope.max }">暂未分类</a>
									</p>
									<c:forEach items="${requestScope.blogTypeList}" var="blogType">
										<p class="option_p" >
											<a href="/blog/list?type=${blogType.typeCode}&status=${requestScope.status}&max=${requestScope.max }">${blogType.typeName}</a>
										</p>
									</c:forEach>
								</div>
							</div>
							<div class="float_left blog_function draft" onmouseover="addShadow(this);hideBlogTypeSelect()" onmouseout="removeShadow(this)" onclick="updateBlogList(0)">博文草稿</div>
							<div class="clear"></div>
						</div>
						<div id="blog_functions2" class="blog_function_content">
							<div class="float_left blog_function delete" onmouseover="addShadow(this);hideBlogTypeSelect()" onmouseout="removeShadow(this)" onclick="preDeleteSelectBlog('${requestScope.blogList.size()}')">批量删除</div>
							<div class="float_left blog_function recycle" onmouseover="addShadow(this);hideBlogTypeSelect()" onmouseout="removeShadow(this)" onclick="updateBlogList(-1)">回收站</div>
							<div class="clear"></div>
						</div>
					</center>
				</div>	
				
				<!-- 列表区 -->
				<div class="my_blogs win">
		    		<div class="win_head">
		   				<p class="win_head_icon"><p>
		   				<p class="win_head_title">我的博文</p>
		    			
		   				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)" class="able" onclick="openWriteBlogWin()"></a><p>
		   				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
		   				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
		    			
		    			<div class="clear"></div>
		    		</div>
		    		
		    		<div class="win_body">
		    			<div class="win_body_all">
			    			<c:choose>
					    		<c:when test="${!empty requestScope.blogList}">
							    	<div class="dataInfo_table">
							    		<div class="dataInfo_table_th">
							    			<p class="dataInfo_table_th_a p_checkbox1 col1" onclick="checkedOrNot('p_checkbox1')">
							    				<input type="checkbox" class="all_dataInfo" onclick="checkedOrNot('p_checkbox1')" value="${user.id }"/>
							    			</p>
							    			
							    			<p class="dataInfo_table_th_b col2">标题</p>
							    			<p class="dataInfo_table_th_a col3">所属分类</p>
							    			<p class="dataInfo_table_th_b col4">公开类型</p>
							    			<p class="dataInfo_table_th_a col5">最后修改时间</p>
							    			<p class="dataInfo_table_th_b col6">评论/阅读</p>
							    			<div class="clear"></div>
							    		</div>
							    		<c:forEach items="${requestScope.blogList}" var="blog">
							    			<div class="dataInfo_table_td dataInfo_table_td${blog.id }" onclick="checkedOrNot('dataInfo_table_td${blog.id }')">
							    				<p class="p_checkbox2 col1">
							    					<input type="checkbox" class="one_dataInfo" onclick="checkedOrNot('dataInfo_table_td${blog.id }')" value="${blog.id }"/>
							    				</p>
							    				
							    				<p class="blogTitle col2">
							    					<a href="/blog/blogDetail/${blog.id }" class="link">${blog.title}</a></p>
							    				<p class="col3">${blog.typeName }</p>
							    				<p class="col4">
							    					<c:if test="${blog.publicType==1 }">对外公开</c:if>
							    					<c:if test="${blog.publicType==2 }">对好友公开</c:if>
							    					<c:if test="${blog.publicType==3 }">仅自己可见</c:if>
							    					<c:if test="${blog.publicType==4 }">指定人可见</c:if>
							    				</p>
							    				<p class="col5">${blog.lastEditTime }</p>
							    				<p class="col6">
							    					${blog.commentsCount }/${blog.readNumber }
							    				</p>
							    				<div class="clear"></div>
							    			</div>
							    		</c:forEach>
							    	</div>
							    	<!-- 分页 -->
							    	<div class="paging">
							    		<div class="page_info">
							    			<%-- 设置每页显示的数据 --%>
							    			共<span class="red total_count">${requestScope.total }</span>条数据,每页显示<input type="text" id="limitInput" value="${requestScope.max }"/>条数据
							    			<a class="page_a" onclick="setLimit('${requestScope.type}','${requestScope.status}')" href="javaScript:void(0)">设置</a>
							    			<a class="page_a" onclick="window.location.href=window.location.href;" href="javaScript:void(0)">刷新</a>
							    		</div>
							    		<div class="page">
							    			<c:if test="${requestScope.nowPage>1}">
								    			<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&max=${requestScope.max }" class="page_a">首页</a>
							    				<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&max=${requestScope.max }&pageCode=${requestScope.nowPage-1}" class="page_a">上页</a>
							    			</c:if>
							    			
							    			<!-- ++++++++++++++++++++++页码++++++++++++++++++++++++ -->
							    			<c:choose>
							    				<%-- 不超过11页 --%>
							    				<c:when test="${requestScope.totalPage<=10}">
									    			<c:forEach begin="1" end="${requestScope.totalPage}" var="pageCode">
									    				<c:if test="${pageCode==requestScope.nowPage}">
									    					<span class="nowPage">${pageCode}</span>
									    				</c:if>
									    				<c:if test="${pageCode!=requestScope.nowPage}">
									    					<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&pageCode=${pageCode}&max=${requestScope.max }" class="page_a">${pageCode}</a>
									    				</c:if>
									    			</c:forEach>
							    				</c:when>
							    				<c:otherwise>
									    			<c:choose>
									    				<%-- 临近首页 --%>
									    				<c:when test="${requestScope.nowPage<=5}">
									    					<c:forEach begin="1" end="10" var="pageCode">
									    						<c:if test="${pageCode==requestScope.nowPage}">
									    							<span class="nowPage">${pageCode}</span>
									    						</c:if>
									    						<c:if test="${pageCode!=requestScope.nowPage}">
									    							<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&pageCode=${pageCode}&max=${requestScope.max }" class="page_a">${pageCode}</a>
									    						</c:if>
									    					</c:forEach>
									    				</c:when>
									    				<c:otherwise>
									    					<c:choose>
									    						<%-- 临近尾页 --%>
									    						<c:when test="${requestScope.nowPage>=(requestScope.totalPage-4)}">
									    							<c:forEach begin="${requestScope.totalPage-9 }" end="${requestScope.totalPage }" var="pageCode">
											    						<c:if test="${pageCode==requestScope.nowPage}">
											    							<span class="nowPage">${pageCode}</span>
											    						</c:if>
											    						<c:if test="${pageCode!=requestScope.nowPage}">
											    							<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&pageCode=${pageCode}&max=${requestScope.max }" class="page_a">${pageCode}</a>
											    						</c:if>
											    					</c:forEach>
									    						</c:when>
									    						<%-- 处于中间位置 --%>
									    						<c:otherwise>
									    							<c:forEach begin="${requestScope.nowPage-4 }" end="${requestScope.nowPage+5 }" var="pageCode">
											    						<c:if test="${pageCode==requestScope.nowPage}">
											    							<span class="nowPage">${pageCode}</span>
											    						</c:if>
											    						<c:if test="${pageCode!=requestScope.nowPage}">
											    							<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&pageCode=${pageCode}&max=${requestScope.max }" class="page_a">${pageCode}</a>
											    						</c:if>
											    					</c:forEach>
									    						</c:otherwise>
									    					</c:choose>
									    				</c:otherwise>
									    			</c:choose>
							    				</c:otherwise>
							    			</c:choose>
							    			<!-- ++++++++++++++++++++++页码++++++++++++++++++++++++ -->
							    			
							    			<c:if test="${requestScope.nowPage<requestScope.totalPage}">
							    				<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&pageCode=${requestScope.nowPage+1}&max=${requestScope.max }" class="page_a">下页</a>
								    			<a href="/blog/list?type=${requestScope.type }&status=${requestScope.status}&pageCode=${requestScope.totalPage}&max=${requestScope.max }" class="page_a">尾页</a>
							    			</c:if>
							    			
							    			<%-- 自定义跳转 --%>
							    			<input type="text" id="pageCodeInput" />/${requestScope.totalPage }
							    			<a class="page_a" onclick="gotoPage('${requestScope.type}','${requestScope.status }','${requestScope.max }','${requestScope.totalPage }','${requestScope.nowPage}')" href="javaScript:void(0)">跳转</a>
							    			
							    		</div>
							    		<div class="clear"></div>
							    	</div>
							    </c:when>
							    <c:otherwise>
							    	<div class="no_data">暂无相关数据...</div>
							    </c:otherwise>
					    	</c:choose>
		    			</div>
		    		</div>
		    		<div class="win_bottom"></div>
		    	</div>
				<!-- 写博文 -->
				<div class="publish_blog win">
					<div class="win_head">
						<p class="win_head_icon">
						</p>
						<p class="win_head_title">写博文</p>
	
						<p class="win_controll_close_btn win_controll_btn">
							<a href="javaScript:void(0)" class="able"  onclick="openWriteBlogWin()"></a>
						</p>
						<p class="win_controll_max_btn win_controll_btn">
							<a href="javaScript:void(0)"></a>
						</p>
						<p class="win_controll_min_btn win_controll_btn">
							<a href="javaScript:void(0)" class="able"></a>
						</p>
						<div class="clear"></div>
					</div>
	
					<div class="win_body">
						<div class="win_body_all">
							<div class="editor_div">
								
								<center>
									<p class="write_blog_title">
										<input type="text" onfocus="clearMark(this);textLengthLimit(this,null,30,1)" onblur="addMark(this);textLengthLimit(this,null,30,1)" id="blog_publish_title"
											onkeyup="textLengthLimit(this,null,30,1)" onkeyDown="textLengthLimit(this,null,30,1)"/>
										<span class="mark">请在此处输入标题</span>
									</p>
									
									<p class="select_p">
										<label>选择分类</label>
										<select class="select" id="blog_type_select">
											<option value="0">暂不分类</option>
											<c:forEach items="${requestScope.blogTypeList}" var="blogType">
												<option value="${blogType.typeCode}">${blogType.typeName}</option>
											</c:forEach>
										</select>
										&nbsp;&nbsp;
										<label>公开类型</label>
										<select class="select" id="blog_public_select">
											<option value="1">对外公开</option>
											<option value="2">对好友公开</option>
											<option value="3">仅自己可见</option>
											<option value="4">指定人可见</option>
										</select>
									</p>
									
									<textarea id="editor"></textarea>
									
									<div class="btn_div">
										<p>
											<a id="submit_btn" onclick="publishBlog('${requestScope.type}','${requestScope.status }')">保存并发布</a>
										</p>
										<p>
											<a id="save_btn">保存到草稿</a>
										</p>
										<div class="clear"></div>
									</div>
								</center>
							
							</div>
						</div>
					</div>
	
					<div class="win_bottom">
						<div class="msg_div"></div>
					</div>
				</div>
			</c:if>
			
		</div>
		<div class="clear"></div>
	</div>
</body>
<script type="text/javascript" src="/resources/default/js/blog.js"></script>
</html>
