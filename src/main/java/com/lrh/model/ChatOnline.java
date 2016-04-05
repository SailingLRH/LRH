package com.lrh.model;

import java.io.Serializable;

import com.lrh.util.ToolDate;

/**
 * 在线聊天
 *
 * @author Lai Rihai
 */
public class ChatOnline implements Serializable {
    private Long id;

    private Integer fromUserId;			//发起人

    private Integer toUserId;			//接收人

    private String content;				//聊天内容

    private String time;				//时间

    private Boolean isReceive;			//是否已送达 	

    private static final long serialVersionUID = 1L;

    /**
     * 拓展属性:发起人用户名
     */
    private String fromUserName;
    
    /**
     * 拓展属性:接收人用户名
     */
    private String toUserName;
    
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

	public Integer getFromUserId()
	{
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId)
	{
		this.fromUserId = fromUserId;
	}

	public Integer getToUserId()
	{
		return toUserId;
	}

	public void setToUserId(Integer toUserId)
	{
		this.toUserId = toUserId;
	}

	public Boolean getIsReceive()
	{
		return isReceive;
	}

	public void setIsReceive(Boolean isReceive)
	{
		this.isReceive = isReceive;
	}

	public String getFromUserName()
	{
		return fromUserName;
	}

	public void setFromUserName(String fromUserName)
	{
		this.fromUserName = fromUserName;
	}

	public String getToUserName()
	{
		return toUserName;
	}

	public void setToUserName(String toUserName)
	{
		this.toUserName = toUserName;
	}

}