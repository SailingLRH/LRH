var spitslotUpload=null;
var count0=0;
var spitslotId=null;
var getMoreIsLock=false;//当页面正在加载数据时,将getMore函数锁住,防止数据还没有加载完又有新的加载

$(function(){
	resetSpitslotSize(0);
	//===================================上传图片================================
	//校验选中的文件
    $('#upload_img').on('change' , function(){
    	var fileUrl=$("#upload_img").val();
    	if(fileUrl!=null && fileUrl!=''){
    		var testUrl=/^[a-zA-Z]:(\\.+)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/;
    		if(!testUrl.test(fileUrl)){
    			$("#edit_photo_form_file").val('');
    			$(".modify_pwd_photo .msg_div").html("对不起,您选择的文件格式不对,请重新选择!");
    			return false;
    		}else{//通过校验
    			$(".add_img_bt").before('<div class="spitslot_upload_img" onmouseenter="addImgZzOfSpitslot(this,event)" onmouseout="removeImgZzOfSpitslot(this,event)">'+
								'<div class="process_zz_div"></div>'+
								'<p class="percent_text"></p>'+
								'<div class="temp_and_new_upload">'+
									'<p class="temp">'+
										'<img src="/resources/default/img/icon/default_photo.png" />'+
									'</p>'+
									'<p class="new_upload_img"></p>'+
								'</div>'+
							'</div>');
				var spitslotImgs=$(".spitslot_upload_img");
				if(spitslotImgs.length>9){
					$(".spitslot_upload_imgs .add_img_bt").hide();
				}
    		}
    	}else{
    		return false;
    	}
    }); 
    //自动上传头像 
    spitslotUpload=$("#upload_img").fileupload({
    	autoUpload: true,  
        url:"spitslot/upload_img",
        progress: function (e, data) {
        	var total=data._progress.total;
        	var done=data._progress.loaded;
        	
        	var percent=(100*done/total).toFixed(2);//百分比保留两位小数
        	var w=100-percent;//总宽100px
        	
        	var process=$(".spitslot_upload_img .process_zz_div");
        	var percent_span=$(".spitslot_upload_img .percent_text");
        	$(process[process.length-1]).stop().animate({width:w+'px'},100);
        	$(percent_span[percent_span.length-1]).html(percent+"%");
        },
        done:function(e,result){
            if(result.result.isSuccess){
        		var percent=$(".spitslot_upload_img .percent_text");
        		var spitslotImgs=$(".spitslot_upload_img");
        		var lastSpitslotImg=$(spitslotImgs[spitslotImgs.length-1]);
        		
            	$(percent[percent.length-1]).html("");
            	lastSpitslotImg.children(".temp_and_new_upload").children(".new_upload_img").html('<img class="new_upload_img_tag" src="/loadImg?imgPath='+result.result.data.fileUrl+'" id="'+result.result.data.fileId+'"/>');
            	lastSpitslotImg.children(".temp_and_new_upload").stop().animate({left:'-100px'},800);
            }else{
            	var process=$(".spitslot_upload_img .process_zz_div");
            	var percent=$(".spitslot_upload_img .percent_text");
            	$(process[process.length-1]).stop().animate({width:'100px'},100);
            	$(percent[percent.length-1]).html("上传失败!");
            	$(".publish_spitslot .msg_div").html(result.result.msg);
            }
        },
        fail: function (e, data) {
        	$(".publish_spitslot .msg_div").html("图片上传失败,请重试!");
        }
    });
});

//点击上传图片按钮
function addNewImg(){
	$('#upload_img').click();
}

//调整页面元素宽高
function resetSpitslotSize(special){
	var textArea_w=0;
	if(special!=null && special==1){
		var w=$(window).width();
		w=w<800?800:w;
		textArea_w=w-282;
		//调整文本域的宽度
		$(".spitslot_content textarea").stop().animate({width:textArea_w+'px'},500,"swing");
	}else{
		var win_w=$(".publish_spitslot").width();
		textArea_w=win_w-142;
		//调整文本域的宽度
		$(".spitslot_content textarea").stop().animate({width:textArea_w+'px'},'fast',"swing");
	}
}

//当鼠标移到上传的图片上时,图片上出现遮罩层,并出现删除按钮
function addImgZzOfSpitslot(obj,e){
	if(isMouseLeaveOrEnter(e,obj)){
		$(obj).children(".process_zz_div").width("100px");
		var html='<a class="remove_img_btn" onclick="removeThisImg(this)"></a>';
		$(obj).append(html);
	}
}
//当鼠标移开图片时,图片上的遮罩层消失,按钮也消失
function removeImgZzOfSpitslot(obj,e){
	if(isMouseLeaveOrEnter(e,obj)){
		var text=$(e).children(".percent_text").html();
		$(obj).children(".remove_img_btn").remove();
		if(text==null || text==''){
			//表示图片已上传完毕,并且没有报错
			$(obj).children(".process_zz_div").width(0);
		}
	}
}

//删除图片
function removeThisImg(obj){
	var imgDiv=$(obj).parent();
	var imgP=imgDiv.children(".temp_and_new_upload").children(".new_upload_img");
	if(imgP!=null){
		var imgUrl=imgP.children("img").attr("src");
		var imgId=imgP.children("img").attr("id");
		if(imgUrl!=null && imgUrl!=''){
			imgUrl=imgUrl.substring(imgUrl.indexOf('=')+1,imgUrl.length);
			$.post("/spitslot/deleteImg",{imgUrl:imgUrl,imgId:imgId},function(result){
				console.log(result.msg);
			});
		}
	}
	
	imgDiv.remove();
	var allImgDiv=$(".spitslot_upload_img");
	if(allImgDiv==null || allImgDiv.length<10){
		$(".add_img_bt").show();
	}
}

//发表吐槽
function publishSpitslot(){
	var text=$(".spitslot_content textarea").val();
	if(text==null || text=="" || text.length<0){
		$(".publish_spitslot .msg_div").html("请先输入内容!");
		$(".spitslot_content textarea").focus();
	}else{
		text=text.replace(/[\r\n]/g,'<br/>');
		if(text.length>500){
			$(".publish_spitslot .msg_div").html("对不起,您输入的文本过长!");
			$(".spitslot_content textarea").focus();
		}else{
			//将表情符全部替换成img标签
			text=converEmoji(text);
			//=====================END
			$.post("/spitslot/publish",{content:text,publicType:1},function(result){
				alertDiv(result.msg);
				if(result.isSuccess){
					var imgs=$(".new_upload_img_tag");
					var imgUrls=new Array();
					for(var i=0;i<imgs.length;i++){
						imgUrls[i]=$(imgs[i]).attr("src");
					}
					
					$(".publish_spitslot .msg_div").html("");
					$(".spitslot_content textarea").val("");
					$(".spitslot_upload_img").remove();
					$("#spitslot_text_length_limit").removeClass("text_limit_too_long").addClass("text_limit_ok").html("您可以输入500个字符");
					//更新页面数据
					
					var html=spitslotData2Html(result.data,imgUrls,result.data.userId);
					
					$(".win_parting_line").after(html);
					
					//调整大小
					resetSize();
				}else{
					$(".publish_spitslot .msg_div").html(result.msg);
				}
			});
		}
	}
}

//更多
function getMore(auto){
	if(thisModel==null || thisModel!=7) return false;
	var allSpitslot=$(".spitslot_data");
	var count=allSpitslot.length;
	//当滚动条滚动到最后一条数据这里时,并且还未开始加载数据,那么开始异步加载数据
	if(!getMoreIsLock && ((count!=count0 && isNearBottom($("#more_spitslot"))) || (auto!=null && !auto)) ){
		//alertDiv('加载数据中...');
		getMoreIsLock=true;//getMore()函数上锁,防止数据还没与加载完毕,页面再次调用getMore函数
		$(".more_data span.text").html('');
		$(".more_data .loading").show();
		//收集页面上已经加载的数据id
		var ids='';
		for(var n=0;n<count;n++){
			var div_id=$($(".spitslot_data")[n]).attr('id');
			if(n!=0){
				ids+=","+div_id.substring(div_id.lastIndexOf('_')+1);
			}else{
				ids+=div_id.substring(div_id.lastIndexOf('_')+1);
			}
		}
		$.post("/spitslot/getMore",{notIncludeIds:ids},function(result){
			if(result.isSuccess){
				alertDiv('悄悄为您加载了'+result.data.list.length+'条数据 ^_^');
				for(var i=0;i<result.data.list.length;i++){
					$("#more_spitslot").before(spitslotData2Html(result.data.list[i],null,result.data.me));
				}
				$(".more_data span.text").html('点击看看有没有了~');
				//调整大小
				resetSize();
			}else{
				//alertDiv('好像就这么多了~');
				$(".more_data span.text").html('没有啦~');
			}
			$(".more_data .loading").hide();
			count0=count;
			getMoreIsLock=false;//getMore()函数解锁,页面可以再次调用getMore()函数
		});
	}
}

//预删除
function preDeleteSpitslot(id){
	spitslotId=id;
	confirmDiv('您确定要删除这条吐槽吗?','deleteSpitslot');
}
function preDeleteSpitslot2(id){
	spitslotId=id;
	return "deleteSpitslot2";
}
function deleteSpitslot(sure){
	slideUpNoticeDiv(1,1);
	if(sure!=null && sure==1 && spitslotId!=null){
		$.post("/spitslot/delete",{id:spitslotId},function(result){
			if(result.isSuccess){
				$("#spitslot_div_"+spitslotId).hide(300);
			}
			window.setTimeout("alertDiv2('"+result.msg+"')", 1000);
			spitslotId=null;
		});
	}else{
		spitslotId=null;
	}
}
//在通知消息那里删除
function deleteSpitslot2(sure){
	slideUpNoticeDiv(1,1);
	if(sure!=null && sure==1 && spitslotId!=null){
		$.post("/spitslot/delete",{id:spitslotId},function(result){
			if(result.isSuccess){
				//回调收起详情弹窗函数
				eval('removeNoticeMsgDetail()');
			}
			window.setTimeout("alertDiv2('"+result.msg+"')", 1000);
			spitslotId=null;
		});
	}else{
		spitslotId=null;
	}
}
