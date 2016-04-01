package com.lrh.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lrh.model.Spitslot;
import com.lrh.model.Comment;
import com.lrh.model.User;
import com.lrh.model.UserUploadFile;
import com.lrh.service.SpitslotService;
import com.lrh.service.UserService;
import com.lrh.util.ToolContext;
import com.lrh.view.CommentView;


@RequestMapping(value = "/data")
@Controller
public class DataController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(DataController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	SpitslotService spitslotService;
	
	public static final int MODEL_SPITSLOT =ToolContext.getSysPropInt("module_spitslot");
	public static final int MODEL_BLOG =ToolContext.getSysPropInt("module_blog");
	
	//查看详情
	@RequestMapping(value = "/tail")
	public String showIndex(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "model", required = true) int model,
			@RequestParam(value = "dataId", required = true) long dataId){
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			this.outJsonResult(response, false, "对不起,您未登录或已掉线,请登录后查看!", null);
			return null;
		}
		if(model==MODEL_SPITSLOT){
			responseSpitslotData(request,response,dataId,user);
		}else if(model==MODEL_BLOG){
			//response.sendRedirect("/blog/blogDetail/"+dataId);
		}else{
			this.outJsonResult(response, false, "数据有误,查看详情失败!", null);
		}
		return null;
	}

	/**
	 * 查看吐槽数据的详情
	 * @author Sailing_LRH
	 * @since 2015年12月26日
	 * @param request
	 * @param response
	 * @param dataId 数据Id
	 */
	public void responseSpitslotData(HttpServletRequest request, HttpServletResponse response, long dataId,User user) {
		Spitslot spitslot = spitslotService.getSpitslotById(dataId);
		if(spitslot==null){
			this.outJsonResult(response, false, "对不起,数据不存在!可能已经被主人删除,刷新看看!", null);
		}else{
			/**
			 * 拼接html代码
			 */
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"notice_detail_float_div absolute\">");
			sb.append("<div class=\"win_head\">");
			sb.append("<p class=\"win_head_icon\">");
			sb.append("<img src=\"/loadImg?imgPath=").append(spitslot.getUser().getPhotoUrl()).append("\"/>");
			sb.append("</p>");
			sb.append("<p class=\"win_head_title\"><span class=\"red_and_shadow\">").append(spitslot.getUser().getShowName()).append("</span>&nbsp;&nbsp;的吐槽</p>");
			sb.append("<p class=\"win_controll_close_btn win_controll_btn\">");
			sb.append("<a href=\"javaScript:void(0)\" onclick=\"hideTailWin()\" class=\"able\"></a>");
			sb.append("</p>");
			sb.append("</div>");
			sb.append("<div class=\"win_body_all\">");
			
			//内容部分开始 
			sb.append("<div class=\"content_and_imgs content_all\" style=\"width: 490px !important;\" id=\"spitslot_content_").append(dataId).append("\">");
			sb.append("<div class=\"content\">");
			sb.append("<p>").append(spitslot.getContent()).append("</p>");
			sb.append("</div>");
			if(spitslot.getImgList()!=null && spitslot.getImgList().size()>0){
				sb.append("<div class=\"data_imgs\">");
				List<UserUploadFile> imgList= spitslot.getImgList();
				for(int i=0;i<imgList.size();i++){
					UserUploadFile img = spitslot.getImgList().get(i);
					sb.append("<p class=\"needShadow\">");
					sb.append("<img src=\"/loadImg?imgPath=").append(img.getFileUrl()).append("\"/>");
					sb.append("</p>");
				}
				sb.append("<div class=\"clear\"></div>");
				sb.append("</div>");
			}
			sb.append("</div>");
			//评论部分开始
			sb.append("<div class=\"comments_and_publish_div notChange\" id=\"comment_list_").append(dataId).append("\">");
			sb.append("<div class=\"comments\" style=\"height: 473px !important;\">");
			if(spitslot.getComments()!=null && spitslot.getComments().size()>0){
				for(CommentView comment:spitslot.getComments()){
					sb.append("<div class=\"comment\">");
					sb.append("<div class=\"comment_content\">");
					sb.append("<p class=\"float_left\">");
					sb.append("<img src=\"/loadImg?imgPath=").append(comment.getCommentUser().getPhotoUrl()).append("\" class=\"user_head_img\"/>");
					sb.append("</p>");
					sb.append("<div class=\"float_left comment_reply_content \">");
					sb.append("<p><a class=\"user_name\">").append(comment.getCommentUser().getShowName()).append("</a></p>");
					sb.append("<p class=\"reply_text\">").append(comment.getContent()).append("</p>");
					sb.append("<p class=\"time_reply_p\">");
					sb.append("<span class=\"float_left italic grey\">").append(comment.getFormateTime()).append("</span>");
					sb.append("<a class=\"float_right reply_a\" onclick=\"prePublishComment(").append(dataId).append(",").append(comment.getId());
					sb.append(",this,'").append(comment.getCommentUser().getShowName()).append("')\">");
					sb.append("回复(<span>").append(comment.getReplyCount()).append("</span>)");
					sb.append("</a>");
					sb.append("<label class=\"clear\"></label>");
					sb.append("</p>");
					sb.append("</div>");
					sb.append("<div class=\"clear\"></div>");
					sb.append("</div>");
					List<Comment> replys = comment.getReplys();
					if(replys!=null && replys.size()>0){
						sb.append("<div class=\"comment_replys\">");
						for(Comment reply:replys){
							sb.append("<div class=\"reply\">");
							sb.append("<p class=\"float_left\">");
							sb.append("<img src=\"/loadImg?imgPath=").append(reply.getReplyUser().getPhotoUrl()).append("\" class=\"user_head_img\"/>");
							sb.append("</p>");
							sb.append("<div class=\"float_left comment_reply_content\">");
							sb.append("<p>");
							sb.append("<a class=\"user_name\">").append(reply.getReplyUser().getShowName()).append("</a>");
							sb.append("<span class=\"grey\"> 回复  ").append(reply.getToUser().getShowName()).append(":</span>");
							sb.append("</p>");
							sb.append("<p class=\"reply_text\">").append(reply.getContent()).append("</p>");
							sb.append("<p class=\"time_reply_p\">");
							sb.append("<span class=\"float_left italic grey\">").append(reply.getFormateTime()).append("</span>");
							sb.append("<a class=\"float_right reply_a\" onclick=\"prePublishComment(").append(dataId).append(",").append(reply.getId());
							sb.append(",this,'").append(reply.getReplyUser().getShowName()).append("')\">回复</a>");
							sb.append("<label class=\"clear\"></label>");
							sb.append("</p>");
							sb.append("</div>");
							sb.append("<div class=\"clear\"></div>");
							sb.append("</div>");
						}
						if(replys.size()>5){
							sb.append("<p class=\"more_reply_p\">");
							sb.append("<a onclick=\"moreReply(").append(dataId).append(",2,").append(comment.getId()).append(",this,").append(comment.getReplyCount());
							sb.append(")\">----------[查看更多回复]----------</a>");
							sb.append("</p>");
						}
						sb.append("</div>");
					}
					sb.append("</div>");
				}
				if(spitslot.getCommentsCount()>5){
					sb.append("<p class=\"more_comment_p\" id=\"more_comment_p_").append(dataId).append("\">");
					sb.append("<a onclick=\"showMoreComment(").append(dataId).append(",2,").append(spitslot.getCommentsCount());
					sb.append(")\">----------[查看更多评论]----------</a>");
					sb.append("</p>");
				}
			}
			sb.append("</div>");
			sb.append("<div class=\"publish_comment\">");
			sb.append("<p class=\"float_left input\">");
			sb.append("<span id=\"mark_").append(dataId).append("\" class=\"mark\"></span>");
			sb.append("<input id=\"comment_publish_input_").append(dataId).append("\" onkeydown=\"addMark(this);textLengthLimit(this,'',500,1)\" onkeyup=\"addMark(this);textLengthLimit(this,'',500,1)\""
					+ " onblur=\"addMark(this);textLengthLimit(this,'',500,1)\" onfocus=\"createFloatEmojiDiv(this);addMark(this);textLengthLimit(this,'',500,1)\" />");
			sb.append("</p>");
			sb.append("<p class=\"float_right btn\">");
			sb.append("<a onclick=\"publishComment(").append(dataId).append(",").append(MODEL_SPITSLOT).append(")\" id=\"comment_publish_btn_").append(dataId).append("\">发表</a>");
			sb.append("</p>");
			sb.append("<div class=\"clear\"></div>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<div class=\"clear\"></div>");
			sb.append("</div>");
			//按钮部分
			sb.append("<div class=\"win_bottom\">");
			sb.append("<div class=\"data_btns\">");
			if(spitslot.getUserId()==user.getId()){
				sb.append("<p class=\"delete\"><a onclick=\"deleteDataAtNotice(").append("'preDeleteSpitslot2(").append(dataId).append(")')\">删除</a></p>");
			}
			sb.append("<p class=\"praise\"><a onclick=\"addPraise(").append(dataId).append(",").append(MODEL_SPITSLOT).append(")\">点赞(");
			sb.append("<span id=\"data_praise_count_").append(dataId).append("\">").append(spitslot.getPraiseCount()).append("</span>)</a></p>");
			sb.append("<p class=\"comment\">");
			sb.append("<a onclick=\"showOrHideComments(").append(dataId).append(",1)\">评论(");
			sb.append("<span id=\"data_comment_count_").append(dataId).append("\">").append(spitslot.getCommentsCount()).append("</span>)");
			sb.append("</a></p>");
			sb.append("</div>");
			sb.append("<div class=\"publish_time\">").append(spitslot.getAddTime()).append("</div>");
			sb.append("<div class=\"clear\"></div>");
			sb.append("</div>");
			sb.append("</div>");
			
			sb.append("</div>");
			this.outJsonResult(response, true, "", sb.toString());
		}
	}
	
	
}
