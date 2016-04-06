var showBigImgTimeOut=null;
// ------------------------------------------对JQUERY的拓展:在textarea光标处插入文本
$(function() {
	(function($) {
		$.fn.extend({
			insertContent : function(myValue, t) {
				var $t = $(this)[0];
				if (document.selection) { // ie
					this.focus();
					var sel = document.selection.createRange();
					sel.text = myValue;
					this.focus();
					sel.moveStart('character', -l);
					var wee = sel.text.length;
					if (arguments.length == 2) {
						var l = $t.value.length;
						sel.moveEnd("character", wee + t);
						t <= 0 ? sel.moveStart("character", wee - 2 * t - myValue.length) : sel.moveStart( "character", wee - t - myValue.length);
						sel.select();
					}
				} else if ($t.selectionStart || $t.selectionStart == '0') {
					var startPos = $t.selectionStart;
					var endPos = $t.selectionEnd;
					var scrollTop = $t.scrollTop;
					$t.value = $t.value.substring(0, startPos)
					+ myValue
					+ $t.value.substring(endPos,$t.value.length);
					this.focus();
					$t.selectionStart = startPos + myValue.length;
					$t.selectionEnd = startPos + myValue.length;
					$t.scrollTop = scrollTop;
					if (arguments.length == 2) {
						$t.setSelectionRange(startPos - t,
						$t.selectionEnd + t);
						this.focus();
					}
				} else {
				this.value += myValue;
				this.focus();
				}
			}
		});
	})(jQuery);
});
//判断当前元素是否是目标元素的子元素
jQuery.fn.isChildOf=function(T){
	return (this.parents(T).length>0);
};
//判断当前元素是否是目标元素的子元素或本身
jQuery.fn.isChildAndSelfOf=function(T){
	return (this.closest(T).length>0);
};

// ------------------cookie操作------------------------
// 保存
function setCookie(name,value){
	var Days = 30;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
// 获取
function getCookie(name){
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg)){
    	var val=unescape(arr[2]);
    	if(val !=null && val !=""){
	    	var last=val.substring(val.length-1,val.length);
	    	if(last=="\"" && val.indexOf("\"")==0){// 一头一尾都是双引号
	    		val=val.substring(1,val.length-1);
	    	}
	    	return val;
    	}else{
    		return null;
    	}
// var regS = new RegExp('\\"','g');//匹配所有引号",然后将其替换成空字符
// return unescape(arr[2]).replace(regS,'');
    }
    else
    	return null;
}
// 删除
function delCookie(name){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
    	document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
// ------------------------------------COOKIES END---------------------
// 数字小于10，则在前面加上0,比如:9-->09
function addZero(n){
	if(n<10){
		n=0+n.toString();
	}
	return n;
}
// 模拟弹窗
function alertDiv(msg){
	if(!hasConfirm){
		$(".notice_div").html('<H4>温馨提示:</H4><p class="msg_content">'+msg+'</p>');
		// var toTop=$(document).scrollTop();
		$(".notice_div").stop().show().animate({top:'0px',opacity:'1'},500,"swing",function(){
			// 3秒后自动收起弹窗
			alertTimeout=window.setTimeout("slideUpNoticeDiv()", 3000);
		});
	}
}
// 模拟弹窗2 有遮罩层也弹出,并且收起时不会把遮罩层收起
function alertDiv2(msg){
	$(".notice_div").html('<H4>温馨提示:</H4><p class="msg_content">'+msg+'</p>');
	$(".notice_div").stop().show().animate({top:'0px',opacity:'1'},500,"swing",function(){
		// 3秒后自动收起弹窗
		alertTimeout=window.setTimeout("slideUpNoticeDiv(0,0)", 3000);
	});
}
// 模拟待确认弹窗
function confirmDiv(msg,functionName){
	$(".notice_div").html('<H4>温馨提示:</H4><p class="msg_content">'+msg+
		'</p><div class="confirm">'
			+'<p class="sure"><a href="javaScript:void(0)" onclick="'+functionName+'(1)">确定</a></p>'
			+'<p class="cancel"><a href="javaScript:void(0)" onclick="slideUpNoticeDiv(1,1)">取消</a></p>'
			+'<div class="clear"></div>'
		+'</div>'
	);
	// 遮罩层出现
	$(".zz_div").css({"top":"0px","z-index":"9999"});
	window.clearTimeout(alertTimeout);// 很重要,没有这行代码弹窗可能自动收起,导致遮罩层无法解除
	hasConfirm=true;// 标记已有confirm弹窗
	// var toTop=$(document).scrollTop();
	$(".notice_div").stop().animate({top:'240px',opacity:'1'},500,"swing");
}
// 收起弹窗
function slideUpNoticeDiv(hasZZ,isCancle){
	$(".notice_div").stop().animate({top:'-100px',opacity:'0'},500,"swing",function(){
		// 将提示内容清空
		$(".notice_div").html("");
		if(hasZZ!=null && hasZZ==1){
			// 获取遮罩层高度
			var h=$(".zz_div").height();
			$(".zz_div").css({"top":-h+"px","z-index":"10010"});
			hasConfirm=false;// 取消标记已有confirm弹窗
		}
	});
	
	// 如果此页是游戏页面则,清空gameAll内容
	if(typeof(gameAll) != "undefined" && isCancle!=null && isCancle=="1"){
		gameAll=null;
	}
}
// 当输入@符号时,出现下拉选
function showSelect(inputDivId){
	var inputDiv=$("#"+inputDivId);
	var inputVal=inputDiv.children("input").val();
	if(inputVal!=null && inputVal!='' && inputVal.indexOf('@')>0){
		inputVal=inputVal.substring(0,(inputVal.indexOf('@')+1));
		var test1=/^[a-zA-Z0-9_]{1,}@$/;
		if(!test1.test(inputVal)) return false;
		var inputVal2="";
		var l=inputVal.length;
		if(inputVal.length<12){
			inputVal2=inputVal;
		}else{
			inputVal2+=inputVal.substring(0,4);
			inputVal2+="...";
			inputVal2+=inputVal.substring(l-5,l+1);
		}
		inputVal2=inputVal2.toLowerCase();
		inputDiv.css("position","relative")
				.append('<ul class="input_ul">'+
		    			'<li><a onclick="setValToInput(\''+inputDivId+'\',\''+inputVal+'qq.com\')" title="'+inputVal+'qq.com">'+inputVal2+'qq.com</a></li>'+
		    			'<li><a onclick="setValToInput(\''+inputDivId+'\',\''+inputVal+'163.com\')" title="'+inputVal+'163.com">'+inputVal2+'163.com</a></li>'+
		    			'<li><a onclick="setValToInput(\''+inputDivId+'\',\''+inputVal+'sina.com\')" title="'+inputVal+'sina.com">'+inputVal2+'sina.com</a></li>'+
		    			'<li><a onclick="setValToInput(\''+inputDivId+'\',\''+inputVal+'outlook.com\')" title="'+inputVal+'outlook.com">'+inputVal2+'outlook.com</a></li>'+
		    			'<li><a onclick="setValToInput(\''+inputDivId+'\',\''+inputVal+'live.com\')" title="'+inputVal+'live.com">'+inputVal2+'live.com</a></li>'+
		    			'<li class="last_li"><a onclick="setValToInput(\''+inputDivId+'\',\''+inputVal+'gmail.com\')" title="'+inputVal+'gmail.com">'+inputVal2+'gmail.com</a></li>'+
		    			'</ul>');
	}else{
		inputDiv.children(".input_ul").remove();
	}
}
// 将下拉选中的邮箱号设置到input
function setValToInput(inputDivId,emailVal){
	$("#"+inputDivId).children(".input_ul").remove();
	$("#"+inputDivId).children("input").val(emailVal);
}
// 移除邮箱号码的下拉选
function clearSelect(inputId){
	$("#"+inputId).parent().children(".input_ul").remove();
}
// 60秒倒计时
function daojishi(){
	return 60-(parseInt((new Date().getTime())/1000)-that_time);
}
// 刷新当前页面
function reload(){
	location.reload();
}
// 出现遮罩层
function showZZ(){
	$(".zz_div").css({"top":"0px","z-index":"9999"});
	hasConfirm=true;// 标记已有confirm弹窗
}
// 隐藏遮罩层
function hideZZ(){
	// 获取遮罩层高度
	var h=$(".zz_div").height();
	$(".zz_div").css({"top":-h+"px","z-index":"10010"});
	hasConfirm=false;// 取消标记已有confirm弹窗
}
// 禁用鼠标滚轮
function scrollFun(e){
	if(hasConfirm){
		e=e||window.event;
		if (e&&e.preventDefault){
			e.preventDefault();
			e.stopPropagation();
		}else{ 
			e.returnvalue=false;  
			return false;     
		}
	}
}
// 鼠标悬停在元素上,元素添加阴影----------------------------------公共函数
function addShadow(e){
	$(e).addClass("addShadow");
}
// 鼠标移开,元素上的阴影消失------------------------------公共函数
function removeShadow(e){
	$(e).removeClass("addShadow");
}
// 字数限制
function textLengthLimit(inputObj,msgObjId,maxLength,needAlert){
	var text=$(inputObj).val();
	text=text.replace(/[\r\n]/g,'<br/>');
	var length=text.length;
	if(length>maxLength){
		text=text.substring(0,maxLength);
		text=text.replace(/(<br\/>)/g, "\r\n");
		$(inputObj).val(text);
		if(typeof(msgObjId)!='undefined' && msgObjId!=null && msgObjId!=''){
			$("#"+msgObjId).removeClass("text_limit_ok").addClass("text_limit_too_long");
			$("#"+msgObjId).html("啊哦~最多只允许输入"+maxLength+"个字符哦![特别提醒:换行算5个字符]");
		}
		if(typeof(needAlert)!='undefined' && needAlert!=null && needAlert==1)
			alertDiv2("啊哦~最多只允许输入"+maxLength+"个字符哦!");
	}else{
		if(typeof(msgObjId)!='undefined' && msgObjId!=null && msgObjId!=''){
			$("#"+msgObjId).removeClass("text_limit_too_long").addClass("text_limit_ok");
			$("#"+msgObjId).html("已输入"+length+"个字符,您还可以继续输入"+(maxLength-length)+"个字符")
		}
	}
}
// 移除水印
function clearMark(obj){
	$(obj).parent().find(".mark").hide();
}
// 添加水印
function addMark(obj){
	var inputVal=$(obj).val();
	if(inputVal==null || inputVal==''){
		$(obj).parent().find(".mark").show();
	}else{
		$(obj).parent().find(".mark").hide();
	}
}
//获取1~x之间的随机整数(x<10)
function getRandom(x){
	var n=Math.floor(Math.random()*10);//0~9
	if(n>0 && n<=x) return n;
	else return getRandom(x);
}
//解决onmousexxx事件冒泡闪烁问题的函数===============================公共函数
function isMouseLeaveOrEnter(e,handler) {
    if (e.type != 'mouseout' && e.type != 'mouseover' && e.type != 'mouseenter') return false;
    var reltg = e.relatedTarget ? e.relatedTarget : e.type == 'mouseout' ? e.toElement : e.fromElement;
    while (reltg && reltg != handler)
        reltg = reltg.parentNode;
    return (reltg != handler);
}
//鼠标点击效果
function mouseIsDown(obj){
	$(obj).addClass('clicked');
}
//鼠标左键弹起
function mouseIsUp(obj){
	$(obj).removeClass('clicked');
}
//添加浮动的loading gif图片
function addFloatLoading(){
	$("body").append('<div class="loading_float_div"></div>');
}
//去除浮动的loading gif图片
function removeFloatLoading(){
	$("body").children('.loading_float_div').remove();
}
//返回顶部
function toTop(){
	$('body,html').animate({scrollTop:0},1000);
}
//判断页面是否接近底部,从而开始悄悄加载数据
function isNearBottom(jqueryObj){
	if(jqueryObj!=null && jqueryObj.length>0){
		if(jqueryObj.offset()!=null){
			var toTop=$(document).scrollTop();//滚动条距离顶部的即时位置
			var offsetTop=jqueryObj.offset().top;//参考元素距离顶部的高度
			var height=document.body.clientHeight;//浏览器工作区高度
			if(offsetTop<=(beginLoadHeight+height+toTop)){
				return true;
			}else{
				return false;
			}
		}
	}else{
		return false;
	}
}
//获取元素的属性[获取到的图片大小是auto,因此暂时不用]
function getELstyle(domObj,styleName){
	if(window.getComputedStyle) {
		return window.getComputedStyle(domObj, null)[styleName];//标准浏览器及IE9+
	}else{
		return domObj.currentStyle[styleName];//IE 6,7,8
	}
}
//添加表情
function addEmojo(emojiIndex,domId,domObj){
	var dest=null;
	if(domObj!=null && $(domObj).length>0) dest=$(domObj);
	else dest=$("#"+domId);
	dest.insertContent('[e:'+emojiIndex+']');
}

//生成一个浮动表情div
function createFloatEmojiDiv(domObj){
	$(domObj).parent().css("position","relative");
	var float_emoji_div = $(domObj).parent().find("#float_emoji_div");
	if(typeof(float_emoji_div)!='undefine' && float_emoji_div!=null && float_emoji_div.length>0){
		//表示在目标文本框或文本域已经存在浮动的表情div,因此不用再创建
		
	}else{
		$("#float_emoji_div").remove();//先将别处的表情清除掉
		var html='<div id="float_emoji_div" class="needShadow">';
		for(var i=1;i<5;i++){
			html+='<div class="float_emoji_div_row float_emoji_div_row_'+i+'">';
			for(var j=1;j<10;j++){
				var emojiIndex=100+(i-1)*9+j;
				html+='<p class="float_left float_emoji_one">' +
						'<a style="background: url(\'/resources/default/img/emoji/'+emojiIndex+'.gif\');" onclick="addEmojo(\''+emojiIndex+'\','+null+','+$(domObj).attr("id")+')"></a>' +
					  '</p>';
			}
			html+='</div>';
		}
		html+='</div>';
		$(domObj).parent().append(html);
	}
}

//移除浮动表情div
function removeFloatEmojiDiv(domObj){
	$(domObj).parent().find("#float_emoji_div").remove();
}

//将[e:xxx]转化为表情<img/>表情
function converEmoji(text){
	for(var e=101;e<137;e++){
		eval("var reg=/\\[e:"+e+"\\]/g;");//拼接正则表达式的方法
		text=text.replace(reg,'<img src="\/resources\/default\/img\/emoji/'+e+'.gif"\/>');
	}
	return text;
}
//点击小图查看大图
function showBigImg(domObj,objIsImg){
	showZZ();
	addFloatLoading();//加载中...
	var w= document.body.clientWidth;
	var h=document.body.clientHeight;
	var html='<a class="close_big_img_btn" onclick="closeBigImg()"></a>' +
			'<div class="big_img_show_div" style="width:'+(w-12)+'px;height:'+(h-12)+'px;">';
	if(objIsImg!=null && objIsImg==1)
		html+='<p><a target="_blank" href="'+$(domObj).find("img").attr("src")+'"><img class="big_img_show" src="'+$(domObj).attr("src")+'"/></a></p></div>';
	else
		html+='<p><a target="_blank" href="'+$(domObj).find("img").attr("src")+'"><img class="big_img_show" src="'+$(domObj).find("img").attr("src")+'"/></a></p></div>';
	$("body").append(html);
	//延迟执行
	showBigImgTimeOut=window.setTimeout("showBigImg2()", 500);
}
//将大图显示出来
function showBigImg2(){
	var w= document.body.clientWidth;
	var h=document.body.clientHeight;
	var img_p=$("div.big_img_show_div p");
	
	var img_temp=$("<img/>");
	img_temp.attr("src", img_p.find("img.big_img_show").attr("src"));
	    img_temp.load(function() {
	    	var iw0 = this.width;   // Note: $(this).width() will not
	    	var ih0 = this.height; // work for in memory images.
	        
	        var iw1=0;
	        var ih1=0;
	        if((iw0+12)>w || (ih0+12)>h){//图片实际宽高 大于 浏览器宽高
		        img_p.find("img.big_img_show").css({'max-height':(h-12)+'px','max-width':(w-12)+'px'});//图片最大不能超出屏幕
	        	if((w/(iw0+12))>(h/(ih0+12))){//以图片当前高度为准,计算当前图片宽度
	        		ih1=h-12;
	        		iw1=iw0*(h-12)/ih0;
	        	}else{//以图片当前宽度为准,计算当前图片高度
	        		iw1=w-12;
	        		ih1=ih0*(w-12)/iw0;
	        	}
		        img_p.css({'margin-top':((h-ih1-12)/2)+'px','margin-left':((w-iw1-12)/2)+'px','height':ih1+'px','width':iw1+'px'}).fadeIn(300);
	        }else{
	        	iw1=(iw0+12)>w?w:(iw0+12);//考虑到边框,加12个像素
		        ih1=(ih0+12)>h?h:(ih0+12);
		        img_p.css({'margin-top':((h-ih1)/2)+'px','margin-left':((w-iw1)/2)+'px','height':(ih1-12)+'px','width':(iw1-12)+'px'}).fadeIn(300);
	        }
	        
	        removeFloatLoading();//去除加载中...
	    });
}
//关闭大图
function closeBigImg(){
	window.clearTimeout(showBigImgTimeOut);
	removeFloatLoading();//去除加载中...
	$("body").children("a.close_big_img_btn").remove();
	$("body").children("div.big_img_show_div").remove();
	hideZZ();
}
//文本框样式改变
function inputStyleChange(domObj){
	$(domObj).parent().parent().children(".h26_input").removeClass("h26_input").addClass("h26_input_hover");
	$(domObj).addClass("input_writting");
}
//文本框样式变回来
function inputStyleChangeBack(domObj){
	$(domObj).parent().parent().children(".h26_input_hover").removeClass("h26_input_hover").addClass("h26_input");
	$(domObj).removeClass("input_writting");
}
