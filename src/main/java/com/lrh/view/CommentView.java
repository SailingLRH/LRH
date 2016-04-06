package com.lrh.view;

import java.util.List;

import com.lrh.model.Comment;
import com.lrh.model.User;
/**
 * 评论视图
 */
public class CommentView extends Comment {
	private static final long serialVersionUID = 1L;

	private List<Comment> replys;		//评论下的回复
    
	private Integer replyCount=0;
    
	private User commentUser;
    
	public List<Comment> getReplys() {
		return replys;
	}

	public void setReplys(List<Comment> replys) {
		this.replys = replys;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public User getCommentUser() {
		return commentUser;
	}

	public void setCommentUser(User commentUser) {
		this.commentUser = commentUser;
	}
	
}