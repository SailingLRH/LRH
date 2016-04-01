var updateNoticeDataLock=false;//函数锁
$(function(){
	
});
//显示消息详情div
function showTailWin(url,id){
	showZZ();// 遮罩层出现
	addFloatLoading();
	$.get(url,function(result){
		if(result.isSuccess){
			$("body").append(result.data);
			//调整位置
			var top=$(document).scrollTop();// 滚动条距离顶部的即时位置
			var w= document.body.clientWidth;
			var h=document.body.clientHeight;
			var noticeDiv=$(".notice_detail_float_div");
			var margin_left=0;
			var margin_top=0;
			
			if(w>=800 && h>=580){//浏览器够宽,够高
				margin_left=(w-800)/2;
				margin_top=(h-580)/2+top;
			}else if(w<800 && h>=580){
				margin_left=0;
				margin_top=(h-580)/2+top;
			}else if(w<800 && h<580){
				margin_left=0;
				margin_top=top;
			}else{
				margin_left=(w-800)/2;
				margin_top=top;
			}
			var body=noticeDiv.children(".win_body_all");
			noticeDiv.hide();
			var body_h=body.height();
			noticeDiv.css({"top":((h-60)/2+top)+"px","left":margin_left+'px'});
			body.css({"height":"0px","min-height":"0px"});
			removeFloatLoading();
			noticeDiv.fadeIn(500,function(){
				body.stop().animate({height:body_h+'px'},1000,"swing");
				noticeDiv.stop().animate({top:margin_top+'px'},1000,"swing");
			});
			
			//设为已读
			hasRead(id);
		}else{
			removeFloatLoading();
			hideZZ();
			alertDiv(result.msg);
		}
	});
}
//设为已读
function hasRead(id){
	$.post("/msg/changeStatus",{dataId:id},function(reuslt2){
		if(reuslt2.isSuccess){
			var oneMsg=$("#one_msg_win_"+id);
			oneMsg.removeClass('one_msg_all_new').addClass('one_msg_all_old');
			oneMsg.children(".one_msg_all").removeClass('one_msg_all_new').addClass('one_msg_all_old');
			oneMsg.children("span.new").remove();
		}
	});
}

//关闭消息详情div
function hideTailWin(){
	var noticeDiv=$(".notice_detail_float_div");
	var body=noticeDiv.children(".win_body_all");
	var top=$(document).scrollTop();// 滚动条距离顶部的即时位置
	var h=window.innerHeight;
	body.stop().animate({height:'0px'},1000,"swing");
	noticeDiv.stop().animate({top:((h-60)/2+top)+"px"},1000,"swing",function(){
		noticeDiv.fadeOut(500,function(){
			noticeDiv.remove();
			hideZZ();//收起遮罩层
		});
	});
}
//删除数据
function deleteDataAtNotice(funName){
	var functionName2=eval(funName);//将字符串当做函数去执行
	//添加二级遮罩
	$(".notice_detail_float_div").append('<div class="notice_detail_float_div_zz"></div>');
	$(".notice_div").html('<H4>温馨提示:</H4><p class="msg_content">'+'确定要删除这条数据和所有评论吗?'+
		'</p><div class="confirm">'
			+'<p class="sure"><a href="javaScript:void(0)" onclick="'+functionName2+'(1)">确定</a></p>'
			+'<p class="cancel"><a href="javaScript:void(0)" onclick="removeNoticeZZ()">取消</a></p>'
			+'<div class="clear"></div>'
		+'</div>'
	);
	window.clearTimeout(alertTimeout);// 很重要,没有这行代码弹窗可能自动收起
	$(".notice_div").stop().animate({top:'240px',opacity:'1'},500,"swing");
}
//取消删除
function removeNoticeZZ(){
	$(".notice_div").stop().animate({top:'-100px',opacity:'0'},500,"swing",function(){
		// 将提示内容清空
		$(".notice_div").html("");
		$(".notice_detail_float_div").children(".notice_detail_float_div_zz").remove();
	});
}
//异步刷新数据
function updateNoticeData(isAuto,isRefresh){
	if(updateNoticeDataLock && isAuto!=null && isAuto==1){
		return false;//如果是通过滚动条滚动到底部触发,那么看函数是否锁住
	}
	//如果是手动点击加载,那么...
	var nowPage=$("#more_notice_message #nowPage").html();
	var totalPage=$("#more_notice_message #totalPage").html();
	var page=1;
	var hasMore=false;
	if(nowPage!=null && nowPage!='' && totalPage!=null && totalPage!=''){
		nowPage=parseInt(nowPage);
		totalPage=parseInt(totalPage);
		if(nowPage<totalPage){
			page=nowPage+1;
			hasMore=true;
		}
	}else{//为空,表示还没有异步加载过数据
		hasMore=true;
	}
	if(hasMore || (isRefresh!=null && isRefresh==1)){
		updateNoticeDataLock=true;//锁定
		var goPage=(page==null || page<2)?2:page;
		$('#more_notice_message .text').html('');
		$(".more_data .loading").show();
		
		if(isRefresh!=null && isRefresh==1) goPage=1;
		$.get("/msg/more?page="+goPage,function(result){
			if(page!=null) alertDiv(result.msg);
			$("#more_notice_message #nowPage").html(result.data.nowPage);
			$("#more_notice_message #totalPage").html(result.data.totalPage);
			
			if(result.isSuccess){
				var list=result.data.dataList;
				var html='';
				for(var i=0;i<list.length;i++){
					var notice=list[i];
					if(notice.isRead){
						html+='<div class="win one_msg_win one_msg_all_old" id="one_msg_win_'+notice.id+'">';
					}else{
						html+='<div class="win one_msg_win one_msg_all_new" id="one_msg_win_'+notice.id+'">';
						html+='<span class="new"></span>';
					}
					html+='<p class="user_head_img">';
					if(notice.initiator!=null)
						html+='<img src="/loadImg?imgPath='+notice.initiator.photoUrl+'"/>';
					else
						html+='<img src="/loadImg?imgPath=null"/>';
					html+='</p>';
					if(notice.isRead){
						html+='<div class="one_msg_all one_msg_all_old">';
					}else{
						html+='<div class="one_msg_all one_msg_all_new">';
					}
					
					html+='<h5>'+notice.title+'</h5>';
					html+='<p class="tail">'+notice.content+'</p>';
					html+='<p class="time">'+notice.formateTime+'</p>';
					html+='<p class="url"><a onclick="showTailWin(\''+notice.url+'\',\''+notice.id+'\')">查看详情&gt;&gt;</a></p>';
					html+='</div>';
					html+='</div>';
					$("#more_notice_message").before(html);
					html='';
				}
				updateNoticeDataLock=false;//解锁
				resetSize();
			}else{
				updateNoticeDataLock=false;//解锁
			}
			$(".more_data .loading").hide();
			if(result.data.nowPage>=result.data.totalPage){
				$('#more_notice_message .text').html('没有更多了~');
			}else{
				$('#more_notice_message .text').html('点击看看有没有了~');
			}
		});
	}
}
//页面滚动到底部时触发
function getMoreNoticeData(){
	if(isNearBottom($("#more_notice_message"))){//接近底部开始加载数据
		updateNoticeData(1);
	}
}
//数据删除成功特效
function removeNoticeMsgDetail(){
	$(".notice_detail_float_div").find(".notice_detail_float_div_zz").remove();
	var noticeDiv=$(".notice_detail_float_div");
	var body=noticeDiv.children(".win_body_all");
	var top=$(document).scrollTop();// 滚动条距离顶部的即时位置
	var w=$(window).width();
	var h=window.innerHeight;
	noticeDiv.stop().animate({top:(h/2+top)+"px",left:(w/2)+"px",width:"0px",height:"0px"},1000,"swing",function(){
		noticeDiv.fadeOut(500,function(){
			noticeDiv.remove();
			hideZZ();//收起遮罩层
		});
	});
	$(".one_msg_win").remove();
	toTop();
	updateNoticeData(0,1);
}
