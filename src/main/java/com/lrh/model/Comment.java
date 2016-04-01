package com.lrh.model;

import java.io.Serializable;

import com.lrh.util.ToolDate;

public class Comment implements Serializable {
    private Long id;

    private Integer toUserId;

    private Integer userId;

    private Long pid;

    private Long oid;

    private String content;

    private String time;

    private Long dataId;

    private static final long serialVersionUID = 1L;

    /**
     * 经过处理的时间
     */
    private String formateTime;
    
    //=======================回复者和被回复者========
    private User replyUser;
    
    private User toUser;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public User getReplyUser() {
		return replyUser;
	}

	public void setReplyUser(User replyUser) {
		this.replyUser = replyUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}


	public String getFormateTime() {
		formateTime=ToolDate.formatTime(time);
		return formateTime;
	}

	public void setFormateTime(String formateTime) {
		this.formateTime = formateTime;
	}
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", userId=").append(userId);
        sb.append(", pid=").append(pid);
        sb.append(", oid=").append(oid);
        sb.append(", content=").append(content);
        sb.append(", time=").append(time);
        sb.append(", dataId=").append(dataId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}