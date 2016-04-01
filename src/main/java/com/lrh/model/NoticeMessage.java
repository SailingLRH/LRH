package com.lrh.model;

import java.io.Serializable;

import com.lrh.util.ToolDate;

public class NoticeMessage implements Serializable {
    private Long id;

    private Integer userId;

    private String title;

    private String content;

    private Short messageType;

    private String time;

    private Boolean isRead;

    private String url;

    private Integer initiatorId;

    private Short model;

    private Long dataId;

    private static final long serialVersionUID = 1L;

    /**
     * 拓展属性:发起人
     */
    private User initiator;
    
    /**
     * 经过处理的时间
     */
    private String formateTime;
    
    /**
     * 分页属性
     */
    private Long start;
    
    private Long max;
    
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Short getMessageType() {
        return messageType;
    }

    public void setMessageType(Short messageType) {
        this.messageType = messageType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Integer initiatorId) {
        this.initiatorId = initiatorId;
    }

    public Short getModel() {
        return model;
    }

    public void setModel(Short model) {
        this.model = model;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public User getInitiator() {
		return initiator;
	}

	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}

	public String getFormateTime() {
		formateTime=ToolDate.formatTime(time);
		return formateTime;
	}

	public void setFormateTime(String formateTime) {
		this.formateTime = formateTime;
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

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", messageType=").append(messageType);
        sb.append(", time=").append(time);
        sb.append(", isRead=").append(isRead);
        sb.append(", url=").append(url);
        sb.append(", initiatorId=").append(initiatorId);
        sb.append(", model=").append(model);
        sb.append(", dataId=").append(dataId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}