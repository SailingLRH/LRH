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
	
}

function initPageInfo(url,total,totalPage,max,currentPage){
	getPageHtml(url,total,max,totalPage,currentPage,"pageInfo","loadMoreUser");
}

function loadMoreUser(url,isGoto){
	var url="/chat/moreUser?pageNum=1&max=10"
	var content= $("#search-content").val();
	var account = "";
	if(content!=null && content!="" && content.indexOf("/男")!=-1){
		account=content.substring(0,content.indexOf("/男"));
		url+="&account="+account+"&sex=1"
	}else if(content!=null && content!="" && content.indexOf("/女")!=-1){
		account=content.substring(0,content.indexOf("/女"));
		url+="&account="+account+"&sex=2"
	}else{
		url+="&account="+content
	}
	$.get(url,function(result){
		if(result.isSuccess){
			if(result.data.list.length>0){
				var html='';
				initPageInfo(url,result.data.page.total,result.data.page.totalPage,10,result.data.page.nowPage);
			}else{
				$("#user-list-foreach").html('<p class="no_data">未能找到任何用户...</p>');
				$("#pageInfo").html('');
				
				alertDiv2('没有搜索到用户,换个词试试');
			}
		}else{
			alertDiv2(result.msg);
		}
	});
}