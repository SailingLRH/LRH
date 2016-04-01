$(function(){
	//图片上下抖动特效
	$("img.editor_insert_img").each(function(k,obj){
		new JumpObj(obj,10);
	});
	//点击小图查看大图
	$("img.editor_insert_img").click(function(){
	 	showBigImg(this,1);
	});
});
//显示评论文本框
function showOrHideBlogComments(domObj,dataId,isRemoveInput){
	var winBottom=$(".blog_detail .win_body");
	if(winBottom.find(".win_bottom_comment_input").length>0 || (isRemoveInput!=null && isRemoveInput==1)){
		winBottom.find(".win_bottom_comment_input").remove();
	}else{
		var w=winBottom.width()-48;
		var iw=w-10;
		var html='<div class="win_bottom_comment_input needShadow">'+
						'<p class="float_left input" style="width:'+w+'px;">'+
							'<span id="mark_'+dataId+'" class="mark"></span>'+
							'<input style="width:'+iw+'px;" id="comment_publish_input_'+dataId+'" onkeydown="addMark(this);textLengthLimit(this,\'\',500,1)" onkeyup="addMark(this);textLengthLimit(this,\'\',500,1)"'+ 
								'onblur="addMark(this);textLengthLimit(this,\'\',500,1);inputStyleChangeBack(this)" onfocus="createFloatEmojiDiv(this);addMark(this);textLengthLimit(this,\'\',500,1);inputStyleChange(this)"/>'+
						'</p>'+
						'<p class="float_right btn">'+
							'<a onclick="publishComment('+dataId+',9,1)" id="comment_publish_btn_'+dataId+'">发表</a>'+
						'</p>'+
						'<div class="clear"></div>'+
					'</div>';
		winBottom.append(html);
	}
	
}
