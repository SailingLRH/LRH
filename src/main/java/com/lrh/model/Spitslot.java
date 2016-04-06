package com.lrh.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.lrh.view.CommentView;

public class Spitslot extends PageBase implements Serializable {
    private Long id;

    private String content;

    private Integer userId;

    private String addTime;

    private Byte publicType;		//公开类型: 1.公开 2.只对好友公开 3.仅自己可以看
    
    //------------------------------以下字段和数据库字段无关联-----------------------------------
    private Integer praiseCount=0;					//点赞数
    
    private Integer commentsCount=0;				//评论数
    
    private List<CommentView> comments;				//关联查询5条评论
    
    private List<Long> imgIdList;					//用于存放关联图片的id
    
    private List<UserUploadFile> imgList;			//用于关联查询
    
    private User user;								//用于关联查询
    
    private String startTime;
    
    private String endTime;
    
    private Set<String> notIncludeIdSet;			//要过滤的数据Id
    
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime == null ? null : addTime.trim();
    }

    public Byte getPublicType() {
        return publicType;
    }

    public void setPublicType(Byte publicType) {
        this.publicType = publicType;
    }

    public List<Long> getImgIdList() {
		return imgIdList;
	}

	public void setImgIdList(List<Long> imgIdList) {
		this.imgIdList = imgIdList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserUploadFile> getImgList() {
		return imgList;
	}

	public void setImgList(List<UserUploadFile> imgList) {
		this.imgList = imgList;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}


	public Integer getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
	}

	public List<CommentView> getComments() {
		return comments;
	}

	public void setComments(List<CommentView> comments) {
		this.comments = comments;
	}

	
	public Set<String> getNotIncludeIdSet() {
		return notIncludeIdSet;
	}

	public void setNotIncludeIdSet(Set<String> notIncludeIdSet) {
		this.notIncludeIdSet = notIncludeIdSet;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", content=").append(content);
        sb.append(", userId=").append(userId);
        sb.append(", addTime=").append(addTime);
        sb.append(", publicType=").append(publicType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}