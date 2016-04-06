package com.lrh.model;

import java.io.Serializable;
import java.util.List;

import com.lrh.view.CommentView;

public class Blog implements Serializable {
    private Long id;

    private Integer userId;

    private String title;

    private Short status;

    private Short publicType;				//公开类型: 1.公开 2.对好友公开 3.仅自己可以看 4.指定人可看

    private String type;

    private String addTime;
    
    private String lastEditTime;

    private String publicToUsers;

    private Integer readNumber=0;

    private String content;
    
    private String simpleContent;			//用于首页显示简短信息
    
    private String firstImg;				//博文中的第一张图片(非表情)
    
    private String typeName;
    
    private Integer praiseCount=0;			//点赞数
    
    private Integer commentsCount=0;		//评论数
    
    private List<CommentView> comments;		//关联查询5条评论
    
    private User user;						//用于关联查询
    
    private String startTime;
    
    private String endTime;
    
    /**
     * 分页属性
     */
    private Long start;
    
    private Long max;
    
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getPublicType() {
        return publicType;
    }

    public void setPublicType(Short publicType) {
        this.publicType = publicType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublicToUsers() {
        return publicToUsers;
    }

    public void setPublicToUsers(String publicToUsers) {
        this.publicToUsers = publicToUsers == null ? null : publicToUsers.trim();
    }

    public Integer getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(Integer readNumber) {
        this.readNumber = readNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getLastEditTime() {
		lastEditTime=lastEditTime==null || lastEditTime.equals("")?addTime:lastEditTime;
		return lastEditTime;
	}

	public void setLastEditTime(String lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", status=").append(status);
        sb.append(", publicType=").append(publicType);
        sb.append(", type=").append(type);
        sb.append(", addTime=").append(addTime);
        sb.append(", lastEditTime=").append(lastEditTime);
        sb.append(", publicToUsers=").append(publicToUsers);
        sb.append(", readNumber=").append(readNumber);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public String getTypeName() {
		typeName=typeName==null || typeName.equals("")?"暂未分类":typeName;
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSimpleContent() {
		return simpleContent;
	}

	public void setSimpleContent(String simpleContent) {
		this.simpleContent = simpleContent;
	}

	public String getFirstImg() {
		return firstImg;
	}

	public void setFirstImg(String firstImg) {
		this.firstImg = firstImg;
	}

}