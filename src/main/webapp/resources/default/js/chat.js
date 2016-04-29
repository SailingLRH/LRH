$(function(){
	initChatPage();
	var total=$("#user-list").attr("total");
	var totalPage=$("#user-list").attr("totalPage");
	var max=$("#user-list").attr("max");
	initPageInfo('/chat/moreUser',total,totalPage,max,1);
});

//调整页面布局
function initChatPage(){
	var win_w=$("#search-user-box").parent().width();
	var input_p_w=win_w-122;
	var input_w=input_p_w-10;
	$("#search-user-box p.search_input").width(input_p_w+'px');
	$("#search-user-box p.search_input").find("input").width(input_w+'px');
}

//打开聊天窗口
function openChatWin(id,userName,headImg){
	alertDiv2('正在打开聊天窗口...');
}

function initPageInfo(url,total,totalPage,max,currentPage){
	getPageHtml(url,total,max,totalPage,currentPage,"pageInfo","loadMoreUser");
}

/**
 * 异步加载更多用户
 * @param url 包含页码,每页显示数量的url
 * @param isGoto 是否是输入页数后,点击跳转
 * @param isSet 是否是重新设置每页显示数量
 */
function loadMoreUser(url,isGoto,isSet){
	//var url="/chat/moreUser?page=1&max=10"
	var max0=$("#user-list").attr("max");
	if(isSet!=null && isSet==1){
		var max=$("#limitInput").val();
		if(max==null || max==''){
			alertDiv2("请输入每页显示的数量!");
			$("#limitInput").focus();
			return;
		}else if(!(/^\+?[1-9]\d*$/).test(max) || parseInt(max)>100){
			alertDiv2("请输入一个0-100的整数!");
			$("#limitInput").val('');
			$("#limitInput").focus();
			return;
		}
		url='/chat/moreUser?page=1&max='+max;
	}
	
	if(isGoto!=null && isGoto==1){
		var gotoPage=$("#pageCodeInput").val();
		if(gotoPage==null || gotoPage==''){
			alertDiv2("请输入要跳转的页码!");
			$("#pageCodeInput").focus();
			return;
		}else if(!(/^\+?[1-9]\d*$/).test(gotoPage)){
			alertDiv2("请输入一个正整数!");
			$("#pageCodeInput").val('');
			$("#pageCodeInput").focus();
			return;
		}
		url='/chat/moreUser?page='+gotoPage+'&max='+max0;
	}
	
	
	url=url==null||url==''?'/chat/moreUser?page=1&max='+max0:url;
	var me=$("#user-list").attr("me");
	var content= $("#search-content").val();
	var account = "";
	if(content!=null && content!="" && content.indexOf("/男")!=-1){
		account=content.substring(0,content.indexOf("/男"));
		url+="&account="+account+"&sex=1"
	}else if(content!=null && content!="" && content.indexOf("/女")!=-1){
		account=content.substring(0,content.indexOf("/女"));
		url+="&account="+account+"&sex=2"
	}else if(content!=null && content!=""){
		url+="&account="+content
	}
	$.get(url,function(result){
		if(result.isSuccess){
			if(result.data.list.length>0){
				$("#user-list-foreach").html('');
				for(var i=0;i<result.data.list.length;i++){
					var html='';
					var one=result.data.list[i];
					if((me!=null && me==one.id) || me==null || !one.isOnline){
						html+='<ul class="one-user-ul data-ul unable">';
					}else{
						html+='<ul class="one-user-ul data-ul able" title="双击与TA聊天" ondblclick="openChatWin(\''+one.id+'\',\''+one.showName+'\',\''+one.photoUrl+'\')">';
					}
					html+='<li class="float_left col1">'+
						'<img src="/loadImg?imgPath='+one.photoUrl+'"/>'+
						'</li>'+
						'<li class="float_left col2" title="'+one.userName+'">'+one.userName+
						'</li>'+
						'<li class="float_left col3" title="'+(one.nickName == null || one.nickName == ""?"[暂无昵称]":one.nickName)+'">'+(one.nickName == null || one.nickName == ""?"[暂无昵称]":one.nickName)+
						'</li>'+
						'<li class="float_left col4">'+one.sexStr+
						'</li>'+
						
						'<li class="float_left col5" title="'+(one.email == null || one.email == ""?"[暂无邮箱]":one.email)+'">'+(one.email == null || one.email == ""?"[暂无邮箱]":one.email)+
						'</li>'+
						'<li class="float_left col6">';
					if(one.isOnline)
						html+='<span style="color:green !important">在线</span>';
					else
						html+='<span style="color:red !important">离线</span>';
					html+='</li>'+
						'</ul>'+
						'<div class="clear"></div>';
					$("#user-list-foreach").append(html);
				}
				
				initPageInfo('/chat/moreUser',result.data.page.total,result.data.page.totalPage,result.data.page.max,result.data.page.nowPage);
			}else{
				$("#user-list-foreach").html('<p class="no_data">未能找到任何用户...</p>');
				$("#pageInfo").html('');
				
				//alertDiv2('没有搜索到用户,换个词试试');
			}
		}else{
			alertDiv2(result.msg);
		}
	});
}