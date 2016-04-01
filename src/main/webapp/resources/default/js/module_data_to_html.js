/**
 *用于拼接html的js 
**/
function spitslotData2Html(data,imgUrls,me,isHomeLoad){
	var html='<div class="spitslot_data win" id="spitslot_div_'+data.id+'" onclick="initSpitslotReply(\''+data.id+'\')">'+
	'<div class="win_head">'+
		'<p class="win_head_icon">'+
			'<img src="/loadImg?imgPath='+data.user.photoUrl+'"/>'+
		'<p>';
		if(isHomeLoad!=null && isHomeLoad==1)
			html+='<p class="win_head_title"><span class="red_and_shadow">'+data.user.showName+'</span>&nbsp;&nbsp;发表了一条吐槽</p>';
		else
			html+='<p class="win_head_title"><span class="red_and_shadow">'+data.user.showName+'</span></p>';

		html+='<p class="win_controll_close_btn win_controll_btn">'+
			'<a href="javaScript:void(0)" class="able"></a>'+
		'<p>'+
		'<p class="win_controll_max_btn win_controll_btn">'+
			'<a href="javaScript:void(0)"></a>'+
		'</p>'+
		'<p class="win_controll_min_btn win_controll_btn">'+
			'<a href="javaScript:void(0)" class="able"></a>'+
		'<p>'+
		'<div class="clear"></div>'+
	'</div>'+

	'<div class="win_body">'+
		'<div class="win_body_all">'+
			'<div class="content_and_imgs content_all" id="spitslot_content_'+data.id+'">'+
				'<div class="content">'+
					'<p>'+data.content+'</p>'+
				'</div>';
				if(imgUrls!=null && imgUrls.length>0){
					html+='<div class="data_imgs">';
					for(var j=0;j<imgUrls.length;j++){
						html+='<p class="needShadow">'+
								'<img src="'+imgUrls[j]+'"/>'+
							'</p>';
					}
					html+='<div class="clear"></div>'+
					'</div>';
				}else if(data.imgList!=null && data.imgList.length>0){
					html+='<div class="data_imgs">';
					for(var j=0;j<data.imgList.length;j++){
						html+='<p class="needShadow">'+
								'<img src="/loadImg?imgPath='+data.imgList[j].fileUrl+'"/>'+
							'</p>';
					}
					html+='<div class="clear"></div>';
					'</div>';
				}
			html+='</div>'+
			
			'<div class="comments_and_publish_div" id="comment_list_'+data.id+'" onmouseover="showZoom(event,this)" onmouseout="hideZoom(event,this)">' +
				'<div class="comments">' +
					'<div class="zoom"><a onclick="changeCommentDivHight(this,\'spitslot_div_'+data.id+'\')" class="smaller" title="高一点"></a></div>';
					for(var k=0;data.comments!=null && k<data.comments.length;k++){
							html+='<div class="comment">'+
								'<div class="comment_content">'+
									'<p class="float_left">'+
										'<img src="/loadImg?imgPath='+data.comments[k].commentUser.photoUrl+'" class="user_head_img"/>'+
									'</p>'+
									'<div class="float_left comment_reply_content ">'+
										'<p><a class="user_name">'+data.comments[k].commentUser.showName+'</a></p>'+
										'<p class="reply_text">'+data.comments[k].content+'</p>'+
										'<p class="time_reply_p">'+
											'<span class="float_left italic grey">'+data.comments[k].formateTime+'</span>'+
											'<a class="float_right reply_a" onclick="prePublishComment(\''+data.id+'\',\''+data.comments[k].id+'\',this,\''+data.comments[k].commentUser.showName+'\')">' +
													'回复(<span>'+data.comments[k].replyCount+'</span>)' +
											'</a>'+
											'<label class="clear"></label>'+
										'</p>'+
									'</div>'+
									'<div class="clear"></div>'+
								'</div>';
								var replys=data.comments[k].replys;
								if(replys != null && replys.length>0){
									html+='<div class="comment_replys">';
									var reply=null;
									for(var r=0;r<replys.length;r++){
										reply=replys[r];
										html+='<div class="reply">'+
												'<p class="float_left">'+
													'<img src="/loadImg?imgPath='+reply.replyUser.photoUrl+'" class="user_head_img"/>'+
												'</p>'+
												
												'<div class="float_left comment_reply_content">'+
													'<p>'+
														'<a class="user_name">'+reply.replyUser.showName+'</a>'+
														'<span class="grey"> 回复  '+reply.toUser.showName+':</span>'+
													'</p>'+
													'<p class="reply_text">'+
														reply.content+
													'</p>'+
													'<p class="time_reply_p">'+
														'<span class="float_left italic grey">'+reply.formateTime+'</span>'+
														'<a class="float_right reply_a" onclick="prePublishComment(\''+data.id+'\',\''+reply.id+'\',this,\''+reply.replyUser.showName+'\')">回复</a>'+
														'<label class="clear"></label>'+
													'</p>'+
												'</div>'+
												
												'<div class="clear"></div>'+
											'</div>';
									}
									if(data.comments[k].replyCount>5){
										html+='<p class="more_reply_p">'+
											'<a onclick="moreReply(\''+data.id+'\',2,\''+data.comments[k].id+'\',this,\''+data.comments[k].replyCount+'\')">----------[查看更多回复]----------</a>'+
										'</p>';
									}
									html+='</div>';
								}
							html+='</div>';
						}
					if(data.commentsCount>5){
						html+='<p class="more_comment_p" id="more_comment_p_'+data.id+'">'+
								'<a onclick="showMoreComment(\''+data.id+'\',2,\''+data.commentsCount+'\')">----------[查看更多评论]----------</a>'+
							'</p>';
					}
					html+='</div>'+
						'<div class="publish_comment">'+
							'<p class="float_left input">'+
								'<span id="mark_'+data.id+'" class="mark"></span>'+
								'<input id="comment_publish_input_'+data.id+'" onkeydown="addMark(this);textLengthLimit(this,\'\',500,1)" onkeyup="addMark(this);textLengthLimit(this,\'\',500,1)"' +
										' onblur="addMark(this);textLengthLimit(this,\'\',500,1)" onfocus="createFloatEmojiDiv(this);addMark(this);textLengthLimit(this,\'\',500,1)"/>'+
							'</p>'+
							'<p class="float_right btn">'+
								'<a onclick="publishComment(\''+data.id+'\',7)" id="comment_publish_btn_'+data.id+'">发表</a>'+
							'</p>'+
							'<div class="clear"></div>'+
						'</div>'+
					'</div>'+
					'<div class="clear"></div>'+
				'</div>'+
			'</div>'+

			'<div class="win_bottom">'+
				'<div class="data_btns">';
					if(me!=null && me==data.userId){
						html+='<p class="delete"><a onclick="preDeleteSpitslot('+data.id+')">删除</a></p>';
					}
					html+='<p class="praise"><a onclick="addPraise(\''+data.id+'\',7)">点赞(' +
					'<span id="data_praise_count_'+data.id+'">'+data.praiseCount+'</span>)</a></p>'+
					'<p class="comment">' +
						'<a onclick="showOrHideComments(\''+data.id+'\')">评论(' +
							'<span id="data_comment_count_'+data.id+'">'+data.commentsCount+'</span>)' +
						'</a>' +
					'</p>'+
				'</div>'+
				'<div class="publish_time">'+data.addTime+'</div>'+
				'<div class="clear"></div>'+
			'</div>'+
		'</div>';
	return html;
}