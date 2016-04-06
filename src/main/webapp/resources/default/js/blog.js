var blogUpload=null;
var openWriteBlogWinLock=false;
var loadBlogTypeLock=false;
var blogId=null;
var blogIds=null;
var deleteNum=null;

//显示写博文的窗口
function openWriteBlogWin(showList){
	if(showList!=null && showList==1){
		$(".publish_blog").stop().slideUp(500);
		$(".my_blogs").stop().slideDown(500,function(){
			$("#blog_functions1 .write").html("写新博文");
			openWriteBlogWinLock=false;
		});
	}else{
		if(!openWriteBlogWinLock){
			openWriteBlogWinLock=true;
			$("#blog_functions1 .write").html("waiting...");
			if($(".publish_blog").css('display')=='none'){
				$(".my_blogs").stop().slideUp(500);
				$(".publish_blog").stop().slideDown(500,function(){
					$("#blog_functions1 .write").html("我的博文");
					openWriteBlogWinLock=false;
				});
			}else{
				$(".publish_blog").stop().slideUp(500);
				$(".my_blogs").stop().slideDown(500,function(){
					$("#blog_functions1 .write").html("写新博文");
					openWriteBlogWinLock=false;
				});
			}
		}
	}
}
//刷新列表中的数据 -1:表示查询已删除	0:表示查草稿		1:表示查以发布
function updateBlogList(o){

	
}
//显示下拉选
function loadBlogType(isShow,obj,event){
	if(!loadBlogTypeLock){
		loadBlogTypeLock=true;
		if(isShow!=null && isShow==1){
			$(".search_blog_select_div").stop().slideDown(300,function(){
				loadBlogTypeLock=false;
			});
		}else{
			if(isMouseLeaveOrEnter(event,obj)){
				hideBlogTypeSelect();
			}else{
				loadBlogTypeLock=false;
			}
		}
	}
}
//隐藏下拉选
function hideBlogTypeSelect(){
	$(".search_blog_select_div").stop().slideUp(300,function(){
		loadBlogTypeLock=false;
	});
}
//批量删除
function preDeleteSelectBlog(num){
	var c2_all=$(".dataInfo_table_td").children("p.p_checkbox2").children("input.one_dataInfo");
	var ids="";
  	for(var i=0,j=0;i<c2_all.size();i++){
  		if(c2_all[i].checked){
  			if(j!=0) 
  				ids+=','+$(c2_all[i]).val();
  			else{
  				ids+=$(c2_all[i]).val();
  				j++;
  			}
  		}
  	}
  	
  	if(ids!=null && ids!=""){
  		blogIds=ids;
  		deleteNum=parseInt(num);
  		confirmDiv("确定要删除所选的博文吗?","deleteSelectBlog");
  	}else{
  		alertDiv2("请先选择要删除的数据!");
  	}
}
//批量删除
function deleteSelectBlog(sure){
	slideUpNoticeDiv(1,1);
	if(sure!=null && sure==1 && blogIds!=null){
		$.post("/blog/deleteMany",{ids:blogIds},function(result){
	  		if(result.isSuccess){
	  			var array=blogIds.split(',');
	  			var less=0;
	  			for(var i=0;i<array.length;i++){
	  				if(array[i] != ''){
	  					$(".dataInfo_table_td"+array[i]).hide(300);
	  					less++;
	  				}
	  			}
	  			if(deleteNum==null || (deleteNum-less<1)){
	  				window.location.href="/blog/index";
	  			}
			}
			var total=parseInt($(".page_info .total_count").html())-less;
			$(".page_info .total_count").html(total);
			
			window.setTimeout("alertDiv2('"+result.msg+"')", 1000);
			blogId=null;	
			deleteNum==null
		});
	}else{
		blogId=null;
		deleteNum==null
	}
}
//预删除
function preDeleteBlog(id){
	blogId=id;
	confirmDiv('您确定要删除这条博文吗?','deleteBlog');
}
//删除当前
function deleteBlog(sure){
	slideUpNoticeDiv(1,1);
	if(sure!=null && sure==1 && blogId!=null){
		$.post("/blog/delete",{id:blogId},function(result){
			if(result.isSuccess){
				$("#blog_div_"+blogId).hide(300);
			}
			window.setTimeout("alertDiv2('"+result.msg+"')", 1000);
			blogId=null;
		});
	}else{
		blogId=null;
	}
}
//发表博文
function publishBlog(type0,status0){
	var title=$("#blog_publish_title").val();
	if(title==null || title==""){
		alertDiv2("请先输入标题!");
		return false;
	}
	if(title.length>30){
		alertDiv2("标题不能超过30个字!");
		return false;
	}
	
	var content = editor.html(); 
	if(content==null || content==""){
		alertDiv2("请输入博文内容!");
		return false;
	}
	
	if(content.length>100000){
		alertDiv2("博文内容过长,请先删减部分!");
		return false;
	}
	
	var type=$("#blog_type_select").val();
	var permission=$("#blog_public_select").val();
	
	$.post("/blog/save",{
		title:title,
		content:content,
		type:type,
		publicType:permission,
		status:1
	},function(result){
		alertDiv2(result.msg);
		if(result.isSuccess){
			var typeName=$("#blog_type_select").children("option[selected]").html();
			typeName=typeName=="暂不分类"?"暂未分类":typeName;
			var publicName=$("#blog_public_select").children("option[selected]").html();
			//异步显示到列表
			var table=$(".dataInfo_table");
			if(table==null || table.length<1){
				window.location.href="/blog/index";
			}else if((type0 !=null && type0==type && status0 !=null && status0==1) || ((type0 ==null || type0=="") && status0 !=null && status0==1)
				|| (type0 !=null && type0==type && (status0 ==null || status0=="")) || ((type0 ==null || type0=="") && (status0 ==null || status0==""))){
				var blog=result.data;
				var html='<div class="dataInfo_table_td_new dataInfo_table_td'+blog.id+'" onclick="checkedOrNot(\'dataInfo_table_td'+blog.id+'\')">'+
		    				'<p class="p_checkbox2 col1">'+
		    					'<input type="checkbox" class="one_dataInfo" onclick="checkedOrNot(\'dataInfo_table_td'+blog.id+'\')" value="'+blog.id+'"/>'+
		    				'</p>'+
		    				
		    				'<p class="blogTitle col2">'+
		    					'<a href="/blog/blogDetail/'+blog.id+'" class="link">'+title+'</a></p>'+
		    				'<p class="col3">'+typeName+'</p>'+
		    				'<p class="col4">'+
		    					publicName+
		    				'</p>'+
		    				'<p class="col5">'+blog.lastEditTime+'</p>'+
		    				'<p class="col6">'+
		    					'0/0'+
		    				'</p>'+
		    				'<div class="clear"></div>'+
		    			'</div>';
				
				$(".dataInfo_table_th").after(html);
				var total=parseInt($(".page_info .total_count").html())+1;
				$(".page_info .total_count").html(total);
			}else window.location.href="/blog/index";
			
			openWriteBlogWin(1);
		}
	});
}







//设置每页显示的数据数量
function setLimit(type,status){
	var limit=$("#limitInput").val();
	if(limit==null || limit==''){
		alertDiv2("请先输入数量!");
		$("#limitInput").focus();
	}else{
		var validat=/^\+?[1-9][0-9]*$/;
		if(validat.test(limit)){
			if(parseInt(limit)>100){
				alertDiv2("每页显示的数据不能超过100条!");
				$("#limitInput").val("");
				$("#limitInput").focus();
			}else{
				window.location.href="/blog/list?type="+type+"&status="+status+"&max="+limit;
			}
		}else{
			alertDiv2("请输入一个正整数!");
			$("#limitInput").val("");
			$("#limitInput").focus();
		}
	}
}
//跳转到某页
function gotoPage(type,status,limit,pageNum,nowPage){
	var pageCode=$('#pageCodeInput').val();
	if(pageCode==null || pageCode==''){
		alertDiv2("请先输入要跳转的页码!");
		$('#pageCodeInput').focus();
	}else{
		var validat=/^\+?[1-9][0-9]*$/;
		if(validat.test(pageCode)){
			if(parseInt(pageCode)>pageNum){
				alertDiv2("不要超过总页数!");
				$('#pageCodeInput').val("");
				$('#pageCodeInput').focus();
			}else if(pageCode==nowPage){
				alertDiv2("已经是第"+nowPage+"页,无需跳转!");
				$('#pageCodeInput').val("");
				$('#pageCodeInput').focus();
			}else{
				window.location.href="/blog/list?type="+type+"&status="+status+"&max="+limit+"&pageCode="+pageCode;
			}
		}else{
			alertDiv2("请输入一个正整数!");
			$('#pageCodeInput').val("");
			$('#pageCodeInput').focus();
		}
	}
}

//点击行则选中/取消选中行
function checkedOrNot(className){
	if(className=="p_checkbox1"){
		var c1=$("p.p_checkbox1").children("input.all_dataInfo");
		var c2_all=$(".dataInfo_table_td").children("p.p_checkbox2").children("input.one_dataInfo");
		if(c1[0].checked){
	      	c1[0].checked=false;
	      	for(var i=0;i<c2_all.size();i++){
	      		c2_all[i].checked=false;
	      	}
	      	$(".dataInfo_table_td").css("background-color","#ffffff");
	    }else{
	   		c1[0].checked=true;
	   		for(var i=0;i<c2_all.size();i++){
	      		c2_all[i].checked=true;
	      	}
	      	$(".dataInfo_table_td").css("background-color","#E0E0E0");
	   	}
	}else{
		var c1=$("p.p_checkbox1").children("input.all_dataInfo");
		var c=$("."+className).children("p.p_checkbox2").children("input.one_dataInfo");
		var c2_all=$(".dataInfo_table_td").children("p.p_checkbox2").children("input.one_dataInfo");
		if(c[0].checked){
	      	c[0].checked=false;
	      	$("."+className).css("background-color","#ffffff");
	      	
	      	c1[0].checked=false;
	    }else{
	   		c[0].checked=true;
	   		$("."+className).css("background-color","#E0E0E0");
	   		
	   		var f=true;
	      	for(var i=0;i<c2_all.size();i++){
	      		if(!c2_all[i].checked){
	      			f=false;
	      			break;
	      		}
	      	}
	      	if(f) c1[0].checked=true;
	   	}
	}
}