var getDynamicDataLock=false;//函数锁
$(function(){
	
});
//首页加载更多数据
function getMoreHomeData(){
	if(typeof(sonModel) != "undefined" && sonModel==2){
		getMoreNoticeData();
	}else if(typeof(sonModel) != "undefined" && sonModel==1){
		if(isNearBottom($("#more_dynamic_message"))){//接近底部开始加载数据
			getMoreDynamicData();
		}
	}
	
}
//异步获取更多动态
function getMoreDynamicData(){
	debugger;
	if(getDynamicDataLock){
		return false;//如果锁住,则不执行
	}
	getDynamicDataLock=true;
	var nowPage_str=$("#nowPage").html();
	var totalPage_str=$("#totalPage").html();
	var url=null;
	if(nowPage_str=='' && totalPage_str==''){
		//表示该页面还没有异步加载过数据
		url="/moreDynamic?page=2";
	}else if(nowPage_str!='' && totalPage_str!=''){
		var nowPage=parseInt(nowPage_str);
		var totalPage=parseInt(totalPage_str);
		if(nowPage<totalPage) url="/moreDynamic?page="+(nowPage+1);
	}
	if(url==null){
		getDynamicDataLock=false;
		return false;
	}
	$('#more_dynamic_message .text').html('');
	$("#more_dynamic_message .loading").show();
	try{
		$.get(url,function(result){
			alertDiv2(result.msg);
			$("#nowPage").html(result.data.nowPage);
			$("#totalPage").html(result.data.totalPage);
			if(result.isSuccess){
				var dataList=result.data.dataList;
				var html=null;
				for(var i=0;i<dataList.length;i++){
					if(dataList[i].moduleId==7) html=spitslotData2Html(dataList[i].data,null,result.data.me,1);
					$("#more_dynamic_message").before(html);
					html=null;
				}
				resetSize();
				getDynamicDataLock=false;
			}else{
				getDynamicDataLock=false;
			}
			if(result.data.nowPage<result.data.totalPage){
				$('#more_dynamic_message .text').html('点击看看有没有了~');
			}else{
				$('#more_dynamic_message .text').html('没有了~');
			}
			$("#more_dynamic_message .loading").hide();
		});	
	}catch(e){
		alertDiv2('加载数据失败!'+e);
		$("#more_dynamic_message .loading").hide();
		$('#more_dynamic_message .text').html('加载数据失败!');
	}
}