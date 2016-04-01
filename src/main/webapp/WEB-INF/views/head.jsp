<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
	<link rel="stylesheet" type="text/css" href="/resources/default/css/all.css">
	<link rel="stylesheet" href="/resources/kindEditor/themes/default/default.css" />
	<link rel="stylesheet" href="/resources/kindEditor/plugins/code/prettify.css" />
	<link rel="stylesheet" type="text/css" href="/resources/default/css/glDatePicker.darkneon.css">
	<link rel="stylesheet" type="text/css" href="/resources/default/css/glDatePicker.default.css">
	<link rel="stylesheet" type="text/css" href="/resources/default/css/glDatePicker.flatwhite.css">
	
	<script type="text/javascript" src="/resources/js/jquery.js"></script>
	<script type="text/javascript" src="/resources/js/jquery-color.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.iframe-transport.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.fileupload.js"></script>
	<script type="text/javascript" src="/resources/js/jquery-resize.js"></script>
	<script type="text/javascript" src="/resources/js/util.js"></script>
	<script type="text/javascript" src="/resources/js/common.js"></script>
	<script type="text/javascript" src="/resources/default/js/module_data_to_html.js"></script>
	<script type="text/javascript" src="/resources/default/js/home.js"></script>
	<script type="text/javascript" src="/resources/js/calendar.js"></script>
	<script type="text/javascript" src="/resources/js/jump.js"></script>
	
	<!-- 模拟弹窗 -->
	<div class="notice_div"></div>
	<!-- 遮罩层 -->
	<div class="zz_div"></div>
	    <div class="head_all">
	    	<div class="head_photo">
	    		<div class="photo_bg">
	    			<div class="photo_shap_zz"></div>
	    			<c:choose>
	    				<c:when test="${!empty sessionScope.user && !empty sessionScope.user.photoUrl && sessionScope.user.photoUrl !=''}">
	    					<img src="/loadImg?imgPath=${sessionScope.user.photoUrl}" id="user_head_img" />
	    				</c:when>
	    				<c:otherwise>
	    					<img src="/resources/default/img/icon/default_photo.png">
	    				</c:otherwise>
	    			</c:choose>
	    		</div>
    			<div class="nickName">
    			<c:choose>
    				<c:when test="${!empty sessionScope.user}">
    						<script type="text/javascript">getNoticeCount();</script>
    						<a href="javaScript:void(0)">${sessionScope.user.showName}</a>
			    			<!-- 用户菜单 -->
			    			<p class="jiantou"></p>
	    					<div class="userMenu needShadow">
			    				<p class="first_li"><a class="userMenu_1" href="/msg/atMe">提到我的</a></p>
			    				<p><a class="userMenu_2" href="javaScript:void(0)" onclick="showEditPwdWin(2)">更换头像</a></p>
			    				<p><a class="userMenu_3" href="javaScript:void(0)" onclick="showEditPwdWin()">修改密码</a></p>
			    				<p><a class="userMenu_4">个人中心</a></p>
			    				<p><a class="userMenu_5" href="javaScript:void(0)" onclick="logout()">安全退出</a></p>
		    				</div>
	    					<span class="noticeCount">99</span>
    				</c:when>
    				<c:otherwise>
    					<a href="javaScript:void(0)" onclick="showLogin()">登录/注册</a>
    				</c:otherwise>
    			</c:choose>
    			</div>
	    		<div class="menu_button"><a class="hide" onclick="hideMenu()">收起菜单栏</a></div>
	    	</div>
	    	
	    	<!-- banner区 -->
	    	<div class="head_banner" onmouseenter="showBannerBtn(event,this)" onmouseout="hideBannerBtn(event,this)" onclick="createFloatEmojiDiv()">
	    		<div class="head_banner_bg head_banner_bg5"></div>
	    		<div class="head_banner_bg head_banner_bg4"></div>
	    		<div class="head_banner_bg head_banner_bg3"></div>
	    		<div class="head_banner_bg head_banner_bg2"></div>
	    		<div class="head_banner_bg head_banner_bg1"></div>
	    		<div class="banner_title"></div>
	    		<div class="bannerBtn">
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="bannerBtn_effect" id="bannerBtn_7" onclick="setBannerEffect(7)">随机特效</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="bannerBtn_effect" id="bannerBtn_1" onclick="setBannerEffect(1)">渐变特效</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="bannerBtn_effect" id="bannerBtn_2" onclick="setBannerEffect(2)">擦除特效</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="bannerBtn_effect" id="bannerBtn_3" onclick="setBannerEffect(3)">绽放特效</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="bannerBtn_effect" id="bannerBtn_4" onclick="setBannerEffect(4)">缩放特效</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="bannerBtn_effect" id="bannerBtn_5" onclick="setBannerEffect(5)">水平滚动</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="bannerBtn_effect" id="bannerBtn_6" onclick="setBannerEffect(6)">垂直滚动</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="able" id="bannerBtn_8" onclick="stopOrPlayBanner(1)">停止播放</a></p>
	    			<p class="float_left" onmouseover="addShadow(this)" onmouseout="removeShadow(this)"><a class="able" id="bannerBtn_9" onclick="showOrHideBannerWords(1)">显示文字</a></p>
	    			<div class="clear"></div>
	    		</div>
	    	</div>
	    	<div class="clear"></div>
	    </div>
	    <!-- 空白div.当顶部处于固定时,会出现一个空白,由这个div去填充 -->
	    <div class="blank_div"></div>
	    <!-- top按钮 -->
	    <div class="to_top_btn_div">
		    <a href="javaScript:void(0)" title="火箭升空" class="to_top_btn"></a>
	    </div>
	    
	    <!-- 登录浮悬窗 -->
	    <div class="login_div">
	    	<div class="title_div">
	    		<h3>登录</h3>
	    		<p class="close_p">
	    			<a href="javaScript:void(0)" onclick="hideLogin()"></a>
	    		</p>
	    	</div>
	    	<div class="body_div">
	    		<div class="body_div_menu">
	    			<p>
	    				<a class="top_menu selected_menu" onclick="changePannel()">登录账号</a>
	    			</p>
	    			<p>
	    				<a class="not_top_menu" onclick="changePannel(2)">注册账号</a>
	    			</p>
	    			<p>
	    				<a class="not_top_menu" onclick="changePannel(3)">忘记密码</a>
	    			</p>
	    		</div>
	    		<div class="body_div_main">
	    			<div class="login_form">
		    			<div>
		    				<p class="label"><label>账号/邮箱:</label></p>
		    				<div class="input h26_input" id="login_form_account_div" onkeyup="showSelect('login_form_account_div')"
		    					onmouseleave="clearSelect('login_form_account')" onmouseenter="showSelect('login_form_account_div')">
		    					<input type="text" id="login_form_account" />
		    				</div>
		    				<p class="clear_icon"><a></a></p>
		    				<div class="clear"></div>
		    			</div>
		    			<div class="not_top_form_input">
		    				<p class="label"><label>登录密码:</label></p>
		    				<div class="input h26_input"><input type="password" id="login_form_password"/></div>
		    				<p class="clear_icon"><a></a></p>
		    				<div class="clear"></div>
		    			</div>
		    			
		    			<div class="not_top_form_input">
		    				<p class="label"><label>记住账号:</label></p>
		    				<p class="input2">
		    					<input type="radio" name="login_form_isRememberAcount" checked="checked"/>
		    					<label>记住</label>
		    				</p>
		    				<p class="input2">
		    					<input type="radio" name="login_form_isRememberAcount" id="login_form_isRememberAcount"/>
		    					<label>不必</label>
		    				</p>
		    				<div class="clear"></div>
		    			</div>
		    			
		    			<div class="form_button">
		    				<p class="login_form_button_login"><a href="javaScript:void(0)" onclick="checkLoginForm()">登录</a></p>
		    			</div>
	    			</div>
	    			<!-- 注册 -->
	    			<div class="regist_form">
	    				<div>
		    				<p class="label"><label>账号:</label></p>
		    				<div class="input h26_input"><input type="text" id="regist_form_account"/></div>
		    				<p class="clear_icon"><a></a></p>
		    				<div class="clear"></div>
		    			</div>
		    			<div class="not_top_form_input">
		    				<p class="label"><label>邮箱:</label></p>
		    				<div class="input h26_input" id="regist_form_email_div" onkeyup="showSelect('regist_form_email_div')"
		    					onmouseleave="clearSelect('regist_form_email')" onmouseenter="showSelect('regist_form_email_div')">
		    					<input type="text" id="regist_form_email"/>
		    				</div>
		    				<p class="clear_icon"><a></a></p>
		    				<div class="clear"></div>
		    			</div>
		    			<div class="not_top_form_input">
		    				<p class="label"><label>验证码:</label></p>
		    				<p class="action_code_input  h26_input"><input type="text" id="regist_form_action_code"/></p>
		    				<p class="action_code_btn"><a class="able" onclick="getActionCode()">获取验证码</a></p>
		    				<div class="clear"></div>
		    			</div>
		    			<div class="not_top_form_input">
		    				<p class="label"><label>登录密码:</label></p>
		    				<div class="input h26_input"><input type="password" id="regist_form_password"/></div>
		    				<p class="clear_icon"><a></a></p>
		    				<div class="clear"></div>
		    			</div>
		    			<div class="not_top_form_input">
		    				<p class="label"><label>确认密码:</label></p>
		    				<div class="input h26_input"><input type="password" id="regist_form_password_confirm"/></div>
		    				<p class="clear_icon"><a></a></p>
		    				<div class="clear"></div>
		    			</div>
		    			<div class="form_button">
		    				<p class="regist_form_button_regist"><a href="javascript:void(0)" onclick="checkRegistForm()">注册</a></p>
		    			</div>
	    			</div>
	    			<!-- 忘记密码 -->
	    			<div class="forgot_password">
	    				<div>
		    				<p class="label"><label>输入邮箱:</label></p>
		    				<div class="input h26_input" id="forgotPwd_form_email_div" onkeyup="showSelect('forgotPwd_form_email_div')"
		    					onmouseleave="clearSelect('forgotPwd_form_email')" onmouseenter="showSelect('forgotPwd_form_email_div')">
		    					<input type="text" id="forgotPwd_form_email"/>
		    				</div>
		    				<p class="clear_icon"><a></a></p>
		    				<div class="clear"></div>
		    			</div>
	    				<div class="form_button">
		    				<p class="forgot_password_form_button_next"><a href="javaScript:void(0)" onclick="getTempPwd()">下一步</a></p>
		    			</div>
	    			</div>
	    		</div>
	    		<div class="clear"></div>
	    	</div>
	    	<div class="msg_div"></div>
	    </div>				
	    <c:if test="${!empty sessionScope.user}" >
	    	<!-- 如果用户已登录,则有此修改密码,头像的div -->
 				<div class="modify_pwd_photo">
 					<div class="title_div">
 						<H3>修改密码</H3>
 						<p class="close_p">
			    			<a href="javaScript:void(0)" onclick="hideEditPwdWin()"></a>
			    		</p>
 					</div>
 					
 					<div class="body_div">
 						<div class="body_div_menu2">
			    			<p>
			    				<a class="top_menu2 selected_menu2" onclick="changeModifyPannel()">修改密码</a>
			    			</p>
			    			<p>
			    				<a class="not_top_menu2" onclick="changeModifyPannel(2)">更换头像</a>
			    			</p>
			    		</div>
	    		
			    		<div class="body_div_main">
			    		<!-- 修改密码 -->
							<div class="edit_pwd_form">
								<div>
				    				<p class="label"><label>新密码:</label></p>
				    				<div class="input h26_input" id="edit_pwd_form_pwd1_div" >
				    					<input type="password" id="edit_pwd_form_pwd1" />
				    				</div>
				    				<p class="clear_icon"><a></a></p>
				    				<div class="clear"></div>
				    			</div>
				    			<div class="not_top_form_input">
				    				<p class="label"><label>确认密码:</label></p>
				    				<div class="input h26_input"><input type="password" id="edit_pwd_form_pwd2"/></div>
				    				<p class="clear_icon"><a></a></p>
				    				<div class="clear"></div>
				    			</div>
				    			
				    			<div class="not_top_form_input">
				    				<p class="label"><label>验证码:</label></p>
				    				<p class="action_code_input h26_input"><input type="text" id="edit_pwd_form_action_code"/></p>
				    				<p class="action_code_btn"><a class="able" onclick="getEditPwdActionCode()">获取验证码</a></p>
				    				<div class="clear"></div>
				    			</div>
				    			
				    			<div class="form_button">
				    				<p class="edit_pwd_form_save"><a href="javaScript:void(0)" onclick="editPwd()">保存</a></p>
				    			</div>
							</div>
							<!-- 上传头像部分 -->
							<div class="edit_photo_form">
								<input type="file" id="edit_photo_form_file" name="file" hidden="true" />
								<div class="choose_photo_div" onclick="choosePhoto()"></div>
								<!-- 之前保存过的头像(最多4张) -->
								<div class="old_head_imgs"></div>
								
								
								<div class="photo_preview_div">
									<div class="buttons">
										<a class="unable">重新选择</a>
										<a class="able" id="cancel_upload_headImg_btn">取消上传</a>
									</div>
									
									<div class="preview_process">
										<div class="process_zz_div"></div>
										<p class="percent_text"></p>
										<div class="preview_img">
											<p class="upload_img"></p>
											<p class="uploading_bg">
												<c:choose>
								    				<c:when test="${!empty sessionScope.user.photoUrl && sessionScope.user.photoUrl !=''}">
								    					<img src="/loadImg?imgPath=${sessionScope.user.photoUrl}" />
								    				</c:when>
								    				<c:otherwise>
								    					<img src="resources/default/img/icon/default_photo.png">
								    				</c:otherwise>
								    			</c:choose>
											</p>
											<div class="clear"></div>
										</div>
									</div>
									
									<div class="clear"></div>
								</div>
								
								<div class="form_button">
				    				<p class="edit_headImg_form_save"><I class="unable">保存</I></p>
				    			</div>
							</div>
						</div>	
						<div class="clear"></div>
					</div>
				<div class="msg_div"></div>
			</div>
	    </c:if>
	    
	    <!-- 锁 -->
	    <div class="lock_head_location">
	    	<a></a>
	    </div>
		<c:if  test="${!empty requestScope.msg}">
			<script type="text/javascript">
				alertDiv2('${requestScope.msg}');
			</script>
		</c:if>