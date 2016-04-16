var menuIsHidden=false;// 全局变量,用于判断左边的菜单栏是否已经隐藏了
var alertTimeout=null;// 全局变量,用于定时调用收起弹窗函数
var hasConfirm=false;// 全局变量,是否存在待确认弹窗
var init_scrollTop=0;// 全局变量,用于记录滚动条距离顶部的位置
var that_time=null;// 全局变量,从调用和时间相关的函数开始的时间.单位:s
var timeOut=null;// 全局变量,用于获取倒计时的递归调用
var getNoticeCountTimeOut=null;
var showLoginDiv=false;// 全局变量,用于判断登录div是否已经弹出
var showEditPwdDiv=false;// 全局变量,用于判断修改密码的浮悬窗是否弹出
var turnRight=1;// 全局变量,用于决定头像照片上传完后,两张图片的切换方式(1:表示向右边滑动,-1:表示向左滑动)
var headIsLock=null;// 全局变量,是否固定头部
var ip=null;// 全局变量,访问者ip
var loginType=0;// 全局变量, 1:登录 2:注册 3:忘记密码
var editType=0;// 全局变量, 1:修改头像 2:修改密码
var commentId=null;// 用于评论和回复评论
var toUser=null;// 用于回复评论
var dataId0=null;// 被评论数据的Id
var stopBanner=null;//用于控制banner图的轮播
var bannerIndex=2;//用于播放图
var bannerCount=5;//banner图片数量
var bannerIntervalTime=8000;//每张banner图展现的时间
var bannerGradualTime=3000;//banner切换所用的时间
var bannerEffect=null;//banner图片切换特效:1.渐变;2.擦除;3.膨胀;4.缩放;5.水平滚动;6.垂直滚动;7.随机效果
var bannerTimeOut=null;//用于递归调用播放banner函数
var bannerWordBtnIsUnLock=true;//为了避免短时间内,重复点击显示/隐藏文字按钮造成文字半透明的bug,给方法加锁
var moreCommentLock=false;//查看更多评论锁,为true时,函数将被锁上,以防还没加载完数据又再次触发
var moreReplyLock=false;//查看更多回复锁,为true时,函数将被锁上,以防还没加载完数据又再次触发
var adjustMenuHeightLock=false;
var beginLoadHeight=0;//当页面向下滚动到距离目标元素出现xx像素时,开始异步加载数据
var rightOldHeight=0;
var windowOldWidth=0;
var rightHeightListenTimeOut=null;
var rightHeightListenTime=500;
var menuNotChange=true;//当前不在显示或隐藏左边的菜单变化过程中
var editor=null;//富文本编辑器
// --------以下是浮悬窗可拖动的全局变量--------------
var mouseUp = true;// 初始化,鼠标没有点击
var mouseX = "0";// 鼠标横坐标
var mouseY = "0";// 鼠标纵坐标
var winx = "0";// 登录窗距离浏览器左边的距离
var winy = "0";// 登录窗距离浏览器顶部的距离
var difx = mouseX - winx;// 鼠标距离登录浮悬窗左边的距离
var dify = mouseY - winy;// 鼠标距离登录浮悬窗顶部的位置

// 页面开始加载事件
window.onload=function(){
	getIp();
}
function getIp(){
	$.get("/visitStart",function(your_ip){
		ip=your_ip;
	});
}
function getNoticeCount(){
	$.get("/user/getNoticeCount",function(result){
		if(result.isSuccess == null){
			confirmDiv(result.msg,"showLogin");
		}else if(result.isSuccess){
			$("div.nickName .noticeCount").show().html(result.data);
		}else{
			$("div.nickName .noticeCount").hide().html("");
		}
		//console.log(result.msg);
		getNoticeCountTimeOut=window.setTimeout("getNoticeCount()", 3000);
	});
}
// 页面渲染完成时
$(function(){
	// 初始化:调用调整大小的方法动态改变相关元素的宽度
	resetSize();
	stopOrPlayBanner();
	bannerEffect=getCookie('bannerEffect');
	if(bannerEffect==null){
		bannerEffect=7;//默认随机特效
	}
	setBannerEffect(bannerEffect);
	showOrHideBannerWords(null,true);
	// 鼠标滚轮事件------start
	$('html').bind('mousewheel', function(e){
		scrollFun(e);
	});
	// 火狐下的鼠标滚动事件
	$('html').bind('DOMMouseScroll', function(e){
		scrollFun(e);
	});
	// --------------------end
	
	//图片垂直居中处理
	$(".content_and_imgs .data_imgs p img").each(function(){
		var h=$(this).height()/2*(-1);
		$(this).css({'top':h+'px','margin-top':'50%'});
	});
	//避免某些浏览器自动显示大图,加个遮罩
	$(".content_and_imgs .data_imgs p").each(function(){
		$(this).append('<I class="data_imgs_zz"></I>');
	});
	//图片上下抖动特效
	$(".content_and_imgs .data_imgs p").each(function(k,obj){
		new JumpObj(obj,10);
	});
	//点击小图查看大图
	$(".content_and_imgs .data_imgs p").click(function(){
	 	showBigImg(this);
	});
	 
	//创建日历
	if($('.calenda')!=null && $('.calenda').length>0){
		$('.calenda').focus(function() {
			$(this).glDatePicker({
				showAlways : false,
				monthNames: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
				dowNames:['日','一','二','三','四','五','六'],
				format : 'yyyy-MM-dd',
				cssName: 'darkneon'
			});
		});
	}
	
	//开启对右边部分的监听,如果大小发生改变,则调整左边高度-----但是影响性能,造成其它特效异常,暂不使用
//	$(".right_all").resize(function(e){
//		adjustMenuHeight();
//	});
	
	//自己写个监听代替
	rightHeightListenTimeOut=window.setTimeout("rightHeightListen()", 500);
	
	// 鼠标经过用户昵称时,显示菜单
	$(".nickName").hover(
	  function () {
	    $(this).children("p.jiantou,div.userMenu").show();
	    $(this).children(".noticeCount").stop().animate({top:'20',right:'18'},300);
	  },
	  function () {
	    $(this).children("p.jiantou,div.userMenu").hide();
	    $(this).children(".noticeCount").stop().animate({top:'-12',right:'-3'},300);
	  }
	);
	
	// 光标落入输入框中时,改变输入框的样式.失去焦点再次变回来
	$("input,textarea").focus(function(){
	   inputStyleChange(this);
	});
	$("input,textarea").blur(function(){
	   inputStyleChangeBack(this);
	});
	
	// 鼠标经过左边的菜单时,改变背景
	$(".left_menu_all .menu_item").hover(
	  function () {
	    $(this).addClass("menu_hover");
	  },
	  function () {
	    $(this).removeClass("menu_hover");
	  }
	);
	
	// 点击表单中的"X"时,清空该输入框中的数据
	$(".clear_icon a").click(function(){
		$(this).parent().parent().children("div.input").children("input").val("");
	});
	// --------------------------------顶部锁-------------------------------
	$(".lock_head_location").hover(
	  function () {
	    $(this).children("a").stop().animate({opacity:'0.8'},300);
	  },
	  function () {
	    $(this).children("a").stop().animate({opacity:'0'},300);
	  }
	);
	// 点击表单中解锁锁图标,则顶部解除固定;反之,则固定
	$(".lock_head_location a").click(function(){
		// 从cookie获取是否固定头部的参数
		headIsLock=getCookie("headIsLock");
		if(headIsLock==null){
			headIsLock=1;// 默认固定头部
		}
		if(headIsLock==1){
			headIsLock=0;
			$(this).removeClass("unlock").addClass("lock");
		}else{
			headIsLock=1;
			$(this).removeClass("lock").addClass("unlock");
		}
		setCookie("headIsLock",headIsLock);
		resetSize();
	});
	
	// ======================================窗口操作=========================
	$(".right_all").click(function(e){
		// 最大化
		if($(e.target).is(".win_controll_max_btn a")){
			var className=$(e.target).attr("class");
			if(className!=null && (className.indexOf("able")!=-1) && !(className.indexOf("unable")!=-1)){
				$(e.target).parent().parent().children(".win_controll_min_btn").children("a")
					.removeClass("unable").addClass("able");
				var win=$(e.target).parent().parent().parent().children(".win_body");
				win.stop().slideDown(1000);
				$(e.target).removeClass("able").addClass("unable");
			}
		}
		// 最小化
		if($(e.target).is(".win_controll_min_btn a")){
			var className=$(e.target).attr("class");
			if(className!=null && (className.indexOf("able")!=-1) && !(className.indexOf("unable")!=-1)){
				$(e.target).parent().parent().children(".win_controll_max_btn").children("a")
					.removeClass("unable").addClass("able");
				$(e.target).parent().parent().parent().children(".win_body")
				.stop().slideUp(1000);
				$(e.target).removeClass("able").addClass("unable");
			}
		}
		// 窗口关闭
		if($(e.target).is(".win_controll_close_btn a")){
			var className=$(e.target).attr("class");
			if(className!=null && (className.indexOf("able")!=-1) && !(className.indexOf("unable")!=-1)){
				$(e.target).parent().parent().parent().hide();
			}
		}
	});
	
	// 鼠标点击非超链接/按钮事件=============================================================
	$("html").mousedown(function (event) {
		var el_class=$(event.toElement).attr('class');
		// 针对游戏模块----------------------------
		if(typeof(el_class) == "undefined" || (el_class.indexOf("cell")!=-1 || el_class.indexOf("number_p")!=-1 || el_class.indexOf("court")!=-1 || el_class.indexOf("btn")!=-1)){
			// console.log("不能清除");
		}else{
	        // 清除已有的浮悬div
			$(".answer_div").remove();
			$(".answer_div2").remove();
			// 移除特殊class属性
			$(".court .normal").removeClass("click_cell");
			// 所有普通单元格背景色/前景色初始化(除了警告过的)
			$(".court .normal:not(.warn_cell) .number_p").stop().animate({backgroundColor:"#f2f2f2",color:"#9900ff"},1000);
			select_cell=null;
		}
		// 针对浮动表情div-----------------------------------
		var float_emoji_div=$("#float_emoji_div");
		if(typeof(float_emoji_div)!='undefine' && float_emoji_div!=null && float_emoji_div.length>0){
			//如果存在表情div,则判断点击的地方是否在父级元素内,不在,则清除浮动表情div
			var armObj=$(event.target);//被点击的地方
			if(!armObj.isChildAndSelfOf(float_emoji_div.parent())){
				float_emoji_div.remove();
			}
		}
    });
	// ==================================================================================END
	// 初始化:调整菜单项的背景色
	$(".left_menu_all .menu_item"+thisModel).css("background","url(\"/resources/default/img/icon/menu_icon/menu-icon"+thisModel+"2.png\") 15px 50% no-repeat transparent")
		.parent().addClass("menu_this").removeClass("menu_item");

	// 获取浏览器宽高
	var w=$(window).width();
	var h=$(window).height();
	// 返回顶部按钮初始化
	$(".to_top_btn_div").css("top",(h+51)+"px");
	// ----------------------垂直滚动条事件----------------------
	$(document).scroll(function(){
		// 重新获取浏览器宽和文档的高
		w=$(window).width();
		h=$(window).height();
		
		var lowIE=false;
		var DEFAULT_VERSION = "8.0";
		var ua = navigator.userAgent.toLowerCase();
		var isIE = ua.indexOf("msie")>-1;
		var safariVersion;
		if(isIE){
		    safariVersion =  ua.match(/msie ([\d.]+)/)[1];
		}
		if(safariVersion <= DEFAULT_VERSION ){// 低版本的IE
		    lowIE=true;
		}
		
		var windowHeight=window.innerHeight;// 浏览器的高度----IE8及以下不支持
		var fromTop=$(document).scrollTop();// 滚动条距离顶部的即时位置
		
		// 异步加载数据,除了游戏模块,每个模块都有getMore()函数,用于异步加载数据
		if(typeof(thisModel) != "undefined" && thisModel==7){
			getMore();
		}else if(typeof(thisModel) != "undefined" && thisModel==1){
			getMoreHomeData();
		}
		
		// 固定头部按钮随滚动条一起滚动
		if(headIsLock==1){
			$(".lock_head_location a").css("top",(fromTop+205)+"px");
		}else{
			if(fromTop>195){
				$(".lock_head_location a").css("top",(fromTop+7)+"px");
			}else{
				$(".lock_head_location a").css("top","205px");
			}
		}
		
		// 滚动条向下滚动后,左边的菜单也跟着向下滚,从而实现左边的菜单一直是可见的
		if(w<800){
			if(!lowIE && windowHeight>451){// 如果浏览器不够高,则菜单就是固定的,不跟随滚动
				fromTop=fromTop<198?198:fromTop;
				$(".left_menu_all .left_menu").stop().animate({top:(fromTop-198)+'px'},500);
			}else{
				$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
			}
		}else{
			// 从cookie获取是否固定头部的参数
			headIsLock=getCookie("headIsLock");
			if(headIsLock==null){
				headIsLock=1;// 默认固定头部
				setCookie("headIsLock",headIsLock);
			}
			// 如果浏览器不够高,则菜单就是固定的,不跟随滚动
			if(headIsLock==1){
				if(!lowIE && windowHeight>646){
					$(".left_menu_all .left_menu").stop().animate({top:fromTop+'px'},500);
				}else{
					$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
				}
			}else{
				if(fromTop<198){
					$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
				}else{
					if(!lowIE && windowHeight>451){
						$(".left_menu_all .left_menu").stop().animate({top:(fromTop-198)+'px'},500);
					}else{
						$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
					}
				}
			}
			
		}
		// 从cookie获取是否固定头部的参数
		headIsLock=getCookie("headIsLock");
		if(headIsLock==null){
			headIsLock=1;// 默认固定头部
			setCookie("headIsLock",headIsLock);
		}
		// 根据参数,设置头部是固定还是非固定,并设置按钮位置
		if(headIsLock==1){// 固定
			$(".lock_head_location").stop().animate({top:(208+fromTop)+'px'},10);
		}else{// 非固定
			if(fromTop>198){
				$(".lock_head_location").stop().animate({top:(fromTop+10)+'px'},10);
			}else{
				$(".lock_head_location").stop().animate({top:'208px'},10);
			}
		}
		// 判断浏览器大小是否显现top按钮
		if(w>=800 && h>300){// 浏览器太小或高度不够,那就不显示top按钮了
			// 滚动条向下滚动,隐藏top按钮
			if(fromTop>init_scrollTop || fromTop<=50){
				// alert("火箭位置:"+$(".to_top_btn_div").css("top")+"\t"+$(".to_top_btn_div").css("right"));
				// 垂直升起+淡出
				var to_top=$(".to_top_btn_div").stop().css("top");
				if(to_top!=(h+51)+"px"){// 如果已经隐藏了,就不用执行下面的代码了
					$(".to_top_btn_div").stop().animate({top:'0',opacity:'0'},500,"swing",function(){
						// 返回顶部按钮初始化
						$(".to_top_btn_div").css("top",(h+51)+"px");
					});
				}
			}else if(fromTop<init_scrollTop && fromTop>50){// 滚动条向上滚动,显现top按钮
				// 垂直进入+淡入
				$(".to_top_btn_div").stop().animate({opacity:'0.8',top:'80%'},300);
			}
		}
		
		window.setTimeout(function(){init_scrollTop=fromTop}, 0);
	});
	
	// 返回顶部
	$("a.to_top_btn").click(function(){
		 toTop();
	});
	// -----------------------div可拖动的实现-----------------------------
	$("html").mousemove(function (event) {
		if(showLoginDiv){// 只有在登录浮悬窗已经弹出的情况下才能拖动
	        mouseX = event.pageX;// 鼠标距离左边的距离
	        mouseY = event.pageY;// 鼠标距离顶部的位置
	        winx = $(".login_div").offset().left;// 登录浮悬窗距离左边的位置
	        winy = $(".login_div").offset().top;// 登录浮悬窗距离顶部的位置
	        if (mouseUp) {
	            difx = mouseX - winx;
	            dify = mouseY - winy;
	        }else{
	        	var toTop=$(document).scrollTop();// 滚动条距离顶部的即时位置
	        	var toLeft=$(document).scrollLeft();// 滚动条距离左边的即时位置
		        var newx = mouseX - difx - $(".login_div").css("marginLeft").replace('px', '')-toLeft;
		        var newy = mouseY - dify - $(".login_div").css("marginTop").replace('px', '')-toTop;
			    $(".login_div").css({ top: newy,left:newx});
	        }
		}else if(showEditPwdDiv){
			mouseX = event.pageX;// 鼠标距离左边的距离
	        mouseY = event.pageY;// 鼠标距离顶部的位置
	        winx = $(".modify_pwd_photo").offset().left;// 浮悬窗距离左边的位置
	        winy = $(".modify_pwd_photo").offset().top;// 浮悬窗距离顶部的位置
	        if (mouseUp) {
	            difx = mouseX - winx;
	            dify = mouseY - winy;
	        }else{
	        	var toTop=$(document).scrollTop();// 滚动条距离顶部的即时位置
	        	var toLeft=$(document).scrollLeft();// 滚动条距离左边的即时位置
		        var newx = mouseX - difx - $(".modify_pwd_photo").css("marginLeft").replace('px', '')-toLeft;
		        var newy = mouseY - dify - $(".modify_pwd_photo").css("marginTop").replace('px', '')-toTop;
			    $(".modify_pwd_photo").css({ top: newy,left:newx});
	        }
		}
    });

    $(".login_div .title_div,.modify_pwd_photo .title_div").mousedown(function (event) {
        mouseUp = false;
    });

    $("html").mouseup(function (event) {
        mouseUp = true;
    });
    // ------------------------------------------------------------end--------上传头像
	// start--------
    // 校验选中的文件
    $('#edit_photo_form_file').on('change' , function(){
    	var fileUrl=$("#edit_photo_form_file").val();
    	if(fileUrl!=null && fileUrl!=''){
    		var testUrl=/^[a-zA-Z]:(\\.+)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/;
    		if(!testUrl.test(fileUrl)){
    			$("#edit_photo_form_file").val('');
    			$(".modify_pwd_photo .msg_div").html("对不起,您选择的文件格式不对,请重新选择!");
    			return false;
    		}else{// 通过校验
    			$(".modify_pwd_photo .msg_div").html("");
    			// 切换界面
    			$(".edit_photo_form .choose_photo_div").hide();
    			$(".edit_photo_form .old_head_imgs").hide();
    			$(".edit_photo_form .photo_preview_div").show();
    			$(".edit_photo_form div.form_button").show();
    			
    			// 进度条,按钮初始化
    			$(".preview_process .process_zz_div").width('120px');
    			$(".photo_preview_div .buttons").html('<a class="unable">重新选择</a>'
    						+'<a class="able" id="cancel_upload_headImg_btn">取消上传</a>');
    		}
    	}else{
    		return false;
    	}
    }); 
    // 自动上传头像
    var jqXHR=$("#edit_photo_form_file").fileupload({
    	autoUpload: true,  
        url:"/user/uploadPhoto",
        progress: function (e, data) {
        	var total=data._progress.total;
        	var done=data._progress.loaded;
        	
        	var percent=(100*done/total).toFixed(2);// 百分比保留两位小数
        	var w=(100-percent)*1.2;// 总宽120px
        	$(".preview_process .process_zz_div").stop().animate({width:w+'px'},100);
        	$(".preview_process .percent_text").html(percent+"%");
        },
        done:function(e,result){
        	$(".modify_pwd_photo .msg_div").html(result.result.msg);
            $(".photo_preview_div .buttons").html('<a class="able" onclick="choosePhoto()">重新选择</a>'
    			+'<a class="unable">取消上传</a>');
            if(result.result.isSuccess){
            	$(".preview_process .percent_text").html("");
            	if(turnRight>0){
	            	$(".preview_process .preview_img p.upload_img").html('<img src="/loadImg?imgPath='+result.result.data.fileUrl+'">');
	            	$(".preview_process .preview_img").stop().animate({left:'0px'},800);
            	}else{
            		$(".preview_process .preview_img p.uploading_bg").html('<img src="/loadImg?imgPath='+result.result.data.fileUrl+'">');
            		$(".preview_process .preview_img").stop().animate({left:'-120px'},800);
            	}
            	turnRight*=-1;
            	
    			$(".edit_photo_form .form_button .edit_headImg_form_save").html('<a href="javaScript:void(0)" onclick="saveNewPhoto()">保存</a>');
            }else{
            	$(".preview_process .process_zz_div").stop().animate({width:'120px'},100);
            	$(".preview_process .percent_text").html("上传失败!");
            }
        },
        fail: function (e, data) {
        	$(".modify_pwd_photo .msg_div").html("图片上传失败,请重试!");
        	$(".photo_preview_div .buttons").html('<a class="able" onclick="choosePhoto()">重新选择</a>'
    			+'<a class="unable">取消上传</a>');
        }
    });
    // 取消头像上传
    $('#cancel_upload_headImg_btn').click(function (e) {
    	jqXHR.abort();
	});
	
	// 快捷键操作
	$(window).keypress(function(event){
		// 如果弹出了登录窗
		if(event.keyCode==13){// 按了回车键
			if(showLoginDiv && loginType!=0){
				switch(loginType){// 登录
					case 1:
						$(".form_button p.login_form_button_login a").click();
					break;
					case 2:
						$(".form_button p.regist_form_button_regist a").click();
					break;
					case 3:
						$(".form_button p.forgot_password_form_button_next a").click();
					break;
				}
			}else if(showEditPwdDiv && editType!=0){// 修改密码
				switch(editType){
					case 1:
						$(".form_button p.edit_headImg_form_save a").click();
					break;
					case 2:
						$(".form_button p.edit_pwd_form_save a").click();
					break;
				}
			}else if(typeof(thisModel) != "undefined" && thisModel==8){// 游戏模块
				if(!isOver){
					if(isStart){
						stop();
					}else if(time==0){
						play();
					}else{
						go_on();
					}
				}else{
					alertDiv("游戏已经结束,<label>[-]</label>和<label>[enter]</label>快捷键已失效!");
				}
			}
		}
		
		// 如果是游戏模块
		if(typeof(thisModel) != "undefined" && thisModel==8){
			if(!isOver){
				if(event.keyCode==45){
					clearCell();
				}
			}else{
				alertDiv("游戏已经结束,<label>[-]</label>和<label>[enter]</label>快捷键已失效!");
			}
		}
	});
	
	// 加载富文本编辑器
	var text_area=$("#editor");
	if(typeof(text_area) != "undefined" && text_area != null && text_area.length>0){
		text_area.val("正在初始化编辑器,请稍后...");
		var jsFile='<script type="text/javascript" src="/resources/kindEditor/kindeditor.js"></script>';
		jsFile+='<script type="text/javascript" src="/resources/kindEditor/lang/zh_CN.js"></script>';
		jsFile+='<script type="text/javascript" src="/resources/kindEditor/plugins/code/prettify.js"></script>';
		
		$("body").after(jsFile);
		// 如果页面存在文本域,则将普通文本域转化成富文本编辑器
		KindEditor.ready(function(K) {
				editor = K.create('#editor', {
				uploadJson : '/resources/kindEditor/upload/upload.jsp',
				fileManagerJson : '/resources/kindEditor/upload/file_manager.jsp',
				basePath:'/resources/kindEditor/',
				allowFileManager : true,
				afterCreate : function() {
					this.html('');
					text_area.val('');
				}
			});
			prettyPrint();
		});
	}
	
});
// 调整页面元素大小
function resetSize(resizeSecondTime){
	// var i=1;//此变量用于当点用该函数时,会递归调用一次,以便彻底解决元素大小自适应问题
	window.clearTimeout(rightHeightListenTimeOut);
	var lowIE=false;
	var DEFAULT_VERSION = "8.0";
	var ua = navigator.userAgent.toLowerCase();
	var isIE = ua.indexOf("msie")>-1;
	var safariVersion;
	if(isIE){
	    safariVersion =  ua.match(/msie ([\d.]+)/)[1];
	}
	if(safariVersion <= DEFAULT_VERSION ){
	    lowIE=true;
	}
	
	// 获取浏览器宽度
	var w=$(window).width();
	// 滚动条距离顶部的即时位置
	var fromTop=$(document).scrollTop();
	var windowHeight=window.innerHeight;// 浏览器的高度
	
	// 使登录窗和修改密码窗水平居中
	var LoginmarginLeft=(w-450)/2;
	if(showLoginDiv){
		$(".login_div").css("left",LoginmarginLeft+"px");
	}
	if(showEditPwdDiv){
		$(".modify_pwd_photo").css("left",LoginmarginLeft+"px");
	}
	
	if(w>999){
		$(".head_banner .banner_title").removeClass("banner_title_small").addClass("banner_title_big");
	}else{
		$(".head_banner .banner_title").removeClass("banner_title_big").addClass("banner_title_small");
	}
	
	// 浏览器宽度小于800,则按宽度800显示,尽管会出现水平滚动条
	if(w<800){
		$(".head_all").css("position","static");
		$(".blank_div").hide();
		
		if(!lowIE && windowHeight>451){// 如果浏览器不够高,则菜单就是固定的,不跟随滚动
			// 菜单栏
			fromTop=fromTop>198?fromTop:198;
			$(".left_menu_all .left_menu").stop().animate({top:(fromTop-198)+'px'},500);
		}else{
			$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
		}
		// 将top按钮隐藏
		var h=$(window).height();
		// 返回顶部按钮初始化
		$(".to_top_btn_div").css("top",(h+51)+"px");
		// 固定头部按钮隐藏
		$(".lock_head_location").hide();
		w=800;
		$('html').removeClass('no_x_scoll');
	}else{
		$('html').addClass('no_x_scoll');
		// 从cookie获取是否固定头部的参数
		headIsLock=getCookie("headIsLock");
		if(headIsLock==null){
			headIsLock=1;// 默认固定头部
			setCookie("headIsLock",headIsLock);
		}
		// 根据参数,设置头部是固定还是非固定,并设置按钮位置
		if(headIsLock==1){// 固定
			$(".head_all").css("position","fixed");
			$(".blank_div").show();
			
			// 固定头部设置按钮样式为解锁并设置按钮位置
			$(".lock_head_location").stop().animate({top:(208+fromTop)+'px'},10);
			$(".lock_head_location a").removeClass("lock").addClass("unlock").attr("title","点击头部解除固定");
		}else{// 非固定
			$(".head_all").css("position","static");
			$(".blank_div").hide();
			
			if(fromTop>198){
				$(".lock_head_location").stop().animate({top:(fromTop+10)+'px'},10);
			}else{
				$(".lock_head_location").stop().animate({top:'208px'},10);
			}
			$(".lock_head_location a").removeClass("unlock").addClass("lock").attr("title","点击固定头部");
		}
		// 固定头部按钮显现
		$(".lock_head_location").show();
		
		// 菜单栏 为避免点击解锁造成显示问题,加入以下判断
		if(headIsLock != 1){
			if(fromTop<198){
				$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
			}else{
				if(!lowIE && windowHeight>451){
					$(".left_menu_all .left_menu").stop().animate({top:(fromTop-198)+'px'},500);
				}else{
					$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
				}
			}
		}else{
			// 如果浏览器不够高,则菜单就是固定的,不跟随滚动
			if(!lowIE && windowHeight>646){
				$(".left_menu_all .left_menu").stop().animate({top:fromTop+'px'},500);
			}else{
				$(".left_menu_all .left_menu").stop().animate({top:'0'},500);
			}
		}
	}
	
	
	$(".all,.zz_div").width(w+"px");
	
	/* 以下代码使页面头部右边banner部分大小自适应 */
	$(".head_all .head_banner").width((w-150)+"px");
	
	/* 以下代码使内容div自适应浏览器大小 */
	if(menuIsHidden){
		$(".right_all").width(w+"px");
	}else{
		$(".right_all").width((w-150)+"px");
	}
	// 内容窗
	var win_width=0;
	if(w>800){
		win_width=$(".right_all").width()-140;
	}else{
		win_width=510;
	}
	$(".win").css({width:win_width+"px",marginLeft:"70px"});
	$(".ke-container-default,#editor").css({width:(win_width-40)+"px"});
	
	// 获取内容div的高度
	var h0=$(".right_all").height();
	// 获取浏览器高度-banner-marginTop
	var h1=$(window).height()-199;
	h0=h0<(h1<450?450:h1)?(h1<450?450:h1):h0;
	$(".right_all").css("min-height",h0+"px");
	
	/* 以下代码使左边的菜单栏高度自适应(前提是菜单没有被隐藏) */
	adjustMenuHeight();
	
	//高度的改变,可能导致滚动条从无到有,因此再次设置banner的宽度
	w=$(window).width()>800?$(window).width():800;
	$(".head_all .head_banner").width((w-150)+"px");
	
	/* 以下代码初始化遮罩层 */
	w=$(document).width();
	h=$(document).height();
	
	if($(".zz_div").css("top")=="0px"){// 如果遮罩层已经滑下来了,则不用设置top属性了
		$(".zz_div").css({"width":w+"px","height":h+"px"});
	}else{
		$(".zz_div").css({"width":w+"px","height":h+"px","top":-h+"px"});
	}
	
	// 调整评论列表和内容区域大小
	var win_body_all=$(".win_body_all");
	var winbody=null;
	var contentHeight=200;
	var comments=null;
	var content=null;
	for(var i=0;i<win_body_all.length;i++){
		winbody = $(win_body_all[i]);
		comments=winbody.find(".comments_and_publish_div");
		if(typeof(comments) != "undefined" && comments!=null && comments.length>0 && comments.attr('class').indexOf('notChange')==-1){
			content=winbody.find(".content_all");
			if(comments.css('right')=='0px'){
				content.width((win_width-310)+'px');
			}else{
				content.width((win_width-10)+'px');
			}
			contentHeight=content.outerHeight()+20;// marginTop 10 ;marginBottom 10
			var zoom=comments.find(".zoom");
			if(contentHeight>200){
				if(contentHeight>=500 && content.attr('class').indexOf('resized')==-1) zoom.addClass('not_need');
				if(contentHeight<500 && zoom!=null && zoom.length>0) zoom.removeClass('not_need');
			}else{
				contentHeight=200;
				if(zoom!=null && zoom.length>0) zoom.removeClass('not_need');
			}
			comments.height(contentHeight+'px');
			comments.find(".comments").height((contentHeight-47)+'px');
		}else if(typeof(comments) != "undefined" && comments!=null && comments.length>0 && comments.attr('class').indexOf('notChange')!=-1){
			var top=$(document).scrollTop();// 滚动条距离顶部的即时位置
			var wid= document.body.clientWidth;
			var hei=document.body.clientHeight;
			var noticeDiv=$(".notice_detail_float_div");
			var margin_left=0;
			var margin_top=0;
			
			if(wid>=800 && hei>=580){//浏览器够宽,够高
				margin_left=(wid-800)/2;
				margin_top=(hei-580)/2+top;
			}else if(wid<800 && hei>=580){
				margin_left=0;
				margin_top=(hei-580)/2+top;
			}else if(wid<800 && hei<580){
				margin_left=0;
				margin_top=top;
			}else{
				margin_left=(wid-800)/2;
				margin_top=top;
			}
			noticeDiv.css({"top":margin_top+'px',"left":margin_left+'px'});
		}else{
			var dataDetailComments = winbody.find("div.data_detail_comments");
			if(typeof(dataDetailComments) != "undefined" && dataDetailComments!=null && dataDetailComments.length>0 && dataDetailComments.find(".data_detail_comment").length>0){
				dataDetailComments.find(".data_detail_comment").each(function(){
					$(this).find(".data_detail_comment_content").width((win_width-40)+"px");
					$(this).find(".data_detail_comment_reply_content").width((win_width-70)+"px");
				});
				if($(".win_bottom_comment_input")!=null && $(".win_bottom_comment_input").length>0){
					var w123=winbody.width()-48;
					var iw123=w-10;
					$(".win_bottom_comment_input").find("p.input").width(w123+"px");
					$(".win_bottom_comment_input").find("p.input input").width(iw123+"px");
				}
			}
		}
	}
	/* 高度发生变化可能会出现滚动条,从而使浏览器宽度改变,再次调用resetSize()函数 */
	if(resizeSecondTime==null){
		window.setTimeout("resetSize(1)", 1000);
	}
	rightHeightListenTimeOut=window.setTimeout("rightHeightListen()", 500);
}
//调整左边菜单高度
function adjustMenuHeight(){
	if(!menuIsHidden && !adjustMenuHeightLock){
		adjustMenuHeightLock=true;
		// 重新获取获取内容div高度
		var h3=$(".right_all").outerHeight();
		h3=h3>451?h3:451;
		$(".left_menu_all").css("min-height",h3+"px");
		adjustMenuHeightLock=false;
	}
}
//监听右边高度变化
function rightHeightListen(){
	if(menuNotChange && windowOldWidth!=$("html").width()){
		windowOldWidth=$("html").width();
		resetSize(1);
	}else if(rightOldHeight!=$(".right_all").outerHeight()){
		rightOldHeight=$(".right_all").outerHeight();
		adjustMenuHeight();
	}
	rightHeightListenTimeOut=window.setTimeout("rightHeightListen()",rightHeightListenTime);
}
// 当浏览器宽度改变时,再调用调整大小的方法动态改变相关元素的大小
window.onresize=function(){
	resetSize();
	if(typeof(thisModel) != "undefined"){
		if(thisModel==8){
			hideOrShowRuleAndBtn();
		}else if(thisModel==7){
			resetSpitslotSize(0);
		}else if(thisModel==12){
			initChatPage();
		}
	}
}
//播放Banner图
function playBanner(){
	if(stopBanner==1) return false;
	var thisBanner=null;
	var nextBanner=null;
	var bannerIndexTemp=bannerIndex;
	var bannerEffectTemp=1;
	if(bannerEffect<=1) bannerEffectTemp=1;
	else if(bannerEffect<7) bannerEffectTemp=bannerEffect;
	else bannerEffectTemp=getRandom(6);//获取1~6的随机数
	
	if(bannerIndex==1){//从第五张切换到第一张
		thisBanner=$(".head_banner_bg"+bannerCount);
		nextBanner=$(".head_banner_bg1");
	}else{
		thisBanner=$(".head_banner_bg"+(bannerIndex-1));
		nextBanner=$(".head_banner_bg"+bannerIndex);
	}
	
	if(bannerEffectTemp==1){//方案一:渐变效果
		thisBanner.css('z-index','1021');
		nextBanner.css({'width':'100%','height':'195','left':'0','top':'0','z-index':'1021','display':'none'});
		thisBanner.stop().fadeOut(bannerGradualTime);
		nextBanner.stop().fadeIn(bannerGradualTime);
	}else if(bannerEffectTemp==2){//方案二:擦除效果
		thisBanner.css('z-index','1021');
		nextBanner.css({'width':'100%','height':'195','left':'0','top':'0','z-index':'1020','display':'block'});////
		thisBanner.stop().fadeIn(1).animate({width:'0'},bannerGradualTime,"swing",function(){
			if(bannerIndexTemp==1) $(".head_banner_bg1").css('z-index',"1020");
			if(bannerIndex==bannerCount) $(".head_banner_bg1").css({'width':'100%','z-index':'500'});
			thisBanner.css({'width':'100%','height':'195','left':'0','top':'0','z-index':'500'});
		});
	}else if(bannerEffectTemp==3){//方案三:绽放效果
		thisBanner.css('z-index','1020');
		nextBanner.css({'width':'0','height':'0','left':'50%','top':'100%','z-index':'1021','display':'block'});///
		nextBanner.stop().fadeIn(1).animate({width:'100%',height:'195px',left:'0',top:'0'},bannerGradualTime,"swing",function(){
			thisBanner.css('z-index','500');
			nextBanner.css('z-index','1020');
		});
	}else if(bannerEffectTemp==4){//方案四:缩放效果
		thisBanner.css('z-index','1022');
		nextBanner.css({'width':'0','height':'0','left':'50%','top':'50%','z-index':'1023','display':'block'});///
		if(bannerIndex<bannerCount){///
			nextBanner.prev().css({'width':'100%','height':'195px','left':'0','top':'0','z-index':'1021','display':'block'}).fadeIn(1);
		}else{
			$(".head_banner_bg1").css({'width':'100%','height':'195px','left':'0','top':'0','z-index':'1021','display':'block'}).fadeIn(1);
		}
		thisBanner.stop().animate({width:'0',height:'0',left:'50%',top:'50%'},bannerGradualTime,"swing");
		nextBanner.stop().fadeIn(1).animate({width:'100%',height:'195px',left:'0',top:'0'},bannerGradualTime,"swing",function(){
			thisBanner.css({'width':'100%','height':'195','left':'0','top':'0','z-index':'500'})
		});
	}else if(bannerEffectTemp==5){//方案五:水平滚动
		thisBanner.css('z-index','1020');
		nextBanner.css({'width':'100%','height':'195px','left':'-100%','top':'0','z-index':'1020','display':'block'});///
		thisBanner.stop().animate({left:'100%',top:'0'},bannerGradualTime,"swing");
		nextBanner.stop().fadeIn(1).animate({left:'0',top:'0'},bannerGradualTime,"swing",function(){
			thisBanner.css({'width':'100%','height':'195px','left':'0','top':'0','z-index':'500'});
		});
	}else if(bannerEffectTemp==6){//方案六:垂直滚动
		thisBanner.css('z-index','1020');
		nextBanner.css({'width':'100%','height':'195px','left':'0','top':'100%','z-index':'1020','display':'block'});///
		thisBanner.stop().animate({left:'0',top:'-100%'},bannerGradualTime,"swing");
		nextBanner.stop().fadeIn(1).animate({left:'0',top:'0'},bannerGradualTime,"swing",function(){
			thisBanner.css({'width':'100%','height':'195px','left':'0','top':'0','z-index':'500'});
		});
	}
	
	if(bannerIndex<bannerCount){
		bannerIndex++;
	}else{
		bannerIndex=1;
	}
	bannerTimeOut=window.setTimeout("playBanner()", bannerIntervalTime);
}
// 收起左边的菜单栏
function hideMenu(){
	menuNotChange=false;
	$(".left_menu_all").stop().animate({width:'0'},500,"swing",function(){
		$(".left_menu_all").hide();
		// 将右边的内容div宽度变成100%(不知道为什么,如果写width:'100%'或width:'+=150px',就会有问题)
		
		// 获取浏览器宽度
		var w=$(window).width();
		// 浏览器宽度小于800,则按宽度800显示,尽管会出现水平滚动条
		w=w<800?800:w;
		$(".right_all").stop().animate({width:w+'px'},500,"swing",function(){
			// 将收起按钮变成显示按钮
			$(".menu_button").html('<a class="show" onclick="showMenu()">展开菜单栏</a>');
			
			// 把窗口宽度增大
			var win_w=w-140;
			
			$(".win").stop().animate({width:win_w+'px'},500,"swing");
			for(var i=0;i<$(".content_all").length;i++){
				var commentPosition=$($(".content_all")[i]).next().css('right');
				if(commentPosition=='0px'){//表示没有隐藏评论
					$($(".content_all")[i]).stop().animate({width:(win_w-310)+'px'},500);
				}else{
					$($(".content_all")[i]).stop().animate({width:(win_w-10)+'px'},500);
				}
			}
			
			if(typeof(thisModel) != "undefined"){
				if(thisModel==8){
					hideOrShowRuleAndBtn();
				}else if(thisModel==7){
					resetSpitslotSize(1);
				}
			}
			
			//改变富文本编辑器的值和menuNotChange的值
			$(".ke-container-default,#editor").stop().animate({width:(win_w-40)+'px'},500,"swing")
			$("title").show(500,function(){
				menuNotChange=true;
			});
		});
		menuIsHidden=true;
	});
	//resetSize();
}
// 展开左边的菜单栏
function showMenu(){
	menuNotChange=false;
	// 获取banner宽度
	var w1=$(".head_all .head_banner").width();
	// 先把窗口缩小
	var win_w=w1-140;
	if(win_w<670){// 此判断针对于游戏窗口
		$(".game_info .lable").html("");
	}
	
	$(".ke-container-default,#editor").stop().animate({width:(win_w-40)+'px'},500,"swing");
	$(".spitslot_content textarea").stop().animate({width:(win_w-142)+'px'},500,"swing");
	
	var win=$(".win");
	if(win.length>0){
		for(var i=0;i<$(".content_all").length;i++){
			var commentPosition=$($(".content_all")[i]).next().css('right');
			if(commentPosition=='0px'){//表示没有隐藏评论
				$($(".content_all")[i]).stop().animate({width:(win_w-310)+'px'},500);
			}else{
				$($(".content_all")[i]).stop().animate({width:(win_w-10)+'px'},500);
			}
		}
		
		win.stop().animate({width:win_w+'px'},500,"swing",function(){
			$(".right_all").stop().animate({width:w1+'px'},500,"swing",function(){
				// 获取内容div高度
				var h=$(".right_all").height();
				h=h>451?h:451;
				$(".left_menu_all").stop().show().css("height",h+"px").animate({width:'150px'},500,"swing",function(){
					// 将显示按钮变成收起按钮
					$(".menu_button").html('<a class="hide" onclick="hideMenu()">收起菜单栏</a>');
					
					if(typeof(thisModel) != "undefined"){
						if(thisModel==8){
							hideOrShowRuleAndBtn();
						}else if(thisModel==7){
							resetSpitslotSize();
						}
					}
					menuNotChange=true;
				});
				
				menuIsHidden=false;
			});
		});
	}else{
		// 并不是所有的页面都有win 窗口,所以还得在写一遍下面的代码
		$(".right_all").stop().animate({width:w1+'px'},500,"swing",function(){
			// 获取内容div高度
			var h=$(".right_all").height();
			h=h>451?h:451;
			$(".left_menu_all").stop().show().css("min-height",h+"px").animate({width:'150px'},500,"swing",function(){
				// 将显示按钮变成收起按钮
				$(".menu_button").html('<a class="hide" onclick="hideMenu()">收起菜单栏</a>');
				
				if(typeof(thisModel) != "undefined"){
					if(thisModel==8){
						hideOrShowRuleAndBtn();
					}
				}
				menuNotChange=true;
			});
			
			menuIsHidden=false;
		});
	}
	//resetSize();
}
// 弹出登录窗口
function showLogin(){
	// 如果已经存在弹出窗,并且是待确定弹窗,那么将弹窗收起
	if(hasConfirm) slideUpNoticeDiv();
	// 遮罩层出现
	showZZ();
	// 初始化登录窗的位置,使其水平居中,先获取窗口宽度
	var w=$(window).width();
	var marginLeft=(w-450)/2;
	
	changePannel(1);// 默认显示登录
	// 弹出登录窗口
	$(".login_div").stop().css("left",marginLeft+"px").show().animate({top:'200px',opacity:'1'},500,"swing",function(){
		$(".login_div .body_div").stop().animate({height:'300px'},1000,function(){
			// 标记登录窗口已经弹出,可以拖动窗口了
			showLoginDiv=true;
		});
	});
}
// 关闭登录窗口
function hideLogin(){
	// 标记窗口已经关闭,这样就不能拖动窗口了
	showLoginDiv=false;
	// 收起登录窗口
	$(".login_div .msg_div").html("");
	$(".login_div .body_div").stop().animate({height:'0'},1000,"swing",function(){
		$(".login_div").stop().animate({top:'-455px',opacity:'0'},500,"swing",function(){
			$(".login_div").hide();
			// 表单数据清空
			$(".login_div input").val("");
			// 收起遮罩层
			hideZZ();
		});
	});
	loginType=0;
}
// 登录/注册/忘记密码界面的切换
function changePannel(index,notClearMsg){
	if(index!=null && index==2){// 切换到注册
		$(".forgot_password").stop().slideUp(300);
		$(".login_form").stop().slideUp(300);
		$(".regist_form").stop().slideDown(300);
		
		$($(".body_div_menu p a")[1]).addClass("selected_menu");
		$($(".body_div_menu p a")[0]).removeClass("selected_menu");
		$($(".body_div_menu p a")[2]).removeClass("selected_menu");
		$(".login_div .title_div h3").html("注册");
		
		loginType=2;
	}else if(index!=null && index==3){// 切换到忘记密码
		$(".regist_form").stop().slideUp(300);
		$(".login_form").stop().slideUp(300);
		$(".forgot_password").stop().slideDown(300);
		
		$($(".body_div_menu p a")[2]).addClass("selected_menu");
		$($(".body_div_menu p a")[0]).removeClass("selected_menu");
		$($(".body_div_menu p a")[1]).removeClass("selected_menu");
		$(".login_div .title_div h3").html("忘记密码");
		
		loginType=3;
	}else{// 默认,切换到登录
		$(".regist_form").stop().slideUp(300);
		$(".forgot_password").stop().slideUp(300);
		$(".login_form").stop().slideDown(300);
		
		$($(".body_div_menu p a")[0]).addClass("selected_menu");
		$($(".body_div_menu p a")[1]).removeClass("selected_menu");
		$($(".body_div_menu p a")[2]).removeClass("selected_menu");
		$(".login_div .title_div h3").html("登录");
		
		// ---------------------------cookies--------------------------
	    var account=getCookie("account");
	    var password=getCookie("password");
	    if(account != null && account!=""){
	    	$("#login_form_account").val(account);
	    	if(password != null && password!=""){
		    	$("#login_form_password").val(password);
		    }  
	    }
	    
	    loginType=1;
	}
	if(notClearMsg==null || notClearMsg==false) $(".login_div .msg_div").html("");
}
// 登录
function login(account,password,isNotRemember){
	$(".login_form_button_login").html('<I class="unable">登录中...</I>');
	var url="/user/login"
	$.post(url,{
		account:account,
		password:password,
		isNotRemember:isNotRemember
	},function(result){
		$(".login_div .msg_div").html(result.msg);
		if(result.isSuccess){// 登录成功!
			timeOut=window.setTimeout("reload()", 1000);
		}else{
			$(".login_form_button_login").html('<a href="javaScript:void(0)" onclick="checkLoginForm()">登录</a>');
		}
	});
}
// 登录表单验证
function checkLoginForm(){
	var account=$("#login_form_account").val();
	var password=$("#login_form_password").val();
	var isNotRemember=$("#login_form_isRememberAcount")[0].checked;
	if(account==null || account==''){
		$(".login_div .msg_div").html("亲,你还没有输入账号呢!");
		$("#login_form_account").focus();
		return false;
	}
	if(password==null || password==''){
		$(".login_div .msg_div").html("亲,密码也是要输入的哦!");
		$("#login_form_password").focus();
		return false;
	}
	$(".login_div .msg_div").html("正在登录,请稍后...");
	login(account,password,isNotRemember);
}
// 退出登录
function logout(){
	var url="/user/logout";
	$.get(url,function(result){
		alertDiv(result);
		// 显示默认头像
		var html_str1='<div class="photo_shap_zz"></div>';
		html_str1+='<img src="/resources/default/img/icon/default_photo.png">';
		// 显示登录/注册
		var html_str2='<a href="javaScript:void(0)" onclick="showLogin()">登录/注册</a>';
		$(".head_photo .photo_bg").html(html_str1);
		$(".head_photo .nickName").html(html_str2);
		window.clearTimeout(getNoticeCountTimeOut);
	});
}
// 校验注册表单
function checkRegistForm(){
	var account=$("#regist_form_account");
	var email=$("#regist_form_email");
	var pwd1=$("#regist_form_password");
	var pwd2=$("#regist_form_password_confirm");
	var actionCode=$("#regist_form_action_code");
	
	// 校验账号
	var test1=/^[a-zA-Z0-9_]{6,18}$/;
	if(account.val()!=null && account.val()!=""){
		if(test1.test(account.val())){
			$.post("/user/checkAccount",{account:account.val(),isEmail:false},function(result){
				if(result!="1"){
					$(".login_div .msg_div").html(result);
					account.focus();
					return false;
				}else{
					// $(".login_div .msg_div").html("");
				}
			});
		}else{
			$(".login_div .msg_div").html("账号只能是字母,\"_\",数字,长度6-18位哦!");
			account.focus();
			return false;
		}
	}else{
		$(".login_div .msg_div").html("账号必须要的哦!");
		account.focus();
		return false;
	}
	// 校验邮箱
	var test2=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if(email.val()==null || email.val()==""){
		$(".login_div .msg_div").html("邮箱必须要的哦!");
		email.focus();
		return false;
	}else{
		if(email.val().length>50){
			$(".login_div .msg_div").html("抱歉,这个邮箱号太长了,还有其它邮箱吗?");
			email.focus();
			return false;
		}else{
			if(test2.test(email.val())){
				$.post("/user/checkAccount",{account:email.val()},function(result){
					if(result!="1"){
						$(".login_div .msg_div").html(result);
						email.focus();
						window.clearTimeout(timeOut);
						$("p.action_code_btn").html('<a class="able" onclick="getActionCode()">获取验证码</a>');
						return false;
					}else{
						// $(".login_div .msg_div").html("");
					}
				});
			}else{
				$(".login_div .msg_div").html("亲,邮箱格式好像不对呢!");
				email.focus();
				return false;
			}
		}
	}
	// 校验密码
	var test3=/^[a-zA-Z0-9]{6,18}$/;
	if(pwd1.val()!=null && pwd1.val()!=""){
		if(!test3.test(pwd1.val())){
			$(".login_div .msg_div").html("密码只能是字母,数字,长度6-18位哦!");
			pwd1.focus();
			return false;
		}else{
			// $(".login_div .msg_div").html("");
		}
	}else{
		$(".login_div .msg_div").html("密码必须要的哦!");
		pwd1.focus();
		return false;
	}
	// 校验密码2:两次输入的密码必须一致
	if(pwd2.val()!=null && pwd2.val()!=""){
		if(pwd2.val()!=pwd1.val()){
			$(".login_div .msg_div").html("啊哦~两次输入的密码不一致哦!");
			pwd2.val("");
			pwd2.focus();
			return false;
		}
		// $(".login_div .msg_div").html("");
	}else{
		$(".login_div .msg_div").html("亲,先确认密码吧!");
		pwd2.focus();
		return false;
	}
	// 校验验证码
	if(actionCode.val()==null || actionCode.val()==''){
		$(".login_div .msg_div").html("亲,别忘了输验证码哦!");
		actionCode.focus();
		return false;
	}else{
		if(actionCode.val().length != 6){
			$(".login_div .msg_div").html("亲,验证码的长度是6位哦!");
			actionCode.focus();
			return false;
		}
		// $(".login_div .msg_div").html("");
	}
	$(".login_div .msg_div").html("正在注册,请稍后...");
	$(".regist_form_button_regist").html('<I class="unable">注册中...</I>');
	$.post("/user/regist",{
		userName:account.val(),
		password:pwd1.val(),
		email:email.val(),
		actionCode:actionCode.val()
	},function(result){
		$(".regist_form_button_regist").html('<a href="javascript:void(0)" onclick="checkRegistForm()">注册</a>');
		$(".login_div .msg_div").html(result.msg);
		if(result.isSuccess){
			timeOut=window.setTimeout("reload()", 3000);
		}else{
			window.clearTimeout(timeOut);
			$("p.action_code_btn").html('<a class="able" onclick="getActionCode()">获取验证码</a>');
		}
	});
}
// 获取验证码
function getActionCode(){
	var email=$("#regist_form_email");
	var test2=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if(email.val()==null || email.val()==""){
		$(".login_div .msg_div").html("邮箱必须要的哦!");
		email.focus();
		return false;
	}else{
		if(email.val().length>50){
			$(".login_div .msg_div").html("抱歉,这个邮箱号太长了,还有其它邮箱吗?");
			email.focus();
			return false;
		}else{
			if(test2.test(email.val())){
				$.post("/user/checkAccount",{account:email.val()},function(result){
					if(result!="1"){
						$(".login_div .msg_div").html(result);
						email.focus();
						window.clearTimeout(timeOut);
						$("p.action_code_btn").html('<a class="able" onclick="getActionCode()">获取验证码</a>');
						return false;
					}else{
						$("p.action_code_btn").html('<a class="unable" >重新获取<span><span></a>');
				
						$(".login_div .msg_div").html('正在发送验证码到您的邮箱...');
						// 开始计时,60秒后自动回复按钮
						that_time=parseInt((new Date().getTime())/1000);
						changeActionCodeBtn();
						
						// 请求后台,发验证码到邮箱
						$.post("/user/getActionCode",{email:email.val()},function(result){
							if(!result.isSuccess){
								window.clearTimeout(timeOut);
								$("p.action_code_btn").html('<a class="able" onclick="getActionCode()">获取验证码</a>');
							}
							$(".login_div .msg_div").html(result.msg);
						});
					}
				});
			}else{
				$(".login_div .msg_div").html("亲,邮箱格式好像不对呢!");
				email.focus();
				return false;
			}
		}
	}
}
function changeActionCodeBtn(){
	var sec=daojishi();// 倒计时
	if(sec>0){
		timeOut=window.setTimeout("changeActionCodeBtn()", 1000);
		$("p.action_code_btn a span").html(sec+"s");
	}else{
		window.clearTimeout(timeOut);
		$("p.action_code_btn").html('<a class="able" onclick="getActionCode()">获取验证码</a>');
	}
}
// 在忘记密码界面点击下一步,获取临时密码
function getTempPwd(){
	var email=$("#forgotPwd_form_email");
	var test2=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if(email.val()==null || email.val()==""){
		$(".login_div .msg_div").html("邮箱必须要的哦!");
		email.focus();
		return false;
	}else{
		if(email.val().length>50){
			$(".login_div .msg_div").html("抱歉,这个邮箱号太长了,还有其它邮箱吗?");
			email.focus();
			return false;
		}else{
			if(test2.test(email.val())){
				$(".forgot_password_form_button_next").html('<I class="unable">下一步<span></span><I>');
				$(".login_div .msg_div").html("正在发送临时密码的邮件到您的邮箱,请稍后...");
				
				// 开始计时,60秒后自动回复按钮
				that_time=parseInt((new Date().getTime())/1000);
				changeNextBtn();
				$.post("/user/forgotPwd",{email:email.val()},function(result){
					
					$(".login_div .msg_div").html(result.msg);
					if(result.isSuccess){
						// 切换到登录界面
						changePannel(1,true);
					}else{
						window.clearTimeout(timeOut);
						$("p.forgot_password_form_button_next").html('<a href="javaScript:void(0)" onclick="getTempPwd()">下一步</a>');
					}
				});
			}else{
				$(".login_div .msg_div").html("亲,邮箱格式好像不对呢!");
				email.focus();
				return false;
			}
		}
	}
}
// 改变忘记密码下一步的按钮样式
function changeNextBtn(){
	var sec=daojishi();// 倒计时
	if(sec>0){
		timeOut=window.setTimeout("changeNextBtn()", 1000);
		$("p.forgot_password_form_button_next I span").html("&nbsp;&nbsp;"+sec+"s");
	}else{
		window.clearTimeout(timeOut);
		$("p.forgot_password_form_button_next").html('<a href="javaScript:void(0)" onclick="getTempPwd()">下一步</a>');
	}
}
// 弹出修改密码窗口
function showEditPwdWin(index){
	showZZ();
	changeModifyPannel(index);
	var marginLeft=($(window).width()-452)/2;
	$(".modify_pwd_photo").stop().show().animate({left:(marginLeft+50)+'px',opacity:'1'},'normal',"swing",function(){
		$(".modify_pwd_photo").stop().animate({left:'-=50px'},300,"swing",function(){
			$(".modify_pwd_photo .body_div").stop().animate({height:'300px'},1000,"swing",function(){
				$(".modify_pwd_photo").stop(true);
				showEditPwdDiv=true;
			});
		})
	});
}
// 关闭修改密码窗口
function hideEditPwdWin(){
	showEditPwdDiv=false;
	$(".modify_pwd_photo .msg_div").html("");
	$(".modify_pwd_photo .body_div").stop().animate({height:'0'},1000,"swing",function(){
		$(".modify_pwd_photo").stop().animate({left:'-=50px'},300,"swing",function(){
			var w=$(window).width()+452;
			$(".modify_pwd_photo").stop().animate({left:w+'px',opacity:'0'},'normal',"swing",function(){
				hideZZ();
				$(".modify_pwd_photo").stop(true).hide().css({'left':'-452px','top':'200px'});
			});
		});
	});
}
// 切换修改密码/修改头像
function changeModifyPannel(index){
	$(".modify_pwd_photo .msg_div").html("");
	if(index!=null && index==2){// 切换到上传头像界面
		// 异步加载曾今使用过的头像
		$.post("/user/getOldHeadImg",{},function(imgs){
			if(imgs.isSuccess){
				var img_html='';
				for(var i=0;i<imgs.data.length;i++){
					img_html+='<p id="old_head_img'+imgs.data[i].id+'" onmouseenter="addShadow(this)" onmouseout="removeShadow(this)" onclick="chooseOldImg(\''+imgs.data[i].id+'\',\''+imgs.data[i].imgUrl+'\')">' +
								'<a class="img_zz">&nbsp;</a>' +
								'<img src="/loadImg?imgPath='+imgs.data[i].imgUrl+'" />' +
							'</p>';
				}
				img_html+='<div class="clear"></div>';
				
				$(".old_head_imgs").html(img_html);
			}else{
				$(".old_head_imgs").html("");
				$(".modify_pwd_photo .msg_div").html(imgs.msg);
			}
		});
		
		// 初始化
		$(".edit_photo_form .form_button .edit_headImg_form_save").html('<I class="unable">保存</I>');
		$(".edit_photo_form .old_head_imgs").show();
		$(".edit_photo_form div.form_button").hide();
		$(".edit_photo_form .choose_photo_div").show();
    	$(".edit_photo_form .photo_preview_div").hide();
		$(".edit_photo_form .form_button .edit_pwd_form_save").html('<p class="edit_pwd_form_save"><I class="unable">保存</I></p>');
		
		$(".edit_pwd_form").stop().animate({height:'0'},500).hide();
		$(".edit_photo_form").stop().show().animate({height:'185px'},500);
		
		$($(".body_div_menu2 p a")[0]).removeClass("selected_menu2");
		$($(".body_div_menu2 p a")[1]).addClass("selected_menu2");
		$(".modify_pwd_photo .title_div h3").html("更换头像");
		
		editType=1;
	}else{// 默认切换到修改密码界面
		$(".edit_pwd_form").stop().show().animate({height:'185'},500);
		$(".edit_photo_form").stop().animate({height:'0'},500).hide();
		
		$($(".body_div_menu2 p a")[1]).removeClass("selected_menu2");
		$($(".body_div_menu2 p a")[0]).addClass("selected_menu2");
		$(".modify_pwd_photo .title_div h3").html("修改密码");
		
		editType=2;
	}
}
// 修改密码时,获取验证码
function getEditPwdActionCode(){
	$(".edit_pwd_form .action_code_btn").html('<a class="unable" >重新获取<span><span></a>');
	$(".modify_pwd_photo .msg_div").html("正在发送验证码的邮件到您的邮箱,请稍后...");
	
	// 开始计时,60秒后自动回复按钮
	that_time=parseInt((new Date().getTime())/1000);
	changeEditPwdAC_bt();
	$.post("/user/editPwdActionCode",{},function(result){
		$(".modify_pwd_photo .msg_div").html(result.msg);
		if(!result.isSuccess){// 邮件发送失败时,立即恢复获取验证码的按钮
			window.clearTimeout(timeOut);
			$(".edit_pwd_form .action_code_btn").html('<a class="able" onclick="getEditPwdActionCode()">获取验证码</a>');
		}
	})
}
// 改变修改密码的获取验证码按钮样式
function changeEditPwdAC_bt(){
	var sec=daojishi();// 倒计时
	if(sec>0){
		timeOut=window.setTimeout("changeEditPwdAC_bt()", 1000);
		$(".edit_pwd_form .action_code_btn a span").html(sec+"s");
	}else{
		window.clearTimeout(timeOut);
		$(".edit_pwd_form .action_code_btn").html('<a class="able" onclick="getEditPwdActionCode()">获取验证码</a>');
	}
}
// 修改密码
function editPwd(){
	var pwd1=$("#edit_pwd_form_pwd1");
	var pwd2=$("#edit_pwd_form_pwd2");
	var actionCode=$("#edit_pwd_form_action_code");
	
	// 校验密码
	var test3=/^[a-zA-Z0-9]{6,18}$/;
	if(pwd1.val()!=null && pwd1.val()!=""){
		if(!test3.test(pwd1.val())){
			$(".modify_pwd_photo .msg_div").html("密码只能是字母,数字,长度6-18位哦!");
			pwd1.focus();
			return false;
		}else{
			// $(".modify_pwd_photo .msg_div").html("");
		}
	}else{
		$(".modify_pwd_photo .msg_div").html("密码必须要的哦!");
		pwd1.focus();
		return false;
	}
	// 校验密码2:两次输入的密码必须一致
	if(pwd2.val()!=null && pwd2.val()!=""){
		if(pwd2.val()!=pwd1.val()){
			$(".modify_pwd_photo .msg_div").html("啊哦~两次输入的密码不一致哦!");
			pwd2.val("");
			pwd2.focus();
			return false;
		}
		// $(".login_div .msg_div").html("");
	}else{
		$(".modify_pwd_photo .msg_div").html("亲,先确认密码吧!");
		pwd2.focus();
		return false;
	}
	// 校验验证码
	if(actionCode.val()==null || actionCode.val()==''){
		$(".modify_pwd_photo .msg_div").html("亲,别忘了输验证码哦!");
		actionCode.focus();
		return false;
	}else{
		if(actionCode.val().length != 6){
			$(".modify_pwd_photo .msg_div").html("亲,验证码的长度是6位哦!");
			actionCode.focus();
			return false;
		}
		// $(".login_div .msg_div").html("");
	}
	
	// 提交表单
	$(".modify_pwd_photo .msg_div").html("正在保存新密码...");
	$(".form_button .edit_pwd_form_save").html('<I class="unable">正在保存...</I>');
	$.post("/user/editPwd",{
		password:pwd1.val(),
		actionCode:actionCode.val()
	},function(result){
		$(".modify_pwd_photo .msg_div").html(result.msg);
		if(result.isSuccess){
			$(".form_button .edit_pwd_form_save").html('<I class="unable">保存成功</I>');
			logout();
			window.setTimeout("hideEditPwdWin()", 1000);
		}else{
			$(".form_button .edit_pwd_form_save").html('<a href="javaScript:void(0)" onclick="editPwd()">保存</a>');
		}
	});
}
// 选取图片
function choosePhoto(){
	$("#edit_photo_form_file").click();
}
// 保存头像
function saveNewPhoto(){
	$(".modify_pwd_photo .msg_div").html("正在保存,请稍后...");
	$(".edit_photo_form .form_button .edit_pwd_form_save").html('<p class="edit_pwd_form_save"><I class="unable">保存中...</I></p>');
	$.post("/user/savePhoto",{},function(result){
		$(".modify_pwd_photo .msg_div").html(result.msg);
		if(result.isSuccess){
			$(".edit_photo_form .form_button .edit_pwd_form_save").html('<p class="edit_pwd_form_save"><I class="unable">保存成功</I></p>');
			$("img#user_head_img").attr("src",'/loadImg?imgPath='+result.data.photoUrl);
			window.setTimeout("hideEditPwdWin()", 1000);
		}else{// 保存失败时,回复保存按钮,使其可点击
			$(".edit_photo_form .form_button .edit_pwd_form_save").html('<a href="javaScript:void(0)" onclick="saveNewPhoto()">保存</a>');
		}
	});
}
// 定时器:定时刷存在
function task(sec){
	getIp();
	window.setTimeout("task("+sec+")", sec);
}
// 点击旧头像,则更换头像
function chooseOldImg(id,imgUrl){
	$(".modify_pwd_photo .msg_div").html("正在保存,请稍后...");
	$.post("/user/savePhoto",{imgId:id,imgUrl:imgUrl},function(result){
		$(".modify_pwd_photo .msg_div").html(result.msg);
		if(result.isSuccess){
			$(".edit_photo_form .form_button .edit_pwd_form_save").html('<p class="edit_pwd_form_save"><I class="unable">保存成功</I></p>');
			$("img#user_head_img").attr("src",'/loadImg?imgPath='+result.data.photoUrl);
			window.setTimeout("hideEditPwdWin()", 1000);
		}
	});
}
// 预评论,用于评论的一级回复和二级回复
function prePublishComment(dataId,id,obj,userName){
	$("a.reply_a span").css({"color":"#0080ff","text-shadow":"none"});
	var b=($(obj).attr('class').indexOf('reply_selected')==-1);
	$('.reply_a').removeClass('reply_selected');
	initSpitslotReply(dataId);
	if(b){
		$(obj).addClass('reply_selected');
		$(obj).children("span").css({'color':'#ff0000','text-shadow':'0px 0px 3px #ff0000'});
		commentId=id;
		dataId0=dataId;
		toUser=$(obj);
		$("#comment_publish_input_"+dataId).focus();
		$("#mark_"+dataId).html('回复 '+userName).show();
	}else{
		toUser=null;
		commentId=null;
		dataId0=null;
		$("#mark_"+dataId).html('');
		$(obj).children("span").css({"color":"#0080ff","text-shadow":"none"});
	}
}
// 发表评论
function publishComment(dataId,model,isDetailPage){
	var url=null;
	var content=$('#comment_publish_input_'+dataId).val();
	if(content==null || content==''){
		alertDiv('请先输入内容!');
		$("#comment_publish_input_"+dataId).focus();
	}else{
		if(model!=null) thisModel=model;
		if(typeof(thisModel) != "undefined"){
			switch(thisModel){
				case 1:
					
					break;
				case 2:
				
					break;
				case 3:
					
					break;
				case 4:
				
					break;
				case 5:
					
					break;
				case 6:
				
					break;
				case 7:
					url = "/spitslot/publishComment";
					break;
				case 8:
				
					break;
				case 9:
					url = "/blog/publishComment";
					break;
			}
			// 比较前后dataId,如果不同,则置空commentId
			if(dataId!=dataId0){
				commentId=null;
				$("#mark_"+dataId0).html('');
				dataId0=null;
				toUser=null;
			}
			content=converEmoji(content);
			$.post(url,{dataId:dataId,pid:commentId,content:content},function(result){
				alertDiv(result.msg);
				if(result.isSuccess){
					try{
						if (typeof(eval('showOrHideBlogComments')) == "function") {
							showOrHideBlogComments(0,0,1);
				        }
					}catch(e) {
						console.log("No such method !");
					}finally{
						// 动态写入数据到评论列表
						if(isDetailPage!=null && isDetailPage==1){
							showDataDetailNewComment(result);
							return;
						}
						if(toUser==null){
							var html='<div class="comment">'+
										'<div class="comment_content">'+
											'<p class="float_left">'+
												'<img src="/loadImg?imgPath='+result.data.replyUser.photoUrl+'" class="user_head_img"/>'+
											'</p>'+
											'<div class="float_left comment_reply_content">'+
												'<p><a class="user_name">'+result.data.replyUser.showName+'</a></p>'+
												'<p class="reply_text">'+content+'</p>'+
												'<p class="time_reply_p">'+
													'<span class="float_left italic grey">刚刚</span>'+
													'<a class="float_right reply_a" onclick="prePublishComment(\''+result.data.dataId+'\',\''+result.data.id+'\',this,\''+result.data.replyUser.showName+'\')">' +
														'回复(<span>0</span>)</a>'+
													'<label class="clear"></label>'+
												'</p>'+
											'</div>'+
											'<div class="clear"></div>'+
										'</div>';
							$("#comment_list_"+dataId+" .comments").append(html);
							
							var commentCount=$("#data_comment_count_"+dataId);						
							var count=parseInt(commentCount.html());
							commentCount.html('+1').css({"color":"red","fontWeight":"bold"});
							// 颜色渐变
							commentCount.stop().animate({color:'#ffffff'},3000,'swing',function(){
								commentCount.html(count+1).css("fontWeight","normal");
							});
						}else{
							var replys=toUser.parent().parent().parent();
							var toUserName=$(replys.children(".comment_reply_content").children("p")[0]).children("a.user_name").html();
							var replyText=$(replys.children(".comment_reply_content").children("p")[0]).children("span.grey");
							var isSecond=(replyText==null || replyText.length==0)?false:true;// 是不是二级回复
							var html=null;
							if(replys.attr('class').indexOf('comment_replys')==-1 && !isSecond){
								html='<div class="comment_replys">';
							}else{
								html='';
							}
							html+='<div class="reply">'+
										'<p class="float_left">'+
											'<img src="/loadImg?imgPath='+result.data.replyUser.photoUrl+'" class="user_head_img"/>'+
										'</p>'+
										
										'<div class="float_left comment_reply_content">'+
											'<p>'+
												'<a class="user_name">'+result.data.replyUser.showName+'</a>'+
												'<span class="grey"> 回复  '+toUserName+':</span>'+
											'</p>'+
											'<p class="reply_text">'+
												content+
											'</p>'+
											'<p class="time_reply_p">'+
												'<span class="float_left italic grey">刚刚</span>'+
												'<a class="float_right reply_a" onclick="prePublishComment(\''+result.data.dataId+'\',\''+result.data.id+'\',this,\''+result.data.replyUser.showName+'\')">回复</a>'+
												'<label class="clear"></label>'+
											'</p>'+
										'</div>'+
										
										'<div class="clear"></div>'+
									'</div>';
							if(replys.attr('class').indexOf('comment_replys')==-1 && !isSecond){
								html+='</div>';
							}
							replys.append(html);
							var toUserParent=toUser;
							if(isSecond){
								toUserParent=replys.parent('div.comment_replys').prev().children('div.comment_reply_content')
														.children('p.time_reply_p').children('a.reply_a');
							}
							var count=toUserParent.children("span").html();
							var replycount_span=toUserParent.children("span");
							replycount_span.html('+1').css({"color":"red","fontWeight":"bold","text-shadow":"none"});
							// 颜色渐变
							replycount_span.stop().animate({color:'#0080ff'},3000,'swing',function(){
								replycount_span.html((parseInt(count)+1)).css("fontWeight","normal");
								replycount_span=null;
							});
						}
						initSpitslotReply('0');
						$("#comment_publish_input_"+dataId).val('');
					}
				}
				commentId=null;
				toUser=null;
				dataId0=null;
			});
		}
	}
}
// 展开或隐藏评论列表
function showOrHideComments(id,isStatic){
	var win_width=0;
	if(isStatic!=null && isStatic==1){
		win_width=800;
	}else{
		var w=$(window).width();
		if(w>800){
			win_width=$(".right_all").width()-140;
		}else{
			win_width=510;
		}
	}
	if($('#comment_list_'+id).attr('class').indexOf("spitslot_hide")==-1){
		// 隐藏
		$('#comment_list_'+id).stop().animate({right:'15px'},350,'swing',function(){
			$('#comment_list_'+id).stop().animate({right:'-305px'},250,'swing',function(){
				// $('#comment_list_'+id).stop().animate({width:'0px'},250);
				$("#spitslot_content_"+id).stop().animate({width:(win_width-10)+'px'},250);
			});
		});
		$('#comment_list_'+id).addClass('spitslot_hide');
	}else{
		// 显现
		$('#comment_list_'+id).removeClass('spitslot_hide');
		$("#spitslot_content_"+id).stop().animate({width:(win_width-310)+'px'},250,'swing',function(){
			$('#comment_list_'+id).stop().animate({right:'15px'},250,'swing',function(){
				$('#comment_list_'+id).stop().animate({right:'0px'},350);
			});
		});
	}
	// resetSize();
}
// 如果点击了其它的数据窗口,那么清除之前预评论的数据
function initSpitslotReply(dataId){
	if(dataId0!=null && dataId!=dataId0){
		if(toUser!=null){
			toUser.children("span").css({"color":"#0080ff","fontWeight":"normal","text-shadow":"none"});
		}
		$("#mark_"+dataId0).html('');
		$("#comment_list_"+dataId0+" .reply_a").removeClass('reply_selected');
		commentId=null;
		dataId0=null;
		toUser=null;
	}
}
// 点赞
function addPraise(dataId,model){
	var url=null;
	if(typeof(thisModel) != "undefined"){
		if(model!=null) thisModel=model;
		switch(thisModel){
			case 1:
				
				break;
			case 2:
			
				break;
			case 3:
				
				break;
			case 4:
			
				break;
			case 5:
				
				break;
			case 6:
			
				break;
			case 7:
				url = "/spitslot/praise";
				break;
			case 8:
				url = "/?/praise";
				break;
			case 9:
				url = "/blog/praise";
				break;
		}
		if(dataId==null || dataId==''){
			alertDiv2("系统错误,该数据不存在!");
			return false;
		}
		
		$.post(url,{dataId:dataId},function(result){
			alertDiv(result.msg);
			if(result.isSuccess){
				var praiseCount=$("#data_praise_count_"+dataId);
				var count=parseInt(praiseCount.html());
				praiseCount.html('+1').css({"color":"red","fontWeight":"bold"});
				// 颜色渐变
				praiseCount.stop().animate({color:'#ffffff'},3000,'swing',function(){
					praiseCount.html(count+1).css("fontWeight","normal");
				});
			}
		});
	}
}
//显示/隐藏"welcome to old man and sea"
function showOrHideBannerWords(v,isInit){
	$("#bannerBtn_9").html('waiting...');
	if(bannerWordBtnIsUnLock){
		bannerWordBtnIsUnLock=false;
		f=getCookie('showOrHideBannerWords');
		if(f==null){
			f=0;//默认播放banner
		}
		if(v!=null){//表示页面点击按钮触发,而不是页面初始化的时候触发
			if(f==0){
				f=1;
			}
			else{
				f=0;
			}
		}
		var words=$(".head_banner .banner_title");
		if(f==0){
			if(isInit!=null && isInit){
				words.stop().fadeOut(1);
				$("#bannerBtn_9").html('显示文字');
				bannerWordBtnIsUnLock=true;
			}else{
				words.stop().fadeOut(1000,function(){
					$("#bannerBtn_9").html('显示文字');
					bannerWordBtnIsUnLock=true;
				});
			}
		}
		else{
			words.stop().fadeIn(1000,function(){
				$("#bannerBtn_9").html('隐藏文字');
				bannerWordBtnIsUnLock=true;
			});
		}
		setCookie('showOrHideBannerWords',f);
	}
}
//设置banner切换特效
function setBannerEffect(ef){
	if(ef!=null && ef>0 && ef<8) bannerEffect=ef;
	else bannerEffect=7;
	$(".bannerBtn_effect").removeClass('selected').addClass('able');
	$("#bannerBtn_"+bannerEffect).addClass('selected').removeClass('able');
	setCookie('bannerEffect',bannerEffect);
}
//定格/播放banner 
function stopOrPlayBanner(v){
	stopBanner=getCookie('stopBanner');
	if(stopBanner==null){
		stopBanner=0;//默认播放banner
	}
	if(v!=null){//表示页面点击按钮触发,而不是页面初始化的时候触发
		if(stopBanner==0){
			stopBanner=1;
		}
		else{
			stopBanner=0;
		}
	}
	if(stopBanner==0){
		if(bannerTimeOut!=null) window.clearTimeout(bannerTimeOut);//防止双重特效的产生
		if(v!=null) bannerTimeOut=window.setTimeout("playBanner()", bannerGradualTime);//轮播banner图
		else bannerTimeOut=window.setTimeout("playBanner()", bannerIntervalTime);//轮播banner图
		$("#bannerBtn_8").html('停止播放');
	}
	else{
		window.clearTimeout(bannerTimeOut);
		$("#bannerBtn_8").html('继续播放');
	}
	setCookie('stopBanner',stopBanner);
}
//鼠标移入banner区,显示控制按钮
function showBannerBtn(e,obj){
	if(isMouseLeaveOrEnter(e,obj)){
		$('.bannerBtn').fadeIn(300);
	}
}
//鼠标移开banner区,按钮隐藏
function hideBannerBtn(e,obj){
	if(isMouseLeaveOrEnter(e,obj)){
		$('.bannerBtn').fadeOut(300);
	}
}
//查看更多评论
function showMoreComment(dataId,pageNum,commentCount){
	if(!moreCommentLock){
		moreCommentLock=true;
		$("#more_comment_p_"+dataId).html('正在加载中...');
		var url=null;
		if(typeof(thisModel) != "undefined"){
			switch(thisModel){
				case 1:
					
					break;
				case 2:
				
					break;
				case 3:
					
					break;
				case 4:
				
					break;
				case 5:
					
					break;
				case 6:
				
					break;
				case 7:
					url = "/spitslot/moreComment";
					break;
			}
			$.post(url,{dataId:dataId,page:pageNum},function(result){
				alertDiv(result.msg);
				if(result.isSuccess){
					var html=null;
					for(var i=0;i<result.data.length;i++){
						var comment=result.data[i];
						html='<div class="comment">'+
								'<div class="comment_content">'+
									'<p class="float_left">'+
										'<img src="/loadImg?imgPath='+comment.commentUser.photoUrl+'" class="user_head_img"/>'+
									'</p>'+
									'<div class="float_left comment_reply_content ">'+
										'<p><a class="user_name">'+comment.commentUser.showName+'</a></p>'+
										'<p class="reply_text">'+comment.content+'</p>'+
										'<p class="time_reply_p">'+
											'<span class="float_left italic grey">'+comment.formateTime+'</span>'+
											'<a class="float_right reply_a" onclick="prePublishComment(\''+dataId+'\',\''+comment.id+'\',this,\''+comment.commentUser.showName+'\')">' +
												'回复(<span>'+comment.replyCount+'</span>)' +
											'</a>'+
											'<label class="clear"></label>'+
										'</p>'+
									'</div>'+
									'<div class="clear"></div>'+
								'</div>';
								if(comment.replys!=null && comment.replys.length>0){
									html+='<div class="comment_replys">';
										for(var j=0;j<comment.replys.length;j++){
											var reply=comment.replys[j];
											html+='<div class="reply">'+
												'<p class="float_left">'+
													'<img src="/loadImg?imgPath='+reply.replyUser.photoUrl+'" class="user_head_img"/>'+
												'</p>'+
												
												'<div class="float_left comment_reply_content">'+
													'<p>'+
														'<a class="user_name">'+reply.replyUser.showName+'</a>'+
														'<span class="grey">回复  '+reply.toUser.showName+':</span>'+
													'</p>'+
													'<p class="reply_text">'+
														+reply.content+
													'</p>'+
													'<p class="time_reply_p">'+
														'<span class="float_left italic grey">'+reply.formateTime+'</span>'+
														'<a class="float_right reply_a" onclick="prePublishComment(\''+dataId+'\',\''+reply.id+'\',this,\''+reply.replyUser.showName+'\')">回复</a>'+
														'<label class="clear"></label>'+
													'</p>'+
												'</div>'+
												'<div class="clear"></div>'+
											'</div>';
										}
										if(comment.replyCount>5){
											html+='<p class="more_reply_p">'+
												'<a onclick="moreReply(\''+dataId+'\',2,\''+comment.id+'\',this,\''+comment.replyCount+'\')">----------[查看更多回复]----------</a>'+
											'</p>';
										}
									html+='</div>';
								}
							html+='</div>';
						$("#more_comment_p_"+dataId).before(html);
						html=null;
					}
					
					if(parseInt(commentCount)>(pageNum*10-5)){
						$("#more_comment_p_"+dataId).html('<a onclick="showMoreComment(\''+dataId+'\','+(++pageNum)+','+commentCount+')">----------[查看更多评论]----------</a>');
					}else{
						$("#more_comment_p_"+dataId).remove();					
					}
				}else{
					$("#more_comment_p_"+dataId).remove();
				}
				moreCommentLock=false;
			});
		}else{
			moreCommentLock=false;
		}	
	}
}
//查看更多回复
function moreReply(dataId,pageNum,oid,obj,replyCount){
	if(!moreReplyLock){
		moreReplyLock=true;
		var moreReply_p=$(obj).parent();
		moreReply_p.html('正在加载中...');
		var url=null;
		if(typeof(thisModel) != "undefined"){
			switch(thisModel){
				case 1:
					
					break;
				case 2:
				
					break;
				case 3:
					
					break;
				case 4:
				
					break;
				case 5:
					
					break;
				case 6:
				
					break;
				case 7:
					url = "/spitslot/moreReply";
					break;
			}
			$.post(url,{dataId:dataId,oid:oid,page:pageNum},function(result){
				alertDiv(result.msg);
				if(result.isSuccess){
					var html=null;
					for(var i=0;i<result.data.length;i++){
						var reply=result.data[i];
						html='<div class="reply">'+
							'<p class="float_left">'+
								'<img src="/loadImg?imgPath='+reply.replyUser.photoUrl+'" class="user_head_img"/>'+
							'</p>'+
							
							'<div class="float_left comment_reply_content">'+
								'<p>'+
									'<a class="user_name">'+reply.replyUser.showName+'</a>'+
									'<span class="grey">回复  '+reply.toUser.showName+':</span>'+
								'</p>'+
								'<p class="reply_text">'+
									+reply.content+
								'</p>'+
								'<p class="time_reply_p">'+
									'<span class="float_left italic grey">'+reply.formateTime+'</span>'+
									'<a class="float_right reply_a" onclick="prePublishComment(\''+dataId+'\',\''+reply.id+'\',this,\''+reply.replyUser.showName+'\')">回复</a>'+
									'<label class="clear"></label>'+
								'</p>'+
							'</div>'+
							'<div class="clear"></div>'+
						'</div>';
						moreReply_p.before(html);
						html=null;
					}
					//计算还有没有
					if(parseInt(replyCount)>(pageNum*10-5)){
						moreReply_p.html('<a onclick="showMoreComment(\''+dataId+'\','+(++pageNum)+',\''+oid+'\',this,'+replyCount+')">----------[查看更多回复]----------</a>');
					}else{
						moreReply_p.remove();					
					}
				}else{
					moreReply_p.remove();
				}
				moreReplyLock=false;
			});
		}else{
			moreReplyLock=false;
		}
	}
}
//显示缩放控件
function showZoom(e,obj){
	var zoom=$(obj).find(".zoom");
	if(zoom!=null && zoom.length>0 && (zoom.attr('class').indexOf('not_need')==-1) && isMouseLeaveOrEnter(e,obj)) zoom.stop().fadeTo(200,1);
}
//隐藏缩放控件
function hideZoom(e,obj){
	if(isMouseLeaveOrEnter(e,obj)) $(obj).find(".zoom").stop().fadeOut(200);
}
//改变评论区高度
function changeCommentDivHight(obj,dataId){
	var w_old=$(window).width();
	var comments= $(obj).parent().parent('.comments');
	var content_all=$("#"+dataId).find("div.win_body_all").find(".content_all");
	if($(obj).attr('class').indexOf('higher')!=-1){//缩短
		//alertDiv2("正在调整页面...");
		var h1=content_all.find("div.content").outerHeight();
		var h2=content_all.find("div.spitslot_imgs").outerHeight();
		var h3=(h1+h2)<180?180:(h1+h2);
		comments.stop().animate({height:(h3-27)+'px'},1000,'swing');
		comments.parent("div.comments_and_publish_div").stop().animate({height:(h3+20)+'px'},1000,'swing',function(){
			content_all.stop().animate({height:h3+'px'},1000,'swing',function(){
				$(obj).removeClass('higher').addClass('smaller').attr('title','高一点');
				var w_new=$(window).width();
				if(w_old!=w_new){
					resetSize();//如果出现了滚动条或滚动条从有到无,那么重新设置元素宽高 
				}
			});
		});
	}else{
		//alertDiv2("正在调整页面...");
		$("#"+dataId).find("div.win_body_all").find(".content_all").stop().animate({height:'480px'},1000,'swing',function(){
			comments.parent("div.comments_and_publish_div").stop().animate({height:'500px'},1000,'swing');
			comments.stop().animate({height:'453px'},1000,'swing',function(){
				$(obj).removeClass('smaller').addClass('higher').attr('title','矮一点');
				content_all.addClass("resized");
				var w_new=$(window).width();
				if(w_old!=w_new){
					resetSize();//如果出现了滚动条或滚动条从有到无,那么重新设置元素宽高 
				}
			});
		});
	}
}

//详情页的评论数据动态写入
function showDataDetailNewComment(result){
	if(toUser==null){
		var html='<div class="comment">'+
					'<div class="comment_content">'+
						'<p class="float_left">'+
							'<img src="/loadImg?imgPath='+result.data.replyUser.photoUrl+'" class="user_head_img"/>'+
						'</p>'+
						'<div class="float_left comment_reply_content">'+
							'<p><a class="user_name">'+result.data.replyUser.showName+'</a></p>'+
							'<p class="reply_text">'+content+'</p>'+
							'<p class="time_reply_p">'+
								'<span class="float_left italic grey">刚刚</span>'+
								'<a class="float_right reply_a" onclick="prePublishComment(\''+result.data.dataId+'\',\''+result.data.id+'\',this,\''+result.data.replyUser.showName+'\')">' +
									'回复(<span>0</span>)</a>'+
								'<label class="clear"></label>'+
							'</p>'+
						'</div>'+
						'<div class="clear"></div>'+
					'</div>';
		$("#comment_list_"+dataId+" .comments").append(html);
		
		var commentCount=$("#data_comment_count_"+dataId);						
		var count=parseInt(commentCount.html());
		commentCount.html('+1').css({"color":"red","fontWeight":"bold"});
		// 颜色渐变
		commentCount.stop().animate({color:'#ffffff'},3000,'swing',function(){
			commentCount.html(count+1).css("fontWeight","normal");
		});
	}else{
		var replys=toUser.parent().parent().parent();
		var toUserName=$(replys.children(".comment_reply_content").children("p")[0]).children("a.user_name").html();
		var replyText=$(replys.children(".comment_reply_content").children("p")[0]).children("span.grey");
		var isSecond=(replyText==null || replyText.length==0)?false:true;// 是不是二级回复
		var html=null;
		if(replys.attr('class').indexOf('comment_replys')==-1 && !isSecond){
			html='<div class="comment_replys">';
		}else{
			html='';
		}
		html+='<div class="reply">'+
					'<p class="float_left">'+
						'<img src="/loadImg?imgPath='+result.data.replyUser.photoUrl+'" class="user_head_img"/>'+
					'</p>'+
					
					'<div class="float_left comment_reply_content">'+
						'<p>'+
							'<a class="user_name">'+result.data.replyUser.showName+'</a>'+
							'<span class="grey"> 回复  '+toUserName+':</span>'+
						'</p>'+
						'<p class="reply_text">'+
							content+
						'</p>'+
						'<p class="time_reply_p">'+
							'<span class="float_left italic grey">刚刚</span>'+
							'<a class="float_right reply_a" onclick="prePublishComment(\''+result.data.dataId+'\',\''+result.data.id+'\',this,\''+result.data.replyUser.showName+'\')">回复</a>'+
							'<label class="clear"></label>'+
						'</p>'+
					'</div>'+
					
					'<div class="clear"></div>'+
				'</div>';
		if(replys.attr('class').indexOf('comment_replys')==-1 && !isSecond){
			html+='</div>';
		}
		replys.append(html);
		var toUserParent=toUser;
		if(isSecond){
			toUserParent=replys.parent('div.comment_replys').prev().children('div.comment_reply_content')
									.children('p.time_reply_p').children('a.reply_a');
		}
		var count=toUserParent.children("span").html();
		var replycount_span=toUserParent.children("span");
		replycount_span.html('+1').css({"color":"red","fontWeight":"bold","text-shadow":"none"});
		// 颜色渐变
		replycount_span.stop().animate({color:'#0080ff'},3000,'swing',function(){
			replycount_span.html((parseInt(count)+1)).css("fontWeight","normal");
			replycount_span=null;
		});
	}
	initSpitslotReply('0');
	$("#comment_publish_input_"+dataId).val('');
}

